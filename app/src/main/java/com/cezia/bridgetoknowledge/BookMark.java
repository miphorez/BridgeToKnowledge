package com.cezia.bridgetoknowledge;

public class BookMark {
    private int numPart;
    private int numFragment;
    private int positionScrollY;
    private int positionScrollX;

    public BookMark() {
        clearBookMark();
    }

    BookMark(BookMark bookMark) {
        numPart = bookMark.getNumPart();
        numFragment = bookMark.getNumFragment();
        positionScrollY = bookMark.getPositionScrollY();
        positionScrollX = bookMark.getPositionScrollX();
    }

    BookMark(int numPart, int numFragment) {
        this.numPart = numPart;
        this.numFragment = numFragment;
    }

    private void clearBookMark() {
        numPart = 0;
        numFragment = 0;
        positionScrollX = 0;
        positionScrollY = 0;
    }

    public int getNumPart() {
        return numPart;
    }

    public void setNumPart(int numPart) {
        this.numPart = numPart;
    }

    public int getNumFragment() {
        return numFragment;
    }

    public void setNumFragment(int numFragment) {
        this.numFragment = numFragment;
    }

    private int getPositionScrollY() {
        return positionScrollY;
    }

    public void setPositionScrollY(int positionScrollY) {
        this.positionScrollY = positionScrollY;
    }

    private int getPositionScrollX() {
        return positionScrollX;
    }

    public void setPositionScrollX(int positionScrollX) {
        this.positionScrollX = positionScrollX;
    }
}
