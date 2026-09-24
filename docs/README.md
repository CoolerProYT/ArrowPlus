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

Item icons load from the hosted renders at `https://storage.googleapis.com/coolerpromc/textures/`: vanilla items under `minecraft/`, every Arrow+ arrow, stick and feather variant under `arrowplus/`. Arrow+ items are tinted at runtime, so their icons are flattened by `scripts/render-icons.py` (needs Pillow) into `.icons/`, then uploaded at 1024x1024. Rerun it and upload the new files after adding or recolouring a built-in arrow, stick or feather.

The legacy docs (`legacy/`) describe 1.21.11 and older, where only arrows are datapack entries. They are written by hand.
