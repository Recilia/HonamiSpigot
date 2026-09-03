package org.bukkit;

public interface TravelAgent {

	public TravelAgent setSearchRadius(int radius);

	public int getSearchRadius();

	public TravelAgent setCreationRadius(int radius);

	public int getCreationRadius();

	public boolean getCanCreatePortal();

	public void setCanCreatePortal(boolean create);

	public Location findOrCreate(Location location);

	public Location findPortal(Location location);

	public boolean createPortal(Location location);
}
