package org.bukkit;

import java.util.Map;

import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.Potion;

import com.google.common.collect.Maps;

public enum Effect {

	CLICK2(1000, Type.SOUND),

	CLICK1(1001, Type.SOUND),

	BOW_FIRE(1002, Type.SOUND),

	DOOR_TOGGLE(1003, Type.SOUND),

	EXTINGUISH(1004, Type.SOUND),

	RECORD_PLAY(1005, Type.SOUND, Material.class),

	GHAST_SHRIEK(1007, Type.SOUND),

	GHAST_SHOOT(1008, Type.SOUND),

	BLAZE_SHOOT(1009, Type.SOUND),

	ZOMBIE_CHEW_WOODEN_DOOR(1010, Type.SOUND),

	ZOMBIE_CHEW_IRON_DOOR(1011, Type.SOUND),

	ZOMBIE_DESTROY_DOOR(1012, Type.SOUND),

	SMOKE(2000, Type.VISUAL, BlockFace.class),

	STEP_SOUND(2001, Type.SOUND, Material.class),

	POTION_BREAK(2002, Type.VISUAL, Potion.class),

	ENDER_SIGNAL(2003, Type.VISUAL),

	MOBSPAWNER_FLAMES(2004, Type.VISUAL),

	FIREWORKS_SPARK("fireworksSpark", Type.PARTICLE),

	CRIT("crit", Type.PARTICLE),

	MAGIC_CRIT("magicCrit", Type.PARTICLE),

	POTION_SWIRL("mobSpell", Type.PARTICLE),

	POTION_SWIRL_TRANSPARENT("mobSpellAmbient", Type.PARTICLE),

	SPELL("spell", Type.PARTICLE),

	INSTANT_SPELL("instantSpell", Type.PARTICLE),

	WITCH_MAGIC("witchMagic", Type.PARTICLE),

	NOTE("note", Type.PARTICLE),

	PORTAL("portal", Type.PARTICLE),

	FLYING_GLYPH("enchantmenttable", Type.PARTICLE),

	FLAME("flame", Type.PARTICLE),

	LAVA_POP("lava", Type.PARTICLE),

	FOOTSTEP("footstep", Type.PARTICLE),

	SPLASH("splash", Type.PARTICLE),

	PARTICLE_SMOKE("smoke", Type.PARTICLE),

	EXPLOSION_HUGE("hugeexplosion", Type.PARTICLE),

	EXPLOSION_LARGE("largeexplode", Type.PARTICLE),

	EXPLOSION("explode", Type.PARTICLE),

	VOID_FOG("depthsuspend", Type.PARTICLE),

	SMALL_SMOKE("townaura", Type.PARTICLE),

	CLOUD("cloud", Type.PARTICLE),

	COLOURED_DUST("reddust", Type.PARTICLE),

	SNOWBALL_BREAK("snowballpoof", Type.PARTICLE),

	WATERDRIP("dripWater", Type.PARTICLE),

	LAVADRIP("dripLava", Type.PARTICLE),

	SNOW_SHOVEL("snowshovel", Type.PARTICLE),

	SLIME("slime", Type.PARTICLE),

	HEART("heart", Type.PARTICLE),

	VILLAGER_THUNDERCLOUD("angryVillager", Type.PARTICLE),

	HAPPY_VILLAGER("happyVillager", Type.PARTICLE),

	LARGE_SMOKE("largesmoke", Type.PARTICLE),

	ITEM_BREAK("iconcrack", Type.PARTICLE, Material.class),

	TILE_BREAK("blockcrack", Type.PARTICLE, MaterialData.class),

	TILE_DUST("blockdust", Type.PARTICLE, MaterialData.class);

	private final int id;
	private final Type type;
	private final Class<?> data;
	private static final Map<Integer, Effect> BY_ID = Maps.newHashMap();
	private static final Map<String, Effect> BY_NAME = Maps.newHashMap();
	private final String particleName;

	private Effect(int id, Type type) {
		this(id, type, null);
	}

	private Effect(int id, Type type, Class<?> data) {
		this.id = id;
		this.type = type;
		this.data = data;
		particleName = null;
	}

	private Effect(String particleName, Type type, Class<?> data) {
		this.particleName = particleName;
		this.type = type;
		id = 0;
		this.data = data;
	}

	private Effect(String particleName, Type type) {
		this.particleName = particleName;
		this.type = type;
		id = 0;
		this.data = null;
	}

	@Deprecated
	public int getId() {
		return this.id;
	}

	public String getName() {
		return particleName;
	}

	public Type getType() {
		return this.type;
	}

	public Class<?> getData() {
		return this.data;
	}

	@Deprecated
	public static Effect getById(int id) {
		return BY_ID.get(id);
	}

	static {
		for (Effect effect : values()) {
			if (effect.type != Type.PARTICLE) {
				BY_ID.put(effect.id, effect);
			}
		}
	}

	public static Effect getByName(String name) {
		return BY_NAME.get(name);
	}

	static {
		for (Effect effect : values()) {
			if (effect.type == Type.PARTICLE) {
				BY_NAME.put(effect.particleName, effect);
			}
		}
	}

	public enum Type {
		SOUND, VISUAL, PARTICLE
	}
}
