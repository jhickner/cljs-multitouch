(ns cljs-multitouch.core)

; use native data structures for speed
(def *touch-lookup* (js-obj))
(def *touches* (js/Array.))

(defn get-target-touches [target]
  (let [*target-touches* (js/Array.)]
    (doseq [t *touches*]
      (when (= target (.-target t))
        (.push *target-touches* t)))
    *target-touches*))

(defn create-event [name touch]
  (let [evt (.createEvent js/document "CustomEvent")
        target (.-target touch)]
    (.initEvent evt name true true)
    (set! (.-touches evt) *touches*)
    (set! (.-targetTouches evt) (get-target-touches target))
    (set! (.-changedTouches evt) (array touch))
    (if target
      (.dispatchEvent target evt)
      (.dispatchEvent js/document evt))))

(defn tuio-callback [type sid fid x y angle]
  (let [page-x (* x (.-innerWidth js/window))
        page-y (* y (.-innerHeight js/window))
        data (if (not= type 3)
               (aget *touch-lookup* sid)
               (js* "{sid:~{sid}, fid:~{fid}}"))]
    (doto data
      (aset "identifier" sid)
      (aset "pageX" page-x)
      (aset "pageY" page-y)
      (aset "target" (.elementFromPoint js/document page-x page-y)))
    (aset *touch-lookup* sid data)
    (condp = type
      3 (do 
          (.push *touches* data)
          (create-event "touchstart" data))
      4 (create-event "touchmove" data)
      5 (do
          (.splice *touches* (.indexOf *touches* data) 1)
          (js-delete *touch-lookup* key)
          (create-event "touchend" data)))))

(defn init 
  "The npTuioClient plugin expects a function called tuio_callback
  to be defined at the top level, so we do that here."
  []
  (set! js/document .-tuio_callback tuio-callback))

