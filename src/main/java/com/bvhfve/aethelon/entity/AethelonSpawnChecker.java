package com.bvhfve.aethelon.entity;

import com.bvhfve.aethelon.Aethelon;
import com.bvhfve.aethelon.config.AethelonConfig;
import com.bvhfve.aethelon.config.ConfigDebugLogger;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Custom spawn checker for Aethelon entities
 * Implements distance-based spawn prevention to avoid overcrowding
 */
public class AethelonSpawnChecker {
    
    /**
     * Check if an Aethelon can spawn at the given position
     * Implements distance checking to prevent spawning too close to existing turtles
     */
    public static boolean canSpawn(EntityType<? extends MobEntity> type, ServerWorldAccess world, 
                                  SpawnReason spawnReason, BlockPos pos, net.minecraft.util.math.random.Random random) {
        
        // Only apply distance checking in server worlds
        if (!(world instanceof ServerWorld serverWorld)) {
            return true; // Allow spawn in other contexts (like creative spawning)
        }
        
        try {
            // Get minimum distance from config
            int minDistance = AethelonConfig.INSTANCE != null ? 
                AethelonConfig.INSTANCE.minimumTurtleDistance : 128;
            
            // Create search area around spawn position
            Box searchArea = Box.of(
                pos.toCenterPos(), 
                minDistance * 2, 
                minDistance * 2, 
                minDistance * 2
            );
            
            // Find all existing Aethelon entities in the area
            List<AethelonEntity> nearbyTurtles = serverWorld.getEntitiesByClass(
                AethelonEntity.class, 
                searchArea, 
                entity -> entity.isAlive() && !entity.isRemoved()
            );
            
            // Check distance to each nearby turtle
            for (AethelonEntity turtle : nearbyTurtles) {
                double distance = turtle.getPos().distanceTo(pos.toCenterPos());
                if (distance < minDistance) {
                    String reason = String.format("Too close to existing turtle (distance: %.1f, minimum: %d)", distance, minDistance);
                    ConfigDebugLogger.logSpawnAttempt(serverWorld, pos, "unknown", false, reason);
                    
                    Aethelon.LOGGER.debug("Preventing Aethelon spawn at {} - too close to existing turtle at {} (distance: {}, minimum: {})", 
                                        pos, turtle.getBlockPos(), Math.round(distance), minDistance);
                    return false;
                }
            }
            
            // Log successful spawn check
            String reason = nearbyTurtles.isEmpty() ? 
                "No nearby turtles found" : 
                String.format("%d nearby turtles found, all at safe distance", nearbyTurtles.size());
            ConfigDebugLogger.logSpawnAttempt(serverWorld, pos, "unknown", true, reason);
            
            if (!nearbyTurtles.isEmpty()) {
                Aethelon.LOGGER.debug("Allowing Aethelon spawn at {} - {} nearby turtles found, all at safe distance (minimum: {})", 
                                    pos, nearbyTurtles.size(), minDistance);
            }
            
            return true;
            
        } catch (Exception e) {
            Aethelon.LOGGER.error("Error checking Aethelon spawn conditions at {}", pos, e);
            return true; // Allow spawn on error to avoid breaking spawning entirely
        }
    }
    
    /**
     * Get the number of Aethelon entities in the world
     * Useful for population tracking
     */
    public static int getWorldTurtleCount(World world) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return 0;
        }
        
        try {
            // Get all entities in the world (no bounding box needed for world-wide search)
            List<AethelonEntity> allTurtles = new java.util.ArrayList<>();
            for (net.minecraft.entity.Entity entity : serverWorld.iterateEntities()) {
                if (entity instanceof AethelonEntity turtle && turtle.isAlive() && !turtle.isRemoved()) {
                    allTurtles.add(turtle);
                }
            }
            return allTurtles.size();
        } catch (Exception e) {
            Aethelon.LOGGER.error("Error counting Aethelon entities in world", e);
            return 0;
        }
    }
    
    /**
     * Check if the world has reached maximum turtle population
     */
    public static boolean hasReachedMaxPopulation(World world) {
        if (AethelonConfig.INSTANCE == null) {
            return false;
        }
        
        int currentCount = getWorldTurtleCount(world);
        int maxPopulation = AethelonConfig.INSTANCE.max_world_population;
        
        if (currentCount >= maxPopulation) {
            Aethelon.LOGGER.debug("World turtle population limit reached: {}/{}", currentCount, maxPopulation);
            return true;
        }
        
        return false;
    }
    
    /**
     * Enhanced spawn check that includes both distance and population limits
     */
    public static boolean canSpawnWithAllChecks(EntityType<? extends MobEntity> type, ServerWorldAccess world, 
                                               SpawnReason spawnReason, BlockPos pos, net.minecraft.util.math.random.Random random) {
        
        // Check population limit first (faster check)
        if (hasReachedMaxPopulation(world.toServerWorld())) {
            ConfigDebugLogger.logSpawnAttempt(world.toServerWorld(), pos, "unknown", false, 
                "World population limit reached");
            return false;
        }
        
        // Then check distance requirements
        return canSpawn(type, world, spawnReason, pos, random);
    }
}