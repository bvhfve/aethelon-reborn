package com.bvhfve.aethelon.items;

import com.bvhfve.aethelon.entity.AethelonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Ancient Compass - Points to the nearest Aethelon turtle
 * 
 * A special item that helps players locate ancient world turtles
 */
public class AncientCompassItem extends Item {
    
    public AncientCompassItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        
        if (!world.isClient) {
            // Find the nearest Aethelon turtle
            AethelonEntity nearestTurtle = findNearestTurtle(world, user.getPos());
            
            if (nearestTurtle != null) {
                double distance = user.getPos().distanceTo(nearestTurtle.getPos());
                Vec3d direction = nearestTurtle.getPos().subtract(user.getPos()).normalize();
                
                // Calculate cardinal direction
                String directionText = getCardinalDirection(direction);
                
                user.sendMessage(Text.literal(String.format(
                    "Ancient Compass detects a world turtle %s, %.0f blocks away",
                    directionText, distance
                )), true);
                
                // Add some mystical particles around the player
                if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                    for (int i = 0; i < 10; i++) {
                        double angle = (2 * Math.PI * i) / 10;
                        double x = user.getX() + Math.cos(angle) * 2;
                        double z = user.getZ() + Math.sin(angle) * 2;
                        double y = user.getY() + 1;
                        
                        serverWorld.spawnParticles(net.minecraft.particle.ParticleTypes.ENCHANT,
                                x, y, z, 1, 0, 0, 0, 0.1);
                    }
                }
            } else {
                user.sendMessage(Text.literal(
                    "The Ancient Compass finds no world turtles nearby..."
                ), true);
            }
            
            // Add cooldown
            user.getItemCooldownManager().set(this, 100); // 5 seconds
        }
        
        return TypedActionResult.success(itemStack, world.isClient());
    }
    
    /**
     * Find the nearest Aethelon turtle within a reasonable range
     */
    private AethelonEntity findNearestTurtle(World world, Vec3d playerPos) {
        List<AethelonEntity> turtles = world.getEntitiesByClass(AethelonEntity.class,
                new net.minecraft.util.math.Box(playerPos.add(-1000, -100, -1000), 
                                               playerPos.add(1000, 100, 1000)),
                turtle -> true);
        
        AethelonEntity nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for (AethelonEntity turtle : turtles) {
            double distance = playerPos.distanceTo(turtle.getPos());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearest = turtle;
            }
        }
        
        return nearest;
    }
    
    /**
     * Convert direction vector to cardinal direction string
     */
    private String getCardinalDirection(Vec3d direction) {
        double angle = Math.atan2(direction.z, direction.x);
        angle = Math.toDegrees(angle);
        if (angle < 0) angle += 360;
        
        if (angle >= 337.5 || angle < 22.5) return "to the East";
        else if (angle >= 22.5 && angle < 67.5) return "to the Southeast";
        else if (angle >= 67.5 && angle < 112.5) return "to the South";
        else if (angle >= 112.5 && angle < 157.5) return "to the Southwest";
        else if (angle >= 157.5 && angle < 202.5) return "to the West";
        else if (angle >= 202.5 && angle < 247.5) return "to the Northwest";
        else if (angle >= 247.5 && angle < 292.5) return "to the North";
        else return "to the Northeast";
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        // Add subtle enchantment glint effect when held
        if (selected && entity instanceof PlayerEntity && !world.isClient) {
            if (world.getTime() % 40 == 0) { // Every 2 seconds
                // Spawn subtle particles around the player
                PlayerEntity player = (PlayerEntity) entity;
                if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                    serverWorld.spawnParticles(net.minecraft.particle.ParticleTypes.PORTAL,
                            player.getX(), player.getY() + 1, player.getZ(),
                            3, 0.5, 0.5, 0.5, 0.1);
                }
            }
        }
    }
}