import socket

from Authentication import Authentication


def get_ip():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        # doesn't even have to be reachable
        s.connect(('10.255.255.255', 1))
        IP = s.getsockname()[0]
    except:
        IP = '127.0.0.1'
    finally:
        s.close()
    return IP


auth = Authentication()
client_ip = get_ip()
username = input("User name: ")

if auth.authenticate_user(username, client_ip):
    print("user authenticated: true")
else:
    print("user authenticated: false")
