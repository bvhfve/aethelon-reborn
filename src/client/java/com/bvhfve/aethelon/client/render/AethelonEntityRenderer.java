package com.bvhfve.aethelon.client.render;

import com.bvhfve.aethelon.Aethelon;
import com.bvhfve.aethelon.client.model.AethelonEntityModel;
import com.bvhfve.aethelon.entity.AethelonEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

/**
 * Renderer for the Aethelon entity
 * Handles the visual representation of the world turtle
 */
public class AethelonEntityRenderer extends MobEntityRenderer<AethelonEntity, AethelonEntityModel<AethelonEntity>> {
    
    private static final Identifier TEXTURE = Identifier.of(Aethelon.MOD_ID, "textures/entity/aethelon.png");
    
    public AethelonEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new AethelonEntityModel<>(context.getPart(com.bvhfve.aethelon.client.model.ModEntityModelLayers.AETHELON)), 4.0f);
    }
    
    @Override
    public Identifier getTexture(AethelonEntity entity) {
        return TEXTURE;
    }
}