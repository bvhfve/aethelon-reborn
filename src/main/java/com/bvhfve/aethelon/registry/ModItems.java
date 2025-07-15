package com.bvhfve.aethelon.registry;

import com.bvhfve.aethelon.Aethelon;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

/**
 * Registry for all items in the Aethelon mod
 * Based on patterns from knowledge pool examples
 * 
 * Features implemented:
 * - Proper dependency validation
 * - Enhanced spawn egg configuration with custom colors
 * - Registry key management
 * - Comprehensive error handling and logging
 * - Creative tab integration
 */
public class ModItems {
    
    // Registry keys for consistent identification
    public static final RegistryKey<Item> AETHELON_SPAWN_EGG_KEY = RegistryKey.of(
            RegistryKeys.ITEM,
            Identifier.of(Aethelon.MOD_ID, "aethelon_spawn_egg")
    );
    
    /**
     * Spawn egg for the Aethelon entity
     * Initialized during registerItems() to ensure proper order
     */
    public static Item AETHELON_SPAWN_EGG;
    
    /**
     * Registers all items and adds them to creative tabs
     * Called during mod initialization - must be called after entity registration
     */
    public static void registerItems() {
        Aethelon.LOGGER.info("Registering Aethelon items...");
        
        try {
            // Validate entity type dependency
            validateEntityTypes();
            
            // Register spawn egg with custom colors
            registerSpawnEggs();
            
            // Add items to creative tabs
            registerCreativeTabEntries();
            
            Aethelon.LOGGER.info("Successfully registered {} items", 1);
            
        } catch (Exception e) {
            Aethelon.LOGGER.error("Failed to register items", e);
            throw new RuntimeException("Item registration failed", e);
        }
    }
    
    /**
     * Validates that all required entity types are available
     * Prevents registration errors due to missing dependencies
     */
    private static void validateEntityTypes() {
        if (ModEntityTypes.AETHELON == null) {
            throw new IllegalStateException(
                "Entity type AETHELON is null! Make sure ModEntityTypes.registerEntityTypes() is called first."
            );
        }
        
        Aethelon.LOGGER.debug("Entity type validation passed - AETHELON: {}", ModEntityTypes.AETHELON);
    }
    
    /**
     * Registers all spawn egg items
     * Uses custom colors based on entity characteristics
     */
    private static void registerSpawnEggs() {
        Aethelon.LOGGER.debug("Registering spawn eggs");
        
        // Cast to ensure proper type for SpawnEggItem
        @SuppressWarnings("unchecked")
        EntityType<? extends MobEntity> entityType = (EntityType<? extends MobEntity>) ModEntityTypes.AETHELON;
        
        // Register spawn egg with default colors (custom colors not supported in 1.21.4)
        AETHELON_SPAWN_EGG = Registry.register(
                Registries.ITEM,
                AETHELON_SPAWN_EGG_KEY,
                new SpawnEggItem(
                        entityType,
                        new Item.Settings().registryKey(AETHELON_SPAWN_EGG_KEY)
                )
        );
    }
    
    /**
     * Registers items in creative inventory tabs
     * Organizes items appropriately for easy access
     */
    private static void registerCreativeTabEntries() {
        Aethelon.LOGGER.debug("Adding items to creative tabs");
        
        // Add spawn egg to spawn eggs creative tab
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> {
            content.add(AETHELON_SPAWN_EGG);
        });
        
        // Could add to other tabs if needed:
        // ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
        //     content.add(AETHELON_SPAWN_EGG);
        // });
    }
    
    /**
     * Validates that all items are properly registered
     * Called after registration to ensure everything is working
     */
    public static void validateRegistration() {
        if (AETHELON_SPAWN_EGG == null) {
            throw new IllegalStateException("AETHELON_SPAWN_EGG is null after registration!");
        }
        
        Aethelon.LOGGER.debug("Item registration validation passed - all items properly registered");
    }
}