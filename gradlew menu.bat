@ECHO off
goto init

:init
CLS
ECHO Select a task
ECHO A: eclipse
ECHO B: build
ECHO C: runClient
ECHO D: Exit
CHOICE /C:ABCD /N
if %ERRORLEVEL% == 1 goto eclipse
if %ERRORLEVEL% == 2 goto build
if %ERRORLEVEL% == 3 goto runClient
if %ERRORLEVEL% == 4 goto exittask


:eclipse
CLS
call gradlew eclipse genEclipseRuns
PAUSE
goto init

:build
CLS
call gradlew build
PAUSE
goto init

:runClient
CLS
call gradlew runClient
PAUSE
goto init


:exittask
CHOICE /M "Are you sure?"
if %ERRORLEVEL% == 1 EXIT
if %ERRORLEVEL% == 2 goto init