package com.popland.pop.complexxmlparser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
   // TextView TV;
    ImageView IV;
ListView LV;
    ArrayList<String> arrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TV =(TextView)findViewById(R.id.TV);
        IV = (ImageView)findViewById(R.id.IV);
        LV = (ListView)findViewById(R.id.LV);
        arrl = new ArrayList<String>();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new XMLParser().execute("http://www.cnet.com/rss/android-update/");
            }
        });
    }

    class XMLParser extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            return docNoiDung_Tu_URL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           // TV.setText(s);
            XMLDOMParser parser1 = new XMLDOMParser();
            Document document1 = parser1.getDocument(s);
            NodeList nodeList1 = document1.getElementsByTagName("description");
            String p ="";
            String newXML ="";
            String P ="";
            for(int i=1;i<nodeList1.getLength();i++){
              Node nodeDes = nodeList1.item(i);
              NodeList nodeListCData = nodeDes.getChildNodes();
                for(int j=0;j<nodeListCData.getLength();j++){
                     Node nodeCData = nodeListCData.item(0);
                     p = nodeCData.getNodeValue();
                     newXML = "<rss>"+p+"</rss>";
                     Document document2 = parser1.getDocument(newXML);
                     NodeList nodeList2 = document2.getElementsByTagName("rss");
                     for(int k=0;k<nodeList2.getLength();k++) {
                         Element element = (Element) nodeList2.item(k);
                         P = parser1.getValue(element,"rss");
                         P = P.replaceAll("&nbsp;"," ");
                             arrl.add(P);
                     }
                 }
            }
            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,arrl);
            LV.setAdapter(adapter);
        }
    }

    private static String docNoiDung_Tu_URL(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }

}
