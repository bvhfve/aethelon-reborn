package com.bvhfve.aethelon;

import com.bvhfve.aethelon.compat.ModCompatibility;
import com.bvhfve.aethelon.config.AethelonConfig;
import com.bvhfve.aethelon.registry.ModBiomeModifications;
import com.bvhfve.aethelon.registry.ModEntityTypes;
import com.bvhfve.aethelon.registry.ModItemGroups;
import com.bvhfve.aethelon.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main mod class for Aethelon - Giant Sea Turtles with Islands
 * 
 * This mod adds massive sea turtles that carry islands on their backs,
 * creating mobile ocean bases and unique exploration opportunities.
 */
public class Aethelon implements ModInitializer {
    public static final String MOD_ID = "aethelon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Aethelon - Giant Sea Turtles mod...");

        try {
            // Initialize configuration
            AethelonConfig.initialize();
            
            // Register mod content
            ModItems.initialize();
            ModEntityTypes.initialize();
            ModItemGroups.initialize();
            ModBiomeModifications.initialize();
            
            // Initialize mod compatibility system
            ModCompatibility.initialize();
            
            LOGGER.info("Aethelon mod initialized successfully!");
            
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Aethelon mod: {}", e.getMessage(), e);
            throw new RuntimeException("Aethelon mod initialization failed", e);
        }
    }
}