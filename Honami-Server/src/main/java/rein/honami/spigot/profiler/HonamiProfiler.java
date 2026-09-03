package rein.honami.spigot.profiler;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import rein.honami.spigot.Honami;

public class HonamiProfiler {

    private static final HonamiProfiler INSTANCE = new HonamiProfiler();

    private final AtomicBoolean profiling = new AtomicBoolean(false);
    private final AtomicLong startTime = new AtomicLong(0);
    private final AtomicInteger tickCount = new AtomicInteger(0);
    private final AtomicLong totalTickDuration = new AtomicLong(0);
    private final AtomicLong minTickDuration = new AtomicLong(Long.MAX_VALUE);
    private final AtomicLong maxTickDuration = new AtomicLong(0);
    private final ConcurrentLinkedQueue<Long> tickDurations = new ConcurrentLinkedQueue<Long>();
    private final ConcurrentLinkedQueue<MemorySnapshot> memorySnapshots = new ConcurrentLinkedQueue<MemorySnapshot>();
    private final ConcurrentLinkedQueue<GcSnapshot> gcSnapshots = new ConcurrentLinkedQueue<GcSnapshot>();
    private final ConcurrentLinkedQueue<ThreadSnapshot> threadSnapshots = new ConcurrentLinkedQueue<ThreadSnapshot>();

    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> snapshotTask;
    private ScheduledFuture<?> autoStopTask;
    private int targetDurationSeconds = 30;

    private HonamiProfiler() {
    }

    public static HonamiProfiler getInstance() {
        return INSTANCE;
    }

    public boolean isProfiling() {
        return profiling.get();
    }

    public void startProfiling(int durationSeconds) {
        if (profiling.compareAndSet(false, true)) {
            this.targetDurationSeconds = durationSeconds;

            tickDurations.clear();
            memorySnapshots.clear();
            gcSnapshots.clear();
            threadSnapshots.clear();
            tickCount.set(0);
            totalTickDuration.set(0);
            minTickDuration.set(Long.MAX_VALUE);
            maxTickDuration.set(0);
            startTime.set(System.nanoTime());

            snapshotGcBaseline();

            scheduler = Executors.newSingleThreadScheduledExecutor(new java.util.concurrent.ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "HonamiProfiler-Snapshot");
                    t.setDaemon(true);
                    return t;
                }
            });

            snapshotTask = scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    takeSnapshot();
                }
            }, 1, 1, TimeUnit.SECONDS);

            autoStopTask = scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    if (profiling.get()) {
                        stopProfiling();
                        Honami.LOGGER.info("[HonamiProfiler] Profiling completed after {} seconds.", targetDurationSeconds);
                    }
                }
            }, durationSeconds, TimeUnit.SECONDS);

            Honami.LOGGER.info("[HonamiProfiler] Started profiling for {} seconds.", durationSeconds);
        }
    }

    public void stopProfiling() {
        if (profiling.compareAndSet(true, false)) {
            if (autoStopTask != null) {
                autoStopTask.cancel(false);
            }
            if (snapshotTask != null) {
                snapshotTask.cancel(false);
            }
            takeSnapshot();
            if (scheduler != null) {
                scheduler.shutdownNow();
                scheduler = null;
            }
            Honami.LOGGER.info("[HonamiProfiler] Profiling stopped. {} ticks recorded.", tickCount.get());
        }
    }

    public void onTick(long tickDurationNanos) {
        if (!profiling.get()) {
            return;
        }

        tickDurations.add(Long.valueOf(tickDurationNanos));
        int count = tickCount.incrementAndGet();
        totalTickDuration.addAndGet(tickDurationNanos);

        long currentMin;
        do {
            currentMin = minTickDuration.get();
            if (tickDurationNanos >= currentMin) {
                break;
            }
        } while (!minTickDuration.compareAndSet(currentMin, tickDurationNanos));

        long currentMax;
        do {
            currentMax = maxTickDuration.get();
            if (tickDurationNanos <= currentMax) {
                break;
            }
        } while (!maxTickDuration.compareAndSet(currentMax, tickDurationNanos));
    }

    private void snapshotGcBaseline() {
        for (GarbageCollectorMXBean gcBean : ManagementFactory.getGarbageCollectorMXBeans()) {
            gcSnapshots.add(new GcSnapshot(
                gcBean.getName(),
                gcBean.getCollectionCount(),
                gcBean.getCollectionTime()
            ));
        }
    }

    private void takeSnapshot() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeap = memoryBean.getNonHeapMemoryUsage();
        memorySnapshots.add(new MemorySnapshot(
            heap.getUsed(), heap.getMax(),
            nonHeap.getUsed(), nonHeap.getMax()
        ));

        for (GarbageCollectorMXBean gcBean : ManagementFactory.getGarbageCollectorMXBeans()) {
            gcSnapshots.add(new GcSnapshot(
                gcBean.getName(),
                gcBean.getCollectionCount(),
                gcBean.getCollectionTime()
            ));
        }

        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        threadSnapshots.add(new ThreadSnapshot(
            threadBean.getThreadCount(),
            threadBean.getPeakThreadCount(),
            threadBean.getDaemonThreadCount(),
            runtimeBean.getUptime()
        ));
    }

    public String getResults() {
        if (tickCount.get() == 0) {
            return "No tick data collected during profiling.";
        }

        StringBuilder sb = new StringBuilder();
        String separator = "\u00A77\u00A7m                                                 ";
        String header = "\u00A76\u00A7lHonami Profiler Report";

        sb.append(separator).append("\n");
        sb.append(header).append("\n");
        sb.append(separator).append("\n");

        long elapsedNanos = System.nanoTime() - startTime.get();
        double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
        int ticks = tickCount.get();

        sb.append("\u00A7eDuration: \u00A7a").append(String.format("%.1f", elapsedSeconds)).append("s");
        sb.append("\u00A77 | \u00A7eTicks: \u00A7a").append(ticks).append("\n");

        double avgMs = (totalTickDuration.get() / (double) ticks) / 1_000_000.0;
        double minMs = minTickDuration.get() / 1_000_000.0;
        double maxMs = maxTickDuration.get() / 1_000_000.0;
        double avgTps = Math.min(20.0, 1_000.0 / (avgMs > 0 ? avgMs : 50.0));

        sb.append("\u00A7eAvg MSPT: \u00A7a").append(String.format("%.2f", avgMs)).append("ms");
        sb.append("\u00A77 | \u00A7eMin: \u00A7a").append(String.format("%.2f", minMs)).append("ms");
        sb.append("\u00A77 | \u00A7eMax: \u00A7a").append(String.format("%.2f", maxMs)).append("ms\n");

        sb.append("\u00A7eAvg TPS: ");
        if (avgTps >= 18.0) {
            sb.append("\u00A7a");
        } else if (avgTps >= 15.0) {
            sb.append("\u00A7e");
        } else {
            sb.append("\u00A7c");
        }
        sb.append(String.format("%.1f", avgTps)).append("\n");

        sb.append("\n\u00A76\u00A7lPercentiles:\n");
        List<Long> sorted = new ArrayList<Long>(tickDurations);
        Collections.sort(sorted);

        long p50 = percentile(sorted, 50);
        long p95 = percentile(sorted, 95);
        long p99 = percentile(sorted, 99);

        sb.append("\u00A7e  50th: \u00A7a").append(String.format("%.2f", p50 / 1_000_000.0)).append("ms\n");
        sb.append("\u00A7e  95th: \u00A7a").append(String.format("%.2f", p95 / 1_000_000.0)).append("ms\n");
        sb.append("\u00A7e  99th: \u00A7a").append(String.format("%.2f", p99 / 1_000_000.0)).append("ms\n");

        sb.append("\n\u00A76\u00A7lTick Duration Distribution:\n");
        sb.append(buildHistogram(sorted));

        if (!memorySnapshots.isEmpty()) {
            sb.append("\n\u00A76\u00A7lMemory:\n");
            MemorySnapshot latestMem = null;
            for (MemorySnapshot ms : memorySnapshots) {
                latestMem = ms;
            }
            if (latestMem != null) {
                sb.append("\u00A7e  Heap: \u00A7a").append(formatBytes(latestMem.heapUsed));
                sb.append("\u00A77 / \u00A7a").append(formatBytes(latestMem.heapMax)).append("\n");
                sb.append("\u00A7e  Non-Heap: \u00A7a").append(formatBytes(latestMem.nonHeapUsed));
                sb.append("\u00A77 / \u00A7a").append(formatBytes(latestMem.nonHeapMax)).append("\n");
            }
        }

        if (!gcSnapshots.isEmpty()) {
            sb.append("\n\u00A76\u00A7lGarbage Collection:\n");
            List<GcSnapshot> gcList = new ArrayList<GcSnapshot>(gcSnapshots);
            java.util.Map<String, GcSnapshot> firstSnapshots = new java.util.LinkedHashMap<String, GcSnapshot>();
            java.util.Map<String, GcSnapshot> lastSnapshots = new java.util.LinkedHashMap<String, GcSnapshot>();
            for (GcSnapshot gc : gcList) {
                if (!firstSnapshots.containsKey(gc.name)) {
                    firstSnapshots.put(gc.name, gc);
                }
                lastSnapshots.put(gc.name, gc);
            }
            for (java.util.Map.Entry<String, GcSnapshot> entry : lastSnapshots.entrySet()) {
                String name = entry.getKey();
                GcSnapshot last = entry.getValue();
                GcSnapshot first = firstSnapshots.get(name);
                long countDiff = last.collectionCount - first.collectionCount;
                long timeDiff = last.collectionTime - first.collectionTime;
                sb.append("\u00A7e  ").append(name).append(": ");
                sb.append("\u00A7a").append(countDiff).append(" collections");
                sb.append("\u00A77 | \u00A7a").append(timeDiff).append("ms\n");
            }
        }

        if (!threadSnapshots.isEmpty()) {
            sb.append("\n\u00A76\u00A7lThreads:\n");
            ThreadSnapshot latestThread = null;
            for (ThreadSnapshot ts : threadSnapshots) {
                latestThread = ts;
            }
            if (latestThread != null) {
                sb.append("\u00A7e  Current: \u00A7a").append(latestThread.threadCount).append("\n");
                sb.append("\u00A7e  Peak: \u00A7a").append(latestThread.peakThreadCount).append("\n");
                sb.append("\u00A7e  Daemon: \u00A7a").append(latestThread.daemonThreadCount).append("\n");
            }
        }

        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        sb.append("\n\u00A76\u00A7lServer Uptime: \u00A7a").append(formatUptime(runtime.getUptime())).append("\n");

        sb.append(separator).append("\n");
        return sb.toString();
    }

    private long percentile(List<Long> sorted, int p) {
        if (sorted.isEmpty()) return 0;
        int index = (int) Math.ceil(p / 100.0 * sorted.size()) - 1;
        index = Math.max(0, Math.min(index, sorted.size() - 1));
        return sorted.get(index);
    }

    private String buildHistogram(List<Long> sorted) {
        if (sorted.isEmpty()) return "";

        long[] buckets = new long[12];
        String[] labels = {
            "<1ms", "1-2ms", "2-3ms", "3-4ms", "4-5ms", "5-10ms",
            "10-15ms", "15-20ms", "20-30ms", "30-50ms", "50-100ms", ">100ms"
        };

        for (Long duration : sorted) {
            double ms = duration / 1_000_000.0;
            int bucket;
            if (ms < 1) bucket = 0;
            else if (ms < 2) bucket = 1;
            else if (ms < 3) bucket = 2;
            else if (ms < 4) bucket = 3;
            else if (ms < 5) bucket = 4;
            else if (ms < 10) bucket = 5;
            else if (ms < 15) bucket = 6;
            else if (ms < 20) bucket = 7;
            else if (ms < 30) bucket = 8;
            else if (ms < 50) bucket = 9;
            else if (ms < 100) bucket = 10;
            else bucket = 11;
            buckets[bucket]++;
        }

        int total = sorted.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buckets.length; i++) {
            if (buckets[i] == 0) continue;
            double pct = (buckets[i] / (double) total) * 100.0;
            int barLen = (int) (pct / 2.0);
            if (barLen < 1 && buckets[i] > 0) barLen = 1;

            sb.append("\u00A7e  ");
            sb.append(String.format("%-8s", labels[i]));
            sb.append("\u00A77: ");

            String color;
            if (i <= 4) color = "\u00A7a";
            else if (i <= 7) color = "\u00A7e";
            else color = "\u00A7c";

            sb.append(color);
            for (int j = 0; j < barLen; j++) {
                sb.append('#');
            }
            sb.append("\u00A77 (").append(buckets[i]).append(")");
            sb.append("\n");
        }
        return sb.toString();
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
        return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
    }

    private String formatUptime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        if (days > 0) return days + "d " + (hours % 24) + "h " + (minutes % 60) + "m";
        if (hours > 0) return hours + "h " + (minutes % 60) + "m " + (seconds % 60) + "s";
        if (minutes > 0) return minutes + "m " + (seconds % 60) + "s";
        return seconds + "s";
    }

    private static class MemorySnapshot {
        final long heapUsed;
        final long heapMax;
        final long nonHeapUsed;
        final long nonHeapMax;

        MemorySnapshot(long heapUsed, long heapMax, long nonHeapUsed, long nonHeapMax) {
            this.heapUsed = heapUsed;
            this.heapMax = heapMax;
            this.nonHeapUsed = nonHeapUsed;
            this.nonHeapMax = nonHeapMax;
        }
    }

    private static class GcSnapshot {
        final String name;
        final long collectionCount;
        final long collectionTime;

        GcSnapshot(String name, long collectionCount, long collectionTime) {
            this.name = name;
            this.collectionCount = collectionCount;
            this.collectionTime = collectionTime;
        }
    }

    private static class ThreadSnapshot {
        final int threadCount;
        final int peakThreadCount;
        final int daemonThreadCount;
        final long uptime;

        ThreadSnapshot(int threadCount, int peakThreadCount, int daemonThreadCount, long uptime) {
            this.threadCount = threadCount;
            this.peakThreadCount = peakThreadCount;
            this.daemonThreadCount = daemonThreadCount;
            this.uptime = uptime;
        }
    }
}
