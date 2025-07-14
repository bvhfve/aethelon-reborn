package com.bvhfve.aethelon.client.model;

import com.bvhfve.aethelon.Aethelon;
import com.bvhfve.aethelon.entity.AethelonEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

/**
 * Model for the Aethelon entity
 * Defines the 3D structure of the world turtle
 */
public class AethelonEntityModel<T extends AethelonEntity> extends EntityModel<T> {
    
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
        
        // Body - main turtle body
        modelPartData.addChild("body", 
            ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-6.0F, -4.0F, -8.0F, 12.0F, 8.0F, 16.0F),
            ModelTransform.pivot(0.0F, 20.0F, 0.0F));
        
        // Head - turtle head extending forward
        modelPartData.addChild("head",
            ModelPartBuilder.create()
                .uv(0, 24)
                .cuboid(-3.0F, -2.0F, -6.0F, 6.0F, 4.0F, 6.0F),
            ModelTransform.pivot(0.0F, 18.0F, -8.0F));
        
        // Shell - large shell on back (where island will sit)
        modelPartData.addChild("shell",
            ModelPartBuilder.create()
                .uv(40, 0)
                .cuboid(-8.0F, -6.0F, -10.0F, 16.0F, 6.0F, 20.0F),
            ModelTransform.pivot(0.0F, 16.0F, 0.0F));
        
        // Legs - four turtle flippers
        modelPartData.addChild("front_left_leg",
            ModelPartBuilder.create()
                .uv(0, 34)
                .cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F),
            ModelTransform.pivot(4.0F, 20.0F, -4.0F));
        
        modelPartData.addChild("front_right_leg",
            ModelPartBuilder.create()
                .uv(0, 34)
                .cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F),
            ModelTransform.pivot(-4.0F, 20.0F, -4.0F));
        
        modelPartData.addChild("back_left_leg",
            ModelPartBuilder.create()
                .uv(0, 34)
                .cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F),
            ModelTransform.pivot(4.0F, 20.0F, 4.0F));
        
        modelPartData.addChild("back_right_leg",
            ModelPartBuilder.create()
                .uv(0, 34)
                .cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F),
            ModelTransform.pivot(-4.0F, 20.0F, 4.0F));
        
        return TexturedModelData.of(modelData, 128, 64);
    }
    
    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Basic swimming animation
        this.head.yaw = netHeadYaw * 0.017453292F;
        this.head.pitch = headPitch * 0.017453292F;
        
        // Gentle leg movement for swimming
        float legSwing = limbSwing * 0.6662F;
        float legAmount = limbSwingAmount * 0.5F;
        
        this.frontLeftLeg.roll = (float) Math.cos(legSwing) * legAmount;
        this.frontRightLeg.roll = (float) Math.cos(legSwing + Math.PI) * legAmount;
        this.backLeftLeg.roll = (float) Math.cos(legSwing + Math.PI) * legAmount;
        this.backRightLeg.roll = (float) Math.cos(legSwing) * legAmount;
    }
    
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        root.render(matrices, vertexConsumer, light, overlay, color);
    }
}