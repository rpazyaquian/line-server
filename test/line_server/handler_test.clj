(ns line-server.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [line-server.handler :refer :all]))

(deftest test-app
  (testing "1st line of 50"
    (let [response (app (mock/request :get "/50/lines/1"))]
      (is (= (:status response) 200))
      (is (= (:body response) "All work and no play makes Jack a dull boy. Line #: 1"))))

  (testing "51st line of 50"
    (let [response (app (mock/request :get "/50/lines/51"))]
      (is (= (:status response) 413)))))
