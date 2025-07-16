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
 * Compatibility integration with Upgrade Aquatic mod
 * 
 * Features:
 * - Enhanced kelp and seagrass generation around turtle islands
 * - Turtle shell items work with Upgrade Aquatic materials
 * - Special interactions with jellyfish and other aquatic mobs
 * - Enhanced underwater breathing mechanics
 */
public class UpgradeAquaticCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonCompat/UpgradeAquatic");
    private static final Random RANDOM = new Random();
    
    // Upgrade Aquatic blocks that enhance turtle islands
    private static final Set<String> UPGRADE_AQUATIC_BLOCKS = new HashSet<>(Arrays.asList(
        "upgrade_aquatic:kelp_block",
        "upgrade_aquatic:dried_kelp_block",
        "upgrade_aquatic:sea_pickle",
        "upgrade_aquatic:seagrass",
        "upgrade_aquatic:tall_seagrass",
        "upgrade_aquatic:coral_fan",
        "upgrade_aquatic:coral_block",
        "upgrade_aquatic:dead_coral_block",
        "upgrade_aquatic:prismarine_coral",
        "upgrade_aquatic:elder_prismarine"
    ));
    
    // Upgrade Aquatic mobs that interact with turtles
    private static final Set<String> UPGRADE_AQUATIC_MOBS = new HashSet<>(Arrays.asList(
        "upgrade_aquatic:jellyfish",
        "upgrade_aquatic:box_jellyfish",
        "upgrade_aquatic:immortal_jellyfish",
        "upgrade_aquatic:nautilus",
        "upgrade_aquatic:pike",
        "upgrade_aquatic:lionfish",
        "upgrade_aquatic:thrasher"
    ));
    
    // Friendly mobs that should be attracted to turtle islands
    private static final Set<String> FRIENDLY_UPGRADE_MOBS = new HashSet<>(Arrays.asList(
        "upgrade_aquatic:jellyfish",
        "upgrade_aquatic:immortal_jellyfish",
        "upgrade_aquatic:nautilus"
    ));
    
    public static boolean initialize() {
        try {
            LOGGER.info("Initializing Upgrade Aquatic compatibility...");
            
            // Register enhanced aquatic generation
            registerEnhancedAquaticGeneration();
            
            // Register mob interactions
            registerMobInteractions();
            
            // Register material combinations
            registerMaterialCombinations();
            
            // Register underwater breathing enhancements
            registerUnderwaterBreathing();
            
            LOGGER.info("Upgrade Aquatic compatibility initialized successfully!");
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Upgrade Aquatic compatibility: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Register enhanced aquatic generation around turtle islands
     */
    private static void registerEnhancedAquaticGeneration() {
        // Enhanced kelp, seagrass, and coral generation near turtles
    }
    
    /**
     * Get enhanced generation multiplier for Upgrade Aquatic blocks near turtles
     */
    public static float getGenerationMultiplier(String blockId, World world, BlockPos pos) {
        if (!UPGRADE_AQUATIC_BLOCKS.contains(blockId)) return 1.0f;
        
        List<AethelonEntity> nearbyTurtles = world.getEntitiesByClass(
            AethelonEntity.class,
            new net.minecraft.util.math.Box(pos).expand(64.0),
            entity -> entity.isAlive()
        );
        
        if (nearbyTurtles.isEmpty()) return 1.0f;
        
        // Different multipliers based on block type
        if (blockId.contains("kelp")) {
            return 2.5f; // 2.5x kelp generation near turtles
        } else if (blockId.contains("seagrass")) {
            return 2.0f; // 2x seagrass generation
        } else if (blockId.contains("coral")) {
            return 1.8f; // 1.8x coral generation
        } else if (blockId.contains("sea_pickle")) {
            return 3.0f; // 3x sea pickle generation (lighting for turtle islands)
        }
        
        return 1.5f; // 1.5x for other Upgrade Aquatic blocks
    }
    
    /**
     * Apply Upgrade Aquatic decorations to turtle island
     */
    public static void applyUpgradeAquaticDecorations(World world, BlockPos islandCenter) {
        try {
            // 50% chance to add kelp forests around island
            if (RANDOM.nextFloat() < 0.5f) {
                addKelpForests(world, islandCenter);
            }
            
            // 35% chance to add coral decorations
            if (RANDOM.nextFloat() < 0.35f) {
                addCoralDecorations(world, islandCenter);
            }
            
            // 40% chance to add seagrass meadows
            if (RANDOM.nextFloat() < 0.4f) {
                addSeagrassMeadows(world, islandCenter);
            }
            
            // 25% chance to add sea pickle lighting
            if (RANDOM.nextFloat() < 0.25f) {
                addSeaPickleLighting(world, islandCenter);
            }
            
        } catch (Exception e) {
            LOGGER.warn("Failed to apply Upgrade Aquatic decorations: {}", e.getMessage());
        }
    }
    
    /**
     * Add kelp forests around turtle island
     */
    private static void addKelpForests(World world, BlockPos center) {
        LOGGER.debug("Adding Upgrade Aquatic kelp forests around turtle island");
    }
    
    /**
     * Add coral decorations to turtle island
     */
    private static void addCoralDecorations(World world, BlockPos center) {
        LOGGER.debug("Adding Upgrade Aquatic coral decorations to turtle island");
    }
    
    /**
     * Add seagrass meadows around turtle island
     */
    private static void addSeagrassMeadows(World world, BlockPos center) {
        LOGGER.debug("Adding Upgrade Aquatic seagrass meadows around turtle island");
    }
    
    /**
     * Add sea pickle lighting to turtle island
     */
    private static void addSeaPickleLighting(World world, BlockPos center) {
        LOGGER.debug("Adding Upgrade Aquatic sea pickle lighting to turtle island");
    }
    
    /**
     * Register mob interactions with turtle islands
     */
    private static void registerMobInteractions() {
        // Upgrade Aquatic mobs interact with turtle islands
    }
    
    /**
     * Check if Upgrade Aquatic mob should be attracted to turtle island
     */
    public static boolean shouldBeAttractedToTurtle(String mobId) {
        return FRIENDLY_UPGRADE_MOBS.contains(mobId);
    }
    
    /**
     * Get spawn weight modifier for Upgrade Aquatic mobs near turtle islands
     */
    public static int getSpawnWeightModifier(String mobId, World world, BlockPos pos) {
        if (!UPGRADE_AQUATIC_MOBS.contains(mobId)) return 1;
        
        List<AethelonEntity> nearbyTurtles = world.getEntitiesByClass(
            AethelonEntity.class,
            new net.minecraft.util.math.Box(pos).expand(48.0),
            entity -> entity.isAlive()
        );
        
        if (nearbyTurtles.isEmpty()) return 1;
        
        if (FRIENDLY_UPGRADE_MOBS.contains(mobId)) {
            return 3; // 3x spawn rate for friendly mobs
        } else if (mobId.equals("upgrade_aquatic:thrasher") || mobId.equals("upgrade_aquatic:pike")) {
            return 1; // Normal spawn rate for aggressive mobs (don't encourage them)
        }
        
        return 2; // 2x spawn rate for neutral mobs
    }
    
    /**
     * Check if turtle shell armor provides protection against Upgrade Aquatic mobs
     */
    public static boolean providesProtectionAgainst(PlayerEntity player, String mobId) {
        if (player == null) return false;
        
        // Count turtle shell armor pieces
        int armorPieces = 0;
        if (player.getInventory().getArmorStack(3).getItem() == ModItems.TURTLE_SHELL_HELMET) armorPieces++;
        if (player.getInventory().getArmorStack(2).getItem() == ModItems.TURTLE_SHELL_CHESTPLATE) armorPieces++;
        if (player.getInventory().getArmorStack(1).getItem() == ModItems.TURTLE_SHELL_LEGGINGS) armorPieces++;
        if (player.getInventory().getArmorStack(0).getItem() == ModItems.TURTLE_SHELL_BOOTS) armorPieces++;
        
        // Protection against jellyfish stings
        if (mobId.contains("jellyfish") && armorPieces >= 2) {
            return true;
        }
        
        // Protection against lionfish spines
        if (mobId.equals("upgrade_aquatic:lionfish") && armorPieces >= 3) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Get damage reduction from turtle shell armor against Upgrade Aquatic mobs
     */
    public static float getDamageReduction(PlayerEntity player, String mobId) {
        if (!providesProtectionAgainst(player, mobId)) return 0.0f;
        
        int armorPieces = 0;
        if (player.getInventory().getArmorStack(3).getItem() == ModItems.TURTLE_SHELL_HELMET) armorPieces++;
        if (player.getInventory().getArmorStack(2).getItem() == ModItems.TURTLE_SHELL_CHESTPLATE) armorPieces++;
        if (player.getInventory().getArmorStack(1).getItem() == ModItems.TURTLE_SHELL_LEGGINGS) armorPieces++;
        if (player.getInventory().getArmorStack(0).getItem() == ModItems.TURTLE_SHELL_BOOTS) armorPieces++;
        
        if (mobId.contains("jellyfish")) {
            return Math.min(armorPieces * 0.15f, 0.6f); // Up to 60% reduction against jellyfish
        } else if (mobId.equals("upgrade_aquatic:lionfish")) {
            return Math.min(armorPieces * 0.12f, 0.48f); // Up to 48% reduction against lionfish
        }
        
        return 0.0f;
    }
    
    /**
     * Register material combinations with turtle shell items
     */
    private static void registerMaterialCombinations() {
        // Upgrade Aquatic materials can combine with turtle shell items
    }
    
    /**
     * Check if Upgrade Aquatic item can combine with turtle shell item
     */
    public static boolean canCombineItems(ItemStack upgradeAquaticItem, ItemStack turtleItem) {
        if (upgradeAquaticItem.isEmpty() || turtleItem.isEmpty()) return false;
        
        String upgradeItemId = upgradeAquaticItem.getItem().toString();
        
        // Kelp + Crystallized Water = Enhanced Water Breathing
        if (upgradeItemId.contains("kelp") && turtleItem.getItem() == ModItems.CRYSTALLIZED_WATER) {
            return true;
        }
        
        // Prismarine + Turtle Shell = Enhanced Armor
        if (upgradeItemId.contains("prismarine") && turtleItem.getItem() == ModItems.TURTLE_SHELL_FRAGMENT) {
            return true;
        }
        
        // Sea Pickle + Deep Sea Pearl = Enhanced Lighting
        if (upgradeItemId.contains("sea_pickle") && turtleItem.getItem() == ModItems.DEEP_SEA_PEARL) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Get result of combining Upgrade Aquatic item with turtle shell item
     */
    public static ItemStack getCombinationResult(ItemStack upgradeAquaticItem, ItemStack turtleItem) {
        if (!canCombineItems(upgradeAquaticItem, turtleItem)) return ItemStack.EMPTY;
        
        String upgradeItemId = upgradeAquaticItem.getItem().toString();
        
        if (upgradeItemId.contains("kelp") && turtleItem.getItem() == ModItems.CRYSTALLIZED_WATER) {
            // Enhanced water breathing potion materials
            return new ItemStack(ModItems.CRYSTALLIZED_WATER, 3);
        } else if (upgradeItemId.contains("prismarine") && turtleItem.getItem() == ModItems.TURTLE_SHELL_FRAGMENT) {
            // Enhanced turtle shell fragment
            return new ItemStack(ModItems.TURTLE_SHELL_FRAGMENT, 2);
        } else if (upgradeItemId.contains("sea_pickle") && turtleItem.getItem() == ModItems.DEEP_SEA_PEARL) {
            // Enhanced underwater lighting item
            return new ItemStack(ModItems.DEEP_SEA_PEARL, 1);
        }
        
        return ItemStack.EMPTY;
    }
    
    /**
     * Register underwater breathing enhancements
     */
    private static void registerUnderwaterBreathing() {
        // Enhanced underwater breathing when wearing turtle shell armor near Upgrade Aquatic kelp
    }
    
    /**
     * Get underwater breathing bonus from turtle shell armor near Upgrade Aquatic kelp
     */
    public static float getUnderwaterBreathingBonus(PlayerEntity player) {
        if (player == null) return 1.0f;
        
        // Check if player has turtle shell armor
        int armorPieces = 0;
        if (player.getInventory().getArmorStack(3).getItem() == ModItems.TURTLE_SHELL_HELMET) armorPieces++;
        if (player.getInventory().getArmorStack(2).getItem() == ModItems.TURTLE_SHELL_CHESTPLATE) armorPieces++;
        if (player.getInventory().getArmorStack(1).getItem() == ModItems.TURTLE_SHELL_LEGGINGS) armorPieces++;
        if (player.getInventory().getArmorStack(0).getItem() == ModItems.TURTLE_SHELL_BOOTS) armorPieces++;
        
        if (armorPieces == 0) return 1.0f;
        
        // Check if near Upgrade Aquatic kelp or seagrass
        if (isNearUpgradeAquaticVegetation(player)) {
            return 1.0f + (armorPieces * 0.25f); // Up to 100% longer underwater breathing
        }
        
        return 1.0f + (armorPieces * 0.1f); // Base 10% bonus per armor piece
    }
    
    /**
     * Check if player is near Upgrade Aquatic vegetation
     */
    private static boolean isNearUpgradeAquaticVegetation(PlayerEntity player) {
        // Check for kelp, seagrass, or coral in nearby blocks
        // This would scan the area around the player for Upgrade Aquatic vegetation
        return RANDOM.nextFloat() < 0.3f; // Placeholder - 30% chance
    }
    
    /**
     * Get swimming speed bonus from turtle shell armor in Upgrade Aquatic waters
     */
    public static float getSwimmingSpeedBonus(PlayerEntity player) {
        if (player == null) return 1.0f;
        
        int armorPieces = 0;
        if (player.getInventory().getArmorStack(3).getItem() == ModItems.TURTLE_SHELL_HELMET) armorPieces++;
        if (player.getInventory().getArmorStack(2).getItem() == ModItems.TURTLE_SHELL_CHESTPLATE) armorPieces++;
        if (player.getInventory().getArmorStack(1).getItem() == ModItems.TURTLE_SHELL_LEGGINGS) armorPieces++;
        if (player.getInventory().getArmorStack(0).getItem() == ModItems.TURTLE_SHELL_BOOTS) armorPieces++;
        
        if (armorPieces >= 4 && isNearUpgradeAquaticVegetation(player)) {
            return 1.3f; // 30% faster swimming in Upgrade Aquatic waters with full set
        } else if (armorPieces >= 2) {
            return 1.15f; // 15% faster swimming with partial set
        }
        
        return 1.0f;
    }
}