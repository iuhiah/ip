@ECHO OFF

REM create bin directory if it doesn't exist
if not exist ..\bin mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.TXT del ACTUAL.TXT

REM compile the code into the bin folder
javac -cp ..\src\main\java -Xlint:none -d ..\bin^
 ..\src\main\java\bibo\*.java^
 ..\src\main\java\bibo\exception\*.java^
 ..\src\main\java\bibo\task\*.java

IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    exit /b 1
)
REM no error here, errorlevel == 0

REM run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath ..\bin bibo.Bibo < input.txt > ACTUAL.TXT

@REM REM REM compare the output to the expected output
@REM REM FC ACTUAL.TXT EXPECTED.TXT
