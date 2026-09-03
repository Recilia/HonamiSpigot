package rein.honami.spigot.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import rein.honami.spigot.profiler.HonamiProfiler;

import net.minecraft.server.MinecraftServer;

public class ProfilerCommand extends Command {

    private static final String PREFIX = ChatColor.GOLD + "[Profiler] " + ChatColor.RESET;
    private static final String SEPARATOR = ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                                                 ";

    public ProfilerCommand(String name) {
        super(name);
        this.description = "Honami built-in performance profiler";
        this.setPermission("honami.command.profiler");
        this.setUsage("/profiler <start|stop|status>");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return false;

        HonamiProfiler profiler = HonamiProfiler.getInstance();

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start":
                handleStart(sender, args, profiler);
                break;
            case "stop":
                handleStop(sender, profiler);
                break;
            case "status":
                handleStatus(sender, profiler);
                break;
            case "results":
                handleResults(sender, profiler);
                break;
            default:
                sendHelp(sender);
                break;
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(SEPARATOR);
        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Honami Profiler");
        sender.sendMessage(SEPARATOR);
        sender.sendMessage(ChatColor.YELLOW + "/profiler start [seconds]" + ChatColor.GRAY + " - Start profiling (default: 30s)");
        sender.sendMessage(ChatColor.YELLOW + "/profiler stop" + ChatColor.GRAY + " - Stop profiling and show results");
        sender.sendMessage(ChatColor.YELLOW + "/profiler status" + ChatColor.GRAY + " - Show profiling status");
        sender.sendMessage(ChatColor.YELLOW + "/profiler results" + ChatColor.GRAY + " - Show last profiling results");
        sender.sendMessage(SEPARATOR);
    }

    private void handleStart(CommandSender sender, String[] args, HonamiProfiler profiler) {
        if (profiler.isProfiling()) {
            sender.sendMessage(PREFIX + ChatColor.RED + "Profiler is already running! Use /profiler stop to stop it first.");
            return;
        }

        int duration = 30;
        if (args.length >= 2) {
            try {
                duration = Integer.parseInt(args[1]);
                if (duration < 5) {
                    sender.sendMessage(PREFIX + ChatColor.RED + "Minimum duration is 5 seconds.");
                    return;
                }
                if (duration > 600) {
                    sender.sendMessage(PREFIX + ChatColor.RED + "Maximum duration is 600 seconds.");
                    return;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(PREFIX + ChatColor.RED + "Invalid duration: " + args[1]);
                return;
            }
        }

        profiler.startProfiling(duration);
        sender.sendMessage(PREFIX + ChatColor.GREEN + "Profiling started for " + ChatColor.AQUA + duration + ChatColor.GREEN + " seconds.");
        sender.sendMessage(PREFIX + ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/profiler stop" + ChatColor.GRAY + " to stop early.");
    }

    private void handleStop(CommandSender sender, HonamiProfiler profiler) {
        if (!profiler.isProfiling()) {
            sender.sendMessage(PREFIX + ChatColor.RED + "Profiler is not running.");
            return;
        }

        profiler.stopProfiling();
        String results = profiler.getResults();
        sender.sendMessage(SEPARATOR);
        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Profiling Stopped - Results:");
        sender.sendMessage(SEPARATOR);
        for (String line : results.split("\n")) {
            sender.sendMessage(line);
        }
    }

    private void handleStatus(CommandSender sender, HonamiProfiler profiler) {
        sender.sendMessage(SEPARATOR);
        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Profiler Status");
        sender.sendMessage(SEPARATOR);

        if (profiler.isProfiling()) {
            sender.sendMessage(ChatColor.GREEN + "Status: " + ChatColor.AQUA + "RUNNING");
            try {
                double tps = MinecraftServer.getServer().tps1.getAverage();
                ChatColor tpsColor = tps >= 18.0 ? ChatColor.GREEN : tps >= 15.0 ? ChatColor.YELLOW : ChatColor.RED;
                sender.sendMessage(ChatColor.YELLOW + "Current TPS: " + tpsColor + String.format("%.1f", tps));
            } catch (Exception e) {
                sender.sendMessage(ChatColor.YELLOW + "Current TPS: " + ChatColor.RED + "N/A");
            }
        } else {
            sender.sendMessage(ChatColor.GREEN + "Status: " + ChatColor.RED + "STOPPED");
        }
        sender.sendMessage(SEPARATOR);
    }

    private void handleResults(CommandSender sender, HonamiProfiler profiler) {
        if (profiler.isProfiling()) {
            sender.sendMessage(PREFIX + ChatColor.YELLOW + "Profiler is still running. Use " + ChatColor.AQUA + "/profiler stop" + ChatColor.YELLOW + " first.");
            return;
        }

        String results = profiler.getResults();
        sender.sendMessage(SEPARATOR);
        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Last Profiling Results:");
        sender.sendMessage(SEPARATOR);
        for (String line : results.split("\n")) {
            sender.sendMessage(line);
        }
    }
}
