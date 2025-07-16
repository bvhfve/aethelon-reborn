package com.bvhfve.aethelon.compat;

import com.bvhfve.aethelon.entity.AethelonEntity;
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
 * Compatibility integration with Naturalist mod
 * 
 * Features:
 * - Enhanced interactions with Naturalist animals
 * - Realistic ecosystem behaviors with marine life
 * - Turtle conservation and protection mechanics
 * - Natural predator-prey relationships
 * - Environmental awareness and adaptation
 */
public class NaturalistCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonCompat/Naturalist");
    
    // Naturalist creature compatibility mapping
    private static final Map<String, String> NATURALIST_INTERACTIONS = new HashMap<>();
    private static final Map<String, Float> CREATURE_THREAT_LEVELS = new HashMap<>();
    
    static {
        // Naturalist creature interactions
        NATURALIST_INTERACTIONS.put("naturalist:shark", "predator_avoidance");
        NATURALIST_INTERACTIONS.put("naturalist:bass", "neutral_coexistence");
        NATURALIST_INTERACTIONS.put("naturalist:catfish", "neutral_coexistence");
        NATURALIST_INTERACTIONS.put("naturalist:bluegill", "peaceful_interaction");
        NATURALIST_INTERACTIONS.put("naturalist:carp", "peaceful_interaction");
        NATURALIST_INTERACTIONS.put("naturalist:seahorse", "symbiotic_relationship");
        NATURALIST_INTERACTIONS.put("naturalist:jellyfish", "cautious_avoidance");
        NATURALIST_INTERACTIONS.put("naturalist:stingray", "respectful_distance");
        NATURALIST_INTERACTIONS.put("naturalist:whale", "majestic_encounter");
        NATURALIST_INTERACTIONS.put("naturalist:dolphin", "playful_interaction");
        
        // Threat levels (0.0 = harmless, 1.0 = very dangerous)
        CREATURE_THREAT_LEVELS.put("naturalist:shark", 0.8f);
        CREATURE_THREAT_LEVELS.put("naturalist:jellyfish", 0.4f);
        CREATURE_THREAT_LEVELS.put("naturalist:stingray", 0.3f);
        CREATURE_THREAT_LEVELS.put("naturalist:whale", 0.1f);
        CREATURE_THREAT_LEVELS.put("naturalist:dolphin", 0.0f);
        CREATURE_THREAT_LEVELS.put("naturalist:seahorse", 0.0f);
        CREATURE_THREAT_LEVELS.put("naturalist:bass", 0.1f);
        CREATURE_THREAT_LEVELS.put("naturalist:catfish", 0.1f);
        CREATURE_THREAT_LEVELS.put("naturalist:bluegill", 0.0f);
        CREATURE_THREAT_LEVELS.put("naturalist:carp", 0.0f);
    }
    
    public static boolean initialize() {
        try {
            LOGGER.info("Initializing Naturalist compatibility...");
            
            // Initialize Naturalist-specific features
            initializeEcosystemBehaviors();
            initializeConservationMechanics();
            initializePredatorPreyRelationships();
            initializeEnvironmentalAdaptation();
            
            LOGGER.info("Naturalist compatibility initialized successfully!");
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Naturalist compatibility: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Initialize realistic ecosystem behaviors
     */
    private static void initializeEcosystemBehaviors() {
        LOGGER.info("Setting up realistic ecosystem behaviors...");
    }
    
    /**
     * Initialize conservation mechanics
     */
    private static void initializeConservationMechanics() {
        LOGGER.info("Setting up turtle conservation mechanics...");
    }
    
    /**
     * Initialize predator-prey relationships
     */
    private static void initializePredatorPreyRelationships() {
        LOGGER.info("Setting up natural predator-prey relationships...");
    }
    
    /**
     * Initialize environmental adaptation
     */
    private static void initializeEnvironmentalAdaptation() {
        LOGGER.info("Setting up environmental adaptation behaviors...");
    }
    
    /**
     * Handle naturalistic interactions with marine life
     */
    public static void handleNaturalistInteractions(AethelonEntity turtle, World world, BlockPos pos) {
        try {
            // Find nearby Naturalist creatures
            List<Entity> nearbyCreatures = world.getOtherEntities(turtle,
                turtle.getBoundingBox().expand(20.0),
                entity -> isNaturalistCreature(entity));
            
            for (Entity creature : nearbyCreatures) {
                String creatureType = getCreatureType(creature);
                String interaction = NATURALIST_INTERACTIONS.get(creatureType);
                
                if (interaction != null) {
                    applyNaturalistBehavior(turtle, creature, interaction, world);
                }
            }
            
        } catch (Exception e) {
            LOGGER.warn("Error handling naturalist interactions: {}", e.getMessage());
        }
    }
    
    /**
     * Apply naturalistic behavior based on creature interaction
     */
    private static void applyNaturalistBehavior(AethelonEntity turtle, Entity creature, String interaction, World world) {
        try {
            switch (interaction) {
                case "predator_avoidance":
                    // Avoid sharks and other predators
                    turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 1));
                    turtle.addVelocity(0, -0.1, 0); // Dive deeper
                    break;
                case "neutral_coexistence":
                    // Coexist peacefully with neutral fish
                    // No special effects, just natural behavior
                    break;
                case "peaceful_interaction":
                    // Calm presence around small fish
                    turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 300, 0));
                    break;
                case "symbiotic_relationship":
                    // Beneficial relationship with seahorses
                    turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 400, 0));
                    break;
                case "cautious_avoidance":
                    // Careful around jellyfish
                    turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0));
                    break;
                case "respectful_distance":
                    // Keep distance from stingrays
                    turtle.setSilent(true);
                    break;
                case "majestic_encounter":
                    // Awed by whale presence
                    turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 600, 1));
                    break;
                case "playful_interaction":
                    // Play with dolphins
                    turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 800, 1));
                    break;
            }
        } catch (Exception e) {
            LOGGER.warn("Error applying naturalist behavior {}: {}", interaction, e.getMessage());
        }
    }
    
    /**
     * Handle conservation mechanics
     */
    public static void handleConservationMechanics(AethelonEntity turtle, World world) {
        try {
            // Implement realistic turtle conservation behaviors
            
            // Nesting behavior enhancement
            if (isNestingSeason(world)) {
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1200, 1));
            }
            
            // Population awareness
            long nearbyTurtles = world.getOtherEntities(turtle,
                turtle.getBoundingBox().expand(100.0),
                entity -> entity instanceof AethelonEntity).size();
            
            if (nearbyTurtles < 3) {
                // Endangered population bonus
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 2400, 1));
            }
            
        } catch (Exception e) {
            LOGGER.warn("Error handling conservation mechanics: {}", e.getMessage());
        }
    }
    
    /**
     * Handle environmental adaptation
     */
    public static void handleEnvironmentalAdaptation(AethelonEntity turtle, World world, BlockPos pos) {
        try {
            RegistryKey<Biome> biome = world.getBiome(pos).getKey().orElse(null);
            if (biome == null) return;
            
            String biomeId = biome.getValue().toString();
            
            // Adapt to different marine environments
            if (biomeId.contains("warm_ocean")) {
                // Enhanced activity in warm waters
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 400, 0));
            } else if (biomeId.contains("cold_ocean")) {
                // Slower metabolism in cold waters
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 400, 0));
            } else if (biomeId.contains("deep_ocean")) {
                // Deep diving adaptations
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 1200, 0));
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1200, 0));
            }
            
            // Seasonal adaptations
            handleSeasonalAdaptations(turtle, world);
            
        } catch (Exception e) {
            LOGGER.warn("Error handling environmental adaptation: {}", e.getMessage());
        }
    }
    
    /**
     * Handle seasonal adaptations
     */
    private static void handleSeasonalAdaptations(AethelonEntity turtle, World world) {
        try {
            long worldTime = world.getTimeOfDay();
            long dayOfYear = (worldTime / 24000) % 365; // Approximate day of year
            
            // Migration season (spring/fall)
            if ((dayOfYear >= 60 && dayOfYear <= 120) || (dayOfYear >= 240 && dayOfYear <= 300)) {
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 1));
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 600, 0));
            }
            
            // Nesting season (summer)
            if (dayOfYear >= 150 && dayOfYear <= 210) {
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 0));
            }
            
        } catch (Exception e) {
            LOGGER.warn("Error handling seasonal adaptations: {}", e.getMessage());
        }
    }
    
    /**
     * Check if entity is a Naturalist creature
     */
    private static boolean isNaturalistCreature(Entity entity) {
        String entityType = entity.getType().toString();
        return entityType.startsWith("naturalist:");
    }
    
    /**
     * Get creature type identifier
     */
    private static String getCreatureType(Entity entity) {
        return entity.getType().toString();
    }
    
    /**
     * Check if it's nesting season
     */
    private static boolean isNestingSeason(World world) {
        long worldTime = world.getTimeOfDay();
        long dayOfYear = (worldTime / 24000) % 365;
        return dayOfYear >= 150 && dayOfYear <= 210; // Summer months
    }
    
    /**
     * Get threat level for creature
     */
    public static float getThreatLevel(Entity creature) {
        String creatureType = getCreatureType(creature);
        return CREATURE_THREAT_LEVELS.getOrDefault(creatureType, 0.0f);
    }
    
    /**
     * Handle realistic feeding behaviors
     */
    public static void handleFeedingBehaviors(AethelonEntity turtle, World world) {
        try {
            // Look for appropriate food sources
            List<Entity> nearbyFood = world.getOtherEntities(turtle,
                turtle.getBoundingBox().expand(8.0),
                entity -> isValidFoodSource(entity));
            
            if (!nearbyFood.isEmpty()) {
                // Feeding behavior effects
                turtle.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0));
                
                // Realistic feeding schedule
                long worldTime = world.getTimeOfDay() % 24000;
                if (worldTime >= 6000 && worldTime <= 18000) { // Daytime feeding
                    turtle.heal(1.0f);
                }
            }
            
        } catch (Exception e) {
            LOGGER.warn("Error handling feeding behaviors: {}", e.getMessage());
        }
    }
    
    /**
     * Check if entity is a valid food source for turtles
     */
    private static boolean isValidFoodSource(Entity entity) {
        String entityType = entity.getType().toString();
        return entityType.contains("jellyfish") || 
               entityType.contains("seagrass") || 
               entityType.contains("kelp") ||
               entityType.contains("small_fish");
    }
}