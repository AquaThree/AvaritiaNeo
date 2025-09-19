## CraftTweaker

### General

```zenscript
net.byAqua3.avaritia.ExtremeRecipe.addShaped("name1", "group1", [
    [<item:minecraft:diamond>, <item:minecraft:diamond>, <item:minecraft:diamond>],
    [<item:minecraft:diamond>, <item:minecraft:diamond>, <item:minecraft:diamond>],
    [<item:minecraft:diamond>, <item:minecraft:diamond>, <item:minecraft:diamond>]], <item:minecraft:diamond_block>); //Add shaped recipe for Extreme Crafting Table
net.byAqua3.avaritia.ExtremeRecipe.addShapeless("name2", "group2", [<item:minecraft:iron_block>, <item:minecraft:gold_block>, <item:minecraft:diamond_block>], <item:minecraft:netherite_block>); //Add shapeless recipe for Extreme Crafting Table
net.byAqua3.avaritia.CompressorRecipe.add("name3", "group3", <item:minecraft:netherite_ingot>, <item:avaritia:netherite_singularity>, 200); //Add recipe for Neutronium Compressor
net.byAqua3.avaritia.CompressorRecipe.add("name4", "group4", <item:minecraft:diamond_block>, <item:avaritia:json_singularity>.withTag({"singularity_id": "red_matter_singularity"}), 200); //Add recipe for Neutronium Compressor
net.byAqua3.avaritia.ExtremeRecipe.remove("name1"); //Remove recipe from Extreme Crafting Table
net.byAqua3.avaritia.ExtremeRecipe.remove(<item:avaritia:cosmic_meatballs>); //Remove recipe from Extreme Crafting Table
net.byAqua3.avaritia.CompressorRecipe.remove(<item:avaritia:diamond_singularity>); //Remove recipe from Neutronium Compressor
```

### 1.20.1

```zenscript
net.byAqua3.avaritia.CompressorRecipe.add("name4", "group4", <item:minecraft:diamond_block>, <item:avaritia:json_singularity>.withTag({"singularity_id": "red_matter_singularity"}), 200); //Add recipe for Neutronium Compressor
```

### 1.21.1

```zenscript
net.byAqua3.avaritia.CompressorRecipe.add("name4", "group4", <item:minecraft:diamond_block>, <item:avaritia:json_singularity>.withJsonComponent(<componenttype:avaritia:singularity_id>, "red_matter_singularity"), 200); //Add recipe for Neutronium Compressor
```
