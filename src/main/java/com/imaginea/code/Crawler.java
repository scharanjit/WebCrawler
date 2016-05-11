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
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author charanjiths
 */
public class Crawler {

    long NUMB = 1000;
    private final List<String> links = new LinkedList<>();          // Just a list of URLs
    private Document htmlDocument;
    private final Set searchWords = new HashSet();                  // final set containing searchWords          

    /**
     *
     * @param currentUrl
     * @param searchString
     * @return true or false it will build a Connection using user agent of
     * availble browser This method finds the href (hypertext reference) from
     * the availble links after successful match found,it will call download
     * method
     */
    public boolean find(String currentUrl, String searchString) {

        Connection connection = Jsoup.connect(currentUrl);
        try {
            htmlDocument = connection.get();
            //  this.htmlDocument = htmlDocument;
        } catch (IOException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }

        Elements linksOnPage = htmlDocument.select("a[href]");
        linksOnPage.stream().forEach((link) -> {
            //finding next url present on page
            //hyperlink
            this.links.add(link.absUrl("href")); //adding next link in a linked list
        });

        links.stream().filter((l) -> (l.contains(searchString))).forEach((l) -> {
            searchWords.add(l.substring(0, 64));
        });
        if (!searchWords.isEmpty()) {
            download(searchWords); //download the files containing searchString
            return true;
        } else {
            System.out.println("Search String NOt found");
        }

        return false;
    }

    /**
     * This method will download all the files matching with search Word
     * criteria
     *
     * @param word
     */
    public void download(Set word) {
        word.stream().forEach((Object s) -> {

            URL link = null;
            try {
                link = new URL(s.toString());
            } catch (MalformedURLException ex) {
                Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
            }
            ByteArrayOutputStream out = null;
            try (InputStream in = new BufferedInputStream(link.openStream())) {
                out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
            }
            byte[] response = out.toByteArray();
            File file = new File(s.toString().substring(53, 64));
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
            }
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(response);
            } catch (IOException ex) {
                Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
    }

}
