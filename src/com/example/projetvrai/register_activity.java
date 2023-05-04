package com.example.projetvrai;



import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class register_activity extends Activity{
	private OnClickListener clickButtons=new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			final String requete ="http://www.google.fr/search?q=Salut";
			Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(requete));
			startActivity(intent);
		}
		
	}; 
	private OnClickListener clickToLogin=new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(register_activity.this,LoginActivity.class);
			startActivity(intent);
			
		}
	};
			

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView text =new TextView(this);
		text.setText(R.string.app_name);
		//setContentView(text);
		
		setContentView(R.layout.activity_main);
		Button b=(Button)findViewById(R.id.enregistrerR);
		b.setOnClickListener(clickButtons);

		Button c=(Button)findViewById(R.id.LoginR);
		c.setOnClickListener(clickToLogin);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
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
