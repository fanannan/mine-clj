(defproject mine-clj "0.1.1-SNAPSHOT"
  :description "mine-clj: a simple clojure wrapper for MINE.jar"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :resource-paths ["./lib/MINE.jar"]
  :injections [(when-not (.exists (clojure.java.io/file "./lib/MINE.jar"))
                (throw (Exception. "\nMINE.jar is not found.\nDownload it from http://www.exploredata.net/ftp/MINE.jar\n")))]
  ;:jvm-opts ["-Xms256M", "-Xmx4096M"]
  :main mine-clj.core)
