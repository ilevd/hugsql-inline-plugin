(ns hugsql-inline-plugin.core-test
  (:require [clojure.test :refer :all]
            [hugsql.core :as hug]
            [hugsql-inline-plugin.core]))

(deftest inline-params-test
  (testing "Inline parameters"
    (is (= (hug/sqlvec "SELECT :sql:sql, :i*:cols FROM table WHERE id IN (:v*:ids) AND t = :t:tuple AND ts = (:t*:tuple-list)"
                       {:inline true}
                       {:sql        "count()"
                        :cols       ["id" "name"]
                        :ids        [10, 20, 30]
                        :key        :keyword
                        :tuple      ["string" true 104 nil]
                        :tuple-list [[1 "a"] [2 "b"]]})
           ["SELECT count(), id, name FROM table WHERE id IN (10,20,30) AND t = ('string',TRUE,104,NULL) AND ts = ((1,'a'),(2,'b'))"]))

    )
  )
