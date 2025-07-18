package com.bvhfve.aethelon.items;

import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Ancient Turtle Shell Armor - Provides water breathing and resistance
 */
public class TurtleShellArmorItem extends ArmorItem {
    
    private final EquipmentSlot equipmentSlot;
    
    public TurtleShellArmorItem(ArmorMaterial material, EquipmentType type, Settings settings) {
        super(material, type, settings);
        this.equipmentSlot = type.getEquipmentSlot();
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player) {
            // Check if player is wearing the armor piece
            ItemStack equippedStack = player.getEquippedStack(this.equipmentSlot);
            if (equippedStack == stack) {
                // Provide water breathing effect
                if (!player.hasStatusEffect(StatusEffects.WATER_BREATHING)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 220, 0, true, false));
                }
                
                // Provide resistance when in water
                if (player.isSubmergedInWater()) {
                    if (!player.hasStatusEffect(StatusEffects.RESISTANCE)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 220, 0, true, false));
                    }
                }
            }
        }
        
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}