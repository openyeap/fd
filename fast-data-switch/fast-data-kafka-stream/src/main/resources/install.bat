@echo off
setlocal enabledelayedexpansion
REM 当前脚本路径
set SCRIPT=%0

REM 当前脚本名称
FOR /F "delims=" %%i IN ("%SCRIPT%") DO (
    set filedrive=%%~di
    set filepath=%%~pi
    set fileextension=%%~xi
    set APPNAME=%%~ni
) 
REM 当前工作目录
for %%I in ("%filedrive%%filepath%") do (
    set HOME=%%~dpnI
)

rs install {project.name} -p "%HOME%\bin\koffer" -w "%HOME%" args java -jar "%HOME%\lib\{project.name}-3.0.0-RELEASE.kjar"