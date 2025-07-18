package com.bvhfve.aethelon.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.Item;
import net.minecraft.util.Rarity;
import net.minecraft.util.Hand;

/**
 * Turtle Shell Shield - A shield made from turtle shell fragments
 * 
 * Features:
 * - Behaves like a vanilla shield
 * - Has tripled durability (1008 instead of 336)
 * - Crafted from turtle shell fragments and deep sea pearl
 */
public class TurtleShellShieldItem extends ShieldItem {
    
    public TurtleShellShieldItem(Settings settings) {
        super(settings);
    }
    
    
}