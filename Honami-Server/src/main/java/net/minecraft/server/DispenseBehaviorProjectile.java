package net.minecraft.server;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;

public abstract class DispenseBehaviorProjectile extends DispenseBehaviorItem {

	public DispenseBehaviorProjectile() {
	}

	@Override
	public ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
		World world = isourceblock.getWorld();
		IPosition iposition = BlockDispenser.a(isourceblock);
		EnumDirection enumdirection = BlockDispenser.b(isourceblock.f());
		IProjectile iprojectile = this.a(world, iposition);

		

		ItemStack itemstack1 = itemstack.cloneAndSubtract(1);
		org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(),
				isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
		CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

		BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(),
				new org.bukkit.util.Vector((double) enumdirection.getAdjacentX(),
						(double) (enumdirection.getAdjacentY() + 0.1F), (double) enumdirection.getAdjacentZ()));
		if (!BlockDispenser.eventFired) {
			world.getServer().getPluginManager().callEvent(event);
		}

		if (event.isCancelled()) {
			itemstack.count++;
			return itemstack;
		}

		if (!event.getItem().equals(craftItem)) {
			itemstack.count++;
			
			ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
			IDispenseBehavior idispensebehavior = BlockDispenser.REGISTRY.get(eventStack.getItem());
			if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
				idispensebehavior.a(isourceblock, eventStack);
				return itemstack;
			}
		}

		iprojectile.shoot(event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(),
				this.getPower(), this.a());
		((Entity) iprojectile).projectileSource = new org.bukkit.craftbukkit.projectiles.CraftBlockProjectileSource(
				(TileEntityDispenser) isourceblock.getTileEntity());
		
		world.addEntity((Entity) iprojectile);
		
		return itemstack;
	}

	@Override
	protected void a(ISourceBlock isourceblock) {
		isourceblock.getWorld().triggerEffect(1002, isourceblock.getBlockPosition(), 0);
	}

	protected abstract IProjectile a(World world, IPosition iposition);

	protected float a() {
		return 6.0F;
	}

	protected float getPower() {
		return 1.1F;
	}
}
