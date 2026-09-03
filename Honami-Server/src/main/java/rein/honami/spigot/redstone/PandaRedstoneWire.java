package rein.honami.spigot.redstone;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.server.BaseBlockPosition;
import net.minecraft.server.Block;
import net.minecraft.server.BlockDiodeAbstract;
import net.minecraft.server.BlockDirectional;
import net.minecraft.server.BlockPiston;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.BlockRedstoneComparator;
import net.minecraft.server.BlockRedstoneTorch;
import net.minecraft.server.BlockRedstoneWire;
import net.minecraft.server.BlockTorch;
import net.minecraft.server.Blocks;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IBlockData;
import net.minecraft.server.World;

public class PandaRedstoneWire extends BlockRedstoneWire {

	
	private final List<BlockPosition> turnOff = Lists.newArrayList();
	
	private final List<BlockPosition> turnOn = Lists.newArrayList();

	private final Set<BlockPosition> updatedRedstoneWire = Sets.newLinkedHashSet();

	private static final EnumDirection[] facingsHorizontal = { EnumDirection.WEST, EnumDirection.EAST,
			EnumDirection.NORTH, EnumDirection.SOUTH };
	private static final EnumDirection[] facingsVertical = { EnumDirection.DOWN, EnumDirection.UP };
	private static final EnumDirection[] facings = ArrayUtils.addAll(facingsVertical, facingsHorizontal);

	private static final BaseBlockPosition[] surroundingBlocksOffset;
	static {
		Set<BaseBlockPosition> set = Sets.newLinkedHashSet();
		for (EnumDirection facing : facings) {
			set.add(ReflectUtil.getOfT(facing, BaseBlockPosition.class));
		}

		for (EnumDirection facing1 : facings) {
			BaseBlockPosition v1 = ReflectUtil.getOfT(facing1, BaseBlockPosition.class);

			for (EnumDirection facing2 : facings) {
				BaseBlockPosition v2 = ReflectUtil.getOfT(facing2, BaseBlockPosition.class);
				set.add(new BlockPosition(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ()));
			}
		}

		set.remove(BlockPosition.ZERO);
		surroundingBlocksOffset = set.toArray(new BaseBlockPosition[0]);
	}

	private boolean canProvidePower = true;

	public PandaRedstoneWire() {
		super();
	}

	private void updateSurroundingRedstone(World worldIn, BlockPosition pos, IBlockData iblockdata) {
		
		calculateCurrentChanges(worldIn, pos, iblockdata);

		Set<BlockPosition> blocksNeedingUpdate = Sets.newLinkedHashSet();

		for (BlockPosition posi : updatedRedstoneWire) {
			addBlocksNeedingUpdate(worldIn, posi, blocksNeedingUpdate);
		}

		
		Iterator<BlockPosition> it = Lists.newLinkedList(updatedRedstoneWire).descendingIterator();
		while (it.hasNext()) {
			addAllSurroundingBlocks(it.next(), blocksNeedingUpdate);
		}
		
		blocksNeedingUpdate.removeAll(updatedRedstoneWire);

		updatedRedstoneWire.clear();

		for (BlockPosition posi : blocksNeedingUpdate) {
			worldIn.d(posi, this);
		}
	}

	protected void calculateCurrentChanges(World worldIn, BlockPosition position, IBlockData state) {
		
		if (state.getBlock() == this) {
			this.turnOff.add(position);
		} else {
			
			checkSurroundingWires(worldIn, position);
		}

		while (!turnOff.isEmpty()) {
			BlockPosition pos = turnOff.remove(0);
			state = worldIn.getType(pos);
			int oldPower = state.get(POWER);
			this.canProvidePower = false;
			int blockPower = worldIn.A(pos);
			this.canProvidePower = true;
			int wirePower = getSurroundingWirePower(worldIn, pos);
			
			--wirePower;
			int newPower = Math.max(blockPower, wirePower);

			if (newPower < oldPower) {
				
				if (blockPower > 0 && !turnOn.contains(pos)) {
					turnOn.add(pos);
				}

				setWireState(worldIn, pos, state, 0);
				
			} else if (newPower > oldPower) {
				
				setWireState(worldIn, pos, state, newPower);
			}

			checkSurroundingWires(worldIn, pos);
		}

		while (!turnOn.isEmpty()) {
			BlockPosition pos = turnOn.remove(0);
			state = worldIn.getType(pos);
			int oldPower = state.get(POWER);
			this.canProvidePower = false;
			int blockPower = worldIn.A(pos);
			this.canProvidePower = true;
			int wirePower = getSurroundingWirePower(worldIn, pos);
			
			wirePower--;
			int newPower = Math.max(blockPower, wirePower);

			if (oldPower != newPower) {
				BlockRedstoneEvent event = new BlockRedstoneEvent(
						worldIn.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ()), oldPower, newPower);
				worldIn.getServer().getPluginManager().callEvent(event);
				newPower = event.getNewCurrent();
			}

			if (newPower > oldPower) {
				setWireState(worldIn, pos, state, newPower);
			}

			checkSurroundingWires(worldIn, pos);
		}

		turnOff.clear();
	}

	protected void addWireToList(World worldIn, BlockPosition pos, int otherPower) {
		IBlockData state = worldIn.getType(pos);
		if (state.getBlock() == this) {
			int power = state.get(POWER);
			
			if (power < otherPower - 1 && !turnOn.contains(pos)) {
				
				turnOn.add(pos);
			}

			if (power > otherPower && !turnOff.contains(pos)) {
				
				turnOff.add(pos);
			}
		}
	}

	protected void checkSurroundingWires(World worldIn, BlockPosition pos) {
		IBlockData state = worldIn.getType(pos);
		int ownPower = 0;
		if (state.getBlock() == Blocks.REDSTONE_WIRE) {
			ownPower = state.get(POWER);
		}
		
		for (EnumDirection facingHorizontal : facingsHorizontal) {
			this.addWireToList(worldIn, pos.shift(facingHorizontal), ownPower);
		}
		for (EnumDirection facingVertical : facingsVertical) {
			BlockPosition offsetPos = pos.shift(facingVertical);
			Block block = worldIn.getType(offsetPos).getBlock();
			boolean solidBlock = block.u();
			for (EnumDirection facingHorizontal : facingsHorizontal) {

				
				
				if (facingVertical == EnumDirection.UP
						&& (!solidBlock ||  block == Blocks.GLOWSTONE)
						|| facingVertical == EnumDirection.DOWN && solidBlock
								&& !worldIn.getType(offsetPos.shift(facingHorizontal)).getBlock().isOccluding()) {
					this.addWireToList(worldIn, offsetPos.shift(facingHorizontal), ownPower);
				}
			}
		}
	}

	private int getSurroundingWirePower(World worldIn, BlockPosition pos) {
		int wirePower = 0;
		for (EnumDirection enumfacing : EnumDirection.EnumDirectionLimit.HORIZONTAL) {
			BlockPosition offsetPos = pos.shift(enumfacing);
			IBlockData iblockdata = worldIn.getType(offsetPos);
			boolean occluding = iblockdata.getBlock().isOccluding();
			
			wirePower = this.getPower(iblockdata, wirePower);

			
			if (occluding && !worldIn.getType(pos.up()).getBlock().isOccluding()) {
				wirePower = this.getPower(worldIn, offsetPos.up(), wirePower);
				
			} else if (!occluding) {
				wirePower = this.getPower(worldIn, offsetPos.down(), wirePower);
			}
		}
		return wirePower;
	}

	private void addBlocksNeedingUpdate(World worldIn, BlockPosition pos, Set<BlockPosition> set) {
		Set<EnumDirection> connectedSides = getSidesToPower(worldIn, pos);
		
		for (EnumDirection facing : facings) {
			BlockPosition offsetPos = pos.shift(facing);
			IBlockData state = worldIn.getType(offsetPos);

			
			boolean flag = connectedSides.contains(facing.opposite()) || facing == EnumDirection.DOWN;
			if (flag || (facing.k().c() && a(state, facing))) {
				if (canBlockBePoweredFromSide(state, facing, true)) {
					set.add(offsetPos);
				}
			}

			if (flag && state.getBlock().isOccluding()) {
				for (EnumDirection facing1 : facings) {
					if (canBlockBePoweredFromSide(worldIn.getType(offsetPos.shift(facing1)), facing1, false)) {
						set.add(offsetPos.shift(facing1));
					}
				}
			}
		}
	}

	private boolean canBlockBePoweredFromSide(IBlockData state, EnumDirection side, boolean isWire) {
		Block block = state.getBlock();
		if (block == Blocks.AIR) {
			return false;
		}
		if (block instanceof BlockPiston && state.get(BlockPiston.FACING) == side.opposite()) {
			return false;
		}
		if (block instanceof BlockDiodeAbstract && state.get(BlockDirectional.FACING) != side.opposite()) {
			return isWire && block instanceof BlockRedstoneComparator
					&& state.get(BlockDirectional.FACING).k() != side.k() && side.k().c();
		}
		return !(state.getBlock() instanceof BlockRedstoneTorch) || (!isWire && state.get(BlockTorch.FACING) == side);
	}

	private Set<EnumDirection> getSidesToPower(World worldIn, BlockPosition pos) {
		Set<EnumDirection> retval = Sets.newHashSet();
		for (EnumDirection facing : facingsHorizontal) {
			if (this.isPowerSourceAt(worldIn, pos, facing)) {
				retval.add(facing);
			}
		}
		if (retval.isEmpty()) {
			return Sets.newHashSet(facingsHorizontal);
		}
		boolean northsouth = retval.contains(EnumDirection.NORTH) || retval.contains(EnumDirection.SOUTH);
		boolean eastwest = retval.contains(EnumDirection.EAST) || retval.contains(EnumDirection.WEST);
		if (northsouth) {
			retval.remove(EnumDirection.EAST);
			retval.remove(EnumDirection.WEST);
		}
		if (eastwest) {
			retval.remove(EnumDirection.NORTH);
			retval.remove(EnumDirection.SOUTH);
		}
		return retval;
	}

	private boolean canSidePower(World worldIn, BlockPosition pos, EnumDirection side) {
		Set<EnumDirection> retval = Sets.newHashSet();
		for (EnumDirection facing : facingsHorizontal) {
			if (this.isPowerSourceAt(worldIn, pos, facing)) {
				retval.add(facing);
			}
		}
		if (retval.isEmpty()) {
			return side != EnumDirection.DOWN && side != EnumDirection.UP;
		}
		boolean northsouth = retval.contains(EnumDirection.NORTH) || retval.contains(EnumDirection.SOUTH);
		boolean eastwest = retval.contains(EnumDirection.EAST) || retval.contains(EnumDirection.WEST);
		if (northsouth) {
			retval.remove(EnumDirection.EAST);
			retval.remove(EnumDirection.WEST);
		}
		if (eastwest) {
			retval.remove(EnumDirection.NORTH);
			retval.remove(EnumDirection.SOUTH);
		}
		return retval.contains(side);
	}

	private void addAllSurroundingBlocks(BlockPosition pos, Set<BlockPosition> set) {
		for (BaseBlockPosition vect : surroundingBlocksOffset) {
			set.add(pos.a(vect));
		}
	}

	private void setWireState(World worldIn, BlockPosition pos, IBlockData state, int power) {
		state = state.set(POWER, power);
		worldIn.setTypeAndData(pos, state, 2);
		updatedRedstoneWire.add(pos);
	}

	@Override
	public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata) {
		this.updateSurroundingRedstone(world, blockposition, world.getType(blockposition));

		for (EnumDirection enumdirection : EnumDirection.values()) {
			world.applyPhysics(blockposition.shift(enumdirection), this);
		}
	}

	@Override
	public void remove(World world, BlockPosition blockposition, IBlockData iblockdata) {
		for (EnumDirection enumdirection : EnumDirection.values()) {
			world.applyPhysics(blockposition.shift(enumdirection), this);
		}

		this.updateSurroundingRedstone(world, blockposition, world.getType(blockposition));
	}

	@Override
	public void doPhysics(World world, BlockPosition blockposition, IBlockData iblockdata, Block block) {
		if (this.canPlace(world, blockposition)) {
			this.updateSurroundingRedstone(world, blockposition, iblockdata);
		} else {
			this.b(world, blockposition, iblockdata, 0);
			world.setAir(blockposition);
		}
	}

	protected final int getPower(IBlockData state, int power) {
		if (state.getBlock() != Blocks.REDSTONE_WIRE) {
			return power;
		}
		int j = state.get(BlockRedstoneWire.POWER);
		return Math.max(j, power);
	}

	@Override
	public int a(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata,
			EnumDirection enumdirection) {
		if (!this.canProvidePower) {
			return 0;
		} else {
			int i = iblockdata.get(BlockRedstoneWire.POWER);
			if (i == 0) { 
				return 0;
			} else if (enumdirection == EnumDirection.UP) {
				return i;
			} else {
				return this.canSidePower((World) iblockaccess, blockposition, enumdirection) ? i : 0;
			}
		}
	}

	private boolean isPowerSourceAt(IBlockAccess iblockaccess, BlockPosition blockposition,
			EnumDirection enumdirection) {
		BlockPosition blockpos = blockposition.shift(enumdirection);
		IBlockData iblockdata = iblockaccess.getType(blockpos);
		Block block = iblockdata.getBlock();
		boolean flag = block.isOccluding();
		boolean flag1 = iblockaccess.getType(blockposition.up()).getBlock().isOccluding();
		return !flag1 && flag && e(iblockaccess, blockpos.up()) || (a(iblockdata, enumdirection)
				|| (block == Blocks.POWERED_REPEATER && iblockdata.get(BlockDirectional.FACING) == enumdirection
						|| !flag && e(iblockaccess, blockpos.down())));
	}

}