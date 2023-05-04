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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private OnClickListener clickToRegister=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			//setContentView(R.layout.activity_main);
			Intent intent=new Intent(LoginActivity.this,register_activity.class);
			startActivity(intent);
		}
		
	};
	
	private OnClickListener clickToSeConnecter=new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub	
			new SigninActivity().execute();
		}
		
	};
	
	public class SigninActivity extends AsyncTask<String, Void, String> {
		protected void onPreExecute(){}
		@Override
		protected String doInBackground(String... arg0) {
			String result="";
			EditText a=(EditText)findViewById(R.id.nomMedEdit2);
			EditText b=(EditText)findViewById(R.id.taux_jEdit);
			String username=a.getText().toString();
			String mdp=b.getText().toString();
			
			try{ 
				URL url = new URL("http://10.0.2.2:8080/restService/rest/prestation/login");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(15000);
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				
				StringBuilder urlParameters=new StringBuilder("username=");
				urlParameters.append(URLEncoder.encode(username,"UTF-8"));
				urlParameters.append("&mdp=");
				urlParameters.append(URLEncoder.encode(mdp,"UTF-8"));
				
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
			//Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
			if(result.equals("1")) {
				Toast.makeText(getApplicationContext(),"Accepted",Toast.LENGTH_LONG).show();
				Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
				startActivity(intent);
			}else if(result.equals("0")) {
				Toast.makeText(getApplicationContext(),"Refused",Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Button d=(Button)findViewById(R.id.clickToR);
		d.setOnClickListener(clickToRegister);
		
		Button f=(Button)findViewById(R.id.ajoutPatient);
		f.setOnClickListener(clickToSeConnecter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//if (id == R.id.action_settings) {
			//return true;
		//}
		return super.onOptionsItemSelected(item);
	}
}
