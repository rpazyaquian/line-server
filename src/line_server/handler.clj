(ns line-server.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [line-server.file :as f]))

(defn handle-get-line [files-lines file-name line-number]
  (let [file-line (f/get-line files-lines file-name line-number)]
    (if (= file-line :error)
      {:status 413}
      {:status 200
       :headers {"Content-Type" "text/html; charset=utf-8"}
       :body file-line})))

(def file-names-and-paths {"50" "resources/public/50.txt"
                           "5000" "resources/public/5000.txt"
                           "50000" "resources/public/50000.txt"})

(def files-lines (f/make-files-lines file-names-and-paths))

(defroutes app-routes
  (GET "/:file-name/lines/:line-number" [file-name line-number]
       (handle-get-line files-lines file-name line-number))
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
