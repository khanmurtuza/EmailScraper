package com.github.khanmurtuza;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public interface Crawler {

    /**
     * Get HTML document for given link
     *
     * @param link link to connect to
     * @return HTML Document
     * @throws IOException
     */
    Document getDocument(final String link) throws IOException;

    /**
     * Determine if given link can be crawled
     *
     * @param link to test if
     * @return TRUE if link can be crawled, FALSE otherwise
     */
    boolean shouldCrawl(final String link);

    /**
     * Get an absolute URL from a URL attribute
     *
     * @param element A href html element
     * @return An absolute URL if one could be made
     */
    String getAbsUrl(final Element element);
}
