package com.bvhfve.aethelon.entity;

import com.bvhfve.aethelon.config.AethelonConfig;
import com.bvhfve.aethelon.util.PerformanceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import java.util.List;
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
    
    // Performance optimization fields
    private static final int TICK_RATE_DIVIDER = 4; // Only process every 4th tick
    private int tickCounter = 0;
    private boolean isNearPlayer = false;
    private int nearPlayerCheckTimer = 0;
    private static final int NEAR_PLAYER_CHECK_INTERVAL = 40; // Check every 2 seconds
    
    public AethelonEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
    }
    
    /**
     * Creates the default attributes for Aethelon entities
     * PERFORMANCE CRITICAL: Reduced values for better performance
     */
    public static DefaultAttributeContainer.Builder createAethelonAttributes() {
        return WaterCreatureEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.health : 2000.0) // Increased from 1000 to 2000
                .add(EntityAttributes.MOVEMENT_SPEED, AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.movement_speed : 0.02) // Reduced from 0.05
                .add(EntityAttributes.FOLLOW_RANGE, 32.0) // Drastically reduced from 128.0
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.knockback_resistance : 1.0)
                .add(EntityAttributes.SCALE, Math.min(AethelonConfig.getTurtleSizeScale(), 2.0f)); // Capped at 2x for performance
    }
    
    public boolean canTakeDamage() {
        // Allow damage but prevent suffocation
        return true;
    }
    
    @Override
    public boolean damage(net.minecraft.server.world.ServerWorld world, net.minecraft.entity.damage.DamageSource source, float amount) {
        // Prevent suffocation damage - world turtles are too large to suffocate
        if (source.isOf(net.minecraft.entity.damage.DamageTypes.IN_WALL) || 
            source.isOf(net.minecraft.entity.damage.DamageTypes.CRAMMING)) {
            return false; // Ignore wall/cramming damage
        }
        
        // Allow other types of damage
        return super.damage(world, source, amount);
    }
    
    protected boolean isInWall() {
        // World turtles are too large to be considered "in wall"
        // This prevents suffocation checks entirely
        return false;
    }
    
    public boolean canAvoidTraps() {
        // World turtles can't be trapped by normal means
        return true;
    }
    
    public boolean cannotDespawn() {
        // World turtles should never despawn - they're too important
        return true;
    }
    
    // Note: canBreatheInWater() is final in parent class, can't override
    // Turtle inherits water breathing from WaterCreatureEntity
    
    public boolean canWalkOnPowderedSnow() {
        // Large turtles don't sink into powder snow
        return true;
    }
    
    protected boolean canClimb() {
        // World turtles don't climb
        return false;
    }
    
    // ========== COLLISION SYSTEM FOR STANDING ON TURTLE ==========
    // Now handled by AethelonEntityCollisionMixin for better integration
    
    @Override
    public void pushAwayFrom(Entity entity) {
        // Don't push entities away - this would prevent standing on turtle
        // Empty implementation allows entities to stay on shell
    }
    
    @Override
    public boolean isPushable() {
        // Turtle is too massive to be pushed by other entities
        return false;
    }
    
    /**
     * Prevent mobs from spawning inside turtle
     */
    public boolean canMobSpawnInside() {
        return false;
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
        // CRITICAL: Skip super.tick() for distant entities to avoid expensive operations
        tickCounter++;
        
        // Early exit for very distant entities - minimal processing only
        if (tickCounter % 20 == 0) { // Check every second
            float distanceToPlayer = getDistanceToNearestPlayer();
            if (distanceToPlayer > 256.0f) {
                // Ultra-minimal tick for very distant entities
                return;
            } else if (distanceToPlayer > 128.0f) {
                // Minimal tick for distant entities
                if (tickCounter % 10 != 0) return; // Only every 0.5 seconds
            } else if (distanceToPlayer > 64.0f) {
                // Reduced tick for mid-distance entities
                if (tickCounter % 4 != 0) return; // Only every 0.2 seconds
            }
        }
        
        // Only call super.tick() for nearby entities
        super.tick();
        
        // Get performance level based on distance to players
        PerformanceManager.PerformanceLevel perfLevel = PerformanceManager.getPerformanceLevel(this);
        int tickDivider = PerformanceManager.getTickRateDivider(this);
        
        // Check if players are nearby less frequently based on performance level
        int checkInterval = switch (perfLevel) {
            case HIGH -> NEAR_PLAYER_CHECK_INTERVAL;
            case MEDIUM -> NEAR_PLAYER_CHECK_INTERVAL * 2;
            case LOW -> NEAR_PLAYER_CHECK_INTERVAL * 4;
            case MINIMAL -> NEAR_PLAYER_CHECK_INTERVAL * 8;
        };
        
        if (nearPlayerCheckTimer++ >= checkInterval) {
            nearPlayerCheckTimer = 0;
            isNearPlayer = checkNearbyPlayers();
        }
        
        // Only process state management based on performance level
        if (perfLevel == PerformanceManager.PerformanceLevel.HIGH || 
            tickCounter % tickDivider == 0) {
            // Basic state management
            stateTimer++;
            
            // TODO: Phase 2 - Implement state machine logic
            // TODO: Phase 4 - Update island position
            // TODO: Phase 5 - Handle island movement
        }
    }
    
    /**
     * Ultra-fast distance check using cached player position
     */
    private float getDistanceToNearestPlayer() {
        // Server side - use cached closest player if available
        var closestPlayer = this.getWorld().getClosestPlayer(this, 300.0);
        if (closestPlayer != null) {
            double dx = Math.abs(this.getX() - closestPlayer.getX());
            double dz = Math.abs(this.getZ() - closestPlayer.getZ());
            return (float) (dx + dz);
        }
        return Float.MAX_VALUE;
    }
    
    /**
     * Check if any players are within interaction range
     * Performance optimized to avoid expensive distance calculations
     */
    private boolean checkNearbyPlayers() {
        if (this.getWorld().isClient) {
            // On client, check distance to local player
            var player = this.getWorld().getClosestPlayer(this, 64.0);
            return player != null;
        } else {
            // On server, use more efficient range check
            return !this.getWorld().getPlayers().isEmpty() && 
                   this.getWorld().getClosestPlayer(this, 64.0) != null;
        }
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
    
    // ========== ISLAND FOUNDATION SYSTEM ==========
    // Foundation for Phase 4: Island Structure System
    // Allows blocks and entities to stand on turtle's back surface
    
    // Island bounds - defines the area where blocks can be placed
    private static final int ISLAND_WIDTH = 24;  // 24 blocks wide
    private static final int ISLAND_LENGTH = 28; // 28 blocks long
    private static final double SHELL_HEIGHT_OFFSET = 5.0; // On top of 5-block hitbox
    
    /**
     * Get the world position of the turtle's shell surface center
     * This is where the island will be anchored
     */
    public Vec3d getShellCenterPos() {
        return new Vec3d(this.getX(), this.getY() + SHELL_HEIGHT_OFFSET, this.getZ());
    }
    
    /**
     * Get the bounds of the island area on the turtle's back
     * Returns min and max coordinates for island placement
     */
    public record IslandBounds(Vec3d min, Vec3d max) {}
    
    public IslandBounds getIslandBounds() {
        Vec3d center = getShellCenterPos();
        double halfWidth = ISLAND_WIDTH / 2.0;
        double halfLength = ISLAND_LENGTH / 2.0;
        
        // Apply turtle's rotation to the bounds
        double yaw = Math.toRadians(this.getYaw());
        double cos = Math.cos(yaw);
        double sin = Math.sin(yaw);
        
        // Calculate rotated corners
        double x1 = center.x + (-halfWidth * cos - (-halfLength) * sin);
        double z1 = center.z + (-halfWidth * sin + (-halfLength) * cos);
        double x2 = center.x + (halfWidth * cos - halfLength * sin);
        double z2 = center.z + (halfWidth * sin + halfLength * cos);
        
        Vec3d min = new Vec3d(Math.min(x1, x2), center.y, Math.min(z1, z2));
        Vec3d max = new Vec3d(Math.max(x1, x2), center.y + 32, Math.max(z1, z2)); // 32 blocks high for island
        
        return new IslandBounds(min, max);
    }
    
    /**
     * Check if a world position is within the turtle's island area
     */
    public boolean isPositionOnShell(Vec3d worldPos) {
        IslandBounds bounds = getIslandBounds();
        return worldPos.x >= bounds.min.x && worldPos.x <= bounds.max.x &&
               worldPos.z >= bounds.min.z && worldPos.z <= bounds.max.z &&
               worldPos.y >= bounds.min.y && worldPos.y <= bounds.max.y;
    }
    
    /**
     * Check if a block position is within the turtle's island area
     */
    public boolean isBlockOnShell(BlockPos blockPos) {
        return isPositionOnShell(Vec3d.ofCenter(blockPos));
    }
    
    /**
     * Convert world coordinates to turtle-relative coordinates
     * Used for storing island structure data
     */
    public Vec3d worldToTurtleRelative(Vec3d worldPos) {
        Vec3d center = getShellCenterPos();
        double dx = worldPos.x - center.x;
        double dz = worldPos.z - center.z;
        double dy = worldPos.y - center.y;
        
        // Apply inverse rotation to get relative position
        double yaw = Math.toRadians(-this.getYaw());
        double cos = Math.cos(yaw);
        double sin = Math.sin(yaw);
        
        double relativeX = dx * cos - dz * sin;
        double relativeZ = dx * sin + dz * cos;
        
        return new Vec3d(relativeX, dy, relativeZ);
    }
    
    /**
     * Convert turtle-relative coordinates to world coordinates
     * Used for placing island structures
     */
    public Vec3d turtleRelativeToWorld(Vec3d relativePos) {
        Vec3d center = getShellCenterPos();
        
        // Apply turtle's rotation
        double yaw = Math.toRadians(this.getYaw());
        double cos = Math.cos(yaw);
        double sin = Math.sin(yaw);
        
        double worldX = center.x + (relativePos.x * cos - relativePos.z * sin);
        double worldZ = center.z + (relativePos.x * sin + relativePos.z * cos);
        double worldY = center.y + relativePos.y;
        
        return new Vec3d(worldX, worldY, worldZ);
    }
    
    /**
     * Get the shell surface Y level at a given X,Z position
     * This creates the "solid surface" that entities can stand on
     */
    public double getShellSurfaceY(double x, double z) {
        // For now, flat surface at shell height
        // TODO Phase 4: Add shell curvature/topology
        return this.getY() + SHELL_HEIGHT_OFFSET;
    }
    
    /**
     * Check if an entity should be considered "standing on" the turtle
     * This replaces the passenger system for actual standing
     */
    public boolean isEntityStandingOnShell(Entity entity) {
        if (entity == this) return false;
        
        Vec3d entityPos = entity.getPos();
        
        // Check if entity is within island bounds
        if (!isPositionOnShell(entityPos)) {
            return false;
        }
        
        // Check if entity is close to shell surface level
        double shellSurfaceY = getShellSurfaceY(entityPos.x, entityPos.z);
        double entityBottomY = entityPos.y;
        
        // Entity is standing on shell if it's within 2 blocks of surface
        return Math.abs(entityBottomY - shellSurfaceY) <= 2.0;
    }
    
    /**
     * Update entities standing on the shell
     * This will be called every tick to move entities with the turtle
     */
    public void updateEntitiesOnShell() {
        if (this.getWorld().isClient) return; // Server-side only
        
        // Find all entities in the area around the turtle
        IslandBounds bounds = getIslandBounds();
        var entitiesInArea = this.getWorld().getOtherEntities(this, 
            net.minecraft.util.math.Box.of(
                bounds.min.add(bounds.max).multiply(0.5), // center
                bounds.max.x - bounds.min.x, // width
                bounds.max.y - bounds.min.y, // height  
                bounds.max.z - bounds.min.z  // depth
            ));
        
        for (Entity entity : entitiesInArea) {
            if (isEntityStandingOnShell(entity)) {
                // TODO Phase 5: Move entity with turtle
                // For now, just prevent them from falling through
                double shellY = getShellSurfaceY(entity.getX(), entity.getZ());
                if (entity.getY() < shellY && entity.getVelocity().y <= 0) {
                    entity.setPosition(entity.getX(), shellY, entity.getZ());
                    entity.setVelocity(entity.getVelocity().x, 0, entity.getVelocity().z);
                    entity.setOnGround(true);
                }
            }
        }
    }
    
}