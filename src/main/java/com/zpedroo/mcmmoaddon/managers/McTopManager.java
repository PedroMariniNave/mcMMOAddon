package com.zpedroo.mcmmoaddon.managers;

import com.gmail.nossr50.database.DatabaseManagerFactory;
import com.gmail.nossr50.datatypes.database.PlayerStat;
import com.zpedroo.mcmmoaddon.utils.config.Messages;
import com.zpedroo.mcmmoaddon.utils.formatter.NumberFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class McTopManager {

    private static McTopManager instance;
    public static McTopManager getInstance() { return instance; }

    private String mcTopName = getTopOneStatFromDatabase() == null ? null : getTopOneStatFromDatabase().name;

    public McTopManager() {
        instance = this;
    }

    public void checkMcTop() {
        PlayerStat playerStat = getTopOneStatFromDatabase();
        if (playerStat == null) return;

        String playerTopOneName = playerStat.name;
        if (mcTopName != null && !StringUtils.equalsIgnoreCase(mcTopName, playerTopOneName)) {
            for (String msg : Messages.NEW_MCTOP) {
                Bukkit.broadcastMessage(StringUtils.replaceEach(msg, new String[]{
                        "{player}",
                        "{level}"
                }, new String[]{
                        playerTopOneName,
                        NumberFormatter.getInstance().formatDecimal(playerStat.statVal)
                }));
            }

            Player player = Bukkit.getPlayer(playerTopOneName);
            if (player != null) {
                player.getWorld().strikeLightningEffect(player.getLocation());
            }
        }

        this.mcTopName = playerTopOneName;
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