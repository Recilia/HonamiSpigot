package rein.honami.spigot.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AsyncNBTSaver {

	private static final Logger LOGGER = LogManager.getLogger(AsyncNBTSaver.class);
	private static final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
		Thread t = new Thread(r, "Honami NBT Saver");
		t.setDaemon(true);
		return t;
	});

	public static void savePlayerDataAsync(Runnable saveTask) {
		executor.execute(() -> {
			try {
				saveTask.run();
			} catch (Exception e) {
				LOGGER.error("Error during async NBT save", e);
			}
		});
	}

	public static void shutdown() {
		try {
			executor.shutdown();
			if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			executor.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
}
