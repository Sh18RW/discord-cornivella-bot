package ru.cornivella.discord.tools;


import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.cornivella.discord.utils.Configuration;

public class Tools {
    private static volatile Tools instance;

    public static Tools getInstance() {
        Tools localInstance = instance;

        if (localInstance == null) {
            synchronized (Tools.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Tools();
                }
            }
        }

        return localInstance;
    }

    private final Logger logger;

    private Tools() {
        logger = LogManager.getLogger(Tools.class);

        try {
            Configuration.init();
        } catch (IOException e) {
            logger.error("Failed Configuration initialization! " + e.getMessage());
        }
    }
}
