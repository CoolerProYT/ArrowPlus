# Configuration

Arrow+ provides a NeoForge TOML config file on legacy versions.

## Config File Location

```
config/arrowplus-common.toml
```

## Config Options

### Restrictions

| Setting | Type | Default | Description |
|---------|------|---------|-------------|
| `restrictions` | `List<String>` | `[]` | Arrow registry path names to disable. Disabled arrows cannot be crafted or used. |

::: info Version Difference
On legacy versions, only `restrictions` exists. The `infinityBlacklist` and `hideTippedArrow` options from the new system (26.1+ / 1.21.1-NeoForge) are **not available** here.
:::

## Config File Example

::: code-group

```toml [arrowplus-common.toml]
[Restrictions]
    # Arrows to disable. Example: ['diamond', 'iron']
    restrictions = []
```

:::

## Arrow Names Reference

Arrow names in config correspond to their datapack registry path:

`wood`, `stone`, `bone`, `copper`, `iron`, `gold`, `lapis`, `brick`, `quartz`, `prismarine`, `glowstone`, `redstone`, `charcoal`, `amethyst`, `emerald`, `diamond`, `obsidian`, `netherite`

## Example

**Disable netherite and diamond arrows:**
```toml
[Restrictions]
    restrictions = ["netherite", "diamond"]
```
