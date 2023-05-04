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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PatTraite extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pat_traite);
		
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		
		String numMed=extras.getString("numMed");
		String nom=extras.getString("nom");
		String taux=extras.getString("taux");
		
		EditText a=(EditText)findViewById(R.id.numMedTR);
		EditText b=(EditText)findViewById(R.id.nomTR);
		EditText c=(EditText)findViewById(R.id.tauxTR);
		a.setText(numMed);
		b.setText(nom);
		c.setText(taux);
		new SendGetRequest().execute(numMed);
	}
	public class SendGetRequest extends AsyncTask<String, Void, String> {
		protected void onPreExecute(){}
		@Override
		protected String doInBackground(String... arg0) {
			String result="";
			try{ 
				URL url = new URL("http://10.0.2.2:8080/restService/rest/prestation/patient/"+arg0[0]);
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

			ListView vue = (ListView) findViewById(R.id.ListePatTraite);
			TextView g=(TextView)findViewById(R.id.total);
			final List<HashMap<String, String>> donnees = new ArrayList<HashMap<String, String>>();
			int somme=0;
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
					String traite=json_data.getString("traite");
					String prestation=json_data.getString("prestation");
					element= new HashMap<String, String>();
					
					element.put("numPat",numPat);
					element.put("nom",nom);
					element.put("adresse", adresse);
					element.put("traite",traite);
					element.put("prestation", prestation);
					somme=somme+Integer.parseInt(prestation);
					donnees.add(element);
					
					//donnees.add(taux_j);
				}
			}
			catch(Exception e) {
				
			}
			ListAdapter adapter = new SimpleAdapter(PatTraite.this,donnees,R.layout.item_traite,new String[] {"numPat","nom","adresse","traite","prestation"},
					new int[] {R.id.te1,R.id.te2,R.id.te3,R.id.te4,R.id.te5});
			vue.setAdapter(adapter);
			String s=somme+"";
			g.setText(s);
			//g.setText(898);
			/*vue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				
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
			});*/
			
		}
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
}
