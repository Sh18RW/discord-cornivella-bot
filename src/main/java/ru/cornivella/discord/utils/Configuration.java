package ru.cornivella.discord.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static Properties properties;

    public static void init() throws IOException {
        InputStream input = Configuration.class.getClassLoader().getResourceAsStream("bot.properties");

        if (input == null) {
            throw new IOException("Can't open resources/bot.properties");
        }

        properties = new Properties();
        properties.load(input);
    }

    public static String getProperty(String name) {
        if (properties == null) {
            throw new IllegalStateException("Configuration hasn't been initialized! Call init() method.");
        }

        String result = properties.getProperty(name, "");

        if (result.isEmpty()) {
            throw new IllegalArgumentException("resources/bot.properties doesn't have " + name + "!");
        }

        return result;
    }
}
