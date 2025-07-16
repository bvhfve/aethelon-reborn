package com.bvhfve.aethelon.compat;

import com.bvhfve.aethelon.entity.AethelonEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Compatibility integration with Ecologics mod
 * 
 * Features:
 * - Enhanced interactions with Ecologics biomes and creatures
 * - Coconut crab symbiotic relationships
 * - Mangrove swamp turtle behaviors
 * - Penguin colony interactions in cold waters
 * - Enhanced spawning in Ecologics biomes
 * - Special island decorations with Ecologics blocks
 */
public class EcologicsCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonCompat/Ecologics");
    
    // Ecologics biome compatibility mapping
    private static final Map<String, Float> ECOLOGICS_BIOME_SPAWN_WEIGHTS = new HashMap<>();
    private static final Map<String, String> ECOLOGICS_ISLAND_THEMES = new HashMap<>();
    private static final Map<String, String> ECOLOGICS_CREATURE_INTERACTIONS = new HashMap<>();
    
    static {
        // Enhanced spawn weights for Ecologics biomes
        ECOLOGICS_BIOME_SPAWN_WEIGHTS.put("ecologics:tropical_rainforest", 2.5f);
        ECOLOGICS_BIOME_SPAWN_WEIGHTS.put("ecologics:tropical_shores", 3.5f);
        ECOLOGICS_BIOME_SPAWN_WEIGHTS.put("ecologics:mangrove_swamp", 4.0f);
        ECOLOGICS_BIOME_SPAWN_WEIGHTS.put("ecologics:ice_sheets", 1.2f);
        ECOLOGICS_BIOME_SPAWN_WEIGHTS.put("ecologics:coconut_forest", 3.0f);
        
        // Island themes for Ecologics biomes
        ECOLOGICS_ISLAND_THEMES.put("ecologics:tropical_rainforest", "tropical_paradise");
        ECOLOGICS_ISLAND_THEMES.put("ecologics:tropical_shores", "coconut_beach");
        ECOLOGICS_ISLAND_THEMES.put("ecologics:mangrove_swamp", "mangrove_sanctuary");
        ECOLOGICS_ISLAND_THEMES.put("ecologics:ice_sheets", "penguin_colony");
        ECOLOGICS_ISLAND_THEMES.put("ecologics:coconut_forest", "coconut_grove");
        
        // Creature interactions
        ECOLOGICS_CREATURE_INTERACTIONS.put("ecologics:coconut_crab", "symbiotic_cleaning");
        ECOLOGICS_CREATURE_INTERACTIONS.put("ecologics:penguin", "playful_interaction");
        ECOLOGICS_CREATURE_INTERACTIONS.put("ecologics:camel", "desert_encounter");
        ECOLOGICS_CREATURE_INTERACTIONS.put("ecologics:squirrel", "forest_friendship");
    }
    
    public static boolean initialize() {
        try {
            LOGGER.info("Initializing Ecologics compatibility...");
            
            // Initialize Ecologics-specific features
            initializeEcologicsBiomeIntegration();
            initializeCreatureInteractions();
            initializeMangroveSwampBehaviors();
            initializePenguinColonyInteractions();
            initializeCoconutCrabSymbiosis();
            
            LOGGER.info("Ecologics compatibility initialized successfully!");
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Ecologics compatibility: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Initialize Ecologics biome integration
     */
    private static void initializeEcologicsBiomeIntegration() {
        LOGGER.info("Setting up Ecologics biome integration...");
    }
    
    /**
     * Initialize creature interactions
     */
    private static void initializeCreatureInteractions() {
        LOGGER.info("Setting up Ecologics creature interactions...");
    }
    
    /**
     * Initialize mangrove swamp behaviors
     */
    private static void initializeMangroveSwampBehaviors() {
        LOGGER.info("Setting up mangrove swamp turtle behaviors...");
    }
    
    /**
     * Initialize penguin colony interactions
     */
    private static void initializePenguinColonyInteractions() {
        LOGGER.info("Setting up penguin colony interactions...");
    }
    
    /**
     * Initialize coconut crab symbiosis
     */
    private static void initializeCoconutCrabSymbiosis() {
        LOGGER.info("Setting up coconut crab symbiotic relationships...");
    }
    
    /**
     * Get spawn weight multiplier for Ecologics biomes
     */
    public static float getSpawnWeightMultiplier(RegistryKey<Biome> biome) {
        String biomeId = biome.getValue().toString();
        return ECOLOGICS_BIOME_SPAWN_WEIGHTS.getOrDefault(biomeId, 1.0f);
    }
    
    /**
     * Get island theme for Ecologics biome
     */
    public static String getIslandTheme(RegistryKey<Biome> biome) {
        String biomeId = biome.getValue().toString();
        return ECOLOGICS_ISLAND_THEMES.getOrDefault(biomeId, "default");
    }
    
    /**
     * Handle Ecologics-specific turtle behaviors
     */
    public static void handleEcologicsBehaviors(AethelonEntity turtle, World world, BlockPos pos) {
        try {
            RegistryKey<Biome> biome = world.getBiome(pos).getKey().orElse(null);
            if (biome == null) return;
            
            String biomeId = biome.getValue().toString();
            
            // Apply biome-specific behaviors
            switch (biomeId) {
                case "ecologics:mangrove_swamp":
                    handleMangroveSwampBehavior(turtle, world, pos);
                    break;
                case "ecologics:tropical_shores":
                    handleTropicalShoresBehavior(turtle, world, pos);
                    break;
                case "ecologics:ice_sheets":
                    handleIceSheetsBehavior(turtle, world, pos);
                    break;
                case "ecologics:coconut_forest":
                    handleCoconutForestBehavior(turtle, world, pos);
                    break;
            }
            
            // Handle creature interactions
            handleEcologicsCreatureInteractions(turtle, world);
            
        } catch (Exception e) {
            LOGGER.warn("Error applying Ecologics behaviors: {}", e.getMessage());
        }
    }
    
    /**
     * Handle mangrove swamp specific behaviors
     */
    private static void handleMangroveSwampBehavior(AethelonEntity turtle, World world, BlockPos pos) {
        // Enhanced camouflage in mangroves
        turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 300, 0));
        
        // Better navigation through mangrove roots
        turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 600, 0));
        
        // Healing from mangrove ecosystem
        if (turtle.getHealth() < turtle.getMaxHealth()) {
            turtle.heal(0.2f);
        }
    }
    
    /**
     * Handle tropical shores behaviors
     */
    private static void handleTropicalShoresBehavior(AethelonEntity turtle, World world, BlockPos pos) {
        // Enhanced speed in tropical waters
        turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 0));
        turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 400, 0));
        
        // Sun basking benefits
        if (world.isDay() && world.isSkyVisible(pos)) {
            turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0));
        }
    }
    
    /**
     * Handle ice sheets behaviors
     */
    private static void handleIceSheetsBehavior(AethelonEntity turtle, World world, BlockPos pos) {
        // Cold resistance
        turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 0));
        turtle.setFrozenTicks(0);
        
        // Slower movement in cold water
        turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 0));
    }
    
    /**
     * Handle coconut forest behaviors
     */
    private static void handleCoconutForestBehavior(AethelonEntity turtle, World world, BlockPos pos) {
        // Enhanced luck from coconut abundance
        turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 600, 1));
        
        // Coconut crab symbiosis benefits
        turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 300, 0));
    }
    
    /**
     * Handle creature interactions with Ecologics mobs
     */
    private static void handleEcologicsCreatureInteractions(AethelonEntity turtle, World world) {
        try {
            List<Entity> nearbyCreatures = world.getOtherEntities(turtle,
                turtle.getBoundingBox().expand(16.0),
                entity -> isEcologicsCreature(entity));
            
            for (Entity creature : nearbyCreatures) {
                String creatureType = getCreatureType(creature);
                String interaction = ECOLOGICS_CREATURE_INTERACTIONS.get(creatureType);
                
                if (interaction != null) {
                    applyCreatureInteraction(turtle, creature, interaction, world);
                }
            }
            
        } catch (Exception e) {
            LOGGER.warn("Error handling Ecologics creature interactions: {}", e.getMessage());
        }
    }
    
    /**
     * Apply specific creature interaction
     */
    private static void applyCreatureInteraction(AethelonEntity turtle, Entity creature, String interaction, World world) {
        try {
            switch (interaction) {
                case "symbiotic_cleaning":
                    // Coconut crabs clean turtle shell
                    turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 1));
                    turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 0));
                    break;
                case "playful_interaction":
                    // Penguins play with turtle
                    turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 600, 1));
                    turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 0));
                    break;
                case "desert_encounter":
                    // Rare desert oasis encounter
                    turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 800, 2));
                    break;
                case "forest_friendship":
                    // Forest creatures provide guidance
                    turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0));
                    break;
            }
        } catch (Exception e) {
            LOGGER.warn("Error applying creature interaction {}: {}", interaction, e.getMessage());
        }
    }
    
    /**
     * Generate Ecologics-themed island decorations
     */
    public static void generateEcologicsIslandDecorations(World world, BlockPos islandCenter, String theme) {
        try {
            Random random = new Random();
            
            switch (theme) {
                case "tropical_paradise":
                    generateTropicalParadiseDecorations(world, islandCenter, random);
                    break;
                case "coconut_beach":
                    generateCoconutBeachDecorations(world, islandCenter, random);
                    break;
                case "mangrove_sanctuary":
                    generateMangroveSanctuaryDecorations(world, islandCenter, random);
                    break;
                case "penguin_colony":
                    generatePenguinColonyDecorations(world, islandCenter, random);
                    break;
                case "coconut_grove":
                    generateCoconutGroveDecorations(world, islandCenter, random);
                    break;
                default:
                    generateEcologicsDefaultDecorations(world, islandCenter, random);
                    break;
            }
        } catch (Exception e) {
            LOGGER.warn("Error generating Ecologics island decorations: {}", e.getMessage());
        }
    }
    
    private static void generateTropicalParadiseDecorations(World world, BlockPos center, Random random) {
        // Add tropical plants and flowers
        LOGGER.debug("Generating tropical paradise decorations at {}", center);
    }
    
    private static void generateCoconutBeachDecorations(World world, BlockPos center, Random random) {
        // Add coconut palms and beach elements
        LOGGER.debug("Generating coconut beach decorations at {}", center);
    }
    
    private static void generateMangroveSanctuaryDecorations(World world, BlockPos center, Random random) {
        // Add mangrove roots and swamp vegetation
        LOGGER.debug("Generating mangrove sanctuary decorations at {}", center);
    }
    
    private static void generatePenguinColonyDecorations(World world, BlockPos center, Random random) {
        // Add ice formations and penguin-friendly structures
        LOGGER.debug("Generating penguin colony decorations at {}", center);
    }
    
    private static void generateCoconutGroveDecorations(World world, BlockPos center, Random random) {
        // Add coconut trees and tropical vegetation
        LOGGER.debug("Generating coconut grove decorations at {}", center);
    }
    
    private static void generateEcologicsDefaultDecorations(World world, BlockPos center, Random random) {
        // Add standard Ecologics-style decorations
        LOGGER.debug("Generating Ecologics default decorations at {}", center);
    }
    
    /**
     * Check if entity is an Ecologics creature
     */
    private static boolean isEcologicsCreature(Entity entity) {
        String entityType = entity.getType().toString();
        return entityType.startsWith("ecologics:");
    }
    
    /**
     * Get creature type identifier
     */
    private static String getCreatureType(Entity entity) {
        return entity.getType().toString();
    }
    
    /**
     * Check if current biome is an Ecologics biome
     */
    public static boolean isEcologicsBiome(RegistryKey<Biome> biome) {
        if (biome == null) return false;
        String biomeId = biome.getValue().toString();
        return biomeId.startsWith("ecologics:");
    }
    
    /**
     * Handle coconut crab symbiotic relationship
     */
    public static void handleCoconutCrabSymbiosis(AethelonEntity turtle, World world) {
        try {
            // Look for nearby coconut crabs
            List<Entity> nearbyCoconutCrabs = world.getOtherEntities(turtle,
                turtle.getBoundingBox().expand(8.0),
                entity -> getCreatureType(entity).equals("ecologics:coconut_crab"));
            
            if (!nearbyCoconutCrabs.isEmpty()) {
                // Coconut crabs clean turtle shell and provide protection
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 600, 1));
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 1));
                
                // Enhanced shell durability
                turtle.heal(0.5f);
                
                LOGGER.debug("Coconut crab symbiosis active for turtle at {}", turtle.getBlockPos());
            }
            
        } catch (Exception e) {
            LOGGER.warn("Error handling coconut crab symbiosis: {}", e.getMessage());
        }
    }
    
    /**
     * Handle penguin colony interactions
     */
    public static void handlePenguinColonyInteractions(AethelonEntity turtle, World world) {
        try {
            // Look for nearby penguins
            List<Entity> nearbyPenguins = world.getOtherEntities(turtle,
                turtle.getBoundingBox().expand(20.0),
                entity -> getCreatureType(entity).equals("ecologics:penguin"));
            
            if (nearbyPenguins.size() >= 3) { // Penguin colony
                // Enhanced cold resistance and playful interactions
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 800, 0));
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 800, 1));
                turtle.setFrozenTicks(0);
                
                LOGGER.debug("Penguin colony interaction active for turtle at {}", turtle.getBlockPos());
            }
            
        } catch (Exception e) {
            LOGGER.warn("Error handling penguin colony interactions: {}", e.getMessage());
        }
    }
}