(ns scales-of-music.midi)
(import '(javax.sound.midi MidiSystem Synthesizer))

(defn play-note [synth channel note-map]
  (let [{:keys [note velocity duration]
         :or {note 60
              velocity 127
              duration 1000}} note-map]
    (. channel noteOn note velocity)
    (Thread/sleep duration)
    (. channel noteOff note)))

(defn play-notes
  [notes]
  (with-open [synth (doto (MidiSystem/getSynthesizer) .open)]
    (let [channel (aget (.getChannels synth) 0)]
      (doseq [note notes]
        (play-note synth channel note)))))

(def note->number
  {"A" 9 "A#/Bb" 10 "B" 11 "C" 0
   "C#/Db" 1 "D" 2 "D#/Eb" 3 "E" 4
   "F" 5 "F#/Gb" 6 "G" 7 "G#/Ab" 8})

(defn scale->notes
  "{:duration 400, :note 60}"
  [scale]
  (map #(hash-map :note (+ (get note->number %) 60) :duration 300) scale))

(defn play-scale 
  [scale]
  (let [notes (scale->notes scale)]
    (play-notes (concat notes
                        (rest (reverse notes))))))