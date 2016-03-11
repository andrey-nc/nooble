package com.ghost.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Page {
    Pattern linkPattern = Pattern.compile("<a\\s(?:[^\\s>]*?\\s)*?href=\"(.*?)\".*?>");
    Pattern textPattern = Pattern.compile("<([A-Za-z][A-Za-z0-9]*)\\b[^>]*>(.*?)</\\1>", Pattern.DOTALL);

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
    public Collection<URL> getText() {
        return extractMatches(textPattern.matcher(content));
    }

    private Collection<URL> extractMatches(Matcher matcher) throws MalformedURLException {
        Set<URL> links = new HashSet<>();
        while(matcher.find()) {
            links.add(new URL(url, matcher.group(1)));
        }
        return links;
    }
}
