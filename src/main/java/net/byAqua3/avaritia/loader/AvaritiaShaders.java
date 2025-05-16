package net.byAqua3.avaritia.loader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;

import net.byAqua3.avaritia.Avaritia;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.client.renderer.ShaderProgram;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT, modid = Avaritia.MODID, bus = EventBusSubscriber.Bus.MOD)
public class AvaritiaShaders {
	
	public static ShaderProgram cosmicShader;
	
	public static ShaderProgram cosmicArmorShader;

	public static boolean cosmicInventoryRender;

	public static float[] COSMIC_UVS = new float[40];
	public static TextureAtlasSprite[] COSMIC_SPRITES = new TextureAtlasSprite[10];

	@SubscribeEvent
	public static void onRegisterShaders(RegisterShadersEvent event) {
		cosmicShader = new ShaderProgram(ResourceLocation.tryBuild(Avaritia.MODID, "core/cosmic"), DefaultVertexFormat.BLOCK, ShaderDefines.EMPTY);
		cosmicArmorShader = new ShaderProgram(ResourceLocation.tryBuild(Avaritia.MODID, "core/cosmic"), DefaultVertexFormat.NEW_ENTITY, ShaderDefines.EMPTY);
	}

	@SubscribeEvent
	public static void onTextureAtlasStitched(TextureAtlasStitchedEvent event) {
		if (event.getAtlas().location().equals(AvaritiaAtlas.BLOCK_ATLAS)) {
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
