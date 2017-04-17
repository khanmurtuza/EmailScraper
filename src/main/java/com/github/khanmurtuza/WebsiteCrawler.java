package com.github.khanmurtuza;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Crawl remote file
 */
public class WebsiteCrawler implements Crawler {

    static final String SCHEMA_HTTP = "http";
    static final String SCHEMA_HTTPS = "https";

    private final String rootDomain;

    public WebsiteCrawler(final String inputUrl) throws MalformedURLException {
        this.rootDomain = getDomainName(inputUrl);
    }

    public Document getDocument(final String link) throws IOException {
        return Jsoup.connect(link).followRedirects(true).get();
//        return Jsoup.connect(link).get();
    }

    public boolean shouldCrawl(final String link) {
        try {
            final String domain = getDomainName(link);
            return rootDomain.equalsIgnoreCase(domain);
        } catch (IllegalArgumentException e) {
            // do not crawl is its malformed url
            return false;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public String getAbsUrl(final Element element) {
        return element.absUrl("href");
    }

    private String getDomainName(String url) throws MalformedURLException {
        if (!url.startsWith(SCHEMA_HTTP) && !url.startsWith(SCHEMA_HTTPS)) {
            url = SCHEMA_HTTP + "://" + url;
        }
        URL netUrl = new URL(url);
        String host = netUrl.getHost();
        if (host.startsWith("www")) {
            host = host.substring("www".length() + 1);
        }
        return host;
    }

}
