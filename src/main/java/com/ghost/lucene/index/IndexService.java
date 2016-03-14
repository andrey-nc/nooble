package com.ghost.lucene.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Uses Indexer to index URL source
 */
@Service
@PropertySource("classpath:lucene.properties")
public class IndexService {

    //number of threads to index pages simultaneously
    @Value("${lucene.index.threads}")
    private int numberOfThreads;

    // URL Indexing depth level
    @Value("${lucene.index.depth}")
    private int maxIndexDepth;

    @Autowired
    private Indexer indexer;

    private ExecutorService executorService;

    private int currentDepth = 0;

    private int indexCount = 0;

    private long indexTime = 0;

    private Collection<URL> indexedLinks = new HashSet<>();

    public IndexService() {}

    /**
     * Call this method to start index page from specified URL.
     * @param sourceLink for index
     * @throws IOException
    */

    public void index(URL sourceLink) throws IOException {

        init();
        currentDepth = 0;
        indexCount = 0;
        long startTime = System.currentTimeMillis();
        try {
            indexer.init();
            IndexTask task = new IndexTask(sourceLink, indexer);
            executorService.execute(task);
            indexRoutine(currentDepth, task.getLinks());
            indexer.close();


        } catch (StackOverflowError e) {
            System.out.println("Depth = " + currentDepth + " IndexCount : " + indexCount);
            e.printStackTrace();
        }
        indexTime = System.currentTimeMillis() - startTime;
        stop();
    }

    /**
     * Recursive indexing routine
     * @param depth of Recursive
     * @param links for indexing
     * @throws IOException
    */

    private void indexRoutine(int depth, Collection<URL> links) throws IOException {
        if (depth < maxIndexDepth) {
            indexedLinks.addAll(links);
            currentDepth = depth;
            IndexTask task;
            for (URL link: links) {
                task = new IndexTask(link, indexer);
                executorService.execute(task);
                indexCount++;
                indexRoutine(++depth, task.getLinks());


            }
        }
    }

    /**
     * Executor initialization
     */
    public void init() {
        executorService = Executors.newFixedThreadPool(numberOfThreads);

    }

    /**
     * Call this method before shutdown an application
    */
    public void stop() {
        executorService.shutdown();
    }

    /**
     *  Method waits while all tasks have finished
    */
    public void awaitTermination() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

    public long getIndexTime() {
        return indexTime;
    }

    public int getIndexCount() {
        return indexCount;
    }

    public Collection<URL> getIndexedLinks() {
        return indexedLinks;
    }

    public boolean isIndexed(URL url) {
        return indexedLinks.contains(url);
    }

    public void setMaxIndexDepth(int maxIndexDepth) {
        this.maxIndexDepth = maxIndexDepth;
    }
}
