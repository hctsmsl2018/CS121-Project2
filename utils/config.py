import re
from urllib.parse import urlparse


class Config(object):
    def __init__(self, config):
        self.user_agent = config["IDENTIFICATION"]["USERAGENT"].strip()
        print (self.user_agent)
        assert self.user_agent != "DEFAULT AGENT", "Set useragent in config.ini"
        assert re.match(r"^[a-zA-Z0-9_ ,]+$", self.user_agent), "User agent should not have any special characters outside '_', ',' and 'space'"
        self.threads_count = int(config["LOCAL PROPERTIES"]["THREADCOUNT"])
        self.save_file = config["LOCAL PROPERTIES"]["SAVE"]
        self.tokens_file = config["LOCAL PROPERTIES"]["TOKENS"]

        self.host = config["CONNECTION"]["HOST"]
        self.port = int(config["CONNECTION"]["PORT"])

        self.seed_urls = config["CRAWLER"]["SEEDURL"].split(",")

        self.seed_url_auths = []

        for url in self.seed_urls:
            parsed = urlparse(url)

            auth_split = parsed.netloc.split('.', maxsplit=1)

            self.seed_url_auths.append(auth_split[1])

        self.time_delay = float(config["CRAWLER"]["POLITENESS"])

        self.cache_server = None