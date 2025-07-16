package com.bvhfve.aethelon.compat;

import com.bvhfve.aethelon.entity.AethelonEntity;
import com.bvhfve.aethelon.items.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Compatibility integration with Waystones mod
 * 
 * Features:
 * - Turtle islands can have special waystones that move with the turtle
 * - Ancient Compass can locate nearby waystones
 * - Turtle Shell items can be used to craft special waystones
 * - Mobile waystones on turtle backs for oceanic travel
 */
public class WaystonesCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonCompat/Waystones");
    
    // Track mobile waystones on turtle islands
    private static final Map<UUID, MobileWaystone> mobileWaystones = new HashMap<>();
    
    /**
     * Initialize Waystones compatibility
     */
    public static boolean initialize() {
        try {
            LOGGER.info("Initializing Waystones compatibility...");
            
            // Register mobile waystone mechanics
            registerMobileWaystones();
            
            // Register Ancient Compass waystone detection
            registerCompassWaystoneDetection();
            
            // Register turtle shell waystone crafting
            registerTurtleShellWaystoneCrafting();
            
            // Register oceanic travel features
            registerOceanicTravelFeatures();
            
            LOGGER.info("Waystones compatibility initialized successfully!");
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Waystones compatibility: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Register mobile waystone mechanics for turtle islands
     */
    private static void registerMobileWaystones() {
        // Mobile waystones move with turtle islands
    }
    
    /**
     * Create a mobile waystone on a turtle island
     */
    public static boolean createMobileWaystone(AethelonEntity turtle, BlockPos relativePos, String name) {
        if (turtle == null || turtle.getIslandManager() == null) return false;
        
        try {
            UUID turtleId = turtle.getUuid();
            MobileWaystone waystone = new MobileWaystone(turtleId, relativePos, name);
            mobileWaystones.put(turtleId, waystone);
            
            LOGGER.info("Created mobile waystone '{}' on turtle {}", name, turtleId);
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to create mobile waystone: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Get the current world position of a mobile waystone
     */
    public static BlockPos getMobileWaystonePosition(UUID turtleId) {
        MobileWaystone waystone = mobileWaystones.get(turtleId);
        if (waystone == null) return null;
        
        // Find the turtle and calculate absolute position
        // This would need to be implemented with proper world access
        return null; // Placeholder
    }
    
    /**
     * Check if a turtle has a mobile waystone
     */
    public static boolean hasMobileWaystone(AethelonEntity turtle) {
        if (turtle == null) return false;
        return mobileWaystones.containsKey(turtle.getUuid());
    }
    
    /**
     * Register Ancient Compass waystone detection
     */
    private static void registerCompassWaystoneDetection() {
        // Ancient Compass can point to nearest waystone
    }
    
    /**
     * Find nearest waystone for Ancient Compass
     */
    public static BlockPos findNearestWaystone(PlayerEntity player) {
        if (player == null || player.getWorld() == null) return null;
        
        World world = player.getWorld();
        BlockPos playerPos = player.getBlockPos();
        
        // Check for mobile waystones first
        BlockPos nearestMobile = findNearestMobileWaystone(world, playerPos);
        if (nearestMobile != null) {
            return nearestMobile;
        }
        
        // Then check for regular waystones (would need Waystones API)
        return findNearestRegularWaystone(world, playerPos);
    }
    
    /**
     * Find nearest mobile waystone
     */
    private static BlockPos findNearestMobileWaystone(World world, BlockPos playerPos) {
        BlockPos nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        
        // Check all turtles with mobile waystones
        List<AethelonEntity> turtles = world.getEntitiesByClass(
            AethelonEntity.class,
            new net.minecraft.util.math.Box(playerPos).expand(1000.0),
            entity -> entity.isAlive() && hasMobileWaystone(entity)
        );
        
        for (AethelonEntity turtle : turtles) {
            BlockPos waystonePos = getMobileWaystonePosition(turtle.getUuid());
            if (waystonePos != null) {
                double distance = playerPos.getSquaredDistance(waystonePos);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearest = waystonePos;
                }
            }
        }
        
        return nearest;
    }
    
    /**
     * Find nearest regular waystone (placeholder - would use Waystones API)
     */
    private static BlockPos findNearestRegularWaystone(World world, BlockPos playerPos) {
        // This would integrate with the actual Waystones mod API
        // For now, return null as placeholder
        return null;
    }
    
    /**
     * Register turtle shell waystone crafting recipes
     */
    private static void registerTurtleShellWaystoneCrafting() {
        // Turtle shell items can be used to craft special waystones
    }
    
    /**
     * Check if item can be used for waystone crafting
     */
    public static boolean canCraftWaystone(ItemStack stack) {
        return stack.getItem() == ModItems.ANCIENT_TURTLE_SCALE ||
               stack.getItem() == ModItems.TURTLE_HEART ||
               stack.getItem() == ModItems.ISLAND_ESSENCE;
    }
    
    /**
     * Get waystone type that can be crafted with turtle shell item
     */
    public static WaystoneType getWaystoneType(ItemStack stack) {
        if (stack.getItem() == ModItems.TURTLE_HEART) {
            return WaystoneType.OCEANIC_WAYSTONE; // Most powerful
        } else if (stack.getItem() == ModItems.ANCIENT_TURTLE_SCALE) {
            return WaystoneType.MOBILE_WAYSTONE; // Can be placed on turtle islands
        } else if (stack.getItem() == ModItems.ISLAND_ESSENCE) {
            return WaystoneType.ISLAND_WAYSTONE; // Enhanced range over water
        }
        return WaystoneType.NORMAL;
    }
    
    /**
     * Register oceanic travel features
     */
    private static void registerOceanicTravelFeatures() {
        // Special teleportation mechanics for ocean travel
    }
    
    /**
     * Check if player can use oceanic teleportation
     */
    public static boolean canUseOceanicTeleport(PlayerEntity player, BlockPos destination) {
        if (player == null || destination == null) return false;
        
        World world = player.getWorld();
        if (world == null) return false;
        
        // Check if destination is over water or on a turtle island
        return isOverWaterOrTurtleIsland(world, destination);
    }
    
    /**
     * Check if position is over water or on a turtle island
     */
    private static boolean isOverWaterOrTurtleIsland(World world, BlockPos pos) {
        // Check if on turtle island
        List<AethelonEntity> nearbyTurtles = world.getEntitiesByClass(
            AethelonEntity.class,
            new net.minecraft.util.math.Box(pos).expand(32.0),
            entity -> entity.isAlive()
        );
        
        for (AethelonEntity turtle : nearbyTurtles) {
            if (turtle.getIslandManager() != null && 
                turtle.getIslandManager().isPositionOnIsland(pos.toCenterPos())) {
                return true;
            }
        }
        
        // Check if over water
        return world.getBlockState(pos.down()).getBlock().toString().contains("water");
    }
    
    /**
     * Apply oceanic teleportation with special effects
     */
    public static boolean performOceanicTeleport(ServerPlayerEntity player, BlockPos destination) {
        if (!canUseOceanicTeleport(player, destination)) return false;
        
        try {
            // Add special water-themed teleportation effects
            player.sendMessage(Text.literal("The ocean currents carry you swiftly to your destination..."), false);
            
            // Perform teleportation (would use Waystones API)
            // For now, just a placeholder
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to perform oceanic teleport: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Get teleportation cost modifier for oceanic travel
     */
    public static float getOceanicTeleportCostModifier(PlayerEntity player, BlockPos destination) {
        if (!canUseOceanicTeleport(player, destination)) return 1.0f;
        
        // Reduced cost for oceanic teleportation
        float modifier = 0.7f;
        
        // Further reduction if player has turtle shell armor
        int turtleArmorPieces = 0;
        if (player.getInventory().getArmorStack(3).getItem() == ModItems.TURTLE_SHELL_HELMET) turtleArmorPieces++;
        if (player.getInventory().getArmorStack(2).getItem() == ModItems.TURTLE_SHELL_CHESTPLATE) turtleArmorPieces++;
        if (player.getInventory().getArmorStack(1).getItem() == ModItems.TURTLE_SHELL_LEGGINGS) turtleArmorPieces++;
        if (player.getInventory().getArmorStack(0).getItem() == ModItems.TURTLE_SHELL_BOOTS) turtleArmorPieces++;
        
        modifier -= (turtleArmorPieces * 0.05f); // 5% reduction per armor piece
        
        return Math.max(modifier, 0.3f); // Minimum 30% of original cost
    }
    
    /**
     * Mobile waystone data class
     */
    private static class MobileWaystone {
        private final UUID turtleId;
        private final BlockPos relativePosition;
        private final String name;
        
        public MobileWaystone(UUID turtleId, BlockPos relativePosition, String name) {
            this.turtleId = turtleId;
            this.relativePosition = relativePosition;
            this.name = name;
        }
        
        public UUID getTurtleId() { return turtleId; }
        public BlockPos getRelativePosition() { return relativePosition; }
        public String getName() { return name; }
    }
    
    /**
     * Waystone types for turtle shell crafting
     */
    public enum WaystoneType {
        NORMAL,
        MOBILE_WAYSTONE,
        ISLAND_WAYSTONE,
        OCEANIC_WAYSTONE
    }
}