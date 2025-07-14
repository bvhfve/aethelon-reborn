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
        
        // Add Aethelon spawning to deep ocean biomes
        // Using BiomeSelectors to target appropriate biomes
        BiomeModifications.addSpawn(
                BiomeSelectors.tag(BiomeTags.IS_DEEP_OCEAN), // Target deep ocean biomes
                SpawnGroup.WATER_CREATURE,
                ModEntityTypes.AETHELON,
                1, // Very low weight for rarity
                1, // Min group size (always spawn alone)
                1  // Max group size (always spawn alone)
        );
        
        // TODO: Phase 7 - Add custom spawn conditions with population control
        // TODO: Add distance-based spawn prevention
        // TODO: Add world population tracking
        
        Aethelon.LOGGER.info("Aethelon spawn conditions registered");
    }
}