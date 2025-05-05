
# Android SDK Setup Script

This PowerShell script automates the installation and setup of the Android SDK, including setting the necessary environment variables and installing required SDK components.

## Script Steps run as Admin

```powershell
# Step 1: Create SDK directory
$env:ANDROID_SDK_ROOT = "$HOME\.android_sdk"
$env:ANDROID_HOME = $env:ANDROID_SDK_ROOT
New-Item -ItemType Directory -Force -Path $env:ANDROID_SDK_ROOT

# Step 2: Unzip the command line tools
Expand-Archive -Path "appium-base-setup\commandlinetools-win-13114758_latest.zip" -DestinationPath $env:ANDROID_SDK_ROOT

# Step 3: Move tools to the 'latest' directory (required for sdkmanager to function)
New-Item -ItemType Directory -Force -Path "$env:ANDROID_SDK_ROOT\cmdline-tools\latest"
Copy-Item -Recurse -Force -Path "$env:ANDROID_SDK_ROOT\cmdline-tools" -Destination "$env:ANDROID_SDK_ROOT\cmdline-tools\latest"

# Step 4: Update PATH for current session
$toolsPath = "$env:ANDROID_SDK_ROOT\cmdline-tools\latest\bin"
$emulatorPath = "$env:ANDROID_SDK_ROOT\emulator"
$env:PATH = "$toolsPath;$emulatorPath;$env:PATH"

# Step 5: Set permanent environment variables
[System.Environment]::SetEnvironmentVariable("ANDROID_SDK_ROOT", $env:ANDROID_SDK_ROOT, [System.EnvironmentVariableTarget]::User)
[System.Environment]::SetEnvironmentVariable("ANDROID_HOME", $env:ANDROID_HOME, [System.EnvironmentVariableTarget]::User)

$oldPath = [System.Environment]::GetEnvironmentVariable("Path", [System.EnvironmentVariableTarget]::User)
$newPaths = "$toolsPath;$emulatorPath"
if ($oldPath -notlike "*$toolsPath*") {
    [System.Environment]::SetEnvironmentVariable("Path", "$newPaths;$oldPath", [System.EnvironmentVariableTarget]::User)
}

# Step 6: Install Android SDK components
$sdkmanager = "$toolsPath\sdkmanager.bat"

& $sdkmanager --sdk_root=$env:ANDROID_SDK_ROOT "cmdline-tools;latest"
& $sdkmanager "platforms;android-33" "build-tools;33.0.2"
& $sdkmanager "platform-tools" "tools"
& $sdkmanager "emulator"
echo "y" | & $sdkmanager --licenses
& $sdkmanager "system-images;android-33;google_apis_playstore;x86_64"

Write-Host "Android SDK setup complete. Please restart PowerShell or your system to apply PATH changes."
````

## Execution Steps

1. **Download** the command line tools and unzip them to the desired directory.
2. **Run the PowerShell script** to install necessary SDK components.
3. **Restart PowerShell or your system** to apply the environment variable changes.

After running this script, the Android SDK will be set up and ready to use on your Windows machin
