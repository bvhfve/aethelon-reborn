package com.bvhfve.aethelon.items;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.item.consume.UseAction;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

/**
 * Ancient Turtle Trident - Enhanced trident with special abilities
 * 
 * Features:
 * - Enhanced underwater abilities
 * - Aquatic creature control
 * - Water manipulation powers
 * - Improved damage and range
 */
public class AncientTridentItem extends TridentItem {
    
    // Configuration constants for easy tuning
    private static final int DOLPHINS_GRACE_DURATION = 400; // 20 seconds
    private static final int WATER_BREATHING_DURATION = 600; // 30 seconds
    private static final int AQUATIC_CONTROL_RANGE = 16;
    private static final int AQUATIC_CONTROL_DURATION = 200; // 10 seconds
    private static final float ENHANCED_DAMAGE_MULTIPLIER = 1.5f;
    private static final int COOLDOWN_TICKS = 100; // 5 seconds
    
    public AncientTridentItem(Item.Settings settings) {
        super(settings);
    }
    
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        
        // Check cooldown
        if (user.getItemCooldownManager().isCoolingDown(itemStack)) {
            return ActionResult.FAIL;
        }
        
        // Enhanced underwater abilities
        if (user.isSubmergedInWater()) {
            activateUnderwaterPowers(world, user, itemStack);
        } else if (user.isTouchingWaterOrRain()) {
            activateAquaticControl(world, user, itemStack);
        }
        
        return super.use(world, user, hand);
    }
    
    /**
     * Activates enhanced underwater abilities
     */
    private void activateUnderwaterPowers(World world, PlayerEntity user, ItemStack stack) {
        // Enhanced underwater effects
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.DOLPHINS_GRACE, DOLPHINS_GRACE_DURATION, 1, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, WATER_BREATHING_DURATION, 0, false, false));
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, WATER_BREATHING_DURATION, 0, false, false));
        
        // Visual effects
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            Vec3d pos = user.getPos();
            serverWorld.spawnParticles(ParticleTypes.BUBBLE_COLUMN_UP, 
                pos.x, pos.y, pos.z, 20, 1.0, 1.0, 1.0, 0.1);
        }
        
        // Set enhanced name
        if (!stack.contains(DataComponentTypes.CUSTOM_NAME)) {
            stack.set(DataComponentTypes.CUSTOM_NAME, 
                Text.literal("Ancient Trident of the Depths").formatted(Formatting.AQUA, Formatting.BOLD));
        }
        
        // Play sound
        world.playSound(null, user.getX(), user.getY(), user.getZ(), 
            SoundEvents.ENTITY_DOLPHIN_AMBIENT, SoundCategory.PLAYERS, 1.0f, 1.2f);
        
        // Set cooldown
        user.getItemCooldownManager().set(stack, COOLDOWN_TICKS);
    }
    
    /**
     * Controls nearby aquatic creatures
     */
    private void activateAquaticControl(World world, PlayerEntity user, ItemStack stack) {
        if (world.isClient) return;
        
        Box searchBox = Box.of(user.getPos(), AQUATIC_CONTROL_RANGE * 2, AQUATIC_CONTROL_RANGE * 2, AQUATIC_CONTROL_RANGE * 2);
        List<Entity> entities = world.getOtherEntities(user, searchBox);
        
        int controlledCount = 0;
        for (Entity entity : entities) {
            if (entity instanceof MobEntity mob && isAquaticCreature(mob)) {
                // Make aquatic creatures friendly temporarily
                mob.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, AQUATIC_CONTROL_DURATION, 0, false, true));
                
                // Simple AI: make them follow the player briefly
                if (mob.getTarget() == user) {
                    mob.setTarget(null);
                }
                
                controlledCount++;
                
                // Visual effect on controlled creatures
                if (world instanceof ServerWorld serverWorld) {
                    Vec3d pos = mob.getPos();
                    serverWorld.spawnParticles(ParticleTypes.HEART, 
                        pos.x, pos.y + mob.getHeight() + 0.5, pos.z, 3, 0.3, 0.3, 0.3, 0.1);
                }
            }
        }
        
        if (controlledCount > 0) {
            user.sendMessage(Text.literal("Controlled " + controlledCount + " aquatic creatures").formatted(Formatting.BLUE), true);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), 
                SoundEvents.BLOCK_CONDUIT_ACTIVATE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            
            // Set cooldown
            user.getItemCooldownManager().set(stack, COOLDOWN_TICKS);
        }
    }
    
    /**
     * Checks if an entity is an aquatic creature
     */
    private boolean isAquaticCreature(MobEntity mob) {
        return mob.canBreatheInWater() || 
               mob.getType().toString().contains("fish") ||
               mob.getType().toString().contains("squid") ||
               mob.getType().toString().contains("dolphin") ||
               mob.getType().toString().contains("turtle") ||
               mob.getType().toString().contains("guardian");
    }
    
    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
            if (i >= 10) {
                // Fixed for 1.21.4 - EnchantmentHelper methods updated
                int riptideLevel = Math.round(EnchantmentHelper.getTridentSpinAttackStrength(stack, playerEntity));
                if (riptideLevel <= 0 || playerEntity.isTouchingWaterOrRain()) {
                    if (!world.isClient) {
                        stack.damage(1, playerEntity, LivingEntity.getSlotForHand(user.getActiveHand()));
                        if (riptideLevel == 0) {
                            TridentEntity tridentEntity = new TridentEntity(world, playerEntity, stack);
                            // Enhanced velocity for Ancient Trident
                            float enhancedVelocity = (2.5F + (float)riptideLevel * 0.5F) * ENHANCED_DAMAGE_MULTIPLIER;
                            tridentEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, enhancedVelocity, 1.0F);
                            if (playerEntity.getAbilities().creativeMode) {
                                // Fixed for 1.21.4 - Direct field assignment with PersistentProjectileEntity.PickupPermission
                                tridentEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                            }

                            world.spawnEntity(tridentEntity);
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
                        // Fixed for 1.21.4 - Updated useRiptide method
                        playerEntity.useRiptide(20, 8.0F, stack);
                        if (playerEntity.isOnGround()) {
                            playerEntity.move(net.minecraft.entity.MovementType.SELF, new net.minecraft.util.math.Vec3d(0.0, 1.1999999284744263, 0.0));
                        }

                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }
    
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }
    
    /**
     * Adds tooltip information about the trident's abilities
     */
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("Ancient Powers:").formatted(Formatting.GOLD, Formatting.BOLD));
        tooltip.add(Text.literal("- Enhanced underwater abilities").formatted(Formatting.AQUA));
        tooltip.add(Text.literal("- Controls aquatic creatures").formatted(Formatting.BLUE));
        tooltip.add(Text.literal("- Increased damage and range").formatted(Formatting.RED));
        tooltip.add(Text.literal("- Perfect accuracy when thrown").formatted(Formatting.YELLOW));
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("Right-click underwater: Aquatic boost").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Right-click in rain: Control sea life").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Thrown tridents fly exactly where aimed").formatted(Formatting.GRAY));
        
        if (stack.contains(DataComponentTypes.CUSTOM_NAME)) {
            tooltip.add(Text.literal(""));
            tooltip.add(Text.literal("Awakened Form").formatted(Formatting.DARK_AQUA, Formatting.ITALIC));
        }
    }
    
    /**
     * Enhanced damage calculation for aquatic environments
     */
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Enhanced damage in water
        if (attacker instanceof PlayerEntity player && (player.isSubmergedInWater() || player.isTouchingWaterOrRain())) {
            // Apply additional effects to aquatic creatures
            if (isAquaticCreature(target) && target instanceof MobEntity mob) {
                // Chance to pacify instead of damage
                if (player.getWorld().random.nextFloat() < 0.3f) {
                    mob.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 2));
                    mob.setTarget(null);
                    
                    player.sendMessage(Text.literal("The creature is calmed by your ancient power").formatted(Formatting.BLUE), true);
                    return true;
                }
            }
        }
        
        return super.postHit(stack, target, attacker);
    }
    
    /**
     * Check if target is aquatic creature (overloaded for LivingEntity)
     */
    private boolean isAquaticCreature(LivingEntity entity) {
        if (entity instanceof MobEntity mob) {
            return isAquaticCreature(mob);
        }
        return entity.canBreatheInWater();
    }
}