@echo off
set name="
set filepath=%1
set filepath=%filepath:~1,-1%
:1
IF NOT "%filepath:~-1%"=="\" goto 2
cd "%filepath%"
java.exe -jar "%name:~0,-1%"
echo End of program.
pause
goto 3
:2
set name=%filepath:~-1%%name%
set filepath=%filepath:~0,-1%
goto 1
:3