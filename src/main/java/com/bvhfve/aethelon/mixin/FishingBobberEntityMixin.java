package com.bvhfve.aethelon.mixin;

import com.bvhfve.aethelon.compat.AquacultureCompat;
import com.bvhfve.aethelon.compat.ModCompatibility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Mixin to enhance fishing mechanics when near Aethelon turtles
 * Integrates with Aquaculture and other fishing mods
 */
@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {
    
    @Shadow
    private PlayerEntity getPlayerOwner() {
        return null;
    }
    
    /**
     * Enhance fishing loot when near Aethelon turtles
     */
    @Inject(method = "use", at = @At("RETURN"), cancellable = true)
    private void enhanceFishingLoot(ItemStack usedItem, CallbackInfoReturnable<Integer> cir) {
        PlayerEntity player = this.getPlayerOwner();
        if (player == null) return;
        
        try {
            // Check for Aquaculture compatibility
            if (ModCompatibility.isModLoaded(ModCompatibility.AQUACULTURE)) {
                // Check if fishing on turtle island for special rewards
                if (AquacultureCompat.isFishingOnTurtleIsland(player)) {
                    ItemStack specialReward = AquacultureCompat.getTurtleIslandFishingReward(player);
                    if (!specialReward.isEmpty()) {
                        // Give the special reward to the player
                        player.getInventory().insertStack(specialReward);
                    }
                }
                
                // Check for enhanced fishing near turtles
                else if (AquacultureCompat.isNearAethelonTurtle(player)) {
                    // This would be applied to the normal fishing loot
                    // The actual implementation would need to modify the loot generation
                }
            }
        } catch (Exception e) {
            // Silently handle any compatibility errors
        }
    }
}