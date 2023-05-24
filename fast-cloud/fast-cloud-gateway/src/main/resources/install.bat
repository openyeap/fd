
@echo off
setlocal enabledelayedexpansion

REM 配置app path
set APPPATH=.\bin\windows\gateway

set SCRIPT=%0

REM 当前脚本名称
FOR /F "delims=" %%i IN ("%SCRIPT%") DO (
    set filedrive=%%~di
    set filepath=%%~pi
    set fileextension=%%~xi
    set APPNAME=%%~ni
) 

%filedrive%
cd %filepath%


if "%1" == "uninstall" (
  goto uninstall
) 

if "%1"  == "install" (
  goto install
) 

goto help

:help
  echo Usage: %APPName% [command]
  echo  Available commands are:
  echo  install   install service
  echo  uninstall uninstall service
  goto end
 
:uninstall
    "%APPPATH%" uninstall
    goto help


:install
    for %%I in (%CD%\%APPPATH%\..) do (
      set HOME=%%~dpnI
    )
    for %%I in (%CD%\%APPPATH%) do (
      cd %HOME%
      start %%~dpnxI install
    )
    goto end

:end
    endlocal
