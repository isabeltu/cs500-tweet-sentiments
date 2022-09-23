public class wordPosNeg {
    public String word;
    public int total;
    public int pos;
    public int neg;

    public wordPosNeg(String word, int total, int pos, int neg){
        this.word = word;
        this.total = total;
        this.pos = pos;
        this.neg = neg;
    }

    public String toString(){
        return word + " pos: " + pos + " neg: " + neg + " total: " + total;
    }
}
