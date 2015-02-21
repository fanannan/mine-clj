(ns mine-clj.test
  (:require [clojure.test :as ct]
            [mine-clj.core :as mc]))

(defn almost= [xm ym & [raise epsiron]]
  (let [eps (if epsiron epsiron 0.0000001)
        raise-flag (if (nil? raise) true false)]
    (println xm)
    (println ym)
    (if (not= (keys xm) (keys ym))
      false
      (every? (fn[v]
                (if (< (Math/abs (- (v xm) (v ym))) eps)
                  true
                  (if raise-flag
                    (throw (Exception. (str "var: " v " " (v xm) "<>" (v ym))))
                    false)))
              (keys xm)))))

(ct/deftest almost=-test
  (ct/is (true?  (almost= {:a 1, :b 2.0} {:a 1, :b 2.0})))
  (ct/is (false? (almost= {:a 1, :b 2.00001} {:a 1, :b 2})))
  (ct/is (true?  (almost= {:a 1, :b 2.00001} {:a 1, :b 2} :exception 0.1)))
  (ct/is (false? (almost= {:a 1, :b 2.00001} {:a 1, :b 2, :c 3})))
  (ct/is (false? (almost= {:a 1, :b 2.1} {:a 1, :b 2.00}))))

(ct/deftest mine-test
  (let [xs  (range 1 250)
        ys1 (map (fn[x] (* x 0.8)) xs)
        ys2 (map (fn[x] (Math/sin (* x 0.01))) xs)
        ys3 (map (fn[x] (+ x (rand 0.25))) ys2)
        ys4 (take (count xs) (repeatedly #(rand 1.0)))]
    (ct/is (true? (almost=
                   (mc/mine (vec xs) (vec ys1))
                   {:MIC 1.0, :MAS 0.0, :MEV 0.99998, :MCN 3.169925, :pearson 1.0, :non-linearity 0.0})))
    (ct/is (true? (almost=
                   (mc/mine (vec xs) (vec ys2) 0.6 15 :brief)
                   {:MIC 1.0, :MAS 0.30589998, :MEV 0.99998, :MCN 3.9068906, :pearson 0.70124334, :non-linearity 0.5082577767465146})))
    (ct/is (true? (almost=
                   (mc/mine (vec xs) (vec ys2) 0.8 20 :full)
                   {:single-to-noise 1.0, :fisher 0.27717713, :spearman 0.59320486, :MIC 1.0, :MAS 0.30589998, :MEV 0.99998, :MCN 3.9068906, :pearson 0.70124334, :non-linearity 0.5082577767465146})))
    (ct/is (true? (almost=
                   (mc/mine (vec xs) (vec ys3) 0.8 20 :full)
                   {:single-to-noise 0.9987927, :fisher 0.18520129, :spearman 0.6076556, :MIC 0.99998, :MAS 0.22435004, :MEV 0.99998, :MCN 6.129283, :pearson 0.6977188, :non-linearity 0.513168450201956})))
    (ct/is (true? (almost=
                   (mc/mine (vec xs) (vec ys4))
                   {:MIC 0.17719, :MAS 0.036560003, :MEV 0.17719, :MCN 4.70044, :pearson 0.011043429, :non-linearity 0.17706804844691248})))))
