package com.zpedroo.mcmmoaddon.listeners;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.datatypes.skills.XPGainReason;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.gmail.nossr50.util.player.UserManager;
import com.zpedroo.mcmmoaddon.managers.DataManager;
import com.zpedroo.mcmmoaddon.objects.ExperienceItem;
import com.zpedroo.mcmmoaddon.utils.config.ActionBar;
import com.zpedroo.mcmmoaddon.utils.config.Messages;
import com.zpedroo.mcmmoaddon.utils.config.Settings;
import com.zpedroo.mcmmoaddon.utils.formatter.NumberFormatter;
import de.ancash.actionbar.ActionBarAPI;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (event.getItem() == null || event.getItem().getType().equals(Material.AIR)) return;

        ItemStack item = event.getItem().clone();
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey("ExperienceAmount")) return;

        event.setCancelled(true);

        ExperienceItem experienceItem = DataManager.getInstance().getExperienceItem(nbt.getString("SkillType"));
        if (experienceItem == null) return;

        int experienceAmount = nbt.getInteger("ExperienceAmount");
        if (experienceAmount <= 0) return;

        Player player = event.getPlayer();

        item.setAmount(1);
        player.getInventory().removeItem(item);
        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 0.5f, 10f);

        SkillType skillType = experienceItem.getSkillType();
        ExperienceAPI.addRawXP(player, skillType.name(), experienceAmount, XPGainReason.UNKNOWN.name());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerUp(McMMOPlayerLevelUpEvent event) {
        if (event.getSkillLevel() % Settings.ANNOUNCE_LEVEL != 0) return;

        Player player = event.getPlayer();
        int skillLevel = event.getSkillLevel();
        String skillName = event.getSkill().getName();

        Bukkit.broadcastMessage(StringUtils.replaceEach(Messages.LEVEL_REACHED, new String[]{
                "{player}",
                "{skill}",
                "{level}"
        }, new String[]{
                player.getName(),
                skillName,
                NumberFormatter.getInstance().formatDecimal(skillLevel)
        }));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGainXp(McMMOPlayerXpGainEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = UserManager.getPlayer(player).getProfile();
        SkillType skillType = event.getSkill();
        String skillName = skillType.getName();
        int skillLevel = profile.getSkillLevel(skillType);
        int actualSkillXp = profile.getSkillXpLevel(skillType);
        int neededSkillXp = profile.getXpToLevel(skillType);
        int xpGained = Math.round(event.getXpGained());

        ActionBarAPI.sendActionBar(player, StringUtils.replaceEach(ActionBar.EXP, new String[]{
                "{xp_gained}",
                "{actual_xp}",
                "{needed_xp}",
                "{skill_level}",
                "{skill_name}"
        }, new String[]{
                NumberFormatter.getInstance().format(xpGained),
                NumberFormatter.getInstance().format(actualSkillXp),
                NumberFormatter.getInstance().format(neededSkillXp),
                NumberFormatter.getInstance().formatDecimal(skillLevel),
                skillName
        }));
    }
}