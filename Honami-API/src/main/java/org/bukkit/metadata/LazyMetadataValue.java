package org.bukkit.metadata;

import java.lang.ref.SoftReference;
import java.util.concurrent.Callable;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

public class LazyMetadataValue extends MetadataValueAdapter implements MetadataValue {
	private Callable<Object> lazyValue;
	private CacheStrategy cacheStrategy;
	private SoftReference<Object> internalValue;
	private static final Object ACTUALLY_NULL = new Object();

	public LazyMetadataValue(Plugin owningPlugin, Callable<Object> lazyValue) {
		this(owningPlugin, CacheStrategy.CACHE_AFTER_FIRST_EVAL, lazyValue);
	}

	public LazyMetadataValue(Plugin owningPlugin, CacheStrategy cacheStrategy, Callable<Object> lazyValue) {
		super(owningPlugin);
		Validate.notNull(cacheStrategy, "cacheStrategy cannot be null");
		Validate.notNull(lazyValue, "lazyValue cannot be null");
		this.internalValue = new SoftReference<Object>(null);
		this.lazyValue = lazyValue;
		this.cacheStrategy = cacheStrategy;
	}

	protected LazyMetadataValue(Plugin owningPlugin) {
		super(owningPlugin);
	}

	public Object value() {
		eval();
		Object value = internalValue.get();
		if (value == ACTUALLY_NULL) {
			return null;
		}
		return value;
	}

	private synchronized void eval() throws MetadataEvaluationException {
		if (cacheStrategy == CacheStrategy.NEVER_CACHE || internalValue.get() == null) {
			try {
				Object value = lazyValue.call();
				if (value == null) {
					value = ACTUALLY_NULL;
				}
				internalValue = new SoftReference<Object>(value);
			} catch (Exception e) {
				throw new MetadataEvaluationException(e);
			}
		}
	}

	public synchronized void invalidate() {
		if (cacheStrategy != CacheStrategy.CACHE_ETERNALLY) {
			internalValue.clear();
		}
	}

	public enum CacheStrategy {

		CACHE_AFTER_FIRST_EVAL,

		NEVER_CACHE,

		CACHE_ETERNALLY
	}
}
