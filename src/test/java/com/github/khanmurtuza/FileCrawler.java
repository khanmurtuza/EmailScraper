package com.github.khanmurtuza;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Crawl file on local disk
 */
public class FileCrawler implements Crawler {

    public Document getDocument(final String link) throws IOException {
        return Jsoup.parse(new File(link), "UTF-8");
    }

    public boolean shouldCrawl(final String link) {
        return true;
    }

    public String getAbsUrl(final Element element) {
        final String link = element.attr("href");
        if (!StringUtils.isBlank(link)) {
            final URL url = this.getClass().getResource(link);
            return url.getFile();
        }
        return link;
    }

}
