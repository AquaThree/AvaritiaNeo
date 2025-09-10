package net.byAqua3.avaritia.loader;

import java.io.IOException;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import net.byAqua3.avaritia.Avaritia;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;

public class AvaritiaShaders {

	public static ShaderInstance cosmicShader;
	
	public static Uniform timeUniform;
	public static Uniform yawUniform;
	public static Uniform pitchUniform;
	public static Uniform externalScaleUniform;
	public static Uniform opacityUniform;
	public static Uniform cosmicuvsUniform;

	public static ShaderInstance cosmicArmorShader;
	
	public static Uniform timeArmorUniform;
	public static Uniform yawArmorUniform;
	public static Uniform pitchArmorUniform;
	public static Uniform externalScaleArmorUniform;
	public static Uniform opacityArmorUniform;
	public static Uniform cosmicuvsArmorUniform;

	public static boolean cosmicInventoryRender;

	public static float[] COSMIC_UVS = new float[40];
	public static TextureAtlasSprite[] COSMIC_SPRITES = new TextureAtlasSprite[10];

	public static void registerShaders(IEventBus modEventBus) {
		modEventBus.addListener(AvaritiaShaders::onRegisterShaders);
		modEventBus.addListener(AvaritiaShaders::onTextureAtlasStitched);
	}

	@SubscribeEvent
	public static void onRegisterShaders(RegisterShadersEvent event) {
		try {
			event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.tryBuild(Avaritia.MODID, "cosmic"), DefaultVertexFormat.BLOCK), shader -> {
				cosmicShader = shader;
				timeUniform = cosmicShader.getUniform("time");
				yawUniform = cosmicShader.getUniform("yaw");
				pitchUniform = cosmicShader.getUniform("pitch");
				externalScaleUniform = cosmicShader.getUniform("externalScale");
				opacityUniform = cosmicShader.getUniform("opacity");
				cosmicuvsUniform = cosmicShader.getUniform("cosmicuvs");
				cosmicShader.apply();
			});
			event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.tryBuild(Avaritia.MODID, "cosmic"), DefaultVertexFormat.NEW_ENTITY), shader -> {
				cosmicArmorShader = shader;
				timeArmorUniform = cosmicArmorShader.getUniform("time");
				yawArmorUniform = cosmicArmorShader.getUniform("yaw");
				pitchArmorUniform = cosmicArmorShader.getUniform("pitch");
				externalScaleArmorUniform = cosmicArmorShader.getUniform("externalScale");
				opacityArmorUniform = cosmicArmorShader.getUniform("opacity");
				cosmicuvsArmorUniform = cosmicArmorShader.getUniform("cosmicuvs");
				cosmicArmorShader.apply();
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public static void onTextureAtlasStitched(TextureAtlasStitchedEvent event) {
		if (event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
			for (int i = 0; i < COSMIC_SPRITES.length; i++) {
				COSMIC_SPRITES[i] = event.getAtlas().getSprite(ResourceLocation.tryBuild(Avaritia.MODID, "shader/cosmic_" + i));
				AvaritiaShaders.COSMIC_UVS[i * 4 + 0] = COSMIC_SPRITES[i].getU0();
				AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = COSMIC_SPRITES[i].getV0();
				AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = COSMIC_SPRITES[i].getU1();
				AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = COSMIC_SPRITES[i].getV1();
			}
		}
	}
}
