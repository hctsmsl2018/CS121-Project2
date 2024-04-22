from pathlib import Path
from hashlib import sha256
from collections import defaultdict
import shelve
from utils import get_urlhash
from tokenizer import tokenize

class FingerprintIndex:
    def __init__(self):
        self._grams_to_pages = defaultdict(set)
        self._pages_to_grams = defaultdict(set)
        self._SELECTION_MOD = 41
        self._THRESHOLD = 0.8

    def add_page(self, url, path_to_contents, config):
        tokens, filtered_grams = self._get_tokens_and_3_grams(path_to_contents, config)

        if self._check_page_addable(filtered_grams):
            hashed_url = get_urlhash(url)

            self._pages_to_grams[hashed_url] = filtered_grams

            for gram in filtered_grams:
                self._grams_to_pages[gram].add(hashed_url)

            with shelve.open(config.tokens_file) as shelf:
                shelf[hashed_url] = tokens

            return True

        return False
            
    def _get_tokens_and_3_grams(self, path_to_contents, config):
        tokens = tokenize(path_to_contents)

        grams = (" ".join(tokens[i: i + 3]) for i in range(len(tokens) - 3))
        hashed_grams = (int(sha256(gram).hexdigest()[:16], 16) for gram in grams)
        filtered_hashed_grams = {gram for gram in hashed_grams if hashed_grams % self._SELECTION_MOD == 0}

        return tokens, filtered_hashed_grams

    def _check_page_addable(self, filtered_hashed_grams):
        searched_urls = set()

        for hash_gram in filtered_hashed_grams:
            for url in self._pages_to_grams[hash_gram]:
                if url not in searched_urls:
                    if not self._compare_page_with(url, filtered_hashed_grams):
                        return False

                    searched_urls.add(url)
        
        return True

    def _compare_page_with(self, hashed_url, filtered_hashed_grams):
        hashed_url_grams = self._pages_to_grams[hashed_url]

        intersection_len = filtered_hashed_grams & hashed_url_grams

        similarity = intersection_len / (len(filtered_hashed_grams) + len(hashed_url_grams) - intersection_len)

        return similarity < self._THRESHOLD