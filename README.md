# clojure-web-app

This is a showcase project aimed to demonstrate how to build
[single-page application](https://en.wikipedia.org/wiki/Single-page_application) on clojure and clojurescript.

It provides basic [CRUD](https://en.wikipedia.org/wiki/CRUD) functionality on editing user accounts
in a single-page style utilizing [websockets](https://en.wikipedia.org/wiki/WebSocket) and exposing [REST](https://en.wikipedia.org/wiki/REST)
interface for external clients.

## Screenshots

[](https://github.com/arevkov/clojure-web-app/raw/master/doc/screen-table.png)

[](https://github.com/arevkov/clojure-web-app/raw/master/doc/screen-edit.png)

[](https://github.com/arevkov/clojure-web-app/raw/master/doc/screen-delete.png)


## Dependencies

| Name | Purpose |
|------|---------|
|[bidi]|routing|
|[hiccup]|template engine|
|[hugsql]|relation-to-function mapping(framework to rdbms)|
|[c3p0]|rdbms connection pool|
|[sente]|an abstraction over WebSockets and Ajax|
|[bootstrap]|html/css/js framework, might want to take a look at [Foundation] as alternative|
|[reagent]|interface between ClojureScript and [React](http://facebook.github.io/react/) - DOM manipulation and 2-way binding|
|[reagent-modals]|modal windows|
|[ring]|web abstraction layer|
|[immutant-web]|embedded app container|
|[figwheel]|lein plugin pushing ClojureScript to browser|

## REST API

## Build

    $ lein uberjar

## Usage

The app requires PostgreSQL running on localhost:5432
The easiest way to get it is to use [the docker image](https://hub.docker.com/r/_/postgres/)

    $ docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres

Run the program:

    $ java -jar clojure-web-app.jar

Open in browser http://localhost:8080

## FAQ

* Why do I need to keep all that javascripts and stylescheets in public folder
  instead of just having links on them in some public CDN?

  Actually, you don't.

* When do I need externs?

  In short, advanced clojurescript compilation breaks references to functions
  defined outside of the compiled code. 'Externs' declare functions
  that should be renamed by compiler. For more details take a look at a [great post](https://blog.8thlight.com/taryn-sauer/2014/07/31/clojurescript-faux-pas.html) on this issue.

  You can use [javascript externs generator](http://jmmk.github.io/javascript-externs-generator/)

* How to improve my UI? (paging, sorting, charts, tabs, templates etc.)

  1. There is a number of recipes at [reagent cookbook](https://github.com/reagent-project/reagent-cookbook)
     (including fully featured [data table recipe](https://github.com/reagent-project/reagent-cookbook/tree/master/recipes/data-tables))
  2. Take a look at [some ui templates](http://startbootstrap.com/) on bootstrap.
  3. Bring in into project your favorite js library.

* How to use another DB?

  Guess replace jdbc driver and edit port in config.

## License

Copyright © 2016 arevkov

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
