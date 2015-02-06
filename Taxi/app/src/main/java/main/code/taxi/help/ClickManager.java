package main.code.taxi.help;

import android.app.Activity;
import android.view.View;

import main.code.taxi.taxi.Main;
import main.code.taxi.taxi.R;

/**
 * Created by Steffan on 06/02/2015.
 */
public class ClickManager implements View.OnClickListener{
    Activity activity;
    private ClickManager man;

    public ClickManager(Activity activity){
            this.activity=activity;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.action_a){
            ((Main)activity).markReady=true;
        }else if(v.getId()==R.id.action_b){
            //do something
        }else if(v.getId()==R.id.action_c){

        }
    }
}
