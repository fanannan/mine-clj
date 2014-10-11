# mine-clj

A Clojure wrapper for MINE.jar, the library for the maximal information-based nonparametric exploration (MINE) statistics, including maximal information coefficient (MIC).

## Usage

You have to place MINE.jar where :resource-paths of project.clj indicates.
MINE.jar can be downloaded at http://www.exploredata.net/Downloads/MINE-Application

The function 'mine' is defined on mine-clj.core and takes 2 numeric vectors.
It returns a map with various MINE statistics including MIC, pearson correlation coefficient.

See mine-clj.core/-main for sample calculations

## License

Copyright © 2014 Takahiro SAWADA

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
