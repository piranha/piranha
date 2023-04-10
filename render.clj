#!/usr/bin/env bb

(require
  '[clojure.data.xml :as xml]
  '[selmer.parser :as t])


(def FEED "https://solovyov.net/blog.atom")


(defn tag= [tag nodes]
  (filter #(= (:tag %) tag) nodes))


(def blog-data (-> (slurp FEED)
                   (xml/parse-str :namespace-aware false)))


(def blog-posts
  (for [entry (tag= :entry (:content blog-data))
        :let  [title (first (tag= :title (:content entry)))
               link  (first (tag= :link (:content entry)))]]
    {:title (apply str (:content title))
     :url   (:href (:attrs link))}))


(println
  (t/render "Github is just code with no context! Find out more:

- follow me on Twitter [@asolovyov](https://twitter.com/asolovyov)
- subscribe to my Telegram channel [@bitethebyte](https://t.me/bitethebyte) (it's in Ukrainian)
- read my [blog](https://solovyov.net/)

Here are latest posts from the blog:

{% for post in posts %}- [{{ post.title }}]({{ post.url }})
{% endfor %}"
    {:posts blog-posts}))
