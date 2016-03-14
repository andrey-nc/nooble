package com.ghost.lucene.search;

import com.ghost.lucene.exceptions.CreateDirectoryException;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;

/**
 * Uses Indexer to index URL source
 */
@Service
@PropertySource("classpath:lucene.properties")
public class SearchService {

    @Autowired
    private Searcher searcher;

    private long indexTime;

    private int totalHits;

    private Collection<Document> resultDocs;

    public SearchService() {}

    /**
     * Call this method to start index page from specified URL.
     * @param queryString to find
     * @throws IOException
     */
    public void search(String queryString) throws IOException, ParseException, CreateDirectoryException {

        long startTime = System.currentTimeMillis();
        resultDocs = searcher.search(queryString);
        indexTime = System.currentTimeMillis() - startTime;
        totalHits = searcher.getTotalHits();
    }

    public long getIndexTime() {
        return indexTime;
    }

    public int getTotalHits() {
        return totalHits;
    }

    public Collection<Document> getResultDocs() {
        return resultDocs;
    }
}
