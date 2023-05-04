package com.example.projetvrai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import com.example.projetvrai.DetailMedActivity.SendDeleteRequest;
import com.example.projetvrai.DetailMedActivity.SendUpdateRequest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DetailPatActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_pat);
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		String numPat=extras.getString("numPat");
		String nom=extras.getString("nom");
		String adresse=extras.getString("adresse");
		
		EditText a=(EditText)findViewById(R.id.numMedEdit2);
		EditText b=(EditText)findViewById(R.id.nomMedEdit2);
		EditText c=(EditText)findViewById(R.id.adresseP);
		TextView d=(TextView)findViewById(R.id.numPatAncien);
		Button sup=(Button)findViewById(R.id.supprimerMed);
		Button mod=(Button)findViewById(R.id.modifierMed);
		a.setText(numPat);
		b.setText(nom);
		c.setText(adresse);
		d.setText(numPat);
		mod.setOnClickListener(clickToModifier);
		sup.setOnClickListener(clickToSupprimer);
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
	private final static int IDENTIFIANT_BOITE_UN = 0;
	private OnClickListener clickToSupprimer=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//setContentView(R.layout.activity_main);
			//showDialog(IDENTIFIANT_BOITE_UN);
			Builder box = new AlertDialog.Builder(DetailPatActivity.this);
			box.setTitle("Vouez vous supprimer ce patient ?");
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
			Intent intent=new Intent(DetailPatActivity.this,PatientActivity.class);
			startActivity(intent);
		}
			
	};
	public class SendUpdateRequest extends AsyncTask<String, Void, String> {
		protected void onPreExecute(){}
		protected String doInBackground(String... arg0) {
			try {
				EditText a=(EditText)findViewById(R.id.numMedEdit2);
				EditText b=(EditText)findViewById(R.id.nomMedEdit2);
				EditText c=(EditText)findViewById(R.id.adresseP);
				TextView d=(TextView)findViewById(R.id.numPatAncien);
				
				String numPat=a.getText().toString();
				String nom=b.getText().toString();
				String adresse=c.getText().toString();
				String numPatAncien=d.getText().toString();
				
				URL url = new URL("http://10.0.2.2:8080/restService/rest/patient/update");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(15000);
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				
				StringBuilder urlParameters=new StringBuilder("numPat=");
				urlParameters.append(URLEncoder.encode(numPat,"UTF-8"));
				urlParameters.append("&nom=");
				urlParameters.append(URLEncoder.encode(nom,"UTF-8"));
				urlParameters.append("&adresse=");
				urlParameters.append(URLEncoder.encode(adresse,"UTF-8"));
				urlParameters.append("&numPatAncien=");
				urlParameters.append(URLEncoder.encode(numPatAncien,"UTF-8"));
				
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
			Intent intent=new Intent(DetailPatActivity.this,PatientActivity.class);
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
			TextView a=(TextView)findViewById(R.id.numPatAncien);
			String numPat=a.getText().toString();
			try{ 
				URL url = new URL("http://10.0.2.2:8080/restService/rest/patient/delete/"+numPat);
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
			
			Intent intent=new Intent(DetailPatActivity.this,PatientActivity.class);
			startActivity(intent);
			Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
		}
	}
	
}
