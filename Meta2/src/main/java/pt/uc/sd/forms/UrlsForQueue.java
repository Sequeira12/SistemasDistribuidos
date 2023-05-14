package pt.uc.sd.forms;

import java.io.Serializable;

public class UrlsForQueue implements Serializable {
    String url;

    /**
     * constructor for class UrlsForQueue
     * @param url to send to the queue
     */
    public UrlsForQueue(String url) {
        this.url = url;
    }

    /**
     * getter for the url
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * setter for the url
     * @param url to send to queue
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
