#!/bin/sh
rundir=$(pwd)
export rundir
#echo rundir=$rundir
# icu is not needed any more:
# $rundir/lib/icu4j-70.1.jar:
java -cp $rundir/javafxwebpages-1.0.jar:$rundir/lib/gson-2.10.1.jar:$CLASSPATH com.metait.javafxwebpages.WebPagesApplication
