### Pantheon data
Go to https://pathofexile.gamepedia.com/api.php?action=cargoquery&format=json&limit=500&tables=pantheon_souls&fields=name,stat_text&where=ordinal!=1&formatversion=1  
Save as `pantheons.json`. Strip away `cargoquery` root element, so we just have an array.

### Map boss data:
Go to https://poedb.tw/us/area.php?cn=Map

Run in developer console:

```javascript
var maps = [];
document.querySelectorAll("table > tbody > tr").forEach(tr => {
    var map = {};
    map.name =  tr.querySelector("td:nth-child(4) a").innerHTML;
    map.bosses = [];
    var bosses = tr.querySelectorAll("td:nth-child(6) a");
    if (typeof bosses !== undefined) {
        bosses.forEach(boss => {
            if (map.bosses.indexOf(boss.innerHTML) === -1) {
                map.bosses.push(boss.innerHTML);
            }
        });
    }
    map.region = tr.querySelector("td:nth-child(7) a") !== undefined ? tr.querySelector("td:nth-child(7) a").innerHTML : "";
    maps.push(map);
});
console.log(maps);
```

Copy JSON object from console to `atlas_maps.json`.