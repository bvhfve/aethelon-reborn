package com.bvhfve.aethelon.config;

import com.bvhfve.aethelon.Aethelon;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

/**
 * Debug logging utility for configuration status and chunk generation
 * Provides detailed logging when debug options are enabled
 */
public class ConfigDebugLogger {
    
    private static boolean hasLoggedConfigStatus = false;
    
    /**
     * Log all configuration options and their current values
     * Called once during mod initialization or when debug logging is enabled
     */
    public static void logConfigurationStatus() {
        if (!AethelonConfig.INSTANCE.enable_debug_logging || hasLoggedConfigStatus) {
            return;
        }
        
        Aethelon.LOGGER.info("=== AETHELON CONFIGURATION DEBUG ===");
        
        // Performance settings
        Aethelon.LOGGER.info("Performance Settings:");
        Aethelon.LOGGER.info("  - Max World Population: {} (Status: APPLIED)", 
                           AethelonConfig.INSTANCE.max_world_population);
        Aethelon.LOGGER.info("  - Min Distance Between Turtles: {} blocks (Status: APPLIED)", 
                           AethelonConfig.INSTANCE.min_distance_between_turtles);
        
        // Spawning settings
        Aethelon.LOGGER.info("Spawning Settings:");
        Aethelon.LOGGER.info("  - Ocean Spawn Weight: {} (Status: {})", 
                           AethelonConfig.INSTANCE.oceanSpawnWeight,
                           AethelonConfig.INSTANCE.oceanSpawnWeight > 0 ? "ENABLED" : "DISABLED");
        Aethelon.LOGGER.info("  - Beach Spawn Weight: {} (Status: HARDCODED/ENABLED)", 
                           AethelonConfig.BEACH_SPAWN_WEIGHT);
        Aethelon.LOGGER.info("  - Snowy Beach Spawn Weight: {} (Status: HARDCODED/ENABLED)", 
                           AethelonConfig.SNOWY_BEACH_SPAWN_WEIGHT);
        Aethelon.LOGGER.info("  - Stony Shore Spawn Weight: {} (Status: HARDCODED/ENABLED)", 
                           AethelonConfig.STONY_SHORE_SPAWN_WEIGHT);
        Aethelon.LOGGER.info("  - Minimum Turtle Distance: {} blocks (Status: APPLIED)", 
                           AethelonConfig.INSTANCE.minimumTurtleDistance);
        Aethelon.LOGGER.info("  - Max World Population: {} (Status: APPLIED)", 
                           AethelonConfig.INSTANCE.max_world_population);
        Aethelon.LOGGER.info("  - Min Distance Between Turtles: {} blocks (Status: APPLIED)", 
                           AethelonConfig.INSTANCE.min_distance_between_turtles);
        Aethelon.LOGGER.info("  - Spawn Chance Per Chunk: {} (Status: APPLIED)", 
                           AethelonConfig.INSTANCE.spawn_chance_per_chunk);
        
        Aethelon.LOGGER.info("  - Spawn Height Range: {}-{} (Status: APPLIED)", 
                           AethelonConfig.INSTANCE.spawn_height_min, 
                           AethelonConfig.INSTANCE.spawn_height_max);
        
        // Spawn conditions
        Aethelon.LOGGER.info("Spawn Conditions:");
        Aethelon.LOGGER.info("  - Water Depth Required: {} blocks (Status: APPLIED)", 
                           AethelonConfig.INSTANCE.water_depth_required);
        Aethelon.LOGGER.info("  - Clearance Above Required: {} blocks (Status: APPLIED)", 
                           AethelonConfig.INSTANCE.clearance_above_required);
        Aethelon.LOGGER.info("  - Spawn Rarity: {} (Status: APPLIED)", 
                           AethelonConfig.INSTANCE.spawn_rarity);
        
        // Debug settings
        Aethelon.LOGGER.info("Debug Settings:");
        Aethelon.LOGGER.info("  - Debug Logging: {} (Status: ENABLED)", 
                           AethelonConfig.INSTANCE.enable_debug_logging);
        Aethelon.LOGGER.info("  - Debug Chunk Generation: {} (Status: {})", 
                           AethelonConfig.INSTANCE.debug_chunk_generation,
                           AethelonConfig.INSTANCE.debug_chunk_generation ? "ENABLED" : "DISABLED");
        Aethelon.LOGGER.info("  - Debug Spawn Attempts: {} (Status: {})", 
                           AethelonConfig.INSTANCE.debug_spawn_attempts,
                           AethelonConfig.INSTANCE.debug_spawn_attempts ? "ENABLED" : "DISABLED");
        
        Aethelon.LOGGER.info("=== END CONFIGURATION DEBUG ===");
        hasLoggedConfigStatus = true;
    }
    
    /**
     * Log configuration status during chunk generation
     * Called when chunks are being generated and debug_chunk_generation is enabled
     */
    public static void logChunkGenerationConfig(net.minecraft.world.WorldAccess world, ChunkPos chunkPos) {
        if (!AethelonConfig.INSTANCE.enable_debug_logging || !AethelonConfig.INSTANCE.debug_chunk_generation) {
            return;
        }
        
        Aethelon.LOGGER.debug("=== CHUNK GENERATION CONFIG STATUS ===");
        Aethelon.LOGGER.debug("Chunk: {} in world access", chunkPos);
        
        // Check spawn conditions for this chunk
        boolean oceanSpawningEnabled = AethelonConfig.INSTANCE.oceanSpawnWeight > 0;
        
        Aethelon.LOGGER.debug("Active Spawn Settings for this chunk:");
        Aethelon.LOGGER.debug("  - Ocean Spawning: {} (Weight: {})", 
                            oceanSpawningEnabled ? "ACTIVE" : "DISABLED", 
                            AethelonConfig.INSTANCE.oceanSpawnWeight);
        Aethelon.LOGGER.debug("  - Beach Spawning: ACTIVE (Hardcoded weights: Beach={}, Snowy={}, Stony={})", 
                            AethelonConfig.BEACH_SPAWN_WEIGHT,
                            AethelonConfig.SNOWY_BEACH_SPAWN_WEIGHT,
                            AethelonConfig.STONY_SHORE_SPAWN_WEIGHT);
        
        // Distance checking status
        Aethelon.LOGGER.debug("Distance Checking:");
        Aethelon.LOGGER.debug("  - Minimum Turtle Distance: {} blocks (Status: ACTIVE)", 
                            AethelonConfig.INSTANCE.minimumTurtleDistance);
        Aethelon.LOGGER.debug("  - Population Limit: {} turtles (Status: ACTIVE)", 
                            AethelonConfig.INSTANCE.max_world_population);
        
        // Performance settings affecting this chunk
        Aethelon.LOGGER.debug("Performance Settings:");
        Aethelon.LOGGER.debug("  - Max World Population: {} (Status: ENFORCED)", 
                            AethelonConfig.INSTANCE.max_world_population);
        
        Aethelon.LOGGER.debug("=== END CHUNK GENERATION CONFIG ===");
    }
    
    /**
     * Log spawn attempt details when debug_spawn_attempts is enabled
     */
    public static void logSpawnAttempt(World world, net.minecraft.util.math.BlockPos pos, 
                                     String biome, boolean success, String reason) {
        if (!AethelonConfig.INSTANCE.enable_debug_logging || !AethelonConfig.INSTANCE.debug_spawn_attempts) {
            return;
        }
        
        String status = success ? "SUCCESS" : "FAILED";
        Aethelon.LOGGER.debug("SPAWN ATTEMPT [{}]: Position {} in biome '{}' - Reason: {}", 
                            status, pos, biome, reason);
        
        if (success) {
            Aethelon.LOGGER.debug("  Applied spawn settings: Ocean weight={}, Min distance={}, Population limit={}", 
                                AethelonConfig.INSTANCE.oceanSpawnWeight,
                                AethelonConfig.INSTANCE.minimumTurtleDistance,
                                AethelonConfig.INSTANCE.max_world_population);
        }
    }
    
    /**
     * Log configuration validation results
     */
    public static void logConfigValidation() {
        if (!AethelonConfig.INSTANCE.enable_debug_logging) {
            return;
        }
        
        Aethelon.LOGGER.info("=== CONFIGURATION VALIDATION ===");
        
        // Validate spawn weights
        boolean hasValidSpawnWeights = AethelonConfig.INSTANCE.oceanSpawnWeight >= 0 &&
                                     AethelonConfig.BEACH_SPAWN_WEIGHT > 0 &&
                                     AethelonConfig.SNOWY_BEACH_SPAWN_WEIGHT > 0 &&
                                     AethelonConfig.STONY_SHORE_SPAWN_WEIGHT > 0;
        
        Aethelon.LOGGER.info("Spawn Weight Validation: {}", hasValidSpawnWeights ? "PASSED" : "FAILED");
        
        // Validate distance settings
        boolean hasValidDistances = AethelonConfig.INSTANCE.minimumTurtleDistance > 0 &&
                                  AethelonConfig.INSTANCE.min_distance_between_turtles > 0;
        
        Aethelon.LOGGER.info("Distance Settings Validation: {}", hasValidDistances ? "PASSED" : "FAILED");
        
        // Validate population settings
        boolean hasValidPopulation = AethelonConfig.INSTANCE.max_world_population > 0;
        
        Aethelon.LOGGER.info("Population Settings Validation: {}", hasValidPopulation ? "PASSED" : "FAILED");
        
        // Validate spawn conditions
        boolean hasValidSpawnConditions = AethelonConfig.INSTANCE.water_depth_required > 0 &&
                                        AethelonConfig.INSTANCE.clearance_above_required >= 0 &&
                                        AethelonConfig.INSTANCE.spawn_rarity > 0 &&
                                        AethelonConfig.INSTANCE.spawn_rarity <= 1.0f;
        
        Aethelon.LOGGER.info("Spawn Conditions Validation: {}", hasValidSpawnConditions ? "PASSED" : "FAILED");
        
        boolean overallValid = hasValidSpawnWeights && hasValidDistances && 
                             hasValidPopulation && hasValidSpawnConditions;
        
        Aethelon.LOGGER.info("Overall Configuration Status: {}", overallValid ? "VALID" : "INVALID");
        
        if (!overallValid) {
            Aethelon.LOGGER.warn("Some configuration values may cause issues. Please check the settings above.");
        }
        
        Aethelon.LOGGER.info("=== END CONFIGURATION VALIDATION ===");
    }
    
    /**
     * Reset the logged status (useful for config reloading)
     */
    public static void resetLoggedStatus() {
        hasLoggedConfigStatus = false;
    }
}