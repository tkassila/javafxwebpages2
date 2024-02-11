package com.metait.javafxwebpages;

/**
 * This class is storing last selected table row
 */
public class JsonConfig {
    public int getLastOrderNumber() {
        return iLastOrderNumber;
    }

    public void setLastOrderNumber(int iLastOrderNumber) {
        this.iLastOrderNumber = iLastOrderNumber;
    }

    public int iLastOrderNumber = -1;
}
