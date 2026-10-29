# Arrow+ wiki

VitePress site for the mod. Arrow, stick and feather stats, recipes and names are read from the mod itself, so regenerate the mod's data before building when it changes.

```bash
./gradlew :fabric:runDatagen   # from the repository root, when the built-in arrows changed
cd docs
npm install
npm run dev                    # syncs data, then serves http://localhost:5173
npm run build                  # syncs data, then builds to .vitepress/dist
```

`npm run sync` (run automatically by `dev` and `build`) writes `.vitepress/data/data.json` from `fabric/src/main/generated/data` and copies the untinted arrow texture layers to `public/layers/` for the arrow designer. Both are git-ignored.

Vanilla item icons load from the hosted renders at `https://storage.googleapis.com/coolerpromc/textures/minecraft/`. Arrow+ arrows, sticks and feathers on the current pages are drawn in the browser from the mod's texture layers, tinted like the game does it, so any combination works and nothing needs uploading when a built-in entry changes.

The current release notes in `getting-started.md` include the repository’s `changelog.md` directly, so editing the release changelog updates the wiki on its next build.

The docs cover three systems, picked with the version switcher:

| Folder | Versions | Data |
| --- | --- | --- |
| `/` | 26.3.0.2+, 26.2.0.2+, 26.1.2.104+, modular arrows | Synced from the mod by `npm run sync` |
| `v26-1/` | 26.1 through 26.1.2.103, 26.2.0.1, 26.3.0.1 and 1.21.1-NeoForge, fixed recipes | Frozen snapshot in `.vitepress/frozen/data-26.1.json`, frozen components in `.vitepress/theme/components/v26-1/` |
| `legacy/` | 1.21.11 and older | Written by hand |

The `v26-1/` and `legacy/` pages use the hosted Arrow+ renders under `arrowplus/`, made by `scripts/render-icons.py` (needs Pillow) for the fixed-recipe arrows. Don't change those: they show what those versions look like.
