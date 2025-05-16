package net.byAqua3.avaritia.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class TextComponent {
	
	public static MutableComponent getText(String text){
		return Component.translatable("").append(text);
	}

}
