package com.bvhfve.aethelon.registry;

import com.bvhfve.aethelon.Aethelon;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Registry for all items in the Aethelon mod
 * Includes spawn eggs and any future items
 */
public class ModItems {
    
    /**
     * Spawn egg for the Aethelon entity
     * Allows easy spawning for testing and creative mode
     */
    public static final Item AETHELON_SPAWN_EGG = Registry.register(
            Registries.ITEM,
            Identifier.of(Aethelon.MOD_ID, "aethelon_spawn_egg"),
            new SpawnEggItem(
                    ModEntityTypes.AETHELON,
                    new Item.Settings()
            )
    );
    
    /**
     * Registers all items and adds them to creative tabs
     * Called during mod initialization
     */
    public static void registerItems() {
        Aethelon.LOGGER.info("Registering Aethelon items");
        
        // Add spawn egg to creative inventory
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> {
            content.add(AETHELON_SPAWN_EGG);
        });
        
        Aethelon.LOGGER.info("Items registered successfully");
    }
}