package org.bukkit.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.InventoryView;

public abstract class InventoryInteractEvent extends InventoryEvent implements Cancellable {
	private Result result = Result.DEFAULT;

	public InventoryInteractEvent(InventoryView transaction) {
		super(transaction);
	}

	public HumanEntity getWhoClicked() {
		return getView().getPlayer();
	}

	public void setResult(Result newResult) {
		result = newResult;
	}

	public Result getResult() {
		return result;
	}

	public boolean isCancelled() {
		return getResult() == Result.DENY;
	}

	public void setCancelled(boolean toCancel) {
		setResult(toCancel ? Result.DENY : Result.ALLOW);
	}

}
