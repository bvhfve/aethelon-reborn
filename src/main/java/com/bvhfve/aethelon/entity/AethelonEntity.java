package com.bvhfve.aethelon.entity;

import com.bvhfve.aethelon.config.AethelonConfig;
import com.bvhfve.aethelon.util.PerformanceManager;
import com.bvhfve.aethelon.ai.AethelonStateMachine;
import com.bvhfve.aethelon.ai.goals.AethelonIdleGoal;
import com.bvhfve.aethelon.ai.goals.AethelonPathfindGoal;
import com.bvhfve.aethelon.island.IslandManager;
import com.bvhfve.aethelon.ai.goals.AethelonTransitionGoal;
import com.bvhfve.aethelon.loot.AethelonLootSystem;
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
import net.minecraft.entity.TntEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import java.util.List;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Aethelon Entity - A colossal world turtle that carries islands on its back
 * 
 * Phase 3 Enhanced: Advanced damage response, player interaction, and dramatic death mechanics
 */
public class AethelonEntity extends WaterCreatureEntity {
    
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonEntity");
    
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
    private static final int TICK_RATE_DIVIDER = 4;
    private int tickCounter = 0;
    private boolean isNearPlayer = false;
    private int nearPlayerCheckTimer = 0;
    private static final int NEAR_PLAYER_CHECK_INTERVAL = 40;
    
    // Scale calculation for proper texture mapping
    public static final float WORLD_TURTLE_SCALE = 2.5f;
    
    // Phase 2: AI and State Management
    private AethelonStateMachine stateMachine;
    
    // Phase 4: Island Structure System
    private IslandManager islandManager;
    
    // Phase 3: Enhanced damage and interaction system
    private int damageImmunityTimer = 0;
    private float lastDamageAmount = 0.0f;
    private Entity lastAttacker = null;
    private int agitationLevel = 0; // 0-100, affects behavior intensity
    private boolean isEnraged = false;
    
    public AethelonEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
        
        // Initialize Phase 2 systems
        this.stateMachine = new AethelonStateMachine(this);
        
        // Initialize Phase 4 systems
        this.islandManager = new IslandManager(this);
        
        // Phase 4: Create island when turtle spawns (server-side only)
        if (!world.isClient && AethelonConfig.INSTANCE.enable_islands && AethelonConfig.INSTANCE.auto_create_islands) {
            // Delay island creation to next tick to ensure turtle is fully initialized
            world.getServer().execute(() -> {
                if (!isRemoved() && !hasIsland()) {
                    // Randomly select island type based on configuration
                    IslandManager.IslandType type = selectRandomIslandType();
                    boolean success = createIsland(type);
                    if (success) {
                        LOGGER.info("Created {} island for turtle at {}", type.name(), getBlockPos());
                    } else {
                        LOGGER.warn("Failed to create island for turtle at {}", getBlockPos());
                    }
                }
            });
        }
    }
    
    /**
     * Creates the default attributes for Aethelon entities
     */
    public static DefaultAttributeContainer.Builder createAethelonAttributes() {
        return WaterCreatureEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.health : 2000.0)
                .add(EntityAttributes.MOVEMENT_SPEED, AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.movement_speed : 0.75)
                .add(EntityAttributes.FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.knockback_resistance : 1.0);
    }
    
    public boolean canTakeDamage() {
        return true;
    }
    
    /**
     * Phase 3: Enhanced damage system with immunity periods, visual feedback, and agitation
     */
    @Override
    public boolean damage(net.minecraft.server.world.ServerWorld world, net.minecraft.entity.damage.DamageSource source, float amount) {
        // Check damage immunity
        if (damageImmunityTimer > 0) {
            return false;
        }
        
        // Prevent suffocation damage - world turtles are too large to suffocate
        if (source.isOf(net.minecraft.entity.damage.DamageTypes.IN_WALL) || 
            source.isOf(net.minecraft.entity.damage.DamageTypes.CRAMMING)) {
            return false;
        }
        
        // Prevent drowning damage - world turtles are aquatic
        if (source.isOf(net.minecraft.entity.damage.DamageTypes.DROWN)) {
            return false;
        }
        
        // Prevent fall damage - world turtles are too massive
        if (source.isOf(net.minecraft.entity.damage.DamageTypes.FALL)) {
            return false;
        }
        
        // Store damage information for enhanced response
        lastDamageAmount = amount;
        lastAttacker = source.getAttacker();
        
        // Calculate damage with turtle's massive health scaling
        float scaledAmount = amount;
        if (source.getAttacker() instanceof PlayerEntity) {
            // Player damage is more significant to encourage interaction
            scaledAmount = amount * 2.0f;
            
            // Increase agitation level
            agitationLevel = Math.min(100, agitationLevel + (int)(amount * 5));
            
            // Trigger enraged state at high agitation
            if (agitationLevel >= 75 && !isEnraged) {
                isEnraged = true;
                broadcastEnrageMessage((PlayerEntity) source.getAttacker());
            }
        }
        
        // Apply damage
        boolean damaged = super.damage(world, source, scaledAmount);
        
        if (damaged) {
            // Set damage immunity period
            damageImmunityTimer = AethelonConfig.INSTANCE.damage_immunity_ticks;
            
            // Phase 3: Enhanced damage response
            if (source.getAttacker() instanceof PlayerEntity) {
                triggerDamageEffects(world, (PlayerEntity) source.getAttacker(), amount);
                
                // Trigger AI response
                if (stateMachine != null) {
                    stateMachine.triggerDamageResponse();
                }
            }
            
            // Visual and audio feedback
            spawnDamageParticles(world);
            playDamageSound(world);
            
            LOGGER.info("Aethelon took {} damage from {} (agitation: {})", 
                       scaledAmount, source.getName(), agitationLevel);
        }
        
        return damaged;
    }
    
    /**
     * Phase 3: Enhanced death mechanics with dramatic TNT explosion
     */
    @Override
    public void onDeath(DamageSource damageSource) {
        if (!getWorld().isClient && getWorld() instanceof ServerWorld serverWorld) {
            LOGGER.info("Aethelon dying at {} - triggering dramatic explosion", getBlockPos());
            
            // Broadcast death message to all players
            broadcastDeathMessage(damageSource);
            
            // Remove island first to prevent corruption
            if (islandManager != null && hasIsland()) {
                LOGGER.info("Removing island before explosion");
                islandManager.removeIsland(getWorld());
            }
            
            // Create dramatic explosion with multiple TNT entities
            createDeathExplosion(serverWorld);
            
            // Spawn death particles and effects
            spawnDeathEffects(serverWorld);
            
            // Play dramatic death sound
            serverWorld.playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_DEATH, 
                                SoundCategory.HOSTILE, 2.0f, 0.5f);
            
            // Generate loot rewards
            PlayerEntity killer = null;
            if (lastAttacker instanceof PlayerEntity) {
                killer = (PlayerEntity) lastAttacker;
            }
            AethelonLootSystem.generateDeathLoot(this, killer, serverWorld);
        }
        
        super.onDeath(damageSource);
    }
    
    /**
     * Create dramatic explosion with multiple primed TNT entities
     */
    private void createDeathExplosion(ServerWorld world) {
        Vec3d center = getPos();
        int tntCount = 15; // Number of TNT entities
        float explosionRadius = 25.0f; // Explosion radius
        
        LOGGER.info("Creating death explosion with {} TNT entities", tntCount);
        
        // Create multiple TNT entities in a pattern around the turtle
        for (int i = 0; i < tntCount; i++) {
            // Calculate position in a spiral pattern
            double angle = (2 * Math.PI * i) / tntCount;
            double radius = (explosionRadius / 3.0) * (1 + (i % 3)); // Varying distances
            
            double x = center.x + Math.cos(angle) * radius;
            double y = center.y + (getWorld().getRandom().nextDouble() * 10 - 5); // Random height variation
            double z = center.z + Math.sin(angle) * radius;
            
            // Create TNT entity
            TntEntity tnt = new TntEntity(world, x, y, z, this);
            
            // Set short fuse time for dramatic effect
            int fuseTime = 10 + getWorld().getRandom().nextInt(20); // 0.5-1.5 seconds
            tnt.setFuse(fuseTime);
            
            // Add some random velocity for more dynamic explosion
            double vx = (getWorld().getRandom().nextDouble() - 0.5) * 0.5;
            double vy = getWorld().getRandom().nextDouble() * 0.3;
            double vz = (getWorld().getRandom().nextDouble() - 0.5) * 0.5;
            tnt.setVelocity(vx, vy, vz);
            
            world.spawnEntity(tnt);
        }
        
        // Create immediate explosion at turtle's position for instant impact
        world.createExplosion(this, center.x, center.y, center.z, 
                            explosionRadius * 0.5f, World.ExplosionSourceType.MOB);
    }
    
    /**
     * Spawn dramatic death particle effects
     */
    private void spawnDeathEffects(ServerWorld world) {
        Vec3d center = getPos();
        
        // Large explosion particles
        for (int i = 0; i < 100; i++) {
            double x = center.x + (world.getRandom().nextDouble() - 0.5) * 40;
            double y = center.y + world.getRandom().nextDouble() * 20;
            double z = center.z + (world.getRandom().nextDouble() - 0.5) * 40;
            
            world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1, 0, 0, 0, 0);
        }
        
        // Smoke and fire effects
        for (int i = 0; i < 200; i++) {
            double x = center.x + (world.getRandom().nextDouble() - 0.5) * 60;
            double y = center.y + world.getRandom().nextDouble() * 30;
            double z = center.z + (world.getRandom().nextDouble() - 0.5) * 60;
            
            world.spawnParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 1, 
                               (world.getRandom().nextDouble() - 0.5) * 0.5, 
                               world.getRandom().nextDouble() * 0.5, 
                               (world.getRandom().nextDouble() - 0.5) * 0.5, 0);
        }
        
        // Water splash effects (since turtle is aquatic)
        for (int i = 0; i < 150; i++) {
            double x = center.x + (world.getRandom().nextDouble() - 0.5) * 50;
            double y = center.y;
            double z = center.z + (world.getRandom().nextDouble() - 0.5) * 50;
            
            world.spawnParticles(ParticleTypes.SPLASH, x, y, z, 1, 
                               (world.getRandom().nextDouble() - 0.5) * 2, 
                               world.getRandom().nextDouble() * 2, 
                               (world.getRandom().nextDouble() - 0.5) * 2, 0);
        }
    }
    
    /**
     * Broadcast death message to all players
     */
    private void broadcastDeathMessage(DamageSource damageSource) {
        String message;
        if (lastAttacker instanceof PlayerEntity player) {
            message = "The ancient world turtle has been slain by " + player.getName().getString() + "! The island is lost forever...";
        } else {
            message = "An ancient world turtle has perished! The island is lost forever...";
        }
        
        // Broadcast to all players in the world
        for (PlayerEntity player : getWorld().getPlayers()) {
            player.sendMessage(Text.literal(message), false);
        }
    }
    
    /**
     * Phase 3: Enhanced damage effects
     */
    private void triggerDamageEffects(ServerWorld world, PlayerEntity attacker, float damage) {
        Vec3d center = getPos();
        
        // Screen shake effect (via particles that clients can detect)
        world.spawnParticles(ParticleTypes.ANGRY_VILLAGER, 
                           center.x, center.y + 10, center.z, 
                           (int)(damage * 2), 5, 5, 5, 0);
        
        // Damage-based particle intensity
        int particleCount = Math.min(50, (int)(damage * 3));
        for (int i = 0; i < particleCount; i++) {
            double x = center.x + (world.getRandom().nextDouble() - 0.5) * 20;
            double y = center.y + world.getRandom().nextDouble() * 15;
            double z = center.z + (world.getRandom().nextDouble() - 0.5) * 20;
            
            world.spawnParticles(ParticleTypes.CRIT, x, y, z, 1, 0, 0, 0, 0.1);
        }
        
        // Send feedback message to attacker
        if (isEnraged) {
            attacker.sendMessage(Text.literal("The ancient turtle is enraged by your attacks!"), true);
        } else if (agitationLevel > 50) {
            attacker.sendMessage(Text.literal("The ancient turtle stirs with anger..."), true);
        } else {
            attacker.sendMessage(Text.literal("The ancient turtle feels your presence..."), true);
        }
    }
    
    /**
     * Spawn damage particles
     */
    private void spawnDamageParticles(ServerWorld world) {
        Vec3d center = getPos();
        
        // Blood/damage particles
        for (int i = 0; i < 20; i++) {
            double x = center.x + (world.getRandom().nextDouble() - 0.5) * 15;
            double y = center.y + world.getRandom().nextDouble() * 10;
            double z = center.z + (world.getRandom().nextDouble() - 0.5) * 15;
            
            world.spawnParticles(ParticleTypes.DAMAGE_INDICATOR, x, y, z, 1, 0, 0, 0, 0);
        }
        
        // Agitation particles if enraged
        if (isEnraged) {
            for (int i = 0; i < 10; i++) {
                double x = center.x + (world.getRandom().nextDouble() - 0.5) * 25;
                double y = center.y + world.getRandom().nextDouble() * 15;
                double z = center.z + (world.getRandom().nextDouble() - 0.5) * 25;
                
                world.spawnParticles(ParticleTypes.ANGRY_VILLAGER, x, y, z, 1, 0, 0, 0, 0);
            }
        }
    }
    
    /**
     * Play damage sound
     */
    private void playDamageSound(ServerWorld world) {
        if (isEnraged) {
            world.playSound(null, getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 
                          SoundCategory.HOSTILE, 1.5f, 0.8f);
        } else {
            world.playSound(null, getBlockPos(), SoundEvents.ENTITY_TURTLE_HURT, 
                          SoundCategory.NEUTRAL, 2.0f, 0.5f);
        }
    }
    
    /**
     * Broadcast enrage message
     */
    private void broadcastEnrageMessage(PlayerEntity attacker) {
        String message = attacker.getName().getString() + " has awakened the fury of the ancient world turtle!";
        
        // Send to all nearby players
        for (PlayerEntity player : getWorld().getPlayers()) {
            if (player.squaredDistanceTo(this) < 10000) { // Within 100 blocks
                player.sendMessage(Text.literal(message), false);
            }
        }
        
        LOGGER.info("Aethelon enraged by player: {}", attacker.getName().getString());
    }
    
    protected boolean isInWall() {
        return false;
    }
    
    public boolean canAvoidTraps() {
        return true;
    }
    
    public boolean cannotDespawn() {
        return true;
    }
    
    @Override
    public void pushAwayFrom(Entity entity) {
        // Don't push entities away - allows standing on turtle
    }
    
    @Override
    public boolean isPushable() {
        return false;
    }
    
    @Override
    protected void initGoals() {
        super.initGoals();
        
        // Phase 2: Add custom AI goals with priority order
        this.goalSelector.add(1, new AethelonTransitionGoal(this));
        this.goalSelector.add(2, new AethelonPathfindGoal(this));
        this.goalSelector.add(3, new AethelonIdleGoal(this));
    }
    
    @Override
    public void tick() {
        tickCounter++;
        
        // Performance optimization - skip processing for distant entities
        if (tickCounter % 20 == 0) {
            float distanceToPlayer = getDistanceToNearestPlayer();
            if (distanceToPlayer > 256.0f) {
                return;
            } else if (distanceToPlayer > 128.0f && tickCounter % 10 != 0) {
                return;
            } else if (distanceToPlayer > 64.0f && tickCounter % 4 != 0) {
                return;
            }
        }
        
        super.tick();
        
        // Get performance level based on distance to players
        PerformanceManager.PerformanceLevel perfLevel = PerformanceManager.getPerformanceLevel(this);
        int tickDivider = PerformanceManager.getTickRateDivider(this);
        
        // Only process state management based on performance level
        if (perfLevel == PerformanceManager.PerformanceLevel.HIGH || 
            tickCounter % tickDivider == 0) {
            // Phase 2: State machine logic
            if (stateMachine != null) {
                stateMachine.tick();
            }
            
            // Basic state management
            stateTimer++;
            
            // Phase 4: Update island position
            if (islandManager != null) {
                islandManager.updateIslandPosition(getWorld());
            }
            
            // Phase 3: Update damage immunity and agitation
            if (damageImmunityTimer > 0) {
                damageImmunityTimer--;
            }
            
            // Gradually reduce agitation over time
            if (agitationLevel > 0 && tickCounter % 60 == 0) { // Every 3 seconds
                agitationLevel = Math.max(0, agitationLevel - 1);
                if (agitationLevel < 50 && isEnraged) {
                    isEnraged = false;
                    LOGGER.info("Aethelon calmed down (agitation: {})", agitationLevel);
                }
            }
        }
    }
    
    private float getDistanceToNearestPlayer() {
        var closestPlayer = this.getWorld().getClosestPlayer(this, 300.0);
        if (closestPlayer != null) {
            double dx = Math.abs(this.getX() - closestPlayer.getX());
            double dz = Math.abs(this.getZ() - closestPlayer.getZ());
            return (float) (dx + dz);
        }
        return Float.MAX_VALUE;
    }
    
    // Getters and setters for state management
    public AethelonState getCurrentState() {
        return currentState;
    }
    
    public void setState(AethelonState newState) {
        if (this.currentState != newState) {
            this.currentState = newState;
            this.stateTimer = 0;
        }
    }
    
    public int getStateTimer() {
        return stateTimer;
    }
    
    // Phase 2: Getter methods for AI system
    public AethelonStateMachine getStateMachine() {
        return stateMachine;
    }
    
    // Phase 4: Island system methods
    public IslandManager getIslandManager() {
        return islandManager;
    }
    
    public boolean createIsland(IslandManager.IslandType type) {
        if (islandManager != null && !getWorld().isClient) {
            return islandManager.loadIslandStructure(getWorld(), type);
        }
        return false;
    }
    
    public void removeIsland() {
        if (islandManager != null && !getWorld().isClient) {
            islandManager.removeIsland(getWorld());
        }
    }
    
    public boolean hasIsland() {
        return islandManager != null && islandManager.hasIsland();
    }
    
    private IslandManager.IslandType selectRandomIslandType() {
        if (!AethelonConfig.INSTANCE.enable_islands || !AethelonConfig.INSTANCE.auto_create_islands) {
            return IslandManager.IslandType.SMALL;
        }
        
        double random = getWorld().getRandom().nextDouble();
        double smallChance = AethelonConfig.INSTANCE.small_island_chance;
        double mediumChance = AethelonConfig.INSTANCE.medium_island_chance;
        
        if (random < smallChance) {
            return IslandManager.IslandType.SMALL;
        } else if (random < smallChance + mediumChance) {
            return IslandManager.IslandType.MEDIUM;
        } else {
            return IslandManager.IslandType.LARGE;
        }
    }
    
    // Phase 3: Enhanced interaction getters
    public int getAgitationLevel() {
        return agitationLevel;
    }
    
    public boolean isEnraged() {
        return isEnraged;
    }
    
    public LivingEntity getLastAttacker() {
        return lastAttacker instanceof LivingEntity ? (LivingEntity) lastAttacker : null;
    }
    
    public float getLastDamageAmount() {
        return lastDamageAmount;
    }
    
    // Island foundation system methods (simplified for space)
    public Vec3d getShellCenterPos() {
        return new Vec3d(this.getX(), this.getY() + 5.0 * WORLD_TURTLE_SCALE, this.getZ());
    }
    
    public boolean isPositionOnShell(Vec3d worldPos) {
        Vec3d center = getShellCenterPos();
        double dx = Math.abs(worldPos.x - center.x);
        double dz = Math.abs(worldPos.z - center.z);
        return dx <= 24 * WORLD_TURTLE_SCALE && dz <= 28 * WORLD_TURTLE_SCALE &&
               worldPos.y >= center.y && worldPos.y <= center.y + 32 * WORLD_TURTLE_SCALE;
    }
    
    public boolean isBlockOnShell(BlockPos blockPos) {
        return isPositionOnShell(Vec3d.ofCenter(blockPos));
    }
    
    public Vec3d turtleRelativeToWorld(Vec3d relativePos) {
        Vec3d center = getShellCenterPos();
        double yaw = Math.toRadians(this.getYaw());
        double cos = Math.cos(yaw);
        double sin = Math.sin(yaw);
        
        double worldX = center.x + (relativePos.x * cos - relativePos.z * sin);
        double worldZ = center.z + (relativePos.x * sin + relativePos.z * cos);
        double worldY = center.y + relativePos.y;
        
        return new Vec3d(worldX, worldY, worldZ);
    }
    
    // Enhanced spawn conditions with distance checking
    public static boolean canSpawn(EntityType<? extends WaterCreatureEntity> type, WorldAccess world, 
                                  SpawnReason spawnReason, BlockPos pos, Random random) {
        
        // Basic water depth and environment checks
        int waterDepthRequired = AethelonConfig.getWaterDepthRequired();
        
        // Must be in very deep water
        for (int i = 0; i < waterDepthRequired; i++) {
            if (!world.getBlockState(pos.down(i)).getFluidState().isIn(net.minecraft.registry.tag.FluidTags.WATER)) {
                return false;
            }
        }
        
        int clearanceRequired = AethelonConfig.getClearanceAboveRequired();
        
        // Must have clear space above
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
        
        // Low light level for rarity
        if (world.getLightLevel(pos) > 7) {
            return false;
        }
        
        // Configurable rarity check
        float spawnRarity = AethelonConfig.getSpawnRarity();
        if (random.nextFloat() >= spawnRarity) {
            return false;
        }
        
        // Enhanced spawn checking with distance and population limits
        if (world instanceof net.minecraft.world.ServerWorldAccess serverWorldAccess) {
            return AethelonSpawnChecker.canSpawnWithAllChecks(type, serverWorldAccess, spawnReason, pos, random);
        }
        
        return true; // Allow spawn in non-server contexts
    }
}