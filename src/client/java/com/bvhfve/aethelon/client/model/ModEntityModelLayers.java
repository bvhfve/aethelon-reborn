package com.bvhfve.aethelon.client.model;

import com.bvhfve.aethelon.Aethelon;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

/**
 * Registry for entity model layers
 * Handles registration of custom entity models
 */
public class ModEntityModelLayers {
    
    public static final EntityModelLayer AETHELON = new EntityModelLayer(
            Identifier.of(Aethelon.MOD_ID, "aethelon"), "main");
    
    /**
     * Registers all entity model layers
     * Called during client initialization
     */
    public static void registerModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(AETHELON, AethelonEntityModel::getTexturedModelData);
    }
}