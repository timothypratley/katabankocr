(defproject katabankocr "0.1.0-SNAPSHOT"
  :description "Kata Bank OCR http://codingdojo.org/cgi-bin/wiki.pl?KataBankOCR"
  :url "http://github.com/timothypratley/katabankocr"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main katabankocr.main
  :profiles {:dev {:dependencies [[midje "1.5.1"]]}}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :plugins [[lein-midje "3.0.0"]
            [lein-ancient "0.5.3"]
            [lein-kibit "0.0.8"]])
