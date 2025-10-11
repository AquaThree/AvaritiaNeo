package net.byAqua3.avaritia.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.singularity.Singularity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ICondition.IContext;

public class AvaritiaSingularities {
	
	private static final AvaritiaSingularities INSTANCE = new AvaritiaSingularities();

	private final List<Singularity> singularities = new ArrayList<>();
	
    public static AvaritiaSingularities getInstance() {
		return INSTANCE;
	}

	public static List<Singularity> loadSingularities(ResourceManager resourceManager) {
		Long lastTime = System.currentTimeMillis();
		List<Singularity> singularities = new ArrayList<>();

		Map<ResourceLocation, List<Resource>> map = resourceManager.listResourceStacks("singularities", resourceLocation -> resourceLocation.getPath().endsWith(".json"));
		for (List<Resource> resources : map.values()) {
			for (Resource resource : resources) {
				try {
					BufferedReader bufferedReader = resource.openAsReader();
					JsonElement jsonElement = JsonParser.parseReader(bufferedReader);
					JsonObject jsonObject = jsonElement.getAsJsonObject();
					if (jsonObject.has(ConditionalOps.DEFAULT_CONDITIONS_KEY)) {
						JsonElement conditions = jsonObject.get(ConditionalOps.DEFAULT_CONDITIONS_KEY);
						DataResult<List<ICondition>> dataResult = ICondition.LIST_CODEC.parse(new Dynamic<>(JsonOps.INSTANCE, conditions));
						
						if (!dataResult.isSuccess()) {
							continue;
						}
						if(dataResult.result().isPresent()) {
							List<ICondition> iconditions = dataResult.result().get();
							boolean skip = false;
							for(ICondition icondition : iconditions) {
								if(!icondition.test(IContext.EMPTY)) {
									skip = true;
								}
							}
							if(skip) {
							   continue;
							}
						}
					}
					String id = jsonObject.get("id").getAsString();
					int color = jsonObject.getAsJsonArray("color").get(0).getAsInt();
					int layerColor = jsonObject.getAsJsonArray("color").get(1).getAsInt();
					Singularity singularity = new Singularity(id, color, layerColor);
					singularities.add(singularity);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Avaritia.LOGGER.info("Finished loading {} singularities json after {}ms", singularities.size(), System.currentTimeMillis() - lastTime);
		return singularities;
	}
	
	public List<Singularity> getSingularities() {
		return this.singularities;
	}
	
	public Singularity getSingularity(String id) {
		for (Singularity singularity : this.singularities) {
			if(singularity.id.equalsIgnoreCase(id)) {
				return singularity;
			}
		}
		return null;
	}}
