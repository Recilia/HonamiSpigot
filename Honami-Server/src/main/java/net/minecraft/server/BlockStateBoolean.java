package net.minecraft.server;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

public class BlockStateBoolean extends BlockState<Boolean> {

	private final ImmutableSet<Boolean> a = ImmutableSet.of(Boolean.valueOf(true), Boolean.valueOf(false));

	protected BlockStateBoolean(String s) {
		super(s, Boolean.class);
	}

	@Override
	public int getValueId(Boolean value) {
		return value ? 1 : 0;
	}

	@Override
	public Boolean getByValueId(int id) {
		switch (id) {
		case 0:
			return false;
		case 1:
			return true;
		default:
			throw new IllegalArgumentException("Invalid id: " + id);
		}
	}

	@Override
	public Collection<Boolean> c() {
		return this.a;
	}

	public static BlockStateBoolean of(String s) {
		return new BlockStateBoolean(s);
	}

	@Override
	public String a(Boolean obool) {
		return obool.toString();
	}

	

	
}
