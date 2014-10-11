(ns mine-clj.core
  (:import ;http://www.exploredata.net/ftp/MINE.jar
           [data VarPairData]
           [mine.core MineParameters]
           [analysis Analysis]
           [analysis.results BriefResult FullResult]))

(defn mine
  ([num-vec1 num-vec2]
     ; http://www.exploredata.net/Usage-instructions/Parameters
     (mine num-vec1 num-vec2 0.6 15))
  ([num-vec1 num-vec2 exp-alpha num-clumps-factor]
     (mine num-vec1 num-vec2 exp-alpha num-clumps-factor :brief))
  ([num-vec1 num-vec2 exp-alpha num-clumps-factor result-type]
   (let [dataset (VarPairData. (float-array num-vec1)(float-array num-vec2))
         parameters (MineParameters. exp-alpha num-clumps-factor 0 nil)
         is_brief (= result-type :brief)
         result (Analysis/getResult (if is_brief BriefResult FullResult)
                                    dataset parameters)
         MIC (.getMIC result)
         MAS (.getMAS result)
         MEV (.getMEV result)
         MCN (.getMCN result)
         pearson (.getPearson result)
         non-linearity (- MIC (* pearson pearson))
         ]
     (conj {:MIC MIC,
            :MAS MAS,
            :MEV MEV,
            :MCN MCN,
            :pearson pearson,
            :non-linearity non-linearity}
           (when-not is_brief
             {:spearman (.getSpearman result),
              :fisher (.getFisher result),
              :single-to-noise (.getSignalToNoise result)})))))


(defn -main []
  (let [xs  (range 1 250)
        ys1 (map (fn[x] (* x 0.8)) xs)
        ys2 (map (fn[x] (Math/sin (* x 0.01))) xs)
        ys3 (map (fn[x] (+ x (rand 0.25))) ys2)
        ys4 (take (count xs) (repeatedly #(rand 1.0)))]
    (println (mine (vec xs) (vec ys1)))
    (println (mine (vec xs) (vec ys2) 0.6 15 :brief))
    (println (mine (vec xs) (vec ys2) 0.8 20 :full))
    (println (mine (vec xs) (vec ys3) 0.8 20 :full))
    (println (mine (vec xs) (vec ys4)))
  ))
