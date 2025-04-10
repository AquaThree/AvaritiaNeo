package net.byAqua3.avaritia.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.mojang.math.Transformation;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.SimpleModelState;

public class AvaritiaRenderUtils {
	
	public static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();

	public static List<BakedQuad> bakeItem(TextureAtlasSprite... sprites) {
		return bakeItem(Transformation.identity(), sprites);
	}

	public static List<BakedQuad> bakeItem(Transformation state, TextureAtlasSprite... sprites) {
		List<BakedQuad> quads = new LinkedList<>();

		for (int i = 0; i < sprites.length; ++i) {
			TextureAtlasSprite sprite = sprites[i];
			List<BlockElement> unbaked = ITEM_MODEL_GENERATOR.processFrames(i, "layer" + i, sprite.contents());

			for (BlockElement element : unbaked) {
				for (Entry<Direction, BlockElementFace> directionBlockElementFaceEntry : element.faces.entrySet()) {
					quads.add(FaceBakery.bakeQuad(element.from, element.to, directionBlockElementFaceEntry.getValue(),
							sprite, directionBlockElementFaceEntry.getKey(), new SimpleModelState(state),
							element.rotation, element.shade, 1));
				}
			}
		}

		return quads;
	}

}
