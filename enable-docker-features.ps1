$features = @(
    'Microsoft-Hyper-V-All',
    'Containers',
    'VirtualMachinePlatform',
    'Microsoft-Windows-Subsystem-Linux'
)

Write-Host "=== Activation des fonctionnalités nécessaires pour Docker Desktop ==="

foreach ($feature in $features) {
    Write-Host "Vérification / activation de : $feature"
    $state = (Get-WindowsOptionalFeature -Online -FeatureName $feature -ErrorAction SilentlyContinue).State
    Write-Host "État actuel : $state"
    if ($state -ne 'Enabled') {
        Enable-WindowsOptionalFeature -Online -FeatureName $feature -All -NoRestart -ErrorAction SilentlyContinue | Out-Null
        $newState = (Get-WindowsOptionalFeature -Online -FeatureName $feature -ErrorAction SilentlyContinue).State
        Write-Host "État après tentative : $newState"
    }
    Write-Host ''
}

Write-Host "Activation terminée. Un redémarrage peut être nécessaire pour appliquer les modifications."
Write-Host "Après redémarrage, relancez Docker Desktop et exécutez .\run.ps1 depuis le dossier c:\Users\user\Desktop\forme."