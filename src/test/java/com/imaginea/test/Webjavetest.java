/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginea.test;

import com.imaginea.Crawler.WebJava;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author charanjiths
 */
public class Webjavetest {

    private static WebJava webjava;

    @BeforeClass
    public static void initWebJava() {
        webjava = new WebJava();
    }

    @Before
    public void beforeEachTest() {
        System.out.println("This is executed before each Test");
    }

    @After
    public void afterEachTest() {
        System.out.println("This is exceuted after each Test");
    }

   
    
    @Test
    public void testFind(){
        String url="http://www.deccanchronicle.com/";
        String searchString="TOP STORIES";
      boolean m=  webjava.find(url, searchString);
        System.out.println(m);
        assertTrue(m);
    }
 
            
    
}
