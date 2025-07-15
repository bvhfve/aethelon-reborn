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
    
    public AethelonEntityModel(ModelPart root) {
        super(root, RenderLayer::getEntityCutoutNoCull);
        this.root = root;
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.shell = root.getChild("shell");
        this.frontLeftLeg = root.getChild("front_left_leg");
        this.frontRightLeg = root.getChild("front_right_leg");
        this.backLeftLeg = root.getChild("back_left_leg");
        this.backRightLeg = root.getChild("back_right_leg");
    }
    
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        // Body - massive turtle body (scaled for world turtle)
        modelPartData.addChild("body", 
            ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-12.0F, -8.0F, -16.0F, 24.0F, 16.0F, 32.0F), // 4x larger
            ModelTransform.pivot(0.0F, 20.0F, 0.0F));
        
        // Head - massive turtle head extending forward
        modelPartData.addChild("head",
            ModelPartBuilder.create()
                .uv(0, 48)
                .cuboid(-6.0F, -4.0F, -12.0F, 12.0F, 8.0F, 12.0F), // 4x larger
            ModelTransform.pivot(0.0F, 16.0F, -16.0F));
        
        // Shell - enormous shell on back (where island will sit)
        modelPartData.addChild("shell",
            ModelPartBuilder.create()
                .uv(80, 0)
                .cuboid(-16.0F, -12.0F, -20.0F, 32.0F, 12.0F, 40.0F), // 4x larger
            ModelTransform.pivot(0.0F, 12.0F, 0.0F));
        
        // Legs - massive turtle flippers
        modelPartData.addChild("front_left_leg",
            ModelPartBuilder.create()
                .uv(0, 68)
                .cuboid(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F), // 4x larger
            ModelTransform.pivot(8.0F, 20.0F, -8.0F));
        
        modelPartData.addChild("front_right_leg",
            ModelPartBuilder.create()
                .uv(0, 68)
                .cuboid(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F), // 4x larger
            ModelTransform.pivot(-8.0F, 20.0F, -8.0F));
        
        modelPartData.addChild("back_left_leg",
            ModelPartBuilder.create()
                .uv(0, 68)
                .cuboid(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F), // 4x larger
            ModelTransform.pivot(8.0F, 20.0F, 8.0F));
        
        modelPartData.addChild("back_right_leg",
            ModelPartBuilder.create()
                .uv(0, 68)
                .cuboid(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F), // 4x larger
            ModelTransform.pivot(-8.0F, 20.0F, 8.0F));
        
        return TexturedModelData.of(modelData, 256, 128); // Larger texture for massive model
    }
    
    @Override
    public void setAngles(LivingEntityRenderState renderState) {
        super.setAngles(renderState);
        
        // Basic swimming animation using render state
        this.head.yaw = renderState.yawDegrees * 0.017453292F;
        this.head.pitch = renderState.pitch * 0.017453292F;
        
        // Gentle leg movement for swimming
        float legSwing = renderState.limbFrequency * 0.6662F;
        float legAmount = renderState.limbAmplitudeMultiplier * 0.5F;
        
        this.frontLeftLeg.roll = (float) Math.cos(legSwing) * legAmount;
        this.frontRightLeg.roll = (float) Math.cos(legSwing + Math.PI) * legAmount;
        this.backLeftLeg.roll = (float) Math.cos(legSwing + Math.PI) * legAmount;
        this.backRightLeg.roll = (float) Math.cos(legSwing) * legAmount;
    }
}