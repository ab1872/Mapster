package server;

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

import explore.Cam;
import floorstructures.NearbyFloor;

public class Server {

    private static final String ns = null;

    public static String Download(long FloorID) {
        return "abcd";
    }

   /* public static NearbyFloor AskForBuildings(Cam camera){
        //(long BuildingIDg, long FloorIDg, LLPos centerg, Cam camerag);
        return new NearbyFloor();
    }*/

    // Source for the XML parser code: https://developer.android.com/training/basics/network-ops/xml
}