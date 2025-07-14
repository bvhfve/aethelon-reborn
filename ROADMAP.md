# Aethelon Mod Development Roadmap

## Mod Summary: Aethelon, the World Turtle

The Aethelon mod introduces a colossal, passive world turtle entity that carries a functional, self-contained island on its back. This island is a dynamic structure, loaded from .nbt files, that is captured and re-pasted block-by-block whenever the Aethelon moves. The turtle's behavior is governed by a simple state machine, causing it to idle for long periods before pathfinding through deep oceans to a new coastal location. Players can trigger its movement by damaging it, and killing the Aethelon results in a massive, configurable TNT explosion that destroys the entire island. Aethelons spawn rarely in specific biomes, with their global population and minimum distance being strictly controlled to maintain world balance and a sense of discovery.

## Technical Architecture

**Based on Knowledge Pool Analysis:**
- **Entity Framework**: Fabric EntityTypeBuilder with custom MobEntity extension
- **AI System**: Custom Goal-based AI with state machine (IDLE, MOVING, TRANSITIONING)
- **Biome Integration**: Fabric BiomeModifications API for spawn control
- **Structure System**: StructureTemplate for .nbt loading and block-by-block operations
- **Event Handling**: Fabric lifecycle events for damage detection and world persistence
- **Configuration**: JSON-based config with runtime reloading

## Development Phases

### **Phase 1: Basic Entity Foundation**
**Goal**: Create a basic turtle entity that spawns and exists in the world

**Tasks:**
- Create the main mod class (`Aethelon.java`) using ModInitializer pattern
- Create basic `AethelonEntity` class extending `WaterCreatureEntity` or `MobEntity`
- Implement entity registration using `FabricEntityTypeBuilder` pattern
- Create simple entity model and renderer using Fabric rendering APIs
- Add basic spawn conditions targeting deep ocean biomes via `BiomeModifications`
- Set up entity attributes (health, size, movement speed)

**Success Criteria:**
- Entity spawns in deep ocean biomes
- Basic model renders correctly with proper hitbox
- Entity has appropriate water creature behavior
- No compilation errors

**Reference Patterns:**
- UntitledDuckMod entity registration
- Fabric EntityTypeBuilder API
- Fabric BiomeModifications for spawning

---

### **Phase 2: Entity Behavior & Movement**
**Goal**: Implement the turtle's basic AI and movement patterns

**Tasks:**
- Create custom AI goals extending `Goal` class (IdleGoal, PathfindGoal, TransitionGoal)
- Implement state machine using enum (IDLE, MOVING, TRANSITIONING, DAMAGED)
- Add pathfinding logic for deep ocean to coastal movement using aquatic navigation
- Implement timer-based behavior with configurable idle periods (20-60 minutes)
- Add basic collision detection and obstacle avoidance
- Create custom movement speed modifiers for different states

**Success Criteria:**
- Turtle moves naturally between locations with proper timing
- State transitions work correctly with visual/audio feedback
- Pathfinding functions in ocean environments without getting stuck
- Idle periods are appropriately long and varied

**Reference Patterns:**
- SmartBrainLib behavior system
- Vanilla aquatic entity AI goals
- Custom Goal implementations

---

### **Phase 3: Damage Response & Player Interaction**
**Goal**: Make the turtle respond to player actions

**Tasks:**
- Implement damage detection using Fabric entity events
- Add forced movement trigger when damaged (interrupt idle state)
- Create health system with massive HP pool (1000+ health)
- Add basic particle effects for interactions (damage, state changes)
- Implement death mechanics (without explosion yet) with proper cleanup
- Add damage immunity periods to prevent exploitation
- Create visual feedback for damage (screen shake, particles)

**Success Criteria:**
- Players can interact with and influence turtle behavior
- Damage triggers appropriate state transitions and movement
- Health system functions correctly with proper scaling
- Visual feedback is clear and impactful

**Reference Patterns:**
- Fabric entity event system
- Vanilla boss entity damage handling
- Particle effect APIs

---

### **Phase 4: Island Structure System**
**Goal**: Add the island that sits on the turtle's back

**Tasks:**
- Create island structure loading system using `StructureTemplate` for .nbt files
- Implement block-by-block island placement on turtle spawn with proper offset calculation
- Add island positioning relative to turtle's back using entity attachment system
- Create basic island templates (small, medium, large variants)
- Implement structure validation and error handling
- Add island bounds detection for collision and interaction

**Success Criteria:**
- Turtle spawns with a functional island on its back
- Island structures load correctly from .nbt files without corruption
- Island positioning is accurate, stable, and moves with turtle
- Multiple island variants work correctly

**Reference Patterns:**
- StructureTemplate API usage
- Structure block entity mechanics
- NBT file handling patterns

---

### **Phase 5: Dynamic Island Movement**
**Goal**: Make the island move with the turtle

**Tasks:**
- Implement island capture system using `StructureTemplate.recordBlocksAndEntities()`
- Create island re-placement system with proper world coordinate translation
- Add smooth transition mechanics with gradual block placement/removal
- Implement block-by-block copying with tick-based processing to prevent lag
- Handle chunk loading/unloading during movement with proper async operations
- Add backup/restore system for movement failures
- Implement entity preservation (items, mobs on island)

**Success Criteria:**
- Island successfully moves with turtle without corruption or data loss
- Block data, NBT data, and entities are preserved during movement
- Chunk operations are handled properly without causing lag
- Movement is visually smooth and doesn't break immersion

**Reference Patterns:**
- StructureTemplate capture/place mechanics
- Chunk loading management
- Async block operations

---

### **Phase 6: Explosion & Destruction System**
**Goal**: Implement the dramatic death mechanics

**Tasks:**
- Create configurable explosion system using custom explosion logic
- Implement island destruction on turtle death with proper block removal
- Add explosion size and power configuration (radius, damage, effects)
- Create dramatic visual and audio effects (particles, screen shake, sounds)
- Implement cleanup systems for destroyed blocks and dropped items
- Add explosion immunity for certain blocks (bedrock, etc.)
- Create loot drop system for turtle death rewards

**Success Criteria:**
- Turtle death creates spectacular, configurable explosion
- Island is properly destroyed with appropriate visual feedback
- Visual effects are impressive and performant
- Explosion mechanics are balanced and configurable

**Reference Patterns:**
- Vanilla explosion mechanics
- Custom particle systems
- Block destruction algorithms

---

### **Phase 7: Spawn Control & Population Management**
**Goal**: Implement sophisticated spawn mechanics

**Tasks:**
- Create global population tracking using persistent world data
- Implement minimum distance constraints (5000+ blocks) between turtles
- Add biome-specific spawn conditions using `BiomeSelectors.foundInOverworld()`
- Create rarity controls with configurable spawn rates (1 per 10000 chunks)
- Implement world data persistence using `PersistentState` for turtle tracking
- Add spawn attempt logging and debugging tools
- Create admin commands for manual spawn management

**Success Criteria:**
- Spawn system maintains proper population (max 3-5 per world)
- Distance constraints are enforced reliably
- Population data persists across world sessions and server restarts
- Spawn rates are balanced and configurable

**Reference Patterns:**
- Fabric BiomeModifications spawn control
- PersistentState for world data
- Custom spawn condition logic

---

### **Phase 8: Configuration & Balancing**
**Goal**: Make the mod highly configurable

**Tasks:**
- Create comprehensive config file system using JSON/TOML format
- Add all major parameters as configurable options (spawn rates, health, movement, etc.)
- Implement in-game admin commands for testing and management
- Add debug modes and comprehensive logging system
- Create default balanced configurations for different server types
- Add runtime config reloading without restart
- Implement config validation and error handling

**Success Criteria:**
- All systems work with various configuration settings
- Config changes take effect properly (runtime reloading)
- Debug tools are functional and provide useful information
- Configuration is intuitive and well-documented

**Reference Patterns:**
- Fabric config API patterns
- Command registration system
- Runtime configuration management

---

### **Phase 9: Polish & Optimization**
**Goal**: Optimize performance and add final touches

**Tasks:**
- Optimize chunk loading and block operations
- Add sound effects and ambient audio
- Implement advanced particle systems
- Add loot tables and rewards
- Optimize entity AI and pathfinding

**Success Criteria:**
- Mod runs smoothly in various scenarios
- Performance is acceptable on average hardware
- Audio and visual polish is complete

---

### **Phase 10: Advanced Features & Integration**
**Goal**: Add sophisticated features and mod compatibility

**Tasks:**
- Implement structure variety (multiple island types)
- Add seasonal or time-based behaviors
- Create integration hooks for other mods
- Add advanced player interaction systems
- Implement data pack support for custom islands

**Success Criteria:**
- All advanced features work seamlessly together
- Mod compatibility is maintained
- Data pack system is functional

---

## Key Technical Considerations

### For Each Phase:
- Each phase must compile and run without errors
- Extensive testing required before proceeding to next phase
- Configuration options should be added incrementally
- Performance monitoring throughout development
- Backup systems for world data during island operations
- Proper error handling and edge case management

### Development Tools Needed:
- NBT structure creation tools
- In-game testing commands
- Performance profiling capabilities
- Debug visualization systems

### Technical Architecture:
- **Loader**: Fabric 1.21.4
- **Language**: Java 21
- **Dependencies**: Fabric API
- **Structure Format**: NBT files
- **Configuration**: JSON/TOML based
- **Data Persistence**: World save data

## Development Guidelines

1. **Testing Protocol**: Each phase must be thoroughly tested before moving to the next
2. **Version Control**: Commit after each successful phase completion
3. **Documentation**: Update documentation as features are added
4. **Performance**: Monitor performance impact at each phase
5. **Compatibility**: Ensure compatibility with base Minecraft and Fabric API
6. **Error Handling**: Implement robust error handling for all systems
7. **Configuration**: Make systems configurable from the start
8. **Logging**: Add comprehensive logging for debugging

## Current Status

- **Phase**: Roadmap Refined - Ready for Implementation
- **Next Action**: Create project structure, then begin Phase 1 - Basic Entity Foundation
- **Dependencies**: Project structure setup required

## Implementation Priority

**Immediate Next Steps:**
1. Create project source structure and basic files
2. Set up entity registration framework
3. Implement basic entity class with minimal functionality
4. Add simple model and renderer
5. Test basic spawning in creative mode

**Critical Success Factors:**
- Follow established Fabric patterns from knowledge pool
- Implement robust error handling from the start
- Design for configurability and extensibility
- Test each phase thoroughly before proceeding

---

*This roadmap serves as the master plan for Aethelon mod development. Each phase builds upon the previous one, ensuring a stable and functional mod at every step.*