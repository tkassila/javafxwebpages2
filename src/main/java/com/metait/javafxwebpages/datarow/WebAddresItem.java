package com.metait.javafxwebpages.datarow;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class instancies constains a data row of user table. The class values are
 * set in GUI or by the app, when loading data from/to a file, by JSONWebAddress class.
 */
public class WebAddresItem {
    public WebAddresItem(JSONWebAddress jsontitem)
    {
        setOrder(jsontitem.getiOrder());
        setDate(jsontitem.getStrDAte());
        setWebaddress(jsontitem.getStrWebAddress());
        setKeyword(jsontitem.getStrKeywore());
        setTitle(jsontitem.getStrTitle());
        setStar(jsontitem.getStrStar());
        setBookmark(jsontitem.getiBookMark());
    }

    public WebAddresItem(int iNr, int iSTar, String strDAte, String keywords, String webAddress, String title, int bookMark)
    {
        orderProperty().set(iNr);
        dateProperty().set(strDAte);
        keywordProperty().set(keywords);
        starProperty().set(getStartsAfter(iSTar));
        webaddressProperty().set(webAddress);
        titleProperty().set(title);
        bookmarkProperty().set(bookMark);
    }

    private IntegerProperty orderProperty;
    public void setOrder(int iValue) {
        if (orderProperty == null)
            orderProperty();
        orderProperty.set(iValue); }
    public int getOrder() {
        if (orderProperty == null)
            orderProperty();
        return orderProperty.get(); }
    public IntegerProperty orderProperty() {
        if (orderProperty == null)
            orderProperty = new SimpleIntegerProperty(this, "order");
        return orderProperty;
    }

    private IntegerProperty bookmarkProperty;
    public void setBookmark(int iValue) {
        if (bookmarkProperty == null)
            bookmarkProperty();
        bookmarkProperty.set(iValue); }
    public int getBookmark() {
        if (bookmarkProperty == null)
            bookmarkProperty();
        return bookmarkProperty.get(); }
    public IntegerProperty bookmarkProperty() {
        if (bookmarkProperty == null)
            bookmarkProperty = new SimpleIntegerProperty(this, "bookmark");
        return bookmarkProperty;
    }

    private StringProperty titleProperty;
    public void setTitle(String value) {
        if (titleProperty == null)
            titleProperty();
        titleProperty.set(value); }
    public String getTitle() {
        if (titleProperty == null)
            titleProperty();
        return titleProperty.get(); }
    public StringProperty titleProperty() {
        if (titleProperty == null)
            titleProperty = new SimpleStringProperty(this, "title");
        return titleProperty;
    }

    private StringProperty starProperty;
    public void setStar(String value) {
        if (starProperty == null)
            starProperty();
        starProperty.set(value); }
    public String getStar() {
        if (starProperty == null)
            starProperty();
        return starProperty.get(); }
    public StringProperty starProperty() {
        if (starProperty == null)
            starProperty = new SimpleStringProperty(this, "star");
        return starProperty;
    }

    private StringProperty dateProperty;
    public void setDate(String date) {
        if (dateProperty == null)
            dateProperty();
        dateProperty.set(date); }
    public String getDate() {
        if (dateProperty == null)
            dateProperty();
        return dateProperty.get(); }
    public StringProperty dateProperty() {
        if (dateProperty == null)
            dateProperty = new SimpleStringProperty(this, "date");
        return dateProperty;
    }

    private StringProperty keywordProperty;
    public void setKeyword(String keyWord) {
        if (keywordProperty == null)
            keywordProperty();
        keywordProperty.set(keyWord); }
    public String getKeyword() {
        if (keywordProperty == null)
            keywordProperty();
        return keywordProperty.get(); }
    public StringProperty keywordProperty() {
        if (keywordProperty == null)
            keywordProperty = new SimpleStringProperty(this, "keyword");
        return keywordProperty;
    }

    private StringProperty webaddressProperty;
    public void setWebaddress(String keyWord) {
        if (webaddressProperty == null)
            webaddressProperty();
        webaddressProperty.set(keyWord); }
    public String getWebaddress() {
        if (webaddressProperty == null)
            webaddressProperty();
        return webaddressProperty.get(); }
    public StringProperty webaddressProperty() {
        if (webaddressProperty == null)
            webaddressProperty = new SimpleStringProperty(this, "webaddress");
        return webaddressProperty;
    }

    private String getStartsAfter(int iStars)
    {
        int max = iStars;
        String ret = "";
        for(int i = 0; i < max; i++)
            ret = ret +"*";
        return ret;
    }

    public String toString()
    {
        return "" + orderProperty().get() +" " +dateProperty().get() +" " +
        keywordProperty().get() +" " +starProperty().get() +" " +
        titleProperty().get() +" " +webaddressProperty().get() + " " +
        bookmarkProperty().get() ;
    }
}