import os
import tokenizer as tk
from urllib.parse import urlparse
from pathlib import Path
import itertools
import shelve
from matplotlib import pyplot as plt

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


def find_avg_content_size(input_file, output_file):
    count = 0
    with open('content_size.txt', 'r') as f:
        lines = [int(line.rstrip()) for line in f]
    
        smallest = min(lines)
        largest = max(lines)
        total_lines = 0
        lines_below_10000 = 0
        lines_above_200000 = 0
        for line in lines:
            total_lines += line
            count +=1
            if(line < 10000):
                lines_below_10000 += 1
            if(line > 350000):
                lines_above_200000 += 1
        average = total_lines / count

        lines.sort()

        plt.plot(lines)
        plt.ylabel("Content Size (bytes)")
        plt.xlabel("Website #")
        plt.savefig("byte_graph.pdf")

        output_file.write("\nSmallest Content Size: " + str(smallest))
        output_file.write("\nLargest Content Size: " + str(largest))
        output_file.write("\nSum of all: " + str(total_lines))
        output_file.write("\nAverage Content Size: " + str(round(average, 2)))
        output_file.write("\n# below 10,000: " + str(lines_below_10000) + "\t Percentage of Total: " + format(lines_below_10000 / count, ".0%"))
        output_file.write("\n# Above 200,000: " + str(lines_above_200000) + "\t Percentage of Total: " + format(lines_above_200000 / count, ".0%"))



def main():
    report = open('report.txt', 'w')
    file_tokens = pre_process("tokens.shelve")
    report.write('Number of unique pages - ' + str(len(file_tokens)) + '\n\n')
    report.write('Longest page by word count:\n ' + longest_page(file_tokens) + '\n')
    write_common_words(common_words(file_tokens), report)
    write_unique_sub_domains(unique_sub_domains(file_tokens.keys()), report)
    find_avg_content_size('content_size.txt', report) #can comment out for submission, is for data use
    report.close()


if __name__ == "__main__":
    main()
