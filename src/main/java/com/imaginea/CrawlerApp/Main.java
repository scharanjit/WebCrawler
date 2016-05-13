package com.imaginea.CrawlerApp;

/**
 *
 * @author charanjiths
 */
public class Main {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        Crawler.find("http://mail-archives.apache.org/mod_mbox/maven-users/", "2014");
//      Crawler.find("http://mail-archives.apache.org/mod_mbox/maven-users/","3014");
//      Crawler.find("http://mail-archives.apache.org/mod_mbox/maven-users/","");
//      Crawler.find("", "2014");
//      Crawler.find("http://www.zx.com", "2014");
    }
}
