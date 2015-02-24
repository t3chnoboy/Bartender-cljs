(ns expedite-bartender.utils
  (:require [cljs.core.async :refer [put! chan]])
  (:import [goog.net Jsonp]
           [goog Uri]))

(defn random-beer-pic []
  (rand-nth ["http://ipelican.com/images_attachments/products/257365/medium/164915.jpg",
             "http://www.kegworks.com/media/catalog/product/cache/1/small_image/200x200/9df78eab33525d08d6e5fb8d27136e95/1/7/176733-libbey-heidelberg-beer-mug-set-b1_1.jpg",
             "http://www.truebeer.com/assets/images/European-Beer-Mug-1.jpg",
             "http://media.onsugar.com/files/2010/09/38/4/192/1922195/6dc8674b1cc20819_beer_mug.xlarge.jpg",
             "http://www.clipartbest.com/cliparts/yck/gpX/yckgpXdcE.jpeg",
             "https://umdarchives.files.wordpress.com/2013/01/cask-beer-mug.jpg",
             "http://thumbs.dreamstime.com/x/mug-beer-5000167.jpg",
             "http://coronadocommonsense.typepad.com/.a/6a00e55417fcfd88340128765e75f1970c-500wi",
             "http://thumbs.dreamstime.com/x/alcohol-light-beer-mug-froth-isolated-6719900.jpg",
             "http://www.clker.com/cliparts/e/0/4/2/1341278757730855408beermug-hi.png",
             "http://publicbar.com/wp-content/uploads/2010/08/beermug.png",
             "http://www.officialpsds.com/images/thumbs/Beer-Mug-psd19134.png",
             "http://publicbar.com/wp-content/uploads/2010/08/beermug.png",
             "http://gentoovps.net/wp-content/uploads/2014/10/beer_PNG2353.png",
             "https://i.imgur.com/xLvjD.png"]))

(defn jsonp [uri]
  (let [out (chan)
        req (Jsonp. (Uri. uri))]
    (.send req nil (fn [res]
                     (put! out (js->clj res :keywordize-keys true))))
    out))
