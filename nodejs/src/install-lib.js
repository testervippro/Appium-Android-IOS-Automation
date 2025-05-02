const { exec } = require('child_process');
const util = require('util');
const execAsync = util.promisify(exec);

// Function to install a package if not installed
const installPackage = async (pkg) => {
  try {
    const { stdout } = await execAsync(`npm list -g ${pkg}`);
    if (!stdout.includes(pkg)) {
      await execAsync(`npm install -g ${pkg}`);
      console.log(`${pkg} installed.`);
    } else {
      console.log(`${pkg} is already installed.`);
    }
  } catch {
    await execAsync(`npm install -g ${pkg}`);
    console.log(`${pkg} installed.`);
  }
};

// Function to install or update Appium driver
const installDriver = async (driver) => {
  try {
    const { stdout } = await execAsync(`appium driver list --installed`);
    if (!stdout.includes(driver)) {
      await execAsync(`appium driver install ${driver}`);
      console.log(`${driver} driver installed.`);
    } else {
      console.log(`${driver} driver is already installed. You can update it if necessary.`);
    }
  } catch (err) {
    console.error('Error installing driver:', err.message);
  }
};

(async () => {
  await installPackage('appium');
  await installPackage('appium-doctor');
  console.log('Appium dependencies setup complete.');
})();
