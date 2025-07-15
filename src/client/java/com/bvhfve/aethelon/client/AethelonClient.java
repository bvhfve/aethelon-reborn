package com.bvhfve.aethelon.client;

import com.bvhfve.aethelon.Aethelon;
import com.bvhfve.aethelon.client.model.ModEntityModelLayers;
import com.bvhfve.aethelon.client.render.AethelonEntityRenderer;
import com.bvhfve.aethelon.registry.ModEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

/**
 * Client-side initialization for Aethelon mod
 * Handles entity renderers and client-specific setup
 * 
 * Based on patterns from knowledge pool examples:
 * - Proper initialization order
 * - Comprehensive error handling
 * - Modular registration methods
 */
public class AethelonClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        Aethelon.LOGGER.info("Initializing Aethelon client-side features...");
        
        try {
            // Register entity model layers first
            registerModelLayers();
            
            // Register entity renderers
            registerEntityRenderers();
            
            // TODO: Register particle factories (Phase 4+)
            // TODO: Register key bindings (Phase 3+)
            // TODO: Register screen handlers (Phase 4+)
            
            Aethelon.LOGGER.info("Aethelon client initialization successful!");
            
        } catch (Exception e) {
            Aethelon.LOGGER.error("Failed to initialize Aethelon client", e);
            throw new RuntimeException("Client initialization failed", e);
        }
    }
    
    /**
     * Registers all entity model layers
     * Must be called before entity renderers
     */
    private void registerModelLayers() {
        Aethelon.LOGGER.debug("Registering entity model layers");
        ModEntityModelLayers.registerModelLayers();
    }
    
    /**
     * Registers all entity renderers
     * Depends on model layers being registered first
     */
    private void registerEntityRenderers() {
        Aethelon.LOGGER.debug("Registering entity renderers");
        
        EntityRendererRegistry.register(ModEntityTypes.AETHELON, AethelonEntityRenderer::new);
        
        Aethelon.LOGGER.debug("Successfully registered {} entity renderers", 1);
    }
}