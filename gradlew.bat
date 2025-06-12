@echo off
set DIR=%~dp0
if "%GRADLE_HOME%"=="" (
    set GRADLE_EXE=gradle
) else (
    set GRADLE_EXE=%GRADLE_HOME%\bin\gradle
)
"%GRADLE_EXE%" --project-dir "%DIR%" %*
