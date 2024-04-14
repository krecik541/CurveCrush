public class Pair<T, U> {
    public T x;
    public U y;
    public Pair(T x, U y)
    {
        this.x = x;
        this.y = y;
    }
    boolean Equal(Pair par1, Pair par2)
    {
        if(par1.x.equals(par2.x) && par1.y.equals(par2.y))
            return true;
        else
            return false;
    }
}
