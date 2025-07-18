# Aethelon Testing & Next Phase Guide

This document outlines what to test before moving to the next development phase, how to create NBT structures, and suggestions for missing item textures.

## 🧪 **Phase 4 & 5 Testing Checklist**

### **Turtle Shell Shield Testing** ⚔️

#### **Quick Crafting Guide**
To easily test the turtle shell shield, use these commands in creative mode or with cheats:

```mcfunction
# Give yourself the crafting materials
/give @p aethelon:turtle_shell_fragment 6
/give @p aethelon:deep_sea_pearl 1

# Or give the shield directly
/give @p aethelon:turtle_shell_shield 1
```

#### **Crafting Recipe**
```
[S][P][S]
[S][S][S]
[ ][S][ ]

S = Turtle Shell Fragment
P = Deep Sea Pearl
```

#### **Testing Checklist**
- [ ] **Crafting**: Recipe works correctly with turtle shell fragments and deep sea pearl
- [ ] **Durability**: Shield has 1008 durability (check with F3+H advanced tooltips) ✅ **FIXED**
- [ ] **Blocking**: Can block melee attacks (reduces damage)
- [ ] **Projectile Defense**: Blocks arrows, crossbow bolts, and other projectiles
- [ ] **Shield Animations**: Proper blocking animation when right-clicking
- [ ] **Enchantments**: Can be enchanted with Unbreaking, Mending, etc.
- [ ] **Repair**: Can be repaired with turtle shell fragments in anvil
- [ ] **Creative Tab**: Appears in Combat creative tab
- [ ] **Sound Effects**: Makes proper shield blocking sounds
- [ ] **Visual**: Shield model displays correctly in hand and when blocking

#### **Recent Fixes Applied** 🔧
- ✅ **Durability Issue**: Fixed shield durability registration (now properly shows 1008)
- ✅ **Recipe Issue**: Recreated recipe file with proper formatting
- ✅ **Build Issue**: Fixed entity registration order to prevent crashes
- ✅ **Durability Loss**: Implemented mixin solution based on Enderite mod approach
- ✅ **Repair System**: Added turtle shell fragment repair compatibility

#### **Final Solution** 🎯
**Problem**: Shield wasn't losing durability when blocking damage
**Solution**: Created `TurtleShellShieldMixin` that injects into `PlayerEntity.damageShield()` method
**Inspiration**: Based on Enderite mod's proven approach for custom shields

#### **Current Status** 📊
- **Durability Display**: ✅ Shows 1008 max durability (3x vanilla)
- **Durability Loss**: ✅ Properly loses durability when blocking (via mixin)
- **Shield Functionality**: ✅ Blocks attacks and projectiles
- **Crafting Recipe**: ✅ Works with turtle shell fragments + deep sea pearl
- **Repair**: ✅ Can be repaired with turtle shell fragments in anvil
- **Build**: ✅ Compiles without errors

#### **Performance Testing**
- [ ] **Multiple Shields**: No lag when multiple players use shields
- [ ] **Rapid Blocking**: No issues with rapid block/unblock actions
- [ ] **Durability Loss**: Proper durability reduction when blocking damage

### **Core Structure System Testing**

#### **1. DatapackStructureManager Testing**
- [ ] **Basic Structure Spawning**
  ```java
  // Test in a test world or development environment
  DatapackStructureManager.spawnStructure(world, "small_island", position);
  ```
  - Verify structure spawns at correct position
  - Check that blocks are placed correctly
  - Ensure no console errors during spawning

- [ ] **Random Structure Selection**
  ```java
  DatapackStructureManager.spawnRandomStructure(world, "medium", position, "ocean");
  ```
  - Test different size categories: "small", "medium", "large"
  - Verify weighted random selection works
  - Check biome category filtering

- [ ] **Structure Registration**
  - Verify all default structures are registered on mod startup
  - Check console logs for successful registration messages
  - Test `hasStructure()` method returns correct values

#### **2. MovingIslandManager Testing (Phase 5)**
- [ ] **Island Spawning on Turtle**
  ```java
  MovingIslandManager manager = new MovingIslandManager(turtle);
  boolean success = manager.spawnMovingIsland(world, "village_island");
  ```
  - Spawn turtle entity in test world
  - Spawn island on turtle's back
  - Verify island appears above turtle

- [ ] **Dynamic Movement**
  - Move turtle using commands or creative mode
  - Verify island moves with turtle
  - Check that blocks are transferred correctly
  - Ensure no blocks are left behind or duplicated

- [ ] **Entity Preservation**
  - Place entities (animals, villagers) on island
  - Move turtle and verify entities move with island
  - Check entity positions remain relative to island

- [ ] **Performance Testing**
  - Test with large islands (32x32)
  - Monitor server performance during movement
  - Verify movement only triggers when turtle moves significantly

#### **3. Integration Testing**
- [ ] **IslandManager Integration**
  - Test fallback to original structure system
  - Verify datapack system takes priority when available
  - Check error handling when structures are missing

- [ ] **Mixin Integration**
  - Verify StructureTemplateMixin logs structure placement
  - Check no conflicts with other mods
  - Ensure clean integration with Minecraft's structure system

### **Error Handling Testing**
- [ ] **Missing Structure Files**
  - Test behavior when .nbt files are missing
  - Verify graceful fallback to procedural generation
  - Check error messages are informative

- [ ] **Invalid Structure Data**
  - Test with corrupted .nbt files
  - Verify system handles invalid structure IDs
  - Check exception handling doesn't crash the game

- [ ] **World Compatibility**
  - Test in single-player worlds
  - Test on dedicated servers
  - Verify client-server synchronization

## 🏗️ **Creating NBT Structures**

### **Method 1: Using Structure Blocks (Recommended)**

#### **Step 1: Build Your Structure**
1. Create a test world in Creative mode
2. Build your island structure
   - **Small islands**: 16x8x16 blocks max
   - **Medium islands**: 24x12x24 blocks max  
   - **Large islands**: 32x16x32 blocks max
3. Include variety: terrain, vegetation, buildings, decorations

#### **Step 2: Save with Structure Blocks**
1. Place a **Structure Block** at one corner of your structure
2. Set mode to "Save"
3. Configure the structure:
   ```
   Structure Name: small_tropical_island
   Size: [measure your structure dimensions]
   Include entities: ✓ (if you want mobs/villagers)
   ```
4. Click "Save" to create the .nbt file

#### **Step 3: Export the Structure**
1. Navigate to your world folder:
   ```
   .minecraft/saves/[WorldName]/generated/structures/
   ```
2. Find your .nbt file (e.g., `small_tropical_island.nbt`)
3. Copy to your mod's structure folder:
   ```
   aethelon/src/main/resources/data/aethelon/structures/
   ```

#### **Step 4: Register the Structure**
Add to `DatapackStructureManager.initialize()`:
```java
registerStructure(new IslandStructureDefinition(
    "small_tropical_island",                           // Structure name
    Identifier.of(Aethelon.MOD_ID, "small_tropical_island"), // Must match .nbt filename
    new Vec3d(16, 10, 16),                            // Approximate size
    80,                                               // Spawn weight
    Set.of("warm_ocean", "lukewarm_ocean"),           // Compatible biomes
    true, true                                        // Allow rotation/mirroring
));
```

### **Method 2: Using WorldEdit (Advanced)**

#### **Prerequisites**
- Install WorldEdit mod in your development environment
- Familiar with WorldEdit commands

#### **Steps**
1. Build your structure in a test world
2. Select the area with WorldEdit:
   ```
   //pos1  (at one corner)
   //pos2  (at opposite corner)
   ```
3. Copy the selection:
   ```
   //copy
   ```
4. Save as schematic:
   ```
   //schem save my_island
   ```
5. Convert schematic to NBT using external tools or WorldEdit's export features

### **Method 3: Using Litematica (Alternative)**

#### **Prerequisites**
- Install Litematica mod
- Familiar with Litematica workflow

#### **Steps**
1. Build structure in test world
2. Create Litematica schematic
3. Use Litematica's NBT export feature
4. Place exported .nbt in mod structure folder

## 📁 **Structure File Organization**

### **Recommended Folder Structure**
```
aethelon/src/main/resources/data/aethelon/structures/
├── islands/
│   ├── small/
│   │   ├── small_island.nbt
│   │   ├── small_tropical_island.nbt
│   │   ├── small_rocky_island.nbt
│   │   └── small_coral_island.nbt
│   ├── medium/
│   │   ├── medium_island.nbt
│   │   ├── medium_forest_island.nbt
│   │   ├── medium_desert_island.nbt
│   │   └── medium_village_island.nbt
│   └── large/
│       ├── large_island.nbt
│       ├── large_village_island.nbt
└── special/
    ├── boss_island.nbt
    ├── treasure_island.nbt
    └── mystical_island.nbt
```

### **Naming Conventions**
- Use lowercase with underscores: `small_tropical_island.nbt`
- Include size category in name: `medium_village_island.nbt`
- Be descriptive: `large_fortress_island.nbt` vs `large_island_3.nbt`

## 🎨 **Missing Item Textures**

### **Current Items Needing Textures**

#### **1. Ancient Items**
- **ancient_compass.png** (16x16)
  - Suggested design: Weathered compass with coral/barnacle details
  - Color palette: Teal, bronze, weathered green
  - Style: Mystical, ocean-themed

- **ancient_trident.png** (16x16)  
  - Suggested design: Ornate trident with sea creature motifs
  - Color palette: Deep blue, silver, aqua accents
  - Style: Powerful, legendary weapon

- **ancient_turtle_scale.png** (16x16)
  - Suggested design: Large, iridescent scale with growth rings
  - Color palette: Green-blue gradient, pearl-like shine
  - Style: Natural, valuable material

#### **2. Turtle Shell Equipment**
- **turtle_shell_helmet.png** (16x16)
  - Suggested design: Dome-shaped helmet with shell pattern
  - Color palette: Brown, green, natural shell colors
  - Style: Protective, organic

- **turtle_shell_chestplate.png** (16x16)
  - Suggested design: Segmented chest armor mimicking shell plates
  - Color palette: Brown, tan, with shell ridge details
  - Style: Sturdy, natural armor

- **turtle_shell_leggings.png** (16x16)
  - Suggested design: Scaled leg protection with shell segments
  - Color palette: Matching chestplate colors
  - Style: Flexible, protective

- **turtle_shell_boots.png** (16x16)
  - Suggested design: Webbed boots with shell reinforcement
  - Color palette: Brown base with webbing details
  - Style: Aquatic, protective footwear

- **turtle_shell_shield.png** (16x16)
  - Suggested design: Round shield with turtle shell pattern
  - Color palette: Natural shell colors with metal rim
  - Style: Defensive, organic
  - **IMPLEMENTED**: ✅ Fully functional shield with tripled durability (1008 vs vanilla 336)

#### **3. Crafting Materials**
- **crystallized_water.png** (16x16)
  - Suggested design: Crystal-like water droplet with inner glow
  - Color palette: Light blue, white, transparent effects
  - Style: Magical, pure essence

- **deep_sea_pearl.png** (16x16)
  - Suggested design: Large, lustrous pearl with depth
  - Color palette: White, pink, iridescent highlights
  - Style: Valuable, oceanic treasure

- **island_essence.png** (16x16)
  - Suggested design: Swirling essence with earth/water elements
  - Color palette: Green, blue, earth tones
  - Style: Mystical, concentrated nature

- **turtle_heart.png** (16x16)
  - Suggested design: Stylized heart with life energy
  - Color palette: Red, pink, with pulsing effect suggestion
  - Style: Vital, life-giving

#### **4. Equipment Items**
- **aquatic_boots.png** (16x16)
  - Suggested design: Streamlined boots with fin details
  - Color palette: Blue, teal, with metallic accents
  - Style: Hydrodynamic, advanced

- **turtle_scale_block.png** (16x16)
  - Suggested design: Block texture with overlapping scales
  - Color palette: Green, brown, natural scale colors
  - Style: Building material, organic

### **Texture Creation Guidelines**

#### **Technical Requirements**
- **Resolution**: 16x16 pixels (standard Minecraft item size)
- **Format**: PNG with transparency support
- **Color depth**: 32-bit RGBA
- **Style**: Match Minecraft's pixel art aesthetic

#### **Design Principles**
1. **Readability**: Clear at small sizes
2. **Consistency**: Match Minecraft's art style
3. **Contrast**: Good visibility against various backgrounds
4. **Theme coherence**: Ocean/turtle theme throughout

#### **Recommended Tools**
- **Aseprite** (paid, excellent for pixel art)
- **GIMP** (free, good for beginners)
- **Photoshop** (paid, professional)
- **Paint.NET** (free, simple)
- **Piskel** (free, web-based)

#### **File Locations**
Place texture files in:
```
aethelon/src/main/resources/assets/aethelon/textures/item/
├── ancient_compass.png
├── ancient_trident.png
├── ancient_turtle_scale.png
├── turtle_shell_helmet.png
├── turtle_shell_chestplate.png
├── turtle_shell_leggings.png
├── turtle_shell_boots.png
├── turtle_shell_shield.png
├── crystallized_water.png
├── deep_sea_pearl.png
├── island_essence.png
├── turtle_heart.png
├── aquatic_boots.png
└── turtle_scale_block.png
```

## 🚀 **Next Phase Preparation**

### **Phase 6: Advanced Features**
After completing testing and content creation:

#### **Potential Features to Implement**
1. **Turtle Breeding System**
   - Turtle eggs and baby turtles
   - Growth mechanics
   - Genetic traits

2. **Island Customization**
   - Player-built structures on islands
   - Island upgrade system
   - Terraforming tools

3. **Ocean Exploration**
   - Underwater dungeons
   - Deep sea biomes
   - Treasure hunting

4. **Multiplayer Features**
   - Turtle trading
   - Island visiting
   - Cooperative building

#### **Technical Improvements**
1. **Performance Optimization**
   - Chunk loading optimization
   - Entity culling for distant turtles
   - Structure caching

2. **API Expansion**
   - Developer API for custom structures
   - Event system for turtle interactions
   - Configuration API

3. **Compatibility**
   - Biome mod integration
   - Structure mod compatibility
   - Shader support

### **Success Criteria for Phase Completion**
- [ ] All core features tested and working
- [ ] At least 3 custom island structures created
- [ ] All item textures implemented
- [ ] No critical bugs or crashes
- [ ] Performance acceptable with multiple turtles
- [ ] Documentation complete and accurate

## 📋 **Testing Checklist Summary**

### **Before Moving to Next Phase**
- [ ] **Structure System**: All spawning and registration working
- [ ] **Moving Islands**: Dynamic movement fully functional
- [ ] **Error Handling**: Graceful failure modes tested
- [ ] **Performance**: Acceptable with multiple islands
- [ ] **Content**: At least 3 custom structures created
- [ ] **Textures**: All missing textures implemented
- [ ] **Documentation**: User guides and API docs complete
- [ ] **Compatibility**: Works in single-player and multiplayer
- [ ] **Integration**: Clean integration with existing systems

### **Quality Gates**
1. **Functionality**: All features work as designed
2. **Stability**: No crashes or major bugs
3. **Performance**: Smooth gameplay with multiple turtles
4. **Content**: Sufficient variety in structures and items
5. **Polish**: Professional appearance and user experience

Once all items are checked off, the system will be ready for the next development phase! 🎯