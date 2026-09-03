package org.bukkit.entity;

public interface Skeleton extends Monster {

	public SkeletonType getSkeletonType();

	public void setSkeletonType(SkeletonType type);

	public enum SkeletonType {
		NORMAL(0), WITHER(1);

		private static final SkeletonType[] types = new SkeletonType[SkeletonType.values().length];
		private final int id;

		static {
			for (SkeletonType type : values()) {
				types[type.getId()] = type;
			}
		}

		private SkeletonType(int id) {
			this.id = id;
		}

		@Deprecated
		public int getId() {
			return id;
		}

		@Deprecated
		public static SkeletonType getType(int id) {
			return (id >= types.length) ? null : types[id];
		}
	}
}
