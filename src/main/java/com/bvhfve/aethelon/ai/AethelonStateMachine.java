package com.bvhfve.aethelon.ai;

import com.bvhfve.aethelon.entity.AethelonEntity;
import com.bvhfve.aethelon.config.AethelonConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * State machine for managing Aethelon turtle behavior
 * Handles transitions between IDLE, MOVING, TRANSITIONING, and DAMAGED states
 * 
 * Based on Phase 2 TODO requirements for AI behavior management
 */
public class AethelonStateMachine {
    
    private final AethelonEntity turtle;
    private AethelonEntity.AethelonState currentState;
    private AethelonEntity.AethelonState previousState;
    private int stateTimer;
    private int idleTimeRemaining;
    private Vec3d targetDestination;
    private boolean damageTriggered;
    
    // Configuration-driven timing
    private static final int MIN_IDLE_TIME = 20 * 60 * 20; // 20 minutes in ticks
    private static final int MAX_IDLE_TIME = 20 * 60 * 60; // 60 minutes in ticks
    private static final int TRANSITION_TIME = 20 * 10; // 10 seconds
    private static final int DAMAGE_RESPONSE_TIME = 20 * 3; // 3 seconds (shorter)
    private static final int MIN_MOVEMENT_TIME = 20 * 30; // Minimum 30 seconds of movement
    
    public AethelonStateMachine(AethelonEntity turtle) {
        this.turtle = turtle;
        this.currentState = AethelonEntity.AethelonState.IDLE;
        this.previousState = AethelonEntity.AethelonState.IDLE;
        this.stateTimer = 0;
        this.idleTimeRemaining = generateRandomIdleTime();
        this.damageTriggered = false;
    }
    
    /**
     * Main state machine update - called every tick
     */
    public void tick() {
        stateTimer++;
        
        // Debug: Print state information every 5 seconds
        if (stateTimer % (20 * 5) == 0) {
            System.out.println("Aethelon State: " + currentState + " (timer: " + stateTimer + ", idle remaining: " + idleTimeRemaining + ")");
        }
        
        // Check for damage-triggered state changes
        if (damageTriggered && currentState == AethelonEntity.AethelonState.IDLE) {
            System.out.println("Damage triggered! Transitioning to DAMAGED state");
            transitionToState(AethelonEntity.AethelonState.DAMAGED);
            damageTriggered = false;
            return;
        }
        
        // State-specific logic
        switch (currentState) {
            case IDLE -> tickIdleState();
            case MOVING -> tickMovingState();
            case TRANSITIONING -> tickTransitioningState();
            case DAMAGED -> tickDamagedState();
        }
    }
    
    /**
     * IDLE state: Turtle is stationary, occasionally checking for movement triggers
     */
    private void tickIdleState() {
        idleTimeRemaining--;
        
        // Check if idle time is up
        if (idleTimeRemaining <= 0) {
            // Start transition to moving
            transitionToState(AethelonEntity.AethelonState.TRANSITIONING);
            return;
        }
        
        // Occasional player proximity checks (every 5 seconds)
        if (stateTimer % (20 * 5) == 0) {
            checkPlayerProximity();
        }
        
        // TODO: Add subtle idle animations
        // - Slow head movement
        // - Breathing effects
        // - Occasional shell adjustment
    }
    
    /**
     * MOVING state: Turtle is actively traveling to destination
     */
    private void tickMovingState() {
        // Ensure minimum movement time before allowing transition
        if (stateTimer < MIN_MOVEMENT_TIME) {
            System.out.println("Moving state - time remaining: " + (MIN_MOVEMENT_TIME - stateTimer) + " ticks");
            return; // Don't check destination until minimum time passes
        }
        
        // Check if we've reached destination
        if (targetDestination != null) {
            double distanceToTarget = turtle.getPos().distanceTo(targetDestination);
            System.out.println("Distance to target: " + distanceToTarget + " blocks");
            
            // If close to destination OR moved for long enough, start transitioning back to idle
            if (distanceToTarget < 20.0 || stateTimer > 20 * 120) { // 20 blocks or 2 minutes max
                System.out.println("Reached destination or timeout, transitioning to idle");
                transitionToState(AethelonEntity.AethelonState.TRANSITIONING);
                return;
            }
        }
        
        // Force movement towards destination using multiple methods
        if (targetDestination != null) {
            // Method 1: Use navigation system (configurable speed)
            double navSpeed = AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.navigation_speed : 0.75;
            turtle.getNavigation().startMovingTo(targetDestination.x, targetDestination.y, targetDestination.z, navSpeed);
            
            // Method 2: Direct velocity application as backup
            Vec3d currentPos = turtle.getPos();
            Vec3d direction = targetDestination.subtract(currentPos).normalize();
            double speed = AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.escape_movement_speed : 0.75;
            
            // Apply direct velocity
            Vec3d velocity = direction.multiply(speed);
            turtle.setVelocity(velocity.x, turtle.getVelocity().y, velocity.z);
            
            // Update rotation
            if (velocity.horizontalLengthSquared() > 0.001) {
                float targetYaw = (float)(Math.atan2(-velocity.x, velocity.z) * 180.0 / Math.PI);
                turtle.setYaw(targetYaw);
                turtle.headYaw = targetYaw;
            }
            
            System.out.println("Applied velocity: " + velocity + " towards " + targetDestination);
        }
    }
    
    /**
     * TRANSITIONING state: Preparing to change between idle and moving
     */
    private void tickTransitioningState() {
        // Transition duration complete
        if (stateTimer >= TRANSITION_TIME) {
            if (previousState == AethelonEntity.AethelonState.IDLE) {
                // Transition from idle to moving
                selectDestination();
                transitionToState(AethelonEntity.AethelonState.MOVING);
            } else {
                // Transition from moving to idle
                idleTimeRemaining = generateRandomIdleTime();
                transitionToState(AethelonEntity.AethelonState.IDLE);
            }
        }
        
        // TODO: Add transition effects
        // - Sound effects
        // - Particle effects
        // - Animation changes
    }
    
    /**
     * DAMAGED state: Responding to player damage
     */
    private void tickDamagedState() {
        // Damage response duration complete
        if (stateTimer >= DAMAGE_RESPONSE_TIME) {
            // Start moving away from damage source
            selectEscapeDestination();
            System.out.println("Damage response complete, starting escape movement");
            transitionToState(AethelonEntity.AethelonState.MOVING); // Go directly to MOVING
        }
        
        // Show agitation during damage response
        if (stateTimer % 10 == 0) {
            System.out.println("Turtle is agitated! Time remaining: " + (DAMAGE_RESPONSE_TIME - stateTimer));
        }
    }
    
    /**
     * Transition to a new state with proper cleanup
     */
    private void transitionToState(AethelonEntity.AethelonState newState) {
        if (currentState != newState) {
            System.out.println("State transition: " + currentState + " -> " + newState);
            previousState = currentState;
            currentState = newState;
            stateTimer = 0;
            
            // Update entity state
            turtle.setState(newState);
            
            // State-specific initialization
            switch (newState) {
                case IDLE -> onEnterIdle();
                case MOVING -> onEnterMoving();
                case TRANSITIONING -> onEnterTransitioning();
                case DAMAGED -> onEnterDamaged();
            }
        }
    }
    
    /**
     * Generate random idle time based on configuration
     */
    private int generateRandomIdleTime() {
        int minTime = AethelonConfig.INSTANCE != null ? 
            (int)(AethelonConfig.INSTANCE.min_idle_time * 20 * 60) : MIN_IDLE_TIME;
        int maxTime = AethelonConfig.INSTANCE != null ? 
            (int)(AethelonConfig.INSTANCE.max_idle_time * 20 * 60) : MAX_IDLE_TIME;
        
        // Ensure maxTime > minTime
        if (maxTime <= minTime) {
            maxTime = minTime + 1200; // Add 1 minute minimum
        }
        
        int idleTime = minTime + turtle.getRandom().nextInt(maxTime - minTime);
        System.out.println("Generated idle time: " + idleTime + " ticks (" + (idleTime / 1200.0) + " minutes)");
        return idleTime;
    }
    
    /**
     * Select a random destination for movement
     */
    private void selectDestination() {
        // TODO: Implement intelligent destination selection
        // - Find deep ocean areas
        // - Avoid shallow water
        // - Consider coastal proximity
        // - Respect world boundaries
        
        // Placeholder: Random direction, 100-500 blocks away
        double angle = turtle.getRandom().nextDouble() * 2 * Math.PI;
        double distance = 100 + turtle.getRandom().nextDouble() * 400;
        
        Vec3d currentPos = turtle.getPos();
        targetDestination = new Vec3d(
            currentPos.x + Math.cos(angle) * distance,
            currentPos.y, // Keep same Y level for now
            currentPos.z + Math.sin(angle) * distance
        );
    }
    
    /**
     * Select escape destination when damaged - head towards deep ocean
     */
    private void selectEscapeDestination() {
        Vec3d turtlePos = turtle.getPos();
        PlayerEntity nearestPlayer = turtle.getWorld().getClosestPlayer(turtle, 100.0);
        
        if (nearestPlayer != null) {
            Vec3d playerPos = nearestPlayer.getPos();
            Vec3d escapeDirection = turtlePos.subtract(playerPos).normalize();
            
            // Find the direction towards deeper water (away from land)
            Vec3d deepWaterDirection = findDeepWaterDirection(turtlePos);
            
            // Combine escape direction with deep water direction
            Vec3d combinedDirection = escapeDirection.add(deepWaterDirection).normalize();
            
            // Move 300-500 blocks away towards deep water
            double escapeDistance = 300 + turtle.getRandom().nextDouble() * 200;
            targetDestination = turtlePos.add(combinedDirection.multiply(escapeDistance));
            
            System.out.println("Escape destination selected: " + targetDestination + " (distance: " + escapeDistance + ")");
        } else {
            // No player found, just head to deep water
            Vec3d deepWaterDirection = findDeepWaterDirection(turtlePos);
            targetDestination = turtlePos.add(deepWaterDirection.multiply(400));
        }
    }
    
    /**
     * Find direction towards deeper water (away from land)
     */
    private Vec3d findDeepWaterDirection(Vec3d currentPos) {
        // Sample water depth in 8 directions around turtle
        Vec3d bestDirection = Vec3d.ZERO;
        double bestDepth = 0;
        
        for (int angle = 0; angle < 360; angle += 45) {
            double radians = Math.toRadians(angle);
            Vec3d direction = new Vec3d(Math.cos(radians), 0, Math.sin(radians));
            Vec3d samplePos = currentPos.add(direction.multiply(50)); // Sample 50 blocks away
            
            double depth = getWaterDepthAt(samplePos);
            if (depth > bestDepth) {
                bestDepth = depth;
                bestDirection = direction;
            }
        }
        
        // If no good direction found, default to moving away from spawn (towards ocean)
        if (bestDirection.equals(Vec3d.ZERO)) {
            Vec3d spawnPos = new Vec3d(0, currentPos.y, 0); // Assume spawn is at 0,0
            bestDirection = currentPos.subtract(spawnPos).normalize();
        }
        
        return bestDirection;
    }
    
    /**
     * Get water depth at a position
     */
    private double getWaterDepthAt(Vec3d pos) {
        int depth = 0;
        int startY = (int)pos.y;
        
        for (int y = startY; y > startY - 20 && y > turtle.getWorld().getBottomY(); y--) {
            if (turtle.getWorld().getBlockState(new net.minecraft.util.math.BlockPos((int)pos.x, y, (int)pos.z))
                .getFluidState().isIn(net.minecraft.registry.tag.FluidTags.WATER)) {
                depth++;
            } else {
                break;
            }
        }
        
        return depth;
    }
    
    /**
     * Check for nearby players and react accordingly
     */
    private void checkPlayerProximity() {
        PlayerEntity nearestPlayer = turtle.getWorld().getClosestPlayer(turtle, 64.0);
        if (nearestPlayer != null) {
            // TODO: Add player interaction logic
            // - Curious behavior when players approach
            // - Defensive behavior if attacked recently
            // - Special behavior for players on shell
        }
    }
    
    // State entry methods
    private void onEnterIdle() {
        // TODO: Initialize idle state
        // - Stop movement
        // - Reset pathfinding
        // - Start idle animations
    }
    
    private void onEnterMoving() {
        // TODO: Initialize moving state
        // - Start movement sounds
        // - Begin pathfinding
        // - Alert entities on shell
    }
    
    private void onEnterTransitioning() {
        // TODO: Initialize transition state
        // - Play transition sounds
        // - Start transition effects
        // - Prepare for state change
    }
    
    private void onEnterDamaged() {
        // TODO: Initialize damaged state
        // - Play damage response sounds
        // - Show damage effects
        // - Alert entities on shell
    }
    
    // Public interface methods
    public AethelonEntity.AethelonState getCurrentState() {
        return currentState;
    }
    
    public int getStateTimer() {
        return stateTimer;
    }
    
    public int getIdleTimeRemaining() {
        return idleTimeRemaining;
    }
    
    public Vec3d getTargetDestination() {
        return targetDestination;
    }
    
    public void triggerDamageResponse() {
        this.damageTriggered = true;
    }
    
    public boolean isMoving() {
        return currentState == AethelonEntity.AethelonState.MOVING;
    }
    
    public boolean isIdle() {
        return currentState == AethelonEntity.AethelonState.IDLE;
    }
}