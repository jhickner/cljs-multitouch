# cljs-multitouch

Multitouch monitor/overlay support in your browser! In ClojureScript!


## How it Works

cljs-multitouch is an interface to the [npTuioClient](https://github.com/fajran/npTuioClient) browser plugin. The plugin supports Firefox, Chrome and Safari (any browser that supports NPAPI plugins).

The plugin expects a [TUIO](http://www.tuio.org/) server at the standard port (localhost:3333). Almost all touchscreens have a way of exposing their touch data over this protocol. We normally use screens from [PQ Labs](http://multi-touch-screen.com/) which have a TUIO server built into the driver software.

On page load the plugin connects to the TIUO server and routes incoming TUIO data to cljs-multicouch, which converts it into standard browser touch events (touchstart, touchmove, touchend). 


## Usage

Step 1.) Install [the plugin](https://github.com/fajran/npTuioClient).
Firefox, Chrome and Safari are supported.

Step 2.) Add a tag to your HTML to load the plugin:

```html
<object id="tuio" type="application/x-tuio">Plugin FAILED to load</object>
```

Step 3.) Add cljs-multitouch to your project.clj and then run ```lein deps```.

```clojure
[cljs-multitouch "1.0"]
```


Step 4.) In your clojurescript app:

```clojure
(ns my-awesome-touchscreen-app
  (:require [cljs-multitouch.core :as multitouch]))

(multitouch/init)
```

That's it! Your app will now receive standard touchdown, touchmove
and touchend events.


## License

Copyright (C) 2012 VaryWell

Distributed under the Eclipse Public License, the same as Clojure.
