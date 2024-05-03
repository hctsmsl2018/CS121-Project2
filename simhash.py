from pathlib import Path
from hashlib import sha256
from collections import defaultdict
import shelve
from tokenizer import tokenize, computeWordFrequencies

class Simhash:
    """Simhash-based data structure for saving fingerprints of visited pages and determining if pages are too similar to be added"""
    def __init__(self):
        """Initialize parameters"""
        # Length of page hash
        self._HASH_LEN = 256
        # Similarity theshold
        self._THRESHOLD = 192
        # Bucket size
        self._BUCKET_SIZE = 32
        # Modulo power for bucket
        self._BUCKET_POWER = 2 ** self._BUCKET_SIZE
        # Container for hash fingerprints
        self._lsh_container = [defaultdict(list) for _ in range(self._HASH_LEN // self._BUCKET_SIZE)]
        # Memo for the hashes of certain words
        self._hash_memo = {}

    def add_page(self, url, path_to_contents, config):
        tokens, page_hash = self._get_toks_and_hash(path_to_contents, config)

        # Duplicate page hash to check addability
        hash_for_component = page_hash

        # Check if page is addable
        if self._check_page_addable(page_hash):
            # Add the hash to each corresponding bucket in the lsh container
            for bucket in self._lsh_container:
                bucket[hash_for_component % self._BUCKET_POWER].append(page_hash)

                hash_for_component >>= self._BUCKET_SIZE

            # Store the tokens in a shelf file for the token if this page is accepted
            with shelve.open(config.tokens_file) as shelf:
                shelf[url] = tokens

            return True
        
        return False
            
    def _get_toks_and_hash(self, path_to_contents, config):
        """Get tokens and hash of the downloaded page"""
        # Tokenize page
        tokens = tokenize(path_to_contents)
        # Compute word frequencies
        frequencies = computeWordFrequencies(tokens)

        # b-dimensional vector for this webpage
        vec = [0] * self._HASH_LEN

        # Process all tokens and add them to b-dimensional vector
        for tok, freq in frequencies.items():
            # If hash never computed, add to memo
            if tok not in self._hash_memo:
                self._hash_memo[tok] = int(sha256(tok.encode("utf-8")).hexdigest(), 16)
            
            # Get hash of token
            tok_hash = self._hash_memo[tok]

            # Loop control variable for each bit
            i = 0

            # Go through each digit of the word's hash, and update the corresponding element of the b-dimensional accordingly
            while i < self._HASH_LEN:
                vec[i] += freq if tok_hash % 2 else -freq
                tok_hash >>= 1

                i += 1

        # Use b-dimensional vector to create a hash for the whole page
        page_hash = 0

        for i in vec:
            page_hash += i > 0

            page_hash <<= 1

        # Return tokens and page hash
        return tokens, page_hash

    def _check_page_addable(self, page_hash):
        """Checks if a page with the given fingerprint is addable"""
        # Store hashes that have been checked
        searched = set()
        # Copy of page hash for checking if it is included
        hash_for_component = page_hash

        # Loop through each segment bucket in the lsh container
        for segment_buckets in self._lsh_container:
            # Get the part of the hash to check if it has a bucket
            component = hash_for_component % self._BUCKET_POWER

            # If it is in a bucket, check if the exact hash is present. If not, continue checking other segments of the hash
            if component in segment_buckets:
                # Get bucket
                bucket = segment_buckets[component]

                # Check throuhg other hashes in bucket
                for other_hash in bucket:
                    # Make sure the hash was not searched before
                    if other_hash not in searched:
                        # If the hashes are too similar, the pages are not addable
                        if not self._compare_page_with(page_hash, other_hash):
                            return False

                        # Add this hash to set of searched hashes
                        searched.add(other_hash)
            
            # Remove checked bits
            hash_for_component >>= self._BUCKET_SIZE

        # Return true if hash to be added is nonexistant
        return True

    def _compare_page_with(self, page_hash, other_hash):
        """Returns True if the hashes are too similar, and returns False otherwise"""
        same = 0

        # Get the number of matching bits in both given fingerprints
        for _ in range(self._HASH_LEN):
            same += page_hash % 2 == other_hash % 2

            page_hash >>= 1
            other_hash >>= 1

        # Return if the number of matching bits is below the threshold
        return same < self._THRESHOLD