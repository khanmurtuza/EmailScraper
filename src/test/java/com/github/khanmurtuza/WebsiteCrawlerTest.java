package com.github.khanmurtuza;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WebsiteCrawlerTest {

    WebsiteCrawler crawler;

    @Before
    public void setup() throws Exception {
        crawler = new WebsiteCrawler("http://www.example.com");
    }

    @Test
    public void shouldCrawlIfDomainIsSame() throws Exception {
        assertTrue(crawler.shouldCrawl("example.com"));
    }

    @Test
    public void shouldCrawlIfSameDomainAndSubPage() throws Exception {
        assertTrue(crawler.shouldCrawl("example.com/page_1.html"));
    }

    @Test
    public void shouldCrawlIfWithWordWidePrefixAndSubPage() throws Exception {
        assertTrue(crawler.shouldCrawl("www.example.com/page_1.html"));
    }

    @Test
    public void shouldNotCrawlIfDomainIsDifferent() throws Exception {
        assertFalse(crawler.shouldCrawl("other-example.com"));
    }

    @Test
    public void shouldNotCrawlIfSubDomain() throws Exception {
        assertFalse(crawler.shouldCrawl("blog.example.com"));
    }
}