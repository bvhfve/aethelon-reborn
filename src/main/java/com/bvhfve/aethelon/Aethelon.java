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
 */
public class Aethelon implements ModInitializer {
    public static final String MOD_ID = "aethelon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Aethelon - The World Turtle mod");
        
        // Phase 1: Basic Entity Foundation
        ModEntityTypes.registerEntityTypes();
        ModItems.registerItems();
        ModBiomeModifications.registerSpawnConditions();
        
        LOGGER.info("Aethelon mod initialization complete");
    }
}