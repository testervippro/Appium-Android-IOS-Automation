const fs = require('fs');
const path = require('path');
const os = require('os');
const { execSync } = require('child_process');

// Setup paths

const userHome = os.homedir();
const sdkPath = path.join(userHome, '.android_sdk');
const avdDir = path.join(userHome, '.android', 'avd');
const avdName = 'iphone';
const avdFolderPath = path.join(avdDir, `${avdName}.avd`);
const quickbootChoiceFile = path.join(avdFolderPath, 'quickbootChoice.ini');

///  Dowload
// URL to download command-line tools
const commandLineToolsUrl = 'https://dl.google.com/android/repository/commandlinetools-win-13114758_latest.zip';

const zipPath = path.join(__dirname, 'commandlinetools-win-13114758_latest.zip');
// Step 1: Download command-line tools zip
console.log("Downloading SDK tools...");
const downloadFile = (url, destinationPath) => {
    return new Promise((resolve, reject) => {
        const file = fs.createWriteStream(destinationPath);
        https.get(url, (response) => {
            if (response.statusCode !== 200) {
                reject(new Error(`Failed to get the file: ${response.statusCode}`));
            }
            response.pipe(file);
            file.on('finish', () => {
                file.close(resolve);
            });
        }).on('error', (err) => {
            fs.unlink(destinationPath, () => reject(err));
        });
    });
};
downloadFile(commandLineToolsUrl, zipPath);
console.log("Finisheds SDK tools...");


const toolsPath = path.join(sdkPath, 'cmdline-tools', 'latest', 'bin');
const sdkManagerCommand = path.join(toolsPath, 'sdkmanager.bat');
const avdManagerCommand = path.join(toolsPath, 'avdmanager.bat');
const emulatorPath = path.join(sdkPath, 'emulator', 'emulator.exe');
const platformToolsPath = path.join(sdkPath, 'platform-tools');
const systemImage = "system-images;android-33;google_apis_playstore;x86_64";

// Function to execute shell commands with error handling
function runCommand(command) {
    try {
        execSync(command, { stdio: 'inherit' });
    } catch (error) {
        console.error(`Command failed: ${command}`, error.message);
        process.exit(1);
    }
}

// Kill adb processes if any
console.log('Killing adb processes...');
runCommand('taskkill /F /IM adb.exe');

// Remove existing SDK and AVD
if (fs.existsSync(sdkPath)) {
    console.log(`Removing existing SDK directory: ${sdkPath}`);
    fs.rmSync(sdkPath, { recursive: true, force: true });
}

if (fs.existsSync(avdFolderPath)) {
    console.log(`Removing existing AVD folder: ${avdFolderPath}`);
    fs.rmSync(avdFolderPath, { recursive: true, force: true });
}

if (fs.existsSync(quickbootChoiceFile)) {
    console.log(`Removing quickbootChoice.ini: ${quickbootChoiceFile}`);
    fs.unlinkSync(quickbootChoiceFile);
}

// Clear environment variables
console.log('Clearing environment variables...');
try {
    execSync('setx ANDROID_HOME ""');
    execSync('setx ANDROID_SDK_ROOT ""');
} catch (error) {
    console.warn('Failed to clear environment variables.');
}

// Create SDK directory
console.log('Creating SDK directory...');
fs.mkdirSync(sdkPath, { recursive: true });

// Extract the SDK zip file
console.log('Extracting SDK zip file...');
runCommand(`powershell -Command "Expand-Archive -Path '${zipPath}' -DestinationPath '${sdkPath}' -Force"`);

// Fix directory structure for cmdline-tools
const cmdlineToolsPath = path.join(sdkPath, 'cmdline-tools');
const latestPath = path.join(cmdlineToolsPath, 'latest');
if (!fs.existsSync(latestPath)) {
    console.log('Fixing cmdline-tools directory structure...');
    fs.mkdirSync(latestPath, { recursive: true });
    const files = fs.readdirSync(cmdlineToolsPath);
    files.forEach(file => {
        if (file !== 'latest') {
            fs.renameSync(
                path.join(cmdlineToolsPath, file),
                path.join(latestPath, file)
            );
        }
    });
}

// Set environment variables temporarily
process.env.PATH = `${toolsPath};${platformToolsPath};${sdkPath}\\emulator;${process.env.PATH}`;
process.env.ANDROID_HOME = sdkPath;
process.env.ANDROID_SDK_ROOT = sdkPath;

// Install required SDK packages
const installPackages = [
    "platforms;android-33",
    "build-tools;33.0.2",
    "platform-tools",
    "emulator",
    systemImage
];

installPackages.forEach(pkg => {
    console.log(`Installing package: ${pkg}`);
    runCommand(`"${sdkManagerCommand}" --install "${pkg}"`);
});

// Accept SDK licenses
console.log('Accepting SDK licenses...');
runCommand(`"${sdkManagerCommand}" --licenses --sdk_root=${sdkPath}`, {
    input: 'y\n'.repeat(100),
    stdio: ['pipe', 'inherit', 'inherit']
});

// Update environment variables permanently
console.log('Updating system environment variables...');
const setxCommands = [
    `setx PATH "%PATH%;${platformToolsPath};${emulatorPath}"`,
    `setx ANDROID_HOME "${sdkPath}"`,
    `setx ANDROID_SDK_ROOT "${sdkPath}"`
];

setxCommands.forEach(command => runCommand(command));

console.log('Successfully updated environment variables.');

// Remove any existing AVD folder before creating a new one
if (fs.existsSync(avdFolderPath)) {
    console.log(`Removing existing AVD folder: ${avdFolderPath}`);
    fs.rmSync(avdFolderPath, { recursive: true, force: true });
}

// Create a new AVD
console.log(`Creating AVD with name: ${avdName}...`);
runCommand(`"${avdManagerCommand}" create avd -n ${avdName} --device pixel -k "${systemImage}" --force`);

// Launch the AVD
console.log(`Launching AVD: ${avdName}...`);
if (fs.existsSync(emulatorPath)) {
    runCommand(`"${emulatorPath}" -avd ${avdName}`);
} else {
    console.error('Emulator not found. Ensure that the emulator package is installed.');
    process.exit(1);
}
