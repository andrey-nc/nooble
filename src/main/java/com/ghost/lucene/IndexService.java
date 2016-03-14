package com.ghost.lucene;

import com.ghost.source.AbstractPage;
import com.ghost.source.JsoupPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

/**
 * Uses Indexer to index URL source
 */
@Service
@Scope("session")
@PropertySource("classpath:lucene.properties")
public class IndexService {

    private static final String URL_FILENAME_PATTERN = "[^a-zA-Z0-9-_\\.]";

    private static final String FILENAME_PARTS_SEPARATOR = "_";

    @Value("${lucene.index.depth}")
    private int maxIndexDepth;

    private int currentDepth = 0;

    private int indexCount = 0;

    private long indexTime = 0;

    @Autowired
    private Indexer indexer;

/*
    @Autowired
    private TextFileFilter filter;
*/

/*
    @Autowired
    private Environment environment;
*/

    public IndexService() {}

    /**
     * Call this method to start index page from specified URL.
     * @param sourceLink
     * @throws IOException
     */
    public void index(URL sourceLink) throws IOException {

        currentDepth = 0;
        indexCount = 0;
        long startTime = System.currentTimeMillis();
        try {
            indexRoutine(sourceLink, currentDepth);
        } catch (StackOverflowError e) {
            System.out.println("Depth = " + currentDepth + " IndexCount : " + indexCount);
            e.printStackTrace();
        }
        indexTime = System.currentTimeMillis() - startTime;
    }

    /**
     * Recursive indexing routine
     * @param sourceLink
     * @throws IOException
     */
    private void indexRoutine(URL sourceLink, int depth) throws IOException {
        if (depth < maxIndexDepth) {
            indexCount++;
            AbstractPage page = new JsoupPage(sourceLink);
            String text = page.getText();
            indexer.indexSource(text, buildFileName(sourceLink), sourceLink.toString());
            Collection<URL> links = page.getLinks();
            for (URL link: links) {
                indexRoutine(link, currentDepth);
            }
        } else {
            currentDepth = 0;
        }
    }

    public long getIndexTime() {
        return indexTime;
    }

    public int getIndexCount() {
        return indexCount;
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
