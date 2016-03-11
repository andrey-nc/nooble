package com.ghost.lucene;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;

@Component
public class TextFileFilter implements FileFilter {

    @Override
    public boolean accept(File pathname) {
        return pathname.getName().toLowerCase().endsWith(".txt");
    }
}
