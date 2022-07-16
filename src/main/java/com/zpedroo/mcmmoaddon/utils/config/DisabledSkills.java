package com.zpedroo.mcmmoaddon.utils.config;

import com.zpedroo.mcmmoaddon.utils.FileUtils;

import java.util.List;

public class DisabledSkills {

    public static final List<String> NAMES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Disabled-Skills");
}