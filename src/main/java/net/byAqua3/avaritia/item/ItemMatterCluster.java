package net.byAqua3.avaritia.item;

import java.util.List;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.byAqua3.avaritia.component.ClusterContainerContents;
import net.byAqua3.avaritia.loader.AvaritiaDataComponents;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.byAqua3.avaritia.loader.AvaritiaTabs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemMatterCluster extends Item {

	public static final int INTERNAL_INV_SIZE = 512;

	public static final int CAPACITY = 4096;

	public ItemMatterCluster(Properties properties) {
		super(properties.stacksTo(1));
		AvaritiaTabs.BLACK_ITEMS.add(this);
	}

	public static ItemStack makeCluster(List<ItemStack> itemStacks) {
		SimpleContainer clusterInventory = new SimpleContainer(INTERNAL_INV_SIZE);
		int count = 0;
		for (ItemStack itemStack : itemStacks) {
			if (count < CAPACITY) {
				if (clusterInventory.canAddItem(itemStack)) {
					clusterInventory.addItem(itemStack.copy());
					count += itemStack.getCount();
					itemStack.setCount(0);
				}
			}
		}
		if (count > 0) {
			ItemStack cluster = new ItemStack(AvaritiaItems.MATTER_CLUSTER.get());
			cluster.update(AvaritiaDataComponents.CLUSTER_CONTAINER.get(), ClusterContainerContents.EMPTY, clusterContainer -> ClusterContainerContents.fromItems(clusterInventory.getItems()));
			return cluster;
		}
		return ItemStack.EMPTY;
	}

	public static List<ItemStack> getClusterItems(ItemStack cluster) {
		ClusterContainerContents clusterContainer = cluster.getOrDefault(AvaritiaDataComponents.CLUSTER_CONTAINER.get(), ClusterContainerContents.EMPTY);
		return clusterContainer.getItems();
	}

	public static int getClusterCount(List<ItemStack> itemStacks) {
		int itemCount = 0;

		for (ItemStack itemStack : itemStacks) {
			if (!itemStack.isEmpty()) {
				itemCount += itemStack.getCount();
			}
		}
		return itemCount;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		if (!stack.has(AvaritiaDataComponents.CLUSTER_CONTAINER.get())) {
			return;
		}

		List<ItemStack> itemStacks = getClusterItems(stack);

		if (getClusterCount(itemStacks) > 0) {
			tooltip.add(Component.translatable("tooltip.matter_cluster.counter", getClusterCount(itemStacks), CAPACITY));
			tooltip.add(Component.literal(""));
		}

		if (Screen.hasShiftDown()) {
			Object2IntMap<Item> object2IntMap = new Object2IntOpenHashMap<>();
			for (ItemStack itemStack : itemStacks) {
				if (!itemStack.isEmpty()) {
					object2IntMap.put(itemStack.getItem(), itemStack.getCount() + object2IntMap.getOrDefault(itemStack.getItem(), 0));
				}
			}
			object2IntMap.forEach((item, count) -> {
				tooltip.add(Component.literal(item.getDescription().getString() + ChatFormatting.GRAY.toString() + " × " + count));
			});
		} else {
			tooltip.add(Component.translatable("tooltip.matter_cluster.desc").withStyle(ChatFormatting.DARK_GRAY));
			tooltip.add(Component.translatable("tooltip.matter_cluster.desc2").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.DARK_GRAY));
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		List<ItemStack> itemStacks = getClusterItems(stack);

		if (stack.has(AvaritiaDataComponents.CLUSTER_CONTAINER.get()) && !itemStacks.isEmpty()) {
			if (!level.isClientSide()) {
				for (ItemStack itemStack : itemStacks) {
					ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), itemStack);
					itemEntity.setDefaultPickUpDelay();
					level.addFreshEntity(itemEntity);
				}
			}
			player.setItemInHand(hand, ItemStack.EMPTY);
		}
		return InteractionResultHolder.success(stack);
	}}
