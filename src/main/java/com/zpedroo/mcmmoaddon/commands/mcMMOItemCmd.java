package com.zpedroo.mcmmoaddon.commands;

import com.zpedroo.mcmmoaddon.managers.DataManager;
import com.zpedroo.mcmmoaddon.objects.ExperienceItem;
import com.zpedroo.mcmmoaddon.utils.config.Messages;
import com.zpedroo.mcmmoaddon.utils.config.Settings;
import com.zpedroo.mcmmoaddon.utils.formatter.NumberFormatter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.zpedroo.mcmmoaddon.utils.config.Settings.ITEM_PERMISSION;
import static com.zpedroo.mcmmoaddon.utils.config.Settings.ITEM_PERMISSION_MESSAGE;

public class mcMMOItemCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(ITEM_PERMISSION) && sender instanceof Player) {
            sender.sendMessage(ITEM_PERMISSION_MESSAGE);
            return true;
        }

        if (args.length >= 3) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Messages.OFFLINE_PLAYER);
                return true;
            }

            ExperienceItem experienceItem = DataManager.getInstance().getExperienceItem(args[1]);
            if (experienceItem == null) {
                sender.sendMessage(Messages.INVALID_SKILL);
                return true;
            }

            int experienceAmount = NumberFormatter.getInstance().filter(args[2]);
            if (experienceAmount <= 0) {
                sender.sendMessage(Messages.INVALID_AMOUNT);
                return true;
            }

            ItemStack item = experienceItem.getItem(experienceAmount);
            if (target.getInventory().firstEmpty() != -1) {
                target.getInventory().addItem(item);
            } else {
                target.getWorld().dropItemNaturally(target.getLocation(), item);
            }
            return true;
        }

        sender.sendMessage(StringUtils.replaceEach(Messages.COMMAND_USAGE, new String[]{
                "{cmd}"
        }, new String[]{
                label
        }));
        return false;
    }
}