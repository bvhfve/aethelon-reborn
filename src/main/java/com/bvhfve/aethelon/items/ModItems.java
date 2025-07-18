package com.bvhfve.aethelon.items;

import com.bvhfve.aethelon.Aethelon;
import net.minecraft.item.ArmorItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.item.Item;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

/**
 * Custom items for the Aethelon mod
 * 
 * Includes turtle-themed items and special artifacts
 */
public class ModItems {
    
    // Turtle-themed items
    public static final Item ANCIENT_TURTLE_SCALE;
    public static final Item TURTLE_SHELL_FRAGMENT;
    public static final Item TURTLE_HEART;
    
    // Special artifacts
    public static final Item ANCIENT_COMPASS;
    public static final Item ISLAND_ESSENCE;
    
    // Crafting materials
    public static final Item CRYSTALLIZED_WATER;
    public static final Item DEEP_SEA_PEARL;
    
    // Turtle Shell Armor Set
    public static final Item TURTLE_SHELL_HELMET;
    public static final Item TURTLE_SHELL_CHESTPLATE;
    public static final Item TURTLE_SHELL_LEGGINGS;
    public static final Item TURTLE_SHELL_BOOTS;
    
    // Ancient Trident
    public static final Item ANCIENT_TRIDENT;
    
    // Utility items
    public static final Item TURTLE_SHELL_SHIELD;
    public static final Item AQUATIC_BOOTS;
    
    // Building blocks
    public static final Item TURTLE_SCALE_BLOCK;
    
    // Spawn eggs
    public static final Item AETHELON_SPAWN_EGG;
    
    // Initialize items using FabricWaystone pattern with registryKey()
    static {
        ANCIENT_TURTLE_SCALE = registerItem("ancient_turtle_scale",
                new Item(new Item.Settings().rarity(Rarity.UNCOMMON).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "ancient_turtle_scale")))));
        
        TURTLE_SHELL_FRAGMENT = registerItem("turtle_shell_fragment", 
                new Item(new Item.Settings().rarity(Rarity.COMMON).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "turtle_shell_fragment")))));
        
        TURTLE_HEART = registerItem("turtle_heart",
                new Item(new Item.Settings().rarity(Rarity.EPIC).maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "turtle_heart")))));
        
        ANCIENT_COMPASS = registerItem("ancient_compass",
                new AncientCompassItem(new Item.Settings().rarity(Rarity.RARE).maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "ancient_compass")))));
        
        ISLAND_ESSENCE = registerItem("island_essence",
                new Item(new Item.Settings().rarity(Rarity.RARE).maxCount(16).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "island_essence")))));
        
        CRYSTALLIZED_WATER = registerItem("crystallized_water",
                new Item(new Item.Settings().rarity(Rarity.UNCOMMON).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "crystallized_water")))));
        
        DEEP_SEA_PEARL = registerItem("deep_sea_pearl",
                new Item(new Item.Settings().rarity(Rarity.RARE).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "deep_sea_pearl")))));
        
        TURTLE_SHELL_HELMET = registerItem("turtle_shell_helmet",
                new Item(new Item.Settings().rarity(Rarity.RARE).maxDamage(275).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "turtle_shell_helmet")))));
        
        TURTLE_SHELL_CHESTPLATE = registerItem("turtle_shell_chestplate",
                new Item(new Item.Settings().rarity(Rarity.RARE).maxDamage(400).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "turtle_shell_chestplate")))));
        
        TURTLE_SHELL_LEGGINGS = registerItem("turtle_shell_leggings",
                new Item(new Item.Settings().rarity(Rarity.RARE).maxDamage(375).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "turtle_shell_leggings")))));
        
        TURTLE_SHELL_BOOTS = registerItem("turtle_shell_boots",
                new Item(new Item.Settings().rarity(Rarity.RARE).maxDamage(325).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "turtle_shell_boots")))));
        
        ANCIENT_TRIDENT = registerItem("ancient_trident",
                new AncientTridentItem(new Item.Settings().rarity(Rarity.EPIC).maxDamage(350).attributeModifiers(TridentItem.createAttributeModifiers()).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "ancient_trident")))));
        
        TURTLE_SHELL_SHIELD = registerItem("turtle_shell_shield",
                new Item(new Item.Settings().rarity(Rarity.UNCOMMON).maxDamage(500).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "turtle_shell_shield")))));
        
        AQUATIC_BOOTS = registerItem("aquatic_boots",
                new Item(new Item.Settings().rarity(Rarity.UNCOMMON).maxDamage(200).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "aquatic_boots")))));
        
        TURTLE_SCALE_BLOCK = registerItem("turtle_scale_block",
                new Item(new Item.Settings().rarity(Rarity.UNCOMMON).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "turtle_scale_block")))));
        
        AETHELON_SPAWN_EGG = registerItem("aethelon_spawn_egg",
                new net.minecraft.item.SpawnEggItem(com.bvhfve.aethelon.registry.ModEntityTypes.AETHELON, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Aethelon.MOD_ID, "aethelon_spawn_egg")))));
    }
    
    /**
     * Register an item with the given name and item instance
     */
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Aethelon.MOD_ID, name), item);
    }
    
    /**
     * Initialize all mod items - called from main mod class
     */
    public static void registerModItems() {
        // Force static initialization by accessing a field
        // This ensures all items are registered
        Item dummy = ANCIENT_TURTLE_SCALE;
        Aethelon.LOGGER.info("Registering custom items for {}", Aethelon.MOD_ID);
    }
}