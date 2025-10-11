package net.byAqua3.avaritia.loader;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;

public class AvaritiaConfigs {
	
	public static ModConfigSpec modConfigSpec;
	
	public static ModConfigSpec.IntValue dropChange;
	public static ModConfigSpec.BooleanValue nightVision;
	public static ModConfigSpec.BooleanValue clearBadEffect;
	public static ModConfigSpec.BooleanValue clearFire;
	public static ModConfigSpec.BooleanValue highJump;
	public static ModConfigSpec.BooleanValue stepHeight;
	public static ModConfigSpec.BooleanValue speed;
	public static ModConfigSpec.DoubleValue speedValue;
	public static ModConfigSpec.IntValue maxStackSize;
	
	public static void registerConfigs() {
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		builder.comment("General settings").push("general");
		builder.push("skullfireSword");
		dropChange = builder.comment("DropChange").defineInRange("dropChange", 100, 0, 100);
		builder.pop();
		builder.push("infinityArmor");
		nightVision = builder.comment("NightVision").define("nightVision", true);
		clearBadEffect = builder.comment("ClearBadEffect").define("clearBadEffect", true);
		clearFire = builder.comment("ClearFire").define("clearFire", true);
		highJump = builder.comment("HighJump").define("highJump", true);
		stepHeight = builder.comment("StepHeight").define("stepHeight", true);
		speed = builder.comment("Speed").define("speed", true);
		speedValue = builder.comment("SpeedValue").defineInRange("speedValue", 1.0D, 0.1D, 2.0D);
		builder.pop();
		builder.push("infinityChest");
		maxStackSize = builder.comment("MaxStackSize").defineInRange("maxStackSize", Integer.MAX_VALUE, 64, Integer.MAX_VALUE);
		builder.pop();
		builder.pop();
		modConfigSpec = builder.build();
		ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.SERVER, modConfigSpec);
	}
	
	public static void registerConfigScreen() {
		ModLoadingContext.get().getActiveContainer().registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}}
