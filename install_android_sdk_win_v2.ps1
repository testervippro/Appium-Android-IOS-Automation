# Set variables
$androidSdkRoot = "$env:USERPROFILE\.android_sdk"
$avdDir = "$env:USERPROFILE\.android\avd"
$cmdlineToolsDir = "$androidSdkRoot\cmdline-tools\latest"
$zipPath = "commandlinetools-win-9477386_latest.zip"  # Adjust this filename if needed

# Step 1: Clean up existing Android SDK and AVD files
Write-Host "Cleaning up old Android SDK and AVD files..."

if (Test-Path $androidSdkRoot) {
    Remove-Item -Recurse -Force $androidSdkRoot
    Write-Host "Removed SDK directory: $androidSdkRoot"
} else {
    Write-Host "No SDK directory found to delete."
}

if (Test-Path $avdDir) {
    Remove-Item -Recurse -Force "$avdDir\*"
    Write-Host "Cleared AVD directory: $avdDir"
} else {
    Write-Host "No AVD directory found."
}

# Step 2: Extract command line tools
Write-Host "Extracting Android command line tools..."
New-Item -ItemType Directory -Force -Path $cmdlineToolsDir
Expand-Archive -Path $zipPath -DestinationPath "$cmdlineToolsDir\tmp"
Move-Item "$cmdlineToolsDir\tmp\cmdline-tools\*" $cmdlineToolsDir
Remove-Item -Recurse -Force "$cmdlineToolsDir\tmp"

# Step 3: Set environment variables permanently
Write-Host "Setting permanent environment variables..."
setx ANDROID_SDK_ROOT $androidSdkRoot /M
setx ANDROID_HOME $androidSdkRoot /M

# Update system PATH
$currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
$newPaths = "$cmdlineToolsDir\bin;$androidSdkRoot\platform-tools;$androidSdkRoot\emulator"
if ($currentPath -notlike "*$cmdlineToolsDir\bin*") {
    $updatedPath = "$newPaths;$currentPath"
    setx PATH $updatedPath /M
    Write-Host "Updated system PATH."
} else {
    Write-Host "PATH already includes Android tools."
}

# Step 4: Install SDK packages
Write-Host "Installing SDK packages..."
& "$cmdlineToolsDir\bin\sdkmanager.bat" --sdk_root="$androidSdkRoot" --licenses | Out-Null
& "$cmdlineToolsDir\bin\sdkmanager.bat" --sdk_root="$androidSdkRoot" `
    "cmdline-tools;latest" `
    "platform-tools" `
    "build-tools;33.0.2" `
    "platforms;android-33" `
    "emulator" `
    "system-images;android-33;google_apis_playstore;x86_64"

# Step 5: Create AVD if it doesn't exist
Write-Host "Creating AVD..."
$avdList = & "$cmdlineToolsDir\bin\avdmanager.bat" list avd
if ($avdList -notmatch "Name: iphone") {
    echo "no" | & "$cmdlineToolsDir\bin\avdmanager.bat" create avd -n iphone `
        --device "pixel" `
        -k "system-images;android-33;google_apis_playstore;x86_64"
    Write-Host "AVD 'iphone' created."
} else {
    Write-Host "AVD 'iphone' already exis
