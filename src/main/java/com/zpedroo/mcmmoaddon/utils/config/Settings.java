package com.zpedroo.mcmmoaddon.utils.config;

import com.zpedroo.mcmmoaddon.utils.FileUtils;
import com.zpedroo.mcmmoaddon.utils.color.Colorize;

import java.util.List;

public class Settings {

    public static final String MCTOP_COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.commands.mctop.command");

    public static final List<String> MCTOP_ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.commands.mctop.aliases");

    public static final String ITEM_COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.commands.item.command");

    public static final List<String> ITEM_ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.commands.item.aliases");

    public static final String ITEM_PERMISSION = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.commands.item.permission");

    public static final String ITEM_PERMISSION_MESSAGE = Colorize.getColored(
            FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.commands.item.permission-message")
    );

    public static final int ANNOUNCE_LEVEL = FileUtils.get().getInt(FileUtils.Files.CONFIG, "Settings.announce-level");

    public static final String MCTOP_TAG = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.mctop.tag"));

    public static final long MCTOP_UPDATE_EVERY = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.mctop.update-every");
}