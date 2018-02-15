package com.nopassword.util.model;

public class Message {

    private static String APIKey = "6bbfbdc5-2797-4d7a-a572-1e6616e70088";
    private String Username;
    private static String Command = "UserStatus";
    private String IPAddress;
    private String DeviceOS = "LDAP";
    private String BrowserApp = "LDAP";
    private static String DeviceName;
    private String Display;
    private static String WebAddress = "wiacts.com";
    private String Password;
    private String SecretKey;

    public Message(String Username, String IPAddress) {
        this.Username = Username;
        this.IPAddress = IPAddress;
    }

    public Message(String username, String password, String secretKey, String ipAddress) {
        this.Username = username;
        this.SecretKey = secretKey;
        this.IPAddress = ipAddress;
    }

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String aPIKey) {
        APIKey = aPIKey;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getCommand() {
        return Command;
    }

    public void setCommand(String command) {
        Command = command;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String iPAddress) {
        IPAddress = iPAddress;
    }

    public String getDeviceOS() {
        return DeviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        DeviceOS = deviceOS;
    }

    public String getBrowserApp() {
        return BrowserApp;
    }

    public void setBrowserApp(String browserApp) {
        BrowserApp = browserApp;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getDisplay() {
        return Display;
    }

    public void setDisplay(String display) {
        Display = display;
    }

    public String getWebAddress() {
        return WebAddress;
    }

    public void setWebAddress(String webAddress) {
        WebAddress = webAddress;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSecretKey() {
        return SecretKey;
    }

    public void setSecretKey(String SecretKey) {
        this.SecretKey = SecretKey;
    }

    @Override
    public String toString() {
        return "Message{ Username=" + Username
                + ", Command=" + Command + ", IPAddress=" + IPAddress
                + ", DeviceOS=" + DeviceOS + ", BrowserApp=" + BrowserApp
                + ", DeviceName=" + DeviceName + ", Display=" + Display
                + ", WebAddress=" + WebAddress
                + ", Password=<omited for safety>"
                + ", SecretKey=<omited for safety>}";
    }

}
