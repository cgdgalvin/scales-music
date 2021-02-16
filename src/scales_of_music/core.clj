(ns scales-of-music.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [clojure.string :as str]))

(defn read-file
  [path]
  (json/read-str (slurp path)
                 :key-fn keyword))

(def notes
  ["A", "A#/Bb" , "B" , "C", "C#/Db", "D", "D#/Eb", "E", "F", "F#/Gb", "G", "G#/Ab"])

(defn rotate [v n]
  (let [cv (count v), n (mod n cv)]
    (vec (concat (subvec v n cv) (subvec v 0 n)))))

(def distances
  {:h 1 :w 2 :wh 3})

(defn modes
  []
  (read-file  (str "resources/mode-families.json")))

(defn list-families
  []
  (->>
    (modes)
    (map :name)))

(defn list-modes
  [family-name]
  (->>
    (modes)
    (filter #(= family-name (% :name)))
    first
    :modes 
   (map :name)))

(defn list-all-modes
  []
  (map list-modes (list-families)))

(defn get-family
  [family-name]
  (first (filter #(= family-name (% :name)) (modes))))

(defn mode->family-name
  "create a map of family - modes, then do a search"
  [mode-name]
  (:name (first (filter #(some #{mode-name} (list-modes (:name %)) ) (modes)))))

(defn count-up
  [collection value]
  (conj collection (+ (or (last collection) 0) value)))

(defn tones->indexes
  [tones]
  (concat [0] (reduce count-up [] tones)))

(defn get-mode 
  [mode family]
  (first (filter #(= mode (% :name)) (:modes family))))

(defn calc-scale
  [root-note family mode-name]
  (let [tones         (mapv distances (map keyword (:degrees family)))
        mode-position (- (:position (get-mode mode-name family)) 1)
        rotated-notes (rotate notes (.indexOf notes root-note))
        indexes       (tones->indexes (rotate tones mode-position))]
    (map #(get rotated-notes (mod % 12)) indexes)))

(defn get-scale
  ([root-note mode-name]
   (get-scale root-note (mode->family-name mode-name) mode-name))
  ([root-note family-name mode-name]
   (let [family (get-family family-name)
         scale (calc-scale root-note family mode-name)]
     (println (str family-name " Family"))
     (println (str root-note " " mode-name " Scale"))
     (println scale))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(comment
  (list-families)
  (list-modes "Major")
  (get-scale "D" "Major" "Dorian")

  (sequence (comp (filter (comp odd? :id))
                  (filter (comp #{"Dow1" "Dow2" "Dow3"} :last-name)))
            data))
