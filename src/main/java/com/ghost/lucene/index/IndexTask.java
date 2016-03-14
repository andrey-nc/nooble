package com.ghost.lucene.index;

import com.ghost.source.AbstractPage;
import com.ghost.source.JsoupPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

@Component
public class IndexTask implements Runnable {

    private static final String URL_FILENAME_PATTERN = "[^a-zA-Z0-9-_\\.]";

    private static final String FILENAME_PARTS_SEPARATOR = "_";

    private URL link;

    private Collection<URL> links;

    @Autowired
    private Indexer indexer;

    public IndexTask(URL link) {
        this.link = link;
        this.links = new HashSet<>();
    }

    public Collection<URL> getLinks() {
        return links;
    }

    @Override
    public void run() {
        try {
            AbstractPage page = new JsoupPage(link);
            indexer.indexSource(page.getText(), buildFileName(link), link.toString(),page.getTitle());
            links = page.getLinks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts url to unique file name, replacing all symbols in url with "_" except letters and numbers
     * @param url to convert
     * @return url based unique file name
     */
    private String buildFileName(URL url) {
        return url.toString().replaceAll(URL_FILENAME_PATTERN, FILENAME_PARTS_SEPARATOR);
    }
}
