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
    public float spawn_rarity = 0.1f; // Spawn chance (0.0 to 1.0, where 0.1 = 10% chance)
    
    // Additional spawn settings
    public int max_world_population = 3;
    public int min_distance_between_turtles = 5000;
    public double spawn_chance_per_chunk = 0.0001; // 1 in 10000 chunks
    public int spawn_height_min = 45;
    public int spawn_height_max = 62;
    
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
    
    // Debug Configuration
    public boolean enable_debug_logging = false;
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
}