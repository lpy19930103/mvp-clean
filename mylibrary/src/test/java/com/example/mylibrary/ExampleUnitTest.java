package com.example.mylibrary;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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
    
    @Test
    public void test3() throws Exception {
        Person person = new Person("张三", new Person.Car("benz"));
        Person clone = person.clone();
        clone.getCar().setBrand("byd");
        System.out.println(person.getName() + "  " + person.getCar().getBrand() + "   clone :  " + clone.getCar().getBrand());
        
        Person person1 = new Person("李四", new Person.Car("benz"));
        Person clone1 = MyUtil.<Person>clone(person1);
        clone1.getCar().setBrand("bmw");
        System.out.print(person1.getName() + "  " + person1.getCar().getBrand() + "   clone :  " + clone1.getCar().getBrand());
    }
    
    @Test
    public void test4() {
        Integer a = 1111;
        Integer b = 1111;
        System.out.print(a == b);
    }
    
}

class MyUtil {
    
    private MyUtil() {
        throw new AssertionError();
    }
    
    public static <T> T clone(T obj) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bout);
        oos.writeObject(obj);
        
        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bin);
        return (T) ois.readObject();
        
        // 说明：调用ByteArrayInputStream或ByteArrayOutputStream对象的close方法没有任何意义
        // 这两个基于内存的流只要垃圾回收器清理对象就能够释放资源
    }
}

class Person implements Cloneable, Serializable {
    @Override
    protected Person clone() throws CloneNotSupportedException {
        return (Person) super.clone();
    }
    
    
    private String name;
    private Car car;
    
    public Person(String name, Car car) {
        this.name = name;
        this.car = car;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Car getCar() {
        return car;
    }
    
    public void setCar(Car car) {
        this.car = car;
    }
    
    static class Car implements Serializable {
        
        private String brand;
        
        public Car(String brand) {
            this.brand = brand;
        }
        
        public String getBrand() {
            return brand;
        }
        
        public void setBrand(String brand) {
            this.brand = brand;
        }
    }
}

class StaticClass {
    static String mA;
    
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
