package com.kiastu.pokerbuddy.model;

import java.util.ArrayList;
import java.util.Iterator;

public class CircularIterator<T> implements Iterator<T> {
    private int index = 0;
    private ArrayList<T> list;

    public CircularIterator(ArrayList<T> arrayList, int index) {
        list = arrayList;
        this.index = index;
    }

    public void remove() {
        list.remove(index);
        index--;//otherwise when they call next, we'd accidentally skip an element.
    }

    public T next() {
        T nextItem = list.get(index);
        if (index == list.size() - 1) {
            //reached the end of the array, loop back.
            index = 0;
        }else{
            index++;
        }
        return nextItem;
    }
    public void setIndex(int index){
        this.index = index;
    }
    public T getCurrent(){
        return list.get(index);
    }

    public boolean hasNext() {
        return !list.isEmpty();
    }
}
