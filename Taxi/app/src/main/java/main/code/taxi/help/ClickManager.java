package main.code.taxi.help;

import android.app.Activity;
import android.view.View;

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
      /*  if(v.getId()== R.id.btn_request_service_okay){
            //do something
        }else if(v.getId()==R.id.btn_request_service_cancel){
            //do something
        }*/
    }
}
