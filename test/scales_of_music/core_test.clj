(ns scales-of-music.core-test
  (:require [clojure.test :refer :all]
            [scales-of-music.core :as sut]))

(deftest major-scale-returns-correctly
  (testing "C Major"
    (is '("C" "D" "E" "F" "G" "A" "B") (sut/get-scale "C" "Ionian"))))
