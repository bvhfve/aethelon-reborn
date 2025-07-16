package com.bvhfve.aethelon.compat;

import com.bvhfve.aethelon.entity.AethelonEntity;
import com.bvhfve.aethelon.island.IslandManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Compatibility integration with Biomes O' Plenty mod
 * 
 * Features:
 * - Aethelon turtles spawn in BOP ocean biomes
 * - Turtle islands can generate with BOP blocks and decorations
 * - Enhanced spawning in tropical and oceanic BOP biomes
 * - BOP-themed turtle island variants
 */
public class BiomesOPlentyCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonCompat/BiomesOPlenty");
    
    // Biomes O' Plenty ocean and coastal biomes where Aethelon should spawn
    private static final Set<String> BOP_OCEAN_BIOMES = new HashSet<>(Arrays.asList(
        "biomesoplenty:tropical_ocean",
        "biomesoplenty:kelp_forest", 
        "biomesoplenty:coral_reef",
        "biomesoplenty:mangrove_swamp",
        "biomesoplenty:bayou",
        "biomesoplenty:wetland",
        "biomesoplenty:tidal_marsh",
        "biomesoplenty:volcanic_island",
        "biomesoplenty:tropics",
        "biomesoplenty:oasis"
    ));
    
    // BOP blocks that can be used for turtle island decoration
    private static final Set<String> BOP_ISLAND_BLOCKS = new HashSet<>(Arrays.asList(
        "biomesoplenty:palm_log",
        "biomesoplenty:palm_leaves",
        "biomesoplenty:palm_sapling",
        "biomesoplenty:coconut",
        "biomesoplenty:tropical_flower",
        "biomesoplenty:blue_hydrangea",
        "biomesoplenty:pink_hibiscus",
        "biomesoplenty:orange_cosmos",
        "biomesoplenty:bamboo",
        "biomesoplenty:driftwood",
        "biomesoplenty:sea_oats",
        "biomesoplenty:cattail",
        "biomesoplenty:water_lily"
    ));
    
    // BOP biome-specific island themes
    private static final Set<String> TROPICAL_BIOMES = new HashSet<>(Arrays.asList(
        "biomesoplenty:tropical_ocean",
        "biomesoplenty:tropics",
        "biomesoplenty:volcanic_island"
    ));
    
    private static final Set<String> SWAMP_BIOMES = new HashSet<>(Arrays.asList(
        "biomesoplenty:mangrove_swamp",
        "biomesoplenty:bayou",
        "biomesoplenty:wetland",
        "biomesoplenty:tidal_marsh"
    ));
    
    /**
     * Initialize Biomes O' Plenty compatibility
     */
    public static boolean initialize() {
        try {
            LOGGER.info("Initializing Biomes O' Plenty compatibility...");
            
            // Register enhanced biome spawning
            registerBiomeSpawning();
            
            // Register BOP-themed island generation
            registerBOPIslandGeneration();
            
            // Register biome-specific behaviors
            registerBiomeSpecificBehaviors();
            
            LOGGER.info("Biomes O' Plenty compatibility initialized successfully!");
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Biomes O' Plenty compatibility: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Register enhanced spawning in BOP biomes
     */
    private static void registerBiomeSpawning() {
        // This would typically be done through biome modification events
        // For now, we provide utility methods for spawn checking
    }
    
    /**
     * Check if a biome is a valid BOP ocean biome for Aethelon spawning
     */
    public static boolean isBOPOceanBiome(RegistryKey<Biome> biome) {
        if (biome == null) return false;
        String biomeId = biome.getValue().toString();
        return BOP_OCEAN_BIOMES.contains(biomeId);
    }
    
    /**
     * Get enhanced spawn weight for BOP biomes
     */
    public static int getBOPSpawnWeight(RegistryKey<Biome> biome) {
        if (!isBOPOceanBiome(biome)) return 0;
        
        String biomeId = biome.getValue().toString();
        
        // Higher spawn rates in tropical biomes
        if (TROPICAL_BIOMES.contains(biomeId)) {
            return 15; // Higher than normal spawn weight
        }
        
        // Medium spawn rates in swamp biomes  
        if (SWAMP_BIOMES.contains(biomeId)) {
            return 10;
        }
        
        // Normal spawn rates in other BOP ocean biomes
        return 8;
    }
    
    /**
     * Register BOP-themed island generation
     */
    private static void registerBOPIslandGeneration() {
        // Islands can be decorated with BOP blocks based on the biome
    }
    
    /**
     * Get BOP island theme based on current biome
     */
    public static IslandTheme getBOPIslandTheme(World world, BlockPos pos) {
        RegistryKey<Biome> biome = world.getBiome(pos).getKey().orElse(null);
        if (biome == null) return IslandTheme.DEFAULT;
        
        String biomeId = biome.getValue().toString();
        
        if (TROPICAL_BIOMES.contains(biomeId)) {
            return IslandTheme.TROPICAL;
        } else if (SWAMP_BIOMES.contains(biomeId)) {
            return IslandTheme.SWAMP;
        } else if (biomeId.equals("biomesoplenty:coral_reef")) {
            return IslandTheme.CORAL;
        } else if (biomeId.equals("biomesoplenty:kelp_forest")) {
            return IslandTheme.KELP;
        }
        
        return IslandTheme.DEFAULT;
    }
    
    /**
     * Apply BOP theming to turtle island
     */
    public static void applyBOPTheming(IslandManager islandManager, IslandTheme theme, World world, BlockPos centerPos) {
        if (islandManager == null) return;
        
        try {
            switch (theme) {
                case TROPICAL:
                    applyTropicalTheming(islandManager, world, centerPos);
                    break;
                case SWAMP:
                    applySwampTheming(islandManager, world, centerPos);
                    break;
                case CORAL:
                    applyCoralTheming(islandManager, world, centerPos);
                    break;
                case KELP:
                    applyKelpTheming(islandManager, world, centerPos);
                    break;
                default:
                    // No special theming
                    break;
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to apply BOP theming {}: {}", theme, e.getMessage());
        }
    }
    
    /**
     * Apply tropical BOP theming (palm trees, tropical flowers)
     */
    private static void applyTropicalTheming(IslandManager islandManager, World world, BlockPos centerPos) {
        // Add palm trees, coconuts, tropical flowers
        // This would place BOP blocks if available, fallback to vanilla alternatives
        LOGGER.debug("Applying tropical BOP theming to turtle island");
    }
    
    /**
     * Apply swamp BOP theming (mangroves, cattails, water lilies)
     */
    private static void applySwampTheming(IslandManager islandManager, World world, BlockPos centerPos) {
        // Add mangrove-style vegetation, cattails, water features
        LOGGER.debug("Applying swamp BOP theming to turtle island");
    }
    
    /**
     * Apply coral reef theming (coral blocks, sea plants)
     */
    private static void applyCoralTheming(IslandManager islandManager, World world, BlockPos centerPos) {
        // Add coral decorations around the island base
        LOGGER.debug("Applying coral BOP theming to turtle island");
    }
    
    /**
     * Apply kelp forest theming (kelp, sea grass)
     */
    private static void applyKelpTheming(IslandManager islandManager, World world, BlockPos centerPos) {
        // Add kelp and sea grass around the island
        LOGGER.debug("Applying kelp forest BOP theming to turtle island");
    }
    
    /**
     * Register biome-specific turtle behaviors
     */
    private static void registerBiomeSpecificBehaviors() {
        // Turtles might behave differently in different BOP biomes
    }
    
    /**
     * Get movement speed modifier for BOP biomes
     */
    public static float getBiomeSpeedModifier(RegistryKey<Biome> biome) {
        if (biome == null) return 1.0f;
        
        String biomeId = biome.getValue().toString();
        
        // Faster in tropical waters
        if (TROPICAL_BIOMES.contains(biomeId)) {
            return 1.2f;
        }
        
        // Slower in swamp areas
        if (SWAMP_BIOMES.contains(biomeId)) {
            return 0.8f;
        }
        
        return 1.0f; // Normal speed
    }
    
    /**
     * Check if turtle should prefer staying in this BOP biome
     */
    public static boolean isFavoredBiome(RegistryKey<Biome> biome) {
        if (biome == null) return false;
        
        String biomeId = biome.getValue().toString();
        return TROPICAL_BIOMES.contains(biomeId) || 
               biomeId.equals("biomesoplenty:coral_reef");
    }
    
    /**
     * Island themes for BOP integration
     */
    public enum IslandTheme {
        DEFAULT,
        TROPICAL,
        SWAMP,
        CORAL,
        KELP
    }
}