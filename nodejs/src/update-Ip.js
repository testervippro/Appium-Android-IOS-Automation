const { exec } = require('child_process');
const os = require('os');
const fs = require('fs').promises;
const path = require('path');
const util = require('util');


const execAsync = util.promisify(exec);

async function getLocalIP() {
  const platform = os.platform();

  try {
    if (platform === 'win32') {
      const { stdout } = await execAsync('ipconfig');
      const match = stdout.match(/IPv4 Address[.\s]*:\s*(.*)/);
      if (match) return match[1].trim();
    } else if (platform === 'darwin') {
      try {
        const { stdout } = await execAsync('ipconfig getifaddr en0');
        return stdout.trim();
      } catch {
        const { stdout } = await execAsync('ipconfig getifaddr en1');
        return stdout.trim();
      }
    }
  } catch (err) {
    console.error('Error getting IP address:', err.message);
  }

  throw new Error(`Unsupported OS or failed to determine IP.`);
}

async function updateHubHostIfExists(ip) {
  const folder = path.join(__dirname, '../../grid3');

  try {
    const files = await fs.readdir(folder);
    const jsonFiles = files.filter(file => file.endsWith('.json'));

    for (const file of jsonFiles) {
      const filePath = path.join(folder, file);
      try {
        const content = await fs.readFile(filePath, 'utf-8');
        const data = JSON.parse(content);

        if (data?.configuration?.hubHost) {
          const oldIP = data.configuration.hubHost;
          data.configuration.hubHost = ip;

          await fs.writeFile(filePath, JSON.stringify(data, null, 2));
          console.log(` ${file}: hubHost updated from ${oldIP} to ${ip}`);
        } else {
          console.log(` ${file}: hubHost not found, skipped`);
        }
      } catch (err) {
        console.error(`Error processing ${file}:`, err.message);
      }
    }
  } catch (err) {
    console.error('Error reading grid3 folder:', err.message);
  }
}

(async () => {
  try {
    const ip = await getLocalIP();
    console.log(' Local IP:', ip);
    await updateHubHostIfExists(ip);
  } catch (err) {
    console.error(err.message);
  }
})();

