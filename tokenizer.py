import sys


# O(n) where n is amount of characters in the file
def tokenize(TextFilePath: str) -> list:
    """
    Tokenizes a text file character by character, for alphanumeric characters only
    """
    # List for storing tokens
    tokens = []

    # Characters to allow in a token
    alpha_numeric = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                     'u', 'v', 'w', 'x', 'y',
                     'z']

    # Get stop words
    stop_words = stop_word_gen()

    # Try to open the file, print error if failed
    try:
        file = open(TextFilePath, "r")
    except FileNotFoundError:
        print('File not found')
    except IsADirectoryError:
        print('Entered file is a directory')
    else:
        # Initialize token
        token = ""
        while True:
            # Read a character
            c = file.read(1).lower()

            #  If character is alphanumeric, add it to the token. If not, save the current token
            if c in alpha_numeric:
                token += c
            elif not c:
                break
            else:
                if token and token not in stop_words and len(token) > 2:
                    tokens.append(token)
                token = ""

        # Add last token if needed
        if token and token not in stop_words and len(token) > 2:
            tokens.append(token)
        file.close()

    return tokens


def stop_word_gen() -> list:
    """
    Creates list of stopwords to avoid from a file
    """
    file = open('stop_words', 'r')
    stop_words = []
    # Add all stopwords in the file
    for word in file:
        stop_words.append(word[:-1])
    file.close()
    return stop_words


# O(n + 2(m * log(m)) where n is the tokens in the file and m being the length of the ending dictionary
# simplified to O(n * log n) where n is the amount of unique tokens in the file
def computeWordFrequencies(Tokens: list) -> dict:
    """
    Finds the number of each word in a file, from the tokenize function result
    """
    # Create dict of counted tokens
    counted_tokens = dict()
    # Get counts for each token
    for tok in Tokens:
        if tok in counted_tokens:
            counted_tokens[tok] += 1
        else:
            counted_tokens.update({tok: 1})
    # Sort dictionary of tokens by frequency
    counted_tokens = dict(sorted(counted_tokens.items()))
    sorted_tokens = dict(sorted(counted_tokens.items(), key=lambda item: item[1], reverse=True))
    return sorted_tokens


# O(n) where n is the amount of tokens in the dictionary
def printFrequencies(Tokens: dict):
    """
    Print how many times each token appears in the text file, regardless of capitalization
    """
    for tok in Tokens:
        print('{} - {}'.format(tok, Tokens[tok]))

