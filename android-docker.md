
#  Docker-Android Quick Start (WSL2 + Ubuntu)

## Requirements
- Windows 11 + WSL2 with Ubuntu
- Docker Desktop with WSL2 integration
- Virtualization enabled in BIOS/UEFI

---

##  Check Virtualization
```bash
sudo apt update && sudo apt install cpu-checker
kvm-ok
```

---

## ⚙️ Configure WSL2 for KVM
Edit config:
```bash
sudo nano /etc/wsl.conf
```

Add:
```ini
[boot]
command = /bin/bash -c 'chown -v root:kvm /dev/kvm && chmod 660 /dev/kvm'

[wsl2]
nestedVirtualization=true
```

Add user to group:
```bash
sudo usermod -a -G kvm $USER
```

Restart WSL:
```bash
wsl --shutdown
```

---

## Run Docker-Android Emulator
```bash
docker run -d -p 6080:6080 \
  -e EMULATOR_DEVICE="Samsung Galaxy S10" \
  -e WEB_VNC=true \
  --device /dev/kvm \
  --name android-container \
  budtmo/docker-android:emulator_11.0
```

Visit: [http://localhost:6080](http://localhost:6080)

---

## 🛠 Useful Commands
Check emulator status:
```bash
docker exec -it android-container cat device_status
```

Persist data:
```bash
docker run -v android-data:/home/androidusr \
  budtmo/docker-android:emulator_11.0
```


More info: [GitHub - docker-android](https://github.com/budtmo/docker-android)

=
Let me know if you want this saved to a file or need help automating the steps!
