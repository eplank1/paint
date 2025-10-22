package com.example.sprint1ms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The type Log helper.
 */
public class LogHelper {
    /**
     * The constant LOG.
     */
    protected static final Logger LOG = LogManager.getLogger();

    /**
     * Add log.
     *
     * @param message the message
     */
    protected void addLog(String message) {
        LOG.info(message);
    }
}
