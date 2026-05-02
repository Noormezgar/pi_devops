@echo off
cd /d %~dp0\back\business-domain\microservices-project\business-service
start "business-service" /min cmd /c "cd /d %~dp0\back\business-domain\microservices-project\business-service && mvn spring-boot:run"
start "partner-performance" /min cmd /c "cd /d %~dp0\back\business-domain\partner-performance-service && mvn spring-boot:run"
start "voucher-fraud" /min cmd /c "cd /d %~dp0\back\business-domain\voucher-fraud-service && mvn spring-boot:run"
start "partner-contract" /min cmd /c "cd /d %~dp0\back\business-domain\partner-contract-service && mvn spring-boot:run"
start "partner-billing" /min cmd /c "cd /d %~dp0\back\business-domain\partner-billing-service && mvn spring-boot:run"
echo Started business services windows.
