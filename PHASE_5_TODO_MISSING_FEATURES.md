# bvhfve's personal TODO note:

Make crafting ingredient items even more useful, by not only using them for crafting, but in an anvil as "gems" to upgrade existing weapons (both vanilla ones and our custom trident. Upgraded weapons will have unique elemental effects based on the type of crafting ingredient used. The upgrade system will have 5 tiers, each tier will increase the damage and effect amplification by 20%. Level 1 will requires 1 gem, 2 more gems with level 2, 3 more gems with level 3, 4 more gems with level 4, and 5 more gems with level 5. A total of 15 gems of any type are needed to max out an upgraded weapon (configurable), and the gems will be gone after every merge (just like enchanted books). NOTE: DO NOT REGISTER MORE CUSTOM ITEMS, and use knowledge_pool/elemental-enchantments-1.21.4 as a reference for the elemental effects


Make the ancient trident have 100% accuracy when thrown (it will directly hit where the crosshair is aimed, no inaccuracy/gravity), based on the logic in knowledge_pool/PerfectAccuracy
- port reimaginedpotato (decompiled illegally from mod jar) from 1.21 to 1.21.4 so that it can be used as knowledge pool
+ before porting, tell rovodev to remove all features EXCEPT the floater item and its moving block logic.
- read "nbt island variants building idea.docx", build the islands with worldedit and export as .nbt 
+ after done building, put .nbt files in their correct place 
- create textures for custom mod items based on the suggestion (use gemini to help)
- gradlew runclient and enable debugging in config.json to verify all features working as intended
+ if there's any bug, report to rovodev to fix

*treasure island's TREASURE CHEST CONTENT: normal chest (nbt: ancient city & buried treasure); trapped chest (nbt: bastion treasure & end city)


# Phase 5 TODO: Missing Features & Implementation Guide

This document outlines the missing features and improvements needed to complete Phase 5 (Dynamic Island Movement) and make it production-ready.

## 📋 **Current Phase 5 Status**

### **✅ Implemented Features**
- Basic moving island framework (`MovingIslandManager`)
- Virtual block tracking system (relative coordinates)
- Island spawning on turtle backs
- Basic movement detection and block transfer
- Integration with datapack structure system
- Entity tracking on islands

### **❌ Missing Critical Features**
The current implementation is a **foundation** but lacks several critical features for production use.

---

## 🚨 **Critical Missing Features**

### **1. Turtle Entity Integration**
**Status**: ⚠️ **NOT IMPLEMENTED**

**Current Issue**: `MovingIslandManager` exists but is not integrated with `AethelonEntity`

**What's Missing**:
```java
// In AethelonEntity.java - MISSING
private MovingIslandManager islandManager;

@Override
public void tick() {
    super.tick();
    
    // MISSING: Update moving island every tick
    if (islandManager != null) {
        islandManager.updateIslandPosition(getWorld());
    }
}

// MISSING: Island spawning methods
public boolean spawnIslandOnBack(String structureName) {
    if (islandManager == null) {
        islandManager = new MovingIslandManager(this);
    }
    return islandManager.spawnMovingIsland(getWorld(), structureName);
}

// MISSING: Island removal methods
public void removeIsland() {
    if (islandManager != null) {
        islandManager.removeMovingIsland(getWorld());
    }
}
```

**Implementation Priority**: 🔴 **CRITICAL**

**Estimated Time**: 2-3 hours

---

### **2. Smooth Movement System**
**Status**: ⚠️ **BASIC IMPLEMENTATION**

**Current Issue**: Blocks are teleported instantly, causing visual glitches

**What's Missing**:
- **Interpolated movement** for smooth visual transitions
- **Movement prediction** to avoid lag
- **Chunk loading management** for large islands
- **Movement batching** to reduce server load

**Implementation Needed**:
```java
public class SmoothMovementSystem {
    private final Queue<MovementStep> movementQueue = new LinkedList<>();
    private final Map<BlockPos, BlockMoveAnimation> activeAnimations = new HashMap<>();
    
    // MISSING: Smooth block movement over multiple ticks
    public void scheduleBlockMovement(BlockPos from, BlockPos to, int durationTicks) {
        // Implement smooth interpolation
    }
    
    // MISSING: Batch movement processing
    public void processBatchedMovements(int maxBlocksPerTick) {
        // Process movements in batches to avoid lag
    }
    
    // MISSING: Movement prediction
    public void predictMovement(Vec3d turtleVelocity) {
        // Pre-calculate next positions to reduce lag
    }
}
```

**Implementation Priority**: 🟡 **HIGH**

**Estimated Time**: 6-8 hours

---

### **3. Entity Management System**
**Status**: ⚠️ **BASIC TRACKING ONLY**

**Current Issue**: Entities are tracked but not properly managed during movement

**What's Missing**:
- **Entity collision prevention** during movement
- **Entity state preservation** (sitting, leashed, etc.)
- **Player handling** when standing on moving islands
- **Entity physics** during movement
- **Villager AI preservation** on moving islands

**Implementation Needed**:
```java
public class IslandEntityManager {
    private final Map<Entity, EntityIslandData> entityData = new HashMap<>();
    
    // MISSING: Proper entity movement with physics
    public void moveEntityWithIsland(Entity entity, Vec3d movement) {
        // Preserve entity state and physics
        // Handle special cases (players, villagers, animals)
    }
    
    // MISSING: Entity collision management
    public void preventCollisionDuringMovement(Entity entity) {
        // Temporarily disable certain collisions during movement
    }
    
    // MISSING: Player camera smoothing
    public void smoothPlayerCameraMovement(PlayerEntity player, Vec3d movement) {
        // Prevent jarring camera movements for players on islands
    }
    
    // MISSING: Villager AI preservation
    public void preserveVillagerAI(VillagerEntity villager) {
        // Maintain villager jobs, trades, and AI during movement
    }
}
```

**Implementation Priority**: 🟡 **HIGH**

**Estimated Time**: 8-10 hours

---

### **4. Chunk Loading Management**
**Status**: ❌ **NOT IMPLEMENTED**

**Current Issue**: Moving islands may cause chunk loading issues

**What's Missing**:
- **Dynamic chunk loading** around moving turtles
- **Chunk unloading** behind turtles to save memory
- **Cross-chunk movement** handling
- **Chunk border collision** prevention

**Implementation Needed**:
```java
public class IslandChunkManager {
    private final Set<ChunkPos> loadedChunks = new HashSet<>();
    private final AethelonEntity turtle;
    
    // MISSING: Dynamic chunk loading
    public void ensureChunksLoaded(Vec3d center, int radius) {
        // Load chunks around moving island
    }
    
    // MISSING: Chunk cleanup
    public void unloadDistantChunks(Vec3d center, int maxDistance) {
        // Unload chunks that are too far away
    }
    
    // MISSING: Cross-chunk movement handling
    public void handleChunkBorderCrossing(Vec3d oldPos, Vec3d newPos) {
        // Manage blocks moving across chunk boundaries
    }
}
```

**Implementation Priority**: 🟡 **HIGH**

**Estimated Time**: 4-6 hours

---

### **5. Performance Optimization**
**Status**: ⚠️ **BASIC OPTIMIZATION**

**Current Issue**: May cause lag with large islands or multiple turtles

**What's Missing**:
- **Movement throttling** based on server performance
- **Block update batching** to reduce lag spikes
- **Distance-based optimization** (LOD for distant islands)
- **Memory management** for large island data

**Implementation Needed**:
```java
public class IslandPerformanceManager {
    private final PerformanceMetrics metrics = new PerformanceMetrics();
    
    // MISSING: Adaptive performance scaling
    public void adjustPerformanceSettings(ServerWorld world) {
        // Reduce movement frequency if server is lagging
        // Batch more operations if performance is good
    }
    
    // MISSING: Level-of-detail system
    public void applyLODOptimizations(Vec3d playerPosition) {
        // Reduce update frequency for distant islands
        // Simplify movement for far-away turtles
    }
    
    // MISSING: Memory management
    public void optimizeMemoryUsage() {
        // Clean up unused island data
        // Compress stored block information
    }
}
```

**Implementation Priority**: 🟡 **HIGH**

**Estimated Time**: 6-8 hours

---

## 🔧 **Quality of Life Features**

### **6. Movement Configuration**
**Status**: ❌ **NOT IMPLEMENTED**

**What's Missing**:
```java
public class MovementConfig {
    // MISSING: Configurable movement settings
    public static final double MOVEMENT_THRESHOLD = 0.1; // Minimum movement to trigger update
    public static final int MAX_BLOCKS_PER_TICK = 50;    // Performance limit
    public static final boolean SMOOTH_MOVEMENT = true;   // Enable smooth transitions
    public static final int MOVEMENT_ANIMATION_TICKS = 10; // Animation duration
    
    // MISSING: Per-island settings
    public static class IslandMovementSettings {
        public boolean allowMovement = true;
        public double movementMultiplier = 1.0;
        public boolean preserveEntities = true;
        public boolean smoothMovement = true;
    }
}
```

**Implementation Priority**: 🟢 **MEDIUM**

**Estimated Time**: 2-3 hours

---

### **7. Visual Effects**
**Status**: ❌ **NOT IMPLEMENTED**

**What's Missing**:
- **Water wake effects** behind moving turtles
- **Particle effects** during island movement
- **Sound effects** for movement and island placement
- **Visual indicators** for island boundaries

**Implementation Needed**:
```java
public class IslandVisualEffects {
    // MISSING: Water wake particles
    public void spawnWakeParticles(Vec3d turtlePos, Vec3d velocity) {
        // Create water splash and wake effects
    }
    
    // MISSING: Movement particles
    public void spawnMovementParticles(Vec3d islandCenter) {
        // Show magical/mystical effects during movement
    }
    
    // MISSING: Sound effects
    public void playMovementSounds(Vec3d position, float intensity) {
        // Water sounds, creaking, magical effects
    }
    
    // MISSING: Island boundary visualization
    public void renderIslandBounds(Vec3d center, Vec3d size) {
        // Debug visualization for island boundaries
    }
}
```

**Implementation Priority**: 🟢 **MEDIUM**

**Estimated Time**: 4-5 hours

---

### **8. Island Persistence**
**Status**: ⚠️ **BASIC NBT SUPPORT**

**Current Issue**: Island data may not persist correctly across world saves

**What's Missing**:
- **World save integration** for moving islands
- **Island state serialization** (position, entities, blocks)
- **Cross-session continuity** for moving islands
- **Backup and recovery** for corrupted island data

**Implementation Needed**:
```java
public class IslandPersistenceManager {
    // MISSING: Complete NBT serialization
    public NbtCompound saveIslandToNbt(MovingIslandManager island) {
        NbtCompound nbt = new NbtCompound();
        // Save all island data including relative positions
        // Save entity data and states
        // Save movement history and current velocity
        return nbt;
    }
    
    // MISSING: NBT deserialization
    public MovingIslandManager loadIslandFromNbt(NbtCompound nbt, AethelonEntity turtle) {
        // Restore island from saved data
        // Recreate entity relationships
        // Restore movement state
        return null;
    }
    
    // MISSING: World save hooks
    public void onWorldSave(ServerWorld world) {
        // Save all active moving islands
    }
    
    // MISSING: World load hooks
    public void onWorldLoad(ServerWorld world) {
        // Restore all moving islands
    }
}
```

**Implementation Priority**: 🟡 **HIGH**

**Estimated Time**: 5-7 hours

---

## 🐛 **Known Issues to Fix**

### **9. Block Update Issues**
**Current Problems**:
- Redstone circuits may break during movement
- Flowing water/lava doesn't move properly
- Block entities (chests, furnaces) may lose data
- Crop growth states not preserved

**Fixes Needed**:
```java
public class BlockUpdateManager {
    // MISSING: Redstone circuit preservation
    public void preserveRedstoneState(BlockPos pos, BlockState state) {
        // Maintain redstone power levels and connections
    }
    
    // MISSING: Fluid movement
    public void moveFluidBlocks(BlockPos from, BlockPos to) {
        // Properly transfer flowing water/lava
    }
    
    // MISSING: Block entity data preservation
    public void preserveBlockEntityData(BlockPos pos) {
        // Maintain chest contents, furnace progress, etc.
    }
}
```

**Implementation Priority**: 🔴 **CRITICAL**

**Estimated Time**: 4-6 hours

---

### **10. Collision Detection**
**Current Problems**:
- Islands may collide with terrain
- No collision between multiple moving islands
- Players may fall through moving blocks

**Fixes Needed**:
```java
public class IslandCollisionManager {
    // MISSING: Terrain collision detection
    public boolean checkTerrainCollision(Vec3d newPosition, Vec3d islandSize) {
        // Prevent islands from moving into solid terrain
        return false;
    }
    
    // MISSING: Island-to-island collision
    public boolean checkIslandCollision(MovingIslandManager island1, MovingIslandManager island2) {
        // Prevent multiple islands from occupying same space
        return false;
    }
    
    // MISSING: Player safety checks
    public void ensurePlayerSafety(PlayerEntity player, Vec3d movement) {
        // Prevent players from falling through moving blocks
    }
}
```

**Implementation Priority**: 🔴 **CRITICAL**

**Estimated Time**: 6-8 hours

---

## 📊 **Implementation Priority Matrix**

### **🔴 Critical (Must Implement Next)**
1. **Turtle Entity Integration** (2-3 hours)
2. **Block Update Issues** (4-6 hours)  
3. **Collision Detection** (6-8 hours)

**Total Critical Work**: ~12-17 hours

### **🟡 High Priority (Important for Production)**
1. **Smooth Movement System** (6-8 hours)
2. **Entity Management System** (8-10 hours)
3. **Chunk Loading Management** (4-6 hours)
4. **Performance Optimization** (6-8 hours)
5. **Island Persistence** (5-7 hours)

**Total High Priority Work**: ~29-39 hours

### **🟢 Medium Priority (Quality of Life)**
1. **Movement Configuration** (2-3 hours)
2. **Visual Effects** (4-5 hours)

**Total Medium Priority Work**: ~6-8 hours

---

## 🎯 **Implementation Roadmap**

### **Phase 5.1: Core Functionality (Week 1)**
- [ ] Implement turtle entity integration
- [ ] Fix block update issues
- [ ] Add basic collision detection
- [ ] Test with simple islands

### **Phase 5.2: Stability & Performance (Week 2)**
- [ ] Implement smooth movement system
- [ ] Add entity management
- [ ] Implement chunk loading management
- [ ] Performance optimization

### **Phase 5.3: Polish & Features (Week 3)**
- [ ] Complete island persistence
- [ ] Add movement configuration
- [ ] Implement visual effects
- [ ] Comprehensive testing

### **Phase 5.4: Production Ready (Week 4)**
- [ ] Bug fixes and edge cases
- [ ] Performance tuning
- [ ] Documentation updates
- [ ] Final testing and validation

---

## 🧪 **Testing Strategy**

### **Unit Tests Needed**
```java
// MISSING: Test classes
public class MovingIslandManagerTest {
    // Test island spawning
    // Test movement calculations
    // Test entity tracking
    // Test performance under load
}

public class IslandCollisionTest {
    // Test terrain collision detection
    // Test island-to-island collision
    // Test player safety
}

public class IslandPersistenceTest {
    // Test NBT save/load
    // Test world restart continuity
    // Test data corruption recovery
}
```

### **Integration Tests**
- Multiple turtles with islands
- Large islands (32x32x16)
- Islands with complex redstone
- Islands with many entities
- Long-distance movement
- Server restart scenarios

### **Performance Benchmarks**
- Maximum number of moving islands
- Acceptable movement frequency
- Memory usage limits
- Server TPS impact

---

## 📝 **Implementation Notes**

### **Code Architecture Suggestions**
```java
// Recommended class structure
public class MovingIslandManager {
    private final IslandMovementSystem movementSystem;
    private final IslandEntityManager entityManager;
    private final IslandChunkManager chunkManager;
    private final IslandCollisionManager collisionManager;
    private final IslandPersistenceManager persistenceManager;
    private final IslandPerformanceManager performanceManager;
    
    // Coordinate all subsystems
}
```

### **Configuration Integration**
```java
// Add to AethelonConfig.java
public static class MovingIslandConfig {
    public boolean enableMovingIslands = true;
    public double movementThreshold = 0.1;
    public int maxBlocksPerTick = 50;
    public boolean smoothMovement = true;
    public boolean enableVisualEffects = true;
    public int chunkLoadRadius = 3;
}
```

### **Event System**
```java
// MISSING: Event system for island movement
public class IslandMovementEvents {
    public static final Event<IslandMoveCallback> ISLAND_MOVE_START = EventFactory.createArrayBacked(IslandMoveCallback.class, callbacks -> (island, from, to) -> {
        for (IslandMoveCallback callback : callbacks) {
            callback.onIslandMove(island, from, to);
        }
    });
    
    @FunctionalInterface
    public interface IslandMoveCallback {
        void onIslandMove(MovingIslandManager island, Vec3d from, Vec3d to);
    }
}
```

---

## 🎯 **Success Criteria**

### **Phase 5 Complete When**:
- [ ] Islands move smoothly with turtles
- [ ] No visual glitches or lag spikes
- [ ] Entities stay on islands during movement
- [ ] Redstone circuits work on moving islands
- [ ] Multiple islands can move simultaneously
- [ ] Islands persist across world saves
- [ ] Performance is acceptable (>15 TPS with 5+ moving islands)
- [ ] No collision or safety issues
- [ ] Comprehensive test coverage
- [ ] Documentation is complete

**Estimated Total Implementation Time**: 47-64 hours

**Recommended Timeline**: 3-4 weeks of focused development

---

This document provides a complete roadmap for finishing Phase 5. Focus on the **Critical** items first to get basic functionality working, then move through the priority levels to achieve production quality.