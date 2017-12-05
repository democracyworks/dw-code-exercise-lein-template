(ns leiningen.new.dw-code-exercise
  (:require [clojure.java.shell :refer [sh] :as shell]
            [clojure.string :as str]
            [leiningen.new.templates :refer [renderer name-to-path ->files
                                             *force?*]]
            [leiningen.core.main :as main]))

(def render (renderer "dw-code-exercise"))

(defn configure-git-repo
  "Configures a git repo in `dir` with anonymized name and email and commits
  the template output so the user starts with a clean working directory."
  [dir]
  (shell/with-sh-dir dir
                     (sh "git" "init")
                     (sh "git" "config" "--local" "user.name"
                         "Anonymous Applicant")
                     (sh "git" "config" "--local" "user.email"
                         "anonymous@example.com")
                     (sh "git" "config" "--local" "commit.gpgSign" "false")
                     (sh "git" "add" ".")
                     (sh "git" "commit" "-qm" "template output")))

(defn dw-code-exercise
  "Main entry point for creating a project from the template"
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info (str "Generating new Democracy Works code exercise project in '" name "' directory."))
    (->files data
             ["src/{{sanitized}}/core.clj" (render "core.clj" data)]
             ["src/{{sanitized}}/home.clj" (render "home.clj" data)]
             ["src/{{sanitized}}/us_state.clj" (render "us_state.clj" data)]
             ["src/{{sanitized}}/submit.clj" (render "submit.clj" data)]
             ["project.clj" (render "project.clj" data)]
             ["resources/public/default.css" (render "default.css" data)]
             ["test/{{sanitized}}/us_state_test.clj" (render "us_state_test.clj" data)]
             [".gitignore" (render "gitignore" data)])
    (configure-git-repo name)
    (main/info (str/join "\n"
                         [""
                          "This is a basic \"match users to their elections\" web app project."
                          ""
                          "It is similar to the kinds of things you'd be working on at Democracy Works"
                          "and uses one of our APIs."
                          ""
                          (str "After you cd into the " name " dir, run `lein ring server`.")
                          "This will open the app in your default web browser."
                          "Further instructions will be presented there."]))))
