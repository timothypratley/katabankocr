(ns katabankocr.main
  (:require [katabankocr.core :refer :all]))


(defn -main [& args]
  (if (seq args)
    (process-file (first args))
    (process-stdin)))
