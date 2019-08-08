# Hacker News App

Neat Hacker News Reader App built using [ClojureScript](https://clojurescript.org)

## Libraries Used
- [Reagent](https://reagent-project.github.io)
- [Re-frame](https://github.com/Day8/re-frame)
- [Reitit](https://metosin.github.io/reitit/)
- [Bulma](https://bulma.io)

## Development Mode

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Build


To compile clojurescript to javascript:

```
lein clean
lein cljsbuild once min
```
