package com.ghost.lucene;

import com.ghost.config.AppConfiguration;
import com.ghost.source.URLSourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Uses Indexer class to index user source
 */
@Service
public class IndexService {

    @Autowired
    private Indexer indexer;

    @Autowired
    private TextFileFilter filter;

    @Autowired
    private URLSourceProvider urlSourceProvider;

    @Autowired
    private AppConfiguration configuration;

    public IndexService() {
    }

    public void index(String url) throws IOException {
        if (urlSourceProvider.isAllowed(url)) {
            String html = urlSourceProvider.load(url);
            String
        }
        indexer.createIndex(configuration.getDataDirectory(), filter);
    }

    /**
     * Detects is current url is valid (exist and reachable)
     * @param url to validate
     * @return true/false
     */
    private boolean isValidURL(URL url) {
        urlSourceProvider.isAllowed()
    }

    /**
     * Parses content returned by url source provider. Removes all tags and system texts. Keeps only plain text.
     * @param content that was received from url source provider
     * @return plain text
     */
    private String parseContent(String content) {
        String regexp = "<text>(.+)?";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1);
        }
        System.out.println("No match found!");
        return "";
    }


}
