package com.metait.javafxwebpages.testjava;

/**
 * This is for "test" fun class.
 */
public class MainTest {
    public static void main(String args[]){
            System.out.println("processors=" +Runtime.getRuntime().availableProcessors());
        System.out.println("freememory= bytes=" +Runtime.getRuntime().freeMemory());
        System.out.println("freememory=       " +Runtime.getRuntime().freeMemory()/10);
        System.out.println("totalMemory bytes=" +Runtime.getRuntime().totalMemory());
        System.out.println("totalMemory=      " +Runtime.getRuntime().totalMemory()/10);
        System.out.println("maxMemory bytes=  " +Runtime.getRuntime().maxMemory());
        System.out.println("maxMemory=        " +Runtime.getRuntime().maxMemory()/10);

   }
}
