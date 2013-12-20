(ns katabankocr.core-test
  (:require [midje.sweet :refer :all]
            [katabankocr.core :refer :all]))


(def all-tokens  [" _     _  _     _  _  _  _  _ "
                  "| |  | _| _||_||_ |_   ||_||_|"
                  "|_|  ||_  _|  | _||_|  ||_| _|"])

(def lines [" _     _  _     _  _  _  _  _ "
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

(def bad-lines [""
                "  |  |  |  |  |  |  |  |  |"
                "  |  |  |  |  |  |  |  |  |"
                ""])


(facts "about Kata bank OCR"
       (fact (partition-str 3 "123456789") => ["123" "456" "789"])

       (fact (first (tokenize all-tokens)) => (numbers 0))

       (fact "all digits match between all-tokens and numbers"
             (every? true? (map = (tokenize all-tokens) (map numbers (range 10)))) => true)

       (fact "ocr correctly matches every digit"
             (process-entry all-tokens) => (range 10))

       (fact (pad 5 "x") => "x     ")

       (fact (checksum [3 4 5 8 8 2 8 6 5]) => true)

       (fact (checksum [4 4 5 8 8 2 8 6 5]) => false)

       (fact (checksum [0 0 0 0 0 0 0 0 0]) => true)

       (fact (checksum [0 nil 0 0 0 0 0 0 0]) => false)

       (fact (render-account [4 5 7 5 0 8 0 0 0]) => "457508000")

       (fact (render-account [6 6 4 3 7 1 4 9 5]) => "664371495 ERR")

       (fact (render-account [8 6 1 1 0 nil nil 3 6]) => "86110??36 ILL")

       (fact (map process-entry (partition-entries lines)) => (repeat 3 (range 10)))

       (fact (map process-entry (partition-entries bad-lines)) => [[1 1 1 1 1 1 1 1 1]]))

