package net.minecraft.server;

import java.util.Random;

public class BlockSnowBlock extends Block {
	protected BlockSnowBlock() {
		super(Material.SNOW_BLOCK);

		this.a(CreativeModeTab.b);
	}

	@Override
	public Item getDropType(IBlockData var1, Random var2, int var3) {
		return Items.SNOWBALL;
	}

	@Override
	public int a(Random var1) {
		return 4;
	}

	

}
