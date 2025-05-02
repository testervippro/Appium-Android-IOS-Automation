const { exec } = require('child_process');

// Function to kill process based on port
function killPort(port) {
  const platform = process.platform;

  let command;

  if (platform === 'win32') {
    // Windows command to kill the process on the given port
    command = `netstat -ano | findstr :${port} | findstr LISTENING | for /f "tokens=5" %a in ('findstr :${port}') do taskkill /PID %a`;
  } else {
    // macOS/Linux command to kill the process on the given port
    command = `lsof -t -i:${port} | xargs kill -9`;
  }

  exec(command, (error, stdout, stderr) => {
    if (error) {
      console.error(`Error: ${error.message}`);
      return;
    }
    if (stderr) {
      console.error(`stderr: ${stderr}`);
      return;
    }
    console.log(`Port ${port} killed successfully!`);
  });
}

// Example usage: kill the process on port 3000
killPort(4444);
killPort(4723);
killPort(4727);
killPort(9999);
