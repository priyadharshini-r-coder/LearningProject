package com.example.learningproject.rxjava.caching.model;

public class Data {
    public String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    public Data clone()
    {
        return new Data();
    }
}
