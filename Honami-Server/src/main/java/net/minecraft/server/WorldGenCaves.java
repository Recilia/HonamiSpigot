package net.minecraft.server;

import java.util.Random;

import rein.honami.spigot.world.CaveConfig;

public class WorldGenCaves extends WorldGenBase {

	private Random a = new Random();

	@Override
	protected void a(World world, int i, int j, int k, int l, ChunkSnapshot chunksnapshot) {
		int frequency = CaveConfig.customCaveGenerationEnabled ? CaveConfig.caveFrequency : 13;
		int minSize = CaveConfig.customCaveGenerationEnabled ? CaveConfig.caveMinSize : 1;
		int maxSize = CaveConfig.customCaveGenerationEnabled ? CaveConfig.caveMaxSize : 5;

		int numCaves = this.a.nextInt(this.a.nextInt(this.a.nextInt(frequency) + 1) + 1);

		for (int n = 0; n < numCaves; ++n) {
			float startX = (float) (i + this.a.nextInt(16));
			float startY = (float) (this.a.nextInt(this.a.nextInt(maxSize * 4) + minSize * 4));
			float startZ = (float) (j + this.a.nextInt(16));

			float direction = this.a.nextFloat() * 3.1415927F * 2.0F;
			float widthScale = (this.a.nextFloat() + 0.5F) * 2.0F;
			float heightScale = this.a.nextFloat() * this.a.nextFloat() * 1.2F;

			this.carve(chunksnapshot, startX, startY, startZ, widthScale, direction, heightScale);
		}
	}

	private void carve(ChunkSnapshot chunksnapshot, float cx, float cy, float cz, float scale, float angle, float vertScale) {
		int radius = (int) (scale * 2.0F + 1.0F);

		for (int dy = 0; dy < 128; ++dy) {
			if (dy < 0 || dy >= 128) continue;

			double yFactor = 1.5D + (double) (MathHelper.sin((float) dy * 3.1415927F / 128.0F) * scale * 0.5F);
			double xzRadius = yFactor * vertScale;

			for (int dx = -radius; dx <= radius; ++dx) {
				for (int dz = -radius; dz <= radius; ++dz) {
					double normX = ((double) dx + 0.5D) / xzRadius;
					double normZ = ((double) dz + 0.5D) / xzRadius;

					int bx = (int) cx + dx;
					int bz = (int) cz + dz;

					if (normX * normX + normZ * normZ < 1.0D && bx >= 0 && bx < 16 && bz >= 0 && bz < 16) {
						IBlockData block = chunksnapshot.a(bx, dy, bz);

						if (block != null && (block.getBlock() == Blocks.STONE || block.getBlock() == Blocks.DIRT
								|| block.getBlock() == Blocks.GRASS)) {
							if (dy < 10) {
								chunksnapshot.a(bx, dy, bz, Blocks.LAVA.getBlockData());
							} else {
								chunksnapshot.a(bx, dy, bz, Blocks.AIR.getBlockData());

								IBlockData above = chunksnapshot.a(bx, dy + 1, bz);
								if (above != null && above.getBlock() == Blocks.DIRT && dy < 70) {
									chunksnapshot.a(bx, dy + 1, bz, Blocks.GRASS.getBlockData());
								}
							}
						}
					}
				}
			}
		}
	}
}
