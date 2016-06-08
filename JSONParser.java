package com.sepedakampus.ipb.sepedakampusipb;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by RIZQI on 12/7/2015.
 */
public class JSONParser {
    String url,reqMethod;
    int timeout;

    public JSONParser(String _url, String _reqMethod, int _timeout){
        this.url = _url;
        this.timeout = _timeout;
        this.reqMethod = _reqMethod;
    }

    public void setJSON(String _url, String _reqMethod, int _timeout){
        this.url = _url;
        this.timeout = _timeout;
        this.reqMethod = _reqMethod;
    }

    public String getUrl(){
        return this.url;
    }

    public int getTimeout(){
        return this.timeout;
    }

    public String getReqMethod(){return this.reqMethod;}

    public String getJSON(){
        HttpURLConnection c = null;
        try{
            URL u = new URL(this.url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod(reqMethod);
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append((line+"\n"));
                    }
                    br.close();
                    return sb.toString();

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(c!=null){
                try {
                    c.disconnect();
                }
                catch (Exception e){

                }
            }
        }
        return null;
    }

    public String getJsonWithQuery(HashMap<String, String> params){
        // Make iterator for postDataParams
        Set set = params.entrySet();
        Iterator i = set.iterator();

        //builder for Query
        Uri.Builder builder = new Uri.Builder();

        //take query
        while(i.hasNext()){
            Map.Entry mhsQuery = (Map.Entry)i.next();
            builder.appendQueryParameter(mhsQuery.getKey().toString(),mhsQuery.getValue().toString());
            Log.d("Parse Map", mhsQuery.getKey().toString() + " " + mhsQuery.getValue().toString());
        }
        String query = builder.build().getEncodedQuery();

        HttpURLConnection c = null;
        String newUrl = url.concat("?"+query);
        Log.d("Query",query);

        try{
            URL u = new URL(newUrl);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod(reqMethod);
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status){
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = br.readLine()) != null){
                        sb.append((line+"\n"));
                    }
                    br.close();
                    return sb.toString();

            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(c!=null){
                try {
                    c.disconnect();
                }
                catch (Exception e){

                }
            }
        }
        return null;
    }
    public String performPostCall(HashMap<String,String> postDataParams){
        String response ="";
        try {
            URL u = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setReadTimeout(this.timeout);
            conn.setConnectTimeout(this.timeout);
            conn.setRequestMethod(this.reqMethod);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Make iterator for postDataParams
            Set set = postDataParams.entrySet();
            Iterator i = set.iterator();

            //builder for Query
            Uri.Builder builder = new Uri.Builder();

            //take query
            while(i.hasNext()){
                Map.Entry mhsQuery = (Map.Entry)i.next();
                builder.appendQueryParameter(mhsQuery.getKey().toString(),mhsQuery.getValue().toString());
                Log.d("Parse Map >>", mhsQuery.getKey().toString() + " " + mhsQuery.getValue().toString());
            }
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            Log.d("Query >>", query);
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            Log.d("ResponseCode ", String.valueOf(responseCode));

            if(responseCode == HttpURLConnection.HTTP_OK){
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
