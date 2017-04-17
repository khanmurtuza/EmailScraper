package com.github.khanmurtuza;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.khanmurtuza.WebsiteCrawler.SCHEMA_HTTP;
import static com.github.khanmurtuza.WebsiteCrawler.SCHEMA_HTTPS;

/**
 * Scrape emails from given website
 */
public class EmailScraper {

    private static final String[] SUPPORTED_SCHEMAS = {SCHEMA_HTTP, SCHEMA_HTTPS};
    private static final String EMAIL_REGX = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";

    private final Crawler crawler;
    private final Queue<String> linksToCrawl = new LinkedList<String>();
    private final Set<String> linksTracker = new HashSet<String>();

    public EmailScraper(final Crawler crawler) {
        this.crawler = crawler;
    }

    /**
     * Crawl given website and return all email address found in reachable web pages in this domain.
     *
     * @return set for emails found in this domain
     */
    public Set<String> getAllEmails(final String inputUrl) {
        final Set<String> allEmails = new HashSet<String>();

        addLinkToCrawler(inputUrl);

        while (!linksToCrawl.isEmpty()) {
            final String linkToCrawl = linksToCrawl.poll();
//            System.out.println("linkToCrawl: " + linkToCrawl);

            try {
                final Document document = crawler.getDocument(linkToCrawl);

                Set<String> newEmails = getEmails(document);
                for (String newEmail : newEmails) {
                    if (!allEmails.contains(newEmail)) {
                        System.out.println(newEmail);
                    }
                }
                // add emails found in current page
                allEmails.addAll(newEmails);

                // get all unique links found in current page
                addLinksToCrawler(getLinks(document));
            } catch (IOException e) {
                // continue crawling when there is exception connecting to a url
                continue;
            }
        }
        return allEmails;
    }

    private void addLinkToCrawler(final String link) {
        if (!linksTracker.contains(link)) {
            linksToCrawl.offer(link);
            linksTracker.add(link);
        }
    }

    private void addLinksToCrawler(final Set<String> links) {
        for (final String link : links) {
            addLinkToCrawler(link);
        }
    }

    private Set<String> getEmails(final Document document) {
        final Set<String> emails = new HashSet<String>();
        final Pattern pattern = Pattern.compile(EMAIL_REGX);
        final Matcher matcher = pattern.matcher(document.text());
        while (matcher.find()) {
            emails.add(matcher.group());
        }

        return emails;
    }

    private Set<String> getLinks(final Document document) {
        final Set<String> links = new HashSet<String>();
        final Elements elements = document.select("a[href]");
        for (Element elem : elements) {
            final String absUrl = crawler.getAbsUrl(elem);
            if (!StringUtils.isBlank(absUrl)) {
                if (crawler.shouldCrawl(absUrl)) {
                    links.add(absUrl);
                }
            }
        }
        return links;
    }

    public static void main(String[] args) throws Exception {
        if (args.length <= 0) {
            System.out.println("Please provide url to crawl");
            System.exit(1);
        }

        final String input = args[0];

        String formattedInputUrl = null;
        if (!input.startsWith(SCHEMA_HTTP) && !input.startsWith(SCHEMA_HTTPS)) {
            formattedInputUrl = SCHEMA_HTTP + "://" + input;
        } else {
            formattedInputUrl = input;
        }

        final UrlValidator urlValidator = new UrlValidator(SUPPORTED_SCHEMAS);
        if (!urlValidator.isValid(formattedInputUrl)) {
            System.out.println("Given url is not valid: " + input);
            System.exit(1);
        }

        try {
            final EmailScraper emailScraper = new EmailScraper(new WebsiteCrawler(formattedInputUrl));
            for (final String link : emailScraper.getAllEmails(formattedInputUrl)) {
//            System.out.println(link);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
