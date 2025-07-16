package com.bvhfve.aethelon.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

/**
 * Ancient Turtle Trident - Enhanced trident with special abilities
 */
public class AncientTridentItem extends TridentItem {
    
    public AncientTridentItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        
        // Enhanced underwater abilities
        if (user.isSubmergedInWater()) {
            // Provide temporary dolphin's grace when used underwater
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, 200, 1, false, false));
        }
        
        return super.use(world, user, hand);
    }
    
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
            if (i >= 10) {
                int riptideLevel = EnchantmentHelper.getRiptide(stack);
                if (riptideLevel <= 0 || playerEntity.isTouchingWaterOrRain()) {
                    if (!world.isClient) {
                        stack.damage(1, playerEntity, LivingEntity.getSlotForHand(user.getActiveHand()));
                        if (riptideLevel == 0) {
                            TridentEntity tridentEntity = new TridentEntity(world, playerEntity, stack);
                            tridentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.5F + (float)riptideLevel * 0.5F, 1.0F);
                            if (playerEntity.getAbilities().creativeMode) {
                                tridentEntity.pickupType = TridentEntity.PickupType.CREATIVE_ONLY;
                            }

                            world.spawnEntity(tridentEntity);
                            world.playSoundFromEntity(null, tridentEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            if (!playerEntity.getAbilities().creativeMode) {
                                playerEntity.getInventory().removeOne(stack);
                            }
                        }
                    }

                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                    if (riptideLevel > 0) {
                        float f = playerEntity.getYaw();
                        float g = playerEntity.getPitch();
                        float h = -net.minecraft.util.math.MathHelper.sin(f * 0.017453292F) * net.minecraft.util.math.MathHelper.cos(g * 0.017453292F);
                        float j = -net.minecraft.util.math.MathHelper.sin(g * 0.017453292F);
                        float k = net.minecraft.util.math.MathHelper.cos(f * 0.017453292F) * net.minecraft.util.math.MathHelper.cos(g * 0.017453292F);
                        float l = net.minecraft.util.math.MathHelper.sqrt(h * h + j * j + k * k);
                        float m = 3.0F * ((1.0F + (float)riptideLevel) / 4.0F);
                        h *= m / l;
                        j *= m / l;
                        k *= m / l;
                        playerEntity.addVelocity(h, j, k);
                        playerEntity.useRiptide(20);
                        if (playerEntity.isOnGround()) {
                            playerEntity.move(net.minecraft.entity.MovementType.SELF, new net.minecraft.util.math.Vec3d(0.0, 1.1999999284744263, 0.0));
                        }

                        SoundEvent soundEvent;
                        if (riptideLevel >= 3) {
                            soundEvent = SoundEvents.ITEM_TRIDENT_RIPTIDE_3;
                        } else if (riptideLevel == 2) {
                            soundEvent = SoundEvents.ITEM_TRIDENT_RIPTIDE_2;
                        } else {
                            soundEvent = SoundEvents.ITEM_TRIDENT_RIPTIDE_1;
                        }

                        world.playSoundFromEntity(null, playerEntity, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }
    
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }
    
    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }
}