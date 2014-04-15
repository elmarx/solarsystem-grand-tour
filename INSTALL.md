Installation/Running
========================

This guide is intended for "end users" of the application.

Prerequisites
-------------

Please install the [Java Runtime][1] to run this application.

Setup
------

These manual steps are only required once.

1. choose a working directory, let's call it *wd*
2. create *wd/data*
3. create *wd/data/output*
4. download ephemerides-data [provided by NASA][2], please mirror the complete *de423* folder
*wd/data/*

    i.e., using wget: `wget -r ftp://ssd.jpl.nasa.gov/pub/eph/planets/ascii/de423/`

5. copy *input.xml* to your working directory *wd*,

Running
-------

1. To customize the calculation, edit *input.xml*.
2. go to *wd*
3. run `/path/to/dist/bin/ssgt`


[1]: https://www.java.com/de/download/manual.jsp "JRE Download, all operating systems"
[2]: ftp://ssd.jpl.nasa.gov/pub/eph/planets/ascii/de423/ "Ehpimerides data provided by NASA's HORIZON project"
