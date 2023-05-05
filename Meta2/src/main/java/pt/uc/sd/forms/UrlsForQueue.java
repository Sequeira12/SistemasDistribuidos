package pt.uc.sd.forms;

import java.io.Serializable;

public class UrlsForQueue implements Serializable {
    String url;

    public UrlsForQueue(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
