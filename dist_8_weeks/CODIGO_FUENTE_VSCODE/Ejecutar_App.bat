@echo off
set "JAVA_HOME=C:\Java\jdk-17"
set "MAVEN_HOME=C:\Maven\apache-maven-3.9.14"
set "PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%"

echo ==========================================================
echo       INICIANDO SISTEMA DE GESTION: CLINICA AAUCA
echo ==========================================================
echo Configurando entorno...
echo.
echo Usando Java: %JAVA_HOME%
echo Usando Maven: %MAVEN_HOME%
echo.
echo Compilando y Ejecutando Aplicacion...
echo.

mvn javafx:run

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Hubo un problema al lanzar la aplicacion.
    echo Asegurese de que las carpetas C:\Java y C:\Maven existan correctamente.
    pause
)
