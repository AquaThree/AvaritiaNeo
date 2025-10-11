package net.byAqua3.avaritia.loader;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.block.BlockCustomCraftingTable;
import net.byAqua3.avaritia.block.BlockExtremeCraftingTable;
import net.byAqua3.avaritia.block.BlockInfinityChest;
import net.byAqua3.avaritia.block.BlockNeutronCollector;
import net.byAqua3.avaritia.block.BlockNeutroniumCompressor;
import net.byAqua3.avaritia.item.ItemInfinityChest;
import net.byAqua3.avaritia.render.blockentity.RenderInfinityChest;
import net.byAqua3.avaritia.tile.TileExtremeCraftingTable;
import net.byAqua3.avaritia.tile.TileInfinityChest;
import net.byAqua3.avaritia.tile.TileNeutronCollector;
import net.byAqua3.avaritia.tile.TileNeutroniumCompressor;
import net.byAqua3.avaritia.util.WrapperInfinityChest;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.datafix.fixes.References;
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
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AvaritiaBlocks {
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Avaritia.MODID);
	public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Avaritia.MODID);
	
	public static final DeferredHolder<Block, Block> NEUTRONIUM_BLOCK = BLOCKS.register("neutronium_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(50.0F, 2000.0F).sound(SoundType.METAL)));
	public static final DeferredHolder<Block, Block> INFINITY_BLOCK = BLOCKS.register("infinity_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(50.0F, 2000.0F).sound(SoundType.METAL)));
	public static final DeferredHolder<Block, Block> CRYSTAL_MATRIX_BLOCK = BLOCKS.register("crystal_matrix_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(50.0F, 2000.0F).sound(SoundType.METAL)));
	public static final DeferredHolder<Block, Block> COMPRESSED_CRAFTING_TABLE = BLOCKS.register("compressed_crafting_table", () -> new BlockCustomCraftingTable(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.WOOD)));
	public static final DeferredHolder<Block, Block> DOUBLE_COMPRESSED_CRAFTING_TABLE = BLOCKS.register("double_compressed_crafting_table", () -> new BlockCustomCraftingTable(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().strength(20.0F).sound(SoundType.WOOD)));
	public static final DeferredHolder<Block, Block> EXTREME_CRAFTING_TABLE = BLOCKS.register("extreme_crafting_table", () -> new BlockExtremeCraftingTable(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().strength(20.0F, 2000.0F).sound(SoundType.GLASS)));
	public static final DeferredHolder<Block, Block> COMPRESSOR = BLOCKS.register("compressor", () -> new BlockNeutroniumCompressor(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().strength(20.0F).sound(SoundType.GLASS)));
	public static final DeferredHolder<Block, Block> NEUTRON_COLLECTOR = BLOCKS.register("neutron_collector", () -> new BlockNeutronCollector(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).requiresCorrectToolForDrops().strength(20.0F).sound(SoundType.GLASS)));
	public static final DeferredHolder<Block, Block> INFINITY_CHEST = BLOCKS.register("infinity_chest", () -> new BlockInfinityChest(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(50.0F, 2000.0F).sound(SoundType.METAL)));

	public static final DeferredHolder<Item, Item> NEUTRONIUM_BLOCK_ITEM = AvaritiaItems.ITEMS.register("neutronium_block", () -> new BlockItem(NEUTRONIUM_BLOCK.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final DeferredHolder<Item, Item> INFINITY_BLOCK_ITEM = AvaritiaItems.ITEMS.register("infinity_block", () -> new BlockItem(INFINITY_BLOCK.get(), new Item.Properties().rarity(AvaritiaItems.COSMIC_RARITY)));
	public static final DeferredHolder<Item, Item> CRYSTAL_MATRIX_BLOCK_ITEM = AvaritiaItems.ITEMS.register("crystal_matrix_block", () -> new BlockItem(CRYSTAL_MATRIX_BLOCK.get(), new Item.Properties().rarity(Rarity.RARE)));
	public static final DeferredHolder<Item, Item> COMPRESSED_CRAFTING_TABLE_ITEM = AvaritiaItems.ITEMS.register("compressed_crafting_table", () -> new BlockItem(COMPRESSED_CRAFTING_TABLE.get(), new Item.Properties()));
	public static final DeferredHolder<Item, Item> DOUBLE_COMPRESSED_CRAFTING_TABLE_ITEM = AvaritiaItems.ITEMS.register("double_compressed_crafting_table", () -> new BlockItem(DOUBLE_COMPRESSED_CRAFTING_TABLE.get(), new Item.Properties()));
	public static final DeferredHolder<Item, Item> EXTREME_CRAFTING_TABLE_ITEM = AvaritiaItems.ITEMS.register("extreme_crafting_table", () -> new BlockItem(EXTREME_CRAFTING_TABLE.get(), new Item.Properties().rarity(Rarity.RARE)));
	public static final DeferredHolder<Item, Item> COMPRESSOR_ITEM = AvaritiaItems.ITEMS.register("compressor", () -> new BlockItem(COMPRESSOR.get(), new Item.Properties().rarity(Rarity.RARE)));
	public static final DeferredHolder<Item, Item> NEUTRON_COLLECTOR_ITEM = AvaritiaItems.ITEMS.register("neutron_collector", () -> new BlockItem(NEUTRON_COLLECTOR.get(), new Item.Properties().rarity(Rarity.RARE)));
	public static final DeferredHolder<Item, Item> INFINITY_CHEST_ITEM = AvaritiaItems.ITEMS.register("infinity_chest", () -> new ItemInfinityChest(INFINITY_CHEST.get(), new Item.Properties().rarity(AvaritiaItems.COSMIC_RARITY)));
	
	public static final DeferredHolder<BlockEntityType<?>,BlockEntityType<TileExtremeCraftingTable>> EXTREME_CRAFTING_TABLE_TILE = TILES.register("extreme_crafting_table", () -> BlockEntityType.Builder.of(TileExtremeCraftingTable::new, new Block[] { EXTREME_CRAFTING_TABLE.get() }).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "extreme_crafting_table")));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileNeutroniumCompressor>> COMPRESSOR_TILE = TILES.register("compressor", () -> BlockEntityType.Builder.of(TileNeutroniumCompressor::new, new Block[] { COMPRESSOR.get() }).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "compressor")));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileNeutronCollector>> NEUTRON_COLLECTOR_TILE = TILES.register("neutron_collector", () -> BlockEntityType.Builder.of(TileNeutronCollector::new, new Block[] { NEUTRON_COLLECTOR.get() }).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "neutron_collector")));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileInfinityChest>> INFINITY_CHEST_TILE = TILES.register("infinity_chest", () -> BlockEntityType.Builder.of(TileInfinityChest::new, new Block[] { INFINITY_CHEST.get() }).build(Util.fetchChoiceType(References.BLOCK_ENTITY, "infinity_chest")));
	
	public static void registerBlocks(IEventBus modEventBus) {
		BLOCKS.register(modEventBus);
		TILES.register(modEventBus);
		modEventBus.addListener(AvaritiaBlocks::onRegisterCapabilities);
		modEventBus.addListener(AvaritiaBlocks::onRegisterRenderers);
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
			return new WrapperInfinityChest(tile.chest);
		});
	}
	
	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(INFINITY_CHEST_TILE.get(), RenderInfinityChest::new);
	}}
