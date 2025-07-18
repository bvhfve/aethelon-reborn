# Aethelon Testing & Next Phase Guide

This comprehensive document outlines all testing procedures for the Aethelon mod, including newly implemented features like the Advanced Ancient Trident, Gem Upgrade System, and Perfect Accuracy mechanics. Use this guide to systematically test all mod features before moving to the next development phase.

## 🧪 **Phase 4 & 5 Testing Checklist**

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

## 🔱 **Advanced Ancient Trident Testing**

### **Enhanced Underwater Powers**
- [ ] **Underwater Activation**
  - Dive fully underwater and right-click with Ancient Trident
  - Verify effects: Dolphin's Grace (20s), Water Breathing (30s), Night Vision (30s)
  - Check bubble particle effects spawn around player
  - Confirm trident name changes to "Ancient Trident of the Depths"
  - Test 5-second cooldown prevents spam

- [ ] **Aquatic Creature Control**
  - Stand in rain or shallow water (not fully submerged)
  - Right-click with Ancient Trident near fish, squids, dolphins
  - Verify creatures get glowing effect and heart particles
  - Check actionbar message shows number of controlled creatures
  - Test 16-block range and 10-second duration

### **Enhanced Combat**
- [ ] **Projectile Enhancement**
  - Throw trident and verify 1.5x increased speed
  - Test enhanced damage in aquatic environments
  - Attack aquatic mobs to test 30% pacification chance

- [ ] **Perfect Accuracy System**
  - Throw Ancient Trident at distant targets
  - Verify trident flies exactly where crosshair is aimed
  - Test that there's no gravity drop or inaccuracy
  - Compare with regular trident accuracy
  - Test at various distances and angles

- [ ] **Tooltip System**
  - Hover over trident to see ability descriptions
  - Use underwater ability and check for "Awakened Form" tooltip
  - Verify all formatting and colors display correctly

### **Configuration Testing**
- [ ] **Modify Constants** (in AncientTridentItem.java)
  ```java
  DOLPHINS_GRACE_DURATION = 400;    // Test different durations
  AQUATIC_CONTROL_RANGE = 16;       // Test different ranges
  COOLDOWN_TICKS = 100;             // Test cooldown timing
  ```

## 🔮 **Gem Upgrade System Testing**

### **Basic Upgrade Mechanics**
- [ ] **Anvil Upgrade Process**
  - Hold weapon in one hand, gems in other hand
  - Right-click any anvil to trigger upgrade
  - Verify upgrade success message appears
  - Check weapon name changes to show tier (e.g., "Iron Sword [Aquatic II]")
  - Test 12% anvil damage chance per upgrade

- [ ] **Upgrade Progression**
  - Test tier 1: Requires 1 gem
  - Test tier 2: Requires 2 more gems (3 total)
  - Test tier 3: Requires 3 more gems (6 total)
  - Test tier 4: Requires 4 more gems (10 total)
  - Test tier 5: Requires 5 more gems (15 total)
  - Verify maximum tier reached message

- [ ] **Gem Type Consistency**
  - Start upgrade with one gem type (e.g., Turtle Scale)
  - Try to upgrade with different gem type - should fail
  - Verify error message about mixing gem types
  - Complete upgrade with same gem type - should succeed

### **Elemental Effects Testing**

#### **🌊 Aquatic Enhancement (Turtle Scale)**
- [ ] **Combat Effects**
  - Attack enemies with upgraded weapon
  - Verify enemies get slowness effect (5-13 seconds)
  - Check bubble particles appear around target
  - Confirm wielder gets water breathing if not already present
  - Test fish swimming sound plays

#### **❤️ Vitality Enhancement (Turtle Heart)**
- [ ] **Healing & Weakness**
  - Attack enemies and verify wielder heals (1-5 hearts per tier)
  - Check enemies get weakness effect (4-9 seconds)
  - Verify heart particles appear above wielder
  - Test weakness particles appear on target
  - Confirm level up sound plays

#### **❄️ Frost Enhancement (Crystallized Water)**
- [ ] **Freezing Effects**
  - Attack enemies and verify they get frozen (3-12 seconds)
  - Check slowness effect applies with freeze
  - Verify snowflake particles spawn around target
  - Test glass breaking sound effect

#### **⚡ Lightning Enhancement (Deep Sea Pearl)**
- [ ] **Chain Lightning**
  - Attack enemy near other mobs
  - Verify 30-60% chance for chain lightning to nearby entities
  - Check glowing effect on primary target
  - Test electric spark particles
  - Confirm thunder sound effect
  - Verify chain damage affects up to [tier] nearby entities

#### **🌍 Earth Enhancement (Island Essence)**
- [ ] **Launch & Fatigue**
  - Attack enemies and verify upward launch (0.5-1.3 blocks)
  - Check mining fatigue effect applies (5-10 seconds)
  - Test block crack particles from ground
  - Confirm stone breaking sound

### **Damage Scaling Testing**
- [ ] **Tier 1**: +20% damage bonus
- [ ] **Tier 2**: +40% damage bonus
- [ ] **Tier 3**: +60% damage bonus
- [ ] **Tier 4**: +80% damage bonus
- [ ] **Tier 5**: +100% damage bonus

### **Tooltip System Testing**
- [ ] **Unupgraded Weapons**
  - Hover over compatible weapons
  - Verify "Can be upgraded with gems at an anvil" hint appears

- [ ] **Upgraded Weapons**
  - Check tier display (e.g., "Aquatic Tier III")
  - Verify damage bonus percentage shown
  - Confirm elemental effect description appears
  - Test all formatting and colors correct

### **Weapon Compatibility Testing**
- [ ] **Vanilla Swords**: Wood, Stone, Iron, Gold, Diamond, Netherite
- [ ] **Vanilla Axes**: Wood, Stone, Iron, Gold, Diamond, Netherite  
- [ ] **Vanilla Trident**: Regular trident
- [ ] **Ancient Trident**: Custom mod trident

## 🎯 **Perfect Accuracy System Testing**

### **Ancient Trident Accuracy**
- [ ] **Basic Accuracy Test**
  - Throw Ancient Trident at distant target (50+ blocks)
  - Verify trident flies exactly where crosshair is aimed
  - Compare with regular trident - should show clear difference
  - Test at various angles (up, down, diagonal)

- [ ] **Gravity Elimination**
  - Throw trident horizontally at long distance
  - Verify no gravity drop occurs
  - Test that trident maintains straight line flight
  - Compare trajectory with regular trident

- [ ] **Range Testing**
  - Test accuracy at 10, 25, 50, 100+ block distances
  - Verify consistent accuracy at all ranges
  - Test in different environments (underwater, in air, in rain)

### **Tooltip Verification**
- [ ] **Perfect Accuracy Indicator**
  - Hover over Ancient Trident
  - Verify "Perfect accuracy when thrown" appears in tooltip
  - Check "Thrown tridents fly exactly where aimed" description

## 🌊 **Advanced Ancient Trident Features**

### **Enhanced Underwater Powers**
- [ ] **Underwater Activation**
  - Dive fully underwater and right-click with Ancient Trident
  - Verify effects: Dolphin's Grace (20s), Water Breathing (30s), Night Vision (30s)
  - Check bubble particle effects spawn around player
  - Confirm trident name changes to "Ancient Trident of the Depths"
  - Test 5-second cooldown prevents spam

- [ ] **Aquatic Creature Control**
  - Stand in rain or shallow water (not fully submerged)
  - Right-click with Ancient Trident near fish, squids, dolphins
  - Verify creatures get glowing effect and heart particles
  - Check actionbar message shows number of controlled creatures
  - Test 16-block range and 10-second duration

### **Enhanced Combat**
- [ ] **Projectile Enhancement**
  - Throw trident and verify 1.5x increased speed
  - Test enhanced damage in aquatic environments
  - Attack aquatic mobs to test 30% pacification chance

- [ ] **Perfect Accuracy System**
  - Throw Ancient Trident at distant targets
  - Verify trident flies exactly where crosshair is aimed
  - Test that there's no gravity drop or inaccuracy
  - Compare with regular trident accuracy
  - Test at various distances and angles

- [ ] **Tooltip System**
  - Hover over trident to see ability descriptions
  - Use underwater ability and check for "Awakened Form" tooltip
  - Verify all formatting and colors display correctly

### **Configuration Testing**
- [ ] **Modify Constants** (in AncientTridentItem.java)
  ```java
  DOLPHINS_GRACE_DURATION = 400;    // Test different durations
  AQUATIC_CONTROL_RANGE = 16;       // Test different ranges
  COOLDOWN_TICKS = 100;             // Test cooldown timing
  ```

## 📋 **Core System Testing**

### **Entity System Testing**
- [ ] **Aethelon Entity Spawning**
  - Use spawn egg in deep ocean biomes
  - Verify entity spawns with correct size (8x4 blocks)
  - Test entity AI states (IDLE, MOVING, TRANSITIONING)
  - Check collision detection works properly

- [ ] **Island Generation**
  - Verify islands spawn on turtle backs
  - Test different island sizes (Small, Medium, Large)
  - Check structure placement works correctly
  - Verify biome-appropriate vegetation

### **Item System Testing**
- [ ] **Turtle-Themed Items**
  - Test all crafting recipes work correctly
  - Verify item tooltips display properly
  - Check creative tab organization
  - Test item durability and functionality

### **Loot System Testing**
- [ ] **Death Loot Generation**
  - Defeat Aethelon entities
  - Verify loot drops based on rarity system
  - Test experience orb generation
  - Check loot modifiers work correctly

## 🔧 **Integration Testing**

### **Mod Compatibility**
- [ ] **Test with popular ocean mods**
- [ ] **Verify no conflicts with other entity mods**
- [ ] **Check performance with large mod packs**

### **Performance Testing**
- [ ] **Entity count limits**
- [ ] **Island rendering performance**
- [ ] **Memory usage monitoring**

## 📝 **Documentation & Polish**

### **User Experience**
- [ ] **All tooltips accurate and helpful**
- [ ] **Sound effects appropriate and balanced**
- [ ] **Particle effects enhance gameplay**
- [ ] **Error messages clear and informative**

### **Configuration**
- [ ] **All config options work correctly**
- [ ] **Default values provide good gameplay**
- [ ] **Config changes apply without restart where possible**

---

## ✅ **Completion Checklist**

Once all items above are checked off:
- [ ] **All core features tested and working**
- [ ] **All new features (Gem Upgrades, Perfect Accuracy) tested**
- [ ] **No critical bugs or crashes**
- [ ] **Performance acceptable in test environments**
- [ ] **Documentation updated and accurate**

**The system will be ready for the next development phase!** 🎯

---

## 🛠️ **NBT Structure Creation Guide**

### **Step 1: Design Your Island**
Use WorldEdit or similar tools to create custom island structures:
```
//wand                    // Get selection tool
//pos1                    // Set first position
//pos2                    // Set second position
//copy                    // Copy the selection
//schem save island_name  // Save as schematic
```

### **Step 2: Convert to NBT**
```
//schem load island_name
//paste
//sel                     // Select the pasted structure
//copy
/nbt save island_name     // Save as NBT file
```

### **Step 3: Export the Structure**
Place the .nbt file in the appropriate directory:
```
src/main/resources/data/aethelon/structures/islands/[size]/[name].nbt
```

**Size Categories:**
- `small/` - 16x8x16 blocks maximum
- `medium/` - 24x12x24 blocks maximum  
- `large/` - 32x16x32 blocks maximum
- `special/` - Unique structures (boss islands, treasure islands)

### **Step 4: Test the Structure**
```java
// In a test environment
DatapackStructureManager.spawnStructure(world, "your_island_name", position);
```