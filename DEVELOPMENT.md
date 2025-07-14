# Aethelon Development Guide

## Project Structure

```
aethelon/
├── src/main/java/com/bvhfve/aethelon/
│   ├── Aethelon.java                    # Main mod class
│   ├── entity/
│   │   └── AethelonEntity.java          # Core turtle entity
│   ├── registry/
│   │   ├── ModEntityTypes.java          # Entity type registration
│   │   └── ModBiomeModifications.java   # Spawn condition setup
│   ├── config/
│   │   └── AethelonConfig.java          # Configuration constants
│   └── util/                            # Utility classes (future)
├── src/main/resources/
│   ├── fabric.mod.json                  # Mod metadata
│   ├── aethelon.mixins.json            # Mixin configuration
│   ├── assets/aethelon/
│   │   ├── lang/en_us.json             # Localization
│   │   ├── models/item/                # Item models
│   │   └── textures/entity/            # Entity textures
│   └── data/aethelon/
│       └── structures/                  # Island structure files
└── ROADMAP.md                          # Development roadmap
```

## Current Implementation Status

### ✅ Phase 1 - COMPLETED
- ✅ Project structure setup
- ✅ Basic mod initialization  
- ✅ Entity class foundation
- ✅ Registry framework
- ✅ Configuration structure
- ✅ Biome spawn integration
- ✅ Entity registration with FabricEntityTypeBuilder
- ✅ Entity attributes with FabricDefaultAttributeRegistry
- ✅ Entity model and renderer system
- ✅ Spawn egg for testing
- ✅ Client-side integration
- ✅ Model layer registration

### ⏳ Next Steps
1. Add entity model and renderer
2. Test basic spawning in creative mode
3. Implement entity attributes registration
4. Add spawn egg for testing

## Development Notes

### Key Patterns Used
- **Entity Registration**: Following UntitledDuckMod pattern with FabricEntityTypeBuilder
- **Biome Integration**: Using Fabric BiomeModifications API for spawn control
- **Configuration**: Centralized config class with static constants
- **Structure**: Modular design with separate registry classes

### Testing Strategy
- Use creative mode for initial entity testing
- Implement spawn egg for easy testing
- Add debug commands for development
- Test in deep ocean biomes specifically

### Known Dependencies
- Fabric API (already configured)
- FabricDefaultAttributeRegistry (for entity attributes)
- Entity model and renderer classes (Phase 1)

## Building and Testing

1. **Build the mod**: `./gradlew build`
2. **Run in development**: `./gradlew runClient`
3. **Test spawning**: Use spawn egg or commands in deep ocean biomes
4. **Debug**: Enable debug logging in AethelonConfig.Debug

## Next Phase Requirements

Before moving to Phase 2, ensure:
- [ ] Entity spawns successfully
- [ ] Basic model renders correctly
- [ ] No console errors during spawn
- [ ] Entity has proper hitbox and collision
- [ ] Spawn egg works in creative mode