import os
import tokenizer as tk
from urllib.parse import urlparse
from pathlib import Path
import itertools
import shelve

def pre_process(shelf_path) -> dict:
    with shelve.open(shelf_path) as shelf:
        return dict(shelf)


def longest_page(file_tokens):
    page = max(k for k, v in file_tokens.items() if v != 0)

    return page


def common_words(file_tokens):
    all_tokens = []
    for file in file_tokens:
        all_tokens += file_tokens[file]
    return tk.computeWordFrequencies(all_tokens)


def write_common_words(com_words, file):
    file.write('\n50 Most common words:\n<Word> - <Frequency>\n')
    top_50 = dict(itertools.islice(com_words.items(), 50))
    for i, word in enumerate(top_50):
        file.write('{} <{}> - <{}>\n'.format(i + 1, word, top_50[word]))


def extract_domain(url):
    return urlparse(url).netloc


def unique_sub_domains(urls):
    sub_domains = []

    for url in urls:
        if url.find('uci.edu') != -1:
            domain = extract_domain(url)
            sub_domains.append(domain)
    return tk.computeWordFrequencies(sub_domains)


def write_unique_sub_domains(sub_domains, file):
    file.write('\nUnique subdomains in ics.uci.edu\n<Subdomain> - <frequency>\n')
    for domain in sub_domains:
        file.write('<{}> - <{}>\n'.format(domain, sub_domains[domain]))


def main():
    report = open('report.txt', 'w')
    file_tokens = pre_process("tokens.shelve")
    report.write('Number of unique pages - ' + str(len(file_tokens)) + '\n\n')
    report.write('Longest page by word count:\n ' + longest_page(file_tokens) + '\n')
    write_common_words(common_words(file_tokens), report)
    write_unique_sub_domains(unique_sub_domains(file_tokens.keys()), report)
    report.close()


if __name__ == "__main__":
    main()
