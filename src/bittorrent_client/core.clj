(ns bittorrent-client.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.tools.reader.reader-types :as rt]
            [clojure.string :as str]))

;; Bencoder
(defn decode-number [stream delimiter & [result]]
  (let [c (str (rt/read-char stream))]
    (if (= c delimiter)
      (read-string (str result))
      (decode-number stream delimiter (str result c)))))

(defn decode-string [stream string-length & result]
  (let [c (rt/read-char stream)
        new-result (str result c)]
    (if (= (count new-result) string-length)
      new-result
      (recur stream string-length new-result))))

(defn decode-list [stream]
  (loop [result []]
    (let [c (rt/peek-char stream)]
      (if (= c \e)
        result
        (recur (conj result (decode stream)))))))

(defn decode-map [stream]
  (apply assoc {} (decode-list stream)))

;; The main decode function
(defn decode [stream & i]
  (let [indicator (if (nil? i) (rt/read-char stream) (first i))]
    (case indicator
      \d (decode-map stream)
      \l (decode-list stream)
      \i (decode-number stream "e")
      (decode-string stream (decode-number stream ":" indicator))
    )))


(def metainfo-stream
  (-> "video.torrent"
      io/resource
      io/file
      io/input-stream
      rt/input-stream-reader))

;; Main
(defn -main
  "this will kick off the whole process"
  [x] (println x "..."))

