package com.bvhfve.aethelon.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Configuration class for Aethelon mod
 * Based on Torcherino config system pattern with JSON serialization
 * Contains all configurable parameters for the mod's behavior
 */
public class AethelonConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("aethelon-config");
    public static AethelonConfig INSTANCE;
    
    // Spawn Configuration - User configurable values
    public int water_depth_required = 30; // Minimum water depth for spawning
    public int clearance_above_required = 50; // Minimum clear space above for shell/island
    public float turtle_size_scale = 4.0f; // Size multiplier for turtle (affects hitbox and appearance)
    public float spawn_rarity = 0.5f; // Spawn chance (0.0 to 1.0, where 0.5 = 50% chance)
    
    // Additional spawn settings
    public int max_world_population = 3;
    public int min_distance_between_turtles = 5000;
    public double spawn_chance_per_chunk = 0.0001; // 1 in 10000 chunks
    public int spawn_height_min = 45;
    public int spawn_height_max = 62;
    
    // New spawning configuration
    public int oceanSpawnWeight = 5; // Configurable ocean spawn weight
    public int minimumTurtleDistance = 128; // Minimum distance between turtles in blocks
    public static final int BEACH_SPAWN_WEIGHT = 15; // Hardcoded beach spawn weight
    public static final int SNOWY_BEACH_SPAWN_WEIGHT = 10; // Hardcoded snowy beach spawn weight  
    public static final int STONY_SHORE_SPAWN_WEIGHT = 12; // Hardcoded stony shore spawn weight
    
    // Entity Behavior Configuration
    public int idle_time_min_ticks = 24000; // 20 minutes
    public int idle_time_max_ticks = 72000; // 60 minutes
    public int damage_immunity_ticks = 100; // 5 seconds
    public double health = 2000.0; // Increased from 1000 to 2000
    public double movement_speed = 0.75; // 3x increased base movement speed
    public double follow_range = 128.0;
    public double knockback_resistance = 1.0;
    
    // Phase 2: AI Behavior Configuration
    public double min_idle_time = 20.0; // Minutes
    public double max_idle_time = 60.0; // Minutes
    public double movement_speed_multiplier = 1.0;
    public boolean enable_damage_response = true;
    public double pathfinding_range = 500.0; // Blocks
    
    // Phase 2: Movement Speed Configuration
    public double normal_movement_speed = 0.45; // Normal movement speed
    public double escape_movement_speed = 0.75; // Speed when escaping from damage
    public double navigation_speed = 0.75; // Speed for pathfinding navigation
    public double acceleration_factor = 0.3; // How quickly turtle accelerates (0.1-1.0)
    public double turning_speed = 0.15; // How quickly turtle turns (0.01-1.0)
    
    // Phase 4: Island Configuration
    public boolean enable_islands = true; // Enable island system
    public boolean auto_create_islands = true; // Automatically create islands on spawn
    public double small_island_chance = 0.5; // 50% chance for small islands
    public double medium_island_chance = 0.35; // 35% chance for medium islands
    public double large_island_chance = 0.15; // 15% chance for large islands
    public boolean preserve_island_entities = true; // Keep entities on islands during movement
    public boolean enable_custom_islands = true; // Allow loading custom island structures
    
    // Phase 3: Enhanced Damage & Death Configuration
    public boolean enable_enhanced_damage = true; // Enable enhanced damage system
    public boolean enable_death_explosion = true; // Enable dramatic death explosion
    public int explosion_tnt_count = 15; // Number of TNT entities spawned on death
    public float explosion_radius = 25.0f; // Explosion radius in blocks
    public boolean broadcast_death_messages = true; // Broadcast death messages to all players
    public boolean remove_island_on_death = true; // Remove island before explosion
    public int agitation_decay_rate = 1; // How fast agitation decreases (per 3 seconds)
    public int enrage_threshold = 75; // Agitation level needed to trigger enrage state
    
    // Loot System Configuration
    public boolean enable_loot_drops = true; // Enable loot drops from defeated turtles
    public boolean show_loot_notifications = true; // Show loot messages to players
    public int base_experience_reward = 500; // Base experience points for defeating turtle
    public float loot_quantity_multiplier = 1.0f; // Global loot quantity multiplier
    public float loot_rarity_bonus = 0.0f; // Global bonus to rare item chances
    public boolean enable_custom_turtle_items = true; // Enable custom turtle-themed items
    public boolean enable_island_loot = true; // Drop loot from destroyed islands
    public boolean enable_enrage_bonus_loot = true; // Extra loot for enraged turtles
    public int max_loot_spread_radius = 10; // Maximum radius for loot spread (blocks)
    
    // Island Configuration
    public int max_island_size = 64; // 64x64 blocks max
    public int island_y_offset = 2; // Blocks above turtle
    public String default_island_structure = "small_island";
    public int movement_blocks_per_tick = 5; // For gradual movement
    
    // Explosion Configuration
    public float explosion_power = 8.0f;
    public boolean destroy_blocks = true;
    public boolean create_fire = false;
    public int explosion_delay_ticks = 60; // 3 seconds after death
    
    // Special Island Configuration
    public boolean enable_special_islands = true;
    public double special_island_spawn_multiplier = 1.0; // Multiplier for special island spawn chances
    public int min_distance_from_spawn_for_special = 5000; // Minimum distance from world spawn
    public int min_distance_between_special_islands = 2000; // Minimum distance between special islands
    public int max_boss_islands_per_world = 3;
    public int max_treasure_islands_per_world = 8;
    public int max_mystical_islands_per_world = 5;
    public int min_playtime_hours_for_boss_islands = 10;
    public int min_playtime_hours_for_mystical_islands = 15;
    
    // Debug Configuration
    public boolean enable_debug_logging = false;
    public boolean debug_chunk_generation = false; // Log config status during chunk generation
    public boolean debug_spawn_attempts = false;   // Log detailed spawn attempt information
    public boolean debug_special_island_spawning = false; // Log special island spawn attempts
    public boolean show_state_particles = false;
    public boolean enable_spawn_commands = true;
    
    /**
     * Initialize the config system
     * Creates default config if none exists, loads existing config otherwise
     */
    public static void initialize() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        
        Path configDir = FabricLoader.getInstance().getConfigDir();
        Path configFile = configDir.resolve("aethelon.json");
        
        AethelonConfig config = null;
        
        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            LOGGER.warn("Failed to create config directory, using default config: {}", e.getMessage());
            config = new AethelonConfig();
        }
        
        if (config == null) {
            if (Files.exists(configFile)) {
                try {
                    String configContent = Files.readString(configFile);
                    config = gson.fromJson(configContent, AethelonConfig.class);
                    LOGGER.info("Loaded Aethelon config from file");
                } catch (IOException e) {
                    LOGGER.warn("Failed to read config file, using default config: {}", e.getMessage());
                    config = new AethelonConfig();
                }
            } else {
                config = new AethelonConfig();
                try {
                    String configJson = gson.toJson(config);
                    Files.writeString(configFile, configJson, StandardOpenOption.CREATE_NEW);
                    LOGGER.info("Created default Aethelon config file at: {}", configFile);
                } catch (IOException e) {
                    LOGGER.warn("Failed to save default config file: {}", e.getMessage());
                }
            }
        }
        
        INSTANCE = config;
        INSTANCE.validateAndClampValues();
        LOGGER.info("Aethelon config initialized - Water depth: {}, Clearance: {}, Size: {}, Rarity: {}", 
                   INSTANCE.water_depth_required, INSTANCE.clearance_above_required, 
                   INSTANCE.turtle_size_scale, INSTANCE.spawn_rarity);
        
        // Log all config options if debug logging is enabled
        if (INSTANCE.enable_debug_logging) {
            INSTANCE.logAllConfigOptions();
        }
    }
    
    /**
     * Validate and clamp config values to safe ranges
     */
    private void validateAndClampValues() {
        // Clamp water depth (minimum 10, maximum 100)
        if (water_depth_required < 10) {
            LOGGER.warn("Water depth {} is too low, clamping to 10", water_depth_required);
            water_depth_required = 10;
        } else if (water_depth_required > 100) {
            LOGGER.warn("Water depth {} is too high, clamping to 100", water_depth_required);
            water_depth_required = 100;
        }
        
        // Clamp clearance above (minimum 20, maximum 200)
        if (clearance_above_required < 20) {
            LOGGER.warn("Clearance above {} is too low, clamping to 20", clearance_above_required);
            clearance_above_required = 20;
        } else if (clearance_above_required > 200) {
            LOGGER.warn("Clearance above {} is too high, clamping to 200", clearance_above_required);
            clearance_above_required = 200;
        }
        
        // Clamp turtle size (minimum 0.5, maximum 10.0)
        if (turtle_size_scale < 0.5f) {
            LOGGER.warn("Turtle size {} is too small, clamping to 0.5", turtle_size_scale);
            turtle_size_scale = 0.5f;
        } else if (turtle_size_scale > 10.0f) {
            LOGGER.warn("Turtle size {} is too large, clamping to 10.0", turtle_size_scale);
            turtle_size_scale = 10.0f;
        }
        
        // Clamp spawn rarity (minimum 0.001, maximum 1.0)
        if (spawn_rarity < 0.001f) {
            LOGGER.warn("Spawn rarity {} is too low, clamping to 0.001", spawn_rarity);
            spawn_rarity = 0.001f;
        } else if (spawn_rarity > 1.0f) {
            LOGGER.warn("Spawn rarity {} is too high, clamping to 1.0", spawn_rarity);
            spawn_rarity = 1.0f;
        }
        
        // Clamp other values
        health = Math.max(100.0, Math.min(10000.0, health));
        movement_speed = Math.max(0.01, Math.min(1.0, movement_speed));
        max_world_population = Math.max(1, Math.min(50, max_world_population));
        
        // Clamp movement speed values
        normal_movement_speed = Math.max(0.01, Math.min(2.0, normal_movement_speed));
        escape_movement_speed = Math.max(0.01, Math.min(3.0, escape_movement_speed));
        navigation_speed = Math.max(0.01, Math.min(3.0, navigation_speed));
        acceleration_factor = Math.max(0.01, Math.min(1.0, acceleration_factor));
        turning_speed = Math.max(0.01, Math.min(1.0, turning_speed));
        
        // Validate and normalize island configuration
        small_island_chance = Math.max(0.0, Math.min(1.0, small_island_chance));
        medium_island_chance = Math.max(0.0, Math.min(1.0, medium_island_chance));
        large_island_chance = Math.max(0.0, Math.min(1.0, large_island_chance));
        
        // Normalize island chances to sum to 1.0
        double totalChance = small_island_chance + medium_island_chance + large_island_chance;
        if (totalChance > 0) {
            small_island_chance /= totalChance;
            medium_island_chance /= totalChance;
            large_island_chance /= totalChance;
        } else {
            // Default values if all are 0
            small_island_chance = 0.5;
            medium_island_chance = 0.35;
            large_island_chance = 0.15;
        }
        
        // Validate Phase 3 configuration
        explosion_tnt_count = Math.max(5, Math.min(50, explosion_tnt_count));
        explosion_radius = Math.max(10.0f, Math.min(100.0f, explosion_radius));
        agitation_decay_rate = Math.max(1, Math.min(10, agitation_decay_rate));
        enrage_threshold = Math.max(50, Math.min(100, enrage_threshold));
        
        // Validate loot system configuration
        base_experience_reward = Math.max(50, Math.min(5000, base_experience_reward));
        loot_quantity_multiplier = Math.max(0.1f, Math.min(5.0f, loot_quantity_multiplier));
        loot_rarity_bonus = Math.max(0.0f, Math.min(1.0f, loot_rarity_bonus));
        max_loot_spread_radius = Math.max(5, Math.min(50, max_loot_spread_radius));
    }
    
    /**
     * Get the current water depth requirement
     */
    public static int getWaterDepthRequired() {
        return INSTANCE != null ? INSTANCE.water_depth_required : 30;
    }
    
    /**
     * Get the current clearance above requirement
     */
    public static int getClearanceAboveRequired() {
        return INSTANCE != null ? INSTANCE.clearance_above_required : 50;
    }
    
    /**
     * Get the current turtle size scale
     */
    public static float getTurtleSizeScale() {
        return INSTANCE != null ? INSTANCE.turtle_size_scale : 4.0f;
    }
    
    /**
     * Get the current spawn rarity
     */
    public static float getSpawnRarity() {
        return INSTANCE != null ? INSTANCE.spawn_rarity : 0.1f;
    }
    
    /**
     * Log all configuration options and their status
     * Called during initialization and chunk generation when debug logging is enabled
     */
    public void logAllConfigOptions() {
        LOGGER.info("=== AETHELON DEBUG CONFIG DUMP ===");
        
        // Spawn Configuration
        LOGGER.info("SPAWN CONFIG:");
        LOGGER.info("  water_depth_required: {} (Status: {})", water_depth_required, 
                   (water_depth_required >= 10 && water_depth_required <= 100) ? "VALID" : "CLAMPED");
        LOGGER.info("  clearance_above_required: {} (Status: {})", clearance_above_required,
                   (clearance_above_required >= 20 && clearance_above_required <= 200) ? "VALID" : "CLAMPED");
        LOGGER.info("  turtle_size_scale: {} (Status: {})", turtle_size_scale,
                   (turtle_size_scale >= 0.5f && turtle_size_scale <= 10.0f) ? "VALID" : "CLAMPED");
        LOGGER.info("  spawn_rarity: {} (Status: {})", spawn_rarity,
                   (spawn_rarity >= 0.001f && spawn_rarity <= 1.0f) ? "VALID" : "CLAMPED");
        LOGGER.info("  max_world_population: {} (Status: {})", max_world_population,
                   (max_world_population >= 1 && max_world_population <= 50) ? "VALID" : "CLAMPED");
        LOGGER.info("  min_distance_between_turtles: {} (Status: VALID)", min_distance_between_turtles);
        LOGGER.info("  spawn_chance_per_chunk: {} (Status: VALID)", spawn_chance_per_chunk);
        LOGGER.info("  spawn_height_min: {} (Status: VALID)", spawn_height_min);
        LOGGER.info("  spawn_height_max: {} (Status: VALID)", spawn_height_max);
        
        // New Spawning Configuration
        LOGGER.info("BIOME SPAWN CONFIG:");
        LOGGER.info("  oceanSpawnWeight: {} (Status: {})", oceanSpawnWeight,
                   oceanSpawnWeight >= 0 ? "VALID" : "INVALID");
        LOGGER.info("  minimumTurtleDistance: {} (Status: VALID)", minimumTurtleDistance);
        LOGGER.info("  BEACH_SPAWN_WEIGHT: {} (Status: HARDCODED)", BEACH_SPAWN_WEIGHT);
        LOGGER.info("  SNOWY_BEACH_SPAWN_WEIGHT: {} (Status: HARDCODED)", SNOWY_BEACH_SPAWN_WEIGHT);
        LOGGER.info("  STONY_SHORE_SPAWN_WEIGHT: {} (Status: HARDCODED)", STONY_SHORE_SPAWN_WEIGHT);
        
        // Entity Behavior Configuration
        LOGGER.info("ENTITY BEHAVIOR CONFIG:");
        LOGGER.info("  health: {} (Status: {})", health,
                   (health >= 100.0 && health <= 10000.0) ? "VALID" : "CLAMPED");
        LOGGER.info("  movement_speed: {} (Status: {})", movement_speed,
                   (movement_speed >= 0.01 && movement_speed <= 1.0) ? "VALID" : "CLAMPED");
        LOGGER.info("  normal_movement_speed: {} (Status: {})", normal_movement_speed,
                   (normal_movement_speed >= 0.01 && normal_movement_speed <= 2.0) ? "VALID" : "CLAMPED");
        LOGGER.info("  escape_movement_speed: {} (Status: {})", escape_movement_speed,
                   (escape_movement_speed >= 0.01 && escape_movement_speed <= 3.0) ? "VALID" : "CLAMPED");
        LOGGER.info("  navigation_speed: {} (Status: {})", navigation_speed,
                   (navigation_speed >= 0.01 && navigation_speed <= 3.0) ? "VALID" : "CLAMPED");
        LOGGER.info("  acceleration_factor: {} (Status: {})", acceleration_factor,
                   (acceleration_factor >= 0.01 && acceleration_factor <= 1.0) ? "VALID" : "CLAMPED");
        LOGGER.info("  turning_speed: {} (Status: {})", turning_speed,
                   (turning_speed >= 0.01 && turning_speed <= 1.0) ? "VALID" : "CLAMPED");
        LOGGER.info("  follow_range: {} (Status: VALID)", follow_range);
        LOGGER.info("  knockback_resistance: {} (Status: VALID)", knockback_resistance);
        
        // AI Behavior Configuration
        LOGGER.info("AI BEHAVIOR CONFIG:");
        LOGGER.info("  min_idle_time: {} minutes (Status: VALID)", min_idle_time);
        LOGGER.info("  max_idle_time: {} minutes (Status: VALID)", max_idle_time);
        LOGGER.info("  movement_speed_multiplier: {} (Status: VALID)", movement_speed_multiplier);
        LOGGER.info("  enable_damage_response: {} (Status: {})", enable_damage_response,
                   enable_damage_response ? "ENABLED" : "DISABLED");
        LOGGER.info("  pathfinding_range: {} blocks (Status: VALID)", pathfinding_range);
        
        // Island Configuration
        LOGGER.info("ISLAND CONFIG:");
        LOGGER.info("  enable_islands: {} (Status: {})", enable_islands,
                   enable_islands ? "ENABLED" : "DISABLED");
        LOGGER.info("  auto_create_islands: {} (Status: {})", auto_create_islands,
                   auto_create_islands ? "ENABLED" : "DISABLED");
        LOGGER.info("  small_island_chance: {} (Status: {})", small_island_chance,
                   (small_island_chance >= 0.0 && small_island_chance <= 1.0) ? "NORMALIZED" : "INVALID");
        LOGGER.info("  medium_island_chance: {} (Status: {})", medium_island_chance,
                   (medium_island_chance >= 0.0 && medium_island_chance <= 1.0) ? "NORMALIZED" : "INVALID");
        LOGGER.info("  large_island_chance: {} (Status: {})", large_island_chance,
                   (large_island_chance >= 0.0 && large_island_chance <= 1.0) ? "NORMALIZED" : "INVALID");
        LOGGER.info("  preserve_island_entities: {} (Status: {})", preserve_island_entities,
                   preserve_island_entities ? "ENABLED" : "DISABLED");
        LOGGER.info("  enable_custom_islands: {} (Status: {})", enable_custom_islands,
                   enable_custom_islands ? "ENABLED" : "DISABLED");
        LOGGER.info("  max_island_size: {} blocks (Status: VALID)", max_island_size);
        LOGGER.info("  island_y_offset: {} blocks (Status: VALID)", island_y_offset);
        LOGGER.info("  default_island_structure: '{}' (Status: VALID)", default_island_structure);
        LOGGER.info("  movement_blocks_per_tick: {} (Status: VALID)", movement_blocks_per_tick);
        
        // Enhanced Damage & Death Configuration
        LOGGER.info("DAMAGE & DEATH CONFIG:");
        LOGGER.info("  enable_enhanced_damage: {} (Status: {})", enable_enhanced_damage,
                   enable_enhanced_damage ? "ENABLED" : "DISABLED");
        LOGGER.info("  enable_death_explosion: {} (Status: {})", enable_death_explosion,
                   enable_death_explosion ? "ENABLED" : "DISABLED");
        LOGGER.info("  explosion_tnt_count: {} (Status: {})", explosion_tnt_count,
                   (explosion_tnt_count >= 5 && explosion_tnt_count <= 50) ? "VALID" : "CLAMPED");
        LOGGER.info("  explosion_radius: {} blocks (Status: {})", explosion_radius,
                   (explosion_radius >= 10.0f && explosion_radius <= 100.0f) ? "VALID" : "CLAMPED");
        LOGGER.info("  explosion_power: {} (Status: VALID)", explosion_power);
        LOGGER.info("  destroy_blocks: {} (Status: {})", destroy_blocks,
                   destroy_blocks ? "ENABLED" : "DISABLED");
        LOGGER.info("  create_fire: {} (Status: {})", create_fire,
                   create_fire ? "ENABLED" : "DISABLED");
        LOGGER.info("  explosion_delay_ticks: {} (Status: VALID)", explosion_delay_ticks);
        LOGGER.info("  broadcast_death_messages: {} (Status: {})", broadcast_death_messages,
                   broadcast_death_messages ? "ENABLED" : "DISABLED");
        LOGGER.info("  remove_island_on_death: {} (Status: {})", remove_island_on_death,
                   remove_island_on_death ? "ENABLED" : "DISABLED");
        LOGGER.info("  agitation_decay_rate: {} (Status: {})", agitation_decay_rate,
                   (agitation_decay_rate >= 1 && agitation_decay_rate <= 10) ? "VALID" : "CLAMPED");
        LOGGER.info("  enrage_threshold: {} (Status: {})", enrage_threshold,
                   (enrage_threshold >= 50 && enrage_threshold <= 100) ? "VALID" : "CLAMPED");
        
        // Loot System Configuration
        LOGGER.info("LOOT SYSTEM CONFIG:");
        LOGGER.info("  enable_loot_drops: {} (Status: {})", enable_loot_drops,
                   enable_loot_drops ? "ENABLED" : "DISABLED");
        LOGGER.info("  show_loot_notifications: {} (Status: {})", show_loot_notifications,
                   show_loot_notifications ? "ENABLED" : "DISABLED");
        LOGGER.info("  base_experience_reward: {} XP (Status: {})", base_experience_reward,
                   (base_experience_reward >= 50 && base_experience_reward <= 5000) ? "VALID" : "CLAMPED");
        LOGGER.info("  loot_quantity_multiplier: {} (Status: {})", loot_quantity_multiplier,
                   (loot_quantity_multiplier >= 0.1f && loot_quantity_multiplier <= 5.0f) ? "VALID" : "CLAMPED");
        LOGGER.info("  loot_rarity_bonus: {} (Status: {})", loot_rarity_bonus,
                   (loot_rarity_bonus >= 0.0f && loot_rarity_bonus <= 1.0f) ? "VALID" : "CLAMPED");
        LOGGER.info("  enable_custom_turtle_items: {} (Status: {})", enable_custom_turtle_items,
                   enable_custom_turtle_items ? "ENABLED" : "DISABLED");
        LOGGER.info("  enable_island_loot: {} (Status: {})", enable_island_loot,
                   enable_island_loot ? "ENABLED" : "DISABLED");
        LOGGER.info("  enable_enrage_bonus_loot: {} (Status: {})", enable_enrage_bonus_loot,
                   enable_enrage_bonus_loot ? "ENABLED" : "DISABLED");
        LOGGER.info("  max_loot_spread_radius: {} blocks (Status: {})", max_loot_spread_radius,
                   (max_loot_spread_radius >= 5 && max_loot_spread_radius <= 50) ? "VALID" : "CLAMPED");
        
        // Debug Configuration
        LOGGER.info("DEBUG CONFIG:");
        LOGGER.info("  enable_debug_logging: {} (Status: ACTIVE)", enable_debug_logging);
        LOGGER.info("  show_state_particles: {} (Status: {})", show_state_particles,
                   show_state_particles ? "ENABLED" : "DISABLED");
        LOGGER.info("  enable_spawn_commands: {} (Status: {})", enable_spawn_commands,
                   enable_spawn_commands ? "ENABLED" : "DISABLED");
        
        LOGGER.info("=== END CONFIG DUMP ===");
    }
    
    /**
     * Log config status during chunk generation (called when debug logging is enabled)
     */
    public static void logChunkGenerationStatus(int chunkX, int chunkZ) {
        if (INSTANCE != null && INSTANCE.enable_debug_logging) {
            LOGGER.debug("CHUNK GENERATION DEBUG [{}:{}]:", chunkX, chunkZ);
            LOGGER.debug("  Spawn conditions: Ocean weight={}, Min distance={}, Max population={}", 
                        INSTANCE.oceanSpawnWeight, INSTANCE.minimumTurtleDistance, INSTANCE.max_world_population);
            LOGGER.debug("  Beach spawn weights: Beach={}, Snowy={}, Stony={}", 
                        BEACH_SPAWN_WEIGHT, SNOWY_BEACH_SPAWN_WEIGHT, STONY_SHORE_SPAWN_WEIGHT);
            LOGGER.debug("  Water requirements: Depth={}, Clearance={}", 
                        INSTANCE.water_depth_required, INSTANCE.clearance_above_required);
            LOGGER.debug("  Spawn chance per chunk: {}, Rarity: {}", 
                        INSTANCE.spawn_chance_per_chunk, INSTANCE.spawn_rarity);
            LOGGER.debug("  Island system: Enabled={}, Auto-create={}", 
                        INSTANCE.enable_islands, INSTANCE.auto_create_islands);
        }
    }
    
    /**
     * Check if debug logging is enabled
     */
    public static boolean isDebugLoggingEnabled() {
        return INSTANCE != null && INSTANCE.enable_debug_logging;
    }
}