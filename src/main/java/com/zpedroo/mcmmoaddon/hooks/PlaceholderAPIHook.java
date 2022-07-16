package com.zpedroo.mcmmoaddon.hooks;

import com.zpedroo.mcmmoaddon.managers.McTopManager;
import com.zpedroo.mcmmoaddon.utils.formatter.NumberFormatter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final Plugin plugin;

    public PlaceholderAPIHook(Plugin plugin) {
        this.plugin = plugin;
    }

    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    public @NotNull String getIdentifier() {
        return "mcmmoaddon";
    }

    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public @NotNull String onPlaceholderRequest(Player player, String identifier) {
        switch (identifier.toUpperCase()) {
            case "POWER":
                return McTopManager.getInstance().getTopOnePlayerStat() == null ? "0" :
                        NumberFormatter.getInstance().formatDecimal(McTopManager.getInstance().getTopOnePlayerStat().statVal);
            default:
                return "";
        }
    }
}