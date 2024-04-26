from tokenizer import tokenize, computeWordFrequencies

class Simhash:
    def __init__(self):
        self._HASH_LEN = 64
        self._THRESHOLD = 60
        self._BUCKET_SIZE = 8
        self._BUCKET_POWER = 2 ** self._BUCKET_SIZE
        self._lsh_container = [{} for _ in range(self._HASH_LEN // self._BUCKET_SIZE)]
        self._hash_memo = {}

    def add_page(self, url, path_to_contents):
        tokens, page_hash = self._get_toks_and_hash(path_to_contents)

        hash_for_component = page_hash

        if self._check_page_addable(page_hash):
            for bucket in self._lsh_container:
                ind = hash_for_component % self._BUCKET_POWER

                if ind in bucket:
                    bucket[ind].append(page_hash)
                else:
                    bucket[ind] = [page_hash]

                hash_for_component >>= self._BUCKET_SIZE

            return tokens
        
        print(f"page not added: {url}")
        return []
            
    def _get_toks_and_hash(self, path_to_contents):
        tokens = tokenize(path_to_contents)
        frequencies = computeWordFrequencies(tokens)

        vec = [0] * self._HASH_LEN

        for tok, freq in frequencies.items():
            if tok not in self._hash_memo:
                self._hash_memo[tok] = hash(tok)
            
            tok_hash = self._hash_memo[tok]

            i = 0

            while i < self._HASH_LEN:
                vec[i] += freq if tok_hash % 2 else -freq
                tok_hash >>= 1

                i += 1

        page_hash = 0

        for i in vec:
            page_hash += i > 0

            page_hash <<= 1

        return tokens, page_hash

    def _check_page_addable(self, page_hash):
        searched = set()
        hash_for_component = page_hash

        for segment_buckets in self._lsh_container:
            component = hash_for_component % self._BUCKET_POWER

            if component in segment_buckets:
                bucket = segment_buckets[component]

                for other_hash in bucket:
                    if other_hash not in searched:
                        if not self._compare_page_with(page_hash, other_hash):
                            return False

                        searched.add(other_hash)
            
            hash_for_component >>= self._BUCKET_SIZE

        return True

    def _compare_page_with(self, page_hash, other_hash):
        same = 0

        for _ in range(self._HASH_LEN):
            same += page_hash % 2 == other_hash % 2

            page_hash >>= 1
            other_hash >>= 1

        return same < self._THRESHOLD