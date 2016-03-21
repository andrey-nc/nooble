package com.ghost.lucene.search;

import com.ghost.lucene.LuceneConstants;
import com.ghost.lucene.LuceneProperties;
import com.ghost.lucene.LuceneUtility;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
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
    private LuceneProperties luceneProperties;

    @Autowired
    private Searcher searcher;
    private long searchTime;
    private int docsPerPage;
    private Collection<SearchDocument> resultDocs = new ArrayList<>();

    /**
     * Queries sent during session
     */
    private Collection<String> sentQueries = new HashSet<>();

    public SearchService() {}

    @PostConstruct
    public void init() {
        docsPerPage = luceneProperties.getSearch().getPerPage();
    }


    /**
     * Call this method to start index page from specified URL.
     * @param queryString to find
     * @throws IOException
     */
    public void search(String queryString) throws IOException, ParseException {
        // TODO: create separate thread for search?
        long startTime = System.currentTimeMillis();
        resultDocs.clear();
        searcher.search(queryString);
        searcher.getHits()
                .stream()
                .forEach(doc -> {
                    String contents = doc.get(LuceneConstants.CONTENTS);
                    String path = doc.get(LuceneConstants.SOURCE_PATH);
                    String title = doc.get(LuceneConstants.SOURCE_TITLE);
                    resultDocs.add(new SearchDocument(title, contents, path));
                });
        searchTime = System.currentTimeMillis() - startTime;
    }

    public String getSearchTimeString() {
        return LuceneUtility.formatTime(searchTime);
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
                .limit(docsPerPage)
                .collect(Collectors.toList());
    }

    public Collection<String> getSentQueries() {
        return sentQueries;
    }

    public int getStart() {
        return searcher.getTotalHits() > docsPerPage ? docsPerPage : 0;
    }
}
