const fs = require('fs');
const path = require('path');
const express = require('express');
const WebSocket = require('ws');
const spawn = require('cross-spawn');
const kill = require('tree-kill');

const app = express();
const logDir = path.join(__dirname, 'logs');
const publicDir = path.join(__dirname, 'public');

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
if (!fs.existsSync(logDir)) fs.mkdirSync(logDir);
if (!fs.existsSync(publicDir)) fs.mkdirSync(publicDir);

const clients = new Set();
const processes = [];

// Store logs for persistence across refreshes
const logsHistory = {
  grid: [],
  android: [],
  ios: []
};

const folder = path.join(__dirname, '../../grid3');

// Process definitions
const commands = [
  {
    name: 'grid',
    cmd: 'java',
    args: [
      '-jar',
      path.join(folder, 'selenium.jar'),
      '-role', 'hub',
      '-hubConfig', path.join(folder, 'grid.json')
    ],
    color: 'green'
  },
  {
    name: 'android',
    cmd: 'appium',
    args: [
      '--nodeconfig', path.join(folder, 'android.json'),
      '--base-path=/wd/hub',
      '--port', '4723'
    ],
    color: 'green'
  },
  {
    name: 'ios',
    cmd: 'appium',
    args: [
      '--nodeconfig', path.join(folder, 'ios.json'),
      '--base-path=/wd/hub',
      '--port', '4727'
    ],
    color: 'green'
  }
];

// Serve logs and static HTML
app.use(express.static(publicDir));
app.use('/logs', express.static(logDir));

// HTML content
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
    <div class="tab active" data-log="grid">Grid</div>
    <div class="tab" data-log="android">Android</div>
    <div class="tab" data-log="ios">iOS</div>
  </div>
  <div id="logs">
    <pre id="grid" class="active"></pre>
    <pre id="android"></pre>
    <pre id="ios"></pre>
  </div>
  <script>
    const tabs = document.querySelectorAll('.tab');
    const logs = document.querySelectorAll('pre');

    // Function to scroll to the bottom of the active log
    function scrollToBottom(logElement) {
      logElement.scrollTop = logElement.scrollHeight;
    }

    // Switch between tabs
    tabs.forEach(tab => {
      tab.onclick = () => {
        tabs.forEach(t => t.classList.remove('active'));
        logs.forEach(l => l.classList.remove('active'));

        tab.classList.add('active');
        const target = tab.getAttribute('data-log');
        const logElement = document.getElementById(target);
        logElement.classList.add('active');

        // Scroll to bottom whenever switching to a tab
        scrollToBottom(logElement);
      };
    });

    // Open WebSocket connection to receive logs
    const ws = new WebSocket('ws://' + location.host);
    ws.onmessage = e => {
      const { processName, message, color } = JSON.parse(e.data);
      const div = document.getElementById(processName);
      if (div) {
        const span = document.createElement('span');
        span.classList.add(color);
        span.textContent = message;
        div.appendChild(span);

        // Auto-scroll to bottom whenever a new message is added
        scrollToBottom(div);
      }
    };
  </script>
</body>
</html>
`;

// Write HTML content to file
fs.writeFileSync(path.join(publicDir, 'index.html'), html);

// WebSocket server
const server = app.listen(0, () => {
  const { port } = server.address();
  console.log(`Web interface: http://localhost:${port}`);
});

const wss = new WebSocket.Server({ server });
wss.on('connection', (ws) => {
  // Send existing log history to client on connection
  ws.send(JSON.stringify({ processName: 'grid', message: logsHistory.grid.join(''), color: 'green' }));
  ws.send(JSON.stringify({ processName: 'android', message: logsHistory.android.join(''), color: 'green' }));
  ws.send(JSON.stringify({ processName: 'ios', message: logsHistory.ios.join(''), color: 'green' }));

  clients.add(ws);
  ws.on('close', () => clients.delete(ws));
});

function broadcastLog(processName, message, color) {
  const payload = JSON.stringify({ processName, message, color });

  // Store the logs to persist on client reconnect
  if (logsHistory[processName]) {
    logsHistory[processName].push(message);
  }

  for (const ws of clients) {
    if (ws.readyState === WebSocket.OPEN) {
      ws.send(payload);
    }
  }
}

function startProcess({ cmd, args, name, color }) {
  const logFile = fs.createWriteStream(path.join(logDir, `${name}.log`), { flags: 'a',encoding: 'utf-8' });
  const proc = spawn(cmd, args, { shell: true });

  proc.stdout.on('data', (data) => {
    const msg = data.toString();
    logFile.write(msg);
    console.log(`[${name}] ${msg}`);
    broadcastLog(name, msg, color);
  });

  proc.stderr.on('data', (data) => {
    const msg = `${data}`;
    logFile.write(msg);
    console.error(`[${name}] ${msg}`);
    broadcastLog(name, msg, color);
  });

  proc.on('close', () => logFile.end());

  processes.push({ name, pid: proc.pid });
}

function killAll() {
  return Promise.all(processes.map(p =>
    new Promise(resolve => kill(p.pid, 'SIGKILL', () => resolve()))
  ));
}

process.on('SIGINT', async () => {
  console.log('\nShutting down...');
  await killAll();
  process.exit();
});

(async () => {
  await killAll();
  for (const cfg of commands) {
    startProcess(cfg);
    await new Promise(res => setTimeout(res, 3000));
  }
})();
