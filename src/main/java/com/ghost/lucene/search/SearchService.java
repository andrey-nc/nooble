package com.ghost.lucene.search;

import com.ghost.lucene.Constants;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Collection;
import java.util.HashSet;

/**
 * Uses Indexer to index URL source
 */
@Service
@PropertySource("classpath:lucene.properties")
public class SearchService {

    @Autowired
    private Searcher searcher;
    private long indexTime;

    public SearchService() {}

    /**
     * Call this method to start index page from specified URL.
     * @param queryString to find
     * @throws IOException
     */
    public Collection<SearchDocument> search(String queryString) throws InvalidPathException, IOException, ParseException {

        long startTime = System.currentTimeMillis();
        Collection<SearchDocument> docs = new HashSet<>();
        searcher.search(queryString)
                .stream()
                .forEach(doc -> {
                    String contents = doc.get(Constants.CONTENTS);
                    String path = doc.get(Constants.SOURCE_PATH);
                    String title = doc.get(Constants.SOURCE_TITLE);
                    docs.add(new SearchDocument(title, contents, path));
                });
        indexTime = System.currentTimeMillis() - startTime;
        return docs;
    }

    public long getIndexTime() {
        return indexTime;
    }

    public int getTotalHits() {
        return searcher.getTotalHits();
    }
}
