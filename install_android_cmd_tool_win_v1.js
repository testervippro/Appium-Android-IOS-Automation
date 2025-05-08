const { execSync } = require('child_process');
const path = require('path');
require('./uninstall_androdi_cmd.js');

// Detect user profile
const userProfile = process.env.USERPROFILE || process.env.HOMEPATH;
const sdkRoot = path.join(userProfile, '.android-sdk');
const avdDir = path.join(userProfile, '.android');
const zipPath = './android_sdk.zip';
const systemImage = "system-images;android-33;google_apis_playstore;x86_64";

// Paths to executables
const sdkManager = `"${sdkRoot}\\cmdline-tools\\latest\\bin\\sdkmanager.bat"`;
const avdManager = `"${sdkRoot}\\cmdline-tools\\latest\\bin\\avdmanager.bat"`;
const emulatorPath = `"${sdkRoot}\\emulator\\emulator.exe"`;
const adbPath = `"${sdkRoot}\\platform-tools\\adb.exe"`;

// Kill ADB
function killAdbProcesses() {
  console.log('Killing adb processes...');
  try {
    execSync('taskkill /f /im adb.exe', { stdio: 'ignore' });
    console.log('ADB killed.');
  } catch {
    console.log('No adb process running.');
  }
}

// Remove directory using PowerShell
function removeDirIfExists(dirPath) {
  try {
    execSync(`powershell -Command "if (Test-Path '${dirPath}') { Remove-Item -Path '${dirPath}' -Recurse -Force }"`);
    console.log(`Removed: ${dirPath}`);
  } catch (err) {
    console.error(`Failed to remove ${dirPath}: ${err.message}`);
  }
}

// Create directory using PowerShell
function createDir(dirPath) {
  execSync(`powershell -Command "New-Item -ItemType Directory -Force -Path '${dirPath}'"`);
}

// Unzip using PowerShell and move contents up
function unzipSdk(callback) {
  if (!require('fs').existsSync(zipPath)) {
    console.error(' android_sdk.zip not found.');
    process.exit(1);
  }

  console.log(' Extracting SDK...');
  const tempExtract = path.join(sdkRoot, '_temp_extract');

  try {
    // Extract to temp folder
    execSync(`powershell -Command "Expand-Archive -Path '${zipPath}' -DestinationPath '${tempExtract}' -Force"`);

    // Get top-level folder name
    const folderName = execSync(`powershell -Command "(Get-ChildItem -Path '${tempExtract}' | Where-Object { $_.PSIsContainer }).Name"`).toString().trim();
    const extractedPath = path.join(tempExtract, folderName);

    // Move each item up to sdkRoot
    execSync(`powershell -Command "Get-ChildItem '${extractedPath}' | ForEach-Object { Move-Item $_.FullName -Destination '${sdkRoot}' -Force }"`);

    // Clean up temp
    execSync(`powershell -Command "Remove-Item '${tempExtract}' -Recurse -Force"`);

    console.log(` SDK extracted to ${sdkRoot}`);
    callback();
  } catch (err) {

    console.error(' Unzip failed:', err.message);
    process.exit(1);
  }
}

// Clean old Android SDK paths from PATH
function cleanPath() {
  const toRemove = [
    `${sdkRoot}\\cmdline-tools\\latest\\bin`,
    `${sdkRoot}\\platform-tools`,
    `${sdkRoot}\\emulator`
  ];
  process.env.PATH = process.env.PATH
    .split(';')
    .filter(p => !toRemove.includes(p))
    .join(';');
}

// Set environment variables
function setEnvVariables() {
  process.env.ANDROID_SDK_ROOT = sdkRoot;
  process.env.ANDROID_HOME = sdkRoot;
  process.env.PATH = [
    `${sdkRoot}\\cmdline-tools\\latest\\bin`,
    `${sdkRoot}\\platform-tools`,
    `${sdkRoot}\\emulator`,
    process.env.PATH
  ].join(';');
  console.log(' Environment variables set for current session.');
}

// Update system PATH using PowerShell
function updateSystemPath() {
  try {
    const newPaths = [
      `${sdkRoot}\\cmdline-tools\\latest\\bin`,
      `${sdkRoot}\\platform-tools`,
      `${sdkRoot}\\emulator`
    ];
    const updated = newPaths.join(';');
    execSync(`powershell -Command "[Environment]::SetEnvironmentVariable('Path', '${updated};' + [Environment]::GetEnvironmentVariable('Path', 'User'), 'User')"`);
    console.log(' System PATH updated.');
  } catch (err) {
    console.error(' Failed to update system PATH:', err.message);
  }
}

// Check ADB version
function checkAdbVersion() {
  try {
    console.log('🔍 Checking ADB version...');
    const output = execSync(`${adbPath} version`, { encoding: 'utf-8' });
    console.log(output.trim());
  } catch (err) {
    console.error(' ADB version check failed:', err.message);
  }
}

// Main execution
function main() {
  console.log('⚙️ Preparing Android SDK setup...');

  killAdbProcesses();
  removeDirIfExists(sdkRoot);
  removeDirIfExists(avdDir);
  createDir(sdkRoot);

  cleanPath();
  setEnvVariables();

  unzipSdk(() => {
    updateSystemPath();
    checkAdbVersion();
    console.log('🎉 Android SDK setup complete. Restart terminal to apply changes.');
  });
}

const fs = require('fs');

// Test functions
function checkSdkInstallationStatus(label) {
  console.log(`\n📦 Checking SDK status: ${label}`);

  const checks = [
    { name: 'SDK Root', path: sdkRoot },
    { name: 'ADB', path: adbPath },
    { name: 'Emulator', path: emulatorPath },
    { name: 'SDK Manager', path: sdkManager },
    { name: 'AVD Manager', path: avdManager },
  ];

  let allGood = true;

  checks.forEach(check => {
    if (fs.existsSync(check.path)) {
      console.log(`✅ ${check.name} found at: ${check.path}`);
    } else {
      console.log(`❌ ${check.name} missing at: ${check.path}`);
      allGood = false;
    }
  });

  const envVars = ['ANDROID_SDK_ROOT', 'ANDROID_HOME'];
  envVars.forEach(varName => {
    if (process.env[varName]) {
      console.log(` ${varName} is set to: ${process.env[varName]}`);
    } else {
      console.log(` ${varName} is not set.`);
      allGood = false;
    }
  });

  console.log(allGood ? '✅ All checks passed.' : '⚠️ Some checks failed.');
  return allGood;
}

// Main execution with before/after tests
console.log('===  PRE-SETUP CHECK ===');
checkSdkInstallationStatus('Before Setup');

main();
