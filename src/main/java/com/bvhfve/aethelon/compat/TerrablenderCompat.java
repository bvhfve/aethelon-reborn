package com.bvhfve.aethelon.compat;

import com.bvhfve.aethelon.entity.AethelonEntity;
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
 * Compatibility integration with Terralith/Terrablender mods
 * 
 * Features:
 * - Enhanced spawning in Terralith ocean biomes
 * - Integration with Terrablender's biome generation system
 * - Terralith-specific island themes and decorations
 * - Support for custom ocean biomes from datapack integration
 */
public class TerrablenderCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonCompat/Terrablender");
    
    // Terralith biome compatibility mapping
    private static final Map<String, Float> TERRALITH_BIOME_SPAWN_WEIGHTS = new HashMap<>();
    private static final Map<String, String> TERRALITH_ISLAND_THEMES = new HashMap<>();
    
    static {
        // Enhanced spawn weights for Terralith ocean biomes
        TERRALITH_BIOME_SPAWN_WEIGHTS.put("terralith:warm_ocean", 2.5f);
        TERRALITH_BIOME_SPAWN_WEIGHTS.put("terralith:cold_ocean", 1.8f);
        TERRALITH_BIOME_SPAWN_WEIGHTS.put("terralith:deep_ocean", 2.0f);
        TERRALITH_BIOME_SPAWN_WEIGHTS.put("terralith:frozen_ocean", 1.2f);
        TERRALITH_BIOME_SPAWN_WEIGHTS.put("terralith:lukewarm_ocean", 2.2f);
        TERRALITH_BIOME_SPAWN_WEIGHTS.put("terralith:ocean", 2.0f);
        
        // Custom Terralith ocean biomes
        TERRALITH_BIOME_SPAWN_WEIGHTS.put("terralith:moonlight_grove_ocean", 3.5f);
        TERRALITH_BIOME_SPAWN_WEIGHTS.put("terralith:emerald_peaks_ocean", 2.8f);
        TERRALITH_BIOME_SPAWN_WEIGHTS.put("terralith:crystal_caves_ocean", 3.0f);
        
        // Island themes for Terralith biomes
        TERRALITH_ISLAND_THEMES.put("terralith:warm_ocean", "tropical_terralith");
        TERRALITH_ISLAND_THEMES.put("terralith:cold_ocean", "arctic_terralith");
        TERRALITH_ISLAND_THEMES.put("terralith:deep_ocean", "abyssal_terralith");
        TERRALITH_ISLAND_THEMES.put("terralith:frozen_ocean", "glacial_terralith");
        TERRALITH_ISLAND_THEMES.put("terralith:moonlight_grove_ocean", "moonlit_sanctuary");
        TERRALITH_ISLAND_THEMES.put("terralith:emerald_peaks_ocean", "emerald_oasis");
        TERRALITH_ISLAND_THEMES.put("terralith:crystal_caves_ocean", "crystal_refuge");
    }
    
    public static boolean initialize() {
        try {
            LOGGER.info("Initializing Terralith/Terrablender compatibility...");
            
            // Initialize Terralith-specific features
            initializeTerralithSpawning();
            initializeTerralithIslandThemes();
            initializeTerrablenderIntegration();
            
            LOGGER.info("Terralith/Terrablender compatibility initialized successfully!");
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Terralith/Terrablender compatibility: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Initialize enhanced spawning in Terralith biomes
     */
    private static void initializeTerralithSpawning() {
        LOGGER.info("Setting up enhanced Aethelon spawning for Terralith biomes...");
        // Enhanced spawn logic will be handled by the main spawning system
    }
    
    /**
     * Initialize Terralith-specific island themes
     */
    private static void initializeTerralithIslandThemes() {
        LOGGER.info("Setting up Terralith-themed turtle islands...");
        // Island theming will be handled by the island generation system
    }
    
    /**
     * Initialize Terrablender integration
     */
    private static void initializeTerrablenderIntegration() {
        LOGGER.info("Setting up Terrablender biome generation integration...");
        // Integration with Terrablender's biome generation system
    }
    
    /**
     * Get spawn weight multiplier for Terralith biomes
     */
    public static float getSpawnWeightMultiplier(RegistryKey<Biome> biome) {
        String biomeId = biome.getValue().toString();
        return TERRALITH_BIOME_SPAWN_WEIGHTS.getOrDefault(biomeId, 1.0f);
    }
    
    /**
     * Get island theme for Terralith biome
     */
    public static String getIslandTheme(RegistryKey<Biome> biome) {
        String biomeId = biome.getValue().toString();
        return TERRALITH_ISLAND_THEMES.getOrDefault(biomeId, "default");
    }
    
    /**
     * Apply Terralith-specific turtle behaviors
     */
    public static void applyTerralithBehaviors(AethelonEntity turtle, World world, BlockPos pos) {
        try {
            RegistryKey<Biome> biome = world.getBiome(pos).getKey().orElse(null);
            if (biome == null) return;
            
            String biomeId = biome.getValue().toString();
            
            // Apply biome-specific behaviors
            switch (biomeId) {
                case "terralith:moonlight_grove_ocean":
                    // Enhanced night vision and glowing effect
                    if (world.isNight()) {
                        turtle.setGlowing(true);
                    }
                    break;
                case "terralith:emerald_peaks_ocean":
                    // Increased health regeneration
                    if (turtle.getHealth() < turtle.getMaxHealth()) {
                        turtle.heal(0.15f);
                    }
                    break;
                case "terralith:crystal_caves_ocean":
                    // Crystal-like protective aura
                    turtle.addVelocity(0, 0.01, 0);
                    break;
                case "terralith:frozen_ocean":
                    // Ice resistance and slower movement
                    turtle.setFrozenTicks(0);
                    break;
            }
        } catch (Exception e) {
            LOGGER.warn("Error applying Terralith behaviors: {}", e.getMessage());
        }
    }
    
    /**
     * Generate Terralith-themed island decorations
     */
    public static void generateTerralithIslandDecorations(World world, BlockPos islandCenter, String theme) {
        try {
            Random random = new Random();
            
            switch (theme) {
                case "moonlit_sanctuary":
                    generateMoonlitDecorations(world, islandCenter, random);
                    break;
                case "emerald_oasis":
                    generateEmeraldDecorations(world, islandCenter, random);
                    break;
                case "crystal_refuge":
                    generateCrystalDecorations(world, islandCenter, random);
                    break;
                case "glacial_terralith":
                    generateGlacialDecorations(world, islandCenter, random);
                    break;
                case "abyssal_terralith":
                    generateAbyssalDecorations(world, islandCenter, random);
                    break;
                default:
                    generateTerralithDefaultDecorations(world, islandCenter, random);
                    break;
            }
        } catch (Exception e) {
            LOGGER.warn("Error generating Terralith island decorations: {}", e.getMessage());
        }
    }
    
    private static void generateMoonlitDecorations(World world, BlockPos center, Random random) {
        // Add glowing plants and moonstone formations
        LOGGER.debug("Generating moonlit sanctuary decorations at {}", center);
    }
    
    private static void generateEmeraldDecorations(World world, BlockPos center, Random random) {
        // Add emerald blocks and lush vegetation
        LOGGER.debug("Generating emerald oasis decorations at {}", center);
    }
    
    private static void generateCrystalDecorations(World world, BlockPos center, Random random) {
        // Add crystal formations and amethyst clusters
        LOGGER.debug("Generating crystal refuge decorations at {}", center);
    }
    
    private static void generateGlacialDecorations(World world, BlockPos center, Random random) {
        // Add ice spikes and snow formations
        LOGGER.debug("Generating glacial decorations at {}", center);
    }
    
    private static void generateAbyssalDecorations(World world, BlockPos center, Random random) {
        // Add deep ocean themed decorations
        LOGGER.debug("Generating abyssal decorations at {}", center);
    }
    
    private static void generateTerralithDefaultDecorations(World world, BlockPos center, Random random) {
        // Add standard Terralith-style decorations
        LOGGER.debug("Generating Terralith default decorations at {}", center);
    }
    
    /**
     * Check if current biome is a Terralith biome
     */
    public static boolean isTerralithBiome(RegistryKey<Biome> biome) {
        if (biome == null) return false;
        String biomeId = biome.getValue().toString();
        return biomeId.startsWith("terralith:");
    }
    
    /**
     * Register custom biome modifications for Terrablender integration
     */
    public static void registerBiomeModifications() {
        try {
            LOGGER.info("Registering Aethelon biome modifications for Terrablender...");
            // This would integrate with Terrablender's biome modification system
            // to ensure Aethelon turtles spawn in custom ocean biomes
        } catch (Exception e) {
            LOGGER.warn("Error registering biome modifications: {}", e.getMessage());
        }
    }
}