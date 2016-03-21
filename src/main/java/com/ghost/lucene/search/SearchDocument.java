package com.ghost.lucene.search;

import com.fasterxml.jackson.annotation.JsonView;
import com.ghost.json.View;

/**
 *  Holds main fields of searched documents and used to translate via json
 */
public class SearchDocument {

    @JsonView(View.Public.class)
    private String title;

    @JsonView(View.Public.class)
    private String contents;

    @JsonView(View.Public.class)
    private String path;

    public SearchDocument(String title, String contents, String path) {
        this.title = title;
        this.contents = contents;
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getPath() {
        return path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setPath(String path) {
        this.path = path;
    }
}