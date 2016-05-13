/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginea.code;

import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * make donlad and anjimg mehtod
 * 
 * make findword method change
 * 
 * make connection mtheod diff from find
 * 
 * find mehtod just find task
 * 
 * 
 * Log the info
 * sys error
 * 
 * 
 * make method specific ArrayLkst use array lst instead of list
 * 
 * kodeBeagle
 * 
 *
 * @author charanjiths
 */
public class CrawlerApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args)   {
        Crawler w= new Crawler();
//        w.findWord("http://mail-archives.apache.org/mod_mbox/maven-users/","2014");//link and searchword
//        w.findWord("http://www.deccanchronicle.com/","sunday-chronicle");//link and searchword
//        w.findWord("http://www.deccanchronicle.com/","nation");//link and searchword
//        System.out.println(System.nanoTime());


String mydata = "http://www.mail-archives.apache.org/mod_mbox/maven-users/201501.mbox/author";
Pattern pattern = Pattern.compile("(.*?)/author");
Matcher matcher = pattern.matcher(mydata);
if (matcher.find())
{
    System.out.println(matcher.group(1));
}
    }
    
}
