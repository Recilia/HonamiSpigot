Write-Host "Waiting for Honami-Server script to finish..."
while ((Get-Process powershell -ErrorAction SilentlyContinue | Where-Object { $_.Id -ne $PID }).Count -gt 0) {
    Start-Sleep -Seconds 15
    $remaining = git ls-files --others --exclude-standard "Honami-Server/" | Measure-Object -Line
    Write-Host "  Honami-Server files remaining: $remaining"
}
Write-Host "Honami-Server script finished. Starting remaining files..."

$files = git ls-files --others --exclude-standard -- ":(exclude)Honami-Server/"
$total = $files.Count
$i = 0
$logFile = "C:\Users\Rein\Documents\ACENTRA STUFFS\Acentra-Github\HonamiSpigot\commit-log-rest.txt"
"Started: $(Get-Date)" | Out-File $logFile
foreach ($file in $files) {
    $i++
    $short = $file.Replace("Honami-API/src/main/java/", "").Replace("Honami-API/", "").Replace("Honami-Server/", "")
    Write-Host "[$i/$total] $short"
    git add "$file"
    git commit -m "Add $short"
    git push origin master
    "[$i/$total] Pushed: $short" | Out-File $logFile -Append
}
"Finished: $(Get-Date)" | Out-File $logFile -Append
Write-Host "DONE! All $total remaining files committed and pushed."
