package com.nopassword;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nopassword.common.model.AuthResult;
import com.nopassword.common.model.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;
import org.springframework.web.client.RestTemplate;

/**
 * A simple class to authenticate with NoPassword
 *
 * @author NoPassword
 */
public class AuthenticationSample {

    private static final String SUCCESS = "Success";
    public static String AUTH_SERVICE_URL;

    public static void main(String[] args) throws SocketException, IOException {
        if (args == null || args.length == 0
                || (!"java".equals(args[0]) && !"spring".equals(args[0]))) {
            System.out.println("Usage: java -jar np-auth-sample-1.0.jar java|spring");
            return;
        }
        Properties properties = new Properties();
        properties.load(AuthenticationSample.class.getResourceAsStream("/config.properties"));
        AUTH_SERVICE_URL = properties.getProperty("nopassword_auth_url");
        String clientIP = getLocalHostLANAddress().getHostAddress();
        String apiKey = properties.getProperty("generic_api_key");
        Scanner scanner = new Scanner(System.in);
        System.out.print("User name: ");
        String username = scanner.nextLine();
        Message message = new Message(apiKey, username, clientIP);

        if ("java".equals(args[0])) {
            System.out.println("User authenticated with Java: "
                    + AuthenticationSample.authenticateUser(message));
        } else if ("spring".equals(args[0])) {
            System.out.println("User authenticated with Spring: "
                    + AuthenticationSample.authenticateUserWithSpring(message));
        }
    }

    /**
     * Authenticates user against NoPassword using pure Java
     *
     * @param msg Authentication request
     * @return true if user is authenticated successfully
     * @throws java.io.IOException
     */
    public static boolean authenticateUser(Message msg) throws IOException {
        AuthResult result = doPost(AUTH_SERVICE_URL, msg, AuthResult.class);

        if (!SUCCESS.equals(result.getAuthStatus())) {
            System.out.println(result.getAuthStatus());
        }

        return SUCCESS.equals(result.getAuthStatus());
    }

    /**
     * Authenticates an user against NoPassword using spring framework
     *
     * @param msg message containing user and user's IP address
     * @return true if user is authenticated successfully
     */
    public static boolean authenticateUserWithSpring(Message msg) {
        RestTemplate rt = new RestTemplate();
        AuthResult result = rt.postForObject(AUTH_SERVICE_URL, msg, AuthResult.class);

        if (!SUCCESS.equals(result.getAuthStatus())) {
            System.out.println(result.getAuthStatus());
        }

        return SUCCESS.equalsIgnoreCase(result.getAuthStatus());
    }

    /**
     * Pure JSE REST client
     *
     * @param <T>
     * @param url URL
     * @param o Data
     * @param resultType Class
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public static <T> T doPost(String url, Object o, Class<T> resultType) throws MalformedURLException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(o);
        URL urlx = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlx.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        OutputStream out = conn.getOutputStream();
        out.write(payload.getBytes());
        out.flush();

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed: HTTP error code " + conn.getResponseCode());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder input = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            input.append(line);
        }

        conn.disconnect();
        return mapper.readValue(input.toString(), resultType);
    }

    /**
     * This method will scan all IP addresses on all network interfaces on the
     * host machine to determine the IP address most likely to be the machine's
     * LAN address.
     *
     * @return
     * @throws UnknownHostException
     */
    public static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // Iterate all NICs (network interface cards)...
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // Iterate all IP addresses assigned to each card...
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // Found non-loopback site-local address. Return it immediately...
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            candidateAddress = inetAddr;
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                // We did not find a site-local address, but we found some other non-loopback address.
                // Server might have a non-site-local address assigned to its NIC (or it might be running
                // IPv6 which deprecates the "site-local" concept).
                // Return this non-loopback candidate address...
                return candidateAddress;
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (IOException e) {
            UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }

}
