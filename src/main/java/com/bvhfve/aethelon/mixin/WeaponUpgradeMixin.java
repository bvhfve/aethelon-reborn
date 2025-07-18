package com.bvhfve.aethelon.mixin;

import com.bvhfve.aethelon.upgrade.ElementalEffects;
import com.bvhfve.aethelon.upgrade.WeaponUpgradeSystem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Mixin to handle weapon upgrade effects and damage bonuses
 */
@Mixin(LivingEntity.class)
public class WeaponUpgradeMixin {
    
    /**
     * Apply elemental effects when attacking with upgraded weapons
     */
    @Inject(method = "onAttacking", at = @At("TAIL"))
    private void applyUpgradeEffects(Entity target, CallbackInfo ci) {
        LivingEntity attacker = (LivingEntity) (Object) this;
        
        if (attacker instanceof PlayerEntity player && target instanceof LivingEntity livingTarget) {
            ItemStack weapon = player.getMainHandStack();
            
            if (WeaponUpgradeSystem.getUpgradeTier(weapon) > 0) {
                ElementalEffects.applyElementalEffect(player.getWorld(), weapon, player, livingTarget);
            }
        }
    }
    
    /**
     * Modify damage based on weapon upgrades
     */
    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float modifyDamageForUpgrades(float amount) {
        LivingEntity entity = (LivingEntity) (Object) this;
        
        if (entity instanceof PlayerEntity player) {
            ItemStack weapon = player.getMainHandStack();
            float damageBonus = WeaponUpgradeSystem.getDamageBonus(weapon);
            
            if (damageBonus > 0) {
                // Get base attack damage
                double baseAttack = player.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
                float bonusDamage = (float) (baseAttack * damageBonus);
                return amount + bonusDamage;
            }
        }
        
        return amount;
    }
}

/**
 * Mixin to add upgrade information to item tooltips
 */
@Mixin(ItemStack.class)
class ItemStackUpgradeMixin {
    
    @Inject(method = "getTooltip", at = @At("RETURN"))
    private void addUpgradeTooltip(net.minecraft.item.Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        List<Text> tooltip = cir.getReturnValue();
        
        if (WeaponUpgradeSystem.canUpgrade(stack)) {
            int tier = WeaponUpgradeSystem.getUpgradeTier(stack);
            
            if (tier > 0) {
                WeaponUpgradeSystem.GemType upgradeType = WeaponUpgradeSystem.getUpgradeType(stack);
                if (upgradeType != null) {
                    tooltip.add(Text.literal(""));
                    tooltip.add(Text.literal("Gem Enhancement:").formatted(Formatting.GOLD, Formatting.BOLD));
                    
                    String tierRoman = switch (tier) {
                        case 1 -> "I";
                        case 2 -> "II";
                        case 3 -> "III";
                        case 4 -> "IV";
                        case 5 -> "V";
                        default -> String.valueOf(tier);
                    };
                    
                    tooltip.add(Text.literal("• " + upgradeType.getDisplayName() + " Tier " + tierRoman)
                        .formatted(upgradeType.getColor()));
                    
                    float damageBonus = WeaponUpgradeSystem.getDamageBonus(stack);
                    tooltip.add(Text.literal("• +" + (int)(damageBonus * 100) + "% Damage")
                        .formatted(Formatting.RED));
                    
                    String effectDesc = ElementalEffects.getEffectDescription(upgradeType, tier);
                    tooltip.add(Text.literal("• " + effectDesc)
                        .formatted(Formatting.GRAY));
                }
            } else {
                // Show upgrade hint for unupgraded weapons
                tooltip.add(Text.literal(""));
                tooltip.add(Text.literal("Can be upgraded with gems at an anvil")
                    .formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
            }
        }
    }
}