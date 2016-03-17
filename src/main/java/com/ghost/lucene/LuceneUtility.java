package com.ghost.lucene;

import com.ghost.NoobleApplication;
import com.ghost.utility.OSValidator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Reads lucene.properties file
 */
@Component
public class LuceneUtility {

    @Autowired
    private Environment environment;

    /**
     * Defines index directory depending on OS (win or unix)
     * @return os specific index directory or default
     */
    public String getIndexPath() {
        switch (OSValidator.getOSType()) {
            case WIN: return environment.getProperty("lucene.index.directory.win");
            case UNIX: return environment.getProperty("lucene.index.directory.unix");
        }
        return environment.getProperty("lucene.index.directory");
    }

    public Directory getIndexDirectory() throws IOException {
        String path = getIndexPath();
        Path indexPath = null;
        try {
            indexPath = Paths.get(path);
        } catch (InvalidPathException e) {
            NoobleApplication.log.error("Invalid index path: {}", path);
        }
        NoobleApplication.log.info("Index directory: {}", indexPath);
        return FSDirectory.open(indexPath);
    }

    public int getMaxSearch() {
        return Integer.valueOf(environment.getProperty("lucene.search.max"));
    }

    public int getResultsPerPage() {
        return Integer.valueOf(environment.getProperty("lucene.search.perpage"));
    }
}
