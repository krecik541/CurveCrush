package org.example;

import java.io.Serializable;

public class Pair<T, U> implements Serializable {
    public T x;
    public U y;
    public Pair(T x, U y)
    {
        this.x = x;
        this.y = y;
    }
    public Pair(Pair pair)
    {
        this.x = (T) pair.x;
        this.y = (U) pair.y;
    }
    boolean Equal(Pair par1, Pair<Object, Object> par2)
    {
        return par1.x.equals(par2.x) && par1.y.equals(par2.y);
    }

    @Override
    public String toString()
    {
        return x.toString() + " " + y.toString();
    }
}
