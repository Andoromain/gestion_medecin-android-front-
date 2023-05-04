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

import android.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;


public class HomeActivity extends Activity {

    private PieChartView chart;
    private PieChartData data;

    private boolean hasLabels = false;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
    private boolean hasCenterText1 = false;
    private boolean hasCenterText2 = false;
    private boolean isExploded = false;
    private boolean hasLabelForSelected = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		new BarChart().execute();
		
	
	}
	public class BarChart extends AsyncTask<String, Void, String> {
		protected void onPreExecute(){}
		@Override
		protected String doInBackground(String... arg0) {
			String result="";
			try{ 
				URL url = new URL("http://10.0.2.2:8080/restService/rest/prestation/etat");
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
			
			final List<HashMap<String, String>> donnees = new ArrayList<HashMap<String, String>>();
			int length=0;
			try {
				JSONObject json_data=null;
				JSONArray jArray = new JSONArray(result);
				length=jArray.length();
				int numSubcolumns = 4;
				int numColumns = length;
				// Column can have many stacked subcolumns, here I use 4 stacke subcolumn in each of 4 columns.
				List<Column> columns = new ArrayList<Column>();
				 List<SliceValue> values1 = new ArrayList<SliceValue>();
				List<SubcolumnValue> values;
				for(int i=0;i<jArray.length();i++){
					json_data = jArray.getJSONObject(i);
					String numMed=json_data.getString("numMed");
					String nom=json_data.getString("nom");
					String prestation=json_data.getString("prestation");
					
					values = new ArrayList<SubcolumnValue>();
					SubcolumnValue test=new SubcolumnValue((float)Integer.parseInt(prestation), ChartUtils.pickColor());
					test.setLabel(numMed);
					values.add(test);
					Column column = new Column(values);
					
					column.setHasLabels(true);
					
					column.setHasLabelsOnlyForSelected(hasLabelForSelected);
					columns.add(column);
					//donnees.add(taux_j);
					
					 SliceValue sliceValue = new SliceValue((float)Integer.parseInt(prestation), ChartUtils.pickColor());
					 sliceValue.setLabel(numMed);
		              values1.add(sliceValue);
				}
				
				ColumnChartData data1 = new ColumnChartData(columns);
				
				// Set stacked flag.
				data1.setStacked(true);
				
				Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                axisX.setName("Medecin");
                axisY.setName("Prestation");
                data1.setAxisXBottom(axisX);
                data1.setAxisYLeft(axisY);
                
				ColumnChartView chart1 = (ColumnChartView)findViewById(R.id.chart1);
				chart1.setColumnChartData(data1);
				
				
				
				
				  data = new PieChartData(values1);
		          data.setHasLabels(true);
		          data.setHasLabelsOutside(true);
		          data.setHasCenterCircle(hasCenterCircle);
		          chart=(PieChartView)findViewById(R.id.chart);
		          chart.setPieChartData(data);

			}
			catch(Exception e) {
				
			}
		
		////////////////////////////////////code bar//////////////////////////////////////////
	

			   //////////////////////////////////code Pie/////////////////////////////////////
		
	        
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
