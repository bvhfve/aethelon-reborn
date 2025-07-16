package com.bvhfve.aethelon.ai.goals;

import com.bvhfve.aethelon.entity.AethelonEntity;
import com.bvhfve.aethelon.config.AethelonConfig;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.EnumSet;

/**
 * AI Goal for Aethelon pathfinding and movement
 * Handles ocean navigation with deep water requirements and obstacle avoidance
 * 
 * Based on Phase 2 requirements for realistic world turtle movement
 */
public class AethelonPathfindGoal extends Goal {
    
    private final AethelonEntity turtle;
    private Vec3d targetDestination;
    private Path currentPath;
    private int pathfindingTimer;
    private int movementTimer;
    private int stuckTimer;
    private Vec3d lastPosition;
    
    // Movement constants
    // Movement speeds now configurable
    private double getMovementSpeed() {
        return AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.normal_movement_speed : 0.45;
    }
    
    private double getEscapeSpeed() {
        return AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.escape_movement_speed : 0.75;
    }
    private static final double ARRIVAL_DISTANCE = 10.0; // Close enough to destination
    private static final int PATHFINDING_INTERVAL = 20 * 10; // Recalculate every 10 seconds
    private static final int STUCK_THRESHOLD = 20 * 30; // 30 seconds without movement
    private static final int MIN_WATER_DEPTH = 10; // Minimum water depth required
    
    public AethelonPathfindGoal(AethelonEntity turtle) {
        this.turtle = turtle;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
        this.lastPosition = turtle.getPos();
    }
    
    @Override
    public boolean canStart() {
        // Only start if turtle is in MOVING state and has a destination
        return turtle.getCurrentState() == AethelonEntity.AethelonState.MOVING &&
               turtle.isAlive() &&
               hasValidDestination();
    }
    
    @Override
    public boolean shouldContinue() {
        // Continue while in moving state and not at destination
        return turtle.getCurrentState() == AethelonEntity.AethelonState.MOVING &&
               turtle.isAlive() &&
               hasValidDestination() &&
               !hasReachedDestination() &&
               stuckTimer < STUCK_THRESHOLD;
    }
    
    @Override
    public void start() {
        // Initialize pathfinding
        pathfindingTimer = 0;
        movementTimer = 0;
        stuckTimer = 0;
        lastPosition = turtle.getPos();
        
        // Get destination from state machine
        // TODO: Get from AethelonStateMachine
        targetDestination = generateTemporaryDestination();
        
        // Calculate initial path
        calculatePath();
    }
    
    @Override
    public void tick() {
        pathfindingTimer++;
        movementTimer++;
        
        // Recalculate path periodically
        if (pathfindingTimer >= PATHFINDING_INTERVAL) {
            calculatePath();
            pathfindingTimer = 0;
        }
        
        // Execute movement
        executeMovement();
        
        // Check if stuck
        checkIfStuck();
        
        // Update entities on shell
        updateShellEntities();
    }
    
    @Override
    public void stop() {
        // Clean up pathfinding
        turtle.getNavigation().stop();
        currentPath = null;
        targetDestination = null;
    }
    
    /**
     * Calculate path to destination with ocean-specific constraints
     */
    private void calculatePath() {
        if (targetDestination == null) return;
        
        // Validate destination is in deep water
        if (!isValidOceanDestination(targetDestination)) {
            // Find alternative destination
            targetDestination = findNearbyDeepWater(targetDestination);
            if (targetDestination == null) {
                // No valid destination found, stop pathfinding
                turtle.setState(AethelonEntity.AethelonState.TRANSITIONING);
                return;
            }
        }
        
        // Use Minecraft's pathfinding with custom constraints
        BlockPos targetPos = BlockPos.ofFloored(targetDestination);
        currentPath = turtle.getNavigation().findPathTo(targetPos, 1);
        
        if (currentPath != null) {
            turtle.getNavigation().startMovingAlong(currentPath, getMovementSpeed());
        } else {
            // Pathfinding failed, try direct movement
            attemptDirectMovement();
        }
    }
    
    /**
     * Execute movement towards destination
     */
    private void executeMovement() {
        if (targetDestination == null) return;
        
        Vec3d currentPos = turtle.getPos();
        Vec3d direction = targetDestination.subtract(currentPos).normalize();
        
        // Apply movement with momentum and realistic constraints
        double actualSpeed = calculateMovementSpeed();
        Vec3d movement = direction.multiply(actualSpeed);
        
        // Apply movement with water resistance
        Vec3d currentVelocity = turtle.getVelocity();
        Vec3d newVelocity = currentVelocity.multiply(0.8).add(movement.multiply(0.2));
        
        // Ensure we don't move too fast
        double maxSpeed = getConfiguredMovementSpeed();
        if (newVelocity.horizontalLength() > maxSpeed) {
            newVelocity = newVelocity.normalize().multiply(maxSpeed);
            newVelocity = new Vec3d(newVelocity.x, currentVelocity.y, newVelocity.z);
        }
        
        turtle.setVelocity(newVelocity);
        
        // Update turtle rotation to face movement direction
        updateTurtleRotation(direction);
    }
    
    /**
     * Calculate appropriate movement speed based on conditions
     */
    private double calculateMovementSpeed() {
        double baseSpeed = getConfiguredMovementSpeed();
        
        // Reduce speed in shallow water
        if (!isInDeepWater(turtle.getPos())) {
            baseSpeed *= 0.5;
        }
        
        // Reduce speed when turning
        Vec3d currentDirection = Vec3d.fromPolar(0, turtle.getYaw());
        Vec3d targetDirection = targetDestination.subtract(turtle.getPos()).normalize();
        double alignment = currentDirection.dotProduct(targetDirection);
        if (alignment < 0.8) { // Turning
            baseSpeed *= 0.7;
        }
        
        // Reduce speed based on entities on shell
        // TODO: Check number of entities on shell and adjust speed
        
        return baseSpeed;
    }
    
    /**
     * Update turtle rotation to face movement direction
     */
    private void updateTurtleRotation(Vec3d direction) {
        if (direction.horizontalLength() > 0.01) {
            float targetYaw = (float) Math.toDegrees(Math.atan2(-direction.x, direction.z));
            float currentYaw = turtle.getYaw();
            
            // Smooth rotation for massive turtle
            float yawDifference = targetYaw - currentYaw;
            while (yawDifference > 180) yawDifference -= 360;
            while (yawDifference < -180) yawDifference += 360;
            
            // Limit rotation speed for realism
            float maxRotationSpeed = 2.0f; // degrees per tick
            yawDifference = Math.max(-maxRotationSpeed, Math.min(maxRotationSpeed, yawDifference));
            
            turtle.setYaw(currentYaw + yawDifference);
            turtle.setHeadYaw(turtle.getYaw());
        }
    }
    
    /**
     * Check if turtle is stuck and handle accordingly
     */
    private void checkIfStuck() {
        Vec3d currentPos = turtle.getPos();
        double distanceMoved = currentPos.distanceTo(lastPosition);
        
        if (distanceMoved < 0.1) { // Very little movement
            stuckTimer++;
            
            if (stuckTimer >= STUCK_THRESHOLD) {
                // Turtle is stuck, try to find alternative path
                handleStuckSituation();
            }
        } else {
            stuckTimer = 0;
            lastPosition = currentPos;
        }
    }
    
    /**
     * Handle situation when turtle gets stuck
     */
    private void handleStuckSituation() {
        // Try to find alternative destination
        Vec3d alternativeDestination = findAlternativeDestination();
        if (alternativeDestination != null) {
            targetDestination = alternativeDestination;
            stuckTimer = 0;
            calculatePath();
        } else {
            // No alternative found, transition back to idle
            turtle.setState(AethelonEntity.AethelonState.TRANSITIONING);
        }
    }
    
    /**
     * Update entities standing on turtle shell during movement
     */
    private void updateShellEntities() {
        // TODO: Implement shell entity updates
        // - Move entities with turtle
        // - Handle entities falling off
        // - Maintain stable platform
        // - Apply movement momentum to entities
        
        // This will be handled by the collision mixin for now
        // But we might need additional logic for smooth movement
    }
    
    /**
     * Attempt direct movement when pathfinding fails
     */
    private void attemptDirectMovement() {
        // Simple direct movement towards destination
        Vec3d currentPos = turtle.getPos();
        Vec3d direction = targetDestination.subtract(currentPos).normalize();
        
        // Check if direct path is clear (no major obstacles)
        if (isDirectPathClear(currentPos, targetDestination)) {
            Vec3d movement = direction.multiply(getMovementSpeed());
            turtle.setVelocity(movement);
        } else {
            // Direct path blocked, give up and transition
            turtle.setState(AethelonEntity.AethelonState.TRANSITIONING);
        }
    }
    
    // Helper methods
    private boolean hasValidDestination() {
        return targetDestination != null;
    }
    
    private boolean hasReachedDestination() {
        return targetDestination != null && 
               turtle.getPos().distanceTo(targetDestination) < ARRIVAL_DISTANCE;
    }
    
    private boolean isValidOceanDestination(Vec3d destination) {
        return isInDeepWater(destination) && 
               isInOceanBiome(destination);
    }
    
    private boolean isInDeepWater(Vec3d position) {
        World world = turtle.getWorld();
        BlockPos pos = BlockPos.ofFloored(position);
        
        // Check water depth
        for (int i = 0; i < MIN_WATER_DEPTH; i++) {
            if (!world.getBlockState(pos.down(i)).getFluidState().isIn(net.minecraft.registry.tag.FluidTags.WATER)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean isInOceanBiome(Vec3d position) {
        World world = turtle.getWorld();
        BlockPos pos = BlockPos.ofFloored(position);
        return world.getBiome(pos).isIn(net.minecraft.registry.tag.BiomeTags.IS_OCEAN);
    }
    
    private boolean isDirectPathClear(Vec3d start, Vec3d end) {
        // TODO: Implement path clearance check
        // - Check for major obstacles
        // - Verify water depth along path
        // - Check for terrain that would block massive turtle
        return true; // Placeholder
    }
    
    private Vec3d findNearbyDeepWater(Vec3d center) {
        // TODO: Implement deep water search
        // - Search in expanding radius
        // - Prioritize ocean biomes
        // - Ensure minimum water depth
        return null; // Placeholder
    }
    
    private Vec3d findAlternativeDestination() {
        // TODO: Implement alternative destination finding
        // - Try different directions
        // - Avoid previous stuck locations
        // - Prefer coastal areas
        return null; // Placeholder
    }
    
    private double getConfiguredMovementSpeed() {
        return AethelonConfig.INSTANCE != null ? 
            AethelonConfig.INSTANCE.movement_speed : getMovementSpeed();
    }
    
    private Vec3d generateTemporaryDestination() {
        // Temporary destination generation for testing
        Vec3d currentPos = turtle.getPos();
        double angle = turtle.getRandom().nextDouble() * 2 * Math.PI;
        double distance = 100 + turtle.getRandom().nextDouble() * 200;
        
        return new Vec3d(
            currentPos.x + Math.cos(angle) * distance,
            currentPos.y,
            currentPos.z + Math.sin(angle) * distance
        );
    }
}