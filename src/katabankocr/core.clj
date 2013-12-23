(ns katabankocr.core
  (:require [clojure.set]))


(def numbers [[" _ "
               "| |"
               "|_|"]

              ["   "
               "  |"
               "  |"]

              [" _ "
               " _|"
               "|_ "]

              [" _ "
               " _|"
               " _|"]

              ["   "
               "|_|"
               "  |"]

              [" _ "
               "|_ "
               " _|"]

              [" _ "
               "|_ "
               "|_|"]

              [" _ "
               "  |"
               "  |"]

              [" _ "
               "|_|"
               "|_|"]

              [" _ "
               "|_|"
               " _|"]])

;create a map of all possibles to actuals
;diff of 1
;binary representation -- 1111 and 1110 => 1110   hamming distance 1 + one directional (and)   there is a small set of valid transforms

; create a reverse lookup map
(def token->number
  "A reverse lookup map for identifying the integer value for a pattern"
  (zipmap (map numbers (range 10))
          (range 10)))

(defn permute
  "Create a set of patterns that can be converted to n by adding a pipe or underscore.
  Replaces a character in digit n with a space."
  [n]
  (disj (set (for [i (range 3)
                   j (range 3)]
               (update-in n [i]
                          #(apply str (assoc (vec %) j \space)))))))

(defn perm-map
  "Build a map of possible patterns to a set of integer values.
  The patterns will only point to the one integer i, and will be merged later."
  [i]
  (reduce (fn [acc n]
            (assoc acc n #{i})) {} (permute (numbers i))))

(def all-potential-meanings
  "Merge pattern->integer maps to get all the possible integers a pattern could be"
  (apply merge-with clojure.set/union (map perm-map (range 10))))


(defn partition-str
  "Like partition but reforms character groups into substrings."
  [n s]
  (map #(apply str %) (partition n s)))

(defn pad
  "Append n spaces to an input string s"
  [n s]
  (apply str s (repeat n \space)))

(defn pad-entry
  "If the line lengths for an entry are not equal, pad them with spaces"
  [entry]
  (let [pad-to (apply max (map count entry))]
    (map #(pad (- pad-to (count %)) %) entry)))

(defn tokenize
  "Split an entry into digits for further processing"
  [entry]
  (apply map vector (map #(partition-str 3 %)
                         (pad-entry entry))))

(defn checksum
  "Returns true for valid account numbers"
  [[& digits]]
  (and (every? number? digits)
       (zero? (mod (reduce + (map * (reverse digits) (iterate inc 1))) 11))))

(defn process-entry
  "Identify all the digits in an entry and return a sequence of numbers"
  [entry]
  (map token->number (tokenize entry)))

(defn possible-accounts
  [entry]
  (let [tokens (tokenize entry)
        base (mapv token->number tokens)
        potentials (map all-potential-meanings tokens)]
    (for [i (range (count tokens))
          p (nth potentials i)]
      (assoc base i p))))

(defn partition-entries
  "Partition 3 line entries from a sequence of four line entries"
  [s]
  (map drop-last (partition 4 s)))

(defn account-str
  [[& digits]]
  (apply str (replace {nil \?} digits)))

(defn render-account
  "Returns a string appropriate for displaying the account number"
  [[& digits]]
  (let [account-str (apply str (replace {nil \?} digits))]
    (str account-str (cond
                      (not (every? number? digits)) " ILL"
                      (not (checksum digits)) " ERR"))))

(defn process-reader
  "Print to stdout the results of processing all the entries from a reader"
  [r]
  (doseq [entry (partition-entries (line-seq r))
          :let [account (process-entry entry)]]
    (if (checksum account)
      (println (render-account account))
      (let [passes (filter checksum (possible-accounts entry))]
        (cond
         (= 1 (count passes)) (println (render-account (first passes)))
         (= 0 (count passes)) (println (render-account account))
         :else (println (account-str account) "AMB [" (clojure.string/join ", " (map account-str passes)) "]"))))))

(defn process-file
  "Read a file and display all the results"
  [filename]
  (with-open [r (clojure.java.io/reader filename)]
    (process-reader r)))

(defn process-stdin
  "Read from stdin and display all the results"
  []
  (process-reader (java.io.BufferedReader. *in*)))

