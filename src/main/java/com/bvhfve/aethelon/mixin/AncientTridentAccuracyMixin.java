package com.bvhfve.aethelon.mixin;

import com.bvhfve.aethelon.items.ModItems;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Mixin to provide 100% accuracy for Ancient Trident projectiles
 * Based on PerfectAccuracy mod logic but specifically for Ancient Tridents
 */
@Mixin(ProjectileEntity.class)
public abstract class AncientTridentAccuracyMixin {
    
    /**
     * Removes divergence (inaccuracy) for Ancient Trident projectiles
     * This makes the trident fly exactly where the crosshair is aimed
     */
    @ModifyVariable(method = "setVelocity(Lnet/minecraft/entity/Entity;FFFFF)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    public float removeAncientTridentDivergence(float divergence) {
        // Check if this is a TridentEntity with Ancient Trident
        ProjectileEntity projectile = (ProjectileEntity) (Object) this;
        if (projectile instanceof TridentEntity trident) {
            ItemStack tridentStack = trident.getItemStack();
            if (tridentStack.getItem() == ModItems.ANCIENT_TRIDENT) {
                // Remove all divergence for perfect accuracy
                return 0.0f;
            }
        }
        
        // Keep normal divergence for other projectiles
        return divergence;
    }
}