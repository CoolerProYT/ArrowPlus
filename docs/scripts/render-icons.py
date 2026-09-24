"""
Renders the wiki's item icons from the mod's own textures and datagen output.

Arrow+ items are one texture per layer, tinted at runtime by the arrow/stick/feather data,
so every variant is flattened here into a plain 16x16 PNG (the in-game inventory look).
Upload the results to the hosted texture bucket (they are then listed by `npm run sync`):

    py -3 docs/scripts/render-icons.py
    python <mod-texture-uploader>/scripts/upload_texture.py docs/.icons/arrowplus/<name>.png arrowplus <name>

Needs Pillow. The lingering potion for the tipped arrow recipe comes from the Minecraft client jar
in the Fabric Loom cache, so run a Fabric build once first.
"""
import json
import zipfile
from pathlib import Path

from PIL import Image

DOCS = Path(__file__).resolve().parent.parent
ROOT = DOCS.parent
DATA = ROOT / "fabric/src/main/generated/data"
TEXTURES = ROOT / "common/src/main/resources/assets/arrowplus/textures/item"
OUT = DOCS / ".icons"

# Colours the tint sources fall back to when an arrow uses a vanilla stick or feather.
VANILLA_STICK = 0xFF886627
VANILLA_FEATHER = 0xFFFFFFFF
# Potion used for the tipped arrow example: Poison.
POISON = 0xFF87A363


def layer(name: str) -> Image.Image:
    return Image.open(TEXTURES / f"{name}.png").convert("RGBA")


def tint(image: Image.Image, argb: int) -> Image.Image:
    rgb = ((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF)
    r, g, b, a = image.split()
    channels = [c.point(lambda v, t=t: v * t // 255) for c, t in zip((r, g, b), rgb)]
    return Image.merge("RGBA", (*channels, a))


def stack(*layers: Image.Image) -> Image.Image:
    result = Image.new("RGBA", layers[0].size)
    for image in layers:
        result = Image.alpha_composite(result, image)
    return result


def entries(kind: str) -> dict:
    result = {}
    for namespace in sorted(p.name for p in DATA.iterdir()):
        for file in sorted((DATA / namespace / "arrowplus" / kind).glob("*.json")):
            result[f"{namespace}:{file.stem}"] = json.loads(file.read_text("utf-8"))
    return result


def save(image: Image.Image, namespace: str, name: str):
    target = OUT / namespace / f"{name}.png"
    target.parent.mkdir(parents=True, exist_ok=True)
    image.save(target)
    print(target.relative_to(DOCS))


def signed(value: int) -> int:
    """Datapack colours are signed ints; Pillow wants the unsigned ARGB."""
    return value & 0xFFFFFFFF


sticks = entries("sticks")
feathers = entries("feathers")
arrows = entries("arrows")

for key, stick in sticks.items():
    namespace, path = key.split(":")
    save(tint(layer("stick"), signed(stick["color"])), namespace, f"{path}_stick")

for key, feather in feathers.items():
    namespace, path = key.split(":")
    save(tint(layer("feather"), signed(feather["color"])), namespace, f"{path}_feather")


def arrow_layers(arrow: dict) -> list:
    stick = sticks.get(arrow.get("stickData", ""))
    feather = feathers.get(arrow.get("featherData", ""))
    return [
        tint(layer("arrow_stick"), signed(stick["color"]) if stick else VANILLA_STICK),
        tint(layer("arrow_head"), signed(arrow["color"])),
        tint(layer("arrow_feather"), signed(feather["color"]) if feather else VANILLA_FEATHER),
    ]


for key, arrow in arrows.items():
    namespace, path = key.split(":")
    save(stack(*arrow_layers(arrow)), namespace, f"{path}_arrow")

# Tipped example: Diamond Arrow of Poison.
diamond = arrows.get("arrowplus:diamond")
if diamond:
    save(stack(*arrow_layers(diamond), tint(layer("arrow_tipped"), POISON)), "arrowplus", "diamond_arrow_tipped")

# Lingering Potion of Poison for the same recipe: the vanilla bottle plus its tinted liquid overlay.
# Kept under arrowplus/ because the hosted minecraft/ icons are untinted defaults.
jars = sorted(Path.home().glob(".gradle/caches/fabric-loom/*/minecraft-client.jar"))
if jars:
    with zipfile.ZipFile(jars[-1]) as jar:
        def vanilla(name: str) -> Image.Image:
            with jar.open(f"assets/minecraft/textures/item/{name}.png") as f:
                return Image.open(f).convert("RGBA")

        save(stack(tint(vanilla("potion_overlay"), POISON), vanilla("lingering_potion")), "arrowplus", "lingering_potion_of_poison")
else:
    print("No Minecraft client jar in the Fabric Loom cache; skipped the lingering potion.")
