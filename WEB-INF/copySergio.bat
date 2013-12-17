rem CORE=E:\ProyectoYhaguy\core-web
rem YHAGUY=E:\ProyectoYhaguy\yhaguy

rem ########### yhaguy ##########################
rem DESDE=$CORE
rem HASTA=$YHAGUY

rem Borrar primeramente
E:
cd E:\ProyectoYhaguy\yhaguy\core
del E:\ProyectoYhaguy\yhaguy\core /Q/S *
cd E:\ProyectoYhaguy\yhaguy\WEB-INF\lib
del  E:\ProyectoYhaguy\yhaguy\WEB-INF\lib\core.jar /Q

rem # Archivos .zul
C:
cd "C:\Program Files\7-Zip"
7z.exe a -tzip E:\ProyectoYhaguy\yhaguy\core.zip    E:\ProyectoYhaguy\core-web\core\*
7z.exe x          E:\ProyectoYhaguy\yhaguy\core.zip -oE:\ProyectoYhaguy\yhaguy\core
cd E:\ProyectoYhaguy\yhaguy
del E:\ProyectoYhaguy\yhaguy\core.zip

rem # Archivos .class (crea jar)
cd "C:\Program Files\7-Zip"
rem    7z.exe a -tzip E:\ProyectoYhaguy\yhaguy\WEB-INF\lib\core.zip E:\ProyectoYhaguy\core-web\WEB-INF\classes\* -x!*.dtd

7z.exe a -tzip E:\ProyectoYhaguy\yhaguy\WEB-INF\lib\core.zip E:\ProyectoYhaguy\core-web\WEB-INF\classes\* 
7z.exe a -tzip E:\ProyectoYhaguy\yhaguy\WEB-INF\lib\core.zip E:\ProyectoYhaguy\core-web\WEB-INF\src\* 

copy E:\ProyectoYhaguy\yhaguy\WEB-INF\lib\core.zip E:\ProyectoYhaguy\yhaguy\WEB-INF\lib\core.jar
copy E:\ProyectoYhaguy\core-web\WEB-INF\lang-addon.xml E:\ProyectoYhaguy\yhaguy\WEB-INF\lang-addon.xml

cd E:\ProyectoYhaguy\yhaguy\WEB-INF\lib
del E:\ProyectoYhaguy\yhaguy\WEB-INF\lib\core.zip

