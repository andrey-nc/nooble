package com.ghost.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

/**
 *  Indexes row data files
 */
@Component
public class Indexer {

    private IndexWriter indexWriter;

    /**
     * Creates new IndexWriter instance
     * @param indexPath for storing indexes
     * @throws IOException
     */
    @Autowired
    public Indexer(@Value("${lucene.index.directory}") String indexPath) throws CreateDirectoryException {
        System.out.println("Indexer - lucene.index.directory = " + indexPath);
        Directory indexDirectory = null;
        try {
            indexDirectory = FSDirectory.open(Paths.get(indexPath));
        } catch (InvalidPathException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new CreateDirectoryException("Error open index directory!");
        }
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        try {
            indexWriter = new IndexWriter(indexDirectory, config);
        } catch (IOException e) {
            throw new CreateDirectoryException("Error read/write index directory!");
        }
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
            System.out.println(String.format("File %s Not Found", file));
        }
        return document;
    }

    /**
     * Builds the Document from a raw content. Indexes contents, file name and link.
     * @param content of raw data (plain text)
     * @param sourceName of raw data
     * @param sourcePath of raw data
     * @return ready to analyze Document
     */
    private Document getDocument(String content, String sourceName, String sourcePath) {
        Document document = new Document();
        document.add(new TextField(Constants.CONTENTS, new StringReader(content)));
        document.add(new StringField(Constants.SOURCE_NAME, sourceName, Field.Store.YES));
        document.add(new StringField(Constants.SOURCE_PATH, sourcePath, Field.Store.YES));
        return document;
    }

    /**
     * Starts file indexing process
     * @param file to be indexed
     * @throws IOException
     */
    private void indexFile(File file) throws IOException{
        System.out.println("Indexing " + file.getCanonicalPath());
        Document document = getDocument(file);
        indexWriter.addDocument(document);
    }

    /**
     * Starts source indexing process
     * @param content of the source to be indexed
     * @param sourceName of the source to be indexed
     * @param sourcePath of the source to be indexed
     * @throws IOException
     */
    public void indexSource(String content, String sourceName, String sourcePath) throws IOException{
        System.out.println("Indexing " + sourceName);
        Document document = getDocument(content,sourceName,sourcePath);
        indexWriter.addDocument(document);
    }

    /**
     * Creates index of all files in the specified directory, excluding filtered ones
     * @param dataDirPath specifies search directory
     * @param filter
     * @return
     * @throws IOException
     */
    public int indexDirectory(String dataDirPath, FileFilter filter) throws IOException{
        File[] files = new File(dataDirPath).listFiles();
        for (File file : files) {
            if(!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && filter.accept(file)) {
                indexFile(file);
            }
        }
        return indexWriter.numDocs();
    }
}
