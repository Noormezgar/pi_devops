$root = Split-Path -Parent $MyInvocation.MyCommand.Definition
Push-Location $root

function Start-PowerShellWindow {
    param(
        [string]$WorkingDirectory,
        [string]$Command,
        [string]$Title
    )
    $escapedCommand = "Set-Location -LiteralPath '$WorkingDirectory'; `$Host.UI.RawUI.WindowTitle = '$Title'; $Command"
    Start-Process powershell -ArgumentList '-NoExit', '-Command', $escapedCommand -WorkingDirectory $WorkingDirectory
}

function Start-BackendMavenModules {
    param(
        [string[]]$Modules
    )

    foreach ($module in $Modules) {
        $modulePath = Join-Path -Path $root -ChildPath "back\$module"
        if (Test-Path $modulePath) {
            Write-Host "Démarrage de $module avec Maven..."
            Start-PowerShellWindow -WorkingDirectory $modulePath -Command 'mvn spring-boot:run' -Title "ForME Backend - $module"
        } else {
            Write-Warning "Module introuvable : $modulePath"
        }
    }
}

function Get-DockerDaemonReady {
    try {
        & docker info > $null 2>&1
        return $LASTEXITCODE -eq 0
    } catch {
        return $false
    }
}

function Start-DockerDesktop {
    $dockerPath = 'C:\Program Files\Docker\Docker\Docker Desktop.exe'
    if (Test-Path $dockerPath) {
        Write-Host "Tentative de démarrage de Docker Desktop..."
        Start-Process -FilePath $dockerPath
        Start-Sleep -Seconds 20
        return Get-DockerDaemonReady
    }
    return $false
}

Write-Host '=== ForME Project Startup ==='

if (-not (Get-Command npm -ErrorAction SilentlyContinue)) {
    Write-Error 'npm n''est pas trouvé. Installez Node.js puis relancez le script.'
    exit 1
}

Write-Host 'Installation des dépendances frontend...'
Set-Location "$root\front"
npm install
if ($LASTEXITCODE -ne 0) {
    Write-Error 'npm install a échoué. Impossible de démarrer le frontend.'
    exit 1
}

Set-Location $root
Write-Host 'Démarrage du frontend dans une nouvelle fenêtre...'
Start-PowerShellWindow -WorkingDirectory "$root\front" -Command 'npm start' -Title 'ForME Frontend'

$dockerAvailable = $false
if (Get-Command docker -ErrorAction SilentlyContinue) {
    Write-Host 'Docker CLI détecté. Vérification du daemon...'
    $dockerAvailable = Get-DockerDaemonReady
}

if (-not $dockerAvailable) {
    Write-Host 'Docker n''est pas disponible. Tentative de démarrage de Docker Desktop...'
    $dockerAvailable = Start-DockerDesktop
}

if ($dockerAvailable) {
    Write-Host 'Docker daemon disponible. Démarrage du backend avec Docker Compose...'
    Start-PowerShellWindow -WorkingDirectory "$root\back" -Command 'docker compose up' -Title 'ForME Backend (Docker)'
} else {
    Write-Warning 'Docker n''est pas disponible ou le daemon n''a pas démarré. Tentative de démarrage local du backend avec Maven...'
    $mysqlReady = Test-NetConnection -ComputerName localhost -Port 3306 -WarningAction SilentlyContinue -InformationLevel Quiet
    if (-not $mysqlReady) {
        Write-Warning 'MySQL sur localhost:3306 n''est pas disponible. Le backend ne pourra pas démarrer localement sans base de données.'
        Write-Host '1) Lancez Docker Desktop et assurez-vous qu''il est en cours d''exécution.'
        Write-Host '2) Ou installez/activez MySQL localement sur le port 3306.'
        Write-Host '3) Ensuite, relancez .\run.ps1 depuis c:\Users\user\Desktop\forme.'
    } else {
        Write-Host 'MySQL local détecté sur localhost:3306. Démarrage du backend avec Maven...'
        Start-BackendMavenModules -Modules @(
            'eureka-server',
            'api-gateway',
            'user-service',
            'document-service',
            'article-service'
        )
        Write-Host 'Le backend local démarre dans plusieurs fenêtres PowerShell.'
    }
}

Pop-Location
