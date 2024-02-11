package com.metait.javafxwebpages.url;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * This class for reading loaded web constent in the right characters.
 * This is not used any more.
 */
public class Url2String {

    private String unknowCharacterSetName = null;
    private int iConfidence = -1;

    public String getUnknowCharacterSetName() {
        return unknowCharacterSetName;
    }

    public int getiConfidence() {
        return iConfidence;
    }

    public String getStringFromUrl(URL url) throws IOException {
        return inputStreamToString(urlToInputStream(url,null));
    }

    public String inputStreamToString(InputStream inputStream) throws IOException {
        try(ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            CharsetDetector detector = new CharsetDetector();
            detector.setText(result.toByteArray());

            Charset charset = StandardCharsets.UTF_8;
            CharsetMatch cm = detector.detect();

            String strcharset = null;
            int confidence = 0;
            unknowCharacterSetName = null;
            iConfidence = -1;

            if (cm != null) {
                confidence = cm.getConfidence();
                System.out.println("Encoding: " + cm.getName() + " - Confidence: " + confidence + "%");
                //Here you have the encode name and the confidence
                //In my case if the confidence is > 50 I return the encode, else I return the default value
                if (confidence > 50) {
                    strcharset = cm.getName();
                    if (strcharset != null)
                    {
                        switch (strcharset)
                        {
                            case "windows-1252":
                                charset = StandardCharsets.ISO_8859_1;
                                break;
                            case "UTF_8":
                            case "UTF-8":
                                charset = StandardCharsets.UTF_8;
                                break;
                            case "UTF_16":
                            case "UTF-16":
                                charset = StandardCharsets.UTF_16;
                                break;
                            case "ISO_8859_1":
                                charset = StandardCharsets.ISO_8859_1;
                                break;
                            case "US_ASCII":
                            case "US-ASCII":
                                charset = StandardCharsets.US_ASCII;
                                break;
                            case "UTF_16BE":
                            case "UTF-16BE":
                                charset = StandardCharsets.UTF_16BE;
                                break;
                            case "UTF_16LE":
                            case "UTF-16LE":
                                charset = StandardCharsets.UTF_16LE;
                                break;
                            default:
                                charset = StandardCharsets.UTF_8;
                                unknowCharacterSetName = strcharset;
                                break;
                        }
                    }
                    else
                        unknowCharacterSetName = "cm returned null!";
                }
            }

            return result.toString(charset);
        }
    }

    private InputStream urlToInputStream(URL url, Map<String, String> args) {
        HttpURLConnection con = null;
        InputStream inputStream = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            if (args != null) {
                for (Map.Entry<String, String> e : args.entrySet()) {
                    con.setRequestProperty(e.getKey(), e.getValue());
                }
            }
            con.connect();
            int responseCode = con.getResponseCode();
            /* By default the connection will follow redirects. The following
             * block is only entered if the implementation of HttpURLConnection
             * does not perform the redirect. The exact behavior depends to
             * the actual implementation (e.g. sun.net).
             * !!! Attention: This block allows the connection to
             * switch protocols (e.g. HTTP to HTTPS), which is <b>not</b>
             * default behavior. See: https://stackoverflow.com/questions/1884230
             * for more info!!!
             */
            if (responseCode < 400 && responseCode > 299) {
                String redirectUrl = con.getHeaderField("Location");
                try {
                    URL newUrl = new URL(redirectUrl);
                    return urlToInputStream(newUrl, args);
                } catch (MalformedURLException e) {
                    URL newUrl = new URL(url.getProtocol() + "://" + url.getHost() + redirectUrl);
                    return urlToInputStream(newUrl, args);
                }
            }
            /*!!!!!*/

            inputStream = con.getInputStream();
            return inputStream;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
