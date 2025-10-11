package net.byAqua3.avaritia.event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.byAqua3.avaritia.item.ItemInfinitySword;
import net.byAqua3.avaritia.loader.AvaritiaShaders;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class AvaritiaClientEvent {

	public static long lastTime = System.currentTimeMillis();

	private static final ChatFormatting[] rainbow = new ChatFormatting[] { ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.BLUE, ChatFormatting.LIGHT_PURPLE };
	private static final ChatFormatting[] sanic = new ChatFormatting[] { ChatFormatting.BLUE, ChatFormatting.BLUE, ChatFormatting.BLUE, ChatFormatting.BLUE, ChatFormatting.WHITE, ChatFormatting.BLUE, ChatFormatting.WHITE, ChatFormatting.WHITE, ChatFormatting.BLUE, ChatFormatting.WHITE, ChatFormatting.WHITE, ChatFormatting.BLUE, ChatFormatting.RED, ChatFormatting.WHITE, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY, ChatFormatting.GRAY };

	public static String ColorTransformationFormatting(String input, ChatFormatting[] colours, double delay, int step, int posstep) {
		StringBuilder stringBuilder = new StringBuilder(input.length() * 3);

		if (delay <= 0.0D) {
			delay = 0.001D;
		}

		int offset = Mth.floor((System.currentTimeMillis() - lastTime) / delay) % colours.length;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);

			int colorIndex = ((i * posstep) + colours.length - offset) % colours.length;

			stringBuilder.append(colours[colorIndex].toString());
			stringBuilder.append(c);
		}

		return stringBuilder.toString();
	}

	public static Component makeRainbow(String text) {
		return Component.literal(ColorTransformationFormatting(text, rainbow, 80.0D, 1, 1));
	}

	public static Component makeSANIC(String text) {
		return Component.literal(ColorTransformationFormatting(text, sanic, 50.0D, 2, 1));
	}

	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent event) {
		ItemStack itemStack = event.getItemStack();
		Item item = itemStack.getItem();
		if (item instanceof ItemInfinitySword) {
			for (int i = 0; i < event.getToolTip().size(); i++) {
				Component component = event.getToolTip().get(i);
				if (component.contains(Component.translatable("attribute.name.generic.attack_damage"))) {
					Pattern pattern = Pattern.compile("[^0-9]");
					Matcher matcher = pattern.matcher(component.getString());
					String text = component.getString().replace(matcher.replaceAll(""), makeRainbow(Component.translatable("tip.infinity").getString()).getString() + ChatFormatting.DARK_GREEN);
					event.getToolTip().set(i, Component.literal(text));
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void drawScreenPre(ScreenEvent.Render.Pre event) {
		AvaritiaShaders.cosmicInventoryRender = true;
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void drawScreenPost(ScreenEvent.Render.Post event) {
		AvaritiaShaders.cosmicInventoryRender = false;
	}
}
