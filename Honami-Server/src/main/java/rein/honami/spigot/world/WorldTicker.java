package rein.honami.spigot.world;

import java.util.List;

import rein.honami.spigot.async.ResettableLatch;
import rein.honami.spigot.config.HonamiConfig;

import net.minecraft.server.CrashReport;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.PlayerConnection;
import net.minecraft.server.ReportedException;
import net.minecraft.server.WorldServer;

public class WorldTicker implements Runnable {

	public final WorldServer worldserver;
	private final ResettableLatch latch = new ResettableLatch(HonamiConfig.trackingThreads);
	protected volatile boolean hasTracked = false;
	
	public WorldTicker(WorldServer worldServer) {
		this.worldserver = worldServer;
	}

	@Override
	public void run() {

		CrashReport crashreport;

		try {
			worldserver.timings.doTick.startTiming(); 
			worldserver.doTick();
			worldserver.timings.doTick.stopTiming(); 
		} catch (Throwable throwable) {
			
			try {
				crashreport = CrashReport.a(throwable, "Exception ticking world");
			} catch (Throwable t) {
				throw new RuntimeException("Error generating crash report", t);
			}
			
			worldserver.a(crashreport);
			throw new ReportedException(crashreport);
		}

		try {
			worldserver.timings.tickEntities.startTiming(); 
			worldserver.tickEntities();
			worldserver.timings.tickEntities.stopTiming(); 
		} catch (Throwable throwable1) {
			
			try {
				crashreport = CrashReport.a(throwable1, "Exception ticking world entities");
			} catch (Throwable t) {
				throw new RuntimeException("Error generating crash report", t);
			}
			
			worldserver.a(crashreport);
			throw new ReportedException(crashreport);
		}

        worldserver.timings.tracker.startTiming(); 

		if (MinecraftServer.getServer().getPlayerList().getPlayerCount() != 0) 
		{
			
			List<NetworkManager> disabledFlushes = new java.util.ArrayList<>(
					MinecraftServer.getServer().getPlayerList().getPlayerCount());
			for (EntityPlayer player : MinecraftServer.getServer().getPlayerList().players) {
				PlayerConnection connection = player.playerConnection;
				if (connection != null) {
					connection.networkManager.disableAutomaticFlush();
					disabledFlushes.add(connection.networkManager);
				}
			}
			try {
				worldserver.getTracker().updatePlayers();
			} finally {
				for (NetworkManager networkManager : disabledFlushes) {
					networkManager.enableAutomaticFlush();
				}
			}
			
		}

		worldserver.timings.tracker.stopTiming(); 

		worldserver.explosionDensityCache.clear(); 
	}
	
	public ResettableLatch getLatch() {
		return latch;
	}

}