@echo off
set "JAVA_HOME=C:\Java\jdk-17"
set "MAVEN_HOME=C:\Maven\apache-maven-3.9.14"
set "PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%"

echo ==========================================================
echo          TEST DE VALIDACION DE USUARIOS: CLINICA AAUCA
echo ==========================================================
echo Configurando entorno de prueba...
echo.
echo Usando Java: %JAVA_HOME%
echo Usando Maven: %MAVEN_HOME%
echo.
echo Ejecutando Prueba Integrada (TestLogin.java)...
echo.

mvn compile exec:java "-Dexec.mainClass=com.clinica.aauca.TestLogin"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [PROCESO OK] Las validaciones de usuarios son correctas.
) else (
    echo.
    echo [ERROR] No se pudo completar la validacion o hubo accesos no autorizados.
)

echo.
echo Presione cualquier tecla para salir...
pause > nul
