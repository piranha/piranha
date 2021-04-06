#!/usr/bin/env bb

(require
  '[clojure.data.xml :as xml]
  '[clojure.string :as str])


(def FEED "https://solovyov.net/blog.atom")
(def HEAD
  "Github is just code with no context! Find out more:

- follow me on Twitter [@asolovyov](https://twitter.com/asolovyov)
- subscribe to my Telegram channel [@bitethebyte](https://t.me/bitethebyte) (it's in Ukrainian)
- read my [blog](https://solovyov.net/)

Here are latest posts from the blog:")


(defn tag= [tag nodes]
  (filter #(= (:tag %) tag) nodes))


(def data (-> (slurp FEED)
              (xml/parse-str :namespace-aware false)))


(def blog-posts
  (for [entry (tag= :entry (:content data))
        :let  [title (first (tag= :title (:content entry)))
               link  (first (tag= :link (:content entry)))]]
    (format "- [%s](%s)"
      (apply str (:content title))
      (:href (:attrs link)))))


(println (str/join "\n" (cons HEAD blog-posts)))
