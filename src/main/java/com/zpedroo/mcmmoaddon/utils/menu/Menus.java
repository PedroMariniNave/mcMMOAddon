package com.zpedroo.mcmmoaddon.utils.menu;

import com.gmail.nossr50.datatypes.database.PlayerStat;
import com.gmail.nossr50.datatypes.player.PlayerProfile;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.util.player.UserManager;
import com.zpedroo.mcmmoaddon.managers.DataManager;
import com.zpedroo.mcmmoaddon.utils.FileUtils;
import com.zpedroo.mcmmoaddon.utils.builder.InventoryBuilder;
import com.zpedroo.mcmmoaddon.utils.builder.InventoryUtils;
import com.zpedroo.mcmmoaddon.utils.builder.ItemBuilder;
import com.zpedroo.mcmmoaddon.utils.formatter.NumberFormatter;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class Menus extends InventoryUtils {

    private static Menus instance;
    public static Menus getInstance() { return instance; }

    public Menus() {
        instance = this;
    }

    public void openMainMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.MAIN;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str, new String[]{
                    "{player}"
            }, new String[]{
                    player.getName()
            }).build();
            int slot = FileUtils.get().getInt(file, "Inventory.items." + str + ".slot");
            String action = FileUtils.get().getString(file, "Inventory.items." + str + ".action");

            inventory.addItem(item, slot, () -> {
                switch (action.toUpperCase()) {
                    case "TOP_SKILLS":
                        openTopSkillsMenu(player);
                        break;
                    case "PLAYER_SKILLS":
                        openSkillsMenu(player, player);
                        break;
                }
            }, ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }

    public void openTopSkillsMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.TOP_SKILLS;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);
        String display = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Display-Format"));

        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            SkillType skillType = SkillType.getSkill(str);
            StringBuilder loreToAdd = null;

            if (skillType != null) {
                List<PlayerStat> topSkill = DataManager.getInstance().getTopSkill(skillType);
                loreToAdd = new StringBuilder(topSkill.size());
                int pos = 0;

                for (PlayerStat playerStat : topSkill) {
                    if (loreToAdd.length() > 0) loreToAdd.append("|");

                    loreToAdd.append(StringUtils.replaceEach(display, new String[]{
                            "{player}",
                            "{pos}",
                            "{level}"
                    }, new String[]{
                            playerStat.name,
                            String.valueOf(++pos),
                            NumberFormatter.getInstance().formatDecimal(playerStat.statVal)
                    }));
                }
            }

            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str, new String[]{
                    "{player}",
                    "{top}"
            }, new String[]{
                    player.getName(),
                    loreToAdd == null ? "" : loreToAdd.toString()
            }).build();
            int slot = FileUtils.get().getInt(file, "Inventory.items." + str + ".slot");

            inventory.addItem(item, slot);
        }

        inventory.open(player);
    }

    public void openSkillsMenu(Player player, Player target) {
        FileUtils.Files file = StringUtils.equals(player.getUniqueId().toString(), target.getUniqueId().toString())
                ? FileUtils.Files.PLAYER_SKILLS : FileUtils.Files.OTHER_SKILLS;

        String title = ChatColor.translateAlternateColorCodes('&', StringUtils.replaceEach(FileUtils.get().getString(file, "Inventory.title"), new String[]{
                "{player}"
        }, new String[]{
                target.getName()
        }));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);
        PlayerProfile playerProfile = UserManager.getPlayer(target.getName()).getProfile();
        Map<SkillType, Integer> playerSkillRanks = DataManager.getInstance().getPlayerRanks(target);

        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            SkillType skillType = SkillType.getSkill(str);

            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str, new String[]{
                    "{player}",
                    "{level}",
                    "{pos}"
            }, new String[]{
                    player.getName(),
                    skillType == null ? "-/-" : NumberFormatter.getInstance().formatDecimal(playerProfile.getSkillLevel(skillType)),
                    skillType == null ? "-/-" : String.valueOf(playerSkillRanks.get(skillType)),
            }).build();
            int slot = FileUtils.get().getInt(file, "Inventory.items." + str + ".slot");

            inventory.addItem(item, slot);
        }

        inventory.open(player);
    }
}