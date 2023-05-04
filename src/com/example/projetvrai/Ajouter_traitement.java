package com.example.projetvrai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.projetvrai.Ajouter_Patient.SendPostRequest;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Toast;

public class Ajouter_traitement extends Activity {
	private Spinner liste = null;
	
	private OnClickListener clickToAjout=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
				
				//Toast.makeText(getApplicationContext(),""+taux_j,Toast.LENGTH_LONG).show();	
				new SendPostRequest().execute();
				
			}
		};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ajouter_traitement);
		new ListeDesMedecins().execute();
		new ListeDesPatients().execute();
		Button a=(Button)findViewById(R.id.ajouterTrait);
		a.setOnClickListener(clickToAjout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
	
	public class ListeDesMedecins extends AsyncTask<String, Void, String> {
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
			liste = (Spinner) findViewById(R.id.spin1);
			List<String> exemple = new ArrayList<String>();
			
			try {
				JSONObject json_data=null;
				HashMap element ;
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++){
					json_data = jArray.getJSONObject(i);
					exemple.add(json_data.getString("numMed"));
				}
			}catch(Exception e) {
				
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(Ajouter_traitement.this,android.R.layout.simple_spinner_item, exemple);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			liste.setAdapter(adapter);
			
		}
	}

	public class ListeDesPatients extends AsyncTask<String, Void, String> {
		protected void onPreExecute(){}
		@Override
		protected String doInBackground(String... arg0) {
			String result="";
			try{ 
				URL url = new URL("http://10.0.2.2:8080/restService/rest/patient/liste");
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

			liste = (Spinner) findViewById(R.id.spin2);
			List<String> exemple = new ArrayList<String>();
			
			try {
				JSONObject json_data=null;
				HashMap element ;
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++){
					json_data = jArray.getJSONObject(i);
					exemple.add(json_data.getString("numPat"));
				}
			}catch(Exception e) {
				
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(Ajouter_traitement.this,android.R.layout.simple_spinner_item, exemple);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			liste.setAdapter(adapter);
			
		}
	}

	//method post ajout	
			public class SendPostRequest extends AsyncTask<String, Void, String> {
				protected void onPreExecute(){}
				protected String doInBackground(String... arg0) {
					try {
						Spinner a=(Spinner)findViewById(R.id.spin1);
						Spinner b=(Spinner)findViewById(R.id.spin2);
						EditText c=(EditText)findViewById(R.id.nbjour);
						
						String numMed=a.getSelectedItem().toString();
						String numPat=b.getSelectedItem().toString();
						String nbjour=c.getText().toString();
						
						URL url = new URL("http://10.0.2.2:8080/restService/rest/traitement/add");
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setReadTimeout(15000);
						conn.setConnectTimeout(15000);
						conn.setRequestMethod("POST");
						conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
						
						StringBuilder urlParameters=new StringBuilder("numMed=");
						urlParameters.append(URLEncoder.encode(numMed,"UTF-8"));
						urlParameters.append("&numPat=");
						urlParameters.append(URLEncoder.encode(numPat,"UTF-8"));
						urlParameters.append("&nbjour=");
						urlParameters.append(URLEncoder.encode(nbjour,"UTF-8"));
						
						conn.setDoInput(true);
						conn.setDoOutput(true);
						OutputStream os = conn.getOutputStream();
						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
						// writer.write (urlParameters);
						writer.write (urlParameters.toString());
						writer.flush();
						writer.close();
						os.close();
						int responseCode=conn.getResponseCode();
						if (responseCode == HttpsURLConnection.HTTP_OK) {
							StringBuffer sb = new StringBuffer("");
							BufferedReader in=new BufferedReader(new
							InputStreamReader(conn.getInputStream()));
							
							String line="";
							while((line = in.readLine()) != null) {
								sb.append(line);
								break; 
							}
							
							in.close();
							return sb.toString();
						}
						else { 
							return new String("false : "+responseCode);
						}
					}catch(Exception e){
						Log.e("tagconnect"," ERREUR RESULTAT.... ");
						return new String("Exception....: " + e.getMessage());
					}
				}
				@Override
				protected void onPostExecute(String result) {
					Intent intent=new Intent(Ajouter_traitement.this,Traitement.class);
					startActivity(intent);
					Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
				}
			}
			
}
