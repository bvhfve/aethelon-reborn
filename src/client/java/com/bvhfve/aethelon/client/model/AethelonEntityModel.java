package com.bvhfve.aethelon.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;

/**
 * Model for the Aethelon entity - a massive world turtle
 * Simplified model for performance with the large entity size
 */
public class AethelonEntityModel extends EntityModel<LivingEntityRenderState> {
    
    private final ModelPart root;
    private final ModelPart shell;
    private final ModelPart head;
    private final ModelPart legs;

    public AethelonEntityModel(ModelPart root) {
        super(root);
        this.root = root;
        this.shell = root.getChild("shell");
        this.head = root.getChild("head");
        this.legs = root.getChild("legs");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        // Back to massive model approach - this works better for textures
        // Shell - main body (large and flat for island carrying)
        modelPartData.addChild("shell", 
            ModelPartBuilder.create()
                .uv(0, 0).cuboid(-16.0F, -8.0F, -14.0F, 32.0F, 8.0F, 28.0F, new Dilation(0.0F)), 
            ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        // Head - larger turtle head
        modelPartData.addChild("head", 
            ModelPartBuilder.create()
                .uv(0, 36).cuboid(-4.0F, -4.0F, -8.0F, 8.0F, 6.0F, 8.0F, new Dilation(0.0F)), 
            ModelTransform.pivot(0.0F, 18.0F, -14.0F));

        // Legs - larger legs
        modelPartData.addChild("legs", 
            ModelPartBuilder.create()
                .uv(32, 36).cuboid(-12.0F, 0.0F, -10.0F, 6.0F, 8.0F, 6.0F, new Dilation(0.0F)) // Front left
                .uv(56, 36).cuboid(6.0F, 0.0F, -10.0F, 6.0F, 8.0F, 6.0F, new Dilation(0.0F))  // Front right
                .uv(32, 50).cuboid(-12.0F, 0.0F, 4.0F, 6.0F, 8.0F, 6.0F, new Dilation(0.0F))   // Back left
                .uv(56, 50).cuboid(6.0F, 0.0F, 4.0F, 6.0F, 8.0F, 6.0F, new Dilation(0.0F)),    // Back right
            ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        return TexturedModelData.of(modelData, 256, 128); // Match your existing texture files
    }

    @Override
    public void setAngles(LivingEntityRenderState renderState) {
        // Simple animation - mostly static due to massive size
        // Slight head movement
        this.head.yaw = renderState.yawDegrees * 0.017453292F * 0.1F;
        
        // Very subtle leg animation for walking
        float limbSwing = renderState.limbFrequency;
        float limbSwingAmount = renderState.limbAmplitudeMultiplier;
        
        // Minimal leg movement due to massive size
        this.legs.pitch = limbSwing * limbSwingAmount * 0.1F;
    }
}