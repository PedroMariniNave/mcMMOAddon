package com.zpedroo.mcmmoaddon.managers;

import com.gmail.nossr50.database.DatabaseManagerFactory;
import com.gmail.nossr50.datatypes.database.PlayerStat;
import com.zpedroo.mcmmoaddon.utils.config.Messages;
import com.zpedroo.mcmmoaddon.utils.formatter.NumberFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class McTopManager {

    private static McTopManager instance;
    public static McTopManager getInstance() { return instance; }

    private PlayerStat topOnePlayerStat = getTopOneStatFromDatabase();
    private String mcTopName = topOnePlayerStat == null ? null : topOnePlayerStat.name;

    public McTopManager() {
        instance = this;
    }

    public void checkMcTop() {
        this.topOnePlayerStat = getTopOneStatFromDatabase();
        if (topOnePlayerStat == null) return;

        String playerTopOneName = topOnePlayerStat.name;
        if (mcTopName != null && !StringUtils.equalsIgnoreCase(mcTopName, playerTopOneName)) {
            for (String msg : Messages.NEW_MCTOP) {
                Bukkit.broadcastMessage(StringUtils.replaceEach(msg, new String[]{
                        "{player}",
                        "{level}"
                }, new String[]{
                        playerTopOneName,
                        NumberFormatter.getInstance().formatDecimal(topOnePlayerStat.statVal)
                }));
            }

            Player player = Bukkit.getPlayer(playerTopOneName);
            // double sound = extra thunder effect
            if (player.isOnline()) {
                player.getPlayer().getWorld().strikeLightningEffect(player.getPlayer().getLocation());
                player.getPlayer().getWorld().strikeLightningEffect(player.getPlayer().getLocation());
            } else {
                Bukkit.getOnlinePlayers().forEach(players -> players.playSound(players.getLocation(), Sound.AMBIENCE_THUNDER, 1f, 1f));
                Bukkit.getOnlinePlayers().forEach(players -> players.playSound(players.getLocation(), Sound.AMBIENCE_THUNDER, 1f, 1f));
            }
        }

        this.mcTopName = playerTopOneName;
    }

    public PlayerStat getTopOnePlayerStat() {
        return topOnePlayerStat;
    }

    @Nullable
    private PlayerStat getTopOneStatFromDatabase() {
        List<PlayerStat> tops = DatabaseManagerFactory.getDatabaseManager().readLeaderboard(null, 1, 1);
        if (tops.isEmpty()) return null;

        return tops.get(0);
    }

    public String getMcTopName() {
        return mcTopName;
    }
}