# Tap Bow (Fabric, Minecraft 1.21.11, client-side)

Tap right-click with a bow and it releases by itself after a short draw, so you get a quick shot
without holding the button. The arrow is exactly as strong as vanilla makes it for that draw time:
a short draw is a **weak arrow**. The release is a normal one (the server counts the draw itself),
so nothing is faked.

- After the automatic release the button counts as "up" until you click again, so holding it does
  not machine-gun arrows.
- Hold **Sneak** while drawing to use the normal hold-to-charge bow (can be turned off).

## Keybind (turn it on/off in game)
Go to **Options > Controls > Key Binds**, find the **Tap Bow** section and set a key for
**Toggle Tap Bow**. It starts unbound so it never clashes with your other keys. Each press flips
the mod ON/OFF and shows "Tap Bow: ON/OFF" above your hotbar. The setting is saved.

## Settings
Install **Mod Menu**, open Mods > Tap Bow > settings button:
- **Tap Bow: ON/OFF**
- **Release after** cycles 4 / 6 / 8 / 10 / 15 / 20 ticks (20 = full draw)
- **Sneak = normal bow: ON/OFF**

Without Mod Menu, edit `config/tapbow.json`:

    { "enabled": true, "chargeTicks": 4, "sneakCharges": true }

Why 4 and not lower: a bow shoots nothing at all below about 3 ticks of draw, and with lag the
server can count one tick less than your game, so 4 is the safe minimum.

## Notes about servers
The server sees a normal draw and a normal release. Some servers ban automation (macros), and an
automatic release at the same tick count every time is automation, so if a server's rules forbid
that, switch the mod OFF there.

## Get the jar
- **Online:** upload this folder to a GitHub repository (create `.github/workflows/build.yml` on GitHub
  if the hidden folder was skipped), then open Actions > latest run > Artifacts > tapbow-jar.
- **On your PC:** install JDK 21, run `gradlew build`, take `build/libs/tapbow-1.0.0.jar`.

Put the jar in `.minecraft/mods` with Fabric API for 1.21.11.

## If the build complains about a version
Open https://fabricmc.net/develop, pick 1.21.11 and copy the values into `gradle.properties`.
