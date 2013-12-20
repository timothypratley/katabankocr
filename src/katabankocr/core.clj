(ns katabankocr.core)

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

; create a reverse lookup map
(def token->number (zipmap (map numbers (range 10))
                           (range 10)))

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

(defn partition-entries
  "Partition 3 line entries from a sequence of four line entries"
  [s]
  (map drop-last (partition 4 s)))

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
    (println (render-account account))))

(defn process-file
  "Read a file and display all the results"
  [filename]
  (with-open [r (clojure.java.io/reader filename)]
    (process-reader r)))

(defn process-stdin
  "Read from stdin and display all the results"
  []
  (process-reader (java.io.BufferedReader. *in*)))

