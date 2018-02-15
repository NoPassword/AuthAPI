from Authentication import Authentication

auth = Authentication()
if auth.authenticate_user("john.smith@example.com", "8.8.8.8.8"):
    print("user authenticated successfully")
else:
    print("invalid credentials")
