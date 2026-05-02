@echo off
set ROOT=%~dp0
cd /d %ROOT%front
npm install
start "ForME Frontend" /min cmd /c "cd /d %ROOT%front && npm start"
echo Frontend started.
