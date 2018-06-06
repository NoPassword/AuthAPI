package com.nopassword;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.junit.BeforeClass;

/**
 *
 * @author NoPassword
 */
public class NoPasswordTest {

    @BeforeClass
    public static void init() {
        org.apache.log4j.Logger rootLogger = org.apache.log4j.Logger.getRootLogger();
        rootLogger.setLevel(org.apache.log4j.Level.DEBUG);
        PatternLayout layout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
        ConsoleAppender console = new ConsoleAppender(layout);
        console.setName("consoleAppender");
        rootLogger.addAppender(console);
    }

}
