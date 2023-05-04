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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DetailMedActivity extends Activity {

	private final static int IDENTIFIANT_BOITE_UN = 0;
	private String numMedTraite="";
	private String nomTraite="";
	private String tauxTraite="";
	private OnClickListener clickToSupprimer=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//setContentView(R.layout.activity_main);
			//showDialog(IDENTIFIANT_BOITE_UN);
			Builder box = new AlertDialog.Builder(DetailMedActivity.this);
			box.setTitle("Vouez vous supprimer ce medecin ?");
			box.setPositiveButton("Oui",supprimer);
			box.setNegativeButton("Non",annuler);
			box.show();
		}
		
	};
	private OnClickListener clickToModifier=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//setContentView(R.layout.activity_main);
			//showDialog(IDENTIFIANT_BOITE_UN);
			new SendUpdateRequest().execute();
		}
		
	};
	private OnClickListener clickToPatTraite=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent=new Intent(DetailMedActivity.this,PatTraite.class);
			intent.putExtra("numMed",numMedTraite);
			intent.putExtra("nom",nomTraite);
			intent.putExtra("taux",tauxTraite);
			startActivity(intent);
		}
		
	};
	
	 DialogInterface.OnClickListener supprimer=new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			new SendDeleteRequest().execute();
		}
			
	};
	DialogInterface.OnClickListener annuler=new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(DetailMedActivity.this,Medecin.class);
			startActivity(intent);
		}
			
	};
	public class SendUpdateRequest extends AsyncTask<String, Void, String> {
		protected void onPreExecute(){}
		protected String doInBackground(String... arg0) {
			try {
				EditText a=(EditText)findViewById(R.id.numMedEdit2);
				EditText b=(EditText)findViewById(R.id.nomMedEdit2);
				EditText c=(EditText)findViewById(R.id.taux_jEdit2);
				TextView d=(TextView)findViewById(R.id.numMedEditAncien);
				
				String numMed=a.getText().toString();
				String nom=b.getText().toString();
				String taux_j=c.getText().toString();
				String numMedAncien=d.getText().toString();
				
				URL url = new URL("http://10.0.2.2:8080/restService/rest/medecin/update");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(15000);
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				
				StringBuilder urlParameters=new StringBuilder("numMed=");
				urlParameters.append(URLEncoder.encode(numMed,"UTF-8"));
				urlParameters.append("&nom=");
				urlParameters.append(URLEncoder.encode(nom,"UTF-8"));
				urlParameters.append("&taux_j=");
				urlParameters.append(URLEncoder.encode(taux_j,"UTF-8"));
				urlParameters.append("&numMedAncien=");
				urlParameters.append(URLEncoder.encode(numMedAncien,"UTF-8"));
				
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
			Intent intent=new Intent(DetailMedActivity.this,Medecin.class);
			startActivity(intent);
			Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
		}
	}
	
	
	///classe supprimer
	public class SendDeleteRequest extends AsyncTask<String, Void, String> {
		protected void onPreExecute(){}
		@Override
		protected String doInBackground(String... arg0) {
			String result="";
			TextView a=(TextView)findViewById(R.id.numMedEditAncien);
			String numMed=a.getText().toString();
			try{ 
				URL url = new URL("http://10.0.2.2:8080/restService/rest/medecin/delete/"+numMed);
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
			
			Intent intent=new Intent(DetailMedActivity.this,Medecin.class);
			startActivity(intent);
			Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_med);
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		
		String numMed=extras.getString("numMed");
		String nom=extras.getString("nom");
		String taux_j=extras.getString("taux_j");
		numMedTraite=extras.getString("numMed");
		nomTraite=extras.getString("nom");
		tauxTraite=extras.getString("taux_j");
		
		EditText a=(EditText)findViewById(R.id.numMedEdit2);
		EditText b=(EditText)findViewById(R.id.nomMedEdit2);
		EditText c=(EditText)findViewById(R.id.taux_jEdit2);
		TextView d=(TextView)findViewById(R.id.numMedEditAncien);
		Button sup=(Button)findViewById(R.id.supprimerMed);
		Button mod=(Button)findViewById(R.id.modifierMed);
		Button patTraite=(Button)findViewById(R.id.patTraite);
		a.setText(numMed);
		b.setText(nom);
		c.setText(taux_j);
		d.setText(numMed);
		patTraite.setOnClickListener(clickToPatTraite);
		mod.setOnClickListener(clickToModifier);
		sup.setOnClickListener(clickToSupprimer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	////////////////////////////////BOITE DE DIALOGUE///////////////////////////////////////
	/*@Override
	public Dialog onCreateDialog(int identifiant) {
		Builder box = null;
		switch(identifiant) {
			case IDENTIFIANT_BOITE_UN :
				box = new AlertDialog.Builder(this);
				box.setTitle("Vouez vous supprimer ce medecin ?s");
				
				break;
		}
		return box;
	}*/
	
	////////////////////////////////////////////////////////////////////////////////////////

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
