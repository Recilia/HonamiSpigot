package org.bukkit.enchantments;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.defaults.EnchantCommand;
import org.bukkit.inventory.ItemStack;

public abstract class Enchantment {

	public static final Enchantment PROTECTION_ENVIRONMENTAL = new EnchantmentWrapper(0);

	public static final Enchantment PROTECTION_FIRE = new EnchantmentWrapper(1);

	public static final Enchantment PROTECTION_FALL = new EnchantmentWrapper(2);

	public static final Enchantment PROTECTION_EXPLOSIONS = new EnchantmentWrapper(3);

	public static final Enchantment PROTECTION_PROJECTILE = new EnchantmentWrapper(4);

	public static final Enchantment OXYGEN = new EnchantmentWrapper(5);

	public static final Enchantment WATER_WORKER = new EnchantmentWrapper(6);

	public static final Enchantment THORNS = new EnchantmentWrapper(7);

	public static final Enchantment DEPTH_STRIDER = new EnchantmentWrapper(8);

	public static final Enchantment DAMAGE_ALL = new EnchantmentWrapper(16);

	public static final Enchantment DAMAGE_UNDEAD = new EnchantmentWrapper(17);

	public static final Enchantment DAMAGE_ARTHROPODS = new EnchantmentWrapper(18);

	public static final Enchantment KNOCKBACK = new EnchantmentWrapper(19);

	public static final Enchantment FIRE_ASPECT = new EnchantmentWrapper(20);

	public static final Enchantment LOOT_BONUS_MOBS = new EnchantmentWrapper(21);

	public static final Enchantment DIG_SPEED = new EnchantmentWrapper(32);

	public static final Enchantment SILK_TOUCH = new EnchantmentWrapper(33);

	public static final Enchantment DURABILITY = new EnchantmentWrapper(34);

	public static final Enchantment LOOT_BONUS_BLOCKS = new EnchantmentWrapper(35);

	public static final Enchantment ARROW_DAMAGE = new EnchantmentWrapper(48);

	public static final Enchantment ARROW_KNOCKBACK = new EnchantmentWrapper(49);

	public static final Enchantment ARROW_FIRE = new EnchantmentWrapper(50);

	public static final Enchantment ARROW_INFINITE = new EnchantmentWrapper(51);

	public static final Enchantment LUCK = new EnchantmentWrapper(61);

	public static final Enchantment LURE = new EnchantmentWrapper(62);

	private static final Map<Integer, Enchantment> byId = new HashMap<Integer, Enchantment>();
	private static final Map<String, Enchantment> byName = new HashMap<String, Enchantment>();
	private static boolean acceptingNew = true;
	private final int id;

	public Enchantment(int id) {
		this.id = id;
	}

	@Deprecated
	public int getId() {
		return id;
	}

	public abstract String getName();

	public abstract int getMaxLevel();

	public abstract int getStartLevel();

	public abstract EnchantmentTarget getItemTarget();

	public abstract boolean conflictsWith(Enchantment other);

	public abstract boolean canEnchantItem(ItemStack item);

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Enchantment)) {
			return false;
		}
		final Enchantment other = (Enchantment) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return "Enchantment[" + id + ", " + getName() + "]";
	}

	public static void registerEnchantment(Enchantment enchantment) {
		if (byId.containsKey(enchantment.id) || byName.containsKey(enchantment.getName())) {
			throw new IllegalArgumentException("Cannot set already-set enchantment");
		} else if (!isAcceptingRegistrations()) {
			throw new IllegalStateException(
					"No longer accepting new enchantments (can only be done by the server implementation)");
		}

		byId.put(enchantment.id, enchantment);
		byName.put(enchantment.getName(), enchantment);
	}

	public static boolean isAcceptingRegistrations() {
		return acceptingNew;
	}

	public static void stopAcceptingRegistrations() {
		acceptingNew = false;
		EnchantCommand.buildEnchantments();
	}

	@Deprecated
	public static Enchantment getById(int id) {
		return byId.get(id);
	}

	public static Enchantment getByName(String name) {
		return byName.get(name);
	}

	public static Enchantment[] values() {
		return byId.values().toArray(new Enchantment[byId.size()]);
	}
}
