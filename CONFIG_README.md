# Aethelon Configuration Guide

The Aethelon mod uses a JSON-based configuration system that allows you to customize various aspects of the world turtle entities. The configuration file is automatically created at `config/aethelon.json` when you first run the mod.

## Key Configurable Values

### Primary Settings (Most Important)

- **`water_depth_required`** (default: 30)
  - Minimum water depth required for turtle spawning
  - Range: 10-100 blocks
  - Higher values make spawning more restrictive (deeper ocean only)

- **`clearance_above_required`** (default: 50)
  - Minimum clear space above the spawn point for the turtle's shell/island
  - Range: 20-200 blocks
  - Higher values ensure more space for future island structures

- **`turtle_size_scale`** (default: 4.0)
  - Size multiplier for the turtle entity
  - Range: 0.5-10.0
  - Affects both visual size and hitbox size

- **`spawn_rarity`** (default: 0.1)
  - Probability that a turtle will spawn when all other conditions are met
  - Range: 0.001-1.0 (0.1% to 100% chance)
  - Lower values make turtles rarer

### Entity Behavior

- **`health`** (default: 2000.0)
  - Maximum health of turtle entities
  - Range: 100.0-10000.0

- **`movement_speed`** (default: 0.05)
  - How fast turtles move
  - Range: 0.01-1.0

- **`follow_range`** (default: 128.0)
  - How far turtles can detect players/entities

### Population Control

- **`max_world_population`** (default: 3)
  - Maximum number of turtles that can exist in a world
  - Range: 1-50

- **`min_distance_between_turtles`** (default: 5000)
  - Minimum distance in blocks between turtle spawns

## Example Configurations

### More Common Turtles
```json
{
  "water_depth_required": 20,
  "clearance_above_required": 30,
  "spawn_rarity": 0.3,
  "max_world_population": 5
}
```

### Rarer, Larger Turtles
```json
{
  "water_depth_required": 50,
  "clearance_above_required": 80,
  "turtle_size_scale": 6.0,
  "spawn_rarity": 0.05,
  "health": 5000.0
}
```

### Smaller, Faster Turtles
```json
{
  "turtle_size_scale": 2.0,
  "movement_speed": 0.15,
  "health": 1000.0,
  "water_depth_required": 15
}
```

## Configuration Notes

- The mod automatically validates and clamps values to safe ranges
- Invalid values will be logged and corrected automatically
- Changes require a game restart to take effect
- The config file is created with default values on first run
- You can delete the config file to reset to defaults

## Troubleshooting

If turtles aren't spawning:
1. Check `spawn_rarity` - increase for more frequent spawns
2. Reduce `water_depth_required` if oceans aren't deep enough
3. Reduce `clearance_above_required` if there are obstacles above
4. Check `max_world_population` - you may have reached the limit

For performance issues:
1. Reduce `turtle_size_scale` for smaller entities
2. Reduce `max_world_population`
3. Increase `min_distance_between_turtles`