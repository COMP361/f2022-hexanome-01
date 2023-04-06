# Splendor COMP 361 - Hexanome-01

## Quick Setup

Splendor runs on Windows x64 or macOS

### Prerequisites
 * Java JDK 14
 * Apache Maven 3.8.6
 * Docker 20.10.17

Clone the repository with the LobbyService submodule
```sh
git clone --recurse-submodules git@github.com:COMP361/f2022-hexanome-01.git
```

Build and run Docker containers
```sh
cd f2022-hexanome-01/server
docker compose up
```

Download [latest client release](https://github.com/COMP361/f2022-hexanome-01/releases/latest) for your operating system.

### For Windows x64
Set the environment variable `SPLENDOR_HOST_IP` as `localhost` (or the IPv4 of the device hosting the Docker database/servers).

Unzip the build and run `Splendor Client.exe`. Hit "Run anyway" if Windows warning shows up.

### For macOS
Unzip the build and run the following bash script:
```sh
#!/bin/bash
export SPLENDOR_HOST_IP="{ip}"


open -n "{path to app}" &>/dev/null &
```
Set `{ip}` to `localhost` (or the IPv4 of the device hosting the Docker database/servers).

Set `{path to app}` to the path to the extracted build (e.g. `/Users/jeremyeichler/Developer/SplendorComp361/Builds/SplendorGameMac.app`)

## Authors

 * Joshua Morency [https://github.com/Joshua-M1]
 * Kevin Cui [https://github.com/kevinjycui]
 * Peixin Jin [https://github.com/RavannaW]
 * Alexandra Charland [https://github.com/alexandracharland]
 * Yang Tian [https://github.com/PictureOfDorian]
 * Cadin Londono [https://github.com/cadinsl]
 * Jeremy Eichler [https://github.com/JeremyE1999]


