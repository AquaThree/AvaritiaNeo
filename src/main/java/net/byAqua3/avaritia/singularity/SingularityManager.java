package net.byAqua3.avaritia.singularity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.byAqua3.avaritia.Avaritia;
import net.byAqua3.avaritia.loader.AvaritiaSingularities;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class SingularityManager extends SimpleJsonResourceReloadListener {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	private Map<ResourceLocation, Singularity> singularities = ImmutableMap.of();

	public SingularityManager() {
		super(GSON, "singularities");
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
		RegistryOps<JsonElement> registryOps = this.makeConditionalOps();
		ImmutableMap.Builder<ResourceLocation, Singularity> builder = ImmutableMap.builder();
		Long lastTime = System.currentTimeMillis();

		for (Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
			ResourceLocation resourcelocation = entry.getKey();
			
			try {
				var decoded = Singularity.CONDITIONAL_CODEC.parse(registryOps, entry.getValue()).getOrThrow(JsonParseException::new);
				decoded.ifPresentOrElse(r -> {
					Singularity singularity = r.carrier();
					builder.put(resourcelocation, singularity);
				}, () -> {
					Avaritia.LOGGER.debug("Skipping loading singularity {} as its conditions were not met", resourcelocation);
				});
			} catch (IllegalArgumentException | JsonParseException jsonParseException) {
				Avaritia.LOGGER.error("Parsing error loading singularity {}", resourcelocation, jsonParseException);
			}
		}
		this.singularities = builder.build().entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));

		AvaritiaSingularities.getInstance().getSingularities().clear();
		AvaritiaSingularities.getInstance().getSingularities().addAll(this.singularities.values());
		Avaritia.LOGGER.info("Loaded {} singularities took {}ms", this.singularities.size(), System.currentTimeMillis() - lastTime);
	}

}
