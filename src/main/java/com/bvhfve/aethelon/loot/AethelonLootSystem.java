package com.bvhfve.aethelon.loot;

import com.bvhfve.aethelon.config.AethelonConfig;
import com.bvhfve.aethelon.entity.AethelonEntity;
import com.bvhfve.aethelon.island.IslandManager;
import com.bvhfve.aethelon.items.ModItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Comprehensive loot system for Aethelon entities
 * 
 * Provides meaningful rewards for defeating ancient world turtles:
 * - Rare materials and resources
 * - Unique turtle-themed items
 * - Experience orbs
 * - Special artifacts based on island type
 * - Bonus loot for enraged turtles
 */
public class AethelonLootSystem {
    
    private static final Logger LOGGER = LoggerFactory.getLogger("AethelonLootSystem");
    
    /**
     * Loot rarity tiers
     */
    public enum LootRarity {
        COMMON(0.6f, "Common"),
        UNCOMMON(0.25f, "Uncommon"), 
        RARE(0.12f, "Rare"),
        LEGENDARY(0.03f, "Legendary");
        
        public final float baseChance;
        public final String displayName;
        
        LootRarity(float baseChance, String displayName) {
            this.baseChance = baseChance;
            this.displayName = displayName;
        }
    }
    
    /**
     * Loot categories for different types of rewards
     */
    public enum LootCategory {
        MATERIALS,      // Basic crafting materials
        TURTLE_PARTS,   // Turtle-specific items
        ARTIFACTS,      // Special magical items
        RESOURCES,      // Valuable resources
        EXPERIENCE      // Experience orbs
    }
    
    /**
     * Generate and drop loot when an Aethelon is defeated
     */
    public static void generateDeathLoot(AethelonEntity turtle, PlayerEntity killer, ServerWorld world) {
        if (!AethelonConfig.INSTANCE.enable_loot_drops) {
            return;
        }
        
        LOGGER.info("Generating loot for defeated Aethelon at {}", turtle.getBlockPos());
        
        Vec3d dropPosition = turtle.getPos();
        Random random = world.getRandom();
        
        // Calculate loot modifiers
        LootModifiers modifiers = calculateLootModifiers(turtle, killer);
        
        // Generate different categories of loot
        List<ItemStack> loot = new ArrayList<>();
        
        // 1. Guaranteed turtle parts (always drop)
        loot.addAll(generateTurtleParts(turtle, modifiers, random));
        
        // 2. Materials and resources
        loot.addAll(generateMaterials(turtle, modifiers, random));
        
        // 3. Rare artifacts (chance-based)
        loot.addAll(generateArtifacts(turtle, modifiers, random));
        
        // 4. Island-specific loot
        loot.addAll(generateIslandLoot(turtle, modifiers, random));
        
        // 5. Bonus loot for enraged turtles
        if (turtle.isEnraged()) {
            loot.addAll(generateEnrageLoot(turtle, modifiers, random));
        }
        
        // Drop all generated loot
        dropLoot(world, dropPosition, loot, random);
        
        // Generate experience orbs
        generateExperience(world, dropPosition, modifiers, random);
        
        // Send loot notification to killer
        if (killer != null) {
            sendLootNotification(killer, loot, modifiers);
        }
        
        LOGGER.info("Generated {} loot items for Aethelon defeat", loot.size());
    }
    
    /**
     * Calculate loot modifiers based on turtle state and killer
     */
    private static LootModifiers calculateLootModifiers(AethelonEntity turtle, PlayerEntity killer) {
        LootModifiers modifiers = new LootModifiers();
        
        // Base modifiers
        modifiers.quantityMultiplier = 1.0f;
        modifiers.rarityBonus = 0.0f;
        modifiers.experienceMultiplier = 1.0f;
        
        // Island type bonus
        if (turtle.hasIsland()) {
            IslandManager.IslandType islandType = turtle.getIslandManager().getCurrentIslandType();
            switch (islandType) {
                case SMALL -> {
                    modifiers.quantityMultiplier += 0.2f;
                    modifiers.experienceMultiplier += 0.3f;
                }
                case MEDIUM -> {
                    modifiers.quantityMultiplier += 0.5f;
                    modifiers.rarityBonus += 0.1f;
                    modifiers.experienceMultiplier += 0.6f;
                }
                case LARGE -> {
                    modifiers.quantityMultiplier += 1.0f;
                    modifiers.rarityBonus += 0.2f;
                    modifiers.experienceMultiplier += 1.0f;
                }
            }
        }
        
        // Enrage bonus
        if (turtle.isEnraged()) {
            modifiers.quantityMultiplier += 0.5f;
            modifiers.rarityBonus += 0.15f;
            modifiers.experienceMultiplier += 0.5f;
            modifiers.hasEnrageBonus = true;
        }
        
        // Agitation level bonus
        int agitation = turtle.getAgitationLevel();
        if (agitation > 50) {
            float agitationBonus = (agitation - 50) / 100.0f; // 0.0 to 0.5
            modifiers.quantityMultiplier += agitationBonus * 0.3f;
            modifiers.rarityBonus += agitationBonus * 0.1f;
        }
        
        // Killer-based bonuses (future expansion)
        if (killer != null) {
            // Could add enchantment bonuses, achievement bonuses, etc.
        }
        
        return modifiers;
    }
    
    /**
     * Generate guaranteed turtle parts
     */
    private static List<ItemStack> generateTurtleParts(AethelonEntity turtle, LootModifiers modifiers, Random random) {
        List<ItemStack> loot = new ArrayList<>();
        
        // Turtle Shell Fragments (guaranteed)
        int shellFragments = 3 + random.nextInt(3); // 3-5 base
        shellFragments = Math.round(shellFragments * modifiers.quantityMultiplier);
        loot.add(createTurtleShellFragment(shellFragments));
        
        // Ancient Turtle Scale (high chance)
        if (random.nextFloat() < 0.8f + modifiers.rarityBonus) {
            int scales = 1 + random.nextInt(2); // 1-2 base
            scales = Math.round(scales * modifiers.quantityMultiplier);
            loot.add(createAncientTurtleScale(scales));
        }
        
        // Turtle Heart (rare, only from large turtles or enraged)
        boolean canDropHeart = turtle.hasIsland() && 
                              (turtle.getIslandManager().getCurrentIslandType() == IslandManager.IslandType.LARGE || 
                               turtle.isEnraged());
        if (canDropHeart && random.nextFloat() < 0.3f + modifiers.rarityBonus) {
            loot.add(createTurtleHeart());
        }
        
        return loot;
    }
    
    /**
     * Generate materials and resources
     */
    private static List<ItemStack> generateMaterials(AethelonEntity turtle, LootModifiers modifiers, Random random) {
        List<ItemStack> loot = new ArrayList<>();
        
        // Prismarine (ocean theme)
        if (random.nextFloat() < 0.7f) {
            int count = 8 + random.nextInt(16); // 8-23 base
            count = Math.round(count * modifiers.quantityMultiplier);
            loot.add(new ItemStack(Items.PRISMARINE, count));
        }
        
        // Sea Lanterns
        if (random.nextFloat() < 0.5f) {
            int count = 2 + random.nextInt(4); // 2-5 base
            count = Math.round(count * modifiers.quantityMultiplier);
            loot.add(new ItemStack(Items.SEA_LANTERN, count));
        }
        
        // Kelp (lots of it)
        if (random.nextFloat() < 0.8f) {
            int count = 16 + random.nextInt(32); // 16-47 base
            count = Math.round(count * modifiers.quantityMultiplier);
            loot.add(new ItemStack(Items.KELP, count));
        }
        
        // Rare materials (chance-based)
        if (random.nextFloat() < 0.4f + modifiers.rarityBonus) {
            // Diamond
            int diamonds = 1 + random.nextInt(3); // 1-3 base
            diamonds = Math.round(diamonds * modifiers.quantityMultiplier);
            loot.add(new ItemStack(Items.DIAMOND, diamonds));
        }
        
        if (random.nextFloat() < 0.6f + modifiers.rarityBonus) {
            // Emerald
            int emeralds = 2 + random.nextInt(4); // 2-5 base
            emeralds = Math.round(emeralds * modifiers.quantityMultiplier);
            loot.add(new ItemStack(Items.EMERALD, emeralds));
        }
        
        // Custom materials
        if (AethelonConfig.INSTANCE.enable_custom_turtle_items) {
            if (random.nextFloat() < 0.3f + modifiers.rarityBonus) {
                // Crystallized Water
                int crystals = 1 + random.nextInt(3); // 1-3 base
                crystals = Math.round(crystals * modifiers.quantityMultiplier);
                loot.add(new ItemStack(ModItems.CRYSTALLIZED_WATER, crystals));
            }
            
            if (random.nextFloat() < 0.2f + modifiers.rarityBonus) {
                // Deep Sea Pearl
                int pearls = 1 + random.nextInt(2); // 1-2 base
                pearls = Math.round(pearls * modifiers.quantityMultiplier);
                loot.add(new ItemStack(ModItems.DEEP_SEA_PEARL, pearls));
            }
        }
        
        return loot;
    }
    
    /**
     * Generate rare artifacts
     */
    private static List<ItemStack> generateArtifacts(AethelonEntity turtle, LootModifiers modifiers, Random random) {
        List<ItemStack> loot = new ArrayList<>();
        
        // Trident (very rare)
        if (random.nextFloat() < 0.1f + modifiers.rarityBonus) {
            ItemStack trident = new ItemStack(Items.TRIDENT);
            // Add custom name and lore
            NbtCompound nbt = trident.getOrCreateNbt();
            nbt.putString("display", "{\"Name\":\"{\\\"text\\\":\\\"Ancient Turtle's Trident\\\",\\\"color\\\":\\\"aqua\\\",\\\"italic\\\":false}\"}");
            loot.add(trident);
        }
        
        // Nautilus Shell (rare)
        if (random.nextFloat() < 0.25f + modifiers.rarityBonus) {
            int shells = 1 + random.nextInt(2); // 1-2 base
            shells = Math.round(shells * modifiers.quantityMultiplier);
            loot.add(new ItemStack(Items.NAUTILUS_SHELL, shells));
        }
        
        // Heart of the Sea (legendary)
        if (random.nextFloat() < 0.05f + modifiers.rarityBonus) {
            loot.add(new ItemStack(Items.HEART_OF_THE_SEA));
        }
        
        // Ancient Compass (very rare)
        if (AethelonConfig.INSTANCE.enable_custom_turtle_items && random.nextFloat() < 0.08f + modifiers.rarityBonus) {
            loot.add(new ItemStack(ModItems.ANCIENT_COMPASS));
        }
        
        // Island Essence (rare, only if turtle had island)
        if (AethelonConfig.INSTANCE.enable_custom_turtle_items && turtle.hasIsland() && 
            random.nextFloat() < 0.15f + modifiers.rarityBonus) {
            int essence = 1 + random.nextInt(3); // 1-3 base
            essence = Math.round(essence * modifiers.quantityMultiplier);
            loot.add(new ItemStack(ModItems.ISLAND_ESSENCE, essence));
        }
        
        return loot;
    }
    
    /**
     * Generate island-specific loot
     */
    private static List<ItemStack> generateIslandLoot(AethelonEntity turtle, LootModifiers modifiers, Random random) {
        List<ItemStack> loot = new ArrayList<>();
        
        if (!turtle.hasIsland()) {
            return loot;
        }
        
        // Wood and plant materials from the island
        if (random.nextFloat() < 0.9f) {
            int wood = 16 + random.nextInt(32); // 16-47 base
            wood = Math.round(wood * modifiers.quantityMultiplier);
            loot.add(new ItemStack(Items.OAK_LOG, wood));
        }
        
        if (random.nextFloat() < 0.7f) {
            int saplings = 2 + random.nextInt(4); // 2-5 base
            saplings = Math.round(saplings * modifiers.quantityMultiplier);
            loot.add(new ItemStack(Items.OAK_SAPLING, saplings));
        }
        
        // Dirt and grass from island
        if (random.nextFloat() < 0.8f) {
            int dirt = 32 + random.nextInt(32); // 32-63 base
            dirt = Math.round(dirt * modifiers.quantityMultiplier);
            loot.add(new ItemStack(Items.DIRT, dirt));
        }
        
        // Flowers and decorative items
        if (random.nextFloat() < 0.6f) {
            int flowers = 4 + random.nextInt(8); // 4-11 base
            flowers = Math.round(flowers * modifiers.quantityMultiplier);
            loot.add(new ItemStack(Items.DANDELION, flowers));
        }
        
        return loot;
    }
    
    /**
     * Generate bonus loot for enraged turtles
     */
    private static List<ItemStack> generateEnrageLoot(AethelonEntity turtle, LootModifiers modifiers, Random random) {
        List<ItemStack> loot = new ArrayList<>();
        
        // Extra rare materials
        if (random.nextFloat() < 0.8f) {
            int gold = 4 + random.nextInt(8); // 4-11 base
            gold = Math.round(gold * modifiers.quantityMultiplier);
            loot.add(new ItemStack(Items.GOLD_INGOT, gold));
        }
        
        // Enchanted books (rare)
        if (random.nextFloat() < 0.3f) {
            // Create a random enchanted book
            ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
            // Note: In a full implementation, you'd add random enchantments here
            loot.add(book);
        }
        
        // Extra turtle parts
        if (random.nextFloat() < 0.6f) {
            loot.add(createAncientTurtleScale(2));
        }
        
        return loot;
    }
    
    /**
     * Create custom turtle shell fragment item
     */
    private static ItemStack createTurtleShellFragment(int count) {
        if (AethelonConfig.INSTANCE.enable_custom_turtle_items) {
            return new ItemStack(ModItems.TURTLE_SHELL_FRAGMENT, count);
        } else {
            // Fallback to vanilla item with custom name
            ItemStack item = new ItemStack(Items.TURTLE_SCUTE, count);
            NbtCompound nbt = item.getOrCreateNbt();
            nbt.putString("display", "{\"Name\":\"{\\\"text\\\":\\\"Ancient Turtle Shell Fragment\\\",\\\"color\\\":\\\"green\\\",\\\"italic\\\":false}\"}");
            return item;
        }
    }
    
    /**
     * Create custom ancient turtle scale item
     */
    private static ItemStack createAncientTurtleScale(int count) {
        if (AethelonConfig.INSTANCE.enable_custom_turtle_items) {
            return new ItemStack(ModItems.ANCIENT_TURTLE_SCALE, count);
        } else {
            // Fallback to vanilla item with custom name
            ItemStack item = new ItemStack(Items.PRISMARINE_SHARD, count);
            NbtCompound nbt = item.getOrCreateNbt();
            nbt.putString("display", "{\"Name\":\"{\\\"text\\\":\\\"Ancient Turtle Scale\\\",\\\"color\\\":\\\"blue\\\",\\\"italic\\\":false}\"}");
            return item;
        }
    }
    
    /**
     * Create legendary turtle heart item
     */
    private static ItemStack createTurtleHeart() {
        if (AethelonConfig.INSTANCE.enable_custom_turtle_items) {
            return new ItemStack(ModItems.TURTLE_HEART);
        } else {
            // Fallback to vanilla item with custom name
            ItemStack item = new ItemStack(Items.HEART_OF_THE_SEA);
            NbtCompound nbt = item.getOrCreateNbt();
            nbt.putString("display", "{\"Name\":\"{\\\"text\\\":\\\"Heart of the Ancient Turtle\\\",\\\"color\\\":\\\"gold\\\",\\\"italic\\\":false}\"}");
            return item;
        }
    }
    
    /**
     * Drop loot items in the world
     */
    private static void dropLoot(ServerWorld world, Vec3d position, List<ItemStack> loot, Random random) {
        for (ItemStack item : loot) {
            if (!item.isEmpty()) {
                // Spread items around the drop position
                double offsetX = (random.nextDouble() - 0.5) * 10; // ±5 blocks
                double offsetZ = (random.nextDouble() - 0.5) * 10; // ±5 blocks
                double offsetY = random.nextDouble() * 3; // 0-3 blocks up
                
                Vec3d dropPos = position.add(offsetX, offsetY, offsetZ);
                
                ItemEntity itemEntity = new ItemEntity(world, dropPos.x, dropPos.y, dropPos.z, item);
                
                // Add some random velocity for dramatic effect
                double vx = (random.nextDouble() - 0.5) * 0.4;
                double vy = random.nextDouble() * 0.3 + 0.1;
                double vz = (random.nextDouble() - 0.5) * 0.4;
                itemEntity.setVelocity(vx, vy, vz);
                
                // Make items glow briefly
                itemEntity.setGlowing(true);
                
                world.spawnEntity(itemEntity);
            }
        }
    }
    
    /**
     * Generate experience orbs
     */
    private static void generateExperience(ServerWorld world, Vec3d position, LootModifiers modifiers, Random random) {
        int baseExperience = AethelonConfig.INSTANCE.base_experience_reward;
        int totalExperience = Math.round(baseExperience * modifiers.experienceMultiplier);
        
        // Split experience into multiple orbs for better visual effect
        int orbCount = 5 + random.nextInt(5); // 5-9 orbs
        int expPerOrb = totalExperience / orbCount;
        int remainingExp = totalExperience % orbCount;
        
        for (int i = 0; i < orbCount; i++) {
            int expValue = expPerOrb;
            if (i == 0) expValue += remainingExp; // Add remainder to first orb
            
            if (expValue > 0) {
                // Spread orbs around the position
                double offsetX = (random.nextDouble() - 0.5) * 8;
                double offsetZ = (random.nextDouble() - 0.5) * 8;
                double offsetY = random.nextDouble() * 2;
                
                Vec3d orbPos = position.add(offsetX, offsetY, offsetZ);
                
                // Create experience orb
                net.minecraft.entity.ExperienceOrbEntity orb = 
                    new net.minecraft.entity.ExperienceOrbEntity(world, orbPos.x, orbPos.y, orbPos.z, expValue);
                
                world.spawnEntity(orb);
            }
        }
        
        LOGGER.info("Generated {} experience points in {} orbs", totalExperience, orbCount);
    }
    
    /**
     * Send loot notification to the killer
     */
    private static void sendLootNotification(PlayerEntity killer, List<ItemStack> loot, LootModifiers modifiers) {
        if (!AethelonConfig.INSTANCE.show_loot_notifications) {
            return;
        }
        
        // Count items by rarity/type
        int commonItems = 0;
        int rareItems = 0;
        int legendaryItems = 0;
        
        for (ItemStack item : loot) {
            if (item.hasNbt() && item.getNbt().contains("display")) {
                String displayName = item.getNbt().getString("display");
                if (displayName.contains("Heart of the Ancient Turtle") || displayName.contains("Ancient Turtle's Trident")) {
                    legendaryItems++;
                } else if (displayName.contains("Ancient")) {
                    rareItems++;
                } else {
                    commonItems++;
                }
            } else {
                commonItems++;
            }
        }
        
        // Send summary message
        String message = String.format("Ancient Turtle defeated! Loot: %d items", loot.size());
        if (modifiers.hasEnrageBonus) {
            message += " (Enrage Bonus!)";
        }
        
        killer.sendMessage(Text.literal(message), false);
        
        // Send detailed breakdown if there are rare items
        if (rareItems > 0 || legendaryItems > 0) {
            String details = String.format("Rare loot obtained: %d rare, %d legendary items!", rareItems, legendaryItems);
            killer.sendMessage(Text.literal(details), false);
        }
    }
    
    /**
     * Loot modifier data class
     */
    private static class LootModifiers {
        float quantityMultiplier = 1.0f;
        float rarityBonus = 0.0f;
        float experienceMultiplier = 1.0f;
        boolean hasEnrageBonus = false;
    }
}