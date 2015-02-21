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
