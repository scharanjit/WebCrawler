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
    private final Set<String> urlNavigated = new HashSet<>();       //no duplicates
    private final List<String> urlRemaining = new LinkedList<>();   //next node address
    private final List<String> links = new LinkedList<>();          // Just a list of URLs
    private Document htmlDocument;
    private final Set searchWords = new HashSet();                  // final set containing searchWords          

    /**
     *
     * @param url is the address of website where crawling action will occur
     * @param searchString contains year it will call find method & find all the
     * href (hypertext reference of other pages it will also match the search
     * String after calling matchWord method this method will search all the
     * links present in the provided url
     */
//    public void search(String url, String searchString) {
//
//        while (this.urlNavigated.size() < NUMB) {
//            String currentUrl;
//            if (this.urlRemaining.isEmpty()) {
//                currentUrl = url;
//                this.urlNavigated.add(url);
//            } else {
//                currentUrl = this.nextUrl();
//            }
//            find(currentUrl, searchString);  //check href of all urls & sav dem to links
//
//            boolean success = matchWord(searchString); //matching the search world..2014
//            if (success) {
//                break;
//            }
//            this.urlRemaining.addAll(nextPath());
//
//        }
//
//    }

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
        if (searchWords != null) {
            download(searchWords); //download the files containing searchString
            return true;
        }
        return false;

    }

    /**
     *
     * @param searchWord
     * @return boolean output true or false This method takes single parameter
     * search word And match it to the available list of urls
     */
//    public boolean matchWord(String searchWord) {
//        try {
//            String bodyText;
//
//            bodyText = this.htmlDocument.body().text();
//            return bodyText.toLowerCase(Locale.ENGLISH).contains(searchWord.toLowerCase(Locale.ENGLISH));
//        } catch (NullPointerException ex) {
//            System.out.println(searchWord+" Not found");
//             Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//        return false;
//
//    }

    /**
     *
     * @return This method will always return the next node in the Linkedlist
     */
    public List<String> nextPath() {
        return this.links;
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

//    private String nextUrl() //always return new url
//    {
//        String nextUrl;
//        do {
//            nextUrl = this.urlRemaining.remove(0);//removes all url
//        } while (this.urlNavigated.contains(nextUrl)); //checking in set
//        this.urlNavigated.add(nextUrl);
//        return nextUrl;
//    }
}
