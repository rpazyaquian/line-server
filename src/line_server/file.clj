(ns line-server.file
  (:require [clojure.string :refer [split]]))

(defn get-file-lines
  [file]
  (split (slurp file) #"\n+"))

(defn get-line [files-lines file-name line-number]
  (let [file-lines (get files-lines file-name)
        file-line (nth file-lines (- (bigdec line-number) 1) :error)]
    file-line))

(defn make-file-lines [file-name-and-path]
  (let [[file-name file-path] file-name-and-path
        file-lines (get-file-lines file-path)]
    (hash-map file-name file-lines)))

(defn make-files-lines [file-names-and-paths]
  (apply merge (map make-file-lines (seq file-names-and-paths))))
