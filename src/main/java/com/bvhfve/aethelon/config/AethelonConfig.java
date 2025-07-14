package com.bvhfve.aethelon.config;

/**
 * Configuration class for Aethelon mod
 * Contains all configurable parameters for the mod's behavior
 */
public class AethelonConfig {
    
    // Spawn Configuration
    public static class Spawn {
        public static final int MAX_WORLD_POPULATION = 3;
        public static final int MIN_DISTANCE_BETWEEN_TURTLES = 5000;
        public static final double SPAWN_CHANCE_PER_CHUNK = 0.0001; // 1 in 10000 chunks
        public static final int SPAWN_HEIGHT_MIN = 45;
        public static final int SPAWN_HEIGHT_MAX = 62;
    }
    
    // Entity Behavior Configuration
    public static class Behavior {
        public static final int IDLE_TIME_MIN_TICKS = 24000; // 20 minutes
        public static final int IDLE_TIME_MAX_TICKS = 72000; // 60 minutes
        public static final double MOVEMENT_SPEED = 0.1;
        public static final int DAMAGE_IMMUNITY_TICKS = 100; // 5 seconds
        public static final double HEALTH = 1000.0;
    }
    
    // Island Configuration
    public static class Island {
        public static final int MAX_ISLAND_SIZE = 64; // 64x64 blocks max
        public static final int ISLAND_Y_OFFSET = 2; // Blocks above turtle
        public static final String DEFAULT_ISLAND_STRUCTURE = "small_island";
        public static final int MOVEMENT_BLOCKS_PER_TICK = 5; // For gradual movement
    }
    
    // Explosion Configuration
    public static class Explosion {
        public static final float EXPLOSION_POWER = 8.0f;
        public static final boolean DESTROY_BLOCKS = true;
        public static final boolean CREATE_FIRE = false;
        public static final int EXPLOSION_DELAY_TICKS = 60; // 3 seconds after death
    }
    
    // Debug Configuration
    public static class Debug {
        public static final boolean ENABLE_DEBUG_LOGGING = false;
        public static final boolean SHOW_STATE_PARTICLES = false;
        public static final boolean ENABLE_SPAWN_COMMANDS = true;
    }
}