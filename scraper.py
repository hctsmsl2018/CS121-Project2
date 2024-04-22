from bs4 import BeautifulSoup
from pathlib import Path
import re
from urllib.parse import urlparse, urlunparse


def scraper(url, resp, seed_url_auths):
    if resp.raw_response is not None:
        page = BeautifulSoup(resp.raw_response.content, "html.parser")
        links = extract_next_links(url, resp, page)

        url_parsed = urlparse(url)
        extract_text(url, resp, page)
        # Return all unabbreviated and valid links
        return [complete_url(link, url_parsed) for link in links if is_valid(link, seed_url_auths)]
    else:
        return []


def extract_next_links(url, resp, page):
    # Implementation required.
    # url: the URL that was used to get the page
    # resp.url: the actual url of the page
    # resp.status: the status code returned by the server. 200 is OK, you got the page. Other numbers mean that there
    # was some kind of problem.
    # resp.error: when status is not 200, you can check the error here, if needed.
    # resp.raw_response: this is where the page actually is. More specifically, the raw_response has two parts:
    #         resp.raw_response.url: the url, again
    #         resp.raw_response.content: the content of the page!
    # Return a list with the hyperlinks (as strings) scrapped from resp.raw_response.content
    if resp.status == 200 and resp.raw_response:
        try:
            # Create BS4 page
            # page = BeautifulSoup(resp.raw_response.content, "html.parser")

            # Find all pages with links
            links = page.find_all(lambda tag: tag.name == "a" and tag.has_attr("href"))

            # Return the links
            return [link['href'] for link in links]
        except:
            return []
    else:
        print(f'{resp.status}: {resp.error}')

    return []


def extract_text(url, response, page):
    # Commonly used tags for text in HTML, may be more
    text_tags = ['p', 'div', 'span', 'li']
    # Creates path to where the downloaded page text should be stored
    cache_dir = str(Path.cwd()) + '/downloaded_pages'
    if response.status == 200 and response.raw_response:
        try:
            Path(cache_dir).mkdir(parents=True, exist_ok=True)
            # Checks for https
            if url[:8] == "https://":
                #  This is here because the '/' character messes with the file path, changed it to
                #  '|' as this is a character not used in urls
                url = url.replace('/', '|')
                #  https link, will truncate the https:// accordingly for the page
                path = Path(cache_dir + '/' + url[8:-1])
            else:
                url = url.replace('/', '-')
                # Does the same but for http links
                path = Path(cache_dir + '/' + url[7:-1])
            # page = BeautifulSoup(response.raw_response.content, "html.parser")
            # Finds all content with the defined HTML tags
            out_file = path.open('w', encoding='utf-8')

            for con in page.find_all(text_tags):
                # Writes the content to a file
                out_file.write(con.text)
            out_file.close()

            return path
        except Exception as e:
            print(e)
            return None
    else:
        print(f'{response.status}: {response.error}')



def complete_url(extracted, src_parsed):
    """Unabbreviates an abbreviated URL found in the website and removes fragment"""
    extracted_parsed = urlparse(extracted)

    # Add scheme if missing
    if not extracted_parsed.scheme:
        extracted_parsed = extracted_parsed._replace(scheme=src_parsed.scheme)

    # Adds authority if missing
    if not extracted_parsed.netloc:
        extracted_parsed = extracted_parsed._replace(netloc=src_parsed.netloc)

    # Adds fragment if missing
    if extracted_parsed.fragment:
        extracted_parsed = extracted_parsed._replace(fragment='')

    return urlunparse(extracted_parsed)


def is_valid(url, config):
    # Decide whether to crawl this url or not. 
    # If you decide to crawl it, return True; otherwise return False.
    # There are already some conditions that return False.
    try:
        parsed = urlparse(url)

        # Check if URL doesn't have a non-http scheme
        if parsed.scheme not in {"http", "https", ''}:
            return False

        # Check if authority is within required domains
        if parsed.netloc:
            for auth in config.seed_url_auths:
                if parsed.netloc.endswith(auth):
                    break
            else:
                return False

        # Check if URL is not only a fragment
        if not any(getattr(parsed, component) for component in ('scheme', 'netloc', 'path', 'params', 'query')):
            return False

        # Check if URL does not have a file extension not corresponding to a webpage
        return not re.match(
            r".*\.(css|js|bmp|gif|jpe?g|ico"
            + r"|png|tiff?|mid|mp2|mp3|mp4"
            + r"|wav|avi|mov|mpeg|ram|m4v|mkv|ogg|ogv|pdf"
            + r"|ps|eps|tex|ppt|pptx|doc|docx|xls|xlsx|names"
            + r"|data|dat|exe|bz2|tar|msi|bin|7z|psd|dmg|iso"
            + r"|epub|dll|cnf|tgz|sha1"
            + r"|thmx|mso|arff|rtf|jar|csv"
            + r"|rm|smil|wmv|swf|wma|zip|rar|gz)$", parsed.path.lower())

    except TypeError:
        print("TypeError for ", parsed)
        raise
