package com.bvhfve.aethelon.registry;

import com.bvhfve.aethelon.Aethelon;
import com.bvhfve.aethelon.config.AethelonConfig;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.biome.SpawnSettings;

/**
 * Handles biome modifications for Aethelon spawning
 * Based on Fabric BiomeModifications API patterns from knowledge pool
 */
public class ModBiomeModifications {
    
    /**
     * Registers spawn conditions for Aethelon entities
     * Targets deep ocean biomes with strict spawn controls
     */
    public static void registerSpawnConditions() {
        Aethelon.LOGGER.info("Setting up Aethelon spawn conditions");
        Aethelon.LOGGER.info("Beach spawn weights: Beach={}, Snowy Beach={}, Stony Shore={}", 
                           AethelonConfig.BEACH_SPAWN_WEIGHT, 
                           AethelonConfig.SNOWY_BEACH_SPAWN_WEIGHT, 
                           AethelonConfig.STONY_SHORE_SPAWN_WEIGHT);
        Aethelon.LOGGER.info("Ocean spawn weight: {}", AethelonConfig.INSTANCE.oceanSpawnWeight);
        Aethelon.LOGGER.info("Minimum turtle distance: {} blocks", AethelonConfig.INSTANCE.minimumTurtleDistance);
        
        // Hardcoded beach biome spawning (always enabled)
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(
                net.minecraft.world.biome.BiomeKeys.BEACH
            ),
            SpawnGroup.WATER_CREATURE,
            ModEntityTypes.AETHELON,
            AethelonConfig.BEACH_SPAWN_WEIGHT, // Hardcoded weight: 15
            1, // min group size
            1  // max group size
        );
        
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(
                net.minecraft.world.biome.BiomeKeys.SNOWY_BEACH
            ),
            SpawnGroup.WATER_CREATURE,
            ModEntityTypes.AETHELON,
            AethelonConfig.SNOWY_BEACH_SPAWN_WEIGHT, // Hardcoded weight: 10
            1, // min group size
            1  // max group size
        );
        
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(
                net.minecraft.world.biome.BiomeKeys.STONY_SHORE
            ),
            SpawnGroup.WATER_CREATURE,
            ModEntityTypes.AETHELON,
            AethelonConfig.STONY_SHORE_SPAWN_WEIGHT, // Hardcoded weight: 12
            1, // min group size
            1  // max group size
        );
        
        // Configurable ocean biome spawning
        if (AethelonConfig.INSTANCE.oceanSpawnWeight > 0) {
            BiomeModifications.addSpawn(
                BiomeSelectors.tag(BiomeTags.IS_DEEP_OCEAN), // Target deep ocean biomes
                SpawnGroup.WATER_CREATURE,
                ModEntityTypes.AETHELON,
                AethelonConfig.INSTANCE.oceanSpawnWeight, // Configurable weight (default: 5)
                1, // Min group size (always spawn alone)
                1  // Max group size (always spawn alone)
            );
        }
        
        // TODO: Phase 7 - Add custom spawn conditions with population control
        // TODO: Add distance-based spawn prevention
        // TODO: Add world population tracking
        
        Aethelon.LOGGER.info("Aethelon spawn conditions registered");
    }
}