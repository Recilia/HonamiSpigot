package net.minecraft.server;

import java.util.Collection;
import java.util.HashSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class BlockStateInteger extends BlockState<Integer> {

	private final ImmutableSet<Integer> a;
	
	private final int min, max;
	private final int range;

	@Override
	public int getValueId(Integer value) {
		if (value < min) {
			throw new IllegalArgumentException("Too small: " + value);
		} else if (value > max) {
			throw new IllegalArgumentException("Too large: " + value);
		} else {
			return value - min;
		}
	}

	@Override
	public Integer getByValueId(int id) {
		if (id < 0) {
			throw new IllegalArgumentException("Negative id: " + id);
		} else if (id > range) {
			throw new IllegalArgumentException("Id is out of range: " + id);
		} else {
			return id;
		}
	}

	protected BlockStateInteger(String s, int i, int j) {
		super(s, Integer.class);
		
		this.min = i;
		this.max = j;
		this.range = (max - min); 

		if (i < 0) {
			throw new IllegalArgumentException("Min value of " + s + " must be 0 or greater");
		} else if (j <= i) {
			throw new IllegalArgumentException("Max value of " + s + " must be greater than min (" + i + ")");
		} else {
			HashSet hashset = Sets.newHashSet();

			for (int k = i; k <= j; ++k) {
				hashset.add(Integer.valueOf(k));
			}

			this.a = ImmutableSet.copyOf(hashset);
		}
	}

	@Override
	public Collection<Integer> c() {
		return this.a;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		} else if (object != null && this.getClass() == object.getClass()) {
			if (!super.equals(object)) {
				return false;
			} else {
				BlockStateInteger blockstateinteger = (BlockStateInteger) object;

				return this.a.equals(blockstateinteger.a);
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int i = super.hashCode();

		i = 31 * i + this.a.hashCode();
		return i;
	}

	public static BlockStateInteger of(String s, int i, int j) {
		return new BlockStateInteger(s, i, j);
	}

	@Override
	public String a(Integer integer) {
		return integer.toString();
	}

	

	
}
