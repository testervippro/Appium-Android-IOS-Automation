const path = require('path');
const fs = require('fs');
const os = require('os');
const { execSync } = require('child_process');

const home = os.homedir();
const baseDir = path.join(home, 'java');
const java17Dir = path.join(baseDir, 'jdk-17');
const archivePath = path.join(baseDir, 'jdk17.zip');
const downloadUrl = 'https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.10+7/OpenJDK17U-jdk_x64_windows_hotspot_17.0.10_7.zip';

// Helper: run command and return stdout
function run(cmd) {
  return execSync(cmd, { stdio: 'pipe' }).toString().trim();
}

// Step 1: Clean ALL Java-related env vars and Path entries
function cleanJavaEnv() {
  try {
    console.log('Removing JAVA_HOME and related Path entries...');
    
    // Remove JAVA_HOME
    execSync('reg delete HKCU\\Environment /v JAVA_HOME /f', { stdio: 'ignore' });

    // Remove old Java paths from Path
    const rawPath = run('reg query HKCU\\Environment /v Path');
    const currentPath = rawPath.split(/\s{4,}/).pop().trim();

    const cleanedPath = currentPath
      .split(';')
      .filter(p => !(p.toLowerCase().includes('java') && p.toLowerCase().includes('bin')))
      .join(';');

    execSync(`setx Path "${cleanedPath}"`);
    console.log('Environment cleaned.');
  } catch (err) {
    console.log('No existing JAVA_HOME or Java Path entries to clean.');
  }
}

// Step 2: Remove existing Java 17 install if present
function removeOldJava() {
  if (fs.existsSync(java17Dir)) {
    console.log('Removing previous Java 17 installation...');
    fs.rmSync(java17Dir, { recursive: true, force: true });
  }
}

// Step 3: Download Java 17 ZIP
function downloadJava() {
  console.log('Downloading Java 17...');
  execSync(`curl -L -o "${archivePath}" "${downloadUrl}"`, { stdio: 'inherit' });
}

// Step 4: Extract ZIP to target location
function extractJava() {
  console.log('Extracting...');
  execSync(`powershell -Command "Expand-Archive -Force '${archivePath}' '${baseDir}'"`);
  const extractedFolder = fs.readdirSync(baseDir).find(f => f.startsWith('jdk-17'));
  const extractedPath = path.join(baseDir, extractedFolder);
  if (extractedPath !== java17Dir) {
    fs.renameSync(extractedPath, java17Dir);
  }
  fs.unlinkSync(archivePath);
}

// Step 5: Set JAVA_HOME and update Path
function setJavaEnv() {
  const binPath = path.join(java17Dir, 'bin');
  execSync(`setx JAVA_HOME "${java17Dir}"`);
  execSync(`setx Path "${binPath};%Path%"`);
  console.log('JAVA_HOME and Path configured.');
}

// Step 6: Show Java version
function verifyJava() {
  try {
    const version = execSync(`"${path.join(java17Dir, 'bin', 'java.exe')}" -version`, {
      stdio: 'pipe',
    }).toString();
    console.log('Java installed successfully:\n' + version);
  } catch (err) {
    console.error('Failed to verify Java:', err.message);
  }
}

// Run all steps
function main() {
  if (!fs.existsSync(baseDir)) fs.mkdirSync(baseDir, { recursive: true });

  cleanJavaEnv();
  removeOldJava();
  downloadJava();
  extractJava();
  setJavaEnv();
  verifyJava();
}

main();
