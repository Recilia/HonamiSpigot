
package org.bukkit.entity;

public interface Ocelot extends Animals, Tameable {

	public Type getCatType();

	public void setCatType(Type type);

	public boolean isSitting();

	public void setSitting(boolean sitting);

	public enum Type {
		WILD_OCELOT(0), BLACK_CAT(1), RED_CAT(2), SIAMESE_CAT(3);

		private static final Type[] types = new Type[Type.values().length];
		private final int id;

		static {
			for (Type type : values()) {
				types[type.getId()] = type;
			}
		}

		private Type(int id) {
			this.id = id;
		}

		@Deprecated
		public int getId() {
			return id;
		}

		@Deprecated
		public static Type getType(int id) {
			return (id >= types.length) ? null : types[id];
		}
	}
}
