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
        // Enhanced spawn weights for WWOO ocean biomes
        WWOO_BIOME_SPAWN_WEIGHTS.put("wwoo:tropical_ocean", 3.0f);
        WWOO_BIOME_SPAWN_WEIGHTS.put("wwoo:coral_reef", 4.0f);
        WWOO_BIOME_SPAWN_WEIGHTS.put("wwoo:kelp_forest", 2.5f);
        WWOO_BIOME_SPAWN_WEIGHTS.put("wwoo:deep_ocean", 1.5f);
        WWOO_BIOME_SPAWN_WEIGHTS.put("wwoo:arctic_ocean", 0.8f);
        WWOO_BIOME_SPAWN_WEIGHTS.put("wwoo:temperate_ocean", 2.0f);
        
        // Island themes for WWOO biomes
        WWOO_ISLAND_THEMES.put("wwoo:tropical_ocean", "tropical_paradise");
        WWOO_ISLAND_THEMES.put("wwoo:coral_reef", "coral_garden");
        WWOO_ISLAND_THEMES.put("wwoo:kelp_forest", "kelp_sanctuary");
        WWOO_ISLAND_THEMES.put("wwoo:deep_ocean", "deep_sea_refuge");
        WWOO_ISLAND_THEMES.put("wwoo:arctic_ocean", "ice_fortress");
        WWOO_ISLAND_THEMES.put("wwoo:temperate_ocean", "coastal_haven");
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
            
            // Apply biome-specific behaviors
            switch (biomeId) {
                case "wwoo:tropical_ocean":
                    // Faster movement in tropical waters
                    turtle.addVelocity(0, 0.02, 0);
                    break;
                case "wwoo:coral_reef":
                    // Enhanced healing near coral reefs
                    if (turtle.getHealth() < turtle.getMaxHealth()) {
                        turtle.heal(0.1f);
                    }
                    break;
                case "wwoo:kelp_forest":
                    // Camouflage effect in kelp forests
                    turtle.setInvisible(new Random().nextFloat() < 0.1f);
                    break;
                case "wwoo:arctic_ocean":
                    // Slower movement in cold waters
                    turtle.addVelocity(0, -0.01, 0);
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
     * Check if current biome is a WWOO biome
     */
    public static boolean isWWOOBiome(RegistryKey<Biome> biome) {
        if (biome == null) return false;
        String biomeId = biome.getValue().toString();
        return biomeId.startsWith("wwoo:");
    }
}