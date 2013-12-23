(ns katabankocr.scratch
  (:require [katabankocr.core :refer :all]))

(+ 1 2)

(def all-tokens  [" _     _  _     _  _  _  _  _ "
                  "| |  | _| _||_||_ |_   ||_||_|"
                  "|_|  ||_  _|  | _||_|  ||_| _|"])

(map #(apply str %) (partition 3 "123456789"))
(partition-str 3 "123456789")

(tokenize all-tokens)

(= (numbers 0)
   (first (tokenize all-tokens)))

(every? true? (map = (tokenize all-tokens) (map numbers (range 10))))

(zipmap (map numbers (range 10))
        (range 10))

(process-entry all-tokens)

(def multi [" _     _  _     _  _  _  _  _ "
            "| |  | _| _||_||_ |_   ||_||_|"
            "|_|  ||_  _|  | _||_|  ||_| _|"
            "                              "
            " _     _  _     _  _  _  _  _ "
            "| |  | _| _||_||_ |_   ||_||_|"
            "|_|  ||_  _|  | _||_|  ||_| _|"
            "                              "
            " _     _  _     _  _  _  _  _ "
            "| |  | _| _||_||_ |_   ||_||_|"
            "|_|  ||_  _|  | _||_|  ||_| _|"
            "                              "])


(map process-entry (partition-entries multi))

(def multi2 [""
             "  |  |  |  |  |  |  |  |  |"
             "  |  |  |  |  |  |  |  |  |"
             ""])

(map process-entry (partition-entries multi2))

(process-file "test.data")

(with-open [r (clojure.java.io/reader "test.data")]
  (doseq [e (take 3 (partition 4 (line-seq r)))]
    (clojure.pprint/pprint (tokenize e))))

(with-open [r (clojure.java.io/reader "test.data")]
  (doseq [e (partition-entries (line-seq r))]
    (println (process-entry e))))


(numbers 0)

(mapv vec (numbers 0))

(defn perms
  [n]
  (disj
                 (set (for [i (range 3)
                            j (range 3)]
                        (assoc-in (mapv vec (numbers n)) [i j] \space)))
            (numbers n)))

(perms 2)



(doseq [p perms]
  (println (clojure.string/join \newline
                                (map #(apply str %) p))))

