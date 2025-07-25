package com.bvhfve.aethelon.mixin;

import com.bvhfve.aethelon.config.ConfigDebugLogger;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.StructureAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to inject debug logging during chunk generation
 * Logs configuration status when chunks are being generated
 */
@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {
    
    /**
     * Inject debug logging when chunks are being generated
     */
    @Inject(method = "generateFeatures", at = @At("HEAD"))
    private void onGenerateFeatures(net.minecraft.world.StructureWorldAccess world, Chunk chunk, net.minecraft.world.gen.StructureAccessor structureAccessor, CallbackInfo ci) {
        try {
            ChunkPos chunkPos = chunk.getPos();
            // Use StructureWorldAccess - it extends WorldAccess
            ConfigDebugLogger.logChunkGenerationConfig(world, chunkPos);
        } catch (Exception e) {
            // Silently ignore errors to avoid breaking chunk generation
        }
    }
}