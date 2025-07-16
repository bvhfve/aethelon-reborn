# Aethelon Mod Compatibility Guide

## Overview

The Aethelon mod now includes comprehensive compatibility with popular ocean and exploration mods. This system automatically detects installed mods and enhances gameplay when they are present.

## Supported Mods

### 🐟 **Aquaculture 2** - Enhanced Fishing
- **Enhanced Fishing**: Better loot when fishing near Aethelon turtles
- **Turtle Island Fishing**: Special rewards when fishing directly on turtle islands
- **Turtle Shell Bait**: Use turtle shell items as enhanced fishing bait
- **Armor Bonuses**: Turtle shell armor improves fishing effectiveness

### 🌊 **Biomes O' Plenty** - Ocean Biomes
- **Enhanced Spawning**: Aethelon turtles spawn in BOP ocean biomes
- **Themed Islands**: Turtle islands adapt to BOP biome themes (tropical, swamp, coral, kelp)
- **Biome-Specific Behavior**: Turtles move faster in tropical waters, slower in swamps
- **Preferred Biomes**: Turtles favor tropical and coral reef biomes

### 🦈 **Alex's Mobs** - Aquatic Creatures
- **Creature Interactions**: Alex's aquatic mobs interact with turtle islands
- **Enhanced Spawning**: Higher spawn rates for aquatic mobs near turtle islands
- **Turtle Shell Protection**: Armor provides protection against hostile Alex's mobs
- **Special Loot**: Enhanced drops from Alex's mobs when killed near turtle islands

### 🗿 **Waystones** - Oceanic Travel
- **Mobile Waystones**: Waystones on turtle islands move with the turtle
- **Ancient Compass Integration**: Compass can locate nearby waystones
- **Oceanic Teleportation**: Reduced costs for water-based teleportation
- **Turtle Shell Crafting**: Use turtle items to craft special waystones

### 🔧 **Supplementaries** - Decorative Integration
- **Island Decorations**: Turtle islands can spawn with Supplementaries blocks
- **Navigation Integration**: Ancient Compass works with Supplementaries items
- **Mechanical Compatibility**: Turtle shell items work with Supplementaries mechanics

### 🌊 **William Wythers' Overhauled Overworld** - Enhanced World Generation
- **Enhanced Spawning**: Aethelon turtles spawn in WWOO ocean biomes with custom weights
- **Themed Islands**: Turtle islands adapt to WWOO biome themes (tropical paradise, coral garden, kelp sanctuary, ice fortress)
- **Biome-Specific Behavior**: Turtles gain special abilities in different WWOO environments
- **Environmental Integration**: Full integration with WWOO's enhanced world generation

### 🌍 **Terralith/Terrablender** - Biome Generation
- **Enhanced Spawning**: Integration with Terralith's custom ocean biomes
- **Terrablender Support**: Full biome modification system integration
- **Unique Island Themes**: Moonlit sanctuary, emerald oasis, crystal refuge themes
- **Special Behaviors**: Glowing effects, enhanced regeneration, crystal protection

### 🌑 **Deeper and Darker** - Deep Dark Integration
- **Ancient Turtle Variants**: Special sculk-resistant turtle variants in deep dark
- **Warden Interaction**: Stealth mechanics and Warden avoidance behaviors
- **Sculk Immunity**: Turtles immune to sculk sensor detection
- **Deep Dark Islands**: Sculk sanctuary and ancient fortress island themes

### 💡 **Aquatic Torches** - Underwater Lighting
- **Automatic Lighting**: Aquatic torches placed around turtle islands automatically
- **Bioluminescent Effects**: Turtle shells provide natural underwater lighting
- **Torch Maintenance**: Self-maintaining underwater lighting systems
- **Enhanced Recipes**: Turtle shell + aquatic torch crafting combinations

### 🐟 **Fins and Tails** - Aquatic Creature Ecosystem
- **Enhanced Spawning**: 3x spawn rates for Fins & Tails creatures near turtle islands
- **Symbiotic Relationships**: Cleaner fish, illuminators, scouts, guardians, treasure hunters
- **Breeding Enhancement**: Enhanced breeding mechanics for aquatic mobs near turtles
- **Ecosystem Dynamics**: Feeding grounds, migration routes, breeding sanctuaries

### 🦌 **Naturalist** - Realistic Marine Life
- **Predator-Prey Relationships**: Realistic interactions with sharks, dolphins, whales
- **Conservation Mechanics**: Endangered population bonuses and nesting behaviors
- **Environmental Adaptation**: Seasonal migrations and biome-specific adaptations
- **Feeding Behaviors**: Realistic feeding schedules and food source interactions

### 🌿 **Ecologics** - Biome Creature Integration
- **Coconut Crab Symbiosis**: Shell cleaning and protection from coconut crabs
- **Penguin Colony Interactions**: Playful interactions and cold resistance
- **Mangrove Behaviors**: Enhanced camouflage and healing in mangrove swamps
- **Tropical Adaptations**: Speed boosts and sun basking benefits in tropical biomes

## Configuration

The compatibility system can be configured through the main Aethelon config file:

```json
{
  "compatibility": {
    "enable_mod_compatibility": true,
    "aquaculture_integration": true,
    "biomes_o_plenty_integration": true,
    "alexs_mobs_integration": true,
    "waystones_integration": true,
    "debug_compatibility": false
  }
}
```

## Features by Mod

### Aquaculture 2 Features
- 25% chance for enhanced loot when fishing near turtles
- 50% chance for special rewards when fishing on turtle islands
- Turtle shell items provide fishing luck bonuses
- Full turtle shell armor set enhances fishing rod effectiveness

### Biomes O' Plenty Features
- **Tropical Theme**: Palm trees, coconuts, tropical flowers
- **Swamp Theme**: Mangrove vegetation, cattails, water lilies
- **Coral Theme**: Coral decorations around island base
- **Kelp Theme**: Kelp forests and sea grass around islands

### Alex's Mobs Features
- **Friendly Mobs**: Orcas, whales, seals attracted to turtle islands
- **Hostile Protection**: 10% damage reduction per turtle armor piece
- **Special Protection**: Extra 20% reduction against sharks, 15% against giant squid
- **Enhanced Loot**: Turtle-themed items from Alex's mobs near islands

### Waystones Features
- **Mobile Waystones**: Move with turtle islands automatically
- **Oceanic Travel**: 30% cost reduction for water-based teleportation
- **Armor Bonuses**: 5% additional cost reduction per turtle armor piece
- **Special Crafting**: Turtle Heart creates Oceanic Waystones

## Technical Implementation

### Automatic Detection
The compatibility system automatically detects installed mods using Fabric's mod loader API. No manual configuration is required.

### Performance Impact
- Minimal performance overhead
- Features only activate when compatible mods are present
- Graceful fallbacks if compatibility fails

### Error Handling
- Comprehensive error handling prevents crashes
- Failed compatibility features are logged but don't affect core functionality
- Safe fallbacks ensure the mod works even if compatibility fails

## Compatibility Status

| Mod | Status | Features |
|-----|--------|----------|
| Aquaculture 2 | ✅ Full | Enhanced fishing, turtle shell bait, armor bonuses |
| Biomes O' Plenty | ✅ Full | Biome spawning, themed islands, behavior changes |
| Alex's Mobs | ✅ Full | Creature interactions, protection, enhanced loot |
| Waystones | ✅ Full | Mobile waystones, oceanic travel, special crafting |
| William Wythers | ✅ Full | WWOO biome integration, themed islands, special behaviors |
| Terralith/Terrablender | ✅ Full | Custom biome spawning, unique themes, biome modifications |
| Deeper and Darker | ✅ Full | Ancient variants, sculk resistance, Warden interactions |
| Aquatic Torches | ✅ Full | Automatic lighting, bioluminescence, maintenance systems |
| Fins and Tails | ✅ Full | Creature ecosystem, symbiosis, breeding enhancement |
| Naturalist | ✅ Full | Realistic behaviors, conservation, environmental adaptation |
| Ecologics | ✅ Full | Biome creatures, coconut crab symbiosis, penguin interactions |
| Supplementaries | 🚧 Partial | Basic integration planned |
| Ocean Floor | 🚧 Planned | Enhanced generation around turtles |
| Upgrade Aquatic | 🚧 Planned | Enhanced ocean feature integration |

## Future Plans

### Planned Integrations
- **Create Mod**: Mechanical contraptions on turtle islands
- **Farmer's Delight**: Cooking and farming on turtle islands
- **Immersive Engineering**: Industrial setups on large turtle islands
- **Applied Energistics**: ME systems for turtle island bases

### Enhancement Ideas
- Dynamic island generation based on installed mods
- Cross-mod recipe integration
- Enhanced turtle AI based on mod-specific creatures
- Compatibility with dimension mods for turtle travel

## Troubleshooting

### Common Issues
1. **Compatibility not working**: Check that both mods are installed and up to date
2. **Performance issues**: Disable debug logging in config
3. **Crashes**: Report with full log including mod versions

### Debug Mode
Enable `debug_compatibility: true` in config for detailed compatibility logging.

### Reporting Issues
When reporting compatibility issues, please include:
- Aethelon version
- Compatible mod version
- Full crash log or error message
- Steps to reproduce

## Developer Information

### Adding New Compatibility
The compatibility system is designed to be easily extensible. New mod integrations can be added by:

1. Creating a new compatibility class in `com.bvhfve.aethelon.compat`
2. Registering it in `ModCompatibility.java`
3. Implementing the required integration methods
4. Adding any necessary mixins for deep integration

### API Usage
The compatibility system provides utility methods for:
- Checking if specific mods are loaded
- Getting mod-specific configuration
- Applying cross-mod effects
- Handling errors gracefully

This compatibility system makes Aethelon a truly integrated part of the modded Minecraft ocean and exploration experience!