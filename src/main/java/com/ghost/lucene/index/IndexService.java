package com.ghost.lucene.index;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Uses Indexer to index URL source
 */
@Service
@Scope("session")
@PropertySource("classpath:lucene.properties")
public class IndexService {

    //number of threads to index pages simultaneously
    @Value("${lucene.index.threads}")
    private int numberOfThreads;

    // URL Indexing depth level
    @Value("${lucene.index.depth}")
    private int maxIndexDepth;

    private ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

    private int currentDepth = 0;

    private int indexCount = 0;

    private long indexTime = 0;

    public IndexService() {}

    /**
     * Call this method to start index page from specified URL.
     * @param sourceLink
     * @throws IOException
     */
    public void index(URL sourceLink) throws IOException {

        currentDepth = 0;
        indexCount = 0;
        long startTime = System.currentTimeMillis();
        try {
            IndexTask task = new IndexTask(sourceLink);
            executorService.execute(task);
            indexRoutine(currentDepth, task.getLinks());
        } catch (StackOverflowError e) {
            System.out.println("Depth = " + currentDepth + " IndexCount : " + indexCount);
            e.printStackTrace();
        }
        indexTime = System.currentTimeMillis() - startTime;
    }

    /**
     * Recursive indexing routine
     * @param depth of Recursive
     * @param links for indexing
     * @throws IOException
     */
    private void indexRoutine(int depth, Collection<URL> links) throws IOException {
        if (depth < maxIndexDepth) {
            IndexTask task;
            for (URL link: links) {
                task = new IndexTask(link);
                executorService.execute(task);
                indexCount++;
                indexRoutine(++currentDepth, task.getLinks());
            }
        } else {
            currentDepth = 0;
        }
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
}
