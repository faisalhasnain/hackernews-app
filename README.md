# Hacker News App

Open-source Hacker News Reader focusing on neat design and better readability built using [ClojureScript](https://clojurescript.org) & [Re-frame](https://github.com/Day8/re-frame)

## Libraries Used
- [Reagent](https://reagent-project.github.io)
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
