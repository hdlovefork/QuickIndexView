package com.dean.quickindexview.bean;

/**
 * Created by Administrator on 2016/3/4.
 */
public class Person implements Comparable<Person> {
    public Person(String name, String letter) {
        this.name = name;
        this.letter = letter;
    }

    public String name;
    public String letter;

    @Override
    public int compareTo(Person another) {
        return letter.compareTo(another.letter);
    }
}
