package com.imaginea.CrawlerApp;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
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

    /**
     * Declaring class level logger
     */
    private static final Logger LOG = Logger.getLogger(Crawler.class.getName());

    /**
     * This method is used to validate the URL
     *
     * @param urlStr user input
     * @return true if valid otherwise false if not valid
     */
    private static boolean isValidURL(String urlStr) {
        LOG.info("Inside isValidURL method..checking validity of URL");
        try {
            String lclStr = urlStr.trim().toLowerCase(Locale.ENGLISH);
            URI uri = new URI(lclStr);
            return uri.getScheme().equals("http") || uri.getScheme().equals("https");
        } catch (URISyntaxException | RuntimeException ex) {
            LOG.log(Level.SEVERE, "Invalid url");
            return false;
        }

    }

    /**
     * This method is used to create a connection with webpage using JSOup
     * Utility
     *
     * @param url user entered url
     * @return
     */
    private static Document connect(String url) {
        LOG.info("Inside connect method");

        Connection connection = Jsoup.connect(url);

        try {
            Document htmlDocument = connection.get();
            return htmlDocument;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Connection failed");
            return null;
        }

    }

    /**
     *
     * @param url user entered url
     * @param pattern is the matching text in particular webpage
     * @return true if success otherwise false
     */
    public static boolean find(String url, String pattern) {
        LOG.info("Inside find method");
        if (!pattern.equals("")) {
            if (isValidURL(url)) {
                Document htmlDoc = connect(url);
                if (htmlDoc != null) {
                    HashSet<String> resultURLs = matchPattern(pattern, htmlDoc);
                    if (!resultURLs.isEmpty()) {
                        download(resultURLs);
                        LOG.info("--___SUCCESS  __--");
                    } else {
                        LOG.warning("Pattern Not Found... Try with something else");
                    }
                }
            }
        } else {
            LOG.warning("Invalid Pattern");
        }
        return false;
    }

    /**
     * This method populates the Set with matching pattern urls
     *
     * @param pattern text
     * @param htmlDoc
     * @return
     */
    private static HashSet<String> matchPattern(String pattern, Document htmlDoc) {
        LOG.info("Inside match pattern method");

        Elements linksOnPage = htmlDoc.select("a[href]");
        HashSet<String> resultURLs = new HashSet<>();
        ArrayList<String> allURL = new ArrayList<>();
        linksOnPage.stream().forEach((link) -> {

            allURL.add(link.absUrl("href"));
        });

        allURL.stream().filter((l) -> (l.contains(pattern))).forEach((l) -> {
            resultURLs.add(l);
        });

        return resultURLs;
    }

    /**
     * This method will download the data from Set of urls
     *
     * @param resultURLs
     */
    private static void download(HashSet<String> resultURLs) {
        LOG.info("Inside download method");

        resultURLs.stream().forEach((s) -> {
            URL link = null;
            ByteArrayOutputStream out;
            InputStream in;
            FileOutputStream fos;
            try {

                link = new URL(s);
            } catch (MalformedURLException ex) {
                LOG.log(Level.SEVERE, "Invalid URL " + s);
            }

            try {
                in = new BufferedInputStream(link.openStream());
                out = new ByteArrayOutputStream();

                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                    out.flush();
                    out.close();
                }

                byte[] response = out.toByteArray();
                String ext = "txt";
                String name = String.format("%s.%s", System.nanoTime(), ext);//s.toString().substring(53, 58) as per busness requirement
                File file = new File(name);

                file.createNewFile();
                fos = new FileOutputStream(file);
                fos.write(response);

                fos.flush();
                fos.close();

            } catch (IOException ex) {
                LOG.log(Level.SEVERE, "Invalid stream ");
            }

        });
    }

}
