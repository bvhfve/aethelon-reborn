# 🎮 Manual Testing Guide for Phase 1

## Minecraft Should Be Loaded Now!

**Java Process Runtime**: 16+ minutes (Minecraft should be fully loaded)

## 🔍 Step-by-Step Testing Instructions

### Step 1: Verify Mod Loading
1. **Check the Minecraft window** - should show main menu or world
2. **Look for any error popups** - mod loading issues would show here
3. **Check console/logs** for our mod messages:
   - "Initializing Aethelon - The World Turtle mod"
   - "Registering Aethelon entity types"
   - "Registering Aethelon items"

### Step 2: Create Test World
1. **Click "Singleplayer"**
2. **Click "Create New World"**
3. **Set Game Mode to "Creative"**
4. **Click "Create New World"**

### Step 3: Test Spawn Egg
1. **Press 'E'** to open Creative inventory
2. **Click "Spawn Eggs" tab** (egg icon)
3. **Look for "Aethelon Spawn Egg"** 
   - Should be **dark green with brown spots**
   - Should be named "Aethelon Spawn Egg"
4. **Take spawn egg** into hotbar

### Step 4: Test Entity Spawning
1. **Find or create water** (ocean, lake, or place water blocks)
2. **Right-click with spawn egg** in the water
3. **Look for large turtle entity** to appear
4. **Expected appearance**:
   - **Large size** (8x4 blocks - much bigger than player)
   - **Turtle shape** with body, head, shell, four legs
   - **May have placeholder texture** (purple/black checkerboard - this is OK!)

### Step 5: Test Entity Behavior
1. **Check entity health** - should show large health bar when looking at it
2. **Watch movement** - should swim slowly in water
3. **Look for animations** - legs should move while swimming
4. **Test damage** - hit entity, should take damage but have lots of HP
5. **Check size** - should be much larger than normal mobs

## ✅ Success Indicators

### 🎯 PASS Criteria
- [ ] **Mod loads** without crashes
- [ ] **Spawn egg appears** in creative spawn eggs tab
- [ ] **Entity spawns** when using spawn egg in water
- [ ] **Large turtle model** appears (even with placeholder texture)
- [ ] **Entity swims** and moves in water
- [ ] **High health** (much more than normal mobs)

### ⚠️ Acceptable Issues (Phase 1)
- **Missing/placeholder texture** - Purple/black checkerboard is OK
- **Basic AI only** - Just swimming, no advanced behaviors yet
- **No island on back** - That's Phase 4
- **Simple animations** - Basic leg movement is sufficient

### ❌ Critical Issues
- **Mod fails to load** - Error messages or crashes
- **No spawn egg** - Missing from creative tab
- **Entity doesn't spawn** - Nothing happens when using spawn egg
- **Invisible entity** - Spawn egg works but no visible entity
- **Game crashes** - When spawning or near entity

## 🐛 Troubleshooting

### If Spawn Egg Missing:
- Check console for "Registering Aethelon items" message
- Verify no registration errors in logs

### If Entity Doesn't Spawn:
- Try in deeper water
- Check console for entity registration messages
- Verify no entity creation errors

### If Entity Invisible:
- This indicates model/renderer issues
- Check for client-side registration errors
- Model layer registration problems

### If Game Crashes:
- Check crash logs in run/crash-reports/
- Look for mod-related stack traces

## 📊 Expected Results Summary

**🎯 Phase 1 SUCCESS = Functional large turtle entity that spawns and swims**

Even with placeholder textures and basic behavior, if the entity:
- ✅ Spawns from spawn egg
- ✅ Appears as large turtle model  
- ✅ Swims in water
- ✅ Has high health

**Then Phase 1 is COMPLETE and ready for Phase 2!**

## 🚀 Next Steps After Testing

### If Successful:
- Document any minor issues found
- Proceed to Phase 2 (AI and Behavior)
- Add proper texture in future iteration

### If Issues Found:
- Document specific problems
- Check error logs for solutions
- Fix critical issues before Phase 2

---

**Ready to test! The turtle awaits! 🐢🌊**