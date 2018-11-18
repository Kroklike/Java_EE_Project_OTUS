package ru.otus.akn.project.gwt.client.model;

import com.google.gwt.core.client.JavaScriptObject;

public class NewsJSO extends JavaScriptObject {

    protected NewsJSO() {
    }

    public final native String getTitle() /*-{ return this.title; }-*/;

    public final native String getUrl() /*-{ return this.url; }-*/;
}
