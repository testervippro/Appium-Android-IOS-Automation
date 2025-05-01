const { exec } = require('child_process');
const os = require('os');

function getIPAddress() {
  const platform = os.platform();

  if (platform === 'win32') {
    // Windows: use ipconfig
    exec('ipconfig', (err, stdout, stderr) => {
      if (err) {
        console.error('Error executing ipconfig:', err);
        return;
      }

      const lines = stdout.split('\n');
      for (const line of lines) {
        const match = line.match(/IPv4 Address[.\s]*:\s*(.*)/);
        if (match) {
          console.log('Your IP address (Windows):', match[1].trim());
          return;
        }
      }

      console.log('No IPv4 address found.');
    });

  } else if (platform === 'darwin') {
    // macOS: use ipconfig getifaddr
    exec('ipconfig getifaddr en0', (err, stdout, stderr) => {
      if (!err && stdout) {
        console.log('Your IP address (macOS):', stdout.trim());
      } else {
        console.log('Trying en1...');
        exec('ipconfig getifaddr en1', (err2, stdout2) => {
          if (!err2 && stdout2) {
            console.log('Your IP address (macOS):', stdout2.trim());
          } else {
            console.error('Could not determine IP address on macOS.');
          }
        });
      }
    });

  } else {
    console.log('Unsupported OS:', platform);
  }
}

getIPAddress();
