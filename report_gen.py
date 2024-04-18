import os
import tokenizer as tk
from pathlib import Path
def longest_page(file_names):
    file_length = dict()
    for file_name in file_names:
        tokens = tk.tokenize(str(Path.cwd()) + '/downloaded_pages/' + file_name)
        file_length[file_name] = len(tokens)
    return max(k for k, v in file_length.items() if v != 0)


def main():
    file_names = []
    file = open('report.txt', 'w')
    for filename in os.listdir(os.getcwd() + '/downloaded_pages'):
        file_names.append(filename)
    file.write('Longest page by word count: ' + longest_page(file_names))




if __name__ == "__main__":
    main()
