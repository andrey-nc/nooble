package com.ghost.source;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class PageCrawler {

    private List<String> urlTypes = Arrays.asList("xml", "html", "txt");

    //number of threads to download images simultaneously
    public static final int NUMBER_OF_THREADS = 15;

    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private URL url;

    public PageCrawler(URL url) throws MalformedURLException {
        this.url = url;
    }

    /**
     * Call this method to start download images from specified URL.
     * @param urlToPage
     * @throws IOException
     */
    public void downloadImages(String urlToPage) throws IOException {
        Collection<URL> urls = new Page(new URL(urlToPage)).getImageLinks();
        urls
                .stream()
                .filter(this::isImageURL)
                .forEach(link -> executorService.execute(new ImageTask(link, folder)));
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

    //detects is current url is an image. Checking for popular extensions should be enough
    private boolean isImageURL(URL url) {
        String path = url.getFile();
        int index = path.lastIndexOf(".");
        String ext;
        if (index > 0) {
            ext = path.substring(index + 1);
        } else {
            return false;
        }
        return imageExtensions
                .stream()
                .anyMatch(item -> item.equalsIgnoreCase(ext));
    }

}
