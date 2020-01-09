@echo off
start image\bin\javaw.exe -jar update4j.jar --remote https://raw.githubusercontent.com/mtricht/lunaris/master/autoupdater.xml --singleInstance
echo Starting lunaris, checking for updates...
timeout /t 3 > NUL