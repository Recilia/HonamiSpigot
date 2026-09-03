$files = git ls-files --others --exclude-standard
$total = $files.Count
$i = 0
$logFile = "C:\Users\Rein\Documents\ACENTRA STUFFS\Acentra-Github\HonamiSpigot\commit-log.txt"
"Started: $(Get-Date)" | Out-File $logFile
foreach ($file in $files) {
    $i++
    $short = $file
    Write-Host "[$i/$total] $short"
    git add "$file"
    if ($LASTEXITCODE -ne 0) { Write-Host "ADD FAILED: $file"; continue }
    git commit -m "Add $short"
    if ($LASTEXITCODE -ne 0) { Write-Host "COMMIT FAILED: $file"; continue }
    $pushed = $false
    while (-not $pushed) {
        git push origin master
        if ($LASTEXITCODE -eq 0) {
            $pushed = $true
        } else {
            Write-Host "  Push failed, pulling and retrying..."
            git pull origin master --rebase
            Start-Sleep -Seconds 2
        }
    }
    "[$i/$total] Pushed: $short" | Out-File $logFile -Append
}
"Finished: $(Get-Date)" | Out-File $logFile -Append
Write-Host "DONE! All $total files committed and pushed."
