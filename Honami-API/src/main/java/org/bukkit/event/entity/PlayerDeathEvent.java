package org.bukkit.event.entity;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerDeathEvent extends EntityDeathEvent {
	private int newExp = 0;
	private String deathMessage = "";
	private int newLevel = 0;
	private int newTotalExp = 0;
	private boolean keepLevel = false;
	private boolean keepInventory = false;

	public PlayerDeathEvent(final Player player, final List<ItemStack> drops, final int droppedExp,
			final String deathMessage) {
		this(player, drops, droppedExp, 0, deathMessage);
	}

	public PlayerDeathEvent(final Player player, final List<ItemStack> drops, final int droppedExp, final int newExp,
			final String deathMessage) {
		this(player, drops, droppedExp, newExp, 0, 0, deathMessage);
	}

	public PlayerDeathEvent(final Player player, final List<ItemStack> drops, final int droppedExp, final int newExp,
			final int newTotalExp, final int newLevel, final String deathMessage) {
		super(player, drops, droppedExp);
		this.newExp = newExp;
		this.newTotalExp = newTotalExp;
		this.newLevel = newLevel;
		this.deathMessage = deathMessage;
	}

	@Override
	public Player getEntity() {
		return (Player) entity;
	}

	public void setDeathMessage(String deathMessage) {
		this.deathMessage = deathMessage;
	}

	public String getDeathMessage() {
		return deathMessage;
	}

	public int getNewExp() {
		return newExp;
	}

	public void setNewExp(int exp) {
		newExp = exp;
	}

	public int getNewLevel() {
		return newLevel;
	}

	public void setNewLevel(int level) {
		newLevel = level;
	}

	public int getNewTotalExp() {
		return newTotalExp;
	}

	public void setNewTotalExp(int totalExp) {
		newTotalExp = totalExp;
	}

	public boolean getKeepLevel() {
		return keepLevel;
	}

	public void setKeepLevel(boolean keepLevel) {
		this.keepLevel = keepLevel;
	}

	public void setKeepInventory(boolean keepInventory) {
		this.keepInventory = keepInventory;
	}

	public boolean getKeepInventory() {
		return keepInventory;
	}
}
