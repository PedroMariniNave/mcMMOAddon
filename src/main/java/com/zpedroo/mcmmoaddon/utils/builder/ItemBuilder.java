package com.zpedroo.mcmmoaddon.utils.builder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    private final ItemStack item;

    public ItemBuilder(@NotNull Material material, int amount, short durability) {
        if (StringUtils.equals(material.toString(), "SKULL_ITEM")) {
            this.item = new ItemStack(material, amount, (short) 3);
        } else {
            this.item = new ItemStack(material, amount, durability);
        }
    }

    public ItemBuilder(@NotNull ItemStack item) {
        this.item = item;
    }

    public static ItemBuilder build(FileConfiguration file, String where) {
        return build(file, where, null, null);
    }

    public static ItemBuilder build(FileConfiguration file, String where, String[] placeholders, String[] replacers) {
        String type = StringUtils.replaceEach(file.getString(where + ".type"), placeholders, replacers);
        short data = (short) file.getInt(where + ".data", 0);
        int amount = file.getInt(where + ".amount", 1);

        Material material = Material.getMaterial(type);
        Validate.notNull(material, "Material cannot be null! Check your item configs. Invalid material: " + type);

        ItemBuilder builder = new ItemBuilder(material, amount, data);

        if (file.contains(where + ".name")) {
            String name = ChatColor.translateAlternateColorCodes('&', file.getString(where + ".name"));
            builder.setName(StringUtils.replaceEach(name, placeholders, replacers));
        }

        if (file.contains(where + ".lore")) {
            builder.setLore(file.getStringList(where + ".lore"), placeholders, replacers);
        }

        if (file.contains(where + ".owner")) {
            String owner = file.getString(where + ".owner");

            if (owner.length() <= 16) {
                builder.setSkullOwner(StringUtils.replaceEach(owner, placeholders, replacers));
            } else {
                builder.setCustomTexture(owner);
            }
        }

        if (file.contains(where + ".potions")) {
            for (String potion : file.getConfigurationSection(where + ".potions").getKeys(false)) {
                if (potion == null) continue;

                String potionType = file.getString(where + ".potions." + potion + ".type");
                int duration = file.getInt(where + ".potions." + potion + ".duration") * 20;
                int amplifier = file.getInt(where + ".potions." + potion + ".amplifier") - 1;

                builder.addPotion(potionType, duration, amplifier);
            }
        }

        if (file.contains(where + ".glow") && file.getBoolean(where + ".glow")) {
            builder.setGlow();
        }

        if (file.contains(where + ".enchants")) {
            for (String str : file.getStringList(where + ".enchants")) {
                if (str == null) continue;

                String enchantment = StringUtils.replace(str, " ", "");

                if (StringUtils.contains(enchantment, ",")) {
                    String[] split = enchantment.split(",");
                    builder.addEnchantment(Enchantment.getByName(split[0]), Integer.parseInt(split[1]));
                } else {
                    builder.addEnchantment(Enchantment.getByName(enchantment));
                }
            }
        }

        if (file.contains(where + ".hide-attributes") && file.getBoolean(where + ".hide-attributes")) {
            builder.hideAttributes();
        }

        return builder;
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore, String[] placeholders, String[] replacers) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;

        List<String> newLore = new ArrayList<>(lore.size());

        for (String str : lore) {
            String coloredLine = ChatColor.translateAlternateColorCodes('&', StringUtils.replaceEach(str, placeholders, replacers));
            String[] split = coloredLine.split("\\|");
            newLore.addAll(Arrays.asList(split));
        }

        meta.setLore(newLore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment) {
        return addEnchantment(enchantment, 1);
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        if (enchantment == null) return this;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;

        meta.addEnchant(enchantment, level, true);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addPotion(String type, int duration, int amplifier) {
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        if (meta == null) return this;

        PotionEffectType potionEffectType = PotionEffectType.getByName(type);
        if (potionEffectType == null) return this;

        PotionEffect potionEffect = new PotionEffect(potionEffectType, duration, amplifier);

        meta.addCustomEffect(potionEffect, true);

        PotionType potionType = PotionType.getByEffect(potionEffectType);
        if (potionType != null) {
            meta.addCustomEffect(potionEffect, true);
        }

        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setGlow() {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;

        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder hideAttributes() {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return this;

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        if (owner == null || owner.isEmpty()) return this;

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta == null) return this;

        meta.setOwner(owner);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setCustomTexture(String base64) {
        if (base64 == null || base64.isEmpty()) return this;

        SkullMeta meta = (SkullMeta) item.getItemMeta();
        setCustomTexture(meta, base64);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setCustomTexture(SkullMeta meta, String base64) {
        try {
            Method metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            metaSetProfileMethod.setAccessible(true);
            metaSetProfileMethod.invoke(meta, createProfile(base64));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            try {
                Field metaProfileField = meta.getClass().getDeclaredField("profile");
                metaProfileField.setAccessible(true);
                metaProfileField.set(meta, createProfile(base64));
            } catch (NoSuchFieldException | IllegalAccessException ex2) {
                ex2.printStackTrace();
            }
        }

        return this;
    }

    private GameProfile createProfile(String base64) {
        UUID uuid = new UUID(base64.substring(base64.length() - 20).hashCode(), base64.substring(base64.length() - 10).hashCode());
        GameProfile profile = new GameProfile(uuid, "Player");
        profile.getProperties().put("textures", new Property("textures", base64));

        return profile;
    }

    public ItemStack build() {
        return item.clone();
    }
}