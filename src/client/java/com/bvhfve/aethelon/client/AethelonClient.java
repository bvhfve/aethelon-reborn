package com.bvhfve.aethelon.client;

import com.bvhfve.aethelon.client.model.ModEntityModelLayers;
import com.bvhfve.aethelon.client.render.AethelonEntityRenderer;
import com.bvhfve.aethelon.registry.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

/**
 * Client-side initialization for Aethelon mod
 * Handles entity renderers and client-specific setup
 */
public class AethelonClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        // Register entity model layers
        ModEntityModelLayers.registerModelLayers();
        
        // Register entity renderer
        EntityRendererRegistry.register(ModEntityTypes.AETHELON, AethelonEntityRenderer::new);
    }
}