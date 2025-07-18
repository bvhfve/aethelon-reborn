package com.bvhfve.aethelon.upgrade;

import com.bvhfve.aethelon.Aethelon;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Handles weapon upgrades using anvils
 * Players can right-click anvils with weapons and gems to upgrade them
 */
public class AnvilUpgradeHandler {
    
    /**
     * Registers the anvil upgrade handler
     */
    public static void register() {
        UseBlockCallback.EVENT.register(AnvilUpgradeHandler::onUseBlock);
        Aethelon.LOGGER.info("Registered anvil upgrade handler");
    }
    
    /**
     * Handles right-clicking on anvils
     */
    private static ActionResult onUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (world.isClient) return ActionResult.PASS;
        
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);
        
        // Check if it's an anvil
        if (!(state.getBlock() instanceof AnvilBlock)) {
            return ActionResult.PASS;
        }
        
        ItemStack heldItem = player.getStackInHand(hand);
        ItemStack offhandItem = player.getStackInHand(hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND);
        
        // Determine which is weapon and which is gem
        ItemStack weapon = null;
        ItemStack gems = null;
        
        if (WeaponUpgradeSystem.canUpgrade(heldItem) && WeaponUpgradeSystem.GemType.fromItem(offhandItem) != null) {
            weapon = heldItem;
            gems = offhandItem;
        } else if (WeaponUpgradeSystem.canUpgrade(offhandItem) && WeaponUpgradeSystem.GemType.fromItem(heldItem) != null) {
            weapon = offhandItem;
            gems = heldItem;
        }
        
        if (weapon == null || gems == null) {
            return ActionResult.PASS;
        }
        
        // Check if upgrade is possible
        if (!WeaponUpgradeSystem.canUpgradeWith(weapon, gems)) {
            // Send feedback about why upgrade failed
            int currentTier = WeaponUpgradeSystem.getUpgradeTier(weapon);
            
            if (currentTier >= WeaponUpgradeSystem.MAX_UPGRADE_TIER) {
                player.sendMessage(Text.literal("This weapon is already at maximum upgrade tier!")
                    .formatted(Formatting.RED), true);
            } else {
                WeaponUpgradeSystem.GemType currentType = WeaponUpgradeSystem.getUpgradeType(weapon);
                WeaponUpgradeSystem.GemType gemType = WeaponUpgradeSystem.GemType.fromItem(gems);
                
                if (currentType != null && currentType != gemType) {
                    player.sendMessage(Text.literal("Cannot mix different gem types! This weapon uses " + 
                        currentType.getDisplayName() + " gems.")
                        .formatted(Formatting.RED), true);
                } else {
                    int needed = WeaponUpgradeSystem.getGemsNeededForNextTier(weapon);
                    player.sendMessage(Text.literal("Need " + needed + " gems for next upgrade (have " + 
                        gems.getCount() + ")")
                        .formatted(Formatting.YELLOW), true);
                }
            }
            return ActionResult.SUCCESS;
        }
        
        // Perform the upgrade
        ItemStack upgradedWeapon = WeaponUpgradeSystem.upgradeWeapon(weapon, gems);
        
        // Replace the weapon in player's inventory
        if (weapon == heldItem) {
            player.setStackInHand(hand, upgradedWeapon);
        } else {
            player.setStackInHand(hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND, upgradedWeapon);
        }
        
        // Play upgrade sound and effects
        world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.5f, 1.5f);
        
        // Send success message
        int newTier = WeaponUpgradeSystem.getUpgradeTier(upgradedWeapon);
        WeaponUpgradeSystem.GemType gemType = WeaponUpgradeSystem.getUpgradeType(upgradedWeapon);
        
        player.sendMessage(Text.literal("Weapon upgraded to ")
            .append(Text.literal(gemType.getDisplayName() + " Tier " + newTier)
                .formatted(gemType.getColor(), Formatting.BOLD))
            .append(Text.literal("!"))
            .formatted(Formatting.GREEN), true);
        
        // Damage the anvil slightly (like normal anvil use)
        if (world.random.nextFloat() < 0.12f) { // 12% chance
            BlockState damagedState = AnvilBlock.getLandingState(state);
            if (damagedState == null) {
                world.removeBlock(pos, false);
                world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_DESTROY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                player.sendMessage(Text.literal("The anvil broke from the intense upgrade process!")
                    .formatted(Formatting.RED), false);
            } else {
                world.setBlockState(pos, damagedState);
                world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.5f, 1.0f);
            }
        }
        
        return ActionResult.SUCCESS;
    }
}