const { exec } = require('child_process');
const net = require('net');

// Function to execute commands in separate processes
function runCommand(command, callback) {
  exec(command, (error, stdout, stderr) => {
    if (error) {
      console.error(`Error executing command: ${stderr}`);
    } else {
      console.log(stdout);
    }
    if (callback) callback();
  });
}

// Check if a port is available
function isPortInUse(port, callback) {
  const server = net.createServer().listen(port, '0.0.0.0');
  server.on('listening', () => {
    server.close();
    callback(false); // Port is available
  });
  server.on('error', () => {
    callback(true); // Port is in use
  });
}

// 1. Start Selenium Hub
function startSeleniumHub() {
  console.log("Starting Selenium Hub...");
  runCommand("java -jar grid3/selenium.jar -role hub -hubConfig grid3/grid.json", () => {
    console.log("Selenium Hub started.");
  });
}

// 2. Register Android Node (with port check)
function registerAndroidNode() {
  const port = 4723;
  console.log(`Checking if port ${port} is available for Android Node...`);

  isPortInUse(port, (inUse) => {
    if (inUse) {
      console.log(`Port ${port} is already in use. Trying a different port...`);
      // You can choose an alternative port here, for example, 4728
      const newPort = 4728;
      console.log(`Registering Android Node with port ${newPort}...`);
      runCommand(`appium --nodeconfig grid3/android.json --base-path=/wd/hub --port ${newPort}`, () => {
        console.log(`Android Node registered on port ${newPort}.`);
      });
    } else {
      console.log(`Port ${port} is available. Registering Android Node...`);
      runCommand(`appium --nodeconfig grid3/android.json --base-path=/wd/hub --port ${port}`, () => {
        console.log("Android Node registered.");
      });
    }
  });
}

// 3. Register iOS Node (with port check)
function registerIOSNode() {
  const port = 4727;
  console.log(`Checking if port ${port} is available for iOS Node...`);

  isPortInUse(port, (inUse) => {
    if (inUse) {
      console.log(`Port ${port} is already in use. Trying a different port...`);
      // You can choose an alternative port here, for example, 4729
      const newPort = 4729;
      console.log(`Registering iOS Node with port ${newPort}...`);
      runCommand(`appium --nodeconfig grid3/ios.json --base-path=/wd/hub --port ${newPort}`, () => {
        console.log(`iOS Node registered on port ${newPort}.`);
      });
    } else {
      console.log(`Port ${port} is available. Registering iOS Node...`);
      runCommand(`appium --nodeconfig grid3/ios.json --base-path=/wd/hub --port ${port}`, () => {
        console.log("iOS Node registered.");
      });
    }
  });
}

// Start the process
startSeleniumHub();
registerAndroidNode();
registerIOSNode();
