package com.ghost.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 */
public class Indexer {

    private IndexWriter indexWriter;

    /**
     * Creates new IndexWriter instance
     * @param indexPath for storing indexes
     * @throws IOException
     */
    public Indexer(File indexPath) throws IOException {
        Directory indexDirectory = FSDirectory.open(indexPath.toPath());
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
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
        Field contentField = null;
        Field fileNameField = null;
        Field filePathField = null;
        if (file.exists() && !file.isDirectory()) {
            contentField = new TextField(LuceneConstants.CONTENTS, new FileReader(file));
            fileNameField = new TextField(LuceneConstants.FILE_NAME, file.getName(), Field.Store.YES);
            filePathField = new TextField(LuceneConstants.FILE_PATH, file.getCanonicalPath(), Field.Store.YES);
        } else {
            throw new RuntimeException(String.format("File %s Not Found", file));
        }
        document.add(contentField);
        document.add(fileNameField);
        document.add(filePathField);
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
     * Creates index of all files in the specified directory, excluding filtered ones
     * @param dataDirPath specifies search directory
     * @param filter
     * @return
     * @throws IOException
     */
    public int createIndex(String dataDirPath, FileFilter filter) throws IOException{
/*
        List list = new ArrayList<Path>();
        //list = List.as new File(dataDirPath).listFiles();
        Arrays.asList(dataDirPath.toFile().listFiles()).stream();
*/


        File[] files = new File(dataDirPath).listFiles();
        for (File file : files) {
            if(!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && filter.accept(file)) {
                indexFile(file);
            }
        }
        return indexWriter.numDocs();
    }
}
