package main.code.taxi.maps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import main.code.taxi.taxi.R;

/**
 * Created by Steffan on 05/02/2015.
 */
public class PopupRequestService extends DialogFragment{
    View view;


    /*private void setupView() {
        Button btnK=(Button)view.findViewById(R.id.btn_request_service_okay);
        Button btnNo=(Button)view.findViewById(R.id.btn_request_service_cancel);
        ClickManager c = new ClickManager(getActivity());
        btnK.setOnClickListener(c);
        btnNo.setOnClickListener(c);
    }*/
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.fragment_request_service,null))
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"Job Accepted",Toast.LENGTH_SHORT).show();
                        PopupRequestService.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PopupRequestService.this.getDialog().cancel();
                    }
                });


        return builder.create();
    }


}
