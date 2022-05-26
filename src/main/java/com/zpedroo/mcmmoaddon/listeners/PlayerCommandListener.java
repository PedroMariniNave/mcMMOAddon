package com.zpedroo.mcmmoaddon.listeners;

import com.zpedroo.mcmmoaddon.utils.config.Messages;
import com.zpedroo.mcmmoaddon.utils.config.Settings;
import com.zpedroo.mcmmoaddon.utils.menu.Menus;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class PlayerCommandListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = StringUtils.replace(event.getMessage().split(" ")[0], "/", "");
        if (!StringUtils.equalsIgnoreCase(Settings.MCTOP_COMMAND, command) && !containsInList(command, Settings.MCTOP_ALIASES)) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        String[] args = event.getMessage().split(" ");
        if (args.length > 1) {
            Player target = target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(Messages.OFFLINE_PLAYER);
                return;
            }

            Menus.getInstance().openSkillsMenu(player, target);
            return;
        }

        Menus.getInstance().openMainMenu(player);
    }

    private boolean containsInList(String string, List<String> listToCheck) {
        for (String str : listToCheck) {
            if (StringUtils.equalsIgnoreCase(string, str)) return true;
        }

        return false;
    }
}