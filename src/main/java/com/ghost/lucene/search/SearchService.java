package com.ghost.lucene.search;

import com.ghost.lucene.Constants;
import com.ghost.lucene.LuceneUtility;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Uses Indexer to index URL source
 */
@Service
@PropertySource("classpath:lucene.properties")
public class SearchService {

    @Autowired
    private LuceneUtility properties;

    @Autowired
    private Searcher searcher;
    private long searchTime;
    private Collection<SearchDocument> resultDocs = new HashSet<>();

    public SearchService() {}

    /**
     * Call this method to start index page from specified URL.
     * @param queryString to find
     * @throws IOException
     */
    public void search(String queryString) throws IOException, ParseException {

        long startTime = System.currentTimeMillis();
        resultDocs.clear();
        searcher.search(queryString)
                .stream()
                .forEach(doc -> {
                    String contents = doc.get(Constants.CONTENTS);
                    String path = doc.get(Constants.SOURCE_PATH);
                    String title = doc.get(Constants.SOURCE_TITLE);
                    resultDocs.add(new SearchDocument(title, contents, path));
                });
        searchTime = System.currentTimeMillis() - startTime;
    }

    public String getSearchTime() {
        return new DecimalFormat("#.##").format(searchTime / 1000.0);
    }

    public int getTotalHits() {
        return searcher.getTotalHits();
    }

    public Collection<SearchDocument> getResultDocs() {
        return resultDocs
                .stream()
                .limit(properties.getResultsPerPage())
                .collect(Collectors.toSet());
    }
}
