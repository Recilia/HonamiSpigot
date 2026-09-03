package org.bukkit.scoreboard;

import java.lang.ref.WeakReference;

public interface ScoreboardManager {

	Scoreboard getMainScoreboard();

	Scoreboard getNewScoreboard();
}
