package com.zpedroo.mcmmoaddon.utils.formatter;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;

public class NumberFormatter {

    private static NumberFormatter instance;
    public static NumberFormatter getInstance() { return instance; }

    private final int THOUSAND = 1000;
    private final NavigableMap<Integer, String> FORMATS = new TreeMap<>();
    private final List<String> NAMES = new LinkedList<>();

    public NumberFormatter(FileConfiguration file) {
        instance = this;
        NAMES.addAll(file.getStringList("Number-Formatter"));

        for (int i = 0; i < NAMES.size(); i++) {
            FORMATS.put((int) Math.pow(THOUSAND, i+1), NAMES.get(i));
        }
    }

    public int filter(String str) {
        String onlyNumbers = str.replaceAll("[^0-9]+", "");
        if (onlyNumbers.isEmpty()) return 0; // invalid amount

        int number = Integer.parseInt(onlyNumbers);

        String onlyLetters = str.replaceAll("[^A-Za-z]+", "");

        int i = -1;
        if (NAMES.contains(onlyLetters)) {
            for (String format : NAMES) {
                ++i;

                if (StringUtils.equals(format, onlyLetters)) break;
            }
        }

        if (i != -1) number *= Math.pow(number, i + 1);

        return number;
    }

    public String format(int value) {
        Map.Entry<Integer, String> entry = FORMATS.floorEntry(value);
        if (entry == null) return String.valueOf(value);

        int key = entry.getKey();
        int divide = key/THOUSAND;
        int divide1 = value/divide;
        float f = divide1 / 1000f;
        float rounded = ((int)(f * 100))/100f;

        if (rounded % 1 == 0) return ((int) rounded) + "" + entry.getValue();

        return rounded + "" + entry.getValue();
    }

    public String formatDecimal(double number) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(number);
    }
}