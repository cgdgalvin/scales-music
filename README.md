# scales-of-music
  
 - add logic to chose them ect

- Need to filter the mode lists better, bring them into edn and apply better filtering

## Usage

   repl only atm

  (list-all-modes)

  (list-families)

  (list-modes "Major")

  (get-scale "D" "Dorian")

  (get-scale "D" "Major" "Dorian")

  (midi/play-scale (get-scale "C" "Lydian Aug Sharp 2"))
