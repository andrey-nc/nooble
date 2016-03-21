package com.ghost.lucene.index;

import com.ghost.NoobleApplication;
import com.ghost.lucene.LuceneUtility;
import com.ghost.lucene.LuceneProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.util.concurrent.*;

/**
 * Uses Indexer to index URL source
 */
@Service
public class IndexService {

    @Autowired
    private LuceneProperties luceneProperties;

    @Autowired
    private Indexer indexer;
    private int numberOfThreads;
    private int indexDepth;
    private int maxIndexDepth;
    private ExecutorService executorService;
    private int indexCount = 0;
    private long indexTime = 0;


    @PostConstruct
    private void initService() {
        numberOfThreads = luceneProperties.getIndex().getThreads();
        indexDepth = luceneProperties.getIndex().getDepth();
        maxIndexDepth = luceneProperties.getIndex().getDepthMax();
    }

    public IndexService() {}

    /**
     * Call this method to start multithreading recursive index page from specified URL.
     * Be careful, for indexDepth values > 2 time indexing will greatly increase
     * @param sourceLink for index
     * @throws IOException
    */
    public void index(URL sourceLink) throws InvalidPathException, IOException {

        long startTime = System.currentTimeMillis();
        init();
        try {
            Future<Integer> future = executorService.submit(new IndexTask(sourceLink, indexer, indexDepth, numberOfThreads));
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
        stop();
        indexTime = System.currentTimeMillis() - startTime;
        NoobleApplication.log.info("Index time: " + getIndexTime());
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
        return LuceneUtility.formatTime(indexTime);
    }

    public int getIndexCount() {
        return indexCount;
    }

    public void setIndexDepth(int indexDepth) {
        this.indexDepth = indexDepth;
    }

    public int getIndexDepth() {
        return indexDepth;
    }

    public int getMaxIndexDepth() {
        return maxIndexDepth;
    }
}
