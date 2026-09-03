package org.bukkit.event.entity;

import java.util.EnumMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.NumberConversions;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;

public class EntityDamageEvent extends EntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private static final DamageModifier[] MODIFIERS = DamageModifier.values();
	private static final Function<? super Double, Double> ZERO = Functions.constant(-0.0);
	private final Map<DamageModifier, Double> modifiers;
	private final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions;
	private final Map<DamageModifier, Double> originals;
	private boolean cancelled;
	private final DamageCause cause;

	@Deprecated
	public EntityDamageEvent(final Entity damagee, final DamageCause cause, final int damage) {
		this(damagee, cause, (double) damage);
	}

	@Deprecated
	public EntityDamageEvent(final Entity damagee, final DamageCause cause, final double damage) {
		this(damagee, cause, new EnumMap<DamageModifier, Double>(ImmutableMap.of(DamageModifier.BASE, damage)),
				new EnumMap<DamageModifier, Function<? super Double, Double>>(
						ImmutableMap.of(DamageModifier.BASE, ZERO)));
	}

	public EntityDamageEvent(final Entity damagee, final DamageCause cause, final Map<DamageModifier, Double> modifiers,
			final Map<DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions) {
		super(damagee);
		Validate.isTrue(modifiers.containsKey(DamageModifier.BASE), "BASE DamageModifier missing");
		Validate.isTrue(!modifiers.containsKey(null), "Cannot have null DamageModifier");
		Validate.noNullElements(modifiers.values(), "Cannot have null modifier values");
		Validate.isTrue(modifiers.keySet().equals(modifierFunctions.keySet()),
				"Must have a modifier function for each DamageModifier");
		Validate.noNullElements(modifierFunctions.values(), "Cannot have null modifier function");
		this.originals = new EnumMap<DamageModifier, Double>(modifiers);
		this.cause = cause;
		this.modifiers = modifiers;
		this.modifierFunctions = modifierFunctions;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	public double getOriginalDamage(DamageModifier type) throws IllegalArgumentException {
		final Double damage = originals.get(type);
		if (damage != null) {
			return damage;
		}
		if (type == null) {
			throw new IllegalArgumentException("Cannot have null DamageModifier");
		}
		return 0;
	}

	public void setDamage(DamageModifier type, double damage)
			throws IllegalArgumentException, UnsupportedOperationException {
		if (!modifiers.containsKey(type)) {
			throw type == null ? new IllegalArgumentException("Cannot have null DamageModifier")
					: new UnsupportedOperationException(type + " is not applicable to " + getEntity());
		}
		modifiers.put(type, damage);
	}

	public double getDamage(DamageModifier type) throws IllegalArgumentException {
		Validate.notNull(type, "Cannot have null DamageModifier");
		final Double damage = modifiers.get(type);
		return damage == null ? 0 : damage;
	}

	public boolean isApplicable(DamageModifier type) throws IllegalArgumentException {
		Validate.notNull(type, "Cannot have null DamageModifier");
		return modifiers.containsKey(type);
	}

	public double getDamage() {
		return getDamage(DamageModifier.BASE);
	}

	public final double getFinalDamage() {
		double damage = 0;
		for (DamageModifier modifier : MODIFIERS) {
			damage += getDamage(modifier);
		}
		return damage;
	}

	@Deprecated
	public int _INVALID_getDamage() {
		return NumberConversions.ceil(getDamage());
	}

	public void setDamage(double damage) {

		double remaining = damage;
		double oldRemaining = getDamage(DamageModifier.BASE);
		for (DamageModifier modifier : MODIFIERS) {
			if (!isApplicable(modifier)) {
				continue;
			}

			Function<? super Double, Double> modifierFunction = modifierFunctions.get(modifier);
			double newVanilla = modifierFunction.apply(remaining);
			double oldVanilla = modifierFunction.apply(oldRemaining);
			double difference = oldVanilla - newVanilla;

			double old = getDamage(modifier);
			if (old > 0) {
				setDamage(modifier, Math.max(0, old - difference));
			} else {
				setDamage(modifier, Math.min(0, old - difference));
			}
			remaining += newVanilla;
			oldRemaining += oldVanilla;
		}

		setDamage(DamageModifier.BASE, damage);
	}

	@Deprecated
	public void _INVALID_setDamage(int damage) {
		setDamage(damage);
	}

	public DamageCause getCause() {
		return cause;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public enum DamageModifier {

		BASE,

		HARD_HAT,

		BLOCKING,

		ARMOR,

		RESISTANCE,

		MAGIC,

		ABSORPTION,;
	}

	public enum DamageCause {

		CONTACT,

		ENTITY_ATTACK,

		PROJECTILE,

		SUFFOCATION,

		FALL,

		FIRE,

		FIRE_TICK,

		MELTING,

		LAVA,

		DROWNING,

		BLOCK_EXPLOSION,

		ENTITY_EXPLOSION,

		VOID,

		LIGHTNING,

		SUICIDE,

		STARVATION,

		POISON,

		MAGIC,

		WITHER,

		FALLING_BLOCK,

		THORNS,

		CUSTOM
	}
}
