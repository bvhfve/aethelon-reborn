package com.bvhfve.aethelon.registry;

import com.bvhfve.aethelon.Aethelon;
import com.bvhfve.aethelon.entity.AethelonEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

/**
 * Registry for all entity types in the Aethelon mod
 * Based on patterns from knowledge pool examples and best practices
 * 
 * Features implemented:
 * - Proper registry key management
 * - Enhanced entity type builder configuration
 * - Spawn restrictions for realistic spawning
 * - Comprehensive logging and error handling
 */
public class ModEntityTypes {
    
    // Registry keys for consistent identification
    public static final RegistryKey<EntityType<?>> AETHELON_KEY = RegistryKey.of(
            RegistryKeys.ENTITY_TYPE, 
            Identifier.of(Aethelon.MOD_ID, "aethelon")
    );
    
    /**
     * The main Aethelon entity type
     * Initialized during registerEntityTypes() to ensure proper order
     */
    public static EntityType<AethelonEntity> AETHELON;
    
    /**
     * Registers all entity types and their attributes
     * Called during mod initialization - must be called before items registration
     */
    public static void registerEntityTypes() {
        Aethelon.LOGGER.info("Registering Aethelon entity types...");
        
        try {
            // Register the Aethelon entity type with enhanced configuration
            AETHELON = Registry.register(
                    Registries.ENTITY_TYPE,
                    AETHELON_KEY,
                    FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, AethelonEntity::new)
                            .dimensions(EntityDimensions.fixed(8.0f, 4.0f)) // Massive world turtle
                            .trackRangeChunks(32) // Large tracking range for big entity
                            .trackedUpdateRate(1) // Frequent updates for smooth movement
                            .build(AETHELON_KEY)
            );
            
            // Register entity attributes
            FabricDefaultAttributeRegistry.register(AETHELON, AethelonEntity.createAethelonAttributes());
            
            // Register spawn restrictions for realistic spawning
            registerSpawnRestrictions();
            
            Aethelon.LOGGER.info("Successfully registered {} entity types", 1);
            
        } catch (Exception e) {
            Aethelon.LOGGER.error("Failed to register entity types", e);
            throw new RuntimeException("Entity type registration failed", e);
        }
    }
    
    /**
     * Registers spawn restrictions for entities
     * Ensures entities only spawn in appropriate conditions
     */
    private static void registerSpawnRestrictions() {
        Aethelon.LOGGER.debug("Registering spawn restrictions for Aethelon entities");
        
        SpawnRestriction.register(
                AETHELON,
                SpawnLocationTypes.IN_WATER,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                AethelonEntity::canSpawn
        );
    }
    
    /**
     * Validates that all entity types are properly registered
     * Called after registration to ensure everything is working
     */
    public static void validateRegistration() {
        if (AETHELON == null) {
            throw new IllegalStateException("AETHELON entity type is null after registration!");
        }
        
        Aethelon.LOGGER.debug("Entity type validation passed - all entities properly registered");
    }
}