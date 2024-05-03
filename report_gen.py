import os
import tokenizer as tk
from urllib.parse import urlparse
from pathlib import Path
import itertools
import shelve
from matplotlib import pyplot as plt

def pre_process(shelf_path) -> dict:
    """Open shelf at indicated path as dictionary"""
    with shelve.open(shelf_path) as shelf:
        return dict(shelf)


def longest_page(file_tokens):
    """Return URL of longest page"""
    page = max(k for k, v in file_tokens.items() if v != 0)

    return page


def common_words(file_tokens):
    # Get freqencies of all words
    all_tokens = []
    # Create a whole list to compute the frequencies of all words
    for file in file_tokens:
        all_tokens += file_tokens[file]
    return tk.computeWordFrequencies(all_tokens)


def write_common_words(com_words, file):
    """Write 40 most common words into file"""
    file.write('\n50 Most common words:\n<Word> - <Frequency>\n')
    top_50 = dict(itertools.islice(com_words.items(), 50))
    # Loop through the words and write each of them in the file
    for i, word in enumerate(top_50):
        file.write('{} <{}> - <{}>\n'.format(i + 1, word, top_50[word]))


def extract_domain(url):
    """Get authority of the given URL"""
    return urlparse(url).netloc


def unique_sub_domains(urls):
    """Get the frequency of each unique subdomain"""
    sub_domains = []

    # Add all domains under uci.edu to sub_domains
    for url in urls:
        if url.find('uci.edu') != -1:
            domain = extract_domain(url)
            sub_domains.append(domain)
    # Return the frequency of each domain
    return tk.computeWordFrequencies(sub_domains)


def write_unique_sub_domains(sub_domains, file):
    # Write frequency of each sub domain into the file
    file.write('\nUnique subdomains in ics.uci.edu\n<Subdomain> - <frequency>\n')
    for domain in sub_domains:
        file.write('<{}> - <{}>\n'.format(domain, sub_domains[domain]))


def find_avg_content_size(input_file, output_file):
    """Get average size of file content"""
    count = 0
    # Open file with all content sizes
    with open('content_size.txt', 'r') as f:
        lines = [int(line.rstrip()) for line in f]
    
        # Get smallest and largest file sizes
        smallest = min(lines)
        largest = max(lines)

        # Get total lines and number of small and large files
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

        # Sort the number of lines found
        lines.sort()

        # Make a graph of website sizes
        plt.plot(lines)
        plt.ylabel("Content Size (bytes)")
        plt.xlabel("Website #")
        plt.savefig("byte_graph.pdf")

        # Write website size analytics in the file
        output_file.write("\nSmallest Content Size: " + str(smallest))
        output_file.write("\nLargest Content Size: " + str(largest))
        output_file.write("\nSum of all: " + str(total_lines))
        output_file.write("\nAverage Content Size: " + str(round(average, 2)))
        output_file.write("\n# below 10,000: " + str(lines_below_10000) + "\t Percentage of Total: " + format(lines_below_10000 / count, ".0%"))
        output_file.write("\n# Above 200,000: " + str(lines_above_200000) + "\t Percentage of Total: " + format(lines_above_200000 / count, ".0%"))



def main():
    """Generates a report of the crawl"""
    # Open report file to write in
    report = open('report.txt', 'w')
    # Preprocess shelve of tokens
    file_tokens = pre_process("tokens.shelve")
    # Write number of unique pages
    report.write('Number of unique pages - ' + str(len(file_tokens)) + '\n\n')
    # Write longest pages
    report.write('Longest page by word count:\n ' + longest_page(file_tokens) + '\n')
    # Write most common words
    write_common_words(common_words(file_tokens), report)
    # Write unique sub domains
    write_unique_sub_domains(unique_sub_domains(file_tokens.keys()), report)
    #find_avg_content_size('content_size.txt', report) #can comment out for submission, is for data use
    report.close()


if __name__ == "__main__":
    main()
