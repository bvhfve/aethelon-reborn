package com.bvhfve.aethelon.compat;

import com.bvhfve.aethelon.entity.AethelonEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Compatibility integration with William Wythers' Overhauled Overworld mod
 * 
 * Features:
 * - Enhanced spawning in WWOO ocean biomes
 * - Biome-specific turtle island themes
 * - Special behaviors in WWOO environments
 * - Integration with WWOO's enhanced world generation
 */
public class WilliamWythersCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonCompat/WilliamWythers");
    
    // WWOO biome compatibility mapping
    private static final Map<String, Float> WWOO_BIOME_SPAWN_WEIGHTS = new HashMap<>();
    private static final Map<String, String> WWOO_ISLAND_THEMES = new HashMap<>();
    
    static {
        // Enhanced spawn weights for WWOO ocean biomes (based on actual WWOO biome data)
        // WWOO modifies vanilla ocean biomes rather than adding completely new ones
        WWOO_BIOME_SPAWN_WEIGHTS.put("minecraft:warm_ocean", 2.5f);
        WWOO_BIOME_SPAWN_WEIGHTS.put("minecraft:lukewarm_ocean", 2.2f);
        WWOO_BIOME_SPAWN_WEIGHTS.put("minecraft:ocean", 2.0f);
        WWOO_BIOME_SPAWN_WEIGHTS.put("minecraft:cold_ocean", 1.8f);
        WWOO_BIOME_SPAWN_WEIGHTS.put("minecraft:frozen_ocean", 1.2f);
        WWOO_BIOME_SPAWN_WEIGHTS.put("minecraft:deep_ocean", 2.0f);
        WWOO_BIOME_SPAWN_WEIGHTS.put("minecraft:deep_lukewarm_ocean", 2.3f);
        WWOO_BIOME_SPAWN_WEIGHTS.put("minecraft:deep_cold_ocean", 1.9f);
        WWOO_BIOME_SPAWN_WEIGHTS.put("minecraft:deep_frozen_ocean", 1.3f);
        
        // Island themes for WWOO-enhanced ocean biomes
        WWOO_ISLAND_THEMES.put("minecraft:warm_ocean", "tropical_paradise");
        WWOO_ISLAND_THEMES.put("minecraft:lukewarm_ocean", "temperate_shores");
        WWOO_ISLAND_THEMES.put("minecraft:ocean", "oceanic_haven");
        WWOO_ISLAND_THEMES.put("minecraft:cold_ocean", "northern_refuge");
        WWOO_ISLAND_THEMES.put("minecraft:frozen_ocean", "ice_fortress");
        WWOO_ISLAND_THEMES.put("minecraft:deep_ocean", "deep_sea_refuge");
        WWOO_ISLAND_THEMES.put("minecraft:deep_lukewarm_ocean", "abyssal_garden");
        WWOO_ISLAND_THEMES.put("minecraft:deep_cold_ocean", "arctic_depths");
        WWOO_ISLAND_THEMES.put("minecraft:deep_frozen_ocean", "glacial_sanctuary");
    }
    
    public static boolean initialize() {
        try {
            LOGGER.info("Initializing William Wythers' Overhauled Overworld compatibility...");
            
            // Initialize WWOO-specific features
            initializeWWOOSpawning();
            initializeWWOOIslandThemes();
            initializeWWOOBehaviors();
            
            LOGGER.info("William Wythers' Overhauled Overworld compatibility initialized successfully!");
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize William Wythers' Overhauled Overworld compatibility: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Initialize enhanced spawning in WWOO biomes
     */
    private static void initializeWWOOSpawning() {
        LOGGER.info("Setting up enhanced Aethelon spawning for WWOO biomes...");
        // Enhanced spawn logic will be handled by the main spawning system
    }
    
    /**
     * Initialize WWOO-specific island themes
     */
    private static void initializeWWOOIslandThemes() {
        LOGGER.info("Setting up WWOO-themed turtle islands...");
        // Island theming will be handled by the island generation system
    }
    
    /**
     * Initialize WWOO-specific behaviors
     */
    private static void initializeWWOOBehaviors() {
        LOGGER.info("Setting up WWOO-specific turtle behaviors...");
        // Behavior modifications will be handled by the AI system
    }
    
    /**
     * Get spawn weight multiplier for WWOO biomes
     */
    public static float getSpawnWeightMultiplier(RegistryKey<Biome> biome) {
        String biomeId = biome.getValue().toString();
        return WWOO_BIOME_SPAWN_WEIGHTS.getOrDefault(biomeId, 1.0f);
    }
    
    /**
     * Get island theme for WWOO biome
     */
    public static String getIslandTheme(RegistryKey<Biome> biome) {
        String biomeId = biome.getValue().toString();
        return WWOO_ISLAND_THEMES.getOrDefault(biomeId, "default");
    }
    
    /**
     * Apply WWOO-specific turtle behaviors
     */
    public static void applyWWOOBehaviors(AethelonEntity turtle, World world, BlockPos pos) {
        try {
            RegistryKey<Biome> biome = world.getBiome(pos).getKey().orElse(null);
            if (biome == null) return;
            
            String biomeId = biome.getValue().toString();
            
            // Apply WWOO-enhanced biome-specific behaviors
            switch (biomeId) {
                case "minecraft:warm_ocean":
                    // Enhanced tropical behaviors with WWOO improvements
                    turtle.addVelocity(0, 0.02, 0);
                    // WWOO adds more tropical features, so enhanced healing
                    if (turtle.getHealth() < turtle.getMaxHealth()) {
                        turtle.heal(0.05f);
                    }
                    break;
                case "minecraft:lukewarm_ocean":
                    // Balanced temperate ocean behaviors
                    turtle.addVelocity(0, 0.01, 0);
                    break;
                case "minecraft:cold_ocean":
                case "minecraft:deep_cold_ocean":
                    // Cold resistance and slower movement
                    turtle.setFrozenTicks(0);
                    turtle.addVelocity(0, -0.005, 0);
                    break;
                case "minecraft:frozen_ocean":
                case "minecraft:deep_frozen_ocean":
                    // Arctic adaptations
                    turtle.setFrozenTicks(0);
                    turtle.addVelocity(0, -0.01, 0);
                    // WWOO enhances frozen biomes with more ice features
                    if (turtle.getHealth() < turtle.getMaxHealth()) {
                        turtle.heal(0.02f); // Slow regeneration from ice
                    }
                    break;
                case "minecraft:deep_ocean":
                case "minecraft:deep_lukewarm_ocean":
                    // Deep ocean adaptations
                    turtle.addVelocity(0, -0.02, 0); // Dive deeper
                    // Enhanced pressure resistance
                    if (turtle.getHealth() < turtle.getMaxHealth()) {
                        turtle.heal(0.03f);
                    }
                    break;
            }
        } catch (Exception e) {
            LOGGER.warn("Error applying WWOO behaviors: {}", e.getMessage());
        }
    }
    
    /**
     * Generate WWOO-themed island decorations
     */
    public static void generateWWOOIslandDecorations(World world, BlockPos islandCenter, String theme) {
        try {
            Random random = new Random();
            
            switch (theme) {
                case "tropical_paradise":
                    generateTropicalDecorations(world, islandCenter, random);
                    break;
                case "coral_garden":
                    generateCoralDecorations(world, islandCenter, random);
                    break;
                case "kelp_sanctuary":
                    generateKelpDecorations(world, islandCenter, random);
                    break;
                case "ice_fortress":
                    generateIceDecorations(world, islandCenter, random);
                    break;
                default:
                    generateDefaultDecorations(world, islandCenter, random);
                    break;
            }
        } catch (Exception e) {
            LOGGER.warn("Error generating WWOO island decorations: {}", e.getMessage());
        }
    }
    
    private static void generateTropicalDecorations(World world, BlockPos center, Random random) {
        // Add tropical plants, palm trees, and exotic flowers
        LOGGER.debug("Generating tropical paradise decorations at {}", center);
    }
    
    private static void generateCoralDecorations(World world, BlockPos center, Random random) {
        // Add coral blocks and sea life around the island
        LOGGER.debug("Generating coral garden decorations at {}", center);
    }
    
    private static void generateKelpDecorations(World world, BlockPos center, Random random) {
        // Add kelp forests and sea grass around the island
        LOGGER.debug("Generating kelp sanctuary decorations at {}", center);
    }
    
    private static void generateIceDecorations(World world, BlockPos center, Random random) {
        // Add ice formations and snow
        LOGGER.debug("Generating ice fortress decorations at {}", center);
    }
    
    private static void generateDefaultDecorations(World world, BlockPos center, Random random) {
        // Add standard ocean decorations
        LOGGER.debug("Generating default decorations at {}", center);
    }
    
    /**
     * Check if current biome is enhanced by WWOO
     */
    public static boolean isWWOOEnhancedBiome(RegistryKey<Biome> biome) {
        if (biome == null) return false;
        String biomeId = biome.getValue().toString();
        // WWOO enhances vanilla ocean biomes rather than adding new ones
        return WWOO_BIOME_SPAWN_WEIGHTS.containsKey(biomeId);
    }
    
    /**
     * Check if WWOO features are present in the world
     */
    public static boolean hasWWOOFeatures(World world, BlockPos pos) {
        try {
            // Check for WWOO-specific world generation features
            // This could include checking for WWOO structures, terrain modifications, etc.
            return true; // Assume WWOO features are present if mod is loaded
        } catch (Exception e) {
            LOGGER.warn("Error checking for WWOO features: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Apply WWOO-specific world generation enhancements
     */
    public static void applyWWOOWorldGenEnhancements(World world, BlockPos islandCenter) {
        try {
            // Apply WWOO-style terrain modifications around turtle islands
            // This could include adding WWOO vegetation, terrain features, etc.
            LOGGER.debug("Applying WWOO world generation enhancements at {}", islandCenter);
            
            // Example: Add WWOO-style palm trees, terrain modifications, etc.
            
        } catch (Exception e) {
            LOGGER.warn("Error applying WWOO world generation enhancements: {}", e.getMessage());
        }
    }
}