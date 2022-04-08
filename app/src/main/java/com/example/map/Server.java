package com.example.map;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Xml;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Server {

    private static final String ns = null;

    public Drawable Download(long num) {
        String a = "abc" + String.valueOf(num);
        //return Drawable.createFromXml(Resources.getSystem(), );
        return Drawable.createFromPath("abc");
    }

    public void AskForBuildings(double south, double west, double north, double east) {
        try {
            HashMap<String, String> params = new HashMap();
            String queryText = "<union>"
            + "<bbox-query s=\"" + String.format("%.5f",south)  + "\" w=\"" +  String.format("%.5f",west) + "\" n=\"" +  String.format("%.5f",north)  + "\" e=\"" + String.format("%.5f",east)  + "\"/>"
            + "<recurse type=\"up\"/>"
            + "</union>"
            + "<print mode=\"meta\"/>";
            params.put("data",queryText);

            // SOURCE OF THIS HTTP-SENDING CODE @ https://riptutorial.com/android/example/12202/sending-an-http-post-request-with-parameters


            StringBuilder sbParams = new StringBuilder();
            int i = 0;
            for (String key : params.keySet()) {
                try {
                    if (i != 0) {
                        sbParams.append("&");
                    }
                    sbParams.append(key).append("=")
                            .append(URLEncoder.encode(params.get(key), "UTF-8"));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                i++;
            }

            String url = "https://lz4.overpass-api.de/api/interpreter";
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            conn.connect();

            String paramsString = sbParams.toString();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();

            try {
                /* InputStream in = new BufferedInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                new ByteArrayInputStream(result.toString().getBytes())
                //Log.d("test", "result from server: " + result.toString().length());
                */
                parse(conn.getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // END CODE
    }

    private List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return read(parser);
        } finally {
            in.close();
        }
    }

    private List read(XmlPullParser parser) throws XmlPullParserException, IOException{
        int count = 0;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("node")) {
                count += 1;
            } else {
                skip(parser);
            }
        }

        Log.d("Map Output", "Counted " + count + " tags");
        return new ArrayList();
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    // Source for the XML parser code: https://developer.android.com/training/basics/network-ops/xml
}