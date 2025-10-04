package com.example.sprint1ms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {
    protected static final Logger LOG = LogManager.getLogger();

    protected void addLog(String message) {
        LOG.info(message);
    }
}
