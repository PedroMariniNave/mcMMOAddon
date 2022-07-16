package com.zpedroo.mcmmoaddon.listeners;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import com.gmail.nossr50.events.skills.alchemy.McMMOPlayerCatalysisEvent;
import com.gmail.nossr50.events.skills.fishing.McMMOPlayerFishingEvent;
import com.gmail.nossr50.events.skills.fishing.McMMOPlayerFishingTreasureEvent;
import com.gmail.nossr50.events.skills.repair.McMMOPlayerRepairCheckEvent;
import com.gmail.nossr50.events.skills.unarmed.McMMOPlayerDisarmEvent;
import com.zpedroo.mcmmoaddon.utils.config.DisabledSkills;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class DisabledSkillsListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onActivate(McMMOPlayerAbilityActivateEvent event) {
        if (DisabledSkills.NAMES.contains(event.getSkill().name())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onXpGain(McMMOPlayerXpGainEvent event) {
        if (DisabledSkills.NAMES.contains(event.getSkill().name())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRepair(McMMOPlayerRepairCheckEvent event) {
        if (DisabledSkills.NAMES.contains(event.getSkill().name())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDisarm(McMMOPlayerDisarmEvent event) {
        if (DisabledSkills.NAMES.contains(event.getSkill().name())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFishing(McMMOPlayerFishingEvent event) {
        if (DisabledSkills.NAMES.contains(event.getSkill().name())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onFishingTreasure(McMMOPlayerFishingTreasureEvent event) {
        if (DisabledSkills.NAMES.contains(event.getSkill().name())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCatalysis(McMMOPlayerCatalysisEvent event) {
        if (DisabledSkills.NAMES.contains(event.getSkill().name())) event.setCancelled(true);
    }
}
