# Tipped arrows

Any Arrow+ arrow can be tipped with a potion, the same way as vanilla arrows: surround a **Lingering Potion** with eight identical arrows: the same head, stick, feather and other item data. Already-tipped arrows cannot be tipped again. You get eight tipped arrows that keep all three parts.

<RecipeCard type="tipped" />

## Name

Tipped arrows are named `<Arrow Name> of <Effect Name>`. A Diamond Arrow tipped with Poison becomes **Diamond Arrow of Poison**.

## Behaviour

- The potion effect is applied on hit, like vanilla tipped arrows.
- The arrow's base damage still applies in full.
- Tipped arrows are **never** kept by Infinity, whatever the config says. Arrows fired in survival can still be recovered.
- Potion contents survive saving and reloading the world. After 600 grounded ticks (about 30 seconds), the potion expires, while the head, stick and feather are preserved.

## Recipe viewers

JEI lists tipped Arrow+ arrows and can show the tipping recipe for a specific modular build. Default tipped variants can be hidden with the [`hideTippedArrow`](./config#recipe-viewers) config option.
