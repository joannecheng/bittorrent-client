(ns bittorrent-client.core-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :as io]
            [clojure.tools.reader.reader-types :as rt]
            [bittorrent-client.core :refer :all]))


(deftest decode-map-test
  (testing "decode"
    (let [testcase (->> (.getBytes "3:cow3:moo4:spam4:eggse")
                        io/input-stream
                        rt/input-stream-reader)]
    (is (= (bittorrent-client.core/decode-map testcase) {"cow" "moo", "spam" "eggs"})))))

(deftest decode-number-test
  (testing "decode number"
    (let [testcase (->> (.getBytes "8:")
                        io/input-stream
                        rt/input-stream-reader)]
      (is (= (bittorrent-client.core/decode-number testcase ":") 8)))))
