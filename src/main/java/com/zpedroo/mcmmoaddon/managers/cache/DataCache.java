package com.zpedroo.mcmmoaddon.managers.cache;

import com.gmail.nossr50.database.DatabaseManagerFactory;
import com.gmail.nossr50.datatypes.database.PlayerStat;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.zpedroo.mcmmoaddon.objects.ExperienceItem;
import com.zpedroo.mcmmoaddon.utils.FileUtils;
import com.zpedroo.mcmmoaddon.utils.builder.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCache {

    private final Map<String, ExperienceItem> experienceItems = getExperienceItemsFromConfig();
    private Map<SkillType, List<PlayerStat>> topSkills = getTopSkillsFromDatabase();
    private final Map<Player, Map<SkillType, Integer>> playerRanks = new HashMap<>(64);

    public Map<String, ExperienceItem> getExperienceItems() {
        return experienceItems;
    }

    public Map<SkillType, List<PlayerStat>> getTopSkills() {
        return topSkills;
    }

    public Map<Player, Map<SkillType, Integer>> getPlayerRanks() {
        return playerRanks;
    }

    private Map<String, ExperienceItem> getExperienceItemsFromConfig() {
        Map<String, ExperienceItem> ret = new HashMap<>(4);

        FileUtils.Files file = FileUtils.Files.CONFIG;
        for (String name : FileUtils.get().getSection(file, "Items")) {
            SkillType skillType = SkillType.getSkill(name.toUpperCase());
            if (skillType == null) continue;

            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Items." + name).build();

            ret.put(name.toUpperCase(), new ExperienceItem(skillType, item));
        }

        return ret;
    }

    private Map<SkillType, List<PlayerStat>> getTopSkillsFromDatabase() {
        Map<SkillType, List<PlayerStat>> ret = new HashMap<>(SkillType.values().length);

        for (SkillType skillType : SkillType.values()) {
            List<PlayerStat> topSkill = DatabaseManagerFactory.getDatabaseManager().readLeaderboard(skillType, 1, 10);

            ret.put(skillType, topSkill);
        }

        return ret;
    }

    public void updateTopCache() {
        this.topSkills = getTopSkillsFromDatabase();
    }
}