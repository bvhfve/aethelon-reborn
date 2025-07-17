package com.bvhfve.aethelon.structure;

import com.bvhfve.aethelon.Aethelon;
import com.bvhfve.aethelon.entity.AethelonEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Phase 5: Moving Island Manager - Handles dynamic island movement without complex structure APIs
 * Uses a virtual block tracking system that moves with the turtle
 */
public class MovingIslandManager {
    
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonMovingIslandManager");
    
    private final AethelonEntity turtle;
    private boolean hasIsland = false;
    private String currentStructureName;
    private Vec3d islandSize = Vec3d.ZERO;
    private Vec3d lastTurtlePosition = Vec3d.ZERO;
    
    // Virtual island tracking - blocks relative to turtle position
    private Map<Vec3d, BlockState> relativeIslandBlocks = new HashMap<>();
    private List<Entity> islandEntities = new ArrayList<>();
    private Box islandBounds = null;
    
    public enum IslandSize {
        SMALL("small", 16),
        MEDIUM("medium", 24),
        LARGE("large", 32);
        
        public final String category;
        public final int maxDimension;
        
        IslandSize(String category, int maxDimension) {
            this.category = category;
            this.maxDimension = maxDimension;
        }
    }
    
    public MovingIslandManager(AethelonEntity turtle) {
        this.turtle = turtle;
        this.lastTurtlePosition = turtle.getPos();
    }
    
    /**
     * Check if the turtle has an island
     */
    public boolean hasIsland() {
        return hasIsland;
    }
    
    /**
     * Get current structure name
     */
    public String getCurrentStructureName() {
        return currentStructureName;
    }
    
    /**
     * Spawn an island structure that moves with the turtle
     */
    public boolean spawnMovingIsland(World world, String structureName) {
        if (world.isClient || !(world instanceof ServerWorld serverWorld)) {
            return false;
        }
        
        try {
            LOGGER.info("Spawning moving island: {}", structureName);
            
            // Get turtle position
            Vec3d turtlePos = turtle.getPos();
            BlockPos spawnPos = BlockPos.ofFloored(turtlePos.add(0, 1, 0)); // Slightly above turtle
            
            // Clear existing island
            if (hasIsland) {
                removeMovingIsland(world);
            }
            
            // Spawn structure using datapack system
            DatapackStructureManager.StructureSpawnResult result = 
                DatapackStructureManager.spawnStructure(serverWorld, structureName, spawnPos);
            
            if (result.success) {
                this.currentStructureName = structureName;
                this.islandSize = result.definition.size;
                this.hasIsland = true;
                this.lastTurtlePosition = turtlePos;
                
                // Capture the island blocks in relative coordinates
                captureIslandBlocks(serverWorld, spawnPos, result.definition.size);
                
                LOGGER.info("Successfully spawned moving island: {} at {}", structureName, spawnPos);
                return true;
            } else {
                LOGGER.error("Failed to spawn moving island: {}", result.errorMessage);
                return false;
            }
            
        } catch (Exception e) {
            LOGGER.error("Exception while spawning moving island: {}", structureName, e);
            return false;
        }
    }
    
    /**
     * Spawn a random moving island based on size
     */
    public boolean spawnRandomMovingIsland(World world, IslandSize size) {
        if (world.isClient || !(world instanceof ServerWorld serverWorld)) {
            return false;
        }
        
        try {
            LOGGER.info("Spawning random {} moving island", size.category);
            
            Vec3d turtlePos = turtle.getPos();
            BlockPos spawnPos = BlockPos.ofFloored(turtlePos.add(0, 1, 0));
            
            // Clear existing island
            if (hasIsland) {
                removeMovingIsland(world);
            }
            
            // Use default ocean biome for simplicity
            String biomeCategory = "ocean";
            
            // Spawn random structure
            DatapackStructureManager.StructureSpawnResult result = 
                DatapackStructureManager.spawnRandomStructure(serverWorld, size.category, spawnPos, biomeCategory);
            
            if (result.success) {
                this.currentStructureName = result.definition.name;
                this.islandSize = result.definition.size;
                this.hasIsland = true;
                this.lastTurtlePosition = turtlePos;
                
                // Capture the island blocks in relative coordinates
                captureIslandBlocks(serverWorld, spawnPos, result.definition.size);
                
                LOGGER.info("Successfully spawned random moving island: {} at {}", 
                          result.definition.name, spawnPos);
                return true;
            } else {
                LOGGER.error("Failed to spawn random moving island: {}", result.errorMessage);
                return false;
            }
            
        } catch (Exception e) {
            LOGGER.error("Exception while spawning random moving island", e);
            return false;
        }
    }
    
    /**
     * Capture island blocks in relative coordinates to the turtle
     */
    private void captureIslandBlocks(ServerWorld world, BlockPos centerPos, Vec3d size) {
        relativeIslandBlocks.clear();
        
        Vec3d turtlePos = turtle.getPos();
        int halfWidth = (int) (size.x / 2);
        int halfLength = (int) (size.z / 2);
        int height = (int) size.y;
        
        // Scan the structure area and store blocks relative to turtle
        for (int x = -halfWidth; x <= halfWidth; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = -halfLength; z <= halfLength; z++) {
                    BlockPos worldPos = centerPos.add(x, y, z);
                    BlockState state = world.getBlockState(worldPos);
                    
                    if (!state.isAir()) {
                        // Store relative to turtle position
                        Vec3d relativePos = new Vec3d(x, y + 1, z); // +1 for offset above turtle
                        relativeIslandBlocks.put(relativePos, state);
                    }
                }
            }
        }
        
        // Update bounds
        updateIslandBounds();
        
        LOGGER.debug("Captured {} blocks for moving island", relativeIslandBlocks.size());
    }
    
    /**
     * Update island bounds relative to current turtle position
     */
    private void updateIslandBounds() {
        if (!hasIsland) {
            islandBounds = null;
            return;
        }
        
        Vec3d turtlePos = turtle.getPos();
        Vec3d center = turtlePos.add(0, islandSize.y / 2 + 1, 0);
        islandBounds = Box.of(center, islandSize.x, islandSize.y, islandSize.z);
    }
    
    /**
     * Update island position when turtle moves - Phase 5 implementation
     */
    public void updateIslandPosition(World world) {
        if (!hasIsland || world.isClient) return;
        
        Vec3d currentTurtlePos = turtle.getPos();
        Vec3d movement = currentTurtlePos.subtract(lastTurtlePosition);
        
        // Only update if turtle moved significantly
        if (movement.length() < 0.1) return;
        
        try {
            // Move all island blocks
            moveIslandBlocks(world, movement);
            
            // Update entity positions
            moveIslandEntities(movement);
            
            // Update bounds
            updateIslandBounds();
            
            this.lastTurtlePosition = currentTurtlePos;
            
        } catch (Exception e) {
            LOGGER.error("Failed to update island position", e);
        }
    }
    
    /**
     * Move all island blocks with the turtle
     */
    private void moveIslandBlocks(World world, Vec3d movement) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        
        // Calculate old and new world positions for all blocks
        Map<BlockPos, BlockPos> blockMoves = new HashMap<>();
        Vec3d oldTurtlePos = lastTurtlePosition;
        Vec3d newTurtlePos = turtle.getPos();
        
        for (Vec3d relativePos : relativeIslandBlocks.keySet()) {
            BlockPos oldWorldPos = BlockPos.ofFloored(oldTurtlePos.add(relativePos));
            BlockPos newWorldPos = BlockPos.ofFloored(newTurtlePos.add(relativePos));
            blockMoves.put(oldWorldPos, newWorldPos);
        }
        
        // Move blocks efficiently
        for (Map.Entry<BlockPos, BlockPos> move : blockMoves.entrySet()) {
            BlockPos oldPos = move.getKey();
            BlockPos newPos = move.getValue();
            
            if (!oldPos.equals(newPos)) {
                BlockState state = serverWorld.getBlockState(oldPos);
                if (!state.isAir()) {
                    // Remove old block
                    serverWorld.setBlockState(oldPos, Blocks.AIR.getDefaultState());
                    // Place at new position
                    serverWorld.setBlockState(newPos, state);
                }
            }
        }
    }
    
    /**
     * Move entities on the island
     */
    private void moveIslandEntities(Vec3d movement) {
        for (Entity entity : islandEntities) {
            if (entity.isAlive() && !entity.isRemoved()) {
                Vec3d newPos = entity.getPos().add(movement);
                entity.setPosition(newPos);
            }
        }
        
        // Remove dead/removed entities
        islandEntities.removeIf(entity -> !entity.isAlive() || entity.isRemoved());
    }
    
    /**
     * Remove the moving island
     */
    public void removeMovingIsland(World world) {
        if (!hasIsland || world.isClient) return;
        
        LOGGER.info("Removing moving island from turtle");
        
        if (world instanceof ServerWorld serverWorld) {
            // Remove all island blocks
            Vec3d turtlePos = turtle.getPos();
            for (Vec3d relativePos : relativeIslandBlocks.keySet()) {
                BlockPos worldPos = BlockPos.ofFloored(turtlePos.add(relativePos));
                serverWorld.setBlockState(worldPos, Blocks.AIR.getDefaultState());
            }
        }
        
        // Clear data
        clearIslandData();
    }
    
    /**
     * Clear all island data
     */
    private void clearIslandData() {
        relativeIslandBlocks.clear();
        islandEntities.clear();
        islandBounds = null;
        hasIsland = false;
        currentStructureName = null;
        islandSize = Vec3d.ZERO;
    }
    
    /**
     * Check if a position is on the island
     */
    public boolean isPositionOnIsland(Vec3d position) {
        if (!hasIsland || islandBounds == null) {
            return false;
        }
        return islandBounds.contains(position);
    }
    
    /**
     * Get island bounds
     */
    public Box getIslandBounds() {
        return islandBounds;
    }
    
    /**
     * Get entities on the island
     */
    public List<Entity> getIslandEntities() {
        return new ArrayList<>(islandEntities);
    }
}