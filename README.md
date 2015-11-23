# ungleich-pricing

This is a frontend only ClojureScript app based on Reagent (which
again is based on Facebook's React) to render
[Ungleich Gmbh's](http://ungleich.ch/) pricing.

See a demo running [here](http://dispatched.ch/ungleich_pricing/).

## Integration

Copy [foo](resources/public/js/compiled/ungleich_pricing.js) into your web project. Also create a `div` with an id of `app`.

Sit back and enjoy.

## Setup for development

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL.

## License

Copyright © 2014 Munen Alain M. Lafon <munen@200ok.ch>

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
