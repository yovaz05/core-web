#!/bin/sh

CORE=/home/daniel/datos/yhaguy/proyecto-core/core
YHAGUY=/home/daniel/datos/yhaguy/proyecto-core/yhaguy
VIDRIOLUZ=/home/daniel/datos/vidrio-luz/proyecto/vidrioluz
TDNPEDIDOS=/home/daniel/datos/tdn/pedidos/pedidosApp

########### yhaguy ##########################
DESDE=$CORE
HASTA=$YHAGUY

cp $DESDE/WEB-INF/lang-addon.xml $HASTA/WEB-INF/lang-addon.xml

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

########### vidrioluz ##########################
DESDE=$CORE
HASTA=$VIDRIOLUZ

cp $DESDE/WEB-INF/lang-addon.xml $HASTA/WEB-INF/lang-addon.xml

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


########### TDN Pedidos ##########################
DESDE=$CORE
HASTA=$TDNPEDIDOS

cp $DESDE/WEB-INF/lang-addon.xml $HASTA/WEB-INF/lang-addon.xml

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

