package org.spigotmc;

import java.io.File;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import rein.honami.spigot.Honami;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class RestartCommand extends Command {

	public RestartCommand(String name) {
		super(name);
		this.description = "Restarts the server";
		this.usageMessage = "/restart";
		this.setPermission("bukkit.command.restart");
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (testPermission(sender)) {
			MinecraftServer.getServer().processQueue.add(new Runnable() {
				@Override
				public void run() {
					restart();
				}
			});
		}
		return true;
	}

	public static void restart() {
		restart(new File(SpigotConfig.restartScript));
	}

	public static void restart(final File script) {
		AsyncCatcher.enabled = false; 
		try {
			if (script.isFile()) {
				Honami.LOGGER.info("Attempting to restart with " + SpigotConfig.restartScript);

				WatchdogThread.doStop();

				for (EntityPlayer p : MinecraftServer.getServer().getPlayerList().players) {
					p.playerConnection.disconnect(SpigotConfig.restartMessage);
				}
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
				}
				
				MinecraftServer.getServer().getServerConnection().stopServer();

				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
				}

				try {
					MinecraftServer.getServer().stop();
				} catch (Throwable t) {
				}

				Thread shutdownHook = new Thread() {
					@Override
					public void run() {
						try {
							String os = System.getProperty("os.name").toLowerCase();
							if (os.contains("win")) {
								Runtime.getRuntime().exec("cmd /c start " + script.getPath());
							} else {
								Runtime.getRuntime().exec(new String[] { "sh", script.getPath() });
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};

				shutdownHook.setDaemon(true);
				Runtime.getRuntime().addShutdownHook(shutdownHook);
			} else {
				Honami.LOGGER.info(
						"Startup script '" + SpigotConfig.restartScript + "' does not exist! Stopping server.");
			}
			System.exit(0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
