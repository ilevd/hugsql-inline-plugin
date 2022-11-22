(ns hugsql-inline-plugin.core
  (:require [hugsql.parameters :as hug-parameters]
            [clojure.string :as string]))


(defn- sql-kw
  "Given a keyword, return a SQL representation of it as a string.
  Any ? is escaped to ??. Replace _embedded_ hyphens."
  [k]
  (-> (name k)
      (string/replace "?" "??")
      (string/replace "-" "_")))


(defn- upper-case
  "Upper-case a string in Locale/US to avoid locale-specific capitalization."
  [^String s]
  (.. s toString (toUpperCase (java.util.Locale/US))))


(defprotocol InlineValue :extend-via-metadata true
  (sqlize [this] "Render value inline in a SQL string."))


(extend-protocol InlineValue
  nil
  (sqlize [_] "NULL")
  Boolean
  (sqlize [x] (upper-case (str x)))
  String
  (sqlize [x] (str \' (string/replace x "'" "''") \'))
  clojure.lang.Keyword
  (sqlize [x] (sql-kw x))
  clojure.lang.Symbol
  (sqlize [x] (sql-kw x))
  clojure.lang.IPersistentVector
  (sqlize [x] (str "[" (string/join ", " (map sqlize x)) "]"))
  java.util.UUID
  ;; Quoted UUIDs for PostgreSQL/ANSI
  (sqlize [x] (str \' x \'))
  Object
  (sqlize [x] (str x)))


(extend-type Object
  hug-parameters/ValueParam
  (value-param [param data options]
    (if (:inline options)
      [(sqlize (get-in data (hug-parameters/deep-get-vec (:name param))))]
      ["?" (get-in data (hug-parameters/deep-get-vec (:name param)))]))

  hug-parameters/ValueParamList
  (value-param-list [param data options]
    (let [coll (get-in data (hug-parameters/deep-get-vec (:name param)))]
      (if (:inline options)
        [(string/join "," (map sqlize coll))]
        (apply vector
               (string/join "," (repeat (count coll) "?"))
               coll)))))
