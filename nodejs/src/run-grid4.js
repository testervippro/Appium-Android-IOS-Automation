const fs = require('fs');
const path = require('path');
const express = require('express');
const WebSocket = require('ws');
const spawn = require('cross-spawn');
const kill = require('tree-kill');

const app = express();
const logDir = path.join(__dirname, 'logs');
const publicDir = path.join(__dirname, 'public');

const folder = path.join(__dirname, '../../grid4');

// Commands for each process

const commands = [
  {
    name: 'hub',
    cmd: 'java',
    args: ['-jar', path.join(folder, 'selenium-server-4.12.0.jar'), 'hub'],
    color: 'green'
  },
  {
    name: 'appium_android',
    cmd: 'appium',
    args: ['--config', path.join(folder, 'appium1.yml')],
    color: 'cyan'
  },
  {
    name: 'node_android',
    cmd: 'java',
    args: ['-jar', path.join(folder, 'selenium-server-4.12.0.jar'), 'node', '--config', path.join(folder, 'node1.toml')],
    color: 'cyan'
  },
  {
      name: 'appium_ios',
      cmd: 'appium',
      args: ['--config', path.join(folder, 'appium2.yml')],
      color: 'cyan'
    },
    {
      name: 'node_ios',
      cmd: 'java',
      args: ['-jar', path.join(folder, 'selenium-server-4.12.0.jar'), 'node', '--config', path.join(folder, 'node2.toml')],
      color: 'cyan'
    }
];

const logHistory = commands.reduce((history, command) => {
  history[command.name] = [];
  return history;
}, {});

const clients = new Set();
const processes = [];

// Directory setup
if (!fs.existsSync(logDir)) fs.mkdirSync(logDir);
if (!fs.existsSync(publicDir)) fs.mkdirSync(publicDir);

// Delete old log files
function deleteLogFiles() {
  fs.readdir(logDir, (err, files) => {
    if (err) {
      console.error('Error reading log directory:', err);
      return;
    }
    files.forEach(file => {
      const filePath = path.join(logDir, file);
      fs.unlink(filePath, (err) => {
        if (err) {
          console.error(`Error deleting file ${file}:`, err);
        } else {
          console.log(`Deleted log file: ${file}`);
        }
      });
    });
  });
}

// Delete log files before setting up static routes
deleteLogFiles();

// Serve static files and logs
app.use(express.static(publicDir));
app.use('/logs', express.static(logDir));

// Generate the HTML content dynamically based on the commands
const html = `
<!DOCTYPE html>
<html>
<head>
  <title>Live Logs</title>
  <style>
    body { background: #111; color: #eee; font-family: monospace; padding: 20px; }
    .tab { cursor: pointer; padding: 10px; display: inline-block; border: 1px solid #444; margin-right: 5px; }
    .tab.active { background: #444; }
    pre { display: none; white-space: pre-wrap; word-break: break-word; border: 1px solid #444; padding: 10px; margin-top: 10px; }
    pre.active { display: block; }
    .cyan { color: cyan; }
    .green { color: green; }
    .purple { color: purple; }
  </style>
</head>
<body>
  <h1>Live Logs</h1>
  <div id="tabs">
    ${commands.map((command, index) => `
      <div class="tab ${index === 0 ? 'active' : ''}" data-log="${command.name}">${command.name}</div>
    `).join('')}
  </div>
  <div id="logs">
    ${commands.map((command, index) => `
      <pre id="${command.name}" class="${index === 0 ? 'active' : ''}"></pre>
    `).join('')}
  </div>
  <script>
    const tabs = document.querySelectorAll('.tab');
    const logs = document.querySelectorAll('pre');

    function scrollToBottom(logElement) {
      logElement.scrollTop = logElement.scrollHeight;
    }

    tabs.forEach(tab => {
      tab.onclick = () => {
        tabs.forEach(t => t.classList.remove('active'));
        logs.forEach(l => l.classList.remove('active'));

        tab.classList.add('active');
        const target = tab.getAttribute('data-log');
        const logElement = document.getElementById(target);
        logElement.classList.add('active');

        scrollToBottom(logElement);
      };
    });

    const ws = new WebSocket('ws://' + location.host);
    ws.onmessage = e => {
      const { processName, message, color } = JSON.parse(e.data);
      const div = document.getElementById(processName);
      if (div) {
        const span = document.createElement('span');
        span.classList.add(color);
        span.textContent = message;
        div.appendChild(span);
        scrollToBottom(div);
      }
    };
  </script>
</body>
</html>
`;

// Write the HTML content to a file
fs.writeFileSync(path.join(publicDir, 'index.html'), html);

// WebSocket server
const server = app.listen(9999, () => {
  const { port } = server.address();
  console.log(`Web interface: http://localhost:${port}`);
});

const wss = new WebSocket.Server({ server });
wss.on('connection', (ws) => {
  // Send existing log history to client on connection
  Object.keys(logHistory).forEach(processName => {
    ws.send(JSON.stringify({ processName, message: logHistory[processName].join(''), color: 'green' }));
  });

  clients.add(ws);
  ws.on('close', () => clients.delete(ws));
});

// Function to broadcast log messages to all clients
function broadcastLog(processName, message, color) {
  const payload = JSON.stringify({ processName, message, color });

  // Store the log in history for that process
  if (logHistory[processName]) {
    logHistory[processName].push(message);
  }

  // Broadcast the log to all clients
  clients.forEach(ws => {
    if (ws.readyState === WebSocket.OPEN) {
      ws.send(payload);
    }
  });
}

// Function to start a new process and monitor its output
function startProcess({ cmd, args, name, color }) {
  const logFile = fs.createWriteStream(path.join(logDir, `${name}.log`), { flags: 'a', encoding: 'utf-8' });
  const proc = spawn(cmd, args, { shell: true });

  processes.push(proc);

  proc.stdout.on('data', (data) => {
    const msg = data.toString();
    logFile.write(msg);
    console.log(`[${name}] ${msg}`);
    broadcastLog(name, msg, color);
  });

  proc.stderr.on('data', (data) => {
    const msg = data.toString();
    logFile.write(msg);
    console.error(`[${name}] ${msg}`);
    broadcastLog(name, msg, color);
  });

  proc.on('close', () => logFile.end());
}

// Function to kill all running processes
function killAll() {
  return Promise.all(processes.map(p =>
    new Promise(resolve => kill(p.pid, 'SIGKILL', () => resolve()))
  ));
}

// Handle process termination on SIGINT
process.on('SIGINT', async () => {
  console.log('\nShutting down...');
  await killAll();
  process.exit();
});

// Start all the processes
(async () => {
  await killAll();
  for (const cfg of commands) {
    startProcess(cfg);
    await new Promise(res => setTimeout(res, 3000)); // Wait 3 seconds before starting the next process
  }
})();
