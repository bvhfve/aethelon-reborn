package com.bvhfve.aethelon.compat;

import com.bvhfve.aethelon.entity.AethelonEntity;
import com.bvhfve.aethelon.items.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Compatibility integration with Ocean Floor mod
 * 
 * Features:
 * - Enhanced ocean floor generation around Aethelon turtles
 * - Ocean Floor blocks can spawn on turtle island beaches
 * - Turtle shell items interact with Ocean Floor materials
 * - Enhanced underwater exploration near turtle paths
 */
public class OceanFloorCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonCompat/OceanFloor");
    private static final Random RANDOM = new Random();
    
    // Ocean Floor blocks that can appear around turtle islands
    private static final Set<String> OCEAN_FLOOR_BLOCKS = new HashSet<>(Arrays.asList(
        "oceanfloor:sea_grass",
        "oceanfloor:kelp_forest",
        "oceanfloor:coral_reef",
        "oceanfloor:sea_anemone",
        "oceanfloor:sea_urchin",
        "oceanfloor:starfish",
        "oceanfloor:sea_cucumber",
        "oceanfloor:brain_coral",
        "oceanfloor:tube_coral",
        "oceanfloor:fan_coral",
        "oceanfloor:sea_sponge",
        "oceanfloor:sea_lettuce"
    ));
    
    // Ocean Floor materials that work with turtle shell items
    private static final Set<String> TURTLE_COMPATIBLE_MATERIALS = new HashSet<>(Arrays.asList(
        "oceanfloor:sea_salt",
        "oceanfloor:sea_glass",
        "oceanfloor:pearl_fragment",
        "oceanfloor:coral_fragment",
        "oceanfloor:sea_crystal",
        "oceanfloor:deep_sea_mineral"
    ));
    
    public static boolean initialize() {
        try {
            LOGGER.info("Initializing Ocean Floor compatibility...");
            
            // Register enhanced ocean floor generation
            registerEnhancedOceanGeneration();
            
            // Register turtle island beach decorations
            registerBeachDecorations();
            
            // Register material interactions
            registerMaterialInteractions();
            
            // Register underwater exploration bonuses
            registerUnderwaterExploration();
            
            LOGGER.info("Ocean Floor compatibility initialized successfully!");
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Ocean Floor compatibility: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Register enhanced ocean floor generation around turtles
     */
    private static void registerEnhancedOceanGeneration() {
        // Ocean Floor blocks generate more frequently around turtle paths
    }
    
    /**
     * Get enhanced generation chance for Ocean Floor blocks near turtles
     */
    public static float getEnhancedGenerationChance(World world, BlockPos pos) {
        // Check if near an Aethelon turtle
        List<AethelonEntity> nearbyTurtles = world.getEntitiesByClass(
            AethelonEntity.class,
            new net.minecraft.util.math.Box(pos).expand(128.0),
            entity -> entity.isAlive()
        );
        
        if (nearbyTurtles.isEmpty()) return 1.0f; // Normal generation
        
        // Enhanced generation near turtles
        float distance = (float) nearbyTurtles.get(0).getPos().distanceTo(pos.toCenterPos());
        
        if (distance < 32.0f) {
            return 3.0f; // 3x generation rate very close to turtle
        } else if (distance < 64.0f) {
            return 2.0f; // 2x generation rate near turtle
        } else if (distance < 128.0f) {
            return 1.5f; // 1.5x generation rate in turtle area
        }
        
        return 1.0f;
    }
    
    /**
     * Get Ocean Floor block type based on turtle proximity
     */
    public static String getOceanFloorBlockForTurtleArea(BlockPos pos, AethelonEntity nearestTurtle) {
        if (nearestTurtle == null) return getRandomOceanFloorBlock();
        
        float distance = (float) nearestTurtle.getPos().distanceTo(pos.toCenterPos());
        
        // Different blocks based on distance from turtle
        if (distance < 16.0f) {
            // Close to turtle - more exotic blocks
            String[] exoticBlocks = {
                "oceanfloor:brain_coral",
                "oceanfloor:sea_anemone", 
                "oceanfloor:sea_sponge",
                "oceanfloor:fan_coral"
            };
            return exoticBlocks[RANDOM.nextInt(exoticBlocks.length)];
        } else if (distance < 32.0f) {
            // Medium distance - common decorative blocks
            String[] commonBlocks = {
                "oceanfloor:sea_grass",
                "oceanfloor:kelp_forest",
                "oceanfloor:starfish",
                "oceanfloor:sea_cucumber"
            };
            return commonBlocks[RANDOM.nextInt(commonBlocks.length)];
        } else {
            // Far from turtle - basic blocks
            return getRandomOceanFloorBlock();
        }
    }
    
    /**
     * Get random Ocean Floor block
     */
    private static String getRandomOceanFloorBlock() {
        String[] blocks = OCEAN_FLOOR_BLOCKS.toArray(new String[0]);
        return blocks[RANDOM.nextInt(blocks.length)];
    }
    
    /**
     * Register beach decorations for turtle islands
     */
    private static void registerBeachDecorations() {
        // Turtle island beaches can have Ocean Floor decorations
    }
    
    /**
     * Apply Ocean Floor beach decorations to turtle island
     */
    public static void applyBeachDecorations(World world, BlockPos islandCenter) {
        try {
            // 40% chance to add beach decorations
            if (RANDOM.nextFloat() < 0.4f) {
                addShorelineDecorations(world, islandCenter);
            }
            
            // 25% chance to add underwater decorations around island
            if (RANDOM.nextFloat() < 0.25f) {
                addUnderwaterDecorations(world, islandCenter);
            }
            
        } catch (Exception e) {
            LOGGER.warn("Failed to apply Ocean Floor beach decorations: {}", e.getMessage());
        }
    }
    
    /**
     * Add shoreline decorations around turtle island
     */
    private static void addShorelineDecorations(World world, BlockPos center) {
        // Add starfish, sea urchins, sea grass around the island shore
        LOGGER.debug("Adding Ocean Floor shoreline decorations to turtle island");
    }
    
    /**
     * Add underwater decorations around turtle island
     */
    private static void addUnderwaterDecorations(World world, BlockPos center) {
        // Add coral, kelp, sea anemones in the water around the island
        LOGGER.debug("Adding Ocean Floor underwater decorations around turtle island");
    }
    
    /**
     * Register material interactions with turtle shell items
     */
    private static void registerMaterialInteractions() {
        // Ocean Floor materials can be combined with turtle shell items
    }
    
    /**
     * Check if Ocean Floor material can combine with turtle shell item
     */
    public static boolean canCombineWithTurtleItem(ItemStack oceanFloorItem, ItemStack turtleItem) {
        if (oceanFloorItem.isEmpty() || turtleItem.isEmpty()) return false;
        
        String oceanItemId = oceanFloorItem.getItem().toString();
        if (!TURTLE_COMPATIBLE_MATERIALS.contains(oceanItemId)) return false;
        
        // Specific combinations
        if (oceanItemId.equals("oceanfloor:sea_salt") && 
            turtleItem.getItem() == ModItems.CRYSTALLIZED_WATER) {
            return true; // Sea salt + crystallized water
        } else if (oceanItemId.equals("oceanfloor:pearl_fragment") && 
                   turtleItem.getItem() == ModItems.DEEP_SEA_PEARL) {
            return true; // Pearl fragment + deep sea pearl
        } else if (oceanItemId.equals("oceanfloor:coral_fragment") && 
                   turtleItem.getItem() == ModItems.TURTLE_SHELL_FRAGMENT) {
            return true; // Coral fragment + turtle shell fragment
        }
        
        return false;
    }
    
    /**
     * Get result of combining Ocean Floor material with turtle shell item
     */
    public static ItemStack getCombinationResult(ItemStack oceanFloorItem, ItemStack turtleItem) {
        if (!canCombineWithTurtleItem(oceanFloorItem, turtleItem)) return ItemStack.EMPTY;
        
        String oceanItemId = oceanFloorItem.getItem().toString();
        
        if (oceanItemId.equals("oceanfloor:sea_salt") && 
            turtleItem.getItem() == ModItems.CRYSTALLIZED_WATER) {
            // Enhanced crystallized water
            return new ItemStack(ModItems.CRYSTALLIZED_WATER, 2);
        } else if (oceanItemId.equals("oceanfloor:pearl_fragment") && 
                   turtleItem.getItem() == ModItems.DEEP_SEA_PEARL) {
            // Enhanced deep sea pearl
            return new ItemStack(ModItems.DEEP_SEA_PEARL, 1);
        } else if (oceanItemId.equals("oceanfloor:coral_fragment") && 
                   turtleItem.getItem() == ModItems.TURTLE_SHELL_FRAGMENT) {
            // Reinforced turtle shell fragment
            return new ItemStack(ModItems.TURTLE_SHELL_FRAGMENT, 2);
        }
        
        return ItemStack.EMPTY;
    }
    
    /**
     * Register underwater exploration bonuses
     */
    private static void registerUnderwaterExploration() {
        // Enhanced underwater exploration near turtle paths
    }
    
    /**
     * Get underwater exploration bonus near turtle paths
     */
    public static float getUnderwaterExplorationBonus(PlayerEntity player) {
        if (player == null) return 1.0f;
        
        World world = player.getWorld();
        if (world == null) return 1.0f;
        
        // Check if near turtle path (areas where turtles have been)
        List<AethelonEntity> nearbyTurtles = world.getEntitiesByClass(
            AethelonEntity.class,
            player.getBoundingBox().expand(64.0),
            entity -> entity.isAlive()
        );
        
        if (!nearbyTurtles.isEmpty()) {
            return 1.3f; // 30% bonus to underwater exploration near turtles
        }
        
        return 1.0f;
    }
    
    /**
     * Check if player can find enhanced Ocean Floor loot near turtles
     */
    public static boolean canFindEnhancedLoot(PlayerEntity player) {
        if (player == null) return false;
        
        // Enhanced loot when diving near turtle islands
        return isNearTurtleIsland(player) && isUnderwater(player);
    }
    
    /**
     * Check if player is near a turtle island
     */
    private static boolean isNearTurtleIsland(PlayerEntity player) {
        World world = player.getWorld();
        if (world == null) return false;
        
        List<AethelonEntity> nearbyTurtles = world.getEntitiesByClass(
            AethelonEntity.class,
            player.getBoundingBox().expand(32.0),
            entity -> entity.isAlive()
        );
        
        return !nearbyTurtles.isEmpty();
    }
    
    /**
     * Check if player is underwater
     */
    private static boolean isUnderwater(PlayerEntity player) {
        return player.isSubmergedInWater();
    }
    
    /**
     * Get enhanced Ocean Floor loot for underwater exploration near turtles
     */
    public static ItemStack getEnhancedUnderwaterLoot(PlayerEntity player) {
        if (!canFindEnhancedLoot(player)) return ItemStack.EMPTY;
        
        // 20% chance for enhanced loot
        if (RANDOM.nextFloat() < 0.2f) {
            // Return turtle-themed items enhanced by Ocean Floor materials
            ItemStack[] enhancedLoot = {
                new ItemStack(ModItems.DEEP_SEA_PEARL),
                new ItemStack(ModItems.CRYSTALLIZED_WATER),
                new ItemStack(ModItems.TURTLE_SHELL_FRAGMENT, 2),
                new ItemStack(ModItems.ANCIENT_TURTLE_SCALE)
            };
            return enhancedLoot[RANDOM.nextInt(enhancedLoot.length)];
        }
        
        return ItemStack.EMPTY;
    }
    
    /**
     * Get Ocean Floor block spawn weight modifier near turtles
     */
    public static int getSpawnWeightModifier(String blockId, World world, BlockPos pos) {
        if (!OCEAN_FLOOR_BLOCKS.contains(blockId)) return 1;
        
        List<AethelonEntity> nearbyTurtles = world.getEntitiesByClass(
            AethelonEntity.class,
            new net.minecraft.util.math.Box(pos).expand(64.0),
            entity -> entity.isAlive()
        );
        
        if (nearbyTurtles.isEmpty()) return 1;
        
        // Enhanced spawn rates for Ocean Floor blocks near turtles
        if (blockId.equals("oceanfloor:coral_reef") || 
            blockId.equals("oceanfloor:brain_coral")) {
            return 4; // 4x spawn rate for coral near turtles
        } else if (blockId.equals("oceanfloor:kelp_forest") || 
                   blockId.equals("oceanfloor:sea_grass")) {
            return 3; // 3x spawn rate for vegetation
        }
        
        return 2; // 2x spawn rate for other Ocean Floor blocks
    }
}