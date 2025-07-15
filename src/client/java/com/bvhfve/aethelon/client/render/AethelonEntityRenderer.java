package com.bvhfve.aethelon.client.render;

import com.bvhfve.aethelon.Aethelon;
import com.bvhfve.aethelon.client.model.AethelonEntityModel;
import com.bvhfve.aethelon.entity.AethelonEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * Renderer for the Aethelon entity
 * Handles the visual representation of the world turtle
 * Updated for Minecraft 1.21.4 render state system
 * Performance optimized with distance-based rendering and shadow management
 */
public class AethelonEntityRenderer extends MobEntityRenderer<AethelonEntity, LivingEntityRenderState, AethelonEntityModel> {
    
    private static final Identifier TEXTURE = Identifier.of(Aethelon.MOD_ID, "textures/entity/aethelon.png");
    private static final Identifier TEXTURE_LOD = Identifier.of(Aethelon.MOD_ID, "textures/entity/aethelon_lod.png");
    
    // Performance optimization constants
    private static final float MAX_RENDER_DISTANCE = 256.0f;
    private static final float LOD_SWITCH_DISTANCE = 64.0f;
    private static final float SHADOW_DISTANCE = 32.0f;
    
    public AethelonEntityRenderer(EntityRendererFactory.Context context) {
        // Reduced shadow radius for performance
        super(context, new AethelonEntityModel(context.getPart(com.bvhfve.aethelon.client.model.ModEntityModelLayers.AETHELON)), 8.0f);
    }
    
    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
    
    @Override
    public Identifier getTexture(LivingEntityRenderState renderState) {
        // Use LOD texture for distant entities
        float distance = getDistanceToCamera(renderState);
        return distance > LOD_SWITCH_DISTANCE ? TEXTURE_LOD : TEXTURE;
    }
    
    @Override
    protected float getShadowRadius(LivingEntityRenderState renderState) {
        // Dynamic shadow radius based on distance
        float distance = getDistanceToCamera(renderState);
        if (distance > SHADOW_DISTANCE) {
            return 0.0f; // No shadow for distant entities
        }
        return super.getShadowRadius(renderState) * (1.0f - distance / SHADOW_DISTANCE);
    }
    
    // Remove @Override as this method signature doesn't exist in parent class
    protected boolean isEntityVisible(LivingEntityRenderState renderState) {
        // CRITICAL: Aggressive distance culling
        float distance = getDistanceToCamera(renderState);
        
        // Don't render if too far away
        if (distance > MAX_RENDER_DISTANCE) {
            return false;
        }
        
        // PERFORMANCE: Skip expensive visibility checks for distant entities
        if (distance > 64.0f) {
            return true; // Assume visible to skip expensive frustum culling
        }
        
        return true; // Default to visible
    }
    
    // Custom render distance check - called from render method
    public boolean shouldRenderAtDistance(LivingEntityRenderState renderState, net.minecraft.client.render.Frustum frustum, double x, double y, double z) {
        float distance = getDistanceToCamera(renderState);
        
        // CRITICAL: Don't render very distant entities at all
        if (distance > MAX_RENDER_DISTANCE) {
            return false;
        }
        
        // Skip frustum culling for distant entities (expensive operation)
        if (distance > 64.0f) {
            return true;
        }
        
        return true; // Default to render
    }
    
    /**
     * Calculate distance to camera for LOD determination
     */
    private float getDistanceToCamera(LivingEntityRenderState renderState) {
        var client = net.minecraft.client.MinecraftClient.getInstance();
        if (client.player != null) {
            float dx = (float) (renderState.x - client.player.getX());
            float dz = (float) (renderState.z - client.player.getZ());
            return (float) Math.sqrt(dx * dx + dz * dz);
        }
        return 0.0f;
    }
}