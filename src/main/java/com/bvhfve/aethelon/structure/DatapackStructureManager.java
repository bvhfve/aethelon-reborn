package com.bvhfve.aethelon.structure;

import com.bvhfve.aethelon.Aethelon;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

/**
 * Simplified structure manager that uses datapack structures instead of complex NBT handling
 * This avoids problematic StructureBlockInfo APIs and leverages Minecraft's built-in structure system
 */
public class DatapackStructureManager {
    
    private static final Map<String, IslandStructureDefinition> REGISTERED_STRUCTURES = new HashMap<>();
    
    /**
     * Simple structure definition for datapack-based structures
     */
    public static class IslandStructureDefinition {
        public final String name;
        public final Identifier structureId;
        public final Vec3d size;
        public final int weight;
        public final Set<String> biomeCategories;
        public final boolean allowRotation;
        public final boolean allowMirroring;
        
        public IslandStructureDefinition(String name, Identifier structureId, Vec3d size, 
                                       int weight, Set<String> biomeCategories, 
                                       boolean allowRotation, boolean allowMirroring) {
            this.name = name;
            this.structureId = structureId;
            this.size = size;
            this.weight = weight;
            this.biomeCategories = biomeCategories;
            this.allowRotation = allowRotation;
            this.allowMirroring = allowMirroring;
        }
    }
    
    /**
     * Structure spawn result
     */
    public static class StructureSpawnResult {
        public final boolean success;
        public final BlockPos position;
        public final IslandStructureDefinition definition;
        public final String errorMessage;
        
        private StructureSpawnResult(boolean success, BlockPos position, 
                                   IslandStructureDefinition definition, String errorMessage) {
            this.success = success;
            this.position = position;
            this.definition = definition;
            this.errorMessage = errorMessage;
        }
        
        public static StructureSpawnResult success(BlockPos position, IslandStructureDefinition definition) {
            return new StructureSpawnResult(true, position, definition, null);
        }
        
        public static StructureSpawnResult failure(String errorMessage) {
            return new StructureSpawnResult(false, null, null, errorMessage);
        }
    }
    
    /**
     * Initialize with default datapack structures
     */
    public static void initialize() {
        Aethelon.LOGGER.info("Initializing Datapack Structure Manager...");
        
        // Register small island structures
        registerStructure(new IslandStructureDefinition(
            "small_island",
            Identifier.of(Aethelon.MOD_ID, "islands/small/small_island"),
            new Vec3d(16, 8, 16),
            100,
            Set.of("ocean", "deep_ocean", "warm_ocean", "lukewarm_ocean", "cold_ocean"),
            true, true
        ));
        
        registerStructure(new IslandStructureDefinition(
            "small_tropical_island",
            Identifier.of(Aethelon.MOD_ID, "islands/small/small_tropical_island"),
            new Vec3d(16, 8, 16),
            80,
            Set.of("warm_ocean", "lukewarm_ocean"),
            true, true
        ));
        
        registerStructure(new IslandStructureDefinition(
            "small_rocky_island",
            Identifier.of(Aethelon.MOD_ID, "islands/small/small_rocky_island"),
            new Vec3d(16, 10, 16),
            70,
            Set.of("ocean", "deep_ocean", "cold_ocean"),
            true, true
        ));
        
        registerStructure(new IslandStructureDefinition(
            "small_coral_island",
            Identifier.of(Aethelon.MOD_ID, "islands/small/small_coral_island"),
            new Vec3d(16, 6, 16),
            60,
            Set.of("warm_ocean"),
            true, true
        ));
        
        // Register medium island structures
        registerStructure(new IslandStructureDefinition(
            "medium_island",
            Identifier.of(Aethelon.MOD_ID, "islands/medium/medium_island"),
            new Vec3d(24, 12, 24),
            60,
            Set.of("ocean", "deep_ocean", "warm_ocean", "lukewarm_ocean", "cold_ocean"),
            true, true
        ));
        
        registerStructure(new IslandStructureDefinition(
            "medium_forest_island",
            Identifier.of(Aethelon.MOD_ID, "islands/medium/medium_forest_island"),
            new Vec3d(24, 14, 24),
            50,
            Set.of("ocean", "deep_ocean", "lukewarm_ocean"),
            true, true
        ));
        
        registerStructure(new IslandStructureDefinition(
            "medium_desert_island",
            Identifier.of(Aethelon.MOD_ID, "islands/medium/medium_desert_island"),
            new Vec3d(24, 10, 24),
            40,
            Set.of("warm_ocean"),
            true, true
        ));
        
        registerStructure(new IslandStructureDefinition(
            "medium_village_island",
            Identifier.of(Aethelon.MOD_ID, "islands/medium/medium_village_island"),
            new Vec3d(24, 16, 24),
            20,
            Set.of("ocean", "deep_ocean", "warm_ocean", "lukewarm_ocean"),
            false, false
        ));
        
        // Register large island structures
        registerStructure(new IslandStructureDefinition(
            "large_island",
            Identifier.of(Aethelon.MOD_ID, "islands/large/large_island"),
            new Vec3d(32, 16, 32),
            30,
            Set.of("ocean", "deep_ocean"),
            true, true
        ));
        
        registerStructure(new IslandStructureDefinition(
            "large_village_island",
            Identifier.of(Aethelon.MOD_ID, "islands/large/large_village_island"),
            new Vec3d(32, 20, 32),
            10,
            Set.of("ocean", "deep_ocean", "warm_ocean", "lukewarm_ocean"),
            false, false
        ));
        
        // Register special island structures
        registerStructure(new IslandStructureDefinition(
            "boss_island",
            Identifier.of(Aethelon.MOD_ID, "special/boss_island"),
            new Vec3d(40, 24, 40),
            5,
            Set.of("deep_ocean"),
            false, false
        ));
        
        registerStructure(new IslandStructureDefinition(
            "treasure_island",
            Identifier.of(Aethelon.MOD_ID, "special/treasure_island"),
            new Vec3d(28, 18, 28),
            8,
            Set.of("ocean", "deep_ocean", "warm_ocean"),
            true, false
        ));
        
        registerStructure(new IslandStructureDefinition(
            "mystical_island",
            Identifier.of(Aethelon.MOD_ID, "special/mystical_island"),
            new Vec3d(36, 22, 36),
            3,
            Set.of("deep_ocean"),
            false, false
        ));
        
        Aethelon.LOGGER.info("Registered {} datapack structures", REGISTERED_STRUCTURES.size());
    }
    
    /**
     * Register a structure definition
     */
    public static void registerStructure(IslandStructureDefinition definition) {
        REGISTERED_STRUCTURES.put(definition.name, definition);
        Aethelon.LOGGER.debug("Registered datapack structure: {} -> {}", definition.name, definition.structureId);
    }
    
    /**
     * Spawn a structure using Minecraft's built-in structure system
     */
    public static StructureSpawnResult spawnStructure(ServerWorld world, String structureName, BlockPos position) {
        IslandStructureDefinition definition = REGISTERED_STRUCTURES.get(structureName);
        if (definition == null) {
            return StructureSpawnResult.failure("Structure not found: " + structureName);
        }
        
        return spawnStructure(world, definition, position);
    }
    
    /**
     * Spawn a structure using its definition
     */
    public static StructureSpawnResult spawnStructure(ServerWorld world, IslandStructureDefinition definition, BlockPos position) {
        try {
            StructureTemplateManager templateManager = world.getStructureTemplateManager();
            Optional<StructureTemplate> templateOpt = templateManager.getTemplate(definition.structureId);
            
            if (templateOpt.isEmpty()) {
                Aethelon.LOGGER.warn("Structure template not found: {}", definition.structureId);
                return StructureSpawnResult.failure("Template not found: " + definition.structureId);
            }
            
            StructureTemplate template = templateOpt.get();
            
            // Determine rotation and mirroring
            BlockRotation rotation = definition.allowRotation ? 
                BlockRotation.values()[world.getRandom().nextInt(BlockRotation.values().length)] : 
                BlockRotation.NONE;
                
            BlockMirror mirror = definition.allowMirroring ? 
                (world.getRandom().nextBoolean() ? BlockMirror.FRONT_BACK : BlockMirror.NONE) : 
                BlockMirror.NONE;
            
            // Create placement data
            StructurePlacementData placementData = new StructurePlacementData()
                    .setRotation(rotation)
                    .setMirror(mirror)
                    .setIgnoreEntities(false);
            
            // Place the structure using Minecraft's built-in system
            template.place(world, position, position, placementData, world.getRandom(), 2);
            
            Aethelon.LOGGER.info("Successfully spawned datapack structure: {} at {}", definition.name, position);
            return StructureSpawnResult.success(position, definition);
            
        } catch (Exception e) {
            String errorMsg = "Failed to spawn structure: " + definition.name + " - " + e.getMessage();
            Aethelon.LOGGER.error(errorMsg, e);
            return StructureSpawnResult.failure(errorMsg);
        }
    }
    
    /**
     * Spawn a random structure from a category
     */
    public static StructureSpawnResult spawnRandomStructure(ServerWorld world, String sizeCategory, 
                                                          BlockPos position, String biomeCategory) {
        List<IslandStructureDefinition> candidates = REGISTERED_STRUCTURES.values().stream()
                .filter(def -> categorizeBySize(def.size).equals(sizeCategory))
                .filter(def -> def.biomeCategories.contains(biomeCategory))
                .toList();
        
        if (candidates.isEmpty()) {
            return StructureSpawnResult.failure("No structures found for category: " + sizeCategory + " in biome: " + biomeCategory);
        }
        
        // Weighted random selection
        int totalWeight = candidates.stream().mapToInt(def -> def.weight).sum();
        int randomWeight = world.getRandom().nextInt(totalWeight);
        
        int currentWeight = 0;
        for (IslandStructureDefinition definition : candidates) {
            currentWeight += definition.weight;
            if (randomWeight < currentWeight) {
                return spawnStructure(world, definition, position);
            }
        }
        
        // Fallback to first candidate
        return spawnStructure(world, candidates.get(0), position);
    }
    
    /**
     * Categorize structure by size
     */
    private static String categorizeBySize(Vec3d size) {
        double maxDimension = Math.max(Math.max(size.x, size.y), size.z);
        if (maxDimension <= 16) return "small";
        if (maxDimension <= 24) return "medium";
        return "large";
    }
    
    /**
     * Get all registered structure names
     */
    public static Set<String> getRegisteredStructureNames() {
        return new HashSet<>(REGISTERED_STRUCTURES.keySet());
    }
    
    /**
     * Get structure definition by name
     */
    public static IslandStructureDefinition getStructureDefinition(String name) {
        return REGISTERED_STRUCTURES.get(name);
    }
    
    /**
     * Check if a structure exists
     */
    public static boolean hasStructure(String name) {
        return REGISTERED_STRUCTURES.containsKey(name);
    }
}