(ns scales-of-music.core-test
  (:require [clojure.test :refer :all]
            [scales-of-music.core :as sut]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(deftest major-scale-returns-correctly
  (testing "C Major"
    (is '("C" "D" "E" "F" "G" "A" "B") (sut/calc-scale "C" "Ionian"))))
