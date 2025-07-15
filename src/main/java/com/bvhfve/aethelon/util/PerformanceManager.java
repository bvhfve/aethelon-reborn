package com.bvhfve.aethelon.util;

import com.bvhfve.aethelon.config.AethelonConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * Performance management utilities for Aethelon entities
 * Provides centralized performance optimization logic
 */
public class PerformanceManager {
    
    // Performance thresholds
    private static final float NEAR_DISTANCE = 32.0f;
    private static final float MID_DISTANCE = 64.0f;
    private static final float FAR_DISTANCE = 128.0f;
    private static final float MAX_DISTANCE = 256.0f;
    
    // Performance levels
    public enum PerformanceLevel {
        HIGH,    // Near distance - full detail
        MEDIUM,  // Mid distance - reduced animations
        LOW,     // Far distance - minimal detail
        MINIMAL  // Very far - basic rendering only
    }
    
    /**
     * Determine performance level based on distance to player
     */
    public static PerformanceLevel getPerformanceLevel(Entity entity) {
        float distance = getDistanceToPlayer(entity);
        
        if (distance <= NEAR_DISTANCE) {
            return PerformanceLevel.HIGH;
        } else if (distance <= MID_DISTANCE) {
            return PerformanceLevel.MEDIUM;
        } else if (distance <= FAR_DISTANCE) {
            return PerformanceLevel.LOW;
        } else {
            return PerformanceLevel.MINIMAL;
        }
    }
    
    /**
     * Check if entity should be rendered at all
     */
    public static boolean shouldRender(Entity entity) {
        float distance = getDistanceToPlayer(entity);
        return distance <= MAX_DISTANCE;
    }
    
    /**
     * Check if entity should have shadows
     */
    public static boolean shouldRenderShadow(Entity entity) {
        return getPerformanceLevel(entity) != PerformanceLevel.MINIMAL;
    }
    
    /**
     * Check if entity should animate
     */
    public static boolean shouldAnimate(Entity entity) {
        PerformanceLevel level = getPerformanceLevel(entity);
        return level == PerformanceLevel.HIGH || level == PerformanceLevel.MEDIUM;
    }
    
    /**
     * Get tick rate divider based on performance level
     */
    public static int getTickRateDivider(Entity entity) {
        PerformanceLevel level = getPerformanceLevel(entity);
        return switch (level) {
            case HIGH -> 1;     // Full tick rate
            case MEDIUM -> 2;   // Half tick rate
            case LOW -> 4;      // Quarter tick rate
            case MINIMAL -> 8;  // Eighth tick rate
        };
    }
    
    /**
     * Get efficient distance to player
     */
    private static float getDistanceToPlayer(Entity entity) {
        // Find closest player (works on both client and server)
        var closestPlayer = entity.getWorld().getClosestPlayer(entity, MAX_DISTANCE);
        if (closestPlayer != null) {
            Vec3d playerPos = closestPlayer.getPos();
            Vec3d entityPos = entity.getPos();
            
            // Use Manhattan distance for performance (avoids sqrt)
            double dx = Math.abs(playerPos.x - entityPos.x);
            double dz = Math.abs(playerPos.z - entityPos.z);
            return (float) (dx + dz);
        }
        return Float.MAX_VALUE;
    }
    
    /**
     * Check if performance optimizations are enabled
     */
    public static boolean isOptimizationEnabled() {
        return AethelonConfig.INSTANCE == null || !AethelonConfig.INSTANCE.enable_debug_logging;
    }
    
    /**
     * Get animation cache interval based on performance level
     */
    public static int getAnimationCacheInterval(Entity entity) {
        PerformanceLevel level = getPerformanceLevel(entity);
        return switch (level) {
            case HIGH -> 1;     // No caching
            case MEDIUM -> 2;   // Cache every 2 frames
            case LOW -> 4;      // Cache every 4 frames
            case MINIMAL -> 8;  // Cache every 8 frames
        };
    }
}