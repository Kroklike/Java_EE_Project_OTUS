package ru.otus.akn.project.soap;

import com.sun.xml.internal.ws.util.ByteArrayDataSource;
import org.json.JSONArray;

import javax.activation.DataSource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static javax.xml.ws.handler.MessageContext.HTTP_REQUEST_METHOD;

public class ServiceProviderWithDataSource {

    private static final String GET_HTTP_METHOD = "GET";
    private final String URLAddress;
    private final WebServiceContext webServiceContext;

    public ServiceProviderWithDataSource(String URLAddress, WebServiceContext webServiceContext) {
        this.URLAddress = URLAddress;
        this.webServiceContext = webServiceContext;
    }

    public DataSource invoke(DataSource request) {
        MessageContext msgCxt = webServiceContext.getMessageContext();
        String httpMethod = (String) msgCxt.get(HTTP_REQUEST_METHOD);

        if (httpMethod.equalsIgnoreCase(GET_HTTP_METHOD)) {
            return doGet();
        }
        throw new UnsupportedOperationException("Only get request is available");
    }

    private DataSource doGet() {
        StringBuffer text = new StringBuffer();

        try {
            URL url = new URL(URLAddress);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(GET_HTTP_METHOD);
            urlConnection.connect();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bReader.readLine()) != null) {
                text = text.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray json = new JSONArray(text.toString());
        return new ByteArrayDataSource(json.toString().getBytes(), "application/json");
    }
}
