package com.branchflow;

import java.util.Properties;

public class ConfigUtils {
    public static Properties getDefaultRepoProperties() {
        Properties config = new Properties();
        config.setProperty("core.repositoryformatversion", "0");
        config.setProperty("core.filemode", "false");
        config.setProperty("core.bare", "false");

        return config;
    }
}
