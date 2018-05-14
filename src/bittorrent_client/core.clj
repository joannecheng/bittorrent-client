(ns bittorrent-client.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(defn -main
  "this will kick off the whole process"
  [x] (println x "..."))


;; Bencoder
(defn decode-number [stream delimiter & result]
  (let [c (char (.read stream))]
    (if (= (str c) delimiter)
      (read-string (str result))
      (recur stream delimiter (str result c)))))

(defn decode-string [stream string-length & result]
  (let [buffer (make-array Byte/TYPE string-length)]
    (.read stream buffer)
    (String. buffer)))

(defn decode-map [stream & result]
  (loop [result []]
    (.mark stream 0)
    (let [c (char (.read stream))]
      (.reset stream)
      (if (= c \e)
        (apply assoc {} result)
        (recur (conj result
               (decode-string stream (decode-number stream ":"))))))))


(defn decode [stream]
  (let [indicator (.read stream)]
    (case indicator
      \d (decode-map (rest stream)))
    ))


(def metainfo-stream
  (-> "video.torrent"
      (io/resource)
      (io/file)
      (io/input-stream)
      ))

(decode (seq metainfo-stream))
;; Parsing metainfo file

