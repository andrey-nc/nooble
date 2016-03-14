package com.ghost.lucene.index;

import com.ghost.lucene.Constants;
import com.ghost.lucene.exceptions.CreateDirectoryException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Paths;

/**
 *  Indexes row data files
 */
@Component
public class Indexer {

    private IndexWriter indexWriter;

    @Value("${lucene.index.directory}")
    private String indexPath;

    public Indexer() {}

    /**
     * Creates new IndexWriter instance. Locks the index directory, so you cant provide parallel search
     * @throws CreateDirectoryException
     */
    public void init() throws IOException {
        System.out.println("Indexer - lucene.index.directory = " + indexPath);
        Directory indexDirectory = FSDirectory.open(Paths.get(indexPath));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        indexWriter = new IndexWriter(indexDirectory, config);
/*
        try {
        } catch (InvalidPathException e) {
            throw new CreateDirectoryException("Invalid path to index directory!", e);
        } catch (IOException e) {
            throw new CreateDirectoryException("Error read/write index directory!", e);
        }
*/
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
        System.out.println("Indexing " + file.getCanonicalPath());
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
        System.out.println("Indexing " + name);
        Document document = getDocument(content, name, path, title);
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
