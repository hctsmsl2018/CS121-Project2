from bs4 import BeautifulSoup

import re
from urllib.parse import urlparse, urlunparse

def scraper(url, resp, seed_url_auths):
    links = extract_next_links(url, resp)
    
    url_parsed = urlparse(url)
    
    # Return all unabbreviated and valid links
    return [complete_url(link, url_parsed) for link in links if is_valid(link, seed_url_auths)]

def extract_next_links(url, resp):
    # Implementation required.
    # url: the URL that was used to get the page
    # resp.url: the actual url of the page
    # resp.status: the status code returned by the server. 200 is OK, you got the page. Other numbers mean that there was some kind of problem.
    # resp.error: when status is not 200, you can check the error here, if needed.
    # resp.raw_response: this is where the page actually is. More specifically, the raw_response has two parts:
    #         resp.raw_response.url: the url, again
    #         resp.raw_response.content: the content of the page!
    # Return a list with the hyperlinks (as strings) scrapped from resp.raw_response.content
    if resp.status == 200 and resp.raw_response:
        try:
            # Create BS4 page
            page = BeautifulSoup(resp.raw_response.content, "html.parser")

            # Find all pages with links
            links = page.find_all(lambda tag: tag.name == "a" and tag.has_attr("href"))
            
            # Return the links
            return [link['href'] for link in links]
        except:
            return []
    else:
        print(f'{resp.status}: {resp.error}')

    return []

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

def is_valid(url, seed_url_auths):
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
            for auth in seed_url_auths:
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
        print ("TypeError for ", parsed)
        raise
