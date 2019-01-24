/**
 * node class
 * main class contains a 2d array of this object
 */
public class Node {
    //e = empty, b = bomb
    private char content;
    private Boolean marked;
    private Boolean opened;
    private int amountBombsNearby;

    public Node() {
        this.content = 'e';
        this.marked = false;
        this.opened = false;
        this.amountBombsNearby = 0;
    }

    public void setContent(char content) {this.content = content;}
    public void setMarked(Boolean marked) {this.marked = marked;}
    public void setOpened(Boolean opened) {this.opened = opened;}
    public void setAmountBombsNearby(int amount) {this.amountBombsNearby = amount;}

    public char getContent() {return this.content;}
    public Boolean getMarked() {return this.marked;}
    public Boolean getOpened() {return this.opened;}
    public int getAmountBombsNearby() {return amountBombsNearby;}
}
