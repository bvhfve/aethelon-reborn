package com.bvhfve.aethelon.registry;

import com.bvhfve.aethelon.Aethelon;
import com.bvhfve.aethelon.entity.AethelonEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

/**
 * Registry for all entity types in the Aethelon mod
 * Based on patterns from UntitledDuckMod and Fabric API examples
 */
public class ModEntityTypes {
    
    /**
     * The main Aethelon entity type
     * Configured as a large water creature with appropriate tracking settings
     */
    public static final RegistryKey<EntityType<?>> AETHELON_KEY = RegistryKey.of(
            RegistryKeys.ENTITY_TYPE, 
            Identifier.of(Aethelon.MOD_ID, "aethelon")
    );
    
    public static final EntityType<AethelonEntity> AETHELON = Registry.register(
            Registries.ENTITY_TYPE,
            AETHELON_KEY,
            FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, AethelonEntity::new)
                    .dimensions(EntityDimensions.fixed(8.0f, 4.0f)) // Large turtle dimensions
                    .build(AETHELON_KEY)
    );
    
    /**
     * Registers all entity types and their attributes
     * Called during mod initialization
     */
    public static void registerEntityTypes() {
        Aethelon.LOGGER.info("Registering Aethelon entity types");
        
        // Register entity attributes
        FabricDefaultAttributeRegistry.register(AETHELON, AethelonEntity.createAethelonAttributes());
        
        Aethelon.LOGGER.info("Entity types registered successfully");
    }
}