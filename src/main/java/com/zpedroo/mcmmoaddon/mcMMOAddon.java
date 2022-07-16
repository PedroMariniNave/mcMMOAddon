package com.zpedroo.mcmmoaddon;

import com.zpedroo.mcmmoaddon.commands.mcMMOItemCmd;
import com.zpedroo.mcmmoaddon.hooks.PlaceholderAPIHook;
import com.zpedroo.mcmmoaddon.listeners.DisabledSkillsListeners;
import com.zpedroo.mcmmoaddon.listeners.McTopTagListener;
import com.zpedroo.mcmmoaddon.listeners.PlayerCommandListener;
import com.zpedroo.mcmmoaddon.listeners.PlayerGeneralListeners;
import com.zpedroo.mcmmoaddon.managers.DataManager;
import com.zpedroo.mcmmoaddon.managers.McTopManager;
import com.zpedroo.mcmmoaddon.tasks.McTopUpdateTask;
import com.zpedroo.mcmmoaddon.utils.FileUtils;
import com.zpedroo.mcmmoaddon.utils.formatter.NumberFormatter;
import com.zpedroo.mcmmoaddon.utils.menu.Menus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import static com.zpedroo.mcmmoaddon.utils.config.Settings.ITEM_COMMAND;
import static com.zpedroo.mcmmoaddon.utils.config.Settings.ITEM_ALIASES;

public class mcMMOAddon extends JavaPlugin {

    private static mcMMOAddon instance;
    public static mcMMOAddon get() { return instance; }

    public void onEnable() {
        instance = this;
        new FileUtils(this);
        new NumberFormatter(getConfig());
        new DataManager();
        new McTopManager();
        new Menus();
        new McTopUpdateTask(this);

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook(this).register();
        }

        registerListeners();
        registerCommand(ITEM_COMMAND, ITEM_ALIASES, new mcMMOItemCmd());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new DisabledSkillsListeners(), this);
        getServer().getPluginManager().registerEvents(new McTopTagListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerCommandListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
    }

    private void registerCommand(String command, List<String> aliases, CommandExecutor executor) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            PluginCommand pluginCmd = constructor.newInstance(command, this);
            pluginCmd.setAliases(aliases);
            pluginCmd.setExecutor(executor);

            Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            commandMap.register(getName().toLowerCase(), pluginCmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}