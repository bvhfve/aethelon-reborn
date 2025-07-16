package com.bvhfve.aethelon.compat;

import com.bvhfve.aethelon.entity.AethelonEntity;
import com.bvhfve.aethelon.items.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Compatibility integration with Alex's Mobs mod
 * 
 * Features:
 * - Alex's aquatic mobs can spawn around turtle islands
 * - Special interactions between Aethelon turtles and Alex's sea creatures
 * - Enhanced loot from Alex's mobs when near turtle islands
 * - Turtle shell items provide protection from hostile Alex's mobs
 */
public class AlexsMobsCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonCompat/AlexsMobs");
    
    // Alex's Mobs aquatic creatures that should interact with Aethelon
    private static final Set<String> ALEXS_AQUATIC_MOBS = new HashSet<>(Arrays.asList(
        "alexsmobs:orca",
        "alexsmobs:cachalot_whale", 
        "alexsmobs:lobster",
        "alexsmobs:seal",
        "alexsmobs:frilled_shark",
        "alexsmobs:hammerhead_shark",
        "alexsmobs:giant_squid",
        "alexsmobs:mantis_shrimp",
        "alexsmobs:sunfish",
        "alexsmobs:blobfish",
        "alexsmobs:comb_jelly",
        "alexsmobs:devil_ray",
        "alexsmobs:flying_fish"
    ));
    
    // Friendly Alex's mobs that should be attracted to turtle islands
    private static final Set<String> FRIENDLY_ALEXS_MOBS = new HashSet<>(Arrays.asList(
        "alexsmobs:orca",
        "alexsmobs:cachalot_whale",
        "alexsmobs:seal",
        "alexsmobs:sunfish",
        "alexsmobs:flying_fish",
        "alexsmobs:comb_jelly"
    ));
    
    // Hostile Alex's mobs that turtle shell armor should protect against
    private static final Set<String> HOSTILE_ALEXS_MOBS = new HashSet<>(Arrays.asList(
        "alexsmobs:frilled_shark",
        "alexsmobs:hammerhead_shark",
        "alexsmobs:giant_squid",
        "alexsmobs:mantis_shrimp"
    ));
    
    /**
     * Initialize Alex's Mobs compatibility
     */
    public static boolean initialize() {
        try {
            LOGGER.info("Initializing Alex's Mobs compatibility...");
            
            // Register aquatic mob interactions
            registerAquaticMobInteractions();
            
            // Register turtle island mob spawning
            registerTurtleIslandSpawning();
            
            // Register protection mechanics
            registerProtectionMechanics();
            
            // Register enhanced loot
            registerEnhancedLoot();
            
            LOGGER.info("Alex's Mobs compatibility initialized successfully!");
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Alex's Mobs compatibility: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Register interactions between Alex's aquatic mobs and Aethelon turtles
     */
    private static void registerAquaticMobInteractions() {
        // Friendly mobs are attracted to turtle islands
        // Hostile mobs might be deterred by large turtle presence
    }
    
    /**
     * Check if an Alex's mob should be attracted to turtle islands
     */
    public static boolean shouldBeAttractedToTurtle(Entity entity) {
        if (entity == null) return false;
        
        String entityId = entity.getType().toString();
        return FRIENDLY_ALEXS_MOBS.contains(entityId);
    }
    
    /**
     * Check if an Alex's mob should avoid Aethelon turtles
     */
    public static boolean shouldAvoidTurtle(Entity entity) {
        if (entity == null) return false;
        
        String entityId = entity.getType().toString();
        // Some hostile mobs might avoid the massive turtle
        return entityId.equals("alexsmobs:frilled_shark") || 
               entityId.equals("alexsmobs:hammerhead_shark");
    }
    
    /**
     * Get attraction range for Alex's mobs to turtle islands
     */
    public static double getAttractionRange(Entity entity) {
        if (!shouldBeAttractedToTurtle(entity)) return 0;
        
        String entityId = entity.getType().toString();
        
        // Large creatures have longer attraction range
        if (entityId.equals("alexsmobs:orca") || entityId.equals("alexsmobs:cachalot_whale")) {
            return 128.0; // Very long range
        } else if (entityId.equals("alexsmobs:seal") || entityId.equals("alexsmobs:sunfish")) {
            return 64.0; // Medium range
        }
        
        return 32.0; // Default range
    }
    
    /**
     * Register enhanced spawning of Alex's mobs around turtle islands
     */
    private static void registerTurtleIslandSpawning() {
        // Alex's aquatic mobs have higher spawn rates near turtle islands
    }
    
    /**
     * Get spawn weight modifier for Alex's mobs near turtle islands
     */
    public static int getSpawnWeightModifier(String mobId, World world, BlockPos pos) {
        // Check if near a turtle island
        List<AethelonEntity> nearbyTurtles = world.getEntitiesByClass(
            AethelonEntity.class,
            new net.minecraft.util.math.Box(pos).expand(64.0),
            entity -> entity.isAlive()
        );
        
        if (nearbyTurtles.isEmpty()) return 1; // Normal spawn rate
        
        if (FRIENDLY_ALEXS_MOBS.contains(mobId)) {
            return 3; // 3x spawn rate for friendly mobs
        } else if (ALEXS_AQUATIC_MOBS.contains(mobId)) {
            return 2; // 2x spawn rate for other aquatic mobs
        }
        
        return 1; // Normal spawn rate
    }
    
    /**
     * Register protection mechanics for turtle shell armor
     */
    private static void registerProtectionMechanics() {
        // Turtle shell armor provides protection against Alex's hostile mobs
    }
    
    /**
     * Check if player has turtle shell protection against Alex's mob
     */
    public static boolean hasTurtleShellProtection(PlayerEntity player, Entity attacker) {
        if (attacker == null) return false;
        
        String attackerId = attacker.getType().toString();
        if (!HOSTILE_ALEXS_MOBS.contains(attackerId)) return false;
        
        // Count turtle shell armor pieces
        int turtleArmorPieces = 0;
        if (player.getInventory().getArmorStack(3).getItem() == ModItems.TURTLE_SHELL_HELMET) turtleArmorPieces++;
        if (player.getInventory().getArmorStack(2).getItem() == ModItems.TURTLE_SHELL_CHESTPLATE) turtleArmorPieces++;
        if (player.getInventory().getArmorStack(1).getItem() == ModItems.TURTLE_SHELL_LEGGINGS) turtleArmorPieces++;
        if (player.getInventory().getArmorStack(0).getItem() == ModItems.TURTLE_SHELL_BOOTS) turtleArmorPieces++;
        
        return turtleArmorPieces >= 2; // Need at least 2 pieces for protection
    }
    
    /**
     * Get damage reduction percentage from turtle shell armor
     */
    public static float getTurtleShellDamageReduction(PlayerEntity player, Entity attacker) {
        if (!hasTurtleShellProtection(player, attacker)) return 0.0f;
        
        String attackerId = attacker.getType().toString();
        
        // Count turtle shell armor pieces
        int turtleArmorPieces = 0;
        if (player.getInventory().getArmorStack(3).getItem() == ModItems.TURTLE_SHELL_HELMET) turtleArmorPieces++;
        if (player.getInventory().getArmorStack(2).getItem() == ModItems.TURTLE_SHELL_CHESTPLATE) turtleArmorPieces++;
        if (player.getInventory().getArmorStack(1).getItem() == ModItems.TURTLE_SHELL_LEGGINGS) turtleArmorPieces++;
        if (player.getInventory().getArmorStack(0).getItem() == ModItems.TURTLE_SHELL_BOOTS) turtleArmorPieces++;
        
        // More pieces = more protection
        float baseReduction = turtleArmorPieces * 0.1f; // 10% per piece
        
        // Extra protection against specific mobs
        if (attackerId.equals("alexsmobs:frilled_shark") || 
            attackerId.equals("alexsmobs:hammerhead_shark")) {
            baseReduction += 0.2f; // Extra 20% against sharks
        } else if (attackerId.equals("alexsmobs:giant_squid")) {
            baseReduction += 0.15f; // Extra 15% against giant squid
        }
        
        return Math.min(baseReduction, 0.8f); // Cap at 80% reduction
    }
    
    /**
     * Register enhanced loot from Alex's mobs
     */
    private static void registerEnhancedLoot() {
        // Alex's mobs drop better loot when killed near turtle islands
    }
    
    /**
     * Get enhanced loot for Alex's mob killed near turtle island
     */
    public static ItemStack getEnhancedAlexsMobLoot(Entity mob, World world) {
        if (mob == null) return ItemStack.EMPTY;
        
        String mobId = mob.getType().toString();
        if (!ALEXS_AQUATIC_MOBS.contains(mobId)) return ItemStack.EMPTY;
        
        // Check if near turtle island
        List<AethelonEntity> nearbyTurtles = world.getEntitiesByClass(
            AethelonEntity.class,
            mob.getBoundingBox().expand(64.0),
            entity -> entity.isAlive()
        );
        
        if (nearbyTurtles.isEmpty()) return ItemStack.EMPTY;
        
        // Return turtle-themed items as enhanced loot
        if (mobId.equals("alexsmobs:cachalot_whale") || mobId.equals("alexsmobs:orca")) {
            return new ItemStack(ModItems.TURTLE_HEART); // Rare drop from large whales
        } else if (mobId.equals("alexsmobs:giant_squid")) {
            return new ItemStack(ModItems.ANCIENT_TURTLE_SCALE); // Rare drop from giant squid
        } else if (mobId.equals("alexsmobs:lobster") || mobId.equals("alexsmobs:mantis_shrimp")) {
            return new ItemStack(ModItems.TURTLE_SHELL_FRAGMENT); // Common drop from crustaceans
        }
        
        return new ItemStack(ModItems.DEEP_SEA_PEARL); // Default enhanced loot
    }
    
    /**
     * Check if Alex's mob should follow Aethelon turtle
     */
    public static boolean shouldFollowTurtle(Entity mob, AethelonEntity turtle) {
        if (mob == null || turtle == null) return false;
        
        String mobId = mob.getType().toString();
        
        // Whales and seals might follow turtles
        return mobId.equals("alexsmobs:orca") || 
               mobId.equals("alexsmobs:cachalot_whale") ||
               mobId.equals("alexsmobs:seal");
    }
    
    /**
     * Get follow distance for Alex's mobs
     */
    public static double getFollowDistance(Entity mob) {
        if (mob == null) return 0;
        
        String mobId = mob.getType().toString();
        
        if (mobId.equals("alexsmobs:orca") || mobId.equals("alexsmobs:cachalot_whale")) {
            return 32.0; // Large whales keep more distance
        } else if (mobId.equals("alexsmobs:seal")) {
            return 16.0; // Seals stay closer
        }
        
        return 24.0; // Default follow distance
    }
}