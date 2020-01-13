# :last_quarter_moon_with_face: lunaris

[Video preview](https://streamable.com/aobjz)

A Path of Exile helper which currently provides the following shortcuts:
- `alt + D` price information from `poe.ninja` and `pathofexile.com/trade`
- `alt + Q` open browser with `pathofexile.com/trade`
- `alt + A` map information (boss, pantheon, mod warnings) and currency stack in chaos equivalent
- `alt + W` browse wiki for item
- `ctrl + scroll` scroll through stashes anywhere on the screen
- `F5` go to hideout
- Switch between leagues through the system tray icon

:construction: Still a work in progress, contributions are welcome!

## Installation
Lunaris automatically updates itself, so you only have to download it once.
You do **not** need install Java.

Head over to the release page to download lunaris. Download the latest `lunaris-0.x.x-win64.zip`, extract it anywhere you like and start with `lunaris.bat`.

[Download the lunaris-0.x.x-win64.zip file here.](https://github.com/mtricht/lunaris/releases)

Having troubles? Check out the [FAQ](#FAQ) or open an issue.

## Boss screenshots
Are we missing a boss? You can help by making a screenshot of the boss in the maps you’re already running anyway, and filling it out in this form: https://forms.gle/tE9e6PshZ1QWutV48  
We will make sure to credit everyone that has submitted a screenshot somewhere in the tool! Thanks in advance!

## Screenshots

#### Map information
![Map information screenshot](/screenshots/map_info.png)

#### Price information from pathofexile.com and poe.ninja
![Astramentis price information screenshot](/screenshots/astramentis.png)
![Rare ring price information screenshot](/screenshots/topaz_rare_ring.png)

#### Currency stack in chaos equivalent
![Jeweller's Orb in chaos equivalent screenshot](/screenshots/currency_stack.png)

#### Path of Exile official trade search opening in browser
![Path of Exile trade in browser screenshot](/screenshots/path_of_exile_browser.png)

## FAQ

##### Pressing F5 only opens the chat

This has been fixed in v0.3.5

##### I have a moon in my system tray but no shortcut keys work

You are most likely running Path of Exile as administrator. Please exit lunaris and start the .bat file as administrator.

##### Some items give no results

The filters are currently not very 'smart', as they just one-to-one copy the item and paste that into pathofexile.com.
Let us know what we can do to improve your experience.

##### Some items outright don't work

Not all items are currently supported. There are more items supported than not though. Let us know which ones we are missing.

## Roadmap
- [ ] Fractured, Delve, Monster, **Pseudo**, Enchant and Veiled affixes for `pathofexile.com/trade` search
- [ ] Add item type filter for `pathofexile.com/trade` search
- [X] Add Linux support
- [ ] Add language support
