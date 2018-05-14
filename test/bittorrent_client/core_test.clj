(ns bittorrent-client.core-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [bittorrent-client.core :refer :all]))


(deftest decode-map-test
  (testing "decode map"
    (let [testcase (io/input-stream (.getBytes "3:cow3:moo4:spam4:eggse"))]
    (is (= (bittorrent-client.core/decode-map testcase) {"cow" "moo", "spam" "eggs"})))))

(deftest decode-number-test
  (testing "decode number"
    (let [testcase (io/input-stream (.getBytes "8:"))]
      (is (= (bittorrent-client.core/decode-number testcase ":") 8)))))
