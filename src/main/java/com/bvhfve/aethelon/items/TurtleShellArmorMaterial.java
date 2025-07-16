package com.bvhfve.aethelon.items;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Ancient Turtle Shell Armor Material
 * Provides excellent protection with water-based bonuses
 */
public class TurtleShellArmorMaterial implements ArmorMaterial {
    
    private static final Map<ArmorItem.Type, Integer> PROTECTION_VALUES = Map.of(
        ArmorItem.Type.BOOTS, 3,
        ArmorItem.Type.LEGGINGS, 6,
        ArmorItem.Type.CHESTPLATE, 8,
        ArmorItem.Type.HELMET, 3
    );
    
    @Override
    public int getProtection(ArmorItem.Type type) {
        return PROTECTION_VALUES.getOrDefault(type, 0);
    }
    
    @Override
    public int getEnchantability() {
        return 15; // Higher than diamond (10) for better enchantments
    }
    
    @Override
    public RegistryEntry<SoundEvent> getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_TURTLE;
    }
    
    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(ModItems.TURTLE_SHELL_FRAGMENT);
    }
    
    @Override
    public List<ArmorMaterial.Layer> getLayers() {
        return List.of(new ArmorMaterial.Layer(Identifier.of("aethelon", "turtle_shell")));
    }
    
    @Override
    public float getToughness() {
        return 3.0f; // Higher than diamond (2.0) for better protection
    }
    
    @Override
    public float getKnockbackResistance() {
        return 0.2f; // 20% knockback resistance
    }
}