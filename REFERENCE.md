# Entity and Spawn Egg Registration Reference

This document provides comprehensive examples and patterns for registering custom entities and spawn eggs in Minecraft Fabric mods, based on analysis of the knowledge pool and the Aethelon project.

## Table of Contents

1. [Entity Registration Patterns](#entity-registration-patterns)
2. [Spawn Egg Registration](#spawn-egg-registration)
3. [Complete Implementation Examples](#complete-implementation-examples)
4. [Best Practices](#best-practices)
5. [Common Patterns from Knowledge Pool](#common-patterns-from-knowledge-pool)

## Entity Registration Patterns

### Basic Entity Type Registration

#### Pattern 1: Simple Fabric Registration (Snuffles Example)
```java
// From: knowledge_pool/snuffles-fabric-1.21.4/src/main/java/io/github/suel_ki/snuffles/core/registry/SnufflesEntityTypes.java

public class SnufflesEntityTypes {
    public static final RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Snuffles.id("snuffle"));

    public static final EntityType<Snuffle> SNUFFLE = FabricEntityType.Builder.createMob(Snuffle::new, SpawnGroup.CREATURE,
                    (mob) -> mob.defaultAttributes(Snuffle::createSnuffleAttributes)
                               .spawnRestriction(SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, Snuffle::checkSnuffleSpawnRules))
            .dimensions(1.2F, 1.2F)
            .maxTrackingRange(8)
            .build(key);

    public static void init() {
        Registry.register(Registries.ENTITY_TYPE, key, SNUFFLE);
    }
}
```

#### Pattern 2: Architectury Multi-Platform (UntitledDuckMod Example)
```java
// From: knowledge_pool/UntitledDuckMod/.../common/src/main/java/net/untitledduckmod/common/init/ModEntityTypes.java

public class ModEntityTypes {
    public final static Supplier<EntityType<DuckEntity>> DUCK;
    public final static Supplier<EntityType<WaterfowlEggEntity>> DUCK_EGG;

    public final static RegistryKey<EntityType<?>> duckKey = RegistryKey.of(RegistryKeys.ENTITY_TYPE, DuckMod.id("duck"));
    public final static RegistryKey<EntityType<?>> duckEggKey = RegistryKey.of(RegistryKeys.ENTITY_TYPE, DuckMod.id("duck_egg"));

    static {
        DUCK = RegistryHelper.registerEntity("duck", () -> 
            EntityType.Builder.create(DuckEntity::new, SpawnGroup.CREATURE)
                .dimensions(0.6f, 0.6f)
                .maxTrackingRange(10)
                .build(duckKey));
        
        DUCK_EGG = RegistryHelper.registerEntity("duck_egg", () -> 
            EntityType.Builder.<WaterfowlEggEntity>create(WaterfowlEggEntity::new, SpawnGroup.MISC)
                .dimensions(0.25F, 0.25F)
                .maxTrackingRange(4)
                .trackingTickInterval(10)
                .build(duckEggKey));
    }

    public static void init() {
        // Static initialization handles registration
    }
}
```

#### Pattern 3: Current Aethelon Implementation
```java
// From: aethelon/src/main/java/com/bvhfve/aethelon/registry/ModEntityTypes.java

public class ModEntityTypes {
    public static EntityType<AethelonEntity> AETHELON;
    
    public static void registerEntityTypes() {
        Aethelon.LOGGER.info("Registering Aethelon entity types");
        
        RegistryKey<EntityType<?>> aethelonKey = RegistryKey.of(
                RegistryKeys.ENTITY_TYPE, 
                Identifier.of(Aethelon.MOD_ID, "aethelon")
        );
        
        AETHELON = Registry.register(
                Registries.ENTITY_TYPE,
                aethelonKey,
                FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, AethelonEntity::new)
                        .dimensions(EntityDimensions.fixed(8.0f, 4.0f))
                        .build(aethelonKey)
        );
        
        // Register entity attributes
        FabricDefaultAttributeRegistry.register(AETHELON, AethelonEntity.createAethelonAttributes());
        
        Aethelon.LOGGER.info("Entity types registered successfully");
    }
}
```

## Spawn Egg Registration

### Pattern 1: Direct SpawnEggItem Registration (Snuffles)
```java
// From: knowledge_pool/snuffles-fabric-1.21.4/src/main/java/io/github/suel_ki/snuffles/core/registry/SnufflesItems.java

public class SnufflesItems {
    public static final Item SNUFFLE_SPAWN_EGG = register("snuffle_spawn_egg", 
        settings -> new SpawnEggItem(SnufflesEntityTypes.SNUFFLE, settings), 
        new Item.Settings());

    private static <T extends Item> T register(String name, Function<Item.Settings, T> factory, Item.Settings settings) {
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Snuffles.id(name));
        T item = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.ITEM, key, item);
    }

    public static void addItemTooItemGroup() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> 
            entries.add(SNUFFLE_SPAWN_EGG));
    }
}
```

### Pattern 2: Platform Helper Registration (UntitledDuckMod)
```java
// From: knowledge_pool/UntitledDuckMod/.../common/src/main/java/net/untitledduckmod/common/init/ModItems.java

public class ModItems {
    public final static Supplier<SpawnEggItem> DUCK_SPAWN_EGG;
    public final static Supplier<SpawnEggItem> GOOSE_SPAWN_EGG;

    static {
        // Spawn Egg registration using helper
        DUCK_SPAWN_EGG = RegistryHelper.registerSpawnEggItem("duck_spawn_egg", ModEntityTypes.DUCK);
        GOOSE_SPAWN_EGG = RegistryHelper.registerSpawnEggItem("goose_spawn_egg", ModEntityTypes.GOOSE);
    }
}
```

### Pattern 3: Current Aethelon Implementation
```java
// From: aethelon/src/main/java/com/bvhfve/aethelon/registry/ModItems.java

public class ModItems {
    public static Item AETHELON_SPAWN_EGG;
    
    public static void registerItems() {
        Aethelon.LOGGER.info("Registering Aethelon items");
        
        // Verify entity type is available
        if (ModEntityTypes.AETHELON == null) {
            throw new IllegalStateException("Entity type AETHELON is null! Make sure registerEntityTypes() is called first.");
        }
        
        // Register spawn egg after entity type is available
        Identifier spawnEggId = Identifier.of(Aethelon.MOD_ID, "aethelon_spawn_egg");
        
        @SuppressWarnings("unchecked")
        EntityType<? extends MobEntity> entityType = (EntityType<? extends MobEntity>) ModEntityTypes.AETHELON;
        
        AETHELON_SPAWN_EGG = Registry.register(
                Registries.ITEM,
                spawnEggId,
                new SpawnEggItem(entityType, new Item.Settings())
        );
        
        // Add spawn egg to creative inventory
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> {
            content.add(AETHELON_SPAWN_EGG);
        });
        
        Aethelon.LOGGER.info("Items registered successfully");
    }
}
```

## Complete Implementation Examples

### Example 1: Simple Water Creature (Based on Aethelon)

#### Entity Class Structure
```java
public class AethelonEntity extends WaterCreatureEntity {
    public AethelonEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public static DefaultAttributeContainer.Builder createAethelonAttributes() {
        return WaterCreatureEntity.createMobAttributes()
            .add(EntityAttributes.GENERIC_MAX_HEALTH, 500.0)
            .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1)
            .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0);
    }
    
    // Additional entity methods...
}
```

#### Registration Pattern
```java
// 1. Entity Type Registration
public static void registerEntityTypes() {
    RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MOD_ID, "entity_name"));
    
    ENTITY_TYPE = Registry.register(
        Registries.ENTITY_TYPE,
        key,
        FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, EntityClass::new)
            .dimensions(EntityDimensions.fixed(8.0f, 4.0f))
            .maxTrackingRange(32)
            .trackedUpdateRate(1)
            .build(key)
    );
    
    FabricDefaultAttributeRegistry.register(ENTITY_TYPE, EntityClass.createAttributes());
}

// 2. Spawn Egg Registration
public static void registerItems() {
    SPAWN_EGG = Registry.register(
        Registries.ITEM,
        Identifier.of(MOD_ID, "entity_spawn_egg"),
        new SpawnEggItem(ENTITY_TYPE, new Item.Settings())
    );
    
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> {
        content.add(SPAWN_EGG);
    });
}
```

### Example 2: Land Creature with Custom Spawn Rules (Based on Snuffles)

```java
// Entity Registration with Spawn Restrictions
public static final EntityType<CustomEntity> CUSTOM_ENTITY = FabricEntityType.Builder
    .createMob(CustomEntity::new, SpawnGroup.CREATURE, (mob) -> 
        mob.defaultAttributes(CustomEntity::createAttributes)
           .spawnRestriction(SpawnLocationTypes.ON_GROUND, 
                           Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, 
                           CustomEntity::checkSpawnRules))
    .dimensions(1.2F, 1.2F)
    .maxTrackingRange(8)
    .build(key);

// Spawn Rules Method in Entity Class
public static boolean checkSpawnRules(EntityType<CustomEntity> type, WorldAccess world, 
                                    SpawnReason spawnReason, BlockPos pos, Random random) {
    return world.getBlockState(pos.down()).isIn(BlockTags.ANIMALS_SPAWNABLE_ON) 
           && world.getBaseLightLevel(pos, 0) > 8;
}
```

## Best Practices

### 1. Registration Order
Always register entities before items that depend on them:
```java
public void onInitialize() {
    ModEntityTypes.registerEntityTypes();  // First
    ModItems.registerItems();              // Second (depends on entity types)
}
```

### 2. Error Handling
Add validation to prevent null entity types:
```java
public static void registerItems() {
    if (ModEntityTypes.ENTITY == null) {
        throw new IllegalStateException("Entity type is null! Register entities first.");
    }
    // Continue with item registration...
}
```

### 3. Logging
Add informative logging for debugging:
```java
public static void registerEntityTypes() {
    LOGGER.info("Registering entity types");
    // Registration code...
    LOGGER.info("Entity types registered successfully");
}
```

### 4. Registry Keys
Use consistent naming patterns:
```java
RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MOD_ID, "entity_name"));
Identifier spawnEggId = Identifier.of(MOD_ID, "entity_name_spawn_egg");
```

## Common Patterns from Knowledge Pool

### Entity Dimensions by Type
- **Small creatures** (Duck): `0.6f x 0.6f`
- **Medium creatures** (Snuffle): `1.2f x 1.2f`
- **Large creatures** (Aethelon): `8.0f x 4.0f`
- **Projectiles** (Eggs): `0.25f x 0.25f`

### Tracking Ranges by Type
- **Small/Medium mobs**: `8-10` chunks
- **Large mobs**: `32` chunks
- **Projectiles**: `4` chunks

### Spawn Groups
- **CREATURE**: Land animals, peaceful mobs
- **WATER_CREATURE**: Aquatic animals
- **MISC**: Projectiles, non-living entities
- **MONSTER**: Hostile mobs

### Creative Tab Integration
Always add spawn eggs to the spawn eggs creative tab:
```java
ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> {
    content.add(SPAWN_EGG);
});
```

## File Structure Reference

```
src/main/java/com/yourmod/modname/
├── ModName.java                    // Main mod class
├── entity/
│   └── CustomEntity.java          // Entity implementation
├── registry/
│   ├── ModEntityTypes.java        // Entity type registration
│   └── ModItems.java              // Item registration (including spawn eggs)
└── client/
    ├── model/
    │   ├── ModEntityModelLayers.java
    │   └── CustomEntityModel.java
    └── render/
        └── CustomEntityRenderer.java
```

## Additional Examples from Knowledge Pool

### Enderscape Mod - Advanced Spawn Egg Registration
```java
// From: knowledge_pool/enderscape-1.21.4/src/main/java/net/bunten/enderscape/registry/EnderscapeItems.java
// Shows advanced spawn egg registration with custom colors

public class EnderscapeItems {
    // Spawn eggs with custom primary and secondary colors
    public static final Item DRIFT_SPAWN_EGG = register("drift_spawn_egg", 
        () -> new SpawnEggItem(EnderscapeEntityTypes.DRIFT, 0x2D1B69, 0x7209B7, new Item.Settings()));
    
    public static final Item STALKER_SPAWN_EGG = register("stalker_spawn_egg", 
        () -> new SpawnEggItem(EnderscapeEntityTypes.STALKER, 0x0F0F23, 0x4C1130, new Item.Settings()));
}
```

### Universal Taming - Entity Conversion Pattern
```java
// From: universaltaming/src/main/java/com/bvhfve/universaltaming/entity/TamedGenericEntity.java
// Shows how to create entities that can represent multiple vanilla entity types

public class TamedGenericEntity extends TameableEntity {
    private EntityType<?> originalEntityType;
    
    public TamedGenericEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }
    
    // Method to convert from vanilla entity to tamed version
    public static TamedGenericEntity fromVanillaEntity(MobEntity original) {
        TamedGenericEntity tamed = new TamedGenericEntity(ModEntityTypes.TAMED_GENERIC.get(), original.getWorld());
        tamed.originalEntityType = original.getType();
        // Copy attributes, position, etc.
        return tamed;
    }
}
```

### Survival Plus - Multiple Entity Registration
```java
// From: universaltaming/knowledgebase/survival-plus-master/src/main/java/survivalplus/modid/entity/custom/
// Shows pattern for registering multiple related entities

public class ModEntityTypes {
    // Multiple hostile entity variants
    public static final EntityType<ScorchedSkeletonEntity> SCORCHED_SKELETON = register("scorched_skeleton",
        EntityType.Builder.of(ScorchedSkeletonEntity::new, MobCategory.MONSTER)
            .sized(0.6F, 1.99F)
            .build("scorched_skeleton"));
    
    public static final EntityType<ReeperEntity> REEPER = register("reeper",
        EntityType.Builder.of(ReeperEntity::new, MobCategory.MONSTER)
            .sized(0.6F, 1.7F)
            .build("reeper"));
    
    public static final EntityType<MinerZombieEntity> MINER_ZOMBIE = register("miner_zombie",
        EntityType.Builder.of(MinerZombieEntity::new, MobCategory.MONSTER)
            .sized(0.6F, 1.95F)
            .build("miner_zombie"));
}
```

## Platform-Specific Implementations

### Fabric Implementation Helper
```java
// Helper method for consistent Fabric registration
public static <T extends Entity> EntityType<T> registerEntity(String name, EntityType<T> entityType) {
    return Registry.register(Registries.ENTITY_TYPE, Identifier.of(MOD_ID, name), entityType);
}

public static <T extends Item> T registerItem(String name, T item) {
    return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), item);
}

// Usage example
public static final EntityType<CustomEntity> CUSTOM_ENTITY = registerEntity("custom_entity",
    FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CustomEntity::new)
        .dimensions(EntityDimensions.fixed(1.0f, 1.0f))
        .build());

public static final Item CUSTOM_SPAWN_EGG = registerItem("custom_spawn_egg",
    new SpawnEggItem(CUSTOM_ENTITY, new Item.Settings()));
```

### Multi-Platform Registration (Architectury)
```java
// From UntitledDuckMod - shows cross-platform registration pattern
public class RegistryHelper {
    @ExpectPlatform
    public static <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, Supplier<EntityType<T>> entity) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static <T extends Item> Supplier<T> registerItem(String name, Function<Item.Settings, T> item, Item.Settings settings) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static Supplier<SpawnEggItem> registerSpawnEggItem(String name, Supplier<EntityType<? extends MobEntity>> entityType) {
        throw new AssertionError();
    }
}
```

## Troubleshooting Common Issues

### Issue 1: Null Entity Type
**Problem**: Spawn egg registration fails with null entity type
**Solution**: Ensure entity types are registered before items
```java
// Wrong order
ModItems.registerItems();      // Fails - entity type not available yet
ModEntityTypes.registerEntityTypes();

// Correct order
ModEntityTypes.registerEntityTypes();  // First
ModItems.registerItems();              // Second
```

### Issue 2: Spawn Egg Not Appearing in Creative Tab
**Problem**: Spawn egg registered but not visible in creative inventory
**Solution**: Add to spawn eggs item group
```java
ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(content -> {
    content.add(SPAWN_EGG);
});
```

### Issue 3: Entity Not Spawning from Egg
**Problem**: Spawn egg exists but doesn't spawn entity
**Solution**: Verify entity type casting and registration
```java
// Ensure proper casting for SpawnEggItem
@SuppressWarnings("unchecked")
EntityType<? extends MobEntity> entityType = (EntityType<? extends MobEntity>) ENTITY_TYPE;

SPAWN_EGG = new SpawnEggItem(entityType, new Item.Settings());
```

### Issue 4: Custom Spawn Egg Colors
**Problem**: Want custom colors for spawn egg
**Solution**: Use SpawnEggItem constructor with color parameters
```java
// Custom colors (primary, secondary)
new SpawnEggItem(ENTITY_TYPE, 0x2D1B69, 0x7209B7, new Item.Settings())
```

# Advanced Implementation Patterns

## Client-Side Rendering

### Entity Renderer Registration
```java
// From: aethelon/src/client/java/com/bvhfve/aethelon/client/render/AethelonEntityRenderer.java
// Basic entity renderer implementation

public class AethelonEntityRenderer extends MobEntityRenderer<AethelonEntity, AethelonEntityModel<AethelonEntity>> {
    private static final Identifier TEXTURE = Identifier.of(Aethelon.MOD_ID, "textures/entity/aethelon.png");
    
    public AethelonEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new AethelonEntityModel<>(context.getPart(ModEntityModelLayers.AETHELON)), 4.0f);
    }
    
    @Override
    public Identifier getTexture(AethelonEntity entity) {
        return TEXTURE;
    }
}

// Registration in client initializer
EntityRendererRegistry.register(ModEntityTypes.AETHELON, AethelonEntityRenderer::new);
```

### Entity Model Implementation
```java
// From: aethelon/src/client/java/com/bvhfve/aethelon/client/model/AethelonEntityModel.java
// Complete entity model with animations

public class AethelonEntityModel<T extends AethelonEntity> extends EntityModel<T> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart shell;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart backLeftLeg;
    private final ModelPart backRightLeg;
    
    public AethelonEntityModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.shell = root.getChild("shell");
        this.frontLeftLeg = root.getChild("front_left_leg");
        this.frontRightLeg = root.getChild("front_right_leg");
        this.backLeftLeg = root.getChild("back_left_leg");
        this.backRightLeg = root.getChild("back_right_leg");
    }
    
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        // Body - main turtle body
        modelPartData.addChild("body", 
            ModelPartBuilder.create()
                .uv(0, 0)
                .cuboid(-6.0F, -4.0F, -8.0F, 12.0F, 8.0F, 16.0F),
            ModelTransform.pivot(0.0F, 20.0F, 0.0F));
        
        // Head - turtle head extending forward
        modelPartData.addChild("head",
            ModelPartBuilder.create()
                .uv(0, 24)
                .cuboid(-3.0F, -2.0F, -6.0F, 6.0F, 4.0F, 6.0F),
            ModelTransform.pivot(0.0F, 18.0F, -8.0F));
        
        // Shell - large shell on back (where island will sit)
        modelPartData.addChild("shell",
            ModelPartBuilder.create()
                .uv(40, 0)
                .cuboid(-8.0F, -6.0F, -10.0F, 16.0F, 6.0F, 20.0F),
            ModelTransform.pivot(0.0F, 16.0F, 0.0F));
        
        // Legs - four turtle flippers
        modelPartData.addChild("front_left_leg",
            ModelPartBuilder.create()
                .uv(0, 34)
                .cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F),
            ModelTransform.pivot(4.0F, 20.0F, -4.0F));
        
        // Additional legs...
        
        return TexturedModelData.of(modelData, 128, 64);
    }
    
    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Basic swimming animation
        this.head.yaw = netHeadYaw * 0.017453292F;
        this.head.pitch = headPitch * 0.017453292F;
        
        // Gentle leg movement for swimming
        float legSwing = limbSwing * 0.6662F;
        float legAmount = limbSwingAmount * 0.5F;
        
        this.frontLeftLeg.roll = (float) Math.cos(legSwing) * legAmount;
        this.frontRightLeg.roll = (float) Math.cos(legSwing + Math.PI) * legAmount;
        this.backLeftLeg.roll = (float) Math.cos(legSwing + Math.PI) * legAmount;
        this.backRightLeg.roll = (float) Math.cos(legSwing) * legAmount;
    }
    
    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        root.render(matrices, vertexConsumer, light, overlay, color);
    }
}
```

### Model Layer Registration
```java
// From: aethelon/src/client/java/com/bvhfve/aethelon/client/model/ModEntityModelLayers.java
// Model layer registration pattern

public class ModEntityModelLayers {
    public static final EntityModelLayer AETHELON = new EntityModelLayer(
            Identifier.of(Aethelon.MOD_ID, "aethelon"), "main");
    
    public static void registerModelLayers() {
        EntityModelLayerRegistry.registerModelLayer(AETHELON, AethelonEntityModel::getTexturedModelData);
    }
}

// Called in client initializer
ModEntityModelLayers.registerModelLayers();
```

### Advanced Renderer Features
```java
// From: universaltaming/src/main/java/com/bvhfve/universaltaming/client/render/TamedGenericEntityRenderer.java
// Advanced renderer with dynamic textures and features

public class TamedGenericEntityRenderer extends MobEntityRenderer<TamedGenericEntity, TamedGenericEntityModel> {
    private static final Map<EntityType<?>, Identifier> TEXTURE_CACHE = new HashMap<>();
    
    public TamedGenericEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new TamedGenericEntityModel(context.getPart(ModEntityModelLayers.TAMED_GENERIC)), 0.5f);
        
        // Add feature renderers for accessories
        this.addFeature(new TamedEntityCollarFeatureRenderer<>(this));
        this.addFeature(new TamedEntityNametagFeatureRenderer<>(this));
    }
    
    @Override
    public Identifier getTexture(TamedGenericEntity entity) {
        EntityType<?> originalType = entity.getOriginalEntityType();
        return TEXTURE_CACHE.computeIfAbsent(originalType, type -> 
            Identifier.of(MOD_ID, "textures/entity/tamed/" + getTextureName(type) + ".png"));
    }
    
    @Override
    protected void scale(TamedGenericEntity entity, MatrixStack matrices, float amount) {
        // Dynamic scaling based on original entity size
        float scale = entity.getOriginalEntityScale();
        matrices.scale(scale, scale, scale);
    }
    
    @Override
    protected boolean hasLabel(TamedGenericEntity entity) {
        return entity.hasCustomName() || entity.isTamed();
    }
}
```

## AI Goals and Behavior

### Custom AI Goal Implementation
```java
// Example custom AI goal for Aethelon entity
public class AethelonIdleGoal extends Goal {
    private final AethelonEntity aethelon;
    private int idleTime;
    private final int maxIdleTime;
    
    public AethelonIdleGoal(AethelonEntity aethelon) {
        this.aethelon = aethelon;
        this.maxIdleTime = 200 + aethelon.getRandom().nextInt(200); // 10-20 seconds
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }
    
    @Override
    public boolean canStart() {
        return aethelon.getCurrentState() == AethelonEntity.AethelonState.IDLE 
               && aethelon.getTarget() == null;
    }
    
    @Override
    public boolean shouldContinue() {
        return idleTime < maxIdleTime && aethelon.getCurrentState() == AethelonEntity.AethelonState.IDLE;
    }
    
    @Override
    public void start() {
        idleTime = 0;
        aethelon.getNavigation().stop();
    }
    
    @Override
    public void tick() {
        idleTime++;
        
        // Gentle head movement while idle
        if (idleTime % 40 == 0) {
            aethelon.getLookControl().lookAt(
                aethelon.getX() + (aethelon.getRandom().nextDouble() - 0.5) * 10,
                aethelon.getY(),
                aethelon.getZ() + (aethelon.getRandom().nextDouble() - 0.5) * 10
            );
        }
    }
    
    @Override
    public void stop() {
        idleTime = 0;
    }
}
```

### Pathfinding Goal
```java
public class AethelonPathfindGoal extends Goal {
    private final AethelonEntity aethelon;
    private BlockPos targetPos;
    private int pathfindingCooldown;
    
    public AethelonPathfindGoal(AethelonEntity aethelon) {
        this.aethelon = aethelon;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }
    
    @Override
    public boolean canStart() {
        return aethelon.getCurrentState() == AethelonEntity.AethelonState.MOVING
               && pathfindingCooldown <= 0
               && aethelon.getTarget() == null;
    }
    
    @Override
    public void start() {
        targetPos = findSuitableDestination();
        if (targetPos != null) {
            aethelon.getNavigation().startMovingTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 0.5);
        }
        pathfindingCooldown = 100; // 5 second cooldown
    }
    
    @Override
    public boolean shouldContinue() {
        return targetPos != null 
               && !aethelon.getNavigation().isIdle()
               && aethelon.getCurrentState() == AethelonEntity.AethelonState.MOVING;
    }
    
    @Override
    public void tick() {
        if (pathfindingCooldown > 0) {
            pathfindingCooldown--;
        }
        
        // Check if reached destination
        if (targetPos != null && aethelon.squaredDistanceTo(Vec3d.ofCenter(targetPos)) < 4.0) {
            aethelon.setState(AethelonEntity.AethelonState.IDLE);
        }
    }
    
    private BlockPos findSuitableDestination() {
        // Find deep water location within range
        World world = aethelon.getWorld();
        BlockPos currentPos = aethelon.getBlockPos();
        
        for (int attempts = 0; attempts < 10; attempts++) {
            int x = currentPos.getX() + aethelon.getRandom().nextInt(200) - 100;
            int z = currentPos.getZ() + aethelon.getRandom().nextInt(200) - 100;
            int y = world.getSeaLevel();
            
            BlockPos testPos = new BlockPos(x, y, z);
            
            // Check if position is suitable (deep water, no obstacles)
            if (isSuitablePosition(world, testPos)) {
                return testPos;
            }
        }
        
        return null;
    }
    
    private boolean isSuitablePosition(World world, BlockPos pos) {
        // Check for deep water (at least 10 blocks deep)
        for (int i = 0; i < 10; i++) {
            if (!world.getBlockState(pos.down(i)).isOf(Blocks.WATER)) {
                return false;
            }
        }
        
        // Check for clear space above (for shell/island)
        for (int i = 1; i <= 20; i++) {
            BlockState state = world.getBlockState(pos.up(i));
            if (!state.isAir() && !state.isOf(Blocks.WATER)) {
                return false;
            }
        }
        
        return true;
    }
}
```

### SmartBrainLib Integration Example
```java
// From: knowledge_pool/SmartBrainLib - Advanced AI framework
// Example of using SmartBrainLib for complex AI behaviors

public class AdvancedAethelonEntity extends SmartBrainOwner<AdvancedAethelonEntity> {
    
    @Override
    protected BrainActivityGroup<AdvancedAethelonEntity> coreTasks() {
        return BrainActivityGroup.coreTasks(
            new LookAtTarget<>(),                    // Basic look behavior
            new MoveToWalkTarget<>(),               // Basic movement
            new FollowEntity<AdvancedAethelonEntity>() // Follow specific entities
                .stopIf(entity -> entity.distanceTo(entity.getOwner()) < 10)
                .speedMod(0.5f)
        );
    }
    
    @Override
    protected BrainActivityGroup<AdvancedAethelonEntity> idleTasks() {
        return BrainActivityGroup.idleTasks(
            new FirstApplicableBehaviour<AdvancedAethelonEntity>(
                new TargetOrRetaliate<>(),          // Defensive behavior
                new SetPlayerLookTarget<>(),        // Look at nearby players
                new SetRandomLookTarget<>()         // Random looking
            ),
            new OneRandomBehaviour<>(
                new SetRandomWalkTarget<>().speedModifier(0.3f), // Slow wandering
                new Idle<>().runFor(entity -> entity.getRandom().nextInt(200, 400))
            )
        );
    }
    
    @Override
    protected BrainActivityGroup<AdvancedAethelonEntity> fightTasks() {
        return BrainActivityGroup.fightTasks(
            new InvalidateAttackTarget<>().invalidateIf((entity, target) -> 
                target.distanceTo(entity) > 32), // Stop attacking if too far
            new SetWalkTargetToAttackTarget<>().speedMod(0.7f),
            new AnimatableMeleeAttack<>(20) // Custom attack with 20 tick cooldown
        );
    }
}
```

## Entity State Management

### State Machine Implementation
```java
// From: aethelon/src/main/java/com/bvhfve/aethelon/entity/AethelonEntity.java
// Advanced state management system

public class AethelonEntity extends WaterCreatureEntity {
    public enum AethelonState {
        IDLE(200, 600),           // 10-30 seconds
        MOVING(1200, 2400),       // 1-2 minutes
        TRANSITIONING(40, 80),    // 2-4 seconds
        DAMAGED(100, 200);        // 5-10 seconds
        
        private final int minDuration;
        private final int maxDuration;
        
        AethelonState(int minDuration, int maxDuration) {
            this.minDuration = minDuration;
            this.maxDuration = maxDuration;
        }
        
        public int getRandomDuration(Random random) {
            return minDuration + random.nextInt(maxDuration - minDuration);
        }
    }
    
    private AethelonState currentState = AethelonState.IDLE;
    private int stateTimer = 0;
    private int stateDuration = 0;
    
    @Override
    public void tick() {
        super.tick();
        
        stateTimer++;
        
        // State transition logic
        if (stateTimer >= stateDuration) {
            transitionToNextState();
        }
        
        // State-specific behavior
        switch (currentState) {
            case IDLE -> tickIdleState();
            case MOVING -> tickMovingState();
            case TRANSITIONING -> tickTransitioningState();
            case DAMAGED -> tickDamagedState();
        }
    }
    
    private void transitionToNextState() {
        AethelonState nextState = determineNextState();
        setState(nextState);
    }
    
    private AethelonState determineNextState() {
        return switch (currentState) {
            case IDLE -> getRandom().nextFloat() < 0.3f ? AethelonState.TRANSITIONING : AethelonState.IDLE;
            case TRANSITIONING -> AethelonState.MOVING;
            case MOVING -> AethelonState.IDLE;
            case DAMAGED -> AethelonState.IDLE;
        };
    }
    
    public void setState(AethelonState newState) {
        if (this.currentState != newState) {
            AethelonState oldState = this.currentState;
            this.currentState = newState;
            this.stateTimer = 0;
            this.stateDuration = newState.getRandomDuration(getRandom());
            
            onStateChanged(oldState, newState);
        }
    }
    
    private void onStateChanged(AethelonState oldState, AethelonState newState) {
        // State transition effects
        if (newState == AethelonState.TRANSITIONING) {
            // Play transition sound/particles
            playSound(SoundEvents.ENTITY_TURTLE_AMBIENT, 2.0f, 0.8f);
        }
        
        // Update AI goals based on state
        goalSelector.getGoals().forEach(goal -> {
            if (goal.getGoal() instanceof StateAwareGoal stateGoal) {
                stateGoal.onStateChanged(oldState, newState);
            }
        });
    }
    
    private void tickIdleState() {
        // Gentle bobbing motion
        if (stateTimer % 60 == 0) {
            setVelocity(getVelocity().add(0, 0.02, 0));
        }
    }
    
    private void tickMovingState() {
        // Ensure movement continues
        if (getNavigation().isIdle()) {
            // Find new destination or return to idle
            setState(AethelonState.IDLE);
        }
    }
    
    private void tickTransitioningState() {
        // Preparation animations/effects
        if (stateTimer == 1) {
            // Start transition particles
            spawnTransitionParticles();
        }
    }
    
    private void tickDamagedState() {
        // Damage response behavior
        if (stateTimer % 10 == 0) {
            // Shake effect or defensive posture
        }
    }
}
```

## Data-Driven Entity Configuration

### Entity Data Components
```java
// Modern data component approach for entity customization
public class EntityDataComponents {
    public static final ComponentType<EntitySize> ENTITY_SIZE = ComponentType.<EntitySize>builder()
        .codec(EntitySize.CODEC)
        .build();
    
    public static final ComponentType<EntityBehaviorConfig> BEHAVIOR_CONFIG = ComponentType.<EntityBehaviorConfig>builder()
        .codec(EntityBehaviorConfig.CODEC)
        .build();
    
    public static void register() {
        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MOD_ID, "entity_size"), ENTITY_SIZE);
        Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MOD_ID, "behavior_config"), BEHAVIOR_CONFIG);
    }
}

public record EntitySize(float width, float height, float scale) {
    public static final Codec<EntitySize> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.FLOAT.fieldOf("width").forGetter(EntitySize::width),
            Codec.FLOAT.fieldOf("height").forGetter(EntitySize::height),
            Codec.FLOAT.fieldOf("scale").forGetter(EntitySize::scale)
        ).apply(instance, EntitySize::new)
    );
}

public record EntityBehaviorConfig(
    float movementSpeed,
    float aggressionLevel,
    int idleTime,
    boolean canSwim
) {
    public static final Codec<EntityBehaviorConfig> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.FLOAT.fieldOf("movement_speed").forGetter(EntityBehaviorConfig::movementSpeed),
            Codec.FLOAT.fieldOf("aggression_level").forGetter(EntityBehaviorConfig::aggressionLevel),
            Codec.INT.fieldOf("idle_time").forGetter(EntityBehaviorConfig::idleTime),
            Codec.BOOL.fieldOf("can_swim").forGetter(EntityBehaviorConfig::canSwim)
        ).apply(instance, EntityBehaviorConfig::new)
    );
}
```

## Real-World AI Goal Examples from Knowledge Pool

### Tamed Entity Follow Goal
```java
// From: universaltaming/src/main/java/com/bvhfve/universaltaming/entity/ai/TamedFollowOwnerGoal.java
// Advanced follow owner goal with teleportation and pathfinding

public class TamedFollowOwnerGoal extends Goal {
    private final TamedGenericEntity entity;
    private LivingEntity owner;
    private final EntityNavigation navigation;
    private int updateCountdownTicks;
    private final float maxDistance;
    private final float minDistance;
    private final float speed;
    private final boolean leavesAllowed;

    public TamedFollowOwnerGoal(TamedGenericEntity entity, float speed, float minDistance, float maxDistance, boolean leavesAllowed) {
        this.entity = entity;
        this.navigation = entity.getNavigation();
        this.speed = speed;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.leavesAllowed = leavesAllowed;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity owner = this.entity.getOwner();
        if (owner == null) return false;
        if (owner.isSpectator()) return false;
        if (this.entity.isSitting()) return false;
        if (this.entity.squaredDistanceTo(owner) < (double)(this.minDistance * this.minDistance)) return false;
        
        this.owner = owner;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        if (this.navigation.isIdle()) return false;
        if (this.entity.isSitting()) return false;
        return !(this.entity.squaredDistanceTo(this.owner) <= (double)(this.maxDistance * this.maxDistance));
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.updateCountdownTicks = 0;
    }

    @Override
    public void tick() {
        this.entity.getLookControl().lookAt(this.owner, 10.0f, this.entity.getMaxLookPitchChange());
        
        if (--this.updateCountdownTicks <= 0) {
            this.updateCountdownTicks = this.getTickCount(10);
            
            if (!this.entity.isLeashed() && !this.entity.hasVehicle()) {
                if (this.entity.squaredDistanceTo(this.owner) >= 144.0) {
                    this.tryTeleport();
                } else {
                    this.navigation.startMovingTo(this.owner, this.speed);
                }
            }
        }
    }

    private void tryTeleport() {
        BlockPos ownerPos = this.owner.getBlockPos();
        
        for (int i = 0; i < 10; ++i) {
            int x = this.getRandomInt(-3, 3);
            int z = this.getRandomInt(-3, 3);
            int y = this.getRandomInt(-1, 1);
            
            if (this.tryTeleportTo(ownerPos.getX() + x, ownerPos.getY() + y, ownerPos.getZ() + z)) {
                return;
            }
        }
    }

    private boolean tryTeleportTo(int x, int y, int z) {
        if (Math.abs(x - this.owner.getX()) < 2.0 && Math.abs(z - this.owner.getZ()) < 2.0) {
            return false;
        }
        
        if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        }
        
        this.entity.refreshPositionAndAngles(x + 0.5, y, z + 0.5, this.entity.getYaw(), this.entity.getPitch());
        this.navigation.stop();
        return true;
    }

    private boolean canTeleportTo(BlockPos pos) {
        PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(this.entity.getWorld(), pos.mutableCopy());
        if (pathNodeType != PathNodeType.WALKABLE) {
            return false;
        }
        
        BlockState blockState = this.entity.getWorld().getBlockState(pos.down());
        if (!this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock) {
            return false;
        }
        
        BlockPos blockPos = pos.subtract(this.entity.getBlockPos());
        return this.entity.getWorld().isSpaceEmpty(this.entity, this.entity.getBoundingBox().offset(blockPos));
    }

    private int getRandomInt(int min, int max) {
        return this.entity.getRandom().nextInt(max - min + 1) + min;
    }
}
```

### Fox Friend Follow Goal
```java
// From: knowledge_pool/Minecraft-Mod-FoxFriend-master/.../fabric-1.21.4/src/main/java/suike/suikefoxfriend/entity/ai/FoxFollowOwnerGoal.java
// Specialized follow goal for fox entities

public class FoxFollowOwnerGoal extends Goal {
    private final FoxEntity fox;
    private LivingEntity owner;
    private final EntityNavigation navigation;
    private int updateCountdownTicks;
    private final float maxDistance;
    private final float minDistance;
    private final float speed;

    public FoxFollowOwnerGoal(FoxEntity fox, float speed, float minDistance, float maxDistance) {
        this.fox = fox;
        this.navigation = fox.getNavigation();
        this.speed = speed;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (!FoxFriendUtil.isFoxTamed(this.fox)) return false;
        
        LivingEntity owner = FoxFriendUtil.getFoxOwner(this.fox);
        if (owner == null) return false;
        if (owner.isSpectator()) return false;
        if (this.fox.squaredDistanceTo(owner) < (double)(this.minDistance * this.minDistance)) return false;
        
        this.owner = owner;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        if (this.navigation.isIdle()) return false;
        return !(this.fox.squaredDistanceTo(this.owner) <= (double)(this.maxDistance * this.maxDistance));
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.updateCountdownTicks = 0;
    }

    @Override
    public void tick() {
        this.fox.getLookControl().lookAt(this.owner, 10.0f, this.fox.getMaxLookPitchChange());
        
        if (--this.updateCountdownTicks <= 0) {
            this.updateCountdownTicks = this.getTickCount(10);
            
            if (!this.fox.isLeashed() && !this.fox.hasVehicle()) {
                if (this.fox.squaredDistanceTo(this.owner) >= 144.0) {
                    this.tryTeleport();
                } else {
                    this.navigation.startMovingTo(this.owner, this.speed);
                }
            }
        }
    }

    private void tryTeleport() {
        BlockPos ownerPos = this.owner.getBlockPos();
        
        for (int i = 0; i < 10; ++i) {
            int x = this.getRandomInt(-3, 3);
            int z = this.getRandomInt(-3, 3);
            int y = this.getRandomInt(-1, 1);
            
            if (this.tryTeleportTo(ownerPos.getX() + x, ownerPos.getY() + y, ownerPos.getZ() + z)) {
                return;
            }
        }
    }

    private boolean tryTeleportTo(int x, int y, int z) {
        if (Math.abs(x - this.owner.getX()) < 2.0 && Math.abs(z - this.owner.getZ()) < 2.0) {
            return false;
        }
        
        if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        }
        
        this.fox.refreshPositionAndAngles(x + 0.5, y, z + 0.5, this.fox.getYaw(), this.fox.getPitch());
        this.navigation.stop();
        return true;
    }

    private boolean canTeleportTo(BlockPos pos) {
        PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(this.fox.getWorld(), pos.mutableCopy());
        if (pathNodeType != PathNodeType.WALKABLE) {
            return false;
        }
        
        BlockPos blockPos = pos.subtract(this.fox.getBlockPos());
        return this.fox.getWorld().isSpaceEmpty(this.fox, this.fox.getBoundingBox().offset(blockPos));
    }

    private int getRandomInt(int min, int max) {
        return this.fox.getRandom().nextInt(max - min + 1) + min;
    }
}
```

## Entity Render States (Modern Minecraft)

### Custom Render State Implementation
```java
// From: knowledge_pool/snuffles-fabric-1.21.4/src/client/java/io/github/suel_ki/snuffles/client/renderer/entity/state/SnuffleEntityRenderState.java
// Modern render state pattern for entity-specific rendering data

public class SnuffleEntityRenderState extends LivingEntityRenderState {
    public boolean licking;
    public boolean frosty;
    public boolean frosting;
    public boolean fluff;
    public int hairstyleId;
    public Snuffle.Hairstyle hairstyle;
}

// Usage in renderer
public class SnuffleEntityRenderer extends MobEntityRenderer<Snuffle, SnuffleEntityRenderState, SnuffleEntityModel> {
    
    @Override
    public SnuffleEntityRenderState createRenderState() {
        return new SnuffleEntityRenderState();
    }
    
    @Override
    public void updateRenderState(Snuffle entity, SnuffleEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        
        // Update custom render state properties
        state.licking = entity.isLicking();
        state.frosty = entity.isFrosty();
        state.frosting = entity.isFrosting();
        state.fluff = entity.hasFluff();
        state.hairstyleId = entity.getHairstyleId();
        state.hairstyle = entity.getHairstyle();
    }
}

// Model uses render state instead of entity directly
public class SnuffleEntityModel extends EntityModel<SnuffleEntityRenderState> {
    
    @Override
    public void setAngles(SnuffleEntityRenderState state) {
        super.setAngles(state);
        
        // Use render state properties for animations
        if (state.licking) {
            this.tongue.pitch = (float) Math.sin(state.age * 0.3f) * 0.2f;
        }
        
        if (state.frosty) {
            // Add frost-specific animations
            this.body.roll = (float) Math.sin(state.age * 0.1f) * 0.05f;
        }
        
        // Hairstyle-specific model changes
        switch (state.hairstyle) {
            case FLUFFY -> this.hair.visible = true;
            case TRIMMED -> this.hair.visible = false;
            case STYLED -> {
                this.hair.visible = true;
                this.hair.pitch = 0.2f;
            }
        }
    }
}
```

## Advanced Entity Features

### Entity Data Synchronization
```java
// Modern data synchronization using TrackedData
public class AdvancedEntity extends MobEntity {
    // Tracked data for client-server synchronization
    private static final TrackedData<Boolean> IS_SPECIAL = DataTracker.registerData(AdvancedEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> SPECIAL_STATE = DataTracker.registerData(AdvancedEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<String> CUSTOM_NAME_DATA = DataTracker.registerData(AdvancedEntity.class, TrackedDataHandlerRegistry.STRING);
    
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(IS_SPECIAL, false);
        builder.add(SPECIAL_STATE, 0);
        builder.add(CUSTOM_NAME_DATA, "");
    }
    
    // Getters and setters with automatic synchronization
    public boolean isSpecial() {
        return this.dataTracker.get(IS_SPECIAL);
    }
    
    public void setSpecial(boolean special) {
        this.dataTracker.set(IS_SPECIAL, special);
    }
    
    public int getSpecialState() {
        return this.dataTracker.get(SPECIAL_STATE);
    }
    
    public void setSpecialState(int state) {
        this.dataTracker.set(SPECIAL_STATE, state);
    }
    
    // NBT serialization
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("IsSpecial", this.isSpecial());
        nbt.putInt("SpecialState", this.getSpecialState());
        nbt.putString("CustomNameData", this.dataTracker.get(CUSTOM_NAME_DATA));
    }
    
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setSpecial(nbt.getBoolean("IsSpecial"));
        this.setSpecialState(nbt.getInt("SpecialState"));
        this.dataTracker.set(CUSTOM_NAME_DATA, nbt.getString("CustomNameData"));
    }
}
```

### Entity Sound Events
```java
// Custom sound events for entities
public class ModSoundEvents {
    public static final SoundEvent AETHELON_AMBIENT = registerSoundEvent("entity.aethelon.ambient");
    public static final SoundEvent AETHELON_HURT = registerSoundEvent("entity.aethelon.hurt");
    public static final SoundEvent AETHELON_DEATH = registerSoundEvent("entity.aethelon.death");
    public static final SoundEvent AETHELON_STEP = registerSoundEvent("entity.aethelon.step");
    public static final SoundEvent AETHELON_SWIM = registerSoundEvent("entity.aethelon.swim");
    public static final SoundEvent AETHELON_TRANSITION = registerSoundEvent("entity.aethelon.transition");
    
    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    
    public static void registerSounds() {
        // Called during mod initialization
    }
}

// Usage in entity
public class AethelonEntity extends WaterCreatureEntity {
    
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.AETHELON_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSoundEvents.AETHELON_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.AETHELON_DEATH;
    }
    
    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(ModSoundEvents.AETHELON_STEP, 0.15f, 1.0f);
    }
    
    @Override
    protected SoundEvent getSwimSound() {
        return ModSoundEvents.AETHELON_SWIM;
    }
    
    // Custom sound for state transitions
    public void playTransitionSound() {
        this.playSound(ModSoundEvents.AETHELON_TRANSITION, 2.0f, 0.8f + this.random.nextFloat() * 0.4f);
    }
}
```

### Entity Loot Tables
```java
// Entity loot table generation
public class ModEntityLootTableProvider extends FabricEntityLootTableProvider {
    
    public ModEntityLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }
    
    @Override
    public void generate() {
        // Aethelon loot table
        add(ModEntityTypes.AETHELON, LootTable.builder()
            .pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(Items.TURTLE_SCUTE)
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2, 5))))
                .with(ItemEntry.builder(Items.KELP)
                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1, 3))))
                .conditionally(KilledByPlayerLootCondition.builder())
            )
            .pool(LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .with(ItemEntry.builder(ModItems.AETHELON_SHELL_FRAGMENT))
                .conditionally(RandomChanceLootCondition.builder(0.1f)) // 10% chance
                .conditionally(KilledByPlayerLootCondition.builder())
            )
        );
    }
}
```

### Entity Spawning Configuration
```java
// Biome modification for entity spawning
public class ModBiomeModifications {
    
    public static void registerSpawnConditions() {
        // Add Aethelon spawning to ocean biomes
        BiomeModifications.addSpawn(
            BiomeSelectors.includeByKey(BiomeKeys.DEEP_OCEAN, BiomeKeys.DEEP_COLD_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN),
            SpawnGroup.WATER_CREATURE,
            ModEntityTypes.AETHELON,
            1,  // weight
            1,  // min group size
            1   // max group size
        );
        
        // Custom spawn restrictions
        SpawnRestriction.register(
            ModEntityTypes.AETHELON,
            SpawnLocationTypes.IN_WATER,
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            AethelonEntity::canSpawn
        );
    }
}

// Custom spawn conditions in entity
public static boolean canSpawn(EntityType<AethelonEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
    // Must be in deep water (at least 15 blocks deep)
    for (int i = 0; i < 15; i++) {
        if (!world.getBlockState(pos.down(i)).isOf(Blocks.WATER)) {
            return false;
        }
    }
    
    // Must have clear space above for shell/island
    for (int i = 1; i <= 25; i++) {
        BlockState state = world.getBlockState(pos.up(i));
        if (!state.isAir() && !state.isOf(Blocks.WATER)) {
            return false;
        }
    }
    
    // Check world conditions
    return world.getDifficulty() != Difficulty.PEACEFUL 
           && world.getLightLevel(pos) <= 7
           && world.getBiome(pos).isIn(BiomeTags.IS_OCEAN);
}
```

## Complete Client-Side Setup

### Client Mod Initializer
```java
// Complete client-side initialization pattern
public class AethelonClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Aethelon client-side features");
        
        // Register entity renderers
        registerEntityRenderers();
        
        // Register model layers
        registerModelLayers();
        
        // Register particle factories (if needed)
        registerParticleFactories();
        
        // Register key bindings (if needed)
        registerKeyBindings();
        
        LOGGER.info("Aethelon client initialization complete");
    }
    
    private void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntityTypes.AETHELON, AethelonEntityRenderer::new);
        
        // Additional entity renderers
        // EntityRendererRegistry.register(ModEntityTypes.OTHER_ENTITY, OtherEntityRenderer::new);
    }
    
    private void registerModelLayers() {
        ModEntityModelLayers.registerModelLayers();
    }
    
    private void registerParticleFactories() {
        // ParticleFactoryRegistry.getInstance().register(ModParticles.AETHELON_BUBBLE, BubbleParticle.Factory::new);
    }
    
    private void registerKeyBindings() {
        // KeyBindingRegistry.registerKeyBinding(ModKeyBindings.AETHELON_INTERACT);
    }
}
```

### Complete Entity Model Layer Registry
```java
public class ModEntityModelLayers {
    // Main entity layers
    public static final EntityModelLayer AETHELON = new EntityModelLayer(
            Identifier.of(Aethelon.MOD_ID, "aethelon"), "main");
    
    // Variant layers (if needed)
    public static final EntityModelLayer AETHELON_BABY = new EntityModelLayer(
            Identifier.of(Aethelon.MOD_ID, "aethelon"), "baby");
    
    public static final EntityModelLayer AETHELON_ARMOR = new EntityModelLayer(
            Identifier.of(Aethelon.MOD_ID, "aethelon"), "armor");
    
    public static void registerModelLayers() {
        Aethelon.LOGGER.info("Registering entity model layers");
        
        // Register main model
        EntityModelLayerRegistry.registerModelLayer(AETHELON, AethelonEntityModel::getTexturedModelData);
        
        // Register variants
        EntityModelLayerRegistry.registerModelLayer(AETHELON_BABY, () -> AethelonEntityModel.getTexturedModelData(0.5f));
        EntityModelLayerRegistry.registerModelLayer(AETHELON_ARMOR, AethelonArmorModel::getTexturedModelData);
        
        Aethelon.LOGGER.info("Entity model layers registered successfully");
    }
}
```

### Feature Renderers for Accessories
```java
// Feature renderer for entity accessories/overlays
public class AethelonShellFeatureRenderer extends FeatureRenderer<AethelonEntity, AethelonEntityModel<AethelonEntity>> {
    private static final Identifier SHELL_TEXTURE = Identifier.of(Aethelon.MOD_ID, "textures/entity/aethelon_shell_overlay.png");
    private final AethelonEntityModel<AethelonEntity> model;
    
    public AethelonShellFeatureRenderer(FeatureRendererContext<AethelonEntity, AethelonEntityModel<AethelonEntity>> context) {
        super(context);
        this.model = new AethelonEntityModel<>(context.getPart(ModEntityModelLayers.AETHELON_ARMOR));
    }
    
    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, 
                      AethelonEntity entity, float limbAngle, float limbDistance, float tickDelta, 
                      float animationProgress, float headYaw, float headPitch) {
        
        if (entity.hasShellUpgrade()) {
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(SHELL_TEXTURE));
            
            this.getContextModel().copyStateTo(this.model);
            this.model.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, -1);
        }
    }
}

// Add to renderer constructor
public AethelonEntityRenderer(EntityRendererFactory.Context context) {
    super(context, new AethelonEntityModel<>(context.getPart(ModEntityModelLayers.AETHELON)), 4.0f);
    
    // Add feature renderers
    this.addFeature(new AethelonShellFeatureRenderer(this));
    this.addFeature(new AethelonGlowFeatureRenderer(this));
}
```

## Performance Optimization Patterns

### Entity Culling and LOD
```java
// Level-of-detail system for large entities
public class AethelonEntityRenderer extends MobEntityRenderer<AethelonEntity, AethelonEntityModel<AethelonEntity>> {
    
    @Override
    public void render(AethelonEntity entity, float yaw, float tickDelta, MatrixStack matrices, 
                      VertexConsumerProvider vertexConsumers, int light) {
        
        // Distance-based level of detail
        double distance = entity.squaredDistanceTo(MinecraftClient.getInstance().player);
        
        if (distance > 10000) { // Very far - skip detailed rendering
            renderSimplified(entity, yaw, tickDelta, matrices, vertexConsumers, light);
            return;
        } else if (distance > 2500) { // Far - reduced detail
            renderReduced(entity, yaw, tickDelta, matrices, vertexConsumers, light);
            return;
        }
        
        // Normal detailed rendering for close entities
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
    
    private void renderSimplified(AethelonEntity entity, float yaw, float tickDelta, 
                                 MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Render as simple colored cube or billboard
        matrices.push();
        matrices.scale(8.0f, 4.0f, 8.0f);
        
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(getTexture(entity)));
        // Render simple geometry
        
        matrices.pop();
    }
    
    private void renderReduced(AethelonEntity entity, float yaw, float tickDelta, 
                              MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        // Render with reduced polygon count model
        // Use simplified model or skip certain features
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
```

### Entity Update Optimization
```java
// Optimized entity ticking
public class AethelonEntity extends WaterCreatureEntity {
    private int optimizationCounter = 0;
    private static final int OPTIMIZATION_INTERVAL = 20; // Every second
    
    @Override
    public void tick() {
        super.tick();
        
        // Optimize expensive operations
        optimizationCounter++;
        if (optimizationCounter >= OPTIMIZATION_INTERVAL) {
            optimizationCounter = 0;
            performExpensiveOperations();
        }
        
        // Only update certain systems when players are nearby
        if (hasNearbyPlayers(32.0)) {
            updateDetailedBehavior();
        } else {
            updateSimplifiedBehavior();
        }
    }
    
    private boolean hasNearbyPlayers(double range) {
        return !getWorld().getPlayers().isEmpty() && 
               getWorld().getClosestPlayer(this, range) != null;
    }
    
    private void performExpensiveOperations() {
        // Pathfinding updates
        // Complex AI decisions
        // World scanning
    }
    
    private void updateDetailedBehavior() {
        // Full AI processing
        // Particle effects
        // Sound effects
        // Complex animations
    }
    
    private void updateSimplifiedBehavior() {
        // Basic movement only
        // Minimal processing
    }
}
```

## Testing and Debugging Utilities

### Entity Debug Renderer
```java
// Debug renderer for development
public class AethelonDebugRenderer {
    
    public static void renderDebugInfo(AethelonEntity entity, MatrixStack matrices, 
                                      VertexConsumerProvider vertexConsumers, int light) {
        if (!MinecraftClient.getInstance().getEntityRenderDispatcher().shouldRenderHitboxes()) {
            return;
        }
        
        matrices.push();
        
        // Render state information
        renderStateInfo(entity, matrices, vertexConsumers);
        
        // Render pathfinding debug
        renderPathfindingDebug(entity, matrices, vertexConsumers);
        
        // Render AI goal debug
        renderAIDebug(entity, matrices, vertexConsumers);
        
        matrices.pop();
    }
    
    private static void renderStateInfo(AethelonEntity entity, MatrixStack matrices, 
                                       VertexConsumerProvider vertexConsumers) {
        // Render current state above entity
        Text stateText = Text.literal("State: " + entity.getCurrentState().name());
        // Render text using font renderer
    }
    
    private static void renderPathfindingDebug(AethelonEntity entity, MatrixStack matrices, 
                                              VertexConsumerProvider vertexConsumers) {
        // Render current path
        // Render target position
        // Render obstacles
    }
    
    private static void renderAIDebug(AethelonEntity entity, MatrixStack matrices, 
                                     VertexConsumerProvider vertexConsumers) {
        // Render active goals
        // Render target entities
        // Render detection ranges
    }
}
```

### Entity Test Commands
```java
// Debug commands for testing entities
public class AethelonCommands {
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("aethelon")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("spawn")
                .executes(AethelonCommands::spawnAethelon))
            .then(CommandManager.literal("state")
                .then(CommandManager.argument("state", StringArgumentType.string())
                    .suggests((context, builder) -> {
                        for (AethelonEntity.AethelonState state : AethelonEntity.AethelonState.values()) {
                            builder.suggest(state.name().toLowerCase());
                        }
                        return builder.buildFuture();
                    })
                    .executes(AethelonCommands::setState)))
            .then(CommandManager.literal("debug")
                .executes(AethelonCommands::toggleDebug))
        );
    }
    
    private static int spawnAethelon(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerWorld world = source.getWorld();
        Vec3d pos = source.getPosition();
        
        AethelonEntity aethelon = new AethelonEntity(ModEntityTypes.AETHELON, world);
        aethelon.refreshPositionAndAngles(pos.x, pos.y, pos.z, 0, 0);
        world.spawnEntity(aethelon);
        
        source.sendFeedback(() -> Text.literal("Spawned Aethelon at " + pos), true);
        return 1;
    }
    
    private static int setState(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String stateName = StringArgumentType.getString(context, "state");
        // Find nearest Aethelon and set state
        return 1;
    }
    
    private static int toggleDebug(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        // Toggle debug rendering
        return 1;
    }
}
```

## Summary and Best Practices

### Key Takeaways
1. **Registration Order**: Always register entities before items that depend on them
2. **Client-Server Separation**: Keep rendering code in client-only classes
3. **Performance**: Use LOD and optimization techniques for large/complex entities
4. **State Management**: Implement robust state machines for complex behaviors
5. **Data Synchronization**: Use TrackedData for client-server sync
6. **Testing**: Include debug tools and commands for development

### Common Pitfalls to Avoid
1. **Null Entity Types**: Always validate entity registration before item registration
2. **Missing Model Layers**: Register all model layers in client initializer
3. **Performance Issues**: Don't run expensive operations every tick
4. **Memory Leaks**: Properly clean up references in AI goals
5. **Render State Confusion**: Use modern render state pattern for 1.21.4+

### Recommended Development Workflow
1. **Phase 1**: Basic entity registration and spawn egg
2. **Phase 2**: Basic model and renderer
3. **Phase 3**: AI goals and behavior
4. **Phase 4**: Advanced features (sounds, particles, etc.)
5. **Phase 5**: Optimization and polish
6. **Phase 6**: Testing and debugging tools

This reference document provides comprehensive patterns for implementing custom entities and spawn eggs in Minecraft Fabric mods, based on real examples from the knowledge pool and the current Aethelon implementation.