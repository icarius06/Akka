package co.m800.assgnt.akka.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created on 6/21/2017.
 * By Mike.
 * First Akka
 * Copyright (c) 2017 Tulasoft Creative Studio <code@tulasoftcreative.com>
 * All Rights Reserved.
 * <p>
 * Helper class to load properties and other util functions
 */
public class Helper {
    private static Properties getProperties() throws IOException {
        Properties properties = new Properties();
        InputStream resourceStream = Helper.class.getClassLoader().getResourceAsStream("config.properties");
        properties.load(resourceStream);
        return properties;
    }

    /**
     * @return a directory where sample log files are in for tests
     * @throws IOException
     */
    public static String getLogsFolder() throws IOException {
        return getProperties().getProperty("logs_dir");
    }


    /**
     * @return the no of files in
     * @throws IOException
     */
    public static long getNoOfFilesInTestDir() throws IOException {
        Path path = Paths.get(getLogsFolder());

        return Files.list(path).count();
    }


}
