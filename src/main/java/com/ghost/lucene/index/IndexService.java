package com.ghost.lucene.index;

import com.ghost.NoobleApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.text.DecimalFormat;
import java.util.concurrent.*;

/**
 * Uses Indexer to index URL source
 */
@Service
public class IndexService {

    //number of threads to index pages simultaneously
    @Value("${lucene.index.threads}")
    private int numberOfThreads;

    // URL Indexing depth level. Be careful, for values > 2 time indexing will greatly increase
    @Value("${lucene.index.depth}")
    private int maxIndexDepth;

    @Autowired
    private Indexer indexer;

    private ExecutorService executorService;
    private int indexCount = 0;
    private long indexTime = 0;

    public IndexService() {
    }

    /**
     * Call this method to start multithreading recursive index page from specified URL.
     * Be careful, for maxIndexDepth values > 2 time indexing will greatly increase
     * @param sourceLink for index
     * @throws IOException
    */
    public void index(URL sourceLink) throws InvalidPathException, IOException {

        long startTime = System.currentTimeMillis();
        init();
        indexer.init();
        try {
            Future<Integer> future = executorService.submit(new IndexTask(sourceLink, indexer, maxIndexDepth, numberOfThreads));
            indexCount = future.get();
            if (future.isDone()) {
                NoobleApplication.log.info("Indexed: {}", indexCount);
            }
        } catch (StackOverflowError e) {
            NoobleApplication.log.error("Stack overflow!", e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            NoobleApplication.log.error("Interrupted thread: " + Thread.currentThread().getName(), e);
        } catch (ExecutionException e) {
            NoobleApplication.log.error("Exception in thread: " + Thread.currentThread().getName(), e);
        }
        indexer.close();
        stop();
        indexTime = System.currentTimeMillis() - startTime;
        NoobleApplication.log.info("Index time: " + indexTime);
    }

    /**
     * Executor initialization
     */
    public void init() {
        executorService = Executors.newSingleThreadExecutor();
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

    public String getIndexTime() {
        return new DecimalFormat("#.##").format(indexTime / 1000.0);
    }

    public int getIndexCount() {
        return indexCount;
    }

    public void setMaxIndexDepth(int maxIndexDepth) {
        this.maxIndexDepth = maxIndexDepth;
    }
}
