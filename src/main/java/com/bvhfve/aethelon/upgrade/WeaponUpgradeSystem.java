package com.bvhfve.aethelon.upgrade;

import com.bvhfve.aethelon.Aethelon;
import com.bvhfve.aethelon.items.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

/**
 * Weapon Upgrade System using existing crafting ingredients as gems
 * 
 * Features:
 * - 5 upgrade tiers with increasing costs (1, 2, 3, 4, 5 gems)
 * - Each tier increases damage and effect by 20%
 * - Different gem types provide different elemental effects
 * - Works with vanilla weapons and custom Ancient Trident
 */
public class WeaponUpgradeSystem {
    
    // Configuration constants for easy tuning
    public static final int MAX_UPGRADE_TIER = 5;
    public static final float DAMAGE_INCREASE_PER_TIER = 0.2f; // 20% per tier
    public static final int TOTAL_GEMS_NEEDED = 15; // 1+2+3+4+5
    
    // NBT keys for storing upgrade data
    private static final String UPGRADE_TIER_KEY = "aethelon_upgrade_tier";
    private static final String UPGRADE_TYPE_KEY = "aethelon_upgrade_type";
    private static final String UPGRADE_DAMAGE_KEY = "aethelon_upgrade_damage";
    
    /**
     * Gem types and their corresponding elemental effects
     */
    public enum GemType {
        TURTLE_SCALE("turtle_scale", "Aquatic", Formatting.AQUA, ModItems.ANCIENT_TURTLE_SCALE),
        TURTLE_HEART("turtle_heart", "Vitality", Formatting.RED, ModItems.TURTLE_HEART),
        CRYSTALLIZED_WATER("crystallized_water", "Frost", Formatting.BLUE, ModItems.CRYSTALLIZED_WATER),
        DEEP_SEA_PEARL("deep_sea_pearl", "Lightning", Formatting.YELLOW, ModItems.DEEP_SEA_PEARL),
        ISLAND_ESSENCE("island_essence", "Earth", Formatting.GREEN, ModItems.ISLAND_ESSENCE);
        
        private final String id;
        private final String displayName;
        private final Formatting color;
        private final Object gemItem;
        
        GemType(String id, String displayName, Formatting color, Object gemItem) {
            this.id = id;
            this.displayName = displayName;
            this.color = color;
            this.gemItem = gemItem;
        }
        
        public String getId() { return id; }
        public String getDisplayName() { return displayName; }
        public Formatting getColor() { return color; }
        public Object getGemItem() { return gemItem; }
        
        public static GemType fromItem(ItemStack stack) {
            if (stack.getItem() == ModItems.ANCIENT_TURTLE_SCALE) return TURTLE_SCALE;
            if (stack.getItem() == ModItems.TURTLE_HEART) return TURTLE_HEART;
            if (stack.getItem() == ModItems.CRYSTALLIZED_WATER) return CRYSTALLIZED_WATER;
            if (stack.getItem() == ModItems.DEEP_SEA_PEARL) return DEEP_SEA_PEARL;
            if (stack.getItem() == ModItems.ISLAND_ESSENCE) return ISLAND_ESSENCE;
            return null;
        }
    }
    
    /**
     * Checks if an item can be upgraded
     */
    public static boolean canUpgrade(ItemStack weapon) {
        // Allow swords, axes, tridents, and our custom trident
        return weapon.getItem() == Items.WOODEN_SWORD || weapon.getItem() == Items.STONE_SWORD ||
               weapon.getItem() == Items.IRON_SWORD || weapon.getItem() == Items.GOLDEN_SWORD ||
               weapon.getItem() == Items.DIAMOND_SWORD || weapon.getItem() == Items.NETHERITE_SWORD ||
               weapon.getItem() == Items.WOODEN_AXE || weapon.getItem() == Items.STONE_AXE ||
               weapon.getItem() == Items.IRON_AXE || weapon.getItem() == Items.GOLDEN_AXE ||
               weapon.getItem() == Items.DIAMOND_AXE || weapon.getItem() == Items.NETHERITE_AXE ||
               weapon.getItem() == Items.TRIDENT || weapon.getItem() == ModItems.ANCIENT_TRIDENT;
    }
    
    /**
     * Gets the current upgrade tier of a weapon
     */
    public static int getUpgradeTier(ItemStack weapon) {
        if (!weapon.contains(DataComponentTypes.CUSTOM_DATA)) {
            return 0;
        }
        
        NbtComponent customData = weapon.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null) return 0;
        
        return customData.copyNbt().getInt(UPGRADE_TIER_KEY);
    }
    
    /**
     * Gets the upgrade type of a weapon
     */
    public static GemType getUpgradeType(ItemStack weapon) {
        if (!weapon.contains(DataComponentTypes.CUSTOM_DATA)) {
            return null;
        }
        
        NbtComponent customData = weapon.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null) return null;
        
        String typeId = customData.copyNbt().getString(UPGRADE_TYPE_KEY);
        for (GemType type : GemType.values()) {
            if (type.getId().equals(typeId)) {
                return type;
            }
        }
        return null;
    }
    
    /**
     * Gets the number of gems needed for the next upgrade
     */
    public static int getGemsNeededForNextTier(ItemStack weapon) {
        int currentTier = getUpgradeTier(weapon);
        if (currentTier >= MAX_UPGRADE_TIER) {
            return 0; // Already maxed
        }
        return currentTier + 1; // Next tier requires (current + 1) gems
    }
    
    /**
     * Checks if a weapon can be upgraded with the given gems
     */
    public static boolean canUpgradeWith(ItemStack weapon, ItemStack gems) {
        if (!canUpgrade(weapon)) return false;
        
        int currentTier = getUpgradeTier(weapon);
        if (currentTier >= MAX_UPGRADE_TIER) return false;
        
        GemType gemType = GemType.fromItem(gems);
        if (gemType == null) return false;
        
        GemType currentType = getUpgradeType(weapon);
        if (currentType != null && currentType != gemType) {
            return false; // Can't mix gem types
        }
        
        int gemsNeeded = getGemsNeededForNextTier(weapon);
        return gems.getCount() >= gemsNeeded;
    }
    
    /**
     * Upgrades a weapon with the given gems
     */
    public static ItemStack upgradeWeapon(ItemStack weapon, ItemStack gems) {
        if (!canUpgradeWith(weapon, gems)) {
            return weapon; // Return unchanged if can't upgrade
        }
        
        GemType gemType = GemType.fromItem(gems);
        int currentTier = getUpgradeTier(weapon);
        int newTier = currentTier + 1;
        int gemsUsed = getGemsNeededForNextTier(weapon);
        
        // Create upgraded weapon
        ItemStack upgraded = weapon.copy();
        
        // Get or create custom data
        NbtCompound nbt = new NbtCompound();
        if (upgraded.contains(DataComponentTypes.CUSTOM_DATA)) {
            nbt = upgraded.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
        }
        
        // Set upgrade data
        nbt.putInt(UPGRADE_TIER_KEY, newTier);
        nbt.putString(UPGRADE_TYPE_KEY, gemType.getId());
        nbt.putFloat(UPGRADE_DAMAGE_KEY, newTier * DAMAGE_INCREASE_PER_TIER);
        
        // Apply custom data
        upgraded.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
        
        // Update weapon name
        String tierRoman = getRomanNumeral(newTier);
        Text newName = Text.literal(weapon.getName().getString() + " ")
            .append(Text.literal("[" + gemType.getDisplayName() + " " + tierRoman + "]")
                .formatted(gemType.getColor(), Formatting.BOLD));
        upgraded.set(DataComponentTypes.CUSTOM_NAME, newName);
        
        // Consume gems
        gems.decrement(gemsUsed);
        
        Aethelon.LOGGER.info("Upgraded {} to tier {} with {} gems", 
            weapon.getName().getString(), newTier, gemType.getDisplayName());
        
        return upgraded;
    }
    
    /**
     * Gets the total damage bonus for an upgraded weapon
     */
    public static float getDamageBonus(ItemStack weapon) {
        if (!weapon.contains(DataComponentTypes.CUSTOM_DATA)) {
            return 0.0f;
        }
        
        NbtComponent customData = weapon.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null) return 0.0f;
        
        return customData.copyNbt().getFloat(UPGRADE_DAMAGE_KEY);
    }
    
    /**
     * Converts number to Roman numeral
     */
    private static String getRomanNumeral(int number) {
        return switch (number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> String.valueOf(number);
        };
    }
    
    /**
     * Gets upgrade information for tooltips
     */
    public static String getUpgradeInfo(ItemStack weapon) {
        int tier = getUpgradeTier(weapon);
        if (tier == 0) return null;
        
        GemType type = getUpgradeType(weapon);
        if (type == null) return null;
        
        float damageBonus = getDamageBonus(weapon);
        return String.format("%s Enhancement %s (+%.0f%% damage)", 
            type.getDisplayName(), getRomanNumeral(tier), damageBonus * 100);
    }
}