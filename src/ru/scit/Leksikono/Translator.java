package ru.scit.Leksikono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Translator {
    private static final String HOST = "http://eoru.ru";
    private static final String VARIANT_PATH = "suggest/";
    private static final String TRANSLATION_PATH = "sercxo/";

    public List<String> getVariants(String input) {
        String url = HOST + "/" + VARIANT_PATH;
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("q", input));
        HttpResponse response = this.postData(url, data);
        return this.readVariants(response);
    }

    public String getTranslation(String word) {
        String url = HOST + "/" + TRANSLATION_PATH;
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        data.add(new BasicNameValuePair("v", word));

        data.add(new BasicNameValuePair("artid", "0"));
        data.add(new BasicNameValuePair("sercxu", "ek"));
        data.add(new BasicNameValuePair("Ek", "%CD%E0%E9%F2%E8"));
        HttpResponse response = this.postData(url, data);
        return this.readTranslation(response);
    }

    private HttpResponse postData(String url, List<NameValuePair> data) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(data));
            return httpClient.execute(httpPost);
        } catch(ClientProtocolException e) {
            return null;
        } catch(IOException e) {
            return null;
        }
    }

    private List<String> readVariants(HttpResponse response) {
        String line = "";
        List<String> variants = new ArrayList<String>();
        try {
            InputStream is = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while((line = br.readLine()) != null) {
                line = line.split("\\|")[0];
                variants.add(line);
            }
            return variants;
        } catch (IOException e) {
            return null;
        }
    }

    private String readTranslation(HttpResponse response) {
        String translation = "";
        String line = "";
        try {
            InputStream is = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "cp1251"));

            while((line = br.readLine()) != null) {
                translation += line;
            }
            return this.parseTranslation(translation);
        } catch (IOException e) {
            return null;
        }
    }

    private String parseTranslation(String htmlText) {
        String pattern = ".*<div id=\"content\">.*?(<strong>.*[.])<br /><br />";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(htmlText);

        if(m.find()) {
            return m.group(1);
        } else {
            return "";
        }
    }
}
