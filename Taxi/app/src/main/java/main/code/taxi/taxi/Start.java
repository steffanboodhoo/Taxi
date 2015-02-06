package main.code.taxi.taxi;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import main.code.taxi.utils.PreferenceManager;
import main.code.taxi.utils.Utils;


public class Start extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if(!PreferenceManager.getUserType(this).equals(Utils.unknownString)){
            startActivity(new Intent(getApplicationContext(),Main.class));
        }
        setup();
    }

    public void setup(){
        Button btn1=(Button)findViewById(R.id.btn_start_driver);
        Button btn2=(Button)findViewById(R.id.btn_start_passenger);

    }
    class Click implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.btn_start_driver){

            }else  if(v.getId()==R.id.btn_start_passenger){

            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
