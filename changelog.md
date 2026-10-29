## 26.1.2.104

### Modular arrows
- Build arrows from a **head**, a **stick** and a **feather**, with any combination of parts
- Heads set base damage, ignition and on-hit effects; sticks set gravity and launch speed; feathers set air drag, water drag, spread and a gravity multiplier
- Arrow stacks must have matching parts, potion contents and other item data to stack together
- Stick, bamboo, bone, blaze rod and breeze rod are used directly; copper, iron, gold, diamond, emerald and netherite sticks are crafted and have their own flight stats
- Feather, phantom membrane and paper are used directly; added Kelp and Prismarine Feathers, and gave the Gilded Feather its own stats
- Wooden rods (`#c:rods/wooden`) and feathers (`#c:feathers`) from other mods work as parts
- Flint is now a head for modular combinations; flint, a vanilla stick and a vanilla feather still craft vanilla arrows
- Parts used directly are also available in the material creative tab

### Flight balance
- Copper and iron extend the range of a wooden shaft; netherite trades range for higher launch damage and a steeper drop
- Bamboo trades launch speed and damage for lower gravity; diamond and emerald have distinct speed and gravity tradeoffs
- Phantom membrane reduces shaft gravity by 40%, retains 99.5% of speed per tick in air, and uses 80% of vanilla spread
- Kelp fletching retains 95% of speed per tick underwater, with more air drag and spread than a vanilla feather
- Prismarine fletching retains 85% of speed per tick underwater and uses 90% of vanilla spread

### Tooltips
- Compact arrow tooltips show the stick, feather, base damage and launch speed
- Hold Shift for grouped **Combat** and **Flight** stats, including combined arrow drop and effects on hit
- Drop and spread use multipliers against vanilla; the repeated vanilla-baseline footer is removed
- Infinity uses readable support text, and an ignition line appears only for heads that ignite targets
- Ingredients show **Arrow material** and **Hold [Shift] for stats**; Shift reveals their roles, craftable parts and stats
- Custom sticks and feathers show a role label and Shift hint, with their stats on Shift
- Vanilla arrows use the same stat layout; improved values are green, worse values red and unchanged values white
- Localized the new text in English, Simplified Chinese, Traditional Chinese and Hong Kong Chinese; corrected a Traditional Chinese tooltip that used Simplified Chinese

### JEI
- Show every eligible stick and feather in the modular arrow recipe
- Generate exact crafting and tipping recipes on demand when looking up arrows with non-default sticks or feathers
- Fixed duplicate vanilla tipped-arrow subtype registration with Fletching Recipe installed, preserving potion and explosive variants
- Fixed recipe-id collisions between heads, potions and datapack namespaces
- Retained the item field types used by released Fletching Recipe integrations

### Fixes
- Deprecated stick and feather items keep their custom names and component data when migrated
- Dispensers shoot Arrow+ projectiles instead of dropping the arrow item
- Fixed Power being applied twice, which made Arrow+ arrows too strong on enchanted bows
- Fixed tipped-arrow potion state after save/reload
- After 600 grounded ticks, tipped arrows lose the potion but retain their modular parts and default components so they stack normally
- Tipped arrows fired with Infinity are consumed and remain recoverable in survival
- Tipping requires eight identical, untipped arrows, preventing different builds from being converted into the first arrow's parts
- Arrow+ items missing required part data, such as bare `/give` items, convert to their vanilla counterpart in a player's inventory

### Datapacks and migration
- Fixed older built-in arrows losing their recipe-specific parts when the old head fields are absent; historical shaft and fletching choices are now restored
- Existing modular part selections are never overwritten, and explicit legacy datapack fields take priority over the built-in recipe history
- Migration also covers arrows inside shulkers, bundles and loaded crossbows, preserving counts, potions, names and custom data
- Added stick fields `gravity`, `velocity` and `standalone`; feather fields `airDrag`, `waterDrag`, `accuracy`, `gravityMultiplier` and `standalone`
- `standalone: true` uses the material item directly instead of crafting a custom stick or feather
- Final projectile gravity is shaft gravity multiplied by the feather's `gravityMultiplier`, which defaults to `1.0`
- Physics values must be finite: gravity `0`–`1`, velocity `0.01`–`4`, air/water speed retention `0`–`1`, spread `0`–`10`, and gravity multiplier `0`–`4`; crafted part output counts must be `1`–`64`
- Head fields `gravity`, `stick`, `stickData`, `feather` and `featherData` are deprecated, and the log names heads still using them
- Legacy stick/feather fields are read to migrate older arrow stacks; new crafting and flight use the selected parts
- Saved arrows migrate when their item stacks are loaded, including stacks stored in containers; they retain their head and receive legacy or vanilla parts as appropriate
