# Step 1–3: Prepare SDK Directory, Unzip SDK, and Set Environment Variables
Write-Host "Setting up Android SDK directory..."

# Define the paths for existing SDK and AVD directories
$existingSdkRoot = "$env:USERPROFILE\.android_sdk"
$existingAdv = "$env:USERPROFILE\.android"

# Remove existing Android SDK directory if it exists
if (Test-Path $existingSdkRoot) {
    Write-Host "Removing existing Android SDK directory..."
    Remove-Item -Recurse -Force $existingSdkRoot
}

# Remove existing AVD directory if it exists
if (Test-Path $existingAdv) {
    Write-Host "Removing existing Android AVD directory..."
    Remove-Item -Recurse -Force $existingAdv
}

# Create a new directory for the SDK
Write-Host "Creating new SDK directory..."
New-Item -ItemType Directory -Force -Path $existingSdkRoot

# Unzip Android SDK
Write-Host "Unzipping Android SDK..."
$zipPath = "android_sdk.zip"  # Assuming the zip file is in the current directory
if (Test-Path $zipPath) {
    Expand-Archive -Path $zipPath -DestinationPath $existingSdkRoot -Force
} else {
    Write-Host "Error: android_sdk.zip file not found in the current directory."
    exit
}

# Set Environment Variables
Write-Host "Setting environment variables..."

# Remove existing environment variables if they exist
[System.Environment]::SetEnvironmentVariable("ANDROID_SDK_ROOT", $null, [System.EnvironmentVariableTarget]::User)
[System.Environment]::SetEnvironmentVariable("ANDROID_HOME", $null, [System.EnvironmentVariableTarget]::User)

# Remove existing SDK paths from system PATH if they exist
$currentPath = [System.Environment]::GetEnvironmentVariable("Path", [System.EnvironmentVariableTarget]::User)
$androidPaths = @(
    "$env:ANDROID_HOME\cmdline-tools\latest\bin",
    "$env:ANDROID_HOME\platform-tools",
    "$env:ANDROID_HOME\emulator"
)

foreach ($path in $androidPaths) {
    if ($currentPath -contains $path) {
        Write-Host "Removing old SDK path: $path"
        $currentPath = $currentPath -replace [regex]::Escape($path), ""
    }
}

# Set the environment variables
[System.Environment]::SetEnvironmentVariable("ANDROID_SDK_ROOT", $existingSdkRoot, [System.EnvironmentVariableTarget]::User)
[System.Environment]::SetEnvironmentVariable("ANDROID_HOME", $existingSdkRoot, [System.EnvironmentVariableTarget]::User)

# Update system PATH for the current session
$env:PATH = "$env:ANDROID_HOME\cmdline-tools\latest\bin;$env:ANDROID_HOME\platform-tools;$env:ANDROID_HOME\emulator;$env:PATH"

# Set the PATH environment variable permanently (for future sessions)
if ($currentPath -notlike "*$env:ANDROID_HOME\cmdline-tools\latest\bin*") {
    $newPath = "$env:ANDROID_HOME\cmdline-tools\latest\bin;$env:ANDROID_HOME\platform-tools;$env:ANDROID_HOME\emulator;$currentPath"
    [System.Environment]::SetEnvironmentVariable("Path", $newPath, [System.EnvironmentVariableTarget]::User)
    Write-Host "Updated system PATH."
} else {
    Write-Host "PATH already includes Android tools."
}

# Reload the profile (you can restart your shell for full effect)
Write-Host "Reloading environment variables. Please restart your PowerShell or open a new session to apply changes."

Write-Host "Android SDK setup complete."
