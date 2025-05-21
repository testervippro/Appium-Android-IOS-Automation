const findProcess = require('find-process');

async function killPort(port, options = {}) {
  const { silent = false } = options;

  try {
    if (Array.isArray(port)) {
      let killedAny = false;
      for (const p of port) {
        const killed = await killPort(p, { silent: true });
        killedAny = killedAny || killed;
      }
      if (!silent) {
        console.log(killedAny
          ? `Killed processes on ports: ${port.join(', ')}`
          : `No processes found on ports: ${port.join(', ')}`);
      }
      return killedAny;
    }

    const fkill = (await import('fkill')).default;
    const processes = await findProcess('port', port);

    const safeProcesses = processes.filter(p => {
      const name = (p.name || '').toLowerCase();
      return !name.includes('emulator') &&
             !name.includes('qemu') &&
             !name.includes('avd') &&
             !name.includes('android');
    });

    if (!safeProcesses.length) {
      if (!silent) console.log(`No process using port ${port}`);
      return false;
    }

    const pids = safeProcesses.map(p => p.pid);
    await fkill(pids, { force: true });
    if (!silent) console.log(`Killed processes using port ${port}: ${pids.join(', ')}`);
    return true;
  } catch (err) {
    if (!silent) console.error(`Failed to kill port ${port}:`, err.message || err);
    return false;
  }
}

// Example usage
(async () => {

  await killPort([4444, 9999, 4723, 4727, 4733, 5555, 5556, 5565]);
})().catch(console.error);
