<p align="center">
  <img src="honami-art.png" alt="Honami" height="240"/>
  <img src="honami-plush.jpg" alt="Honami plush" height="200"/>
  <img src="honami-umbrella.jpg" alt="Honami" height="240"/>
</p>

<h1 align="center">⚡ Honami</h1>

<p align="center">
  <b>High-performance, feature-packed 1.8.8 Spigot fork for Minecraft.</b><br/>
  Paper-grade engineering. CarbonSpigot & ImanitySpigot-grade depth. 
</p>

<p align="center">
  <b>Discord: @ncros</b> · <b>GitHub: <a href="https://github.com/Recilia">github.com/Recilia</a></b>
</p>

---

## Honami

Honami is a fork of **WindSpigot** and **NachoSpigot**, based on **Paper**, with heavy inspiration from **CarbonSpigot** and **ImanitySpigot**.

It's a 1.8.8 server built for PvP: heavy work like entity tracking, pathfinding, NBT saving and packet handling is moved off the main thread, world ticking can run in parallel, knockback and combat mechanics are fully configurable, and ViaVersion + ViaRewind are bundled so clients from 1.7.x to the latest release can join out of the box.

**Requirements:** Java 11 – Java 25 · Minecraft 1.8.8 · Maven 3.6+ (to build from source)

## 🛠️ Build it yourself

> ⚠️ **Honami is source-only.** 

**Prerequisites:** Java 11+ (the build targets Java 8 bytecode) · Maven 3.6+ · Git

**1. Clone**

```bash
git clone https://github.com/Recilia/HonamiSpigot.git
cd HonamiSpigot
```

**2. Build** (compiles API + server, tests included)

```bash
mvn clean package
# skip tests for a faster build:
mvn clean package -DskipTests
```

First build downloads the dependency repositories declared in the poms (Carbon-API, elmakers' vanilla server artifact); this can take a while.

**3. Run**

```bash
java -Xmx2G -jar Honami-Server/target/Honami.jar --nogui
```

Accept the EULA (`eula.txt` → `eula=true`), restart, done. ViaVersion & ViaRewind are bundled inside the jar and extract themselves on first start — no plugin downloads. First boot generates `honami.yml`, `knockback.yml` and the rest of your server files.

**Artifacts:**

| Module | Output | What it is |
|---|---|---|
| Honami-Server | `Honami-Server/target/Honami.jar` | The runnable server (shaded — run with `java -jar`) |
| Honami-API | `Honami-API/target/Honami-API.jar` | Plugin API + shaded variant (`api-<version>-shaded.jar`) |

**Plugins targeting Honami:** depend on `rein.honami:api:1.0.0.0` (published from `Honami-API`) for the combat/knockback, pearl, event and entity-visibility extensions.

## The headline feature

### 🎮 Every client can join — ViaVersion & ViaRewind built-in
ViaVersion and ViaRewind ship **inside the server jar** and are extracted automatically on first start. No plugin downloads, no configuration.

- **1.7.x clients** (ViaRewind) — the golden-age clients keep working
- **Latest Minecraft clients** (ViaVersion 5.x) — join straight from modern launchers
- Toggle anytime: `settings.via-version.enabled` in `honami.yml`

## ⚔️ PvP & Combat

- **Knockback profiles & presets** — 10 built-in profiles (`vanilla`, `honami`, `hypixel`, `kohi`, `easy`, `smooth`, `detailed`, `exclusive`, `expert`, `explicit`), per-player profiles (perfect for practice servers), full control over horizontal/vertical, friction, W-tap extras and rod/bow/pearl/snowball/egg projectiles
- **In-game tuning** — `/kb` & `/knockback` commands with permission support
- **Developer combat API** — `rein.honami.api.combat` profiles you can attach to any player
- **Hit delay control** — 0 for full 1.7-style spam-click, or classic values
- **Configurable potion speeds** — instant splashes, no more waiting
- **Reach cap** — server-side limit with creative bypass
- **Pearl physics** — Minemen Club delayed-teleport mode, no-damage option, and passthrough for fences, slabs, glass, snow and more
- **Cannoning fixes** — east/west cannoning correction, TNT velocity cache, fast cannon entity tracker
- **Panda-wire redstone** — the optimized redstone algorithm, no plugins needed
- **Fishing-rod multiplier** — tune your rod combos

## 🚀 Performance

- **Async engine** — entity tracking, entity pathfinding, player NBT saving, explosions, keep-alives and packet handling all run off the main thread
- **Parallel world ticking** — tick your worlds across workers
- **Enhanced chunk cache** & configurable region-file cache
- **Custom cave generation** — control cave frequency, sizes and ore rates per world (Badlion-style)
- **Thread affinity** — pin the server to dedicated CPU cores on properly configured systems
- **Network tuning** — TCP_NODELAY, TCP_FASTOPEN, modern keep-alive handling
- **Everyday optimizations** — fastutil, branchless hot paths, optimized collisions, lighting and entity loops (full patch list below)

## 🛡️ Security & Stability

- Log4j patched to a modern version (no more Log4Shell)
- Built-in **anti-crash** & **anti-malware**
- Illegal-behavior detection & disconnect-spam protection
- Book & inventory exploit patches
- Toggleable `/reload`, `/version`, `/plugins` and console IP display

## 🕵️ Anti-cheat friendly

- **Artemis anticheat SDK hook** (`settings.anticheat.artemis-sdk`)
- **Packet & movement listener APIs** (`PacketListener`, `MovementListener`) for plugin developers
- **Vanish patch** — vanished players can't leak through projectiles, drops, XP or orbs; plus `/hide` and the `EntityHider` API

## 📋 Feature highlights at a glance

| Area | What you get |
|---|---|
| Client support | ViaVersion + ViaRewind built-in (1.7.x → latest) |
| Async | Entity tracker · pathfinding · NBT saves · explosions · packets · keep-alive |
| Knockback | 10 presets · per-player profiles · dev API · `/kb` |
| Combat | Hit delay · potion speed · reach cap · pearl physics · rod/arrow tuning |
| Worlds | Parallel ticking · enhanced chunk cache · custom caves |
| Redstone | PandaWire algorithm |
| TNT | Velocity cache · liquid-explosion optimization |
| Security | Anti-crash · anti-malware · patched Log4j · exploit fixes |
| Network | TCP_NODELAY · TCP_FASTOPEN · modern keep-alive |
| API | Combat profiles · packet/movement listeners · optimized event bus |

## ⚙️ Configuration

`honami.yml` — the main config · `knockback.yml` — knockback profiles · `honami-taco.yml` — world tweaks
(plus the standard `spigot.yml`, `paper.yml`, `bukkit.yml`, `commands.yml`)

Every option is documented inside the files themselves with YAML comments.

## 👾 Handy commands

`/kb` & `/knockback` · `/profiler` · `/tps` · `/ping` · `/hide` · `/mobai` · `/killall` · `/spawnmob` · `/setmaxslots` · `/day` & `/night` · `/chunkunload`

## 📚 Source & Support

- **Repository:** [github.com/Recilia/HonamiSpigot](https://github.com/Recilia/HonamiSpigot)
- **Discord:** @ncros

-clone with `git clone https://github.com/Recilia/HonamiSpigot.git`


---

## Credits & Inspirations

Honami stands on the shoulders of giants — **PaperSpigot**, **Spigot**, **NachoSpigot**, **CarbonSpigot** and **ImanitySpigot**, plus the countless forks and contributors whose patches power this server. The complete credit list lives below.

### Third-Party Licenses

Honami bundles [ViaVersion](https://github.com/ViaVersion/ViaVersion) and [ViaRewind](https://github.com/ViaVersion/ViaRewind) to allow clients of any Minecraft version to join. Both are licensed under the [GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.html); their full license texts are available in their source repositories.

## Patches
**All credit goes to the people that made honami possible.**<br/>

```
[Honami-0001] Thread affinity
[Honami-0002] Honami config
[Honami-0003] Mob AI toggle command
[Honami-0004] Parallel world ticking
[Honami-0006] Remove FastMath
[Honami-0007] Player ping command
[Honami-0008] Make NachoSpigot's async TNT configurable
[Honami-0009] Configurable entity hit delay
[Honami-0010] Configurable potion speeds
[Honami-0011] Make console display of player ips toggleable
[Honami-0013] More configuration for knockback
[Honami-0014] Async entity path searching
[Honami-0015] Configurable explosion animations and sound
[Honami-0016] Configurable weather changes
[Honami-0017] Configurable fishing rod speed multiplier

[ViaVersion] ViaVersion & ViaRewind bundled & auto-extracted
```
