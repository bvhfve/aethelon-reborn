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
                        // Spawn Egg - temporarily disabled due to initialization order
                        // if (ModItems.AETHELON_SPAWN_EGG != null) {
                        //     entries.add(ModItems.AETHELON_SPAWN_EGG);
                        // }
                        
                        // Raw Materials
                        if (ModItems.TURTLE_SHELL_FRAGMENT != null) entries.add(ModItems.TURTLE_SHELL_FRAGMENT);
                        if (ModItems.ANCIENT_TURTLE_SCALE != null) entries.add(ModItems.ANCIENT_TURTLE_SCALE);
                        if (ModItems.TURTLE_HEART != null) entries.add(ModItems.TURTLE_HEART);
                        if (ModItems.CRYSTALLIZED_WATER != null) entries.add(ModItems.CRYSTALLIZED_WATER);
                        if (ModItems.DEEP_SEA_PEARL != null) entries.add(ModItems.DEEP_SEA_PEARL);
                        if (ModItems.ISLAND_ESSENCE != null) entries.add(ModItems.ISLAND_ESSENCE);
                        
                        // Tools & Utilities
                        if (ModItems.ANCIENT_COMPASS != null) entries.add(ModItems.ANCIENT_COMPASS);
                        if (ModItems.ANCIENT_TRIDENT != null) entries.add(ModItems.ANCIENT_TRIDENT);
                        if (ModItems.TURTLE_SHELL_SHIELD != null) entries.add(ModItems.TURTLE_SHELL_SHIELD);
                        
                        // Armor Set
                        if (ModItems.TURTLE_SHELL_HELMET != null) entries.add(ModItems.TURTLE_SHELL_HELMET);
                        if (ModItems.TURTLE_SHELL_CHESTPLATE != null) entries.add(ModItems.TURTLE_SHELL_CHESTPLATE);
                        if (ModItems.TURTLE_SHELL_LEGGINGS != null) entries.add(ModItems.TURTLE_SHELL_LEGGINGS);
                        if (ModItems.TURTLE_SHELL_BOOTS != null) entries.add(ModItems.TURTLE_SHELL_BOOTS);
                        if (ModItems.AQUATIC_BOOTS != null) entries.add(ModItems.AQUATIC_BOOTS);
                        
                        // Building Blocks
                        if (ModItems.TURTLE_SCALE_BLOCK != null) entries.add(ModItems.TURTLE_SCALE_BLOCK);
                    })
                    .build());
    
    public static void registerItemGroups() {
        Aethelon.LOGGER.info("Registering item groups for {}", Aethelon.MOD_ID);
    }
}