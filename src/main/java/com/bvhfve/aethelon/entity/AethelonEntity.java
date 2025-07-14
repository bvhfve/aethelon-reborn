package com.bvhfve.aethelon.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.world.World;

/**
 * The Aethelon Entity - A colossal world turtle that carries islands on its back
 * 
 * This entity represents the core of the mod: a massive, passive turtle that
 * moves slowly through ocean biomes while carrying a functional island structure.
 */
public class AethelonEntity extends WaterCreatureEntity {
    
    // Entity states for behavior management
    public enum AethelonState {
        IDLE,           // Stationary, players can interact with island
        MOVING,         // Actively traveling to new location
        TRANSITIONING,  // Preparing to move or settling at destination
        DAMAGED         // Responding to player damage
    }
    
    private AethelonState currentState = AethelonState.IDLE;
    private int stateTimer = 0;
    
    public AethelonEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
    }
    
    /**
     * Creates the default attributes for Aethelon entities
     * Based on patterns from knowledge pool entity examples
     */
    public static DefaultAttributeContainer.Builder createAethelonAttributes() {
        return WaterCreatureEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 1000.0) // Massive health pool
                .add(EntityAttributes.MOVEMENT_SPEED, 0.1) // Very slow movement
                .add(EntityAttributes.FOLLOW_RANGE, 64.0)  // Large detection range
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 1.0); // Immune to knockback
    }
    
    @Override
    protected void initGoals() {
        super.initGoals();
        // TODO: Phase 2 - Add custom AI goals
        // this.goalSelector.add(1, new AethelonIdleGoal(this));
        // this.goalSelector.add(2, new AethelonPathfindGoal(this));
        // this.goalSelector.add(3, new AethelonTransitionGoal(this));
    }
    
    @Override
    public void tick() {
        super.tick();
        
        // Basic state management
        stateTimer++;
        
        // TODO: Phase 2 - Implement state machine logic
        // TODO: Phase 4 - Update island position
        // TODO: Phase 5 - Handle island movement
    }
    
    // Getters and setters for state management
    public AethelonState getCurrentState() {
        return currentState;
    }
    
    public void setState(AethelonState newState) {
        if (this.currentState != newState) {
            this.currentState = newState;
            this.stateTimer = 0;
            // TODO: Add state transition effects
        }
    }
    
    public int getStateTimer() {
        return stateTimer;
    }
}