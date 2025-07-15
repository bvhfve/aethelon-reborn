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
 */
public class AethelonEntityRenderer extends MobEntityRenderer<AethelonEntity, LivingEntityRenderState, AethelonEntityModel> {
    
    private static final Identifier TEXTURE = Identifier.of(Aethelon.MOD_ID, "textures/entity/aethelon.png");
    
    public AethelonEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new AethelonEntityModel(context.getPart(com.bvhfve.aethelon.client.model.ModEntityModelLayers.AETHELON)), 4.0f);
    }
    
    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
    
    @Override
    public Identifier getTexture(LivingEntityRenderState renderState) {
        return TEXTURE;
    }
}