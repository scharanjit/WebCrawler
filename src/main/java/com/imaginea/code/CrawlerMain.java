/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginea.Crawler;

/**
 *
 * @author charanjiths
 */
public class WebJavaMain {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        WebJava w= new WebJava();
            w.search("http://mail-archives.apache.org/mod_mbox/maven-users/","2014");//link and searchword
        
        
    }
    
}
