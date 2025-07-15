package com.bvhfve.aethelon.mixin;

import com.bvhfve.aethelon.entity.AethelonEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Performance mixin for Aethelon entities
 * Prevents expensive operations for distant entities
 */
@Mixin(Entity.class)
public class EntityMixin {
    
    /**
     * Skip expensive collision checks for distant Aethelon entities
     */
    @Inject(method = "checkBlockCollision", at = @At("HEAD"), cancellable = true)
    private void skipCollisionForDistantAethelon(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        
        if (entity instanceof AethelonEntity aethelon) {
            // Skip collision for very distant entities
            var closestPlayer = entity.getWorld().getClosestPlayer(entity, 150.0);
            if (closestPlayer != null) {
                float distance = aethelon.distanceTo(closestPlayer);
                if (distance > 128.0f) {
                    ci.cancel(); // Skip collision entirely
                }
            } else {
                // No players nearby, skip collision
                ci.cancel();
            }
        }
    }
    
    /**
     * Skip expensive movement calculations for distant Aethelon entities
     */
    @Inject(method = "updateVelocity", at = @At("HEAD"), cancellable = true)
    private void skipVelocityForDistantAethelon(float speed, net.minecraft.util.math.Vec3d movementInput, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        
        if (entity instanceof AethelonEntity aethelon) {
            var closestPlayer = entity.getWorld().getClosestPlayer(entity, 300.0);
            if (closestPlayer != null) {
                float distance = aethelon.distanceTo(closestPlayer);
                if (distance > 256.0f) {
                    ci.cancel(); // Skip velocity updates for very distant entities
                }
            } else {
                // No players nearby, skip velocity updates
                ci.cancel();
            }
        }
    }
}