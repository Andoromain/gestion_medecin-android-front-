package com.example.projetvrai;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.projetvrai.R;
import com.example.projetvrai.Medecin.SendGetRequest;
import com.example.projetvrai.R.id;
import com.example.projetvrai.R.layout;
import com.example.projetvrai.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PatientActivity extends Activity {
	
	private OnClickListener clickToAjoutPat=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(PatientActivity.this,Ajouter_Patient.class);
			startActivity(intent);
			}
		};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient);
		new SendGetRequest().execute();
		Button a=(Button)findViewById(R.id.ajoutPat);
		a.setOnClickListener(clickToAjoutPat);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	
	public class SendGetRequest extends AsyncTask<String, Void, String> {
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

			ListView vue = (ListView) findViewById(R.id.ListPatient);
			final List<HashMap<String, String>> donnees = new ArrayList<HashMap<String, String>>();
			//List<String> donnees = new ArrayList<String>();
			try {
				JSONObject json_data=null;
				HashMap element ;
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++){
					json_data = jArray.getJSONObject(i);
					String numPat=json_data.getString("numPat");
					String nom=json_data.getString("nom");
					String adresse=json_data.getString("adresse");
					element= new HashMap<String, String>();
					
					element.put("numPat",numPat);
					element.put("nom",nom);
					element.put("adresse", adresse);
					donnees.add(element);
					//donnees.add(taux_j);
				}
			}
			catch(Exception e) {
				
			}
			ListAdapter adapter = new SimpleAdapter(PatientActivity.this,donnees,R.layout.item,new String[] {"numPat","nom","adresse"},
					new int[] {R.id.text1,R.id.text2,R.id.text3});
			vue.setAdapter(adapter);
			vue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(PatientActivity.this,DetailPatActivity.class);
					intent.putExtra("numPat",donnees.get(arg2).get("numPat"));
					intent.putExtra("nom",donnees.get(arg2).get("nom"));
					intent.putExtra("adresse",donnees.get(arg2).get("adresse"));
					startActivity(intent);
					//Toast.makeText(getApplicationContext(),donnees.get(arg2).get("numMed"),Toast.LENGTH_LONG).show();
				}
			});
			
		}
	}

}
