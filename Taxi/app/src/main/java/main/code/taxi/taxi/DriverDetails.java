package main.code.taxi.taxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import main.code.taxi.utils.PreferenceManager;
import main.code.taxi.utils.Utils;


public class DriverDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);
        setup();
    }

    private void setup(){
        final EditText username=(EditText)findViewById(R.id.et_driver_name);
        final EditText numberplate=(EditText)findViewById(R.id.et_driver_numberPlate);
        Button btn=(Button)findViewById(R.id.btn_driver_details_done);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.btn_driver_details_done){
                    String unmae=username.getText().toString();
                    String numberPlate=numberplate.getText().toString();
                    Toast.makeText(getApplicationContext(),"saved "+unmae+" "+numberPlate,Toast.LENGTH_SHORT).show();
                    PreferenceManager.saveNumberPlate(DriverDetails.this,numberPlate);
                    PreferenceManager.saveUserName(DriverDetails.this, unmae);
                    PreferenceManager.saveUserType(DriverDetails.this, Utils.userTypeDriver);
                    startActivity(new Intent(getApplicationContext(),Main.class));
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver_details, menu);
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
