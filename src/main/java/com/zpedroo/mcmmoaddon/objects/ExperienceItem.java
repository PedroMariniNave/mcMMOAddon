package com.zpedroo.mcmmoaddon.objects;

import com.gmail.nossr50.datatypes.skills.SkillType;
import com.zpedroo.mcmmoaddon.utils.formatter.NumberFormatter;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ExperienceItem {

    private final SkillType skillType;
    private final ItemStack item;

    public ExperienceItem(SkillType skillType, ItemStack item) {
        this.skillType = skillType;
        this.item = item;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public ItemStack getItem(int experienceAmount) {
        NBTItem nbt = new NBTItem(item.clone());
        nbt.setInteger("ExperienceAmount", experienceAmount);
        nbt.setString("SkillType", skillType.name());
        ItemStack item = nbt.getItem();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        String[] placeholders = new String[]{
                "{amount}"
        };
        String[] replacers = new String[]{
                NumberFormatter.getInstance().format(experienceAmount)
        };
        if (meta.getDisplayName() != null) {
            String displayName = meta.getDisplayName();
            meta.setDisplayName(StringUtils.replaceEach(displayName, placeholders, replacers));
        }

        if (meta.getLore() != null) {
            List<String> lore = meta.getLore();
            for (int i = 0; i < lore.size(); ++i) {
                lore.set(i, StringUtils.replaceEach(lore.get(i), placeholders, replacers));
            }

            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }
}