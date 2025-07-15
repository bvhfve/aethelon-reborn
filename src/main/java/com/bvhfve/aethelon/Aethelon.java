package com.bvhfve.aethelon;

import com.bvhfve.aethelon.registry.ModBiomeModifications;
import com.bvhfve.aethelon.registry.ModEntityTypes;
import com.bvhfve.aethelon.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main mod class for Aethelon - The World Turtle mod
 * 
 * This mod introduces colossal turtle entities that carry functional islands
 * on their backs, creating a unique and dynamic world experience.
 * 
 * Based on best practices from knowledge pool examples:
 * - Proper initialization order with validation
 * - Comprehensive error handling
 * - Phase-based development approach
 */
public class Aethelon implements ModInitializer {
    public static final String MOD_ID = "aethelon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Aethelon - The World Turtle mod v{}", getModVersion());
        
        try {
            // Phase 1: Basic Entity Foundation
            // Order is critical: entities -> items -> biome modifications
            
            LOGGER.info("Phase 1: Registering core components...");
            
            // Step 1: Register entity types first (required for items)
            ModEntityTypes.registerEntityTypes();
            ModEntityTypes.validateRegistration();
            
            // Step 2: Register items (depends on entity types)
            ModItems.registerItems();
            ModItems.validateRegistration();
            
            // Step 3: Register biome modifications (depends on entity types)
            ModBiomeModifications.registerSpawnConditions();
            
            LOGGER.info("Phase 1 initialization complete - {} entities, {} items registered", 
                       1, 1);
            
            // TODO: Phase 2 - Entity Behavior & Movement (AI, pathfinding, swimming)
            // TODO: Phase 3 - Damage Response & Player Interaction
            // TODO: Phase 4 - Island Structure System
            
            LOGGER.info("Aethelon mod initialization successful!");
            
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Aethelon mod", e);
            throw new RuntimeException("Mod initialization failed", e);
        }
    }
    
    /**
     * Gets the mod version for logging purposes
     * @return mod version string
     */
    private String getModVersion() {
        // In a real implementation, this would read from fabric.mod.json
        return "1.0.0-SNAPSHOT";
    }
}