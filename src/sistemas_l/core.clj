(ns sistemas-l.core
  (:gen-class)
  (:require [clojure.java.io :as io]))

(defn read-file [file]
  "Recibe un archivo por parametro, si existe, lee su contenido y escribe como un vector"
  (try
    (with-open [reader (io/reader file)] (apply conj [] (line-seq reader)))
    (catch Exception _)))

(defn gen-patron [axioma reemp it]
  (if (zero? it)
    axioma
    (gen-patron (apply str (sequence (replace reemp (vec axioma)))) reemp (dec it))
    )
  )

(defn hash-create [remp]
  (println remp)
  (if (empty? remp) {}
                   (let [key (first (seq (apply str (first remp))))
                         value (apply str (nnext (seq (apply str (first remp)))))
                         ]

                     (merge (hash-map key value) (hash-create (vec (rest remp)))))
    )
  )

(defn tortuga [x y angulo]
  {:x x,  :y y ,:angulo angulo})

(defn gen-texto-svg [patron pila-tortuga]
  (println pila-tortuga)
  (if (empty? patron)
    " "
    (let [letra (first patron)
          resto (rest patron)
          tortuga (first pila-tortuga)
          resto-pila (rest pila-tortuga)]
      (cond
        (= letra \F) (str "Avanzo. "           (gen-texto-svg resto (cons tortuga resto-pila))) ;una nueva lista con la tortuga modificada en pos en vez de la tortuga
        (= letra \f) (str "Avanzo dibujando. " (gen-texto-svg resto (cons tortuga resto-pila))) ;lo mismo
        (= letra \+) (str "Giro Der. "         (gen-texto-svg resto (cons tortuga resto-pila))) ;una nueva lista con la tortuga modificada en angulo en vez de la tortuga
        (= letra \-) (str "Giro Izq. "         (gen-texto-svg resto (cons tortuga resto-pila))) ;lo mismo
        (= letra \|) (str "Giro 180. "         (gen-texto-svg resto (cons tortuga resto-pila))) ;lo mismo
        (= letra \[) (str "Apilo Tortuga. "    (gen-texto-svg resto (cons tortuga pila-tortuga)))
        (and (not (empty? resto-pila)) (= letra  \])) (str "Desapilo Tortuga. " (gen-texto-svg resto resto-pila))
        :else (gen-texto-svg resto pila-tortuga))
        )
      )
  )


(defn write-file [outputFile texto]
  (println texto)
  )

(defn -main [inputFile it outputFile]
  (let [info (read-file inputFile)
        angulo (Double/parseDouble (first info))
        patron (gen-patron (first (rest info)) (hash-create (vec (drop 2 info))) it)
        pila-tortuga (list (tortuga 0 0 angulo))
        ]
    (write-file outputFile (gen-texto-svg patron pila-tortuga))
    ))
