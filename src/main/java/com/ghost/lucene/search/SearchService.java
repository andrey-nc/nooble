package com.ghost.lucene.search;

import com.ghost.lucene.Constants;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Uses Indexer to index URL source
 */
@Service
public class SearchService {

    @Autowired
    private Environment environment;

    @Autowired
    private Searcher searcher;
    private long searchTime;
    private Collection<SearchDocument> resultDocs = new ArrayList<>();

    /**
     * Queries sent during session
     */
    private Collection<String> sentQueries = new HashSet<>();

    public SearchService() {}

    /**
     * Call this method to start index page from specified URL.
     * @param queryString to find
     * @throws IOException
     */
    public void search(String queryString) throws IOException, ParseException {

        long startTime = System.currentTimeMillis();
        resultDocs.clear();
//        searcher.init();
        searcher.search(queryString);
        searcher.getHits()
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

    /**
     * Retrieves a part of found docs according to results per page property in lucene.properties
     * @param start is a an index of the last displayed document
     * @return a part of the list of found docs from start+1 index with resultsPerPage length
     */
    public Collection<SearchDocument> getResultDocs(int start) {
        return resultDocs
                .stream()
                .skip(start)
                .limit(getDocsPerPage())
                .collect(Collectors.toList());
    }

    public Collection<String> getSentQueries() {
        return sentQueries;
    }

    public int getDocsPerPage() {
        return Integer.parseInt(environment.getProperty("lucene.search.perpage"));
    }
}
