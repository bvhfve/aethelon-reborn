package com.bvhfve.aethelon.mixin;

import com.bvhfve.aethelon.items.ModItems;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Additional mixin to handle gravity effects for Ancient Trident
 * Ensures the trident flies in a perfectly straight line
 */
@Mixin(TridentEntity.class)
public abstract class AncientTridentGravityMixin {
    
    /**
     * Prevents gravity from affecting Ancient Trident projectiles
     * This ensures they fly in a perfectly straight line to the target
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void preventGravityForAncientTrident(CallbackInfo ci) {
        TridentEntity trident = (TridentEntity) (Object) this;
        ItemStack tridentStack = trident.getItemStack();
        
        // Check if this is an Ancient Trident
        if (tridentStack.getItem() == ModItems.ANCIENT_TRIDENT) {
            // Disable gravity effects by setting hasNoGravity to true
            trident.setNoGravity(true);
        }
    }
}