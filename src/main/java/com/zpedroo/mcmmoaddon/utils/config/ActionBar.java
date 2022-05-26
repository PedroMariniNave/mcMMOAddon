package com.zpedroo.mcmmoaddon.utils.config;

import com.zpedroo.mcmmoaddon.utils.FileUtils;
import org.bukkit.ChatColor;

public class ActionBar {

    public static final String EXP = ChatColor.translateAlternateColorCodes('&',
            FileUtils.get().getString(FileUtils.Files.CONFIG, "Action-Bar.exp")
    );
}