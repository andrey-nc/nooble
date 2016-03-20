package com.ghost.lucene.index;

import com.ghost.NoobleApplication;
import com.ghost.lucene.Constants;
import com.ghost.lucene.LuceneUtility;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

/**
 *  Indexes row data files
 */
@Component
public class Indexer {

    @Autowired
    private LuceneUtility properties;

    private IndexWriter indexWriter;

    public Indexer() {}

    /**
     * Creates new IndexWriter instance. Locks the index directory, so one cant provide parallel search
     */
    public void init() throws IOException {

        Directory indexDirectory;
        try {
            indexDirectory = properties.getIndexDirectory();
        } catch (IOException e) {
            NoobleApplication.log.error("Error initializing index directory: {}", e);
            throw new RuntimeException("Error initializing index directory!");
        }
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        indexWriter = new IndexWriter(indexDirectory, config);
    }

    public void close() throws IOException{
        indexWriter.close();
    }

    /**
     * Builds the Document from a raw content file. Indexes file contents, name and path.
     * @param file with raw data (plain text file)
     * @return ready to analyze Document
     * @throws IOException
     */
    private Document getDocument(File file) throws IOException{
        Document document = new Document();
        if (file.exists() && !file.isDirectory()) {
            document.add(new TextField(Constants.CONTENTS, new FileReader(file)));
            document.add(new StringField(Constants.SOURCE_NAME, file.getName(), Field.Store.YES));
            document.add(new StringField(Constants.SOURCE_PATH, file.getCanonicalPath(), Field.Store.YES));
        } else {
            NoobleApplication.log.error("File {} Not Found", file);
        }
        return document;
    }

    /**
     * Builds the Document from a raw content. Indexes contents, file name, link and title.
     * @param content of raw data (plain text)
     * @param name of the Document
     * @param path of the Document
     * @param title of the Document
     * @return ready to analyze Document
     */
    private Document getDocument(String content, String name, String path, String title) {
        Document document = new Document();
        document.add(new TextField(Constants.CONTENTS, new StringReader(content)));
        document.add(new StringField(Constants.SOURCE_TITLE, title, Field.Store.YES));
        document.add(new StringField(Constants.SOURCE_NAME, name, Field.Store.YES));
        document.add(new StringField(Constants.SOURCE_PATH, path, Field.Store.YES));
        return document;
    }

    /**
     * Starts file indexing process
     * @param file to be indexed
     * @throws IOException
     */
    private void indexFile(File file) throws IOException{
        NoobleApplication.log.info("Indexing: {}", file.getCanonicalPath());
        Document document = getDocument(file);
        indexWriter.addDocument(document);
    }

    /**
     * Indexes built Document
     * @param content of the source to be indexed
     * @param name of the source
     * @param path of the source
     * @param title of the source
     * @throws IOException
     */
    public void indexSource(String content, String name, String path, String title) throws IOException{
        NoobleApplication.log.info("Indexing: {}", path);
        Document document = getDocument(content, name, path, title);
        // TODO: avoid document duplicating while indexing or searching?
        indexWriter.updateDocument(new Term(name), document);
//        indexWriter.addDocument(document);
    }

}
