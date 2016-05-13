/*
 * File Name: Crawler.java
 */
package com.imaginea.code;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.apache.commons.lang.RandomStringUtils;
/**
 *
 * @author charanjiths
 */
public class Crawler {

    long NUMB = 1000;
    private final List<String> links = new LinkedList<>();          // Just a list of URLs
    private Document htmlDocument;
    private final HashSet searchWords = new HashSet();                  // final set containing searchWords          

    /**
     *
     * @param currentUrl
     * @param searchString
     * @return true or false it will build a Connection using user agent of
 availble browser This method finds the href (hypertext reference) from
 the availble links after successful match found,it will call downloadMails
 method
     */
    public boolean findWord(String currentUrl, String searchString) {
        if (isValidURL(currentUrl)) {
            
            try {
                Connection connection = Jsoup.connect(currentUrl);
                htmlDocument = connection.get();
            } catch (Exception ex) {
                System.out.println("Connection Not Established");
                return false;
            }

            Elements linksOnPage = htmlDocument.select("a[href]");
            linksOnPage.stream().forEach((link) -> {
                //finding next url present on page
                //hyperlink
                this.links.add(link.absUrl("href")); //adding next link in a linked list
            });

            links.stream().filter((l) -> (l.contains(searchString))).forEach((l) -> {
                searchWords.add(l);
            });
            if (!searchWords.isEmpty()) {
                downloadMails(searchWords); //download the files containing searchString
                System.out.println("----------SUCCESS-------------");
                return true;
            } else {
                System.out.println("Search String NOt found");
            }
        }

        return false;
    }

    /**
     *
     * @param urlStr is the url entered by the user
     * @return url itself if it is valid one else false
     */
    public boolean isValidURL(String urlStr) {
        try {
            String lclStr = urlStr.trim().toLowerCase(Locale.ENGLISH);
            URI uri = new URI(lclStr);
            return uri.getScheme().equals("http") || uri.getScheme().equals("https");
        } catch (Exception ex) {
            System.out.println("Invalid URL");
            return false;
        }
    }

    /**
     * This method will downloadMails all the files matching with search Word
 criteria
     *
     * @param resultURLs
     */
    public void downloadMails(HashSet resultURLs) {
        resultURLs.stream().forEach((s) -> {
            try {
                
                FileOutputStream fos;
                URL link;

                link = new URL(s.toString());

                ByteArrayOutputStream out;
                InputStream in = new BufferedInputStream(link.openStream());
                out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }
                out.close();

                byte[] response = out.toByteArray();
                String ext="txt";
                String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(4), ext);
                File file = new File(name);

                file.createNewFile();
                fos = new FileOutputStream(file);
                fos.write(response);
                fos.close();
            } catch (MalformedURLException ex) {
                Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, (Supplier<String>) ex);
            } catch (IOException ex) {
                Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, (Supplier<String>) ex);
            }

        });
    }

}
