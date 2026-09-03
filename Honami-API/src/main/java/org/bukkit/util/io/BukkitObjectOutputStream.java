package org.bukkit.util.io;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class BukkitObjectOutputStream extends ObjectOutputStream {

	protected BukkitObjectOutputStream() throws IOException, SecurityException {
		super();
		super.enableReplaceObject(true);
	}

	public BukkitObjectOutputStream(OutputStream out) throws IOException {
		super(out);
		super.enableReplaceObject(true);
	}

	@Override
	protected Object replaceObject(Object obj) throws IOException {
		if (!(obj instanceof Serializable) && (obj instanceof ConfigurationSerializable)) {
			obj = Wrapper.newWrapper((ConfigurationSerializable) obj);
		}

		return super.replaceObject(obj);
	}
}
