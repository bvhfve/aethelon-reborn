# Aethelon Debug Logging Implementation

This document describes the enhanced debug logging system implemented for the Aethelon mod.

## 🔧 **Debug Configuration Options**

### **New Config Options Added**
```java
// In AethelonConfig.java
public boolean enable_debug_logging = false;        // Master debug toggle (existing)
public boolean debug_chunk_generation = false;      // Log config status during chunk generation
public boolean debug_spawn_attempts = false;        // Log detailed spawn attempt information
```

### **Configuration Usage**
- **`enable_debug_logging`**: Master switch for all debug logging
- **`debug_chunk_generation`**: Logs configuration status when chunks are generated
- **`debug_spawn_attempts`**: Logs detailed information about turtle spawn attempts

## 📊 **Debug Logging Features**

### **1. Configuration Status Logging**
When `enable_debug_logging = true`, the system logs:

#### **At Mod Initialization**:
```
=== AETHELON CONFIGURATION DEBUG ===
Performance Settings:
  - Performance Optimizations: true (Status: APPLIED)
  - Max Active Aethelons: 10 (Status: APPLIED)
  - Render Distance: 128.0 blocks (Status: APPLIED)

Spawning Settings:
  - Ocean Spawn Weight: 5 (Status: ENABLED)
  - Beach Spawn Weight: 15 (Status: HARDCODED/ENABLED)
  - Snowy Beach Spawn Weight: 10 (Status: HARDCODED/ENABLED)
  - Stony Shore Spawn Weight: 12 (Status: HARDCODED/ENABLED)
  - Minimum Turtle Distance: 128 blocks (Status: APPLIED)
  - Max World Population: 3 (Status: APPLIED)

[... additional config sections ...]
=== END CONFIGURATION DEBUG ===
```

#### **Configuration Validation**:
```
=== CONFIGURATION VALIDATION ===
Spawn Weight Validation: PASSED
Distance Settings Validation: PASSED
Population Settings Validation: PASSED
Spawn Conditions Validation: PASSED
Overall Configuration Status: VALID
=== END CONFIGURATION VALIDATION ===
```

### **2. Chunk Generation Logging**
When `debug_chunk_generation = true`, logs during chunk generation:

```
=== CHUNK GENERATION CONFIG STATUS ===
Chunk: [256, 128] in dimension: minecraft:overworld
Active Spawn Settings for this chunk:
  - Ocean Spawning: ACTIVE (Weight: 5)
  - Beach Spawning: ACTIVE (Hardcoded weights: Beach=15, Snowy=10, Stony=12)
  - Island Generation: ACTIVE (Chance: 0.3)
  - Deep Sea Spawning: ACTIVE

Distance Checking:
  - Minimum Turtle Distance: 128 blocks (Status: ACTIVE)
  - Population Limit: 3 turtles (Status: ACTIVE)

Performance Settings:
  - Performance Optimizations: true (Status: APPLIED)
  - Max Active Aethelons: 10 (Status: ENFORCED)
=== END CHUNK GENERATION CONFIG ===
```

### **3. Spawn Attempt Logging**
When `debug_spawn_attempts = true`, logs each spawn attempt:

```
SPAWN ATTEMPT [SUCCESS]: Position BlockPos{x=256, y=62, z=128} in biome 'minecraft:beach' - Reason: No nearby turtles found
  Applied spawn settings: Ocean weight=5, Min distance=128, Population limit=3

SPAWN ATTEMPT [FAILED]: Position BlockPos{x=300, y=62, z=150} in biome 'minecraft:ocean' - Reason: Too close to existing turtle (distance: 45.2, minimum: 128)

SPAWN ATTEMPT [FAILED]: Position BlockPos{x=400, y=62, z=200} in biome 'minecraft:ocean' - Reason: World population limit reached
```

## 🏗️ **Implementation Details**

### **Core Components**

#### **1. ConfigDebugLogger.java**
- **`logConfigurationStatus()`**: Logs all config options and their status
- **`logChunkGenerationConfig()`**: Logs config status during chunk generation
- **`logSpawnAttempt()`**: Logs individual spawn attempts with details
- **`logConfigValidation()`**: Validates and logs config correctness

#### **2. ChunkGeneratorMixin.java**
- Injects into chunk generation process
- Calls debug logging when chunks are being generated
- Non-intrusive - errors don't break chunk generation

#### **3. Enhanced AethelonSpawnChecker.java**
- Integrated with debug logging system
- Logs spawn success/failure reasons
- Provides detailed spawn attempt information

### **Integration Points**

#### **Configuration Initialization**
```java
// In AethelonConfig.initialize()
ConfigDebugLogger.logConfigurationStatus();
ConfigDebugLogger.logConfigValidation();
```

#### **Spawn Checking**
```java
// In AethelonSpawnChecker.canSpawn()
ConfigDebugLogger.logSpawnAttempt(world, pos, biome, success, reason);
```

#### **Chunk Generation**
```java
// In ChunkGeneratorMixin.onGenerateFeatures()
ConfigDebugLogger.logChunkGenerationConfig(world, chunkPos);
```

## 🎯 **Configuration Status Tracking**

### **Status Types**
- **APPLIED**: Setting is active and working
- **ENABLED/DISABLED**: Boolean settings status
- **HARDCODED**: Values that cannot be changed
- **ENFORCED**: Limits that are actively checked
- **ACTIVE**: Features currently functioning
- **PASSED/FAILED**: Validation results

### **Validation Checks**
1. **Spawn Weight Validation**: Ensures all spawn weights are valid
2. **Distance Settings Validation**: Checks distance values are positive
3. **Population Settings Validation**: Verifies population limits are reasonable
4. **Spawn Conditions Validation**: Validates spawn requirement values

## 🚀 **Usage Instructions**

### **Enable Debug Logging**
1. Set `enable_debug_logging = true` in config
2. Optionally enable specific debug categories:
   - `debug_chunk_generation = true` for chunk generation logs
   - `debug_spawn_attempts = true` for spawn attempt details

### **Reading Debug Output**
- **INFO level**: Configuration status and validation
- **DEBUG level**: Detailed chunk generation and spawn attempts
- **WARN level**: Configuration issues or validation failures

### **Performance Considerations**
- Debug logging has minimal performance impact when disabled
- Chunk generation logging only activates when chunks are generated
- Spawn attempt logging only occurs during actual spawn attempts
- All debug operations are wrapped in try-catch to prevent crashes

## 📋 **Debug Output Examples**

### **Successful Configuration Load**
```
[INFO] Initializing Aethelon configuration...
[INFO] Configuration loaded from file: config/aethelon.json
[INFO] === AETHELON CONFIGURATION DEBUG ===
[INFO] Performance Settings:
[INFO]   - Performance Optimizations: true (Status: APPLIED)
[... full config dump ...]
[INFO] === CONFIGURATION VALIDATION ===
[INFO] Overall Configuration Status: VALID
[INFO] Configuration initialized successfully!
```

### **Spawn Attempt Debugging**
```
[DEBUG] === CHUNK GENERATION CONFIG STATUS ===
[DEBUG] Chunk: [64, 32] in dimension: minecraft:overworld
[DEBUG] Active Spawn Settings for this chunk:
[DEBUG]   - Ocean Spawning: ACTIVE (Weight: 5)
[DEBUG] === END CHUNK GENERATION CONFIG ===

[DEBUG] SPAWN ATTEMPT [SUCCESS]: Position BlockPos{x=1024, y=62, z=512} in biome 'minecraft:beach' - Reason: No nearby turtles found
[DEBUG]   Applied spawn settings: Ocean weight=5, Min distance=128, Population limit=3
```

### **Configuration Issues**
```
[WARN] Some configuration values may cause issues. Please check the settings above.
[INFO] Spawn Weight Validation: FAILED
[INFO] Overall Configuration Status: INVALID
```

## 🔧 **Troubleshooting**

### **No Debug Output**
- Check `enable_debug_logging = true` in config
- Verify log level includes INFO and DEBUG messages
- Ensure config file is being loaded correctly

### **Too Much Debug Output**
- Disable specific debug categories:
  - Set `debug_chunk_generation = false`
  - Set `debug_spawn_attempts = false`
- Keep only `enable_debug_logging = true` for basic config info

### **Performance Issues**
- Debug logging is designed to be lightweight
- If experiencing issues, disable chunk generation debugging first
- Spawn attempt debugging only occurs during actual spawning

## ✅ **Benefits**

1. **Configuration Transparency**: See exactly what settings are applied
2. **Spawn Debugging**: Understand why turtles spawn or don't spawn
3. **Chunk Generation Insight**: Monitor config application during world generation
4. **Validation Feedback**: Know if configuration values are valid
5. **Troubleshooting Support**: Detailed logs for issue diagnosis
6. **Development Aid**: Helpful for mod development and testing

The debug logging system provides comprehensive insight into the Aethelon mod's configuration and behavior, making it easier to troubleshoot issues and understand how the mod is functioning.