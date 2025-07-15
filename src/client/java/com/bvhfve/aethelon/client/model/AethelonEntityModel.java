package com.bvhfve.aethelon.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

/**
 * Model for the Aethelon entity
 * Defines the 3D structure of the world turtle
 * Updated for Minecraft 1.21.4 render state system
 * Performance optimized with LOD and animation caching
 */
public class AethelonEntityModel extends EntityModel<LivingEntityRenderState> {
    
    // Layer location is now handled by ModEntityModelLayers
    
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart shell;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart backLeftLeg;
    private final ModelPart backRightLeg;
    
    // Performance optimization fields
    private static final float LOD_DISTANCE_NEAR = 32.0f;
    private static final float LOD_DISTANCE_FAR = 128.0f;
    private static final int ANIMATION_CACHE_INTERVAL = 4; // Cache every 4 frames
    private int animationCacheCounter = 0;
    private float cachedLegSwing = 0.0f;
    private float cachedLegAmount = 0.0f;
    
    public AethelonEntityModel(ModelPart root) {
        super(root, RenderLayer::getEntityCutoutNoCull);
        this.root = root;
        
        // Initialize model parts with error handling
        try {
            this.body = root.getChild("body");
            this.head = root.getChild("head");
            this.shell = root.getChild("shell");
            this.frontLeftLeg = root.getChild("front_left_leg");
            this.frontRightLeg = root.getChild("front_right_leg");
            this.backLeftLeg = root.getChild("back_left_leg");
            this.backRightLeg = root.getChild("back_right_leg");
        } catch (Exception e) {
            System.err.println("Failed to initialize Aethelon model parts: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Aethelon model initialization failed", e);
        }
    }
    
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        // Body - Main turtle body
        modelPartData.addChild("body", 
            ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-10.0F, -4.0F, -14.0F, 20.0F, 8.0F, 28.0F), // Proper proportions
            ModelTransform.pivot(0.0F, 18.0F, 0.0F));
        
        // Head - Turtle head extending forward
        modelPartData.addChild("head",
            ModelPartBuilder.create()
                .uv(0, 36)
                .cuboid(-5.0F, -3.0F, -10.0F, 10.0F, 6.0F, 10.0F), // Proper head size
            ModelTransform.pivot(0.0F, 16.0F, -14.0F));
        
        // Shell - Large shell on back (where island sits)
        modelPartData.addChild("shell",
            ModelPartBuilder.create()
                .uv(80, 0)
                .cuboid(-14.0F, -6.0F, -18.0F, 28.0F, 6.0F, 36.0F), // Large shell for island
            ModelTransform.pivot(0.0F, 14.0F, 0.0F));
        
        // Legs - Turtle flippers
        modelPartData.addChild("front_left_leg",
            ModelPartBuilder.create()
                .uv(0, 52)
                .cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F), // Proper flipper size
            ModelTransform.pivot(8.0F, 18.0F, -8.0F));
        
        modelPartData.addChild("front_right_leg",
            ModelPartBuilder.create()
                .uv(24, 52)
                .cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F), // Proper flipper size
            ModelTransform.pivot(-8.0F, 18.0F, -8.0F));
        
        modelPartData.addChild("back_left_leg",
            ModelPartBuilder.create()
                .uv(48, 52)
                .cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F), // Proper flipper size
            ModelTransform.pivot(8.0F, 18.0F, 8.0F));
        
        modelPartData.addChild("back_right_leg",
            ModelPartBuilder.create()
                .uv(72, 52)
                .cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F), // Proper flipper size
            ModelTransform.pivot(-8.0F, 18.0F, 8.0F));
        
        return TexturedModelData.of(modelData, 256, 128); // Proper texture size for model
    }
    
    @Override
    public void setAngles(LivingEntityRenderState renderState) {
        super.setAngles(renderState);
        
        // Performance optimization: Determine LOD level based on distance
        float distanceToCamera = getDistanceToCamera(renderState);
        boolean isNearLOD = distanceToCamera < LOD_DISTANCE_NEAR;
        boolean isMidLOD = distanceToCamera < LOD_DISTANCE_FAR;
        
        // Always update head rotation (most visible)
        this.head.yaw = renderState.yawDegrees * 0.017453292F;
        this.head.pitch = renderState.pitch * 0.017453292F;
        
        // Only update leg animations for near/mid LOD and cache results
        if (isMidLOD) {
            animationCacheCounter++;
            
            // Update animation cache less frequently for distant entities
            if (isNearLOD || animationCacheCounter % ANIMATION_CACHE_INTERVAL == 0) {
                cachedLegSwing = renderState.limbFrequency * 0.6662F;
                cachedLegAmount = renderState.limbAmplitudeMultiplier * 0.5F;
            }
            
            // Apply cached animation values
            this.frontLeftLeg.roll = (float) Math.cos(cachedLegSwing) * cachedLegAmount;
            this.frontRightLeg.roll = (float) Math.cos(cachedLegSwing + Math.PI) * cachedLegAmount;
            this.backLeftLeg.roll = (float) Math.cos(cachedLegSwing + Math.PI) * cachedLegAmount;
            this.backRightLeg.roll = (float) Math.cos(cachedLegSwing) * cachedLegAmount;
        } else {
            // Far LOD: No leg animations to save performance
            this.frontLeftLeg.roll = 0.0f;
            this.frontRightLeg.roll = 0.0f;
            this.backLeftLeg.roll = 0.0f;
            this.backRightLeg.roll = 0.0f;
        }
    }
    
    /**
     * Calculate distance to camera for LOD determination
     * Uses approximation for performance
     */
    private float getDistanceToCamera(LivingEntityRenderState renderState) {
        // Simple distance approximation - in a real implementation,
        // this would use the actual camera position
        return (float)(Math.abs(renderState.x) + Math.abs(renderState.z));
    }
}