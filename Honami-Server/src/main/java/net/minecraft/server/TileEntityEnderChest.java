package net.minecraft.server;

public class TileEntityEnderChest extends TileEntity { 

	public float a; 
	public float f;
	public int g; 
	private int h;

	public TileEntityEnderChest() {
	}

	public void c() {

		
	}

	@Override
	public boolean c(int i, int j) {
		if (i == 1) {
			this.g = j;
			return true;
		} else {
			return super.c(i, j);
		}
	}

	@Override
	public void y() {
		this.E();
		super.y();
	}

	public void b() {
		++this.g;

		if (this.g > 0 && this.a == 0.0F) {
			this.a = 0.7F;

			double d1 = this.getPosition().getX() + 0.5D;
			double d0 = this.getPosition().getZ() + 0.5D;

			this.world.makeSound(d1, this.getPosition().getY() + 0.5D, d0, "random.chestopen", 0.5F,
					this.world.random.nextFloat() * 0.1F + 0.9F);
		}

		this.world.playBlockAction(this.position, Blocks.ENDER_CHEST, 1, this.g);
	}

	public void d() {
		--this.g;

		if (this.g == 0 && this.a > 0.0F || this.g > 0 && this.a < 1.0F) {
			double d0 = this.getPosition().getX() + 0.5D;
			double d2 = this.getPosition().getZ() + 0.5D;

			this.world.makeSound(d0, this.getPosition().getY() + 0.5D, d2, "random.chestclosed", 0.5F,
					this.world.random.nextFloat() * 0.1F + 0.9F);
			this.a = 0.0F;
		}

		this.world.playBlockAction(this.position, Blocks.ENDER_CHEST, 1, this.g);
	}

	public boolean a(EntityHuman entityhuman) {
		return this.world.getTileEntity(this.position) != this ? false
				: entityhuman.e(this.position.getX() + 0.5D, this.position.getY() + 0.5D,
						this.position.getZ() + 0.5D) <= 64.0D;
	}
}
