package org.bukkit.craftbukkit.inventory;

import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import net.minecraft.server.ChatComponentText;
import net.minecraft.server.Container;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.PacketPlayOutOpenWindow;
import net.minecraft.server.Slot;

public class CraftContainer extends Container {
	private final InventoryView view;
	private InventoryType cachedType;
	private String cachedTitle;
	private final int cachedSize;

	public CraftContainer(InventoryView view, int id) {
		this.view = view;
		this.windowId = id;
		
		IInventory top = ((CraftInventory) view.getTopInventory()).getInventory();
		IInventory bottom = ((CraftInventory) view.getBottomInventory()).getInventory();
		cachedType = view.getType();
		cachedTitle = view.getTitle();
		cachedSize = getSize();
		setupSlots(top, bottom);
	}

	public CraftContainer(final Inventory inventory, final HumanEntity player, int id) {
		this(new InventoryView() {
			@Override
			public Inventory getTopInventory() {
				return inventory;
			}

			@Override
			public Inventory getBottomInventory() {
				return player.getInventory();
			}

			@Override
			public HumanEntity getPlayer() {
				return player;
			}

			@Override
			public InventoryType getType() {
				return inventory.getType();
			}
		}, id);
	}

	@Override
	public InventoryView getBukkitView() {
		return view;
	}

	private int getSize() {
		return view.getTopInventory().getSize();
	}

	@Override
	public boolean c(EntityHuman entityhuman) {
		if (cachedType == view.getType() && cachedSize == getSize() && cachedTitle.equals(view.getTitle())) {
			return true;
		}

		
		boolean typeChanged = (cachedType != view.getType());
		cachedType = view.getType();
		cachedTitle = view.getTitle();
		if (view.getPlayer() instanceof CraftPlayer) {
			CraftPlayer player = (CraftPlayer) view.getPlayer();
			String type = getNotchInventoryType(cachedType);
			IInventory top = ((CraftInventory) view.getTopInventory()).getInventory();
			IInventory bottom = ((CraftInventory) view.getBottomInventory()).getInventory();
			this.b.clear();
			this.c.clear();
			if (typeChanged) {
				setupSlots(top, bottom);
			}
			int size = getSize();
			player.getHandle().playerConnection.sendPacket(
					new PacketPlayOutOpenWindow(this.windowId, type, new ChatComponentText(cachedTitle), size));
			player.updateInventory();
		}
		return true;
	}

	public static String getNotchInventoryType(InventoryType type) {
		switch (type) {
		case WORKBENCH:
			return "minecraft:crafting_table";
		case FURNACE:
			return "minecraft:furnace";
		case DISPENSER:
			return "minecraft:dispenser";
		case ENCHANTING:
			return "minecraft:enchanting_table";
		case BREWING:
			return "minecraft:brewing_stand";
		case BEACON:
			return "minecraft:beacon";
		case ANVIL:
			return "minecraft:anvil";
		case HOPPER:
			return "minecraft:hopper";
		default:
			return "minecraft:chest";
		}
	}

	private void setupSlots(IInventory top, IInventory bottom) {
		switch (cachedType) {
		case CREATIVE:
			break; 
		case PLAYER:
		case CHEST:
			setupChest(top, bottom);
			break;
		case DISPENSER:
			setupDispenser(top, bottom);
			break;
		case FURNACE:
			setupFurnace(top, bottom);
			break;
		case CRAFTING: 
		case WORKBENCH:
			setupWorkbench(top, bottom);
			break;
		case ENCHANTING:
			setupEnchanting(top, bottom);
			break;
		case BREWING:
			setupBrewing(top, bottom);
			break;
		case HOPPER:
			setupHopper(top, bottom);
			break;
		}
	}

	private void setupChest(IInventory top, IInventory bottom) {
		int rows = top.getSize() / 9;
		int row;
		int col;
		
		int i = (rows - 4) * 18;
		for (row = 0; row < rows; ++row) {
			for (col = 0; col < 9; ++col) {
				this.a(new Slot(top, col + row * 9, 8 + col * 18, 18 + row * 18));
			}
		}

		for (row = 0; row < 3; ++row) {
			for (col = 0; col < 9; ++col) {
				this.a(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 103 + row * 18 + i));
			}
		}

		for (col = 0; col < 9; ++col) {
			this.a(new Slot(bottom, col, 8 + col * 18, 161 + i));
		}
		
	}

	private void setupWorkbench(IInventory top, IInventory bottom) {
		
		this.a(new Slot(top, 0, 124, 35));

		int row;
		int col;

		for (row = 0; row < 3; ++row) {
			for (col = 0; col < 3; ++col) {
				this.a(new Slot(top, 1 + col + row * 3, 30 + col * 18, 17 + row * 18));
			}
		}

		for (row = 0; row < 3; ++row) {
			for (col = 0; col < 9; ++col) {
				this.a(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
			}
		}

		for (col = 0; col < 9; ++col) {
			this.a(new Slot(bottom, col, 8 + col * 18, 142));
		}
		
	}

	private void setupFurnace(IInventory top, IInventory bottom) {
		
		this.a(new Slot(top, 0, 56, 17));
		this.a(new Slot(top, 1, 56, 53));
		this.a(new Slot(top, 2, 116, 35));

		int row;
		int col;

		for (row = 0; row < 3; ++row) {
			for (col = 0; col < 9; ++col) {
				this.a(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
			}
		}

		for (col = 0; col < 9; ++col) {
			this.a(new Slot(bottom, col, 8 + col * 18, 142));
		}
		
	}

	private void setupDispenser(IInventory top, IInventory bottom) {
		
		int row;
		int col;

		for (row = 0; row < 3; ++row) {
			for (col = 0; col < 3; ++col) {
				this.a(new Slot(top, col + row * 3, 61 + col * 18, 17 + row * 18));
			}
		}

		for (row = 0; row < 3; ++row) {
			for (col = 0; col < 9; ++col) {
				this.a(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
			}
		}

		for (col = 0; col < 9; ++col) {
			this.a(new Slot(bottom, col, 8 + col * 18, 142));
		}
		
	}

	private void setupEnchanting(IInventory top, IInventory bottom) {
		
		this.a((new Slot(top, 0, 25, 47)));

		int row;

		for (row = 0; row < 3; ++row) {
			for (int i1 = 0; i1 < 9; ++i1) {
				this.a(new Slot(bottom, i1 + row * 9 + 9, 8 + i1 * 18, 84 + row * 18));
			}
		}

		for (row = 0; row < 9; ++row) {
			this.a(new Slot(bottom, row, 8 + row * 18, 142));
		}
		
	}

	private void setupBrewing(IInventory top, IInventory bottom) {
		
		this.a(new Slot(top, 0, 56, 46));
		this.a(new Slot(top, 1, 79, 53));
		this.a(new Slot(top, 2, 102, 46));
		this.a(new Slot(top, 3, 79, 17));

		int i;

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.a(new Slot(bottom, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.a(new Slot(bottom, i, 8 + i * 18, 142));
		}
		
	}

	private void setupHopper(IInventory top, IInventory bottom) {
		
		byte b0 = 51;

		int i;

		for (i = 0; i < top.getSize(); ++i) {
			this.a(new Slot(top, i, 44 + i * 18, 20));
		}

		for (i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.a(new Slot(bottom, j + i * 9 + 9, 8 + j * 18, i * 18 + b0));
			}
		}

		for (i = 0; i < 9; ++i) {
			this.a(new Slot(bottom, i, 8 + i * 18, 58 + b0));
		}
		
	}

	@Override
	public boolean a(EntityHuman entity) {
		return true;
	}
}
