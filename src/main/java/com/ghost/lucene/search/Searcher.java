package com.ghost.lucene.search;

import com.ghost.NoobleApplication;
import com.ghost.lucene.Constants;
import com.ghost.lucene.LuceneUtility;
import com.ghost.lucene.index.Indexer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class Searcher {

    private IndexSearcher indexSearcher;
    private DirectoryReader directoryReader;
    private TopScoreDocCollector collector;

    @Autowired
    private LuceneUtility luceneUtility;

    @Autowired
    private Indexer indexer;

    public Searcher() {}

    /**
     * Initializes searcher. Locks the index directory, so you cant provide parallel index
     * Initializes directory reader from index writer. So it is possible to perform index and search ar one time.
     */
    @PostConstruct
    public void init() throws IOException {
        try {
            directoryReader = DirectoryReader.open(indexer.getIndexWriter());
        } catch (CorruptIndexException e) {
            NoobleApplication.log.error("Corrupt Index Exception!", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            NoobleApplication.log.error("IO Error open Index Reader!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Provides search of the given search string. Results store in Collector. Max search results count defined in
     * lucene.properties
     * @param queryString to search
     * @throws IOException
     * @throws ParseException
     */
    public void search(String queryString) throws IOException, ParseException {
/*
        Directory indexDirectory = luceneUtility.getIndexDirectory();
        if (!DirectoryReader.indexExists(indexDirectory)) {
            NoobleApplication.log.error("Index is not exist in directory: {}!", indexDirectory);
        }
*/
        if (directoryReader == null) {
            init();
        }
        DirectoryReader newDirectoryReader = DirectoryReader.openIfChanged(directoryReader);
        directoryReader = newDirectoryReader == null ? directoryReader : newDirectoryReader;
        indexSearcher = new IndexSearcher(directoryReader);
        QueryParser queryParser = new QueryParser(Constants.CONTENTS, new StandardAnalyzer());
        Query query = queryParser.parse(queryString);
        collector = TopScoreDocCollector.create(luceneUtility.getMaxSearch());
        indexSearcher.search(query, collector);
    }

    /**
     * Returns relevant sorted collection of found documents
     * @return collection of found documents
     * @throws IOException
     */
    public Collection<Document> getHits() throws IOException {
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        NoobleApplication.log.info("Docs found: {}", getTotalHits());
        Collection<Document> documents = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            documents.add(indexSearcher.doc(hit.doc));
        }
        return documents;
    }

    /**
     * @return Total count of found documents
     */
    public int getTotalHits() {
        return collector.getTotalHits();
    }
}
