package org.bukkit.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class BukkitObjectInputStream extends ObjectInputStream {

	protected BukkitObjectInputStream() throws IOException, SecurityException {
		super();
		super.enableResolveObject(true);
	}

	public BukkitObjectInputStream(InputStream in) throws IOException {
		super(in);
		super.enableResolveObject(true);
	}

	@Override
	protected Object resolveObject(Object obj) throws IOException {
		if (obj instanceof Wrapper) {
			try {
				(obj = ConfigurationSerialization.deserializeObject(((Wrapper<?>) obj).map)).getClass(); 
			} catch (Throwable ex) {
				throw newIOException("Failed to deserialize object", ex);
			}
		}

		return super.resolveObject(obj);
	}

	private static IOException newIOException(String string, Throwable cause) {
		IOException exception = new IOException(string);
		exception.initCause(cause);
		return exception;
	}
}
