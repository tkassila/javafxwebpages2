package com.metait.javafxwebpages;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLAnchorElement;
// import org.w3c.dom.html.HTMLAnchorElement;

// import java.awt.*;


/**
 * This class is listening for just loaded web document and extract some meta values (keywords and title).
 */
public class WebPageLoadListener implements ChangeListener<Worker.State>, EventListener
{
    private static final String CLICK_EVENT = "click";
    private static final String FOCUS_EVENT = "focusin";
    private static final String ANCHOR_TAG = "a";

    private final WebView webView;
    private WebPagesController m_webviewController;

    public WebPageLoadListener(WebView webView, WebPagesController webPagesController)
    {
        this.webView = webView;
        this.m_webviewController = webPagesController;
    }

    @Override
    public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue)
    {
        if (Worker.State.SUCCEEDED.equals(newValue))
        { // search for title an keywords:
            String strTitle = null;
            String strKeyword = null;
            Document document = webView.getEngine().getDocument();
            Node node;
            NodeList titleList = document.getElementsByTagName("title");
            if (titleList == null || titleList.getLength() == 0)
                titleList = document.getElementsByTagName("TITLE");
            if (titleList != null && titleList.getLength()>0)
            {
                node = titleList.item(0);
                if (node != null)
                    strTitle = node.getTextContent();
            }

            String strNoeName, strContent;
            Node nodeContent, nodeName;
            NamedNodeMap attributes;
            NodeList keywordList = document.getElementsByTagName("meta");
            if (keywordList == null || keywordList.getLength() == 0)
                keywordList = document.getElementsByTagName("META");
            if (keywordList != null && keywordList.getLength()>0) {
                for (int i = 0; i < keywordList.getLength(); i++) {
                    node = keywordList.item(i);
                    attributes = node.getAttributes();
                    nodeName = attributes.getNamedItem("name");
                    nodeContent = attributes.getNamedItem("content");
                    if (nodeName != null && nodeName.getNodeValue().equals("keywords")
                            && nodeContent != null) {
                        strKeyword = nodeContent.getNodeValue();
                        break;
                    }
                }
            }
            if (strTitle != null)
                strTitle = strTitle.replaceAll("\n"," ")
                        .replaceAll("\\s+"," ");
            if (strKeyword != null)
                strKeyword = strKeyword.replaceAll("\n"," ")
                        .replaceAll("\\s+"," ");
            m_webviewController.calledWhenWebViewDocLoaded(strTitle,  strKeyword);
        }
    }

    @Override
    public void handleEvent(Event event)
    {
        // EventTarget anchorElement = event.getCurrentTarget();
        String eventType = event.getType();
        HTMLAnchorElement anchorElement = (HTMLAnchorElement) event.getCurrentTarget();
        // System.out.println("anchorElement=" +anchorElement.getClass().getName());
        String href = anchorElement.getHref();
        String text = anchorElement.getTextContent();
        String id = anchorElement.getId();
        //     System.out.println("-1- href=" +href);
        if (eventType != null && eventType.equals("focusin"))
        {
          //  m_playerController.handleFocusIn(href, id, text);
            return;
        }
      //  m_playerController.handleLinkClick(href, id, text);

        /*
        if (Desktop.isDesktopSupported())
        {
            openLinkInSystemBrowser(href);
        } else
        {
            // LOGGER.warn("OS does not support desktop operations like browsing. Cannot open link '{}'.", href);
        }
        */
    //   event.preventDefault();
   }

    private void openLinkInSystemBrowser(String url)
    {
        // LOGGER.debug("Opening link '{}' in default system browser.", url);

        /*
        try
        {
            URI uri = new URI(url);
            Desktop.getDesktop().browse(uri);
        } catch (Throwable e)
        {
            // LOGGER.error("Error on opening link '{}' in system browser.", url);
        }
         */
    }
}
