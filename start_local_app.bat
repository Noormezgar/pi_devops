@echo off
set ROOT=%~dp0
cd /d %ROOT%

echo Starting local ForME application...

start "Eureka Server" /min cmd /c "cd /d %ROOT%back\eureka-server && mvn spring-boot:run"
start "API Gateway" /min cmd /c "cd /d %ROOT%back\api-gateway && mvn spring-boot:run"
start "User Service" /min cmd /c "cd /d %ROOT%back\user-service && mvn -Dspring-boot.run.profiles=local spring-boot:run"
start "Business Service" /min cmd /c "cd /d %ROOT%back\business-domain\microservices-project\business-service && mvn -Dspring-boot.run.profiles=local spring-boot:run"
start "Partner Contract Service" /min cmd /c "cd /d %ROOT%back\business-domain\partner-contract-service && mvn -Dspring-boot.run.profiles=local spring-boot:run"
start "Partner Billing Service" /min cmd /c "cd /d %ROOT%back\business-domain\partner-billing-service && mvn -Dspring-boot.run.profiles=local spring-boot:run"
start "Partner Intelligence Service" /min cmd /c "cd /d %ROOT%back\business-domain\partner-intelligence-service && mvn -Dspring-boot.run.profiles=local spring-boot:run"
start "Voucher Fraud Service" /min cmd /c "cd /d %ROOT%back\business-domain\voucher-fraud-service && mvn -Dspring-boot.run.profiles=local spring-boot:run"
start "Certification Service" /min cmd /c "cd /d \"%ROOT%back\formecertification\certification-service\" && mvn -Dspring-boot.run.profiles=local spring-boot:run"
start "Shop Service" /min cmd /c "cd /d \"%ROOT%back\gestion shop\" && .\mvnw.cmd spring-boot:run"

echo Local services started. Wait a minute for startup to complete.
pause
