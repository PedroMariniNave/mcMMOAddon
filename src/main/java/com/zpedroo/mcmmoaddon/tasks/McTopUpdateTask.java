package com.zpedroo.mcmmoaddon.tasks;

import com.zpedroo.mcmmoaddon.managers.DataManager;
import com.zpedroo.mcmmoaddon.managers.McTopManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static com.zpedroo.mcmmoaddon.utils.config.Settings.MCTOP_UPDATE_EVERY;

public class McTopUpdateTask extends BukkitRunnable {

    public McTopUpdateTask(Plugin plugin) {
        this.runTaskTimerAsynchronously(plugin, 20L * MCTOP_UPDATE_EVERY, 20L * MCTOP_UPDATE_EVERY);
    }

    @Override
    public void run() {
        McTopManager.getInstance().checkMcTop();
        DataManager.getInstance().getCache().updateTopCache();
    }
}