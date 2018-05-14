package ru.ifmo.rain.Petrovski.arrayset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NavigableSet;

public class TestAreaArraySet {
    public static void main(String[] args) {
        Integer[] temp = new Integer[] {2, 5, 6, 7, 10};
        ArraySet<Integer> a = new ArraySet<>(Arrays.asList(temp));
        for (int i = 0; i < 12; ++i) {
            System.out.println(a.floor(i) + "\t" + i + "\t" + a.ceiling(i));
        }
        NavigableSet<Integer> rvs = a.descendingSet();
        for (Integer it : rvs) {
            System.out.println(it);
        }
    }
}
