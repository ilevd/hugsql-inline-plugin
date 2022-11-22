# hugsql-inline-plugin

[HoneySQL](https://github.com/seancorfield/honeysql#format) allows you to format the query as a 
string with no parameters (e.g. to use the SQL statement in a SQL console) 
by passing `:inline true` as an option to sql/format:

```clojure

(sql/format {:select [:a :b :c]
             :from   [:foo]
             :where  [:= :foo.a "baz"]}
            {:inline true})
=> 
["SELECT a, b, c FROM foo WHERE foo.a = 'baz'"]
```

The purpose of this plugin is to do the same for HugSQL.

Just require the library in namespace to load it before using HugSQL:

```clojure
(ns yournamespace.core
  (require [husql-inline-plugin.core]))
```

And use it with `:inline true` option: 

```clojure
(hugsql/sqlvec "SELECT :sql:sql, :i*:cols FROM table WHERE id IN (:v*:ids) AND t = :t:tuple AND ts = (:t*:tuple-list)"
               {:inline true}
               {:sql        "count()"
                :cols       ["id" "name"]
                :ids        [10, 20, 30]
                :key        :keyword
                :tuple      ["string" true 104 nil]
                :tuple-list [[1 "a"] [2 "b"]]})
=>
["SELECT count(), id, name FROM table WHERE id IN (10,20,30) AND t = ('string',TRUE,104,NULL) AND ts = ((1,'a'),(2,'b'))"]                
```

And without `:inline` option it would be: 


```clojure
(hugsql/sqlvec "SELECT :sql:sql, :i*:cols FROM table WHERE id IN (:v*:ids) AND t = :t:tuple AND ts = (:t*:tuple-list)"
               {:sql        "count()"
                :cols       ["id" "name"]
                :ids        [10, 20, 30]
                :key        :keyword
                :tuple      ["string" true 104 nil]
                :tuple-list [[1 "a"] [2 "b"]]})
=>
["SELECT count(), id, name FROM table WHERE id IN (?,?,?) AND t = (?,?,?,?) AND ts = ((?,?),(?,?))"
 10
 20
 30
 "string"
 true
 104
 nil
 1
 "a"
 2
 "b"]            
```

## License

Copyright © 2022 ilevd

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.
