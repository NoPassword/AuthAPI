package com.nopassword.common.utils;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author NoPassword
 */
public class Utils {

    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    /**
     * Gets UTC
     *
     * @return UTC current time yyyy-mm-dd hh:mi:ssZ
     */
    public static String currentTime() {
        return ZonedDateTime.now(ZoneOffset.UTC).toString().substring(0, 19).replace("T", " ") + "Z";
    }

    public static String getLocalIp() {
        String ip = null;
        try (final DatagramSocket socket = new DatagramSocket()) {
            // ip address doesn't need to be reachable
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (Exception ex) {
            LOG.warn("Couldn't get local IP address", ex);
        }
        return ip;
    }

}
