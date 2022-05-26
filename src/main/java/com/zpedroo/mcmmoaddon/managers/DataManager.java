package com.zpedroo.mcmmoaddon.managers;

import com.gmail.nossr50.database.DatabaseManagerFactory;
import com.gmail.nossr50.datatypes.database.PlayerStat;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.zpedroo.mcmmoaddon.managers.cache.DataCache;
import com.zpedroo.mcmmoaddon.objects.ExperienceItem;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() { return instance; }

    private final DataCache dataCache = new DataCache();

    public DataManager() {
        instance = this;
    }

    public ExperienceItem getExperienceItem(String name) {
        return dataCache.getExperienceItems().get(name.toUpperCase());
    }

    public List<PlayerStat> getTopSkill(SkillType skillType) {
        return dataCache.getTopSkills().get(skillType);
    }

    public Map<SkillType, Integer> getPlayerRanks(Player player) {
        Map<SkillType, Integer> playerRanks = dataCache.getPlayerRanks().get(player);
        if (playerRanks == null) {
            playerRanks = DatabaseManagerFactory.getDatabaseManager().readRank(player.getName());
            dataCache.getPlayerRanks().put(player, playerRanks);
        }

        return playerRanks;
    }

    public DataCache getCache() {
        return dataCache;
    }
}