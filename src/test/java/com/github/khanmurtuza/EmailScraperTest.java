package com.github.khanmurtuza;

import org.junit.Test;

import java.net.URL;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EmailScraperTest {

    // class under test
    EmailScraper scraper = new EmailScraper(new FileCrawler());

    @Test
    public void shouldScrapeAllEmails() throws Exception {
        URL url = this.getClass().getResource("/two_email_no_link.html");
        Set<String> allEmails = scraper.getAllEmails(url.getFile());
        assertEquals(2, allEmails.size());
        assertTrue(allEmails.contains("me1@xyz.com"));
        assertTrue(allEmails.contains("me2@xyz.com"));

    }

    @Test
    public void shouldScrapeOnlyUniqueEmails() throws Exception {
        URL url = this.getClass().getResource("/three_email_with_one_duplicate_no_link.html");
        Set<String> allEmails = scraper.getAllEmails(url.getFile());
        assertEquals(2, allEmails.size());
        assertTrue(allEmails.contains("me1@xyz.com"));
        assertTrue(allEmails.contains("me2@xyz.com"));

    }

    @Test
    public void shouldAlsoScrapeEmailsFromLinkedPages() throws Exception {
        URL url = this.getClass().getResource("/with_one_link.html");
        Set<String> allEmails = scraper.getAllEmails(url.getFile());
        assertTrue(allEmails.contains("me1@xyz.com"));
        assertTrue(allEmails.contains("me2@xyz.com"));
        assertTrue(allEmails.contains("me3@xyz.com"));
    }
    @Test
    public void shouldSkipCrawlingVisitedLinks() throws Exception {
        URL url = this.getClass().getResource("/with_self_link.html");
        Set<String> allEmails = scraper.getAllEmails(url.getFile());
        assertTrue(allEmails.contains("me3@xyz.com"));
    }

}
