package com.zpedroo.mcmmoaddon.utils.config;

import com.zpedroo.mcmmoaddon.utils.FileUtils;
import com.zpedroo.mcmmoaddon.utils.color.Colorize;
import org.bukkit.ChatColor;

import java.util.List;

public class Messages {

    public static final String COMMAND_USAGE = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.command-usage"));

    public static final String INVALID_SKILL = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-skill"));

    public static final String INVALID_AMOUNT = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.invalid-amount"));

    public static final String OFFLINE_PLAYER = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.offline-player"));

    public static final String LEVEL_REACHED = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.level-reached"));

    public static final List<String> NEW_MCTOP = Colorize.getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.new-mctop"));
}