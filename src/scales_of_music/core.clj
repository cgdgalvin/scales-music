(ns scales-of-music.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [clojure.string :as str]
            [scales-of-music.midi :as midi]))

(defn modes []
  (json/read-str (slurp "resources/mode-families.json")
                 :key-fn keyword))

(def notes
  ["A", "A#/Bb" , "B" , "C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab"])

(def distances
  {:h 1 :w 2 :wh 3 :ww 5 :wwh 6 :www 7})

(defn rotate 
  "Rotate Vector, N times"
  [v n]
  (into [] (take (count v) (nthrest (cycle v) n))))

(defn list-families []
  (->> (modes) (map :name)))

(defn list-modes
  [family-name]
  (->> (modes)
       (some #(when (= family-name (% :name)) %))
       :modes
       (map :name)))

(defn list-all-modes[]
  (map list-modes (list-families)))

(defn name->family
  [family-name]
  (some #(when (= family-name (% :name)) %) (modes)))

(defn mode->family-name
  [mode-name]
  (:name (some 
          #(when #(some #{mode-name} (list-modes (:name %))) %) 
          (modes))))

(defn count-up
  [collection value]
  (conj collection (+ (or (last collection) 0) value)))

(defn tones->indexes
  [tones]
  (concat [0] (reduce count-up [] tones)))

(defn get-mode 
  [mode family]
  (some #(when (= mode (% :name)) %) (:modes family)))

(defn calc-scale
  [root-note family mode-name]
  (let [tones         (mapv distances (map keyword (:degrees family)))
        mode-position (- (:position (get-mode mode-name family)) 1)
        rotated-notes (rotate notes (.indexOf notes root-note))
        indexes       (tones->indexes (rotate tones mode-position))]
    (mapv #(get rotated-notes (mod % 12)) indexes)))

(defn get-scale
  ([root-note mode-name]
   (get-scale root-note (mode->family-name mode-name) mode-name))
  ([root-note family-name mode-name]
   (let [family (name->family family-name)
         scale (calc-scale root-note family mode-name)]
     (println (str family-name " Family"))
     (println (str root-note " " mode-name " Scale"))
     (println scale)
     scale)))

(defn -main
  [& args]
  (list-all-modes))

(comment
  (list-families)
  (list-modes "Major")
  (get-scale "D" "Major" "Dorian")
  (get-scale "C" "Lydian Aug Sharp 2")
  (list-all-modes)
  (midi/play-scale (get-scale "C" "Lydian Aug Sharp 2")))
