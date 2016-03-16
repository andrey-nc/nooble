package com.ghost.lucene.search;

/*
@Component
@Scope("prototype")
*/
public class SearchDocument {

    private String title;
    private String contents;
    private String path;

    public SearchDocument(String title, String contents, String path) {
        this.title = title;
        this.contents = contents;
        this.path = path;
    }

    /*
    public SearchDocument(Document document) {
        this.contents = document.get(Constants.CONTENTS);
        this.path = document.get(Constants.SOURCE_PATH);
        this.title = document.get(Constants.SOURCE_TITLE);

    }
*/

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