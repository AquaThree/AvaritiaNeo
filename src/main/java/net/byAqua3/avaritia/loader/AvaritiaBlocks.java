package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.block.BlockCustomCraftingTable;
import net.byAqua3.avaritia.block.BlockExtremeCraftingTable;
import net.byAqua3.avaritia.block.BlockInfinityChest;
import net.byAqua3.avaritia.block.BlockNeutronCollector;
import net.byAqua3.avaritia.block.BlockNeutroniumCompressor;
import net.byAqua3.avaritia.item.ItemInfinityChest;
import net.byAqua3.avaritia.render.blockentity.RenderInfinityChest;
import net.byAqua3.avaritia.render.blockentity.RenderItemInfinityChest;
import net.byAqua3.avaritia.tile.TileExtremeCraftingTable;
import net.byAqua3.avaritia.tile.TileInfinityChest;
import net.byAqua3.avaritia.tile.TileNeutronCollector;
import net.byAqua3.avaritia.tile.TileNeutroniumCompressor;
import net.byAqua3.avaritia.util.InfinityInvWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AvaritiaBlocks {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Avaritia.MODID);
	public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Avaritia.MODID);
	
	public static final DeferredHolder<Block, Block> NEUTRONIUM_BLOCK = BLOCKS.register("neutronium_block", id -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(50.0F, 2000.0F).sound(SoundType.METAL).setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredHolder<Block, Block> INFINITY_BLOCK = BLOCKS.register("infinity_block", id -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(50.0F, 2000.0F).sound(SoundType.METAL).setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredHolder<Block, Block> CRYSTAL_MATRIX_BLOCK = BLOCKS.register("crystal_matrix_block", id -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(50.0F, 2000.0F).sound(SoundType.METAL).setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredHolder<Block, Block> COMPRESSED_CRAFTING_TABLE = BLOCKS.register("compressed_crafting_table", id -> new BlockCustomCraftingTable(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredHolder<Block, Block> DOUBLE_COMPRESSED_CRAFTING_TABLE = BLOCKS.register("double_compressed_crafting_table", id -> new BlockCustomCraftingTable(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().strength(20.0F).sound(SoundType.WOOD).setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredHolder<Block, Block> EXTREME_CRAFTING_TABLE = BLOCKS.register("extreme_crafting_table", id -> new BlockExtremeCraftingTable(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().strength(20.0F, 2000.0F).sound(SoundType.GLASS).setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredHolder<Block, Block> COMPRESSOR = BLOCKS.register("compressor", id -> new BlockNeutroniumCompressor(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().strength(20.0F).sound(SoundType.GLASS).setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredHolder<Block, Block> NEUTRON_COLLECTOR = BLOCKS.register("neutron_collector", id -> new BlockNeutronCollector(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().strength(20.0F).sound(SoundType.GLASS).setId(ResourceKey.create(Registries.BLOCK, id))));
	public static final DeferredHolder<Block, Block> INFINITY_CHEST = BLOCKS.register("infinity_chest", id -> new BlockInfinityChest(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(50.0F, 2000.0F).sound(SoundType.METAL).setId(ResourceKey.create(Registries.BLOCK, id))));

	public static final DeferredHolder<Item, Item> NEUTRONIUM_BLOCK_ITEM = AvaritiaItems.ITEMS.register("neutronium_block", id -> new BlockItem(NEUTRONIUM_BLOCK.get(), new Item.Properties().rarity(Rarity.UNCOMMON).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_BLOCK_ITEM = AvaritiaItems.ITEMS.register("infinity_block", id -> new BlockItem(INFINITY_BLOCK.get(), new Item.Properties().rarity(AvaritiaItems.COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> CRYSTAL_MATRIX_BLOCK_ITEM = AvaritiaItems.ITEMS.register("crystal_matrix_block", id -> new BlockItem(CRYSTAL_MATRIX_BLOCK.get(), new Item.Properties().rarity(Rarity.RARE).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> COMPRESSED_CRAFTING_TABLE_ITEM = AvaritiaItems.ITEMS.register("compressed_crafting_table", id -> new BlockItem(COMPRESSED_CRAFTING_TABLE.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> DOUBLE_COMPRESSED_CRAFTING_TABLE_ITEM = AvaritiaItems.ITEMS.register("double_compressed_crafting_table", id -> new BlockItem(DOUBLE_COMPRESSED_CRAFTING_TABLE.get(), new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> EXTREME_CRAFTING_TABLE_ITEM = AvaritiaItems.ITEMS.register("extreme_crafting_table", id -> new BlockItem(EXTREME_CRAFTING_TABLE.get(), new Item.Properties().rarity(Rarity.RARE).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> COMPRESSOR_ITEM = AvaritiaItems.ITEMS.register("compressor", id -> new BlockItem(COMPRESSOR.get(), new Item.Properties().rarity(Rarity.RARE).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> NEUTRON_COLLECTOR_ITEM = AvaritiaItems.ITEMS.register("neutron_collector", id -> new BlockItem(NEUTRON_COLLECTOR.get(), new Item.Properties().rarity(Rarity.RARE).setId(ResourceKey.create(Registries.ITEM, id))));
	public static final DeferredHolder<Item, Item> INFINITY_CHEST_ITEM = AvaritiaItems.ITEMS.register("infinity_chest", id -> new ItemInfinityChest(INFINITY_CHEST.get(), new Item.Properties().rarity(AvaritiaItems.COSMIC_RARITY).setId(ResourceKey.create(Registries.ITEM, id))));
	
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileExtremeCraftingTable>> EXTREME_CRAFTING_TABLE_TILE = TILES.register("extreme_crafting_table", () -> new BlockEntityType<>(TileExtremeCraftingTable::new, new Block[] { EXTREME_CRAFTING_TABLE.get() }));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileNeutroniumCompressor>> COMPRESSOR_TILE = TILES.register("compressor", () -> new BlockEntityType<>(TileNeutroniumCompressor::new, new Block[] { COMPRESSOR.get() }));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileNeutronCollector>> NEUTRON_COLLECTOR_TILE = TILES.register("neutron_collector", () -> new BlockEntityType<>(TileNeutronCollector::new, new Block[] { NEUTRON_COLLECTOR.get() }));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileInfinityChest>> INFINITY_CHEST_TILE = TILES.register("infinity_chest", () -> new BlockEntityType<>(TileInfinityChest::new, new Block[] { INFINITY_CHEST.get() }));
	
	public static void registerBlocks(IEventBus modEventBus) {
		BLOCKS.register(modEventBus);
		TILES.register(modEventBus);
		modEventBus.addListener(AvaritiaBlocks::onRegisterCapabilities);
		modEventBus.addListener(AvaritiaBlocks::onRegisterRenderers);
		modEventBus.addListener(AvaritiaBlocks::onRegisterSpecialModelRenderer);
	}
	
	@SubscribeEvent
	public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COMPRESSOR_TILE.get(), (tile, direction) -> {
			return direction == null ?  new InvWrapper(tile) :  new SidedInvWrapper(tile, direction);
		});
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, NEUTRON_COLLECTOR_TILE.get(), (tile, direction) -> {
			return new InvWrapper(tile.result);
		});
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, INFINITY_CHEST_TILE.get(), (tile, direction) -> {
			return new InfinityInvWrapper(tile.chest);
		});
	}
	
	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(INFINITY_CHEST_TILE.get(), RenderInfinityChest::new);
	}
	
	@SubscribeEvent
	public static void onRegisterSpecialModelRenderer(RegisterSpecialModelRendererEvent event) {
	    event.register(ResourceLocation.tryBuild(Avaritia.MODID, "infinity_chest"), RenderItemInfinityChest.Unbaked.MAP_CODEC);
	}

}
