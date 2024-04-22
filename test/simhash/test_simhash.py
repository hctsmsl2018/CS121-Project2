import unittest
from collections import namedtuple
from pathlib import Path
from os import chdir
import sys

sys.path.append("../../")

from simhash import Simhash

EXP_RETURN_FALSE = {2, 5}

class TestSimhash(unittest.TestCase):
    def test_simhash(self):
        simhash = Simhash()

        chdir("../../")

        Config = namedtuple("Config", ("tokens_file"))
        config = Config("test.shelve")

        for i in range(1, 10):
            with self.subTest(f"{i}.txt"):
                self.assertEqual(simhash.add_page(str(i), Path(f"test/simhash/sample_pages/{i}.txt"), config), i not in EXP_RETURN_FALSE)

if __name__ == "__main__":
    unittest.main()