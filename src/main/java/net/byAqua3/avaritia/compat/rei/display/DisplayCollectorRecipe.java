package net.byAqua3.avaritia.compat.rei.display;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.byAqua3.avaritia.compat.rei.AvaritiaREIPlugin;
import net.byAqua3.avaritia.loader.AvaritiaItems;
import net.minecraft.resources.ResourceLocation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

public class DisplayCollectorRecipe implements Display {
	
	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return AvaritiaREIPlugin.COLLECTOR;
	}

	@Override
	public List<EntryIngredient> getInputEntries() {
		return Collections.singletonList(EntryIngredients.of((AvaritiaItems.NEUTRON_PILE.get())));
	}

	@Override
	public List<EntryIngredient> getOutputEntries() {
		return Collections.singletonList(EntryIngredients.of((AvaritiaItems.NEUTRON_PILE.get())));
	}

	@Override
	public Optional<ResourceLocation> getDisplayLocation() {
		return Optional.of(AvaritiaREIPlugin.COLLECTOR.getIdentifier());
	}

	@Override
	public @Nullable DisplaySerializer<? extends Display> getSerializer() {
		return null;
	}
}