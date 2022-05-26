package com.zpedroo.mcmmoaddon.listeners;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.zpedroo.mcmmoaddon.managers.McTopManager;
import com.zpedroo.mcmmoaddon.utils.config.Settings;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class McTopTagListener implements Listener {

    @EventHandler
    public void onChat(ChatMessageEvent event) {
        if (McTopManager.getInstance().getMcTopName() == null || !StringUtils.equalsIgnoreCase(McTopManager.getInstance().getMcTopName(), event.getSender().getName()))
            return;

        event.setTagValue("mctop", Settings.MCTOP_TAG);
    }
}