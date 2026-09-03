package org.bukkit.plugin;

import java.util.Collection;
import java.util.List;

public interface ServicesManager {

	public <T> void register(Class<T> service, T provider, Plugin plugin, ServicePriority priority);

	public void unregisterAll(Plugin plugin);

	public void unregister(Class<?> service, Object provider);

	public void unregister(Object provider);

	public <T> T load(Class<T> service);

	public <T> RegisteredServiceProvider<T> getRegistration(Class<T> service);

	public List<RegisteredServiceProvider<?>> getRegistrations(Plugin plugin);

	public <T> Collection<RegisteredServiceProvider<T>> getRegistrations(Class<T> service);

	public Collection<Class<?>> getKnownServices();

	public <T> boolean isProvidedFor(Class<T> service);

}
