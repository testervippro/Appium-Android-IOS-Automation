# Run PowerShell as Administrator
# To allow script execution: Set-ExecutionPolicy RemoteSigned -Scope Process

# ========== JAVA INSTALLATION ==========

$javaUrl = "https://download.oracle.com/java/17/archive/jdk-17.0.12_windows-x64_bin.exe"
$javaInstaller = "$env:USERPROFILE\Downloads\jdk-17-installer.exe"
$javaHome = "C:\Program Files\Java\jdk-17"

# Re-download if installer is missing
if (-Not (Test-Path $javaInstaller)) {
    Write-Host "Downloading JDK installer..."
    Invoke-WebRequest -Uri $javaUrl -OutFile $javaInstaller
} else {
    Write-Host "JDK installer already exists."
}

# Install JDK silently
Start-Process -FilePath $javaInstaller -ArgumentList "/s" -NoNewWindow -Wait
Write-Host "JDK installation completed."

# Set JAVA_HOME
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", $javaHome, "Machine")
Write-Host "JAVA_HOME set to $javaHome"

# Add Java bin to PATH without duplicates
$javaBin = "$javaHome\bin"
$systemPath = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
$cleanPath = ($systemPath -split ";") | Where-Object { $_ -and ($_ -notlike "*jdk-17*") }
$newPath = ($cleanPath + $javaBin) -join ";"
[System.Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")
Write-Host "Java bin added to PATH"

# Confirm Java installation
Write-Host "`nJAVA VERSION:"
java -version


# ========== MAVEN INSTALLATION ==========

$mavenVersion = "3.9.9"
$mavenUrl = "https://dlcdn.apache.org/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$mavenZip = "$env:USERPROFILE\Downloads\apache-maven-$mavenVersion-bin.zip"
$mavenInstallDir = "C:\Program Files\Apache\maven-$mavenVersion"
$mavenExtracted = "$mavenInstallDir\apache-maven-$mavenVersion"
$mavenBin = "$mavenExtracted\bin"

# Re-download if zip is missing
if (-Not (Test-Path $mavenZip)) {
    Write-Host "Downloading Maven zip..."
    Invoke-WebRequest -Uri $mavenUrl -OutFile $mavenZip
} else {
    Write-Host "Maven zip already exists."
}

# Extract Maven zip
New-Item -ItemType Directory -Path $mavenInstallDir -Force | Out-Null
Expand-Archive -Path $mavenZip -DestinationPath $mavenInstallDir -Force
Write-Host "Maven extracted to $mavenInstallDir"

# Set MAVEN_HOME
[System.Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenExtracted, "Machine")
Write-Host "MAVEN_HOME set to $mavenExtracted"

# Add Maven bin to PATH without duplicates
$systemPath = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
$cleanPath = ($systemPath -split ";") | Where-Object { $_ -and ($_ -notlike "*apache-maven*") }
$newPath = ($cleanPath + $mavenBin) -join ";"
[System.Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")
Write-Host "Maven bin added to PATH"

# Confirm Maven installation
Write-Host "`nMAVEN VERSION:"
$mvnOutput = mvn -version 2>&1
if ($mvnOutput) {
    Write-Host $mvnOutput
} else {
    Write-Host "Maven installation failed. Please verify manually."
}
