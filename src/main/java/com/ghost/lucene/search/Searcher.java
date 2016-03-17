package com.ghost.lucene.search;

import com.ghost.NoobleApplication;
import com.ghost.lucene.Constants;
import com.ghost.lucene.LuceneUtility;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
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
    private TopDocs resultDocs;

    @Autowired
    private LuceneUtility properties;

    public Searcher() {}

    /**
     * Initializes searcher. Locks the index directory, so you cant provide parallel index
     */
    private void init() throws IOException {
        Directory indexDirectory = properties.getIndexDirectory();
        if (!DirectoryReader.indexExists(indexDirectory)) {
            NoobleApplication.log.error("Index is not exist in directory: {}!", indexDirectory);
        }
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        indexSearcher = new IndexSearcher(indexReader);
        queryParser = new QueryParser(Constants.CONTENTS, new StandardAnalyzer());
    }

    public Collection<Document> search(String queryString) throws InvalidPathException, IOException, ParseException {
        init();
        Query query = queryParser.parse(queryString);
        resultDocs = indexSearcher.search(query, properties.getMaxSearch());
        ScoreDoc[] hits = resultDocs.scoreDocs;
        Collection<Document> documents = new ArrayList<>();
        for (ScoreDoc hit : hits) {
            documents.add(indexSearcher.doc(hit.doc));
        }
        return documents;
    }

    public int getTotalHits() {
        return resultDocs.totalHits;
    }
}
