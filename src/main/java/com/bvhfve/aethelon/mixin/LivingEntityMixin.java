package com.bvhfve.aethelon.mixin;

import com.bvhfve.aethelon.compat.AlexsMobsCompat;
import com.bvhfve.aethelon.compat.ModCompatibility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Mixin to provide turtle shell armor protection against Alex's Mobs creatures
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    
    /**
     * Reduce damage from Alex's Mobs when wearing turtle shell armor
     */
    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float reduceDamageFromAlexsMobs(float amount, net.minecraft.server.world.ServerWorld world, DamageSource source, float originalAmount) {
        try {
            // Only apply to players
            if (!((Object) this instanceof PlayerEntity)) {
                return amount;
            }
            
            PlayerEntity player = (PlayerEntity) (Object) this;
            
            // Check if Alex's Mobs is loaded
            if (!ModCompatibility.isModLoaded(ModCompatibility.ALEXS_MOBS)) {
                return amount;
            }
            
            // Check if damage is from an Alex's mob
            Entity attacker = source.getAttacker();
            if (attacker == null) {
                return amount;
            }
            
            // Apply turtle shell protection
            if (AlexsMobsCompat.hasTurtleShellProtection(player, attacker)) {
                float reduction = AlexsMobsCompat.getTurtleShellDamageReduction(player, attacker);
                return amount * (1.0f - reduction);
            }
            
        } catch (Exception e) {
            // Silently handle any compatibility errors
        }
        
        return amount;
    }
}