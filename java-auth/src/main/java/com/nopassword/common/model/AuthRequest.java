package com.nopassword.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * A message representing an authentication request to NoPassword
 *
 * @author NoPassword
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthRequest {

    @JsonProperty("APIKey")
    private String apiKey;

    @JsonProperty("Username")
    private String username;

    @JsonProperty("Command")
    private String command;// = "UserStatus";

    @JsonProperty("IPAddress")
    private String ipAddress;

    @JsonProperty("DeviceOS")
    private String deviceOS;

    @JsonProperty("BrowserId")
    private String browserId;

    @JsonProperty("DeviceName")
    private String deviceName;

    @JsonProperty("Display")
    private String display;

    @JsonProperty("WebAddress")
    private String webAddress;

    @JsonProperty("ClientToken")
    private String clientToken;

    @JsonProperty("WorkstationId")
    private String workstationId;

    @JsonIgnore
    private String password;

    public AuthRequest() {
    }

    /**
     * Creates an authentication message request
     *
     * @param username Username
     * @param apiKey Key
     * @param ipAddress
     */
    public AuthRequest(String username, String apiKey, String ipAddress) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.apiKey = apiKey;
    }

    public AuthRequest(String apiKey, String username) {
        this.apiKey = apiKey;
        this.username = username;
    }

    /**
     * Gets username
     *
     * @return Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username
     *
     * @param username Username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets API key
     *
     * @return
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets API key
     *
     * @param apiKey
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Gets command
     *
     * @return Command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Gets client IP address
     *
     * @return IP address
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets client IP address
     *
     * @param iPAddress Client IP address
     */
    public void setIpAddress(String iPAddress) {
        ipAddress = iPAddress;
    }

    /**
     * Gets device operating system
     *
     * @return OS name
     */
    public String getDeviceOS() {
        return deviceOS;
    }

    /**
     * Sets device operating system
     *
     * @param deviceOS OS name
     */
    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    public String getBrowserId() {
        return browserId;
    }

    public void setBrowserId(String browserId) {
        this.browserId = browserId;
    }

    /**
     * Gets device name
     *
     * @return Device name
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * Sets device name
     *
     * @param deviceName Device name
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * Gets display
     *
     * @return Display
     */
    public String getDisplay() {
        return display;
    }

    /**
     * Sets display
     *
     * @param display Display
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * Gets web address
     *
     * @return Web address
     */
    public String getWebAddress() {
        return webAddress;
    }

    /**
     * Sets web address
     *
     * @param webAddress Web address
     */
    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    /**
     * Gets password
     *
     * @return Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password
     *
     * @param password Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Message{ APIKey=" + (apiKey == null ? null : "<omited for safety>")
                + ", Username=" + username
                + ", Command=" + command + ", IPAddress=" + ipAddress
                + ", DeviceOS=" + deviceOS + ", BrowserApp=" + browserId
                + ", DeviceName=" + deviceName + ", Display=" + display
                + ", WebAddress=" + webAddress
                + ", Password=" + (password == null ? null : "<omited for safety>") + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuthRequest other = (AuthRequest) obj;
        if (!Objects.equals(this.apiKey, other.apiKey)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.command, other.command)) {
            return false;
        }
        if (!Objects.equals(this.ipAddress, other.ipAddress)) {
            return false;
        }
        if (!Objects.equals(this.deviceOS, other.deviceOS)) {
            return false;
        }
        if (!Objects.equals(this.browserId, other.browserId)) {
            return false;
        }
        if (!Objects.equals(this.deviceName, other.deviceName)) {
            return false;
        }
        if (!Objects.equals(this.display, other.display)) {
            return false;
        }
        if (!Objects.equals(this.webAddress, other.webAddress)) {
            return false;
        }
        return Objects.equals(this.password, other.password);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.apiKey);
        hash = 89 * hash + Objects.hashCode(this.username);
        hash = 89 * hash + Objects.hashCode(this.command);
        hash = 89 * hash + Objects.hashCode(this.ipAddress);
        hash = 89 * hash + Objects.hashCode(this.deviceOS);
        hash = 89 * hash + Objects.hashCode(this.browserId);
        hash = 89 * hash + Objects.hashCode(this.deviceName);
        hash = 89 * hash + Objects.hashCode(this.display);
        hash = 89 * hash + Objects.hashCode(this.webAddress);
        hash = 89 * hash + Objects.hashCode(this.clientToken);
        hash = 89 * hash + Objects.hashCode(this.workstationId);
        hash = 89 * hash + Objects.hashCode(this.password);
        return hash;
    }

}
