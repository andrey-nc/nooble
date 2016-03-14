package com.ghost.source;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JsoupPage extends AbstractPage {

    private Document document;

    public JsoupPage(URL url) throws IOException {
        super(url);
        getContent();
    }

    protected void getContent() throws IOException {
        document = Jsoup.connect(url.toString()).get();
    }

    @Override
    public String getText() {
        //String text = document.body().text();
        return Jsoup.parse(document.toString()).text();
    }

    @Override
    public String getTitle() {
        return document.title();
    }

    @Override
    public Collection<URL> getLinks() throws IOException {
        Elements linkElements = document.select("a[href]");
        Set<String> links = linkElements
                .stream()
                .map(link -> link.attr("abs:href"))
                .collect(Collectors.toSet());
        Collection<URL> urls = new HashSet<>();
        for (String link: links) {
            urls.add(new URL(link));
        }
        return urls;
    }
}
