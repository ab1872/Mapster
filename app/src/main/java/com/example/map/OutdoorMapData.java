package com.example.map;

import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class OutdoorMapData {

    public void GetData(double south, double west, double north, double east) {
        try {
            HashMap<String, String> params = new HashMap();

            params.put("data","<query type=\"node\"><has-kv k=\"name\" v=\"Bristol\"/></query><around radius=\"10\"/><print/>");
                    //"<osm-script timeout=\"900\" element-limit=\"1073\"><bbox-query s=\"51.15\" w=\"7.0\" n=\"51.35\" e=\"7.3\"/><print/></osm-script>");//"<osm-script timeout=\"900\" element-limit=\"107\"><bbox-query s=\"" + String.format("%.3f",south) + "\" w=\"" + String.format("%.3f",west) + "\" n=\""+ String.format("%.3f",north) +"\" e=\""+ String.format("%.3f",east) +"\"/><print/></osm-script>");
// http://overpass-api.de/query_form.html
            // https://wiki.openstreetmap.org/wiki/Overpass_API#Public_Overpass_API_instances

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
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                Log.d("test", "result from server: " + result.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        // END CODE
    }
}