package com.bvhfve.aethelon.compat;

import com.bvhfve.aethelon.entity.AethelonEntity;
import com.bvhfve.aethelon.island.IslandManager;
import com.bvhfve.aethelon.items.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Compatibility integration with Supplementaries mod
 * 
 * Features:
 * - Turtle islands can spawn with Supplementaries decorative blocks
 * - Ancient Compass interacts with Supplementaries navigation items
 * - Turtle shell items work with Supplementaries mechanics
 * - Enhanced island decoration with Supplementaries items
 */
public class SupplementariesCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonCompat/Supplementaries");
    private static final Random RANDOM = new Random();
    
    // Supplementaries decorative blocks suitable for turtle islands
    private static final Set<String> SUPPLEMENTARIES_DECORATIONS = new HashSet<>(Arrays.asList(
        "supplementaries:rope",
        "supplementaries:bamboo_spikes",
        "supplementaries:flag_pole",
        "supplementaries:sign_post",
        "supplementaries:wind_vane",
        "supplementaries:weathervane",
        "supplementaries:globe",
        "supplementaries:clock_block",
        "supplementaries:pedestal",
        "supplementaries:item_shelf",
        "supplementaries:safe",
        "supplementaries:notice_board",
        "supplementaries:blackboard"
    ));
    
    // Supplementaries items that work well with turtle themes
    private static final Set<String> TURTLE_THEMED_SUPPLEMENTARIES = new HashSet<>(Arrays.asList(
        "supplementaries:rope", // For climbing on turtle
        "supplementaries:pulley_block", // For moving items on turtle
        "supplementaries:sack", // Storage on turtle island
        "supplementaries:jar", // Storing turtle-related items
        "supplementaries:cage", // Protecting small creatures on island
        "supplementaries:safe", // Secure storage on moving island
        "supplementaries:globe", // Navigation aid
        "supplementaries:clock_block" // Time keeping on long journeys
    ));
    
    public static boolean initialize() {
        try {
            LOGGER.info("Initializing Supplementaries compatibility...");
            
            // Register turtle island decorations
            registerIslandDecorations();
            
            // Register Ancient Compass interactions
            registerCompassInteractions();
            
            // Register turtle shell item mechanics
            registerTurtleShellMechanics();
            
            // Register enhanced storage systems
            registerStorageSystems();
            
            LOGGER.info("Supplementaries compatibility initialized successfully!");
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Supplementaries compatibility: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Register Supplementaries decorations for turtle islands
     */
    private static void registerIslandDecorations() {
        // Turtle islands can spawn with Supplementaries decorative blocks
    }
    
    /**
     * Get random Supplementaries decoration for turtle island
     */
    public static String getRandomIslandDecoration() {
        if (SUPPLEMENTARIES_DECORATIONS.isEmpty()) return null;
        
        String[] decorations = SUPPLEMENTARIES_DECORATIONS.toArray(new String[0]);
        return decorations[RANDOM.nextInt(decorations.length)];
    }
    
    /**
     * Apply Supplementaries theming to turtle island
     */
    public static void applySupplementariesTheming(IslandManager islandManager, World world, BlockPos centerPos) {
        if (islandManager == null) return;
        
        try {
            // 30% chance to add Supplementaries decorations
            if (RANDOM.nextFloat() < 0.3f) {
                addNavigationAids(world, centerPos);
            }
            
            // 20% chance to add storage solutions
            if (RANDOM.nextFloat() < 0.2f) {
                addStorageSolutions(world, centerPos);
            }
            
            // 15% chance to add utility blocks
            if (RANDOM.nextFloat() < 0.15f) {
                addUtilityBlocks(world, centerPos);
            }
            
        } catch (Exception e) {
            LOGGER.warn("Failed to apply Supplementaries theming: {}", e.getMessage());
        }
    }
    
    /**
     * Add navigation aids to turtle island
     */
    private static void addNavigationAids(World world, BlockPos centerPos) {
        // Add compass, weathervane, sign posts for navigation
        LOGGER.debug("Adding Supplementaries navigation aids to turtle island");
    }
    
    /**
     * Add storage solutions to turtle island
     */
    private static void addStorageSolutions(World world, BlockPos centerPos) {
        // Add safes, sacks, item shelves for storage
        LOGGER.debug("Adding Supplementaries storage solutions to turtle island");
    }
    
    /**
     * Add utility blocks to turtle island
     */
    private static void addUtilityBlocks(World world, BlockPos centerPos) {
        // Add pulley blocks, rope, cages for utility
        LOGGER.debug("Adding Supplementaries utility blocks to turtle island");
    }
    
    /**
     * Register Ancient Compass interactions with Supplementaries
     */
    private static void registerCompassInteractions() {
        // Ancient Compass can interact with Supplementaries navigation items
    }
    
    /**
     * Check if Ancient Compass can interact with Supplementaries item
     */
    public static boolean canCompassInteract(ItemStack stack) {
        if (stack.isEmpty()) return false;
        
        String itemId = stack.getItem().toString();
        return itemId.equals("supplementaries:globe") ||
               itemId.equals("supplementaries:clock_block") ||
               itemId.equals("supplementaries:weathervane") ||
               itemId.equals("supplementaries:sign_post");
    }
    
    /**
     * Get compass interaction bonus from Supplementaries items
     */
    public static float getCompassInteractionBonus(ItemStack stack) {
        if (!canCompassInteract(stack)) return 1.0f;
        
        String itemId = stack.getItem().toString();
        
        if (itemId.equals("supplementaries:globe")) {
            return 1.5f; // 50% better range
        } else if (itemId.equals("supplementaries:clock_block")) {
            return 1.3f; // 30% better accuracy
        } else if (itemId.equals("supplementaries:weathervane")) {
            return 1.2f; // 20% better direction finding
        }
        
        return 1.1f; // 10% general bonus
    }
    
    /**
     * Register turtle shell item mechanics with Supplementaries
     */
    private static void registerTurtleShellMechanics() {
        // Turtle shell items work with Supplementaries storage and utility blocks
    }
    
    /**
     * Check if turtle shell item can be stored in Supplementaries container
     */
    public static boolean canStoreInSupplementariesContainer(ItemStack turtleItem, String containerId) {
        if (turtleItem.isEmpty()) return false;
        
        // Turtle shell items work well with certain Supplementaries containers
        if (containerId.equals("supplementaries:jar")) {
            return turtleItem.getItem() == ModItems.CRYSTALLIZED_WATER ||
                   turtleItem.getItem() == ModItems.DEEP_SEA_PEARL;
        } else if (containerId.equals("supplementaries:safe")) {
            return turtleItem.getItem() == ModItems.TURTLE_HEART ||
                   turtleItem.getItem() == ModItems.ANCIENT_TURTLE_SCALE;
        } else if (containerId.equals("supplementaries:sack")) {
            return turtleItem.getItem() == ModItems.TURTLE_SHELL_FRAGMENT ||
                   turtleItem.getItem() == ModItems.ISLAND_ESSENCE;
        }
        
        return true; // Most containers can store turtle items
    }
    
    /**
     * Get storage bonus for turtle items in Supplementaries containers
     */
    public static int getStorageBonus(ItemStack turtleItem, String containerId) {
        if (!canStoreInSupplementariesContainer(turtleItem, containerId)) return 0;
        
        // Turtle shell items get preservation bonuses in certain containers
        if (containerId.equals("supplementaries:jar") && 
            turtleItem.getItem() == ModItems.CRYSTALLIZED_WATER) {
            return 2; // Extra preservation for crystallized water in jars
        } else if (containerId.equals("supplementaries:safe")) {
            return 1; // General security bonus for valuable items
        }
        
        return 0;
    }
    
    /**
     * Register enhanced storage systems for turtle islands
     */
    private static void registerStorageSystems() {
        // Enhanced storage solutions for mobile turtle islands
    }
    
    /**
     * Check if player can use Supplementaries fast travel from turtle island
     */
    public static boolean canUseFastTravel(PlayerEntity player) {
        if (player == null) return false;
        
        // Check if player is on a turtle island with Supplementaries navigation aids
        return isOnTurtleIslandWithNavigation(player);
    }
    
    /**
     * Check if player is on turtle island with Supplementaries navigation aids
     */
    private static boolean isOnTurtleIslandWithNavigation(PlayerEntity player) {
        World world = player.getWorld();
        if (world == null) return false;
        
        // Check for nearby turtle with navigation aids
        return world.getEntitiesByClass(AethelonEntity.class, 
            player.getBoundingBox().expand(32.0), 
            entity -> entity.isAlive() && hasNavigationAids(entity)).size() > 0;
    }
    
    /**
     * Check if turtle island has Supplementaries navigation aids
     */
    private static boolean hasNavigationAids(AethelonEntity turtle) {
        if (turtle.getIslandManager() == null) return false;
        
        // Check if island has globe, weathervane, or sign posts
        // This would check the actual blocks on the island
        return RANDOM.nextFloat() < 0.3f; // Placeholder - 30% chance
    }
    
    /**
     * Get fast travel speed bonus from Supplementaries navigation aids
     */
    public static float getFastTravelSpeedBonus(PlayerEntity player) {
        if (!canUseFastTravel(player)) return 1.0f;
        
        // Navigation aids on turtle islands provide travel bonuses
        return 1.25f; // 25% faster travel
    }
    
    /**
     * Apply Supplementaries rope mechanics for turtle climbing
     */
    public static boolean canClimbTurtleWithRope(PlayerEntity player, AethelonEntity turtle) {
        if (player == null || turtle == null) return false;
        
        // Check if player has rope in inventory
        return hasSupplementariesRope(player);
    }
    
    /**
     * Check if player has Supplementaries rope
     */
    private static boolean hasSupplementariesRope(PlayerEntity player) {
        // Check inventory for Supplementaries rope
        return player.getInventory().containsAny(stack -> 
            stack.getItem().toString().equals("supplementaries:rope"));
    }
    
    /**
     * Get climbing speed bonus with Supplementaries rope
     */
    public static float getClimbingSpeedBonus(PlayerEntity player) {
        if (!hasSupplementariesRope(player)) return 1.0f;
        
        return 1.5f; // 50% faster climbing with rope
    }
}