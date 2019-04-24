package com.example.mylibrary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    
    @Test
    public void test() {
        String a = "11";
        String b = "1" + "1";
        System.out.print(a == b);
    }
    
    @Test
    public void test2() {
        StaticClass staticClass1 = new StaticClass();
        StaticClass staticClass2 = new StaticClass();
        staticClass1.say("hah");
    }
    
}

class StaticClass {
  static String mA ;
    static {
        mA = "1";
    }
    
    
    static void say(String str) {
        System.out.print("hello " + str);
    }
    
    static class class1 {
        
        private void say() {
            System.out.print("hello");
        }
    }
}
