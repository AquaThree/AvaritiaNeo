package net.byAqua3.avaritia.loader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.render.RenderGapingVoid;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class AvaritiaRenderTypes {

	public static final RenderType COSMIC_RENDER_TYPE = RenderType.create(
			ResourceLocation.tryBuild(Avaritia.MODID, "cosmic").toString(), DefaultVertexFormat.BLOCK,
			VertexFormat.Mode.QUADS, 2097152, true, false,
			RenderType.CompositeState.builder()
					.setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicShader))
					.setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
					.setLightmapState(RenderStateShard.LIGHTMAP)
					.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
					.setTextureState(RenderStateShard.BLOCK_SHEET)
					.createCompositeState(true));

	public static final RenderType COSMIC_ARMOR_RENDER_TYPE = RenderType.create(
			ResourceLocation.tryBuild(Avaritia.MODID, "cosmic").toString(), DefaultVertexFormat.NEW_ENTITY,
			VertexFormat.Mode.QUADS, 2097152, true, false,
			RenderType.CompositeState.builder()
					.setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicArmorShader))
					.setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
					.setLightmapState(RenderStateShard.LIGHTMAP)
					.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
					.setWriteMaskState(RenderStateShard.COLOR_WRITE)
					.setCullState(RenderStateShard.NO_CULL)
					.setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
					.setTextureState(RenderStateShard.BLOCK_SHEET)
					.createCompositeState(true));

	public static final RenderType VOID = RenderType.create(
			ResourceLocation.tryBuild(Avaritia.MODID, "void_hemisphere").toString(), DefaultVertexFormat.NEW_ENTITY,
			VertexFormat.Mode.QUADS, 2097152, true, false,
			RenderType.CompositeState.builder()
			        .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
					.setCullState(RenderStateShard.NO_CULL)
					.setTextureState(new RenderStateShard.TextureStateShard(RenderGapingVoid.VOID, false, false))
					.createCompositeState(false));
	
	public static final RenderType Glow(ResourceLocation resourceLocation) {
		return RenderType.create(ResourceLocation.tryBuild(Avaritia.MODID, "glow").toString(),
				DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 2097152, true, false,
				RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
						.setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
						.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
						.setCullState(RenderStateShard.NO_CULL)
						.setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
						.setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
						.createCompositeState(true));
	}

	public static final RenderType WingGlow(ResourceLocation resourceLocation) {
		return RenderType.create(ResourceLocation.tryBuild(Avaritia.MODID, "glow").toString(),
				DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 2097152, true, false,
				RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
						.setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
						.setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
						.setCullState(RenderStateShard.NO_CULL)
						.setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
						.setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
						.createCompositeState(true));
	}
}
