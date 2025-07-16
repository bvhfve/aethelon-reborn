package com.bvhfve.aethelon.items;

import com.bvhfve.aethelon.Aethelon;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

/**
 * Custom items for the Aethelon mod
 * 
 * Includes turtle-themed items and special artifacts
 */
public class ModItems {
    
    // Turtle-themed items
    public static final Item ANCIENT_TURTLE_SCALE = registerItem("ancient_turtle_scale",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    
    public static final Item TURTLE_SHELL_FRAGMENT = registerItem("turtle_shell_fragment", 
            new Item(new Item.Settings().rarity(Rarity.COMMON)));
    
    public static final Item TURTLE_HEART = registerItem("turtle_heart",
            new Item(new Item.Settings().rarity(Rarity.EPIC).maxCount(1)));
    
    // Special artifacts
    public static final Item ANCIENT_COMPASS = registerItem("ancient_compass",
            new AncientCompassItem(new Item.Settings().rarity(Rarity.RARE).maxCount(1)));
    
    public static final Item ISLAND_ESSENCE = registerItem("island_essence",
            new Item(new Item.Settings().rarity(Rarity.RARE).maxCount(16)));
    
    // Crafting materials
    public static final Item CRYSTALLIZED_WATER = registerItem("crystallized_water",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    
    public static final Item DEEP_SEA_PEARL = registerItem("deep_sea_pearl",
            new Item(new Item.Settings().rarity(Rarity.RARE)));
    
    // Armor material
    public static final ArmorMaterial TURTLE_SHELL_ARMOR_MATERIAL = new TurtleShellArmorMaterial();
    
    // Turtle Shell Armor Set
    public static final Item TURTLE_SHELL_HELMET = registerItem("turtle_shell_helmet",
            new TurtleShellArmorItem(TURTLE_SHELL_ARMOR_MATERIAL, ArmorItem.Type.HELMET, 
                    new Item.Settings().rarity(Rarity.RARE).maxDamage(ArmorItem.Type.HELMET.getMaxDamage(37))));
    
    public static final Item TURTLE_SHELL_CHESTPLATE = registerItem("turtle_shell_chestplate",
            new TurtleShellArmorItem(TURTLE_SHELL_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Settings().rarity(Rarity.RARE).maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(37))));
    
    public static final Item TURTLE_SHELL_LEGGINGS = registerItem("turtle_shell_leggings",
            new TurtleShellArmorItem(TURTLE_SHELL_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Settings().rarity(Rarity.RARE).maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(37))));
    
    public static final Item TURTLE_SHELL_BOOTS = registerItem("turtle_shell_boots",
            new TurtleShellArmorItem(TURTLE_SHELL_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Settings().rarity(Rarity.RARE).maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(37))));
    
    // Ancient Trident
    public static final Item ANCIENT_TRIDENT = registerItem("ancient_trident",
            new AncientTridentItem(new Item.Settings().rarity(Rarity.EPIC).maxDamage(350).attributeModifiers(TridentItem.createAttributeModifiers())));
    
    // Utility items
    public static final Item TURTLE_SHELL_SHIELD = registerItem("turtle_shell_shield",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON).maxDamage(500)));
    
    public static final Item AQUATIC_BOOTS = registerItem("aquatic_boots",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON).maxDamage(200)));
    
    // Building blocks
    public static final Item TURTLE_SCALE_BLOCK = registerItem("turtle_scale_block",
            new Item(new Item.Settings().rarity(Rarity.UNCOMMON)));
    
    /**
     * Register an item with the given name and item instance
     */
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Aethelon.MOD_ID, name), item);
    }
    
    /**
     * Initialize all mod items
     */
    public static void registerModItems() {
        // Items are registered when the class is loaded
        Aethelon.LOGGER.info("Registering custom items for {}", Aethelon.MOD_ID);
    }
}