package com.metait.javafxwebpages.datarow;

/**
 * This class is for storing web address item into a file by google jar.
 */
public class JSONWebAddress {
    public int getiOrder() {
        return iOrder;
    }

    public void setiOrder(int iOrder) {
        this.iOrder = iOrder;
    }

    public int getiBookMark() {
        return iBookMark;
    }

    public void setiBookMark(int iBookMark) {
        this.iBookMark = iBookMark;
    }

    public String getStrDAte() {
        return strDAte;
    }

    public void setStrDAte(String strDAte) {
        this.strDAte = strDAte;
    }

    public String getStrKeywore() {
        return strKeywore;
    }

    public void setStrKeywore(String strKeywore) {
        this.strKeywore = strKeywore;
    }

    public String getStrStar() {
        return strStar;
    }

    public void setStrStar(String strStar) {
        this.strStar = strStar;
    }

    public String getStrWebAddress() {
        return strWebAddress;
    }

    public void setStrWebAddress(String strWebAddress) {
        this.strWebAddress = strWebAddress;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    private int iOrder = 0;
    private int iBookMark = 0;
    private String strDAte = "";
    private String strKeywore = "";
    private String strStar = "";
    private String strWebAddress = "";
    private String strTitle = "";

        public JSONWebAddress(WebAddresItem item) {
            iOrder = item.getOrder();
            strDAte = item.getDate();
            strKeywore = item.getKeyword();
            strStar = item.getStar();
            strWebAddress = item.getWebaddress();
            strTitle = item.getTitle();
            iBookMark = item.getBookmark();
        }

        public String toString() {
            return "" + iOrder + " " + strStar + " " +
                    strKeywore + " " + strStar + " " +
                    strTitle + " " + strWebAddress;
        }
    }
