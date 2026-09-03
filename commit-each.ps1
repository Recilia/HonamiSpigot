$files = git ls-files --others --exclude-standard
$total = $files.Count
$i = 0
foreach ($file in $files) {
    $i++
    $name = [System.IO.Path]::GetFileName($file)
    $dir = [System.IO.Path]::GetDirectoryName($file)
    Write-Host "[$i/$total] $file"
    git add "$file"
    git commit -m "Add $file"
    git push origin master
    Write-Host "--- Pushed $i/$total ---"
}
Write-Host "Done! All $total files committed and pushed."
