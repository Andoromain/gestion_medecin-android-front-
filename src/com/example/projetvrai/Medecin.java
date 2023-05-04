package com.example.projetvrai;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class Medecin extends Activity {
	ListView liste;
	TextView b;
	
		private OnClickListener clickToAjoutMed=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(Medecin.this,Ajouter_medecin.class);
			startActivity(intent);
			}
		};
		
		private OnClickListener clickToPrestation=new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Medecin.this,Prestation.class);
				startActivity(intent);
				}
			};

		public boolean isNetworkAvailable() {
			ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
			return true; }
			return false;
		}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medecin);
		
		Button a=(Button)findViewById(R.id.ajoutPat);
		a.setOnClickListener(clickToAjoutMed);
		
		Button d=(Button)findViewById(R.id.buttonPrestation);
		d.setOnClickListener(clickToPrestation);
	
		InputStream is = null;
		b=(TextView)findViewById(R.id.text1);
		new SendGetRequest().execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public class SendGetRequest extends AsyncTask<String, Void, String> {
		protected void onPreExecute(){}
		@Override
		protected String doInBackground(String... arg0) {
			String result="";
			try{ 
				URL url = new URL("http://10.0.2.2:8080/restService/rest/medecin/liste");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(15000);
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("GET");
				
				int responseCode=conn.getResponseCode();
				if (responseCode == HttpsURLConnection.HTTP_OK) {
					
					StringBuffer sb = new StringBuffer("");
					BufferedReader in=new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
					String line="";
					while((line = in.readLine()) != null) {
						sb.append(line);
						break;
					}
					in.close();
					result=sb.toString();
					
				}
			}
			catch(Exception e) {
				result= e.toString();
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {	

			ListView vue = (ListView) findViewById(R.id.ListPatient);
			final List<HashMap<String, String>> donnees = new ArrayList<HashMap<String, String>>();
			//List<String> donnees = new ArrayList<String>();
			try {
				JSONObject json_data=null;
				HashMap element ;
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++){
					json_data = jArray.getJSONObject(i);
					String numMed=json_data.getString("numMed");
					String nom=json_data.getString("nom");
					String taux_j=json_data.getString("taux_j");
					element= new HashMap<String, String>();
					
					element.put("numMed",numMed);
					element.put("nom",nom);
					element.put("taux_j", taux_j);
					donnees.add(element);
					//donnees.add(taux_j);
				}
			}
			catch(Exception e) {
				
			}
			ListAdapter adapter = new SimpleAdapter(Medecin.this,donnees,R.layout.item,new String[] {"numMed","nom","taux_j"},
					new int[] {R.id.text1,R.id.text2,R.id.text3});
			vue.setAdapter(adapter);
			vue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(Medecin.this,DetailMedActivity.class);
					intent.putExtra("numMed",donnees.get(arg2).get("numMed"));
					intent.putExtra("nom",donnees.get(arg2).get("nom"));
					intent.putExtra("taux_j",donnees.get(arg2).get("taux_j"));
					startActivity(intent);
					//Toast.makeText(getApplicationContext(),donnees.get(arg2).get("numMed"),Toast.LENGTH_LONG).show();
				}
			});
			
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.medecin) {
			Intent intent=new Intent(this,Medecin.class);
			startActivity(intent);
		}else if(id==R.id.home) {
			Intent intent=new Intent(this,HomeActivity.class);
			startActivity(intent);
		}else if(id==R.id.patient) {
			Intent intent=new Intent(this,PatientActivity.class);
			startActivity(intent);
		}else if(id==R.id.logout) {
			Intent intent=new Intent(this,LoginActivity.class);
			startActivity(intent);
		}else if(id==R.id.quitter) {
			
		}else if(id==R.id.traitement) {
			Intent intent=new Intent(this,Traitement.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
