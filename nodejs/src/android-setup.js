
const fs = require('fs');
const os = require('os');
const path = require('path');
const { execSync } = require('child_process');

const home = os.homedir();
const isWin = os.platform() === 'win32';
const sdkDir = isWin
  ? path.join(process.env.LOCALAPPDATA, 'Android', 'Sdk')
  : path.join(home, 'Library', 'Android', 'sdk');
const toolsSubpath = 'platform-tools';
const toolsPath = path.join(sdkDir, toolsSubpath);

// Helper to log success of a registry or file update
function logSuccess(message) {
  console.log(` Success: ${message}`);
}

// ---------- WINDOWS ----------
function setupWindows() {
  try {
    console.log('Overwriting ANDROID_HOME →', sdkDir);
    execSync(`setx ANDROID_HOME "${sdkDir}"`);
    // Verify ANDROID_HOME was set
    const newHome = execSync('reg query HKCU\\Environment /v ANDROID_HOME', { stdio: 'pipe' })
      .toString()
      .split('\n')
      .find(line => line.includes('ANDROID_HOME')) || '';
    console.log('ANDROID_HOME now:', newHome.split(/\s{4,}/).pop().trim());
    logSuccess('ANDROID_HOME updated in registry');

    // Read current user-level Path
    const raw = execSync('reg query HKCU\\Environment /v Path', { stdio: 'pipe' })
      .toString()
      .split('\n')
      .find(line => line.includes('Path')) || '';
    const currentPath = raw.split(/\s{4,}/).pop().trim();

    // Remove old platform-tools entries and append fresh one
    const parts = currentPath.split(';')
      .map(p => p.trim())
      .filter(p => p && !p.toLowerCase().includes(toolsPath.toLowerCase()));
    parts.push(toolsPath.replace(/\//g, '\\'));
    const newPath = parts.join(';');

    console.log('Overwriting Path →', newPath);
    execSync(`setx Path "${newPath}"`);
    // Verify Path was set
    const updatedRaw = execSync('reg query HKCU\\Environment /v Path', { stdio: 'pipe' })
      .toString()
      .split('\n')
      .find(line => line.includes('Path')) || '';
    console.log('Path now ends with:', updatedRaw.split(/\s{4,}/).pop().trim().split(';').slice(-1)[0]);
    logSuccess('Path updated in registry');

    // Print effective user env and adb version
    const envHome = execSync('cmd /c "echo %ANDROID_HOME%"').toString().trim();
    const envPath = execSync('cmd /c "echo %Path%"').toString().trim();
    console.log('User-level ANDROID_HOME:', envHome);
    console.log('User-level PATH snippet:', envPath.split(';').slice(-3).join(';'));
    try {
      const adbVer = execSync('adb version', { stdio: 'pipe' }).toString().trim();
      console.log('adb version:', adbVer);
    } catch (e) {
      console.error('Failed to run adb version:', e.message);
    }

    console.log('\n Windows user env overwritten. Restart shell or sign out/in to apply.');
  } catch (err) {
    console.error(' Windows setup failed:', err.message);
  }
}

// ---------- UNIX (macOS/Linux) ----------
function setupUnix() {
  const shell = process.env.SHELL || '';
  const rcFile = shell.includes('bash')
    ? path.join(home, '.bashrc')
    : path.join(home, '.zshrc');

  const markerStart = '# >>> Android SDK setup >>>';
  const markerEnd = '# <<< Android SDK setup <<<';
  const exportLines = [
    `export ANDROID_HOME=\"${sdkDir}\"`,
    `export PATH=\"$ANDROID_HOME/${toolsSubpath}:$PATH\"`
  ].join('\n');

  let rcContent = '';
  try {
    rcContent = fs.readFileSync(rcFile, 'utf8');
  } catch {
    // file may not exist yet
  }

  // Remove existing block if present
  const blockPattern = new RegExp(
    `\\n?${markerStart}[\\s\\S]*?${markerEnd}\\n?`, 'g'
  );
  const cleaned = rcContent.replace(blockPattern, '');

  // Always append fresh block
  const block = `\n${markerStart}\n${exportLines}\n${markerEnd}\n`;
  fs.writeFileSync(rcFile, cleaned + block, 'utf8');

  // Print exports and check adb version
  console.log(` macOS/Linux config overwritten in ${rcFile}`);
  console.log('Current ANDROID_HOME:', sdkDir);
  console.log('PATH snippet:', `\$ANDROID_HOME/${toolsSubpath}:$PATH`);
  try {
    const adbVer = execSync('adb version', { stdio: 'pipe' }).toString().trim();
    console.log('adb version:', adbVer);
  } catch (e) {
    console.error('Failed to run adb version:', e.message);
  }
  console.log(' Run `source', rcFile, '` or restart your shell for changes.');
}

// ---------- RUN ----------
if (isWin) {
  setupWindows();
} else {
  setupUnix();
}
