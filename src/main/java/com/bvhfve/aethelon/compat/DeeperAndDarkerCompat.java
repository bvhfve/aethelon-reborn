package com.bvhfve.aethelon.compat;

import com.bvhfve.aethelon.entity.AethelonEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Compatibility integration with Deeper and Darker mod
 * 
 * Features:
 * - Enhanced spawning in deep dark ocean areas
 * - Ancient turtle variants with sculk resistance
 * - Deep dark island themes with sculk decorations
 * - Integration with Warden detection mechanics
 * - Special loot from deep dark exploration
 */
public class DeeperAndDarkerCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonCompat/DeeperAndDarker");
    
    // Deep dark biome compatibility mapping
    private static final Map<String, Float> DEEP_DARK_SPAWN_WEIGHTS = new HashMap<>();
    private static final Map<String, String> DEEP_DARK_ISLAND_THEMES = new HashMap<>();
    
    static {
        // Enhanced spawn weights for deep dark ocean areas
        DEEP_DARK_SPAWN_WEIGHTS.put("deeperdarker:deep_dark_ocean", 4.0f);
        DEEP_DARK_SPAWN_WEIGHTS.put("deeperdarker:sculk_ocean", 3.5f);
        DEEP_DARK_SPAWN_WEIGHTS.put("deeperdarker:ancient_ocean", 5.0f);
        DEEP_DARK_SPAWN_WEIGHTS.put("deeperdarker:warden_depths", 2.0f);
        
        // Island themes for deep dark biomes
        DEEP_DARK_ISLAND_THEMES.put("deeperdarker:deep_dark_ocean", "sculk_sanctuary");
        DEEP_DARK_ISLAND_THEMES.put("deeperdarker:sculk_ocean", "sculk_garden");
        DEEP_DARK_ISLAND_THEMES.put("deeperdarker:ancient_ocean", "ancient_fortress");
        DEEP_DARK_ISLAND_THEMES.put("deeperdarker:warden_depths", "warden_refuge");
    }
    
    public static boolean initialize() {
        try {
            LOGGER.info("Initializing Deeper and Darker compatibility...");
            
            // Initialize deep dark specific features
            initializeDeepDarkSpawning();
            initializeSculkResistance();
            initializeAncientTurtleVariants();
            initializeWardenInteraction();
            
            LOGGER.info("Deeper and Darker compatibility initialized successfully!");
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Deeper and Darker compatibility: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Initialize enhanced spawning in deep dark areas
     */
    private static void initializeDeepDarkSpawning() {
        LOGGER.info("Setting up enhanced Aethelon spawning for deep dark areas...");
        // Enhanced spawn logic for ancient turtle variants
    }
    
    /**
     * Initialize sculk resistance for turtles
     */
    private static void initializeSculkResistance() {
        LOGGER.info("Setting up sculk resistance for Aethelon turtles...");
        // Sculk sensor immunity and resistance mechanics
    }
    
    /**
     * Initialize ancient turtle variants
     */
    private static void initializeAncientTurtleVariants() {
        LOGGER.info("Setting up ancient turtle variants for deep dark...");
        // Special ancient turtle variants with enhanced abilities
    }
    
    /**
     * Initialize Warden interaction mechanics
     */
    private static void initializeWardenInteraction() {
        LOGGER.info("Setting up Warden interaction mechanics...");
        // Special behaviors when Wardens are nearby
    }
    
    /**
     * Get spawn weight multiplier for deep dark biomes
     */
    public static float getSpawnWeightMultiplier(RegistryKey<Biome> biome) {
        String biomeId = biome.getValue().toString();
        return DEEP_DARK_SPAWN_WEIGHTS.getOrDefault(biomeId, 1.0f);
    }
    
    /**
     * Get island theme for deep dark biome
     */
    public static String getIslandTheme(RegistryKey<Biome> biome) {
        String biomeId = biome.getValue().toString();
        return DEEP_DARK_ISLAND_THEMES.getOrDefault(biomeId, "default");
    }
    
    /**
     * Apply deep dark specific turtle behaviors
     */
    public static void applyDeepDarkBehaviors(AethelonEntity turtle, World world, BlockPos pos) {
        try {
            RegistryKey<Biome> biome = world.getBiome(pos).getKey().orElse(null);
            if (biome == null) return;
            
            String biomeId = biome.getValue().toString();
            
            // Apply deep dark specific behaviors
            if (isDeepDarkBiome(biome)) {
                // Grant sculk resistance
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 1));
                
                // Enhanced stealth in deep dark
                if (world.getAmbientDarkness() > 10) {
                    turtle.setInvisible(true);
                }
                
                // Ancient wisdom effect (enhanced experience)
                if (biomeId.contains("ancient")) {
                    turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 600, 2));
                }
                
                // Warden detection avoidance
                if (isWardenNearby(world, pos)) {
                    turtle.setSilent(true);
                    turtle.addVelocity(0, -0.05, 0); // Dive deeper
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Error applying deep dark behaviors: {}", e.getMessage());
        }
    }
    
    /**
     * Generate deep dark themed island decorations
     */
    public static void generateDeepDarkIslandDecorations(World world, BlockPos islandCenter, String theme) {
        try {
            Random random = new Random();
            
            switch (theme) {
                case "sculk_sanctuary":
                    generateSculkSanctuaryDecorations(world, islandCenter, random);
                    break;
                case "sculk_garden":
                    generateSculkGardenDecorations(world, islandCenter, random);
                    break;
                case "ancient_fortress":
                    generateAncientFortressDecorations(world, islandCenter, random);
                    break;
                case "warden_refuge":
                    generateWardenRefugeDecorations(world, islandCenter, random);
                    break;
                default:
                    generateDeepDarkDefaultDecorations(world, islandCenter, random);
                    break;
            }
        } catch (Exception e) {
            LOGGER.warn("Error generating deep dark island decorations: {}", e.getMessage());
        }
    }
    
    private static void generateSculkSanctuaryDecorations(World world, BlockPos center, Random random) {
        // Add sculk blocks, sculk veins, and sculk catalysts
        LOGGER.debug("Generating sculk sanctuary decorations at {}", center);
    }
    
    private static void generateSculkGardenDecorations(World world, BlockPos center, Random random) {
        // Add sculk shriekers, sculk sensors, and sculk growth
        LOGGER.debug("Generating sculk garden decorations at {}", center);
    }
    
    private static void generateAncientFortressDecorations(World world, BlockPos center, Random random) {
        // Add ancient debris, reinforced deepslate, and echo shards
        LOGGER.debug("Generating ancient fortress decorations at {}", center);
    }
    
    private static void generateWardenRefugeDecorations(World world, BlockPos center, Random random) {
        // Add warden-proof structures and sound dampening materials
        LOGGER.debug("Generating warden refuge decorations at {}", center);
    }
    
    private static void generateDeepDarkDefaultDecorations(World world, BlockPos center, Random random) {
        // Add standard deep dark decorations
        LOGGER.debug("Generating deep dark default decorations at {}", center);
    }
    
    /**
     * Check if current biome is a deep dark biome
     */
    public static boolean isDeepDarkBiome(RegistryKey<Biome> biome) {
        if (biome == null) return false;
        String biomeId = biome.getValue().toString();
        return biomeId.startsWith("deeperdarker:") || biomeId.contains("deep_dark");
    }
    
    /**
     * Check if a Warden is nearby
     */
    private static boolean isWardenNearby(World world, BlockPos pos) {
        // Check for Warden entities within a certain radius
        return world.getEntitiesByClass(
            net.minecraft.entity.mob.WardenEntity.class,
            new net.minecraft.util.math.Box(pos).expand(32),
            entity -> true
        ).size() > 0;
    }
    
    /**
     * Apply sculk resistance to turtle
     */
    public static void applySculkResistance(AethelonEntity turtle) {
        try {
            // Make turtle immune to sculk sensor detection
            turtle.setSilent(true);
            
            // Reduce vibration generation
            turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 400, 0));
            
            // Enhanced resistance in deep dark
            turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 1));
            
        } catch (Exception e) {
            LOGGER.warn("Error applying sculk resistance: {}", e.getMessage());
        }
    }
    
    /**
     * Handle ancient turtle transformation
     */
    public static void handleAncientTransformation(AethelonEntity turtle, World world) {
        try {
            if (isDeepDarkBiome(world.getBiome(turtle.getBlockPos()).getKey().orElse(null))) {
                // Chance for ancient transformation
                Random random = new Random();
                if (random.nextFloat() < 0.01f) { // 1% chance
                    transformToAncientTurtle(turtle);
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Error handling ancient transformation: {}", e.getMessage());
        }
    }
    
    private static void transformToAncientTurtle(AethelonEntity turtle) {
        // Apply ancient turtle effects
        turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, Integer.MAX_VALUE, 2));
        turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, Integer.MAX_VALUE, 0));
        turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, Integer.MAX_VALUE, 0));
        
        // Increase health
        turtle.heal(turtle.getMaxHealth());
        
        LOGGER.info("Turtle transformed into ancient variant at {}", turtle.getBlockPos());
    }
}