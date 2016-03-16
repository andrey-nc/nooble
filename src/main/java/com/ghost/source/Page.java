package com.ghost.source;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Page extends AbstractPage {

    Pattern linkPattern = Pattern.compile("<a\\s(?:[^\\s>]*?\\s)*?href=\"(.*?)\".*?>");
    // TODO: check rexexp
    Pattern textPattern = Pattern.compile("<([A-Za-z][A-Za-z0-9]*)\\b[^>]*>(.*?)</\\1>", Pattern.DOTALL);
    Pattern titlePattern = Pattern.compile("<title>(.*?)</title>");

    private String content;

    public Page(URL url) throws IOException {
        super(url);
        this.content = getContent();
    }

    protected String getContent() throws IOException {
        return ConnectionUtils.loadText(url.toString());
    }

    /**
     * Extracts plain text from the page excluding tags like </tag> text </tag>.
     * @return plain text without tags from that page.
     */
    @Override
    public String getText() {
        return extractText(textPattern.matcher(content));
    }

    public String extractText(Matcher matcher) {
        StringBuilder text = new StringBuilder();
        while(matcher.find()) {
            text.append(matcher.group(1)).append(" ");
        }
        return text.toString();
    }

    /**
     * Extracts title
     * @return title
     */
    @Override
    public String getTitle() {
        return extractTitle(titlePattern.matcher(content));
    }

    public String extractTitle(Matcher matcher) {
        return matcher.find() ? matcher.group(1) : url.toString();
    }

    /**
     * Extracts all links from the page like <a href={link}>bla</a>. Method does not cache content. Each time new list will be returned.
     * @return list of URLs from that page.
     * @throws MalformedURLException
     */
    @Override
    public Collection<URL> getLinks() throws IOException {
            return extractMatches(linkPattern.matcher(content));
    }

    private Collection<URL> extractMatches(Matcher matcher) throws IOException {
        Set<URL> links = new HashSet<>();
        while(matcher.find()) {
            links.add(new URL(url, matcher.group(1)));
        }
        return links;
    }
}
