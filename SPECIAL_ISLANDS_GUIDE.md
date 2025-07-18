# Special Islands Spawning System

## Overview
The Aethelon mod features three types of special islands that spawn under specific conditions, adding rare and exciting encounters to oceanic exploration:

- **Boss Islands**: Dangerous islands with powerful guardians
- **Treasure Islands**: Hidden islands containing valuable loot  
- **Mystical Islands**: Ancient islands with magical properties

## Spawning Mechanics

### Base Spawn Chances (per 1000 turtle spawns)
- Boss Islands: 0.1% base chance
- Treasure Islands: 0.3% base chance
- Mystical Islands: 0.2% base chance

### Requirements

#### Basic Conditions (All Special Islands)
- Must be in deep ocean biomes
- Minimum 5000 blocks from world spawn (configurable)
- Minimum 2000 blocks from other special islands (configurable)
- Player must be within 100 blocks to witness the spawn
- Turtle must not already have an island

#### Boss Islands
- **Player Progression**: Minimum experience level 10 (configurable, represents playtime)
- **Equipment Check**: Higher chance if player has 15+ armor points
- **Combat Experience**: Bonus chance if player has 20+ experience levels
- **Depth Bonus**: +200% chance in very deep water (Y < 30)
- **World Limit**: Maximum 3 per world (configurable)

#### Treasure Islands
- **Weather Bonus**: +100% chance during thunderstorms
- **Equipment Bonus**: +33% chance if player has 10+ armor points
- **Time Bonus**: +33% chance during nighttime
- **World Limit**: Maximum 8 per world (configurable)

#### Mystical Islands
- **Player Progression**: Minimum experience level 15 (configurable, represents playtime)
- **Time Bonus**: +100% chance during dawn/dusk (22000-24000 or 0-2000 ticks)
- **Magic Bonus**: +50% chance if player has enchanted items
- **Experience Bonus**: +50% chance if player has 30+ experience levels
- **World Limit**: Maximum 5 per world (configurable)

## Configuration Options

All special island settings can be configured in `aethelon.json`:

```json
{
  "enable_special_islands": true,
  "special_island_spawn_multiplier": 1.0,
  "min_distance_from_spawn_for_special": 5000,
  "min_distance_between_special_islands": 2000,
  "max_boss_islands_per_world": 3,
  "max_treasure_islands_per_world": 8,
  "max_mystical_islands_per_world": 5,
  "min_playtime_hours_for_boss_islands": 10,
  "min_playtime_hours_for_mystical_islands": 15,
  "debug_special_island_spawning": false
}
```

## Player Experience

### Discovery Messages
When a special island spawns, players receive distinctive messages:
- **Boss Island**: "§c§lA menacing presence stirs beneath the waves... A Boss Island has emerged!"
- **Treasure Island**: "§6§lThe ocean whispers of hidden riches... A Treasure Island has surfaced!"
- **Mystical Island**: "§d§lAncient magic pulses through the water... A Mystical Island has appeared!"

### Visual Effects
Each special island type has unique particle effects and sounds (to be implemented):
- **Boss Islands**: Dark particles, thunder sounds
- **Treasure Islands**: Golden particles, treasure sounds
- **Mystical Islands**: Magical particles, mystical sounds

## Integration with Existing Systems

### Turtle Spawning
Special islands integrate seamlessly with the existing turtle spawning system:
1. When a turtle spawns, it first checks for special island conditions
2. If special conditions are met, it attempts to spawn a special island
3. If no special island spawns, it falls back to regular island spawning

### Island Manager
The `IslandManager` class provides new methods:
- `tryLoadSpecialIsland()`: Attempts to spawn a special island
- `loadRandomIslandStructure()`: Loads random island with special island priority

### Structure System
Special islands use the same structure system as regular islands but with:
- Enhanced spawning logic
- Global spawn tracking
- Distance validation
- Player progression checks

## Development Notes

### Adding New Special Islands
To add new special island types:
1. Add the island type to `IslandType` enum
2. Register the structure in `DatapackStructureManager`
3. Add spawning logic in `SpecialIslandSpawner`
4. Create the NBT structure file
5. Update configuration options

### Debugging
Enable `debug_special_island_spawning` in config to see detailed logs about:
- Spawn attempt conditions
- Chance calculations
- Success/failure reasons
- Global limit checks

### Performance Considerations
- Special island checks only occur during turtle spawning
- Distance calculations are optimized
- Global tracking uses efficient data structures
- Config values are cached for performance

## Balancing Philosophy

The special island system is designed to:
- Reward long-term exploration and progression
- Create memorable moments for dedicated players
- Maintain rarity to preserve excitement
- Scale with player advancement
- Encourage deep ocean exploration

Special islands should feel like significant discoveries that mark important milestones in a player's oceanic adventure journey.