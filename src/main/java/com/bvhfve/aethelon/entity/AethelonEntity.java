package com.bvhfve.aethelon.entity;

import com.bvhfve.aethelon.config.AethelonConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

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
     * Uses configurable values for health, movement speed, and scale
     */
    public static DefaultAttributeContainer.Builder createAethelonAttributes() {
        return WaterCreatureEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.health : 2000.0)
                .add(EntityAttributes.MOVEMENT_SPEED, AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.movement_speed : 0.05)
                .add(EntityAttributes.FOLLOW_RANGE, AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.follow_range : 128.0)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.knockback_resistance : 1.0)
                .add(EntityAttributes.SCALE, AethelonConfig.getTurtleSizeScale()); // Configurable size scale
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
    
    /**
     * Custom spawn conditions for Aethelon entities
     * Based on patterns from knowledge pool examples
     * Uses configurable values for water depth, clearance, and spawn rarity
     * 
     * @param type The entity type
     * @param world The world access
     * @param spawnReason The spawn reason
     * @param pos The spawn position
     * @param random Random instance
     * @return true if the entity can spawn at this location
     */
    public static boolean canSpawn(EntityType<? extends WaterCreatureEntity> type, WorldAccess world, 
                                  SpawnReason spawnReason, BlockPos pos, Random random) {
        // Get configurable water depth requirement
        int waterDepthRequired = AethelonConfig.getWaterDepthRequired();
        
        // Must be in very deep water (configurable depth for massive turtle)
        for (int i = 0; i < waterDepthRequired; i++) {
            if (!world.getBlockState(pos.down(i)).getFluidState().isIn(net.minecraft.registry.tag.FluidTags.WATER)) {
                return false;
            }
        }
        
        // Get configurable clearance above requirement
        int clearanceRequired = AethelonConfig.getClearanceAboveRequired();
        
        // Must have clear space above for shell/island (configurable clearance)
        for (int i = 1; i <= clearanceRequired; i++) {
            if (!world.getBlockState(pos.up(i)).isAir() && 
                !world.getBlockState(pos.up(i)).getFluidState().isIn(net.minecraft.registry.tag.FluidTags.WATER)) {
                return false;
            }
        }
        
        // Check world conditions
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        
        // Must be in ocean biome
        if (!world.getBiome(pos).isIn(BiomeTags.IS_OCEAN)) {
            return false;
        }
        
        // Low light level for rarity (deep ocean spawning)
        if (world.getLightLevel(pos) > 7) {
            return false;
        }
        
        // Configurable rarity check
        float spawnRarity = AethelonConfig.getSpawnRarity();
        return random.nextFloat() < spawnRarity;
    }
}