package com.ghost.source;

import com.ghost.NoobleApplication;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JsoupPage extends AbstractPage {

    private Document document;

    public JsoupPage(URL url) {
        super(url);
        getContent();
    }

    protected void getContent() {
        try {
            document = Jsoup.connect(url.toString()).get();
        } catch (IOException e) {
            NoobleApplication.log.error("Error creating page {}, exception: {}", url.toString(), e.getCause());
        }
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
    public Collection<URL> getLinks() {
        Elements linkElements = document.select("a[href]");
        Set<String> links = linkElements
                .stream()
                .map(link -> link.attr("abs:href"))
                .collect(Collectors.toSet());
        Collection<URL> urls = new HashSet<>();
        links
                .stream()
                .forEach(link -> {
                    try {
                        urls.add(new URL(link));
                    } catch (MalformedURLException e) {
                        NoobleApplication.log.error("Malformed link {}", link);
                    }
                });
/*
        for (String link: links) {
            urls.add(new URL(link));
        }
*/
        return urls;
    }
}
