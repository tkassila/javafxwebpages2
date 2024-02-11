/**
 * The web address application: module-info.java
 */
module com.metait.javafxwebpages {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.base;
    requires java.desktop;
    requires com.google.gson;
    requires com.ibm.icu;
    requires jdk.xml.dom;
    requires java.net.http;
    // requires org.apache.httpcomponents.httpclient;

    opens com.metait.javafxwebpages to javafx.fxml;
    // opens com.metait.javafxwebpages.datarow to javafx.fxml;
    opens com.metait.javafxwebpages.datarow to com.google.gson, javafx.base;
    exports com.metait.javafxwebpages;
}