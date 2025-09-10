package net.byAqua3.avaritia.trigger;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

public class TriggerAvaritiaRoot extends SimpleCriterionTrigger<TriggerAvaritiaRoot.Instance> {

	public static final Codec<TriggerAvaritiaRoot.Instance> CODEC = RecordCodecBuilder
			.create(p_312304_ -> p_312304_
					.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
							.forGetter(TriggerAvaritiaRoot.Instance::player))
					.apply(p_312304_, TriggerAvaritiaRoot.Instance::new));

	@Override
	public Codec<TriggerAvaritiaRoot.Instance> codec() {
		return CODEC;
	}

	public void trigger(ServerPlayer player) {
		this.trigger(player, instance -> true);
	}

	public static class Instance implements SimpleCriterionTrigger.SimpleInstance {
		private Optional<ContextAwarePredicate> player;

		public Instance(Optional<ContextAwarePredicate> player) {
			super();
			this.player = player;
		}

		@Override
		public Optional<ContextAwarePredicate> player() {
			return player;
		}
	}
}
