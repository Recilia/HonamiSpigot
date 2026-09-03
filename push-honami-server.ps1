$files = git ls-files --others --exclude-standard "Honami-Server/"
$total = $files.Count
$i = 0
$logFile = "C:\Users\Rein\Documents\ACENTRA STUFFS\Acentra-Github\HonamiSpigot\commit-log.txt"
"Started: $(Get-Date)" | Out-File $logFile
foreach ($file in $files) {
    $i++
    $short = $file.Replace("Honami-Server/src/main/java/", "").Replace("Honami-Server/src/test/java/", "").Replace("Honami-Server/src/main/resources/", "").Replace("Honami-Server/", "")
    Write-Host "[$i/$total] $short"
    git add "$file"
    git commit -m "Add $short"
    git push origin master
    "[$i/$total] Pushed: $short" | Out-File $logFile -Append
}
"Finished: $(Get-Date)" | Out-File $logFile -Append
Write-Host "DONE! All $total Honami-Server files committed and pushed."
