
```markdown
# 🚀 Docker-Android Quick Start (WSL2 + Ubuntu)

## ✅ Requirements
- Windows 11 + WSL2 with Ubuntu
- [Docker Desktop with WSL2 integration](https://www.docker.com/products/docker-desktop/)
- Virtualization enabled in BIOS/UEFI

---

## 🔍 Check Virtualization
```bash
sudo apt update && sudo apt install cpu-checker
kvm-ok
```

---

## ⚙️ Configure WSL2 for KVM (Required for Hardware Acceleration)

Edit WSL configuration:
```bash
sudo nano /etc/wsl.conf
```

Add the following:
```ini
[boot]
command = /bin/bash -c 'chown -v root:kvm /dev/kvm && chmod 660 /dev/kvm'

[wsl2]
nestedVirtualization=true
```

Add your user to the `kvm` group:
```bash
sudo usermod -a -G kvm $USER
```

Then restart WSL:
```bash
wsl --shutdown
```

---

## 📦 Run Docker-Android Emulator
```bash
docker run -d -p 6080:6080 \
  -e EMULATOR_DEVICE="Samsung Galaxy S10" \
  -e WEB_VNC=true \
  --device /dev/kvm \
  --name android-container \
  budtmo/docker-android:emulator_11.0
```

Then visit: [http://localhost:6080](http://localhost:6080)

---

## 🛠 Useful Commands

✅ **Check emulator status**:
```bash
docker exec -it android-container cat device_status
```

💾 **Persist data between sessions**:
```bash
docker run -v android-data:/home/androidusr \
  budtmo/docker-android:emulator_11.0
```

---

## 🔗 Resources
- 📦 Docker Desktop: [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)
- 📘 Docker-Android GitHub: [https://github.com/budtmo/docker-android](https://github.com/budtmo/docker-android)

---

```
