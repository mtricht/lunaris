# Linux
Lunaris has partial support for linux.

### Prerequisites:

1. Path of Exile has to be played in windowed or borderless window
2. Path of Exile has to be played on your main screen
3. Java 11+ needs to be installed (we currently provide no .zip for linux, coming soon!)

Grab the jar from the [releases page](https://github.com/mtricht/lunaris/releases) and start pricing! Please let us know of any issues.
It has been tested on Arch/Manjaro (GNOME, KDE and i3) and a cinnamon system.

### Known bugs/issues:

- Stash scroll does not work. X11 does not allow event consumption.
- Macros can't be on an hotkey that also use a skill or opens a in-game window. Also due to X11 not being able to consume events.
- The hotkeys will work even outside Path of Exile. We're unsure how to use X11 to query if PoE is the active window, contributions are more than welcome.
- The tooltip is cut off by a few pixels.
- The background is grey as transparency is not supported.
- GNOME has no system tray icon

Special thanks to [thorsten-passfeld](https://github.com/thorsten-passfeld) and [mkalte666](https://github.com/mkalte666) for testing.
