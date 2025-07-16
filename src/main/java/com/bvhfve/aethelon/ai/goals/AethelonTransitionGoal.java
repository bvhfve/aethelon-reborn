package com.bvhfve.aethelon.ai.goals;

import com.bvhfve.aethelon.entity.AethelonEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import java.util.EnumSet;

/**
 * AI Goal for Aethelon state transitions
 * Handles smooth transitions between idle and moving states with effects
 * 
 * Based on Phase 2 requirements for polished state changes
 */
public class AethelonTransitionGoal extends Goal {
    
    private final AethelonEntity turtle;
    private int transitionTimer;
    private boolean effectsTriggered;
    private AethelonEntity.AethelonState transitioningFrom;
    private AethelonEntity.AethelonState transitioningTo;
    
    // Transition timing
    private static final int TRANSITION_DURATION = 20 * 8; // 8 seconds
    private static final int EFFECTS_TRIGGER_TIME = 20 * 2; // 2 seconds in
    
    public AethelonTransitionGoal(AethelonEntity turtle) {
        this.turtle = turtle;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }
    
    @Override
    public boolean canStart() {
        return turtle.getCurrentState() == AethelonEntity.AethelonState.TRANSITIONING;
    }
    
    @Override
    public boolean shouldContinue() {
        return turtle.getCurrentState() == AethelonEntity.AethelonState.TRANSITIONING &&
               transitionTimer < TRANSITION_DURATION;
    }
    
    @Override
    public void start() {
        transitionTimer = 0;
        effectsTriggered = false;
        
        // Determine what we're transitioning from/to
        determineTransitionType();
        
        // Stop any current movement
        turtle.getNavigation().stop();
        
        // Play initial transition sound
        playTransitionSound(true);
    }
    
    @Override
    public void tick() {
        transitionTimer++;
        
        // Trigger effects partway through transition
        if (!effectsTriggered && transitionTimer >= EFFECTS_TRIGGER_TIME) {
            triggerTransitionEffects();
            effectsTriggered = true;
        }
        
        // Apply transition-specific behavior
        switch (transitioningFrom) {
            case IDLE -> tickIdleToMovingTransition();
            case MOVING -> tickMovingToIdleTransition();
            case DAMAGED -> tickDamagedTransition();
        }
        
        // Gradual movement changes
        applyTransitionMovement();
    }
    
    @Override
    public void stop() {
        // Play completion sound
        playTransitionSound(false);
        
        // Clean up
        transitionTimer = 0;
        effectsTriggered = false;
    }
    
    /**
     * Determine what type of transition this is
     */
    private void determineTransitionType() {
        // Get previous state from state machine
        if (turtle.getStateMachine() != null) {
            // For now, assume idle <-> moving transitions
            // TODO: Get actual previous state from state machine
            transitioningFrom = AethelonEntity.AethelonState.IDLE;
            transitioningTo = AethelonEntity.AethelonState.MOVING;
        }
    }
    
    /**
     * Handle idle to moving transition
     */
    private void tickIdleToMovingTransition() {
        // Gradual awakening behavior
        float progress = (float) transitionTimer / TRANSITION_DURATION;
        
        // Increase activity level
        if (transitionTimer % 20 == 0) { // Every second
            // Head movements become more frequent
            performAwakeningMovement(progress);
        }
        
        // Start generating movement particles
        if (progress > 0.5f) {
            generateMovementParticles();
        }
    }
    
    /**
     * Handle moving to idle transition
     */
    private void tickMovingToIdleTransition() {
        // Gradual settling behavior
        float progress = (float) transitionTimer / TRANSITION_DURATION;
        
        // Slow down movement
        Vec3d currentVelocity = turtle.getVelocity();
        Vec3d reducedVelocity = currentVelocity.multiply(1.0 - progress * 0.1);
        turtle.setVelocity(reducedVelocity);
        
        // Settling effects
        if (progress > 0.7f) {
            generateSettlingParticles();
        }
    }
    
    /**
     * Handle damaged state transition
     */
    private void tickDamagedTransition() {
        // Agitated behavior
        if (transitionTimer % 10 == 0) { // Every 0.5 seconds
            performAgitatedMovement();
        }
        
        // Damage response particles
        generateDamageParticles();
    }
    
    /**
     * Apply movement during transition
     */
    private void applyTransitionMovement() {
        float progress = (float) transitionTimer / TRANSITION_DURATION;
        
        // Subtle vertical movement (breathing/settling)
        if (turtle.isTouchingWater()) {
            double verticalOffset = Math.sin(transitionTimer * 0.1) * 0.02;
            turtle.addVelocity(0, verticalOffset, 0);
        }
        
        // Gentle swaying motion
        double swayAmount = 0.01 * (1.0 - progress); // Decreases over time
        double sway = Math.sin(transitionTimer * 0.2) * swayAmount;
        turtle.addVelocity(sway, 0, 0);
    }
    
    /**
     * Trigger visual and audio effects for transition
     */
    private void triggerTransitionEffects() {
        if (turtle.getWorld().isClient) return; // Server-side only
        
        Vec3d pos = turtle.getPos();
        
        // Bubble particles around turtle
        for (int i = 0; i < 20; i++) {
            double offsetX = (turtle.getRandom().nextDouble() - 0.5) * 32;
            double offsetY = turtle.getRandom().nextDouble() * 8;
            double offsetZ = (turtle.getRandom().nextDouble() - 0.5) * 28;
            
            turtle.getWorld().addParticle(
                ParticleTypes.BUBBLE,
                pos.x + offsetX, pos.y + offsetY, pos.z + offsetZ,
                0, 0.1, 0
            );
        }
        
        // Water splash particles
        for (int i = 0; i < 10; i++) {
            double offsetX = (turtle.getRandom().nextDouble() - 0.5) * 40;
            double offsetZ = (turtle.getRandom().nextDouble() - 0.5) * 35;
            
            turtle.getWorld().addParticle(
                ParticleTypes.SPLASH,
                pos.x + offsetX, pos.y + 8, pos.z + offsetZ,
                0, 0.2, 0
            );
        }
    }
    
    /**
     * Perform awakening head movement
     */
    private void performAwakeningMovement(float progress) {
        // More dramatic head movements as turtle awakens
        float currentYaw = turtle.getYaw();
        float movementAmount = 20.0f * progress; // Increases with progress
        float newYaw = currentYaw + (turtle.getRandom().nextFloat() - 0.5f) * movementAmount;
        
        turtle.setYaw(newYaw);
        turtle.headYaw = newYaw;
    }
    
    /**
     * Perform agitated movement when damaged
     */
    private void performAgitatedMovement() {
        // Quick, jerky movements
        float currentYaw = turtle.getYaw();
        float agitatedMovement = (turtle.getRandom().nextFloat() - 0.5f) * 45.0f;
        
        turtle.setYaw(currentYaw + agitatedMovement);
        turtle.headYaw = currentYaw + agitatedMovement;
        
        // Small random velocity changes
        double velocityChange = 0.05;
        turtle.addVelocity(
            (turtle.getRandom().nextDouble() - 0.5) * velocityChange,
            0,
            (turtle.getRandom().nextDouble() - 0.5) * velocityChange
        );
    }
    
    /**
     * Generate particles for movement preparation
     */
    private void generateMovementParticles() {
        if (turtle.getWorld().isClient) return;
        
        Vec3d pos = turtle.getPos();
        
        // Increased bubble activity
        if (turtle.getRandom().nextFloat() < 0.3f) {
            turtle.getWorld().addParticle(
                ParticleTypes.BUBBLE_COLUMN_UP,
                pos.x + (turtle.getRandom().nextDouble() - 0.5) * 20,
                pos.y,
                pos.z + (turtle.getRandom().nextDouble() - 0.5) * 20,
                0, 0.1, 0
            );
        }
    }
    
    /**
     * Generate particles for settling down
     */
    private void generateSettlingParticles() {
        if (turtle.getWorld().isClient) return;
        
        Vec3d pos = turtle.getPos();
        
        // Settling dust/sand particles
        if (turtle.getRandom().nextFloat() < 0.2f) {
            turtle.getWorld().addParticle(
                ParticleTypes.UNDERWATER,
                pos.x + (turtle.getRandom().nextDouble() - 0.5) * 30,
                pos.y - 2,
                pos.z + (turtle.getRandom().nextDouble() - 0.5) * 25,
                0, -0.05, 0
            );
        }
    }
    
    /**
     * Generate particles for damage response
     */
    private void generateDamageParticles() {
        if (turtle.getWorld().isClient) return;
        
        Vec3d pos = turtle.getPos();
        
        // Angry bubble particles
        if (turtle.getRandom().nextFloat() < 0.4f) {
            turtle.getWorld().addParticle(
                ParticleTypes.ANGRY_VILLAGER,
                pos.x + (turtle.getRandom().nextDouble() - 0.5) * 15,
                pos.y + 8 + turtle.getRandom().nextDouble() * 5,
                pos.z + (turtle.getRandom().nextDouble() - 0.5) * 15,
                0, 0.1, 0
            );
        }
    }
    
    /**
     * Play transition sounds
     */
    private void playTransitionSound(boolean starting) {
        if (turtle.getWorld().isClient) return;
        
        // TODO: Add custom turtle sounds
        // For now, use existing sounds
        if (starting) {
            turtle.getWorld().playSound(
                null, turtle.getBlockPos(),
                SoundEvents.ENTITY_TURTLE_AMBIENT_LAND,
                SoundCategory.NEUTRAL,
                2.0f, 0.8f
            );
        } else {
            turtle.getWorld().playSound(
                null, turtle.getBlockPos(),
                SoundEvents.ENTITY_TURTLE_AMBIENT_LAND,
                SoundCategory.NEUTRAL,
                1.5f, 1.2f
            );
        }
    }
}