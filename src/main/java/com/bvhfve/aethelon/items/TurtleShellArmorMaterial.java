package com.bvhfve.aethelon.items;

import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.item.Item;

import java.util.EnumMap;

/**
 * Ancient Turtle Shell Armor Material
 * Provides excellent protection with water-based bonuses
 */
public class TurtleShellArmorMaterial {
    
    // Equipment asset registry key
    static RegistryKey<? extends Registry<EquipmentAsset>> EQUIPMENT_REGISTRY_KEY = 
        RegistryKey.ofRegistry(Identifier.ofVanilla("equipment_asset"));
    
    // Register equipment asset for turtle shell armor
    public static final RegistryKey<EquipmentAsset> TURTLE_SHELL_EQUIPMENT_ASSET = 
        RegistryKey.of(EQUIPMENT_REGISTRY_KEY, Identifier.of("aethelon", "turtle_shell"));
    
    // Create a custom repair tag for turtle shell items
    public static final TagKey<Item> TURTLE_SHELL_REPAIR_ITEMS = TagKey.of(RegistryKeys.ITEM, Identifier.of("aethelon", "turtle_shell_repair_items"));
    
    // Create the armor material using the correct 1.21.4 API
    public static final ArmorMaterial TURTLE_SHELL_ARMOR_MATERIAL = new ArmorMaterial(
        275, // durability multiplier
        Util.make(new EnumMap<>(EquipmentType.class), map -> {
            map.put(EquipmentType.BOOTS, 3);
            map.put(EquipmentType.LEGGINGS, 6);
            map.put(EquipmentType.CHESTPLATE, 8);
            map.put(EquipmentType.HELMET, 3);
            map.put(EquipmentType.BODY, 8); // For elytra-style items
        }),
        15, // enchantability
        SoundEvents.ITEM_ARMOR_EQUIP_TURTLE,
        3.0f, // toughness
        0.2f, // knockback resistance
        TURTLE_SHELL_REPAIR_ITEMS, // repair ingredient tag
        TURTLE_SHELL_EQUIPMENT_ASSET
    );
}