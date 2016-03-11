package com.ghost.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Pattern;

@Component
public class Page {
    Pattern linkPattern = Pattern.compile("<a\\s(?:[^\\s>]*?\\s)*?href=\"(.*?)\".*?>");
    Pattern imageLinkPattern = Pattern.compile("<img.*?src=\"(.*?)\".*?(/>|</img>)", Pattern.DOTALL);

    private String content;
    private URL url;

    @Autowired
    private URLSourceProvider urlSourceProvider;

    /**
     * Be careful, constructor downloads content, it could be slow.
     * @param url to page with links
     * @throws IOException
     */
    public Page(URL url) throws IOException {
        this.url = url;
        this.content = new String(urlSourceProvider.load(url));
    }

    /**
     * Extracts all links from the page like <a href={link}>bla</a>. Method does not cache content. Each time new list will be returned.
     * @return list of URLs from that page.
     * @throws MalformedURLException
     */
    public Collection<URL> getLinks() throws MalformedURLException {
        return extractMatches(linkPattern.matcher(content));
    }

    /**
     * Extracts all links to images from the page like <img src={link}/>. Method does not cache content. Each time new list will be returned.
     * @return list of URLs to images from that page.
     * @throws MalformedURLException
     */
    public Collection<URL> getImageLinks() throws MalformedURLException {
        return extractMatches(imageLinkPattern.matcher(content));
    }

    private Collection<URL> extractMatches(Matcher matcher) throws MalformedURLException {
        Set<URL> links = new HashSet<>();
        while(matcher.find()) {
            links.add(new URL(url, matcher.group(1)));
        }
        return links;
    }
}
