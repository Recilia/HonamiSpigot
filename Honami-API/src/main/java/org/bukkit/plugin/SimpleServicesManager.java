package org.bukkit.plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.event.server.ServiceUnregisterEvent;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class SimpleServicesManager implements ServicesManager {

	private final Map<Class<?>, List<RegisteredServiceProvider<?>>> providers = new HashMap<Class<?>, List<RegisteredServiceProvider<?>>>();

	public <T> void register(Class<T> service, T provider, Plugin plugin, ServicePriority priority) {
		RegisteredServiceProvider<T> registeredProvider = null;
		synchronized (providers) {
			List<RegisteredServiceProvider<?>> registered = providers.get(service);
			if (registered == null) {
				registered = new ArrayList<RegisteredServiceProvider<?>>();
				providers.put(service, registered);
			}

			registeredProvider = new RegisteredServiceProvider<T>(service, provider, priority, plugin);

			int position = Collections.binarySearch(registered, registeredProvider);
			if (position < 0) {
				registered.add(-(position + 1), registeredProvider);
			} else {
				registered.add(position, registeredProvider);
			}

		}
		Bukkit.getServer().getPluginManager().callEvent(new ServiceRegisterEvent(registeredProvider));
	}

	public void unregisterAll(Plugin plugin) {
		ArrayList<ServiceUnregisterEvent> unregisteredEvents = new ArrayList<ServiceUnregisterEvent>();
		synchronized (providers) {
			Iterator<Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>>> it = providers.entrySet().iterator();

			try {
				while (it.hasNext()) {
					Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>> entry = it.next();
					Iterator<RegisteredServiceProvider<?>> it2 = entry.getValue().iterator();

					try {

						while (it2.hasNext()) {
							RegisteredServiceProvider<?> registered = it2.next();

							if (registered.getPlugin().equals(plugin)) {
								it2.remove();
								unregisteredEvents.add(new ServiceUnregisterEvent(registered));
							}
						}
					} catch (NoSuchElementException e) { 
					}

					if (entry.getValue().size() == 0) {
						it.remove();
					}
				}
			} catch (NoSuchElementException e) {
			}
		}
		for (ServiceUnregisterEvent event : unregisteredEvents) {
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
	}

	public void unregister(Class<?> service, Object provider) {
		ArrayList<ServiceUnregisterEvent> unregisteredEvents = new ArrayList<ServiceUnregisterEvent>();
		synchronized (providers) {
			Iterator<Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>>> it = providers.entrySet().iterator();

			try {
				while (it.hasNext()) {
					Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>> entry = it.next();

					if (entry.getKey() != service) {
						continue;
					}

					Iterator<RegisteredServiceProvider<?>> it2 = entry.getValue().iterator();

					try {

						while (it2.hasNext()) {
							RegisteredServiceProvider<?> registered = it2.next();

							if (registered.getProvider() == provider) {
								it2.remove();
								unregisteredEvents.add(new ServiceUnregisterEvent(registered));
							}
						}
					} catch (NoSuchElementException e) { 
					}

					if (entry.getValue().size() == 0) {
						it.remove();
					}
				}
			} catch (NoSuchElementException e) {
			}
		}
		for (ServiceUnregisterEvent event : unregisteredEvents) {
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
	}

	public void unregister(Object provider) {
		ArrayList<ServiceUnregisterEvent> unregisteredEvents = new ArrayList<ServiceUnregisterEvent>();
		synchronized (providers) {
			Iterator<Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>>> it = providers.entrySet().iterator();

			try {
				while (it.hasNext()) {
					Map.Entry<Class<?>, List<RegisteredServiceProvider<?>>> entry = it.next();
					Iterator<RegisteredServiceProvider<?>> it2 = entry.getValue().iterator();

					try {

						while (it2.hasNext()) {
							RegisteredServiceProvider<?> registered = it2.next();

							if (registered.getProvider().equals(provider)) {
								it2.remove();
								unregisteredEvents.add(new ServiceUnregisterEvent(registered));
							}
						}
					} catch (NoSuchElementException e) { 
					}

					if (entry.getValue().size() == 0) {
						it.remove();
					}
				}
			} catch (NoSuchElementException e) {
			}
		}
		for (ServiceUnregisterEvent event : unregisteredEvents) {
			Bukkit.getServer().getPluginManager().callEvent(event);
		}
	}

	public <T> T load(Class<T> service) {
		synchronized (providers) {
			List<RegisteredServiceProvider<?>> registered = providers.get(service);

			if (registered == null) {
				return null;
			}

			return service.cast(registered.get(0).getProvider());
		}
	}

	@SuppressWarnings("unchecked")
	public <T> RegisteredServiceProvider<T> getRegistration(Class<T> service) {
		synchronized (providers) {
			List<RegisteredServiceProvider<?>> registered = providers.get(service);

			if (registered == null) {
				return null;
			}

			return (RegisteredServiceProvider<T>) registered.get(0);
		}
	}

	public List<RegisteredServiceProvider<?>> getRegistrations(Plugin plugin) {
		ImmutableList.Builder<RegisteredServiceProvider<?>> ret = ImmutableList.<RegisteredServiceProvider<?>>builder();
		synchronized (providers) {
			for (List<RegisteredServiceProvider<?>> registered : providers.values()) {
				for (RegisteredServiceProvider<?> provider : registered) {
					if (provider.getPlugin().equals(plugin)) {
						ret.add(provider);
					}
				}
			}
		}
		return ret.build();
	}

	@SuppressWarnings("unchecked")
	public <T> List<RegisteredServiceProvider<T>> getRegistrations(Class<T> service) {
		ImmutableList.Builder<RegisteredServiceProvider<T>> ret;
		synchronized (providers) {
			List<RegisteredServiceProvider<?>> registered = providers.get(service);

			if (registered == null) {
				return ImmutableList.<RegisteredServiceProvider<T>>of();
			}

			ret = ImmutableList.<RegisteredServiceProvider<T>>builder();

			for (RegisteredServiceProvider<?> provider : registered) {
				ret.add((RegisteredServiceProvider<T>) provider);
			}

		}
		return ret.build();
	}

	public Set<Class<?>> getKnownServices() {
		synchronized (providers) {
			return ImmutableSet.<Class<?>>copyOf(providers.keySet());
		}
	}

	public <T> boolean isProvidedFor(Class<T> service) {
		synchronized (providers) {
			return providers.containsKey(service);
		}
	}
}
