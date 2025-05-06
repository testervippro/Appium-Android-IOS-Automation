# Set environment variables
$androidSdkRoot = "$env:USERPROFILE\.android_sdk"
$zipPath = "android_sdk.zip"  # Assuming the zip file is in the current directory

# Step 1–3: Prepare SDK Directory, Unzip SDK, and Set Environment Variables
Write-Host "Setting up Android SDK directory..."
New-Item -ItemType Directory -Force -Path $androidSdkRoot

Write-Host "Unzipping Android SDK..."
Expand-Archive -Path $zipPath -DestinationPath $androidSdkRoot -Force

Write-Host "Setting environment variables..."
[System.Environment]::SetEnvironmentVariable("ANDROID_SDK_ROOT", $androidSdkRoot, [System.EnvironmentVariableTarget]::User)
[System.Environment]::SetEnvironmentVariable("ANDROID_HOME", $androidSdkRoot, [System.EnvironmentVariableTarget]::User)

# Update system PATH for the current session
$env:PATH = "$env:ANDROID_HOME\cmdline-tools\latest\bin;$env:ANDROID_HOME\platform-tools;$env:ANDROID_HOME\emulator;$env:PATH"

# Set the PATH environment variable permanently (for future sessions)
$currentPath = [System.Environment]::GetEnvironmentVariable("Path", [System.EnvironmentVariableTarget]::User)
$newPath = "$env:ANDROID_HOME\cmdline-tools\latest\bin;$env:ANDROID_HOME\platform-tools;$env:ANDROID_HOME\emulator"
if ($currentPath -notlike "*$newPath*") {
    [System.Environment]::SetEnvironmentVariable("Path", "$newPath;$currentPath", [System.EnvironmentVariableTarget]::User)
    Write-Host "Updated system PATH."
} else {
    Write-Host "PATH already includes Android tools."
}

# Reload the profile (you can restart your shell for full effect)
Write-Host "Reloading environment variables. Please restart your PowerShell or open a new session to apply changes."

Write-Host "Android SDK setup complete."
