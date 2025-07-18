package com.bvhfve.aethelon.upgrade;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Elemental effects for upgraded weapons based on gem types
 * Inspired by elemental-enchantments mod but simplified for our use case
 */
public class ElementalEffects {
    
    /**
     * Applies elemental effects when hitting an entity with an upgraded weapon
     */
    public static void applyElementalEffect(World world, ItemStack weapon, LivingEntity attacker, LivingEntity target) {
        WeaponUpgradeSystem.GemType upgradeType = WeaponUpgradeSystem.getUpgradeType(weapon);
        if (upgradeType == null) return;
        
        int tier = WeaponUpgradeSystem.getUpgradeTier(weapon);
        float effectStrength = tier * 0.2f; // 20% per tier
        
        switch (upgradeType) {
            case TURTLE_SCALE -> applyAquaticEffect(world, attacker, target, tier, effectStrength);
            case TURTLE_HEART -> applyVitalityEffect(world, attacker, target, tier, effectStrength);
            case CRYSTALLIZED_WATER -> applyFrostEffect(world, attacker, target, tier, effectStrength);
            case DEEP_SEA_PEARL -> applyLightningEffect(world, attacker, target, tier, effectStrength);
            case ISLAND_ESSENCE -> applyEarthEffect(world, attacker, target, tier, effectStrength);
        }
    }
    
    /**
     * Aquatic Effect - Turtle Scale upgrades
     * Provides water-based benefits and slows enemies
     */
    private static void applyAquaticEffect(World world, LivingEntity attacker, LivingEntity target, int tier, float strength) {
        // Slow the target (like being caught in water)
        int slownessDuration = (int)(100 + (tier * 40)); // 5-13 seconds
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, slownessDuration, tier - 1));
        
        // Give attacker water breathing if they don't have it
        if (attacker instanceof PlayerEntity player && !player.canBreatheInWater()) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 200, 0, false, false));
        }
        
        // Visual effects
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            Vec3d pos = target.getPos();
            serverWorld.spawnParticles(ParticleTypes.BUBBLE, 
                pos.x, pos.y + target.getHeight() / 2, pos.z, 
                5 + tier * 2, 0.5, 0.5, 0.5, 0.1);
        }
        
        world.playSound(null, target.getX(), target.getY(), target.getZ(), 
            SoundEvents.ENTITY_FISH_SWIM, SoundCategory.PLAYERS, 0.5f, 1.0f + tier * 0.2f);
    }
    
    /**
     * Vitality Effect - Turtle Heart upgrades
     * Heals attacker and applies weakness to target
     */
    private static void applyVitalityEffect(World world, LivingEntity attacker, LivingEntity target, int tier, float strength) {
        // Heal the attacker
        float healAmount = tier * 1.0f; // 1-5 hearts
        attacker.heal(healAmount);
        
        // Apply weakness to target
        int weaknessDuration = (int)(80 + (tier * 20)); // 4-9 seconds
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, weaknessDuration, tier - 1));
        
        // Visual effects
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            Vec3d attackerPos = attacker.getPos();
            Vec3d targetPos = target.getPos();
            
            // Healing particles for attacker
            serverWorld.spawnParticles(ParticleTypes.HEART, 
                attackerPos.x, attackerPos.y + attacker.getHeight() + 0.5, attackerPos.z, 
                tier, 0.3, 0.3, 0.3, 0.1);
            
            // Weakness particles for target
            serverWorld.spawnParticles(ParticleTypes.EFFECT, 
                targetPos.x, targetPos.y + target.getHeight() / 2, targetPos.z, 
                3 + tier, 0.5, 0.5, 0.5, 0.1);
        }
        
        world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), 
            SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.3f, 1.5f);
    }
    
    /**
     * Frost Effect - Crystallized Water upgrades
     * Freezes and slows targets
     */
    private static void applyFrostEffect(World world, LivingEntity attacker, LivingEntity target, int tier, float strength) {
        // Freeze the target
        int freezeTicks = (int)(60 + (tier * 30)); // 3-12 seconds
        target.setFrozenTicks(Math.max(target.getFrozenTicks(), freezeTicks));
        
        // Apply slowness
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, freezeTicks, tier));
        
        // Visual effects
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            Vec3d pos = target.getPos();
            serverWorld.spawnParticles(ParticleTypes.SNOWFLAKE, 
                pos.x, pos.y + target.getHeight() / 2, pos.z, 
                10 + tier * 3, 0.5, 0.5, 0.5, 0.2);
        }
        
        world.playSound(null, target.getX(), target.getY(), target.getZ(), 
            SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.5f, 1.5f + tier * 0.1f);
    }
    
    /**
     * Lightning Effect - Deep Sea Pearl upgrades
     * Creates lightning-like effects and chain damage
     */
    private static void applyLightningEffect(World world, LivingEntity attacker, LivingEntity target, int tier, float strength) {
        // Apply glowing effect
        int glowDuration = (int)(100 + (tier * 20)); // 5-10 seconds
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, glowDuration, 0));
        
        // Chance for chain lightning to nearby entities
        if (world.random.nextFloat() < 0.3f + (tier * 0.1f)) {
            world.getOtherEntities(target, target.getBoundingBox().expand(3 + tier), 
                entity -> entity instanceof LivingEntity && entity != attacker)
                .stream()
                .limit(tier)
                .forEach(entity -> {
                    if (entity instanceof LivingEntity living) {
                        living.damage((ServerWorld) world, world.getDamageSources().lightningBolt(), 2.0f + tier);
                        living.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60, 0));
                    }
                });
        }
        
        // Visual effects
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            Vec3d pos = target.getPos();
            serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK, 
                pos.x, pos.y + target.getHeight() / 2, pos.z, 
                8 + tier * 2, 0.3, 0.3, 0.3, 0.3);
        }
        
        world.playSound(null, target.getX(), target.getY(), target.getZ(), 
            SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 0.3f, 1.8f);
    }
    
    /**
     * Earth Effect - Island Essence upgrades
     * Launches targets upward and creates ground effects
     */
    private static void applyEarthEffect(World world, LivingEntity attacker, LivingEntity target, int tier, float strength) {
        // Launch target upward
        float launchStrength = 0.3f + (tier * 0.2f); // 0.5-1.3 blocks
        target.setVelocity(target.getVelocity().x, launchStrength, target.getVelocity().z);
        target.velocityModified = true;
        
        // Apply mining fatigue
        int fatigeDuration = (int)(100 + (tier * 20)); // 5-10 seconds
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, fatigeDuration, tier - 1));
        
        // Visual effects
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            Vec3d pos = target.getPos();
            // Create block particles from the ground
            BlockStateParticleEffect blockParticle = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.STONE.getDefaultState());
            serverWorld.spawnParticles(blockParticle, 
                pos.x, pos.y, pos.z, 
                15 + tier * 5, 1.0, 0.1, 1.0, 0.5);
        }
        
        world.playSound(null, target.getX(), target.getY(), target.getZ(), 
            SoundEvents.BLOCK_STONE_BREAK, SoundCategory.PLAYERS, 0.8f, 0.8f);
    }
    
    /**
     * Gets a description of the elemental effect for tooltips
     */
    public static String getEffectDescription(WeaponUpgradeSystem.GemType gemType, int tier) {
        return switch (gemType) {
            case TURTLE_SCALE -> "Slows enemies and grants water breathing";
            case TURTLE_HEART -> "Heals wielder and weakens enemies";
            case CRYSTALLIZED_WATER -> "Freezes and slows targets";
            case DEEP_SEA_PEARL -> "Lightning effects with chain damage";
            case ISLAND_ESSENCE -> "Launches enemies and reduces mining speed";
        };
    }
}