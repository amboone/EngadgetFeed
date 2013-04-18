package com.engagetfeed;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BrowseActivity extends ListActivity {
	
	ArrayList<String> headlines;
	ArrayList<String> stories;
	
	JSONObject results = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        try {
			results = new JSONObject(new LongRunningGetIO().execute().get());
		} catch (JSONException e1) {
			Log.d("error",e1.toString());
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			Log.d("error",e1.toString());
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			Log.d("error",e1.toString());
			e1.printStackTrace();
		}
	 // Initializing instance variables
	    headlines = new ArrayList<String>();
	    stories = new ArrayList<String>();
	    if (results != null) {
	    try {
			JSONArray entries = results.getJSONObject("responseData").getJSONObject("feed").getJSONArray("entries");
			for (int i = 0; i < entries.length(); i++) {
				headlines.add(entries.getJSONObject(i).getString("title"));
				stories.add(entries.getJSONObject(i).getString("content"));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("error",e.toString());
			e.printStackTrace();
		}
	    }
	    // Binding data
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, headlines);

	    setListAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_browse, menu);
        return true;
    }
    

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent i = new Intent(getApplicationContext(), ViewActivity.class);
    	i.putExtra("content", stories.get(position).toString());
    	i.putExtra("headline", headlines.get(position).toString());
    	startActivity(i);
    }

    private class LongRunningGetIO extends AsyncTask <Void, Void, String> {
    	
    	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
    		InputStream in = entity.getContent();
    		StringBuffer out = new StringBuffer();
    		int n = 1;
    		while (n > 0) {
    			byte[] b = new byte[4096];
    			n =  in.read(b);
    			if (n > 0) {
    				out.append(new String(b, 0, n));
    			}
    		}
    		return out.toString();
    	}
    	
    	@Override
    	protected String doInBackground(Void... params) {
    		HttpClient httpClient = new DefaultHttpClient();
    		HttpContext localContext = new BasicHttpContext();
    		HttpGet httpGet = new HttpGet("https://ajax.googleapis.com/ajax/services/feed/load?q=http://www.engadget.com/rss.xml&v=1.0&num=-1");
    		String text = null;
    		try {
    			HttpResponse response = httpClient.execute(httpGet, localContext);
    			HttpEntity entity = response.getEntity();
    			text = getASCIIContentFromEntity(entity);
    		} catch (Exception e) {
    			return e.getLocalizedMessage();
    		}
    		return text;
    	}
    	
    	
    }
    }
