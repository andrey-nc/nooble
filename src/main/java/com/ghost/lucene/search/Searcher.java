package com.ghost.lucene.search;

import com.ghost.NoobleApplication;
import com.ghost.lucene.Constants;
import com.ghost.lucene.LuceneUtility;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class Searcher {

    private IndexSearcher indexSearcher;
    private QueryParser queryParser;
    private DirectoryReader directoryReader;
    private TopScoreDocCollector collector;
    private ScoreDoc[] hits;
    @Autowired
    private LuceneUtility luceneUtility;

    public Searcher() {}

    /**
     * Initializes searcher. Locks the index directory, so you cant provide parallel index
     */
    public void init() throws IOException {
        Directory indexDirectory = luceneUtility.getIndexDirectory();
        if (!DirectoryReader.indexExists(indexDirectory)) {
            NoobleApplication.log.error("Index is not exist in directory: {}!", indexDirectory);
        }
        if (directoryReader == null) {
            directoryReader = DirectoryReader.open(indexDirectory);
        }
        DirectoryReader newDirectoryReader = DirectoryReader.openIfChanged(directoryReader);
        directoryReader = newDirectoryReader == null ? directoryReader : newDirectoryReader;
        indexSearcher = new IndexSearcher(directoryReader);
        queryParser = new QueryParser(Constants.CONTENTS, new StandardAnalyzer());
    }

    public void search(String queryString) throws InvalidPathException, IOException, ParseException {
        Query query = queryParser.parse(queryString);
        collector = TopScoreDocCollector.create(luceneUtility.getMaxSearch());
        indexSearcher.search(query, collector);
    }

    public Collection<Document> getHits() throws IOException {
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        NoobleApplication.log.info("Docs found: {}", getTotalHits());
        Collection<Document> documents = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            documents.add(indexSearcher.doc(hit.doc));
        }
        return documents;
    }

    public int getTotalHits() {
        return collector.getTotalHits();
    }
}
