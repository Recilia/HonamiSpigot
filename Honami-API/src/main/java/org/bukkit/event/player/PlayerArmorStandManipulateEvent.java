package org.bukkit.event.player;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerArmorStandManipulateEvent extends PlayerInteractEntityEvent {

	private static final HandlerList handlers = new HandlerList();

	private final ItemStack playerItem;
	private final ItemStack armorStandItem;
	private final EquipmentSlot slot;

	public PlayerArmorStandManipulateEvent(final Player who, final ArmorStand clickedEntity, final ItemStack playerItem,
			final ItemStack armorStandItem, final EquipmentSlot slot) {
		super(who, clickedEntity);
		this.playerItem = playerItem;
		this.armorStandItem = armorStandItem;
		this.slot = slot;
	}

	public ItemStack getPlayerItem() {
		return this.playerItem;
	}

	public ItemStack getArmorStandItem() {
		return this.armorStandItem;
	}

	public EquipmentSlot getSlot() {
		return this.slot;
	}

	@Override
	public ArmorStand getRightClicked() {
		return (ArmorStand) this.clickedEntity;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
