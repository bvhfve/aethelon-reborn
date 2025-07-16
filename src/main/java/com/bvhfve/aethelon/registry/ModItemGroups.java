package com.bvhfve.aethelon.registry;

import com.bvhfve.aethelon.Aethelon;
import com.bvhfve.aethelon.items.ModItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Custom item group for Aethelon mod items
 */
public class ModItemGroups {
    
    public static final ItemGroup AETHELON_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Aethelon.MOD_ID, "aethelon"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup.aethelon.main"))
                    .icon(() -> new ItemStack(ModItems.TURTLE_HEART))
                    .entries((displayContext, entries) -> {
                        // Spawn Egg
                        entries.add(com.bvhfve.aethelon.registry.ModItems.AETHELON_SPAWN_EGG);
                        
                        // Raw Materials
                        entries.add(ModItems.TURTLE_SHELL_FRAGMENT);
                        entries.add(ModItems.ANCIENT_TURTLE_SCALE);
                        entries.add(ModItems.TURTLE_HEART);
                        entries.add(ModItems.CRYSTALLIZED_WATER);
                        entries.add(ModItems.DEEP_SEA_PEARL);
                        entries.add(ModItems.ISLAND_ESSENCE);
                        
                        // Tools & Utilities
                        entries.add(ModItems.ANCIENT_COMPASS);
                        entries.add(ModItems.ANCIENT_TRIDENT);
                        entries.add(ModItems.TURTLE_SHELL_SHIELD);
                        
                        // Armor Set
                        entries.add(ModItems.TURTLE_SHELL_HELMET);
                        entries.add(ModItems.TURTLE_SHELL_CHESTPLATE);
                        entries.add(ModItems.TURTLE_SHELL_LEGGINGS);
                        entries.add(ModItems.TURTLE_SHELL_BOOTS);
                        entries.add(ModItems.AQUATIC_BOOTS);
                        
                        // Building Blocks
                        entries.add(ModItems.TURTLE_SCALE_BLOCK);
                    })
                    .build());
    
    public static void registerItemGroups() {
        Aethelon.LOGGER.info("Registering item groups for {}", Aethelon.MOD_ID);
    }
}