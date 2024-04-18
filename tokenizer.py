import sys


# O(n) where n is the length of the word
# UPdATE 4/9/24 not needed anymore keeping it just for reference
def multiSplit(word: str) -> list:
    alpha_numeric = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                     'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9']
    split_word = []
    start = 0
    end = 0
    for c in word:
        if c not in alpha_numeric:
            split_word.append(word[start:end])
            start = end + 1
            end += 1
        else:
            end += 1
    split_word.append(word[start:end])
    split_word = list(filter(None, split_word))
    return split_word


# O(n) where n is amount of characters in the file
def tokenize(TextFilePath: str) -> list:
    tokens = []
    alpha_numeric = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                     'u', 'v', 'w', 'x', 'y',
                     'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9']
    try:
        file = open(TextFilePath, "r")
    except FileNotFoundError:
        print('File not found')
    except IsADirectoryError:
        print('Entered file is a directory')
    else:
        token = ""
        while True:
            c = file.read(1).lower()
            if c in alpha_numeric:
                token += c
            elif not c:
                break
            else:
                if token:
                    tokens.append(token)
                token = ""
        if token:
            tokens.append(token)
        file.close()

    return tokens


# O(n + 2(m * log(m)) where n is the tokens in the file and m being the length of the ending dictionary
# simplified to O(n * log n) where n is the amount of unique tokens in the file
def computeWordFrequencies(Tokens: list) -> dict:
    counted_tokens = dict()
    for tok in Tokens:
        if tok in counted_tokens:
            counted_tokens[tok] += 1
        else:
            counted_tokens.update({tok: 1})
    counted_tokens = dict(sorted(counted_tokens.items()))
    sorted_tokens = dict(sorted(counted_tokens.items(), key=lambda item: item[1], reverse=True))
    return sorted_tokens


# O(n) where n is the amount of tokens in the dictionary
def printFrequencies(Tokens: dict):
    for tok in Tokens:
        print('{} - {}'.format(tok, Tokens[tok]))

