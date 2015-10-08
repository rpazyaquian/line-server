;; usage:
;; `lein repl` -> (load-file "script/create-file.clj") (load-file "script/create-file.clj")

(require '[clojure.string :refer [join]])

(defn join-by-newline
  "Join sequence of strings by newline. Returns one long string."
  [string-lines]
  (join "\n" string-lines))

(defn generate-lines
  "Generate number-of-lines lines of template-string with counter."
  [number-of-lines template-string]
  (let [line-numbers (range 1 (+ 1 number-of-lines))
        lines (repeat number-of-lines template-string)
        lines-with-line-numbers (map vector lines line-numbers)]
    (map #(join " Line #: " %) lines-with-line-numbers)))

(defn write-to-file
  "Write text to file."
  [file-name text]
  (spit file-name text))

(defn create-file
  "Write number-of-lines lines of template-string (plus a counter) to file-name."
  [number-of-lines template-string file-name]
  (let [string-lines (generate-lines number-of-lines template-string)
        text (join-by-newline string-lines)]
    (write-to-file file-name text)))

(create-file 50 "All work and no play makes Jack a dull boy." "resources/public/50.txt")
(create-file 5000 "All work and no play makes Jack a dull boy." "resources/public/5000.txt")
(create-file 50000 "All work and no play makes Jack a dull boy." "resources/public/50000.txt")
