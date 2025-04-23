# Define URL and paths
$adbUrl = "https://dl.google.com/android/repository/platform-tools-latest-windows.zip"
$extractPath = "$env:USERPROFILE\platform-tools"

# Download and extract ADB
Invoke-WebRequest $adbUrl -OutFile "$env:USERPROFILE\Downloads\platform-tools.zip"
Expand-Archive "$env:USERPROFILE\Downloads\platform-tools.zip" -DestinationPath $env:USERPROFILE -Force

# Add to PATH if not already present
if (-not ($env:Path -contains $extractPath)) {
    [System.Environment]::SetEnvironmentVariable("Path", "$env:Path;$extractPath", "User")
}

# Check ADB version
& "$extractPath\adb.exe" version
