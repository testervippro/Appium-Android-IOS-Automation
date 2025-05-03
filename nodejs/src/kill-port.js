const findProcess = require('find-process');

async function killPort(port, options = {}) {
  const { silent = false } = options;

  try {
    // Handle array of ports
    if (Array.isArray(port)) {
      let killedAny = false;
      for (const p of port) {
        killedAny = (await killPort(p, { silent: true })) || killedAny;
      }
      if (!silent) {
        console.log(killedAny
          ? ` Killed processes on ports: ${port.join(', ')}`
          : ` No processes found on ports: ${port.join(', ')}`);
      }
      return killedAny;
    }

    // Dynamically import fkill (ESM module)
    const fkill = (await import('fkill')).default;

    // Single port case
    const processes = await findProcess('port', port);
    if (!processes.length) {
      if (!silent) console.log(` No process using port ${port}`);
      return false;
    }

    await fkill(processes.map(p => p.pid), { force: true });
    if (!silent) console.log(` Killed ${processes.length} process(es) on port ${port}`);
    return true;
  } catch (error) {
    if (!silent) {
      console.error(` Error killing port ${port}:`, error.message);
    }
    throw error;
  }
}

// Example usage
(async () => {

  await killPort([4444, 9999, 4723, 4727, 4733, 5555, 5556, 5565]);
})().catch(console.error);
