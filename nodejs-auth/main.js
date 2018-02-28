var https = require("request");
var propertiesReader = require("properties-reader");
var properties = propertiesReader("config.properties");
var apiKey = properties.get("generic_api_key");
var authentication_url = properties.get("nopassword_auth_url");
var ip = require("ip").address();

var data = {
    "APIKey": apiKey,
    "Username": "john.smith@example.com",
    "IPAddress": ip
};

data = JSON.stringify(data);
https.post({
    "headers": {"content-type": "application/json"},
    "url": authentication_url,
    "body": data
}, (error, response, body) => {
    if (error) {
        return console.dir(error);
        return false;
    }
    result = JSON.parse(body);

    if (result.AuthStatus === "Success") {
        console.log("User authenticated: true");
    } else {
        console.log(result.AuthStatus);
        console.log("User authenticated: false");
    }
});