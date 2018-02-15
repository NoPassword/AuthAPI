import configparser
import json

import requests


class Authentication(object):
    APPLICATION_JSON = "application/json"

    def __init__(self):
        config = configparser.RawConfigParser()
        config.read('config.properties')
        self.NOPASSWORD_AUTH_URL = config.get("Authentication", "nopassword_auth_url")
        headers = {"Content-type": self.APPLICATION_JSON}
        self.HEADERS = headers
        self.API_KEY = config.get("Authentication", "api_key")

    def authenticate_user(self, email, ip):
        """Authenticates an user with NoPassword
        :param email: User email
        :param ip: Client IP address
        :return: True if use is authenticated successfully
        """
        message = {
            "Username": email,
            "IPAddress": ip,
            "APIKey": self.API_KEY,
        }
        response = requests.post(self.NOPASSWORD_AUTH_URL, json.dumps(message), headers=self.HEADERS)
        result = response.json()
        return result["AuthStatus"]
