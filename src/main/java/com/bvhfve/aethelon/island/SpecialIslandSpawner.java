package com.bvhfve.aethelon.island;

import com.bvhfve.aethelon.Aethelon;
import com.bvhfve.aethelon.entity.AethelonEntity;
import com.bvhfve.aethelon.config.AethelonConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Special Island Spawning System for Aethelon Mod
 * 
 * Handles the spawning logic for rare and powerful special islands:
 * - Boss Islands: Rare, dangerous islands with powerful guardians
 * - Treasure Islands: Hidden islands containing valuable loot
 * - Mystical Islands: Ancient islands with magical properties
 * 
 * These islands spawn based on specific conditions and player progression.
 */
public class SpecialIslandSpawner {
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonSpecialIslands");
    
    // Spawn chance modifiers (per 1000 spawns)
    private static final int BOSS_ISLAND_BASE_CHANCE = 1;      // 0.1% base chance
    private static final int TREASURE_ISLAND_BASE_CHANCE = 3;  // 0.3% base chance  
    private static final int MYSTICAL_ISLAND_BASE_CHANCE = 2;  // 0.2% base chance
    
    // Get distance requirements from config
    private static double getMinDistanceFromSpawn() {
        return AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.min_distance_from_spawn_for_special : 5000.0;
    }
    
    private static double getMinDistanceBetweenSpecial() {
        return AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.min_distance_between_special_islands : 2000.0;
    }
    
    // Get player progression requirements from config
    private static int getMinPlaytimeForBoss() {
        return AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.min_playtime_hours_for_boss_islands : 10;
    }
    
    private static int getMinPlaytimeForMystical() {
        return AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.min_playtime_hours_for_mystical_islands : 15;
    }
    
    // Tracking for spawned special islands
    private static final Map<String, Set<BlockPos>> SPAWNED_SPECIAL_ISLANDS = new HashMap<>();
    
    static {
        SPAWNED_SPECIAL_ISLANDS.put("boss", new HashSet<>());
        SPAWNED_SPECIAL_ISLANDS.put("treasure", new HashSet<>());
        SPAWNED_SPECIAL_ISLANDS.put("mystical", new HashSet<>());
    }
    
    /**
     * Attempt to spawn a special island on the given turtle
     */
    public static boolean trySpawnSpecialIsland(AethelonEntity turtle, ServerWorld world) {
        // Check if special islands are enabled
        if (AethelonConfig.INSTANCE != null && !AethelonConfig.INSTANCE.enable_special_islands) {
            return false;
        }
        
        if (turtle.getIslandManager().hasIsland()) {
            return false; // Turtle already has an island
        }
        
        Vec3d turtlePos = turtle.getPos();
        PlayerEntity nearestPlayer = world.getClosestPlayer(turtlePos.x, turtlePos.y, turtlePos.z, 100.0, false);
        
        if (nearestPlayer == null) {
            return false; // No player nearby to witness the special island
        }
        
        // Debug logging
        if (AethelonConfig.INSTANCE != null && AethelonConfig.INSTANCE.debug_special_island_spawning) {
            LOGGER.debug("Attempting special island spawn at {} for player {}", turtlePos, nearestPlayer.getName().getString());
        }
        
        // Check basic conditions
        if (!meetsBasicSpawnConditions(world, turtlePos)) {
            if (AethelonConfig.INSTANCE != null && AethelonConfig.INSTANCE.debug_special_island_spawning) {
                LOGGER.debug("Basic spawn conditions not met for special island at {}", turtlePos);
            }
            return false;
        }
        
        // Determine which special island type to attempt
        IslandManager.IslandType specialType = determineSpecialIslandType(world, turtlePos, nearestPlayer);
        
        if (specialType == null) {
            if (AethelonConfig.INSTANCE != null && AethelonConfig.INSTANCE.debug_special_island_spawning) {
                LOGGER.debug("No special island type selected for spawn at {}", turtlePos);
            }
            return false; // No special island should spawn
        }
        
        // Check global limits
        if (!canSpawnSpecialIsland(specialType)) {
            if (AethelonConfig.INSTANCE != null && AethelonConfig.INSTANCE.debug_special_island_spawning) {
                LOGGER.debug("Global limit reached for {} islands", specialType.name());
            }
            return false;
        }
        
        // Attempt to spawn the special island
        return spawnSpecialIsland(turtle, world, specialType, nearestPlayer);
    }
    
    /**
     * Check basic conditions for special island spawning
     */
    private static boolean meetsBasicSpawnConditions(ServerWorld world, Vec3d position) {
        // Must be in deep ocean
        var biomeEntry = world.getBiome(BlockPos.ofFloored(position));
        if (!biomeEntry.matchesKey(BiomeKeys.DEEP_OCEAN) && !biomeEntry.matchesKey(BiomeKeys.DEEP_COLD_OCEAN) && 
            !biomeEntry.matchesKey(BiomeKeys.DEEP_FROZEN_OCEAN) && !biomeEntry.matchesKey(BiomeKeys.DEEP_LUKEWARM_OCEAN)) {
            return false;
        }
        
        // Must be far from world spawn
        BlockPos worldSpawn = world.getSpawnPos();
        double distanceFromSpawn = position.distanceTo(Vec3d.ofCenter(worldSpawn));
        if (distanceFromSpawn < getMinDistanceFromSpawn()) {
            return false;
        }
        
        // Must be far from other special islands
        return isPositionValidForSpecialIsland(position);
    }
    
    /**
     * Determine which special island type should spawn based on conditions
     */
    private static IslandManager.IslandType determineSpecialIslandType(ServerWorld world, Vec3d position, PlayerEntity player) {
        net.minecraft.util.math.random.Random random = world.getRandom();
        
        // Calculate spawn chances based on various factors
        int treasureChance = calculateTreasureChance(world, position, player);
        int bossChance = calculateBossChance(world, position, player);
        int mysticalChance = calculateMysticalChance(world, position, player);
        
        // Apply config multiplier
        double multiplier = AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.special_island_spawn_multiplier : 1.0;
        treasureChance = (int) (treasureChance * multiplier);
        bossChance = (int) (bossChance * multiplier);
        mysticalChance = (int) (mysticalChance * multiplier);
        
        int totalChance = treasureChance + bossChance + mysticalChance;
        
        if (totalChance == 0) {
            return null; // No special island should spawn
        }
        
        // Weighted random selection
        int roll = random.nextInt(1000);
        
        if (roll < treasureChance) {
            LOGGER.info("Special island spawn: Treasure Island selected (chance: {})", treasureChance);
            return IslandManager.IslandType.TREASURE;
        } else if (roll < treasureChance + bossChance) {
            LOGGER.info("Special island spawn: Boss Island selected (chance: {})", bossChance);
            return IslandManager.IslandType.BOSS;
        } else if (roll < totalChance) {
            LOGGER.info("Special island spawn: Mystical Island selected (chance: {})", mysticalChance);
            return IslandManager.IslandType.MYSTICAL;
        }
        
        return null;
    }
    
    /**
     * Calculate treasure island spawn chance
     */
    private static int calculateTreasureChance(ServerWorld world, Vec3d position, PlayerEntity player) {
        int chance = TREASURE_ISLAND_BASE_CHANCE;
        
        // Higher chance during storms (if weather system exists)
        if (world.isRaining() && world.isThundering()) {
            chance *= 2;
        }
        
        // Higher chance if player has good equipment (simplified check)
        if (player.getArmor() >= 10) {
            chance += 1;
        }
        
        // Higher chance in certain moon phases
        long dayTime = world.getTimeOfDay() % 24000;
        if (dayTime >= 13000 && dayTime <= 23000) { // Night time
            chance += 1;
        }
        
        return Math.min(chance, 10); // Cap at 1% chance
    }
    
    /**
     * Calculate boss island spawn chance
     */
    private static int calculateBossChance(ServerWorld world, Vec3d position, PlayerEntity player) {
        // Simplified progression check - use player level as proxy for playtime
        if (player.experienceLevel < getMinPlaytimeForBoss()) {
            return 0; // Player not ready for boss islands
        }
        
        int chance = BOSS_ISLAND_BASE_CHANCE;
        
        // Higher chance if player has good equipment
        if (player.getArmor() >= 15) { // Well-equipped player
            chance += 1;
        }
        
        // Higher chance if player has high experience level (proxy for mob kills)
        if (player.experienceLevel > 20) {
            chance += 1;
        }
        
        // Much higher chance in the deepest ocean areas
        if (position.y < 30) { // Very deep water
            chance += 2;
        }
        
        return Math.min(chance, 8); // Cap at 0.8% chance
    }
    
    /**
     * Calculate mystical island spawn chance
     */
    private static int calculateMysticalChance(ServerWorld world, Vec3d position, PlayerEntity player) {
        // Simplified progression check - use player level as proxy for playtime
        if (player.experienceLevel < getMinPlaytimeForMystical()) {
            return 0; // Player not ready for mystical islands
        }
        
        int chance = MYSTICAL_ISLAND_BASE_CHANCE;
        
        // Higher chance during specific times (dawn and dusk)
        long dayTime = world.getTimeOfDay() % 24000;
        if ((dayTime >= 22000 && dayTime <= 24000) || (dayTime >= 0 && dayTime <= 2000)) {
            chance += 2; // Dawn/dusk bonus
        }
        
        // Higher chance if player has enchanted items
        if (player.getMainHandStack().hasEnchantments() || player.getOffHandStack().hasEnchantments()) {
            chance += 1;
        }
        
        // Higher chance if player has high experience level (proxy for exploration)
        if (player.experienceLevel > 30) {
            chance += 1;
        }
        
        return Math.min(chance, 6); // Cap at 0.6% chance
    }
    
    /**
     * Check if position is valid for special island spawning
     */
    private static boolean isPositionValidForSpecialIsland(Vec3d position) {
        for (Set<BlockPos> islandPositions : SPAWNED_SPECIAL_ISLANDS.values()) {
            for (BlockPos existingPos : islandPositions) {
                double distance = position.distanceTo(Vec3d.ofCenter(existingPos));
                if (distance < getMinDistanceBetweenSpecial()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Actually spawn the special island
     */
    private static boolean spawnSpecialIsland(AethelonEntity turtle, ServerWorld world, 
                                            IslandManager.IslandType type, PlayerEntity player) {
        try {
            // Load the special island structure
            boolean success = turtle.getIslandManager().loadIslandStructure(world, type);
            
            if (success) {
                // Track the spawned island
                String typeKey = type.name().toLowerCase();
                SPAWNED_SPECIAL_ISLANDS.get(typeKey).add(BlockPos.ofFloored(turtle.getPos()));
                
                // Send special message to player
                sendSpecialIslandMessage(player, type);
                
                // Add special effects
                addSpecialIslandEffects(world, turtle.getPos(), type);
                
                LOGGER.info("Successfully spawned {} at {} for player {}", 
                          type.name(), turtle.getPos(), player.getName().getString());
                
                return true;
            }
            
        } catch (Exception e) {
            LOGGER.error("Failed to spawn special island {}: {}", type.name(), e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Send special message to player when they discover a special island
     */
    private static void sendSpecialIslandMessage(PlayerEntity player, IslandManager.IslandType type) {
        String message = switch (type) {
            case BOSS -> "§c§lA menacing presence stirs beneath the waves... A Boss Island has emerged!";
            case TREASURE -> "§6§lThe ocean whispers of hidden riches... A Treasure Island has surfaced!";
            case MYSTICAL -> "§d§lAncient magic pulses through the water... A Mystical Island has appeared!";
            default -> "§b§lSomething extraordinary has emerged from the depths...";
        };
        
        player.sendMessage(net.minecraft.text.Text.literal(message), false);
    }
    
    /**
     * Add special visual/audio effects when special islands spawn
     */
    private static void addSpecialIslandEffects(ServerWorld world, Vec3d position, IslandManager.IslandType type) {
        // Add particle effects and sounds based on island type
        // This would be implemented with proper particle and sound systems
        
        switch (type) {
            case BOSS -> {
                // Dark particles, thunder sounds
                LOGGER.debug("Adding boss island effects at {}", position);
            }
            case TREASURE -> {
                // Golden particles, treasure sounds
                LOGGER.debug("Adding treasure island effects at {}", position);
            }
            case MYSTICAL -> {
                // Magical particles, mystical sounds
                LOGGER.debug("Adding mystical island effects at {}", position);
            }
        }
    }
    
    /**
     * Get the count of spawned special islands of a specific type
     */
    public static int getSpawnedCount(String type) {
        return SPAWNED_SPECIAL_ISLANDS.getOrDefault(type.toLowerCase(), new HashSet<>()).size();
    }
    
    /**
     * Reset special island tracking (for testing or world reset)
     */
    public static void resetTracking() {
        SPAWNED_SPECIAL_ISLANDS.values().forEach(Set::clear);
        LOGGER.info("Special island tracking reset");
    }
    
    /**
     * Check if a special island can spawn based on global limits
     */
    public static boolean canSpawnSpecialIsland(IslandManager.IslandType type) {
        String typeKey = type.name().toLowerCase();
        int currentCount = getSpawnedCount(typeKey);
        
        // Get limits from config
        int maxLimit = switch (type) {
            case BOSS -> AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.max_boss_islands_per_world : 3;
            case TREASURE -> AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.max_treasure_islands_per_world : 8;
            case MYSTICAL -> AethelonConfig.INSTANCE != null ? AethelonConfig.INSTANCE.max_mystical_islands_per_world : 5;
            default -> 10;
        };
        
        return currentCount < maxLimit;
    }
}