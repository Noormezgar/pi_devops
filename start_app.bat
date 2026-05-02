@echo off
set ROOT=%~dp0
cd /d %ROOT%

start "ForME Backend" /min cmd /c "cd /d %ROOT% && start_local_app.bat"
start "ForME Frontend" /min cmd /c "cd /d %ROOT%front && npm install && npm start"
echo Full app startup initiated.
pause
