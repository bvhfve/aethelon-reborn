package com.bvhfve.aethelon.mixin;

import com.bvhfve.aethelon.entity.AethelonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Mixin to make Aethelon turtles solid and allow entities to stand on them
 * Based on solid_mobs implementation but specifically for Aethelon entities
 */
@Mixin(Entity.class)
public abstract class AethelonEntityCollisionMixin {

    @Shadow
    public abstract EntityType<?> getType();

    @Shadow
    public abstract World getWorld();

    @Shadow
    private Box boundingBox;

    @Shadow
    public abstract Vec3d getVelocity();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract Vec3d getPos();

    @Shadow
    public abstract float getHeight();

    @Shadow
    public abstract boolean isAlive();

    @Shadow
    public abstract boolean isLiving();

    @Shadow
    public abstract boolean isSneaking();

    /**
     * Make Aethelon entities collidable like blocks
     */
    @Inject(method = "isCollidable", cancellable = true, at = @At("RETURN"))
    private void aethelon$makeAethelonCollidable(CallbackInfoReturnable<Boolean> cir) {
        // If this is an Aethelon entity, make it collidable
        if (((Entity)(Object)this) instanceof AethelonEntity) {
            cir.setReturnValue(true);
        }
    }

    /**
     * Handle collision logic for Aethelon entities
     */
    @Inject(method = "collidesWith", cancellable = true, at = @At("RETURN"))
    private void aethelon$handleAethelonCollision(Entity other, CallbackInfoReturnable<Boolean> cir) {
        Entity thisEntity = (Entity)(Object)this;
        
        // If this is an Aethelon entity, handle collision with other entities
        if (thisEntity instanceof AethelonEntity aethelonEntity) {
            // Aethelon should collide with all living entities (to act as a platform)
            if (other.isLiving() && other.isAlive()) {
                // Check if the other entity is above the turtle (standing on it)
                boolean isAbove = other.getY() >= thisEntity.getY() + thisEntity.getHeight() - 0.5;
                cir.setReturnValue(isAbove);
            } else {
                // Don't collide with non-living entities (items, projectiles, etc.)
                cir.setReturnValue(false);
            }
        }
        // If other entity is Aethelon, this entity should collide with it
        else if (other instanceof AethelonEntity) {
            if (thisEntity.isLiving() && thisEntity.isAlive()) {
                // Check if this entity is above the turtle
                boolean isAbove = thisEntity.getY() >= other.getY() + other.getHeight() - 0.5;
                cir.setReturnValue(isAbove);
            } else {
                cir.setReturnValue(false);
            }
        }
    }

    /**
     * Move entities that are standing on Aethelon turtles
     */
    @Inject(method = "tick", at = @At("TAIL"))
    private void aethelon$moveEntitiesOnTurtle(CallbackInfo ci) {
        Entity thisEntity = (Entity)(Object)this;
        
        // Only process if this is an Aethelon entity
        if (!(thisEntity instanceof AethelonEntity aethelonEntity)) {
            return;
        }

        // Server-side only
        if (thisEntity.getWorld().isClient) {
            return;
        }

        try {
            // Find entities standing on the turtle
            Box searchBox = boundingBox.expand(1.0, 2.0, 1.0);
            List<Entity> nearbyEntities = thisEntity.getWorld().getOtherEntities(thisEntity, searchBox);

            for (Entity entity : nearbyEntities) {
                if (!entity.isLiving() || !entity.isAlive()) {
                    continue;
                }

                // Check if entity is standing on the turtle
                boolean isStandingOn = entity.getY() >= thisEntity.getY() + thisEntity.getHeight() - 0.1 &&
                                     entity.getY() <= thisEntity.getY() + thisEntity.getHeight() + 2.0;

                if (isStandingOn) {
                    // Keep entity on turtle surface
                    double turtleTopY = thisEntity.getY() + thisEntity.getHeight();
                    
                    // If entity is falling through, place it on surface
                    if (entity.getY() < turtleTopY && entity.getVelocity().y <= 0) {
                        entity.setPosition(entity.getX(), turtleTopY, entity.getZ());
                        entity.setVelocity(entity.getVelocity().x, 0, entity.getVelocity().z);
                        entity.fallDistance = 0.0f;
                    }

                    // Move entity with turtle (reduced multiplier for stability)
                    Vec3d turtleVelocity = thisEntity.getVelocity();
                    if (!turtleVelocity.equals(Vec3d.ZERO)) {
                        if (entity.isSneaking()) {
                            // If sneaking, keep entity centered on turtle
                            entity.setPosition(
                                thisEntity.getX(),
                                entity.getY(),
                                thisEntity.getZ()
                            );
                        } else {
                            // Move with turtle
                            entity.addVelocity(
                                turtleVelocity.x * 0.9,
                                0,
                                turtleVelocity.z * 0.9
                            );
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Silently handle any errors to prevent crashes
        }
    }
}