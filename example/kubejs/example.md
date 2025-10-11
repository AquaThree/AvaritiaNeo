## KubeJS

### General

```javascript
ServerEvents.recipes((event) => {
    event.recipes.avaritia.extreme_shaped([
    "AAAAAAAAA",
    "AAAAAAAAA",
    "AAAAAAAAA",
    "AAAAAAAAA",
    "AAAABAAAA",
    "AAAAAAAAA",
    "AAAAAAAAA",
    "AAAAAAAAA",
    "AAAAAAAAA"], { A: "minecraft:iron_ingot", B: "minecraft:gold_ingot" }, "minecraft:diamond"); //Add shaped recipe for Extreme Crafting Table
    event.recipes.avaritia.extreme_shapeless(["minecraft:iron_ingot", "minecraft:gold_ingot", "minecraft:diamond"], "minecraft:netherite_ingot"); //Add shapeless recipe for Extreme Crafting Table
    event.recipes.avaritia.compressor(["minecraft:gold_ingot"], "avaritia:gold_singularity", 200); //Add recipe for Neutronium Compressor
    event.remove({ type: "avaritia:extreme_shaped", output: "avaritia:infinity_sword" }); //Remove shaped recipe from Extreme Crafting Table
    event.remove({ type: "avaritia:extreme_shapeless", output: "avaritia:ultimate_stew" }); //Remove shapeless recipe from Extreme Crafting Table
    event.remove({ type: "avaritia:compressor", output: "avaritia:redstone_singularity" }); //Remove recipe from Neutronium Compressor
});
```

### 1.20.1

```javascript
event.recipes.avaritia.compressor(["minecraft:diamond"], Item.of("avaritia:json_singularity", {"singularity_id": "dark_matter_singularity"}), 200); //Add recipe for Neutronium Compressor
```

### 1.21.1

```javascript
event.recipes.avaritia.compressor(["minecraft:diamond"], "avaritia:json_singularity[avaritia:singularity_id='red_matter_singularity']", 200); //Add recipe for Neutronium Compressor
```
