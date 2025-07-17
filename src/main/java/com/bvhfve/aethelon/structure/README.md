# Aethelon NBT Structure System

This package implements a comprehensive NBT structure registration and spawning system for the Aethelon mod, inspired by patterns from MES (Moog's End Structures) and FabricWaystones.

## Overview

The NBT structure system provides:
- **Structure Registration**: Register custom NBT structures with metadata
- **Dynamic Spawning**: Spawn structures with rotation, mirroring, and processing
- **Biome Adaptation**: Structures adapt to different ocean biomes
- **Structure Processing**: Custom block processing for environmental integration
- **Persistence**: Save/load structure data to/from NBT

## Components

### 1. StructureRegistry
Registers structure pieces and processors with Minecraft's registry system.

```java
// Automatically registers structure components during mod initialization
StructureRegistry.initialize();
```

### 2. NBTStructureManager
Main manager for structure definitions and spawning operations.

```java
// Register a custom structure
NBTStructureManager.registerStructure(new StructureDefinition(
    "my_island",
    Identifier.of("mymod", "structures/my_island"),
    new Vec3d(24, 12, 24), // size
    new Vec3d(0, 0, 0),    // offset
    true,  // allow rotation
    true,  // allow mirroring
    50,    // spawn weight
    Set.of("ocean", "deep_ocean") // biome categories
));

// Spawn a structure
StructureSpawnResult result = NBTStructureManager.spawnStructure(
    serverWorld, "my_island", position, null, null
);
```

### 3. IslandStructurePiece
Custom structure piece for handling NBT-based structures with proper serialization.

### 4. IslandStructureProcessor
Processes blocks during structure placement for environmental adaptation:
- Water level consistency
- Vegetation adaptation
- Structural integrity
- Air pocket prevention

### 5. EnhancedIslandManager
Enhanced island manager that integrates with the NBT structure system.

```java
EnhancedIslandManager manager = new EnhancedIslandManager(turtle);

// Spawn specific structure
manager.spawnIslandStructure(world, "large_village_island");

// Spawn random structure by size and biome
manager.spawnRandomIslandStructure(world, IslandSize.MEDIUM);
```

## Structure Definition Format

```java
new StructureDefinition(
    "structure_name",           // Unique identifier
    templateId,                 // NBT file location
    new Vec3d(width, height, length), // Structure dimensions
    new Vec3d(offsetX, offsetY, offsetZ), // Placement offset
    allowRotation,              // Can be rotated randomly
    allowMirroring,             // Can be mirrored randomly
    spawnWeight,                // Weight for random selection
    biomeCategories             // Compatible biome categories
)
```

## Creating NBT Structure Files

1. **Build in Minecraft**: Create your structure in a test world
2. **Use Structure Blocks**: Save the structure using structure blocks
3. **Export NBT**: Copy the .nbt file from `world/generated/structures/`
4. **Place in Resources**: Put the file in `src/main/resources/data/modid/structures/`

## Biome Categories

The system supports these biome categories:
- `ocean` - Standard ocean biomes
- `deep_ocean` - Deep ocean variants
- `warm_ocean` - Warm ocean biomes
- `lukewarm_ocean` - Lukewarm ocean biomes
- `cold_ocean` - Cold ocean biomes

## Structure Processing

The `IslandStructureProcessor` handles:

### Water Processing
- Ensures water blocks are properly placed at/below sea level
- Converts above-sea-level water to air unless intentional

### Vegetation Processing
- Adapts vegetation to local biome (future enhancement)
- Preserves intentional vegetation placement

### Structural Processing
- Ensures structural block integrity
- Maintains building stability

### Air Processing
- Prevents unwanted air pockets underwater
- Fills underwater air with water when appropriate

## Integration with Existing Systems

The NBT structure system integrates seamlessly with the existing `IslandManager`:

```java
// Original IslandManager automatically uses NBT system when available
islandManager.loadIslandStructure(world, IslandType.MEDIUM);

// Falls back to original structure template system if NBT structure not found
// Falls back to procedural generation if no structure files available
```

## Error Handling

The system provides comprehensive error handling:
- Missing structure files fall back to procedural generation
- Invalid NBT data logs errors and uses defaults
- Structure spawn failures are logged with detailed error messages

## Performance Considerations

- Structure definitions are cached after registration
- Block scanning is optimized for bounding box iteration
- Entity capture is limited to structure bounds
- NBT serialization is minimized for better performance

## Future Enhancements

- **Dynamic Structure Modification**: Modify structures after placement
- **Structure Variants**: Multiple variants per structure type
- **Advanced Biome Adaptation**: Automatic block substitution based on biome
- **Structure Networking**: Sync structure data between client and server
- **Structure Commands**: Admin commands for structure management

## Example Usage

See `EnhancedIslandManager` for a complete implementation example of how to integrate the NBT structure system with entity-based structure management.