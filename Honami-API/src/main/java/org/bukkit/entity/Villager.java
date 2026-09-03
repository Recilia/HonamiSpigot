package org.bukkit.entity;

public interface Villager extends Ageable, NPC {

	public Profession getProfession();

	public void setProfession(Profession profession);

	public enum Profession {
		FARMER(0), LIBRARIAN(1), PRIEST(2), BLACKSMITH(3), BUTCHER(4);

		private static final Profession[] professions = new Profession[Profession.values().length];
		private final int id;

		static {
			for (Profession type : values()) {
				professions[type.getId()] = type;
			}
		}

		private Profession(int id) {
			this.id = id;
		}

		@Deprecated
		public int getId() {
			return id;
		}

		@Deprecated
		public static Profession getProfession(int id) {
			return (id >= professions.length) ? null : professions[id];
		}
	}
}
