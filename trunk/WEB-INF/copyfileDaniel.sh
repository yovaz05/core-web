#!/bin/sh

CORE=/home/daniel/datos/yhaguy/proyecto-core/core
YHAGUY=/home/daniel/datos/yhaguy/proyecto-core/yhaguy
VIDRIOLUZ=/home/daniel/datos/vidrio-luz/proyecto/vidrioluz
TDNAPP=/home/daniel/datos/tdn/tdnapp

########### yhaguy ##########################
DESDE=$CORE
HASTA=$YHAGUY

# Archivos .zul
cd $DESDE
zip -r core core/*
mv core.zip  $HASTA/
cd $HASTA
unzip -o core.zip
#chmod 777 -R core
rm core.zip

# Archivos .class (crea jar)

cd $CORE/WEB-INF/classes
zip -r core com/*  -x *.svn*
mv core.zip ../src
cd ../src
zip -r core com/*  -x *.svn*
mv core.zip core.jar
mv core.jar  $HASTA/WEB-INF/lib/

########### vidrio luz ##########################
DESDE=$CORE
HASTA=$VIDRIOLUZ

# Archivos .zul
cd $DESDE
zip -r core core/*
mv core.zip  $HASTA/
cd $HASTA
unzip -o core.zip
#chmod 777 -R core
rm core.zip

# Archivos .class (crea jar)

cd $CORE/WEB-INF/classes
zip -r core com/*  -x *.svn*
mv core.zip ../src
cd ../src
zip -r core com/*  -x *.svn*
mv core.zip core.jar
mv core.jar  $HASTA/WEB-INF/lib/


########### tdnapp ##########################
DESDE=$CORE
HASTA=$TDNAPP

# Archivos .zul
cd $DESDE
zip -r core core/*
mv core.zip  $HASTA/
cd $HASTA
unzip -o core.zip
#chmod 777 -R core
rm core.zip

# Archivos .class (crea jar)

cd $CORE/WEB-INF/classes
zip -r core com/*  -x *.svn*
mv core.zip ../src
cd ../src
zip -r core com/*  -x *.svn*
mv core.zip core.jar
mv core.jar  $HASTA/WEB-INF/lib/




