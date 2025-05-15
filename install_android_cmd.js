const fs = require('fs');
const path = require('path');
const os = require('os');
const https = require('https');
const { execSync } = require('child_process');

// Define paths
const userHome = os.homedir();
const sdkPath = path.join(userHome, '.android_sdk');
const avdDir = path.join(userHome, '.android', 'avd');
const avdName = 'iphone';
const avdFolderPath = path.join(avdDir, `${avdName}.avd`);
const quickbootChoiceFile = path.join(avdFolderPath, 'quickbootChoice.ini');
const toolsZipUrl = 'https://dl.google.com/android/repository/commandlinetools-win-13114758_latest.zip';
const zipPath = path.join(__dirname, 'commandlinetools.zip');

// SDK tool paths
const toolsPath = path.join(sdkPath, 'cmdline-tools', 'latest', 'bin');
const sdkManagerCommand = path.join(toolsPath, 'sdkmanager.bat');
const avdManagerCommand = path.join(toolsPath, 'avdmanager.bat');
const emulatorPath = path.join(sdkPath, 'emulator', 'emulator.exe');
const platformToolsPath = path.join(sdkPath, 'platform-tools');
const systemImage = "system-images;android-33;google_apis_playstore;x86_64";

const platform = os.platform(); // 'win32', 'darwin', 'linux'


// Utility to run shell command
function runCommand(command, options = {}) {
    try {
        execSync(command, { stdio: 'inherit', ...options });
    } catch (err) {
        console.error(`Command failed: ${command}`);
        console.error(err.message);
        process.exit(1);
    }
}


// Download zip file
function downloadFile(url, destPath) {
    return new Promise((resolve, reject) => {
        const file = fs.createWriteStream(destPath);
        https.get(url, response => {
            if (response.statusCode !== 200) {
                return reject(new Error(`Failed to download file. Status code: ${response.statusCode}`));
            }
            response.pipe(file);
            file.on('finish', () => file.close(resolve));
        }).on('error', err => {
            fs.unlink(destPath, () => reject(err));
        });
    });
}


if (platform === 'darwin') {
    console.log('macOS detected. Running mac-specific install script...');
    runCommand('chmod +x dowload_android_cmd.sh');
    runCommand('./dowload_android_cmd.sh');
    process.exit(0);
}

async function main() {
    console.log('Starting Android SDK and AVD setup...');

    // Clean up old SDK and AVD
    if (fs.existsSync(sdkPath)) {
        console.log(`Removing existing SDK: ${sdkPath}`);
        fs.rmSync(sdkPath, { recursive: true, force: true });
    }
    if (fs.existsSync(avdFolderPath)) {
        console.log(`Removing existing AVD: ${avdFolderPath}`);
        fs.rmSync(avdFolderPath, { recursive: true, force: true });
    }
    if (fs.existsSync(quickbootChoiceFile)) {
        fs.unlinkSync(quickbootChoiceFile);
    }

    // Clear old env vars
    console.log('Clearing Android environment variables...');
    runCommand('setx ANDROID_HOME ""');
    runCommand('setx ANDROID_SDK_ROOT ""');

    // Download SDK tools
    console.log('Downloading command-line tools...');
    await downloadFile(toolsZipUrl, zipPath);
    console.log('Download complete.');

    // Create SDK dir and extract
    console.log('Creating SDK directory...');
    fs.mkdirSync(sdkPath, { recursive: true });

    console.log('Extracting tools...');
    if (!fs.existsSync(zipPath)) {
        console.error('Zip file not found!');
        process.exit(1);
    }
    runCommand(`powershell -Command "Expand-Archive -Path '${zipPath}' -DestinationPath '${sdkPath}' -Force"`);

    // Fix directory structure
    const cmdlineToolsPath = path.join(sdkPath, 'cmdline-tools');
    const latestPath = path.join(cmdlineToolsPath, 'latest');
    if (!fs.existsSync(latestPath)) {
        console.log('Fixing directory structure...');
        fs.mkdirSync(latestPath, { recursive: true });
        const files = fs.readdirSync(cmdlineToolsPath);
        for (const file of files) {
            if (file !== 'latest') {
                fs.renameSync(
                    path.join(cmdlineToolsPath, file),
                    path.join(latestPath, file)
                );
            }
        }
    }

    // Set temporary environment vars
    process.env.PATH = `${toolsPath};${platformToolsPath};${sdkPath}\\emulator;${process.env.PATH}`;
    process.env.ANDROID_HOME = sdkPath;
    process.env.ANDROID_SDK_ROOT = sdkPath;

    // Install SDK packages
    const packages = [
        "platforms;android-33",
        "build-tools;33.0.2",
        "platform-tools",
        "emulator",
        systemImage
    ];

    for (const pkg of packages) {
        console.log(`Installing: ${pkg}`);
        runCommand(`"${sdkManagerCommand}" --install "${pkg}"`);
    }

    // Accept licenses
    console.log('Accepting licenses...');
    runCommand(`"${sdkManagerCommand}" --licenses --sdk_root=${sdkPath}`, {
        input: 'y\n'.repeat(100),
        stdio: ['pipe', 'inherit', 'inherit']
    });

    // Set environment variables permanently
    console.log('Updating environment variables...');
    runCommand(`setx PATH "%PATH%;${platformToolsPath};${sdkPath}\\emulator"`);
    runCommand(`setx ANDROID_HOME "${sdkPath}"`);
    runCommand(`setx ANDROID_SDK_ROOT "${sdkPath}"`);

    // Create AVD
    console.log(`Creating AVD "${avdName}"...`);
    runCommand(`"${avdManagerCommand}" create avd -n ${avdName} --device pixel -k "${systemImage}" --force`);

    // Launch emulator
    console.log(`Launching emulator "${avdName}"...`);
    if (fs.existsSync(emulatorPath)) {
        runCommand(`"${emulatorPath}" -avd ${avdName}`);
    } else {
        console.error('Emulator binary not found.');
        process.exit(1);
    }
}

main();
