(ns leiningen.new.dw-code-exercise
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "dw-code-exercise"))

(defn dw-code-exercise
  "FIXME: write documentation"
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' dw-code-exercise project.")
    (->files data
             ["src/{{sanitized}}/foo.clj" (render "foo.clj" data)])))
