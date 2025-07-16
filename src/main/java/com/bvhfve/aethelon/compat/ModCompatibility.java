package com.bvhfve.aethelon.compat;

import com.bvhfve.aethelon.Aethelon;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Central compatibility manager for popular ocean and exploration mods
 * 
 * Handles detection and integration with:
 * - Aquaculture 2 (fishing and aquatic content)
 * - Biomes O' Plenty (ocean biomes)
 * - Alex's Mobs (aquatic creatures)
 * - Supplementaries (exploration items)
 * - Waystones (exploration and travel)
 * - Ocean Floor (ocean generation)
 * - Aquatic Torches (underwater lighting)
 * - Upgrade Aquatic (ocean overhaul)
 * - Fins and Tails (aquatic mobs)
 * - Deeper and Darker (deep ocean exploration)
 */
public class ModCompatibility {
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonCompat");
    
    // Popular ocean/exploration mod IDs
    public static final String AQUACULTURE = "aquaculture";
    public static final String BIOMES_O_PLENTY = "biomesoplenty";
    public static final String ALEXS_MOBS = "alexsmobs";
    public static final String SUPPLEMENTARIES = "supplementaries";
    public static final String WAYSTONES = "waystones";
    public static final String OCEAN_FLOOR = "oceanfloor";
    public static final String AQUATIC_TORCHES = "aquatictorches";
    public static final String UPGRADE_AQUATIC = "upgrade_aquatic";
    public static final String FINS_AND_TAILS = "fins";
    public static final String DEEPER_AND_DARKER = "deeperdarker";
    public static final String TERRALITH = "terralith";
    public static final String WILLIAM_WYTHERS = "wwoo";
    public static final String ECOLOGICS = "ecologics";
    public static final String NATURALIST = "naturalist";
    
    // Compatibility handlers
    private static final Map<String, Supplier<Boolean>> compatHandlers = new HashMap<>();
    private static final Map<String, Boolean> modPresence = new HashMap<>();
    
    /**
     * Initialize compatibility system
     */
    public static void initialize() {
        LOGGER.info("Initializing Aethelon mod compatibility system...");
        
        // Register compatibility handlers
        registerCompatibilityHandlers();
        
        // Detect installed mods
        detectInstalledMods();
        
        // Initialize compatibility features
        initializeCompatibilityFeatures();
        
        LOGGER.info("Aethelon compatibility system initialized successfully!");
    }
    
    /**
     * Register all compatibility handlers
     */
    private static void registerCompatibilityHandlers() {
        compatHandlers.put(AQUACULTURE, AquacultureCompat::initialize);
        compatHandlers.put(BIOMES_O_PLENTY, BiomesOPlentyCompat::initialize);
        compatHandlers.put(ALEXS_MOBS, AlexsMobsCompat::initialize);
        compatHandlers.put(SUPPLEMENTARIES, SupplementariesCompat::initialize);
        compatHandlers.put(WAYSTONES, WaystonesCompat::initialize);
        compatHandlers.put(OCEAN_FLOOR, OceanFloorCompat::initialize);
        compatHandlers.put(AQUATIC_TORCHES, AquaticTorchesCompat::initialize);
        compatHandlers.put(UPGRADE_AQUATIC, UpgradeAquaticCompat::initialize);
        compatHandlers.put(FINS_AND_TAILS, FinsAndTailsCompat::initialize);
        compatHandlers.put(DEEPER_AND_DARKER, DeeperAndDarkerCompat::initialize);
        compatHandlers.put(TERRALITH, TerrablenderCompat::initialize);
        compatHandlers.put(WILLIAM_WYTHERS, WilliamWythersCompat::initialize);
        compatHandlers.put(ECOLOGICS, EcologicsCompat::initialize);
        compatHandlers.put(NATURALIST, NaturalistCompat::initialize);
    }
    
    /**
     * Detect which compatible mods are installed
     */
    private static void detectInstalledMods() {
        for (String modId : compatHandlers.keySet()) {
            boolean isPresent = FabricLoader.getInstance().isModLoaded(modId);
            modPresence.put(modId, isPresent);
            
            if (isPresent) {
                LOGGER.info("Detected compatible mod: {}", modId);
            }
        }
    }
    
    /**
     * Initialize compatibility features for detected mods
     */
    private static void initializeCompatibilityFeatures() {
        for (Map.Entry<String, Boolean> entry : modPresence.entrySet()) {
            if (entry.getValue()) {
                String modId = entry.getKey();
                try {
                    boolean success = compatHandlers.get(modId).get();
                    if (success) {
                        LOGGER.info("Successfully initialized compatibility for {}", modId);
                    } else {
                        LOGGER.warn("Failed to initialize compatibility for {}", modId);
                    }
                } catch (Exception e) {
                    LOGGER.error("Error initializing compatibility for {}: {}", modId, e.getMessage());
                }
            }
        }
    }
    
    /**
     * Check if a specific mod is loaded
     */
    public static boolean isModLoaded(String modId) {
        return modPresence.getOrDefault(modId, false);
    }
    
    /**
     * Get all loaded compatible mods
     */
    public static Map<String, Boolean> getLoadedMods() {
        return new HashMap<>(modPresence);
    }
    
    /**
     * Check if any ocean/exploration mods are loaded
     */
    public static boolean hasOceanMods() {
        return modPresence.values().stream().anyMatch(Boolean::booleanValue);
    }
}