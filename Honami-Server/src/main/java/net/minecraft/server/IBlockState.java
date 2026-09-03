package net.minecraft.server;

import java.util.Collection;

import rein.honami.spigot.taco.Indexer;

public interface IBlockState<T extends Comparable<T>> {

	String a();

	Collection<T> c();

	Class<T> b();

	String a(T t0);

	@SuppressWarnings("Convert2Lambda") 
	public static final Indexer<IBlockState> INDEXER = new Indexer<IBlockState>() {
		@Override
		public int getId(IBlockState state) {
			return state.getId();
		}
	};

	public default void tryInitId() {
	}

	public int getId();

	public int getValueId(T value);

	public T getByValueId(int id);
	
}
