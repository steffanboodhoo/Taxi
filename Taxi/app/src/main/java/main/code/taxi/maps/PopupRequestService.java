package main.code.taxi.maps;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import main.code.taxi.help.ClickManager;
import main.code.taxi.taxi.R;

/**
 * Created by Steffan on 05/02/2015.
 */
public class PopupRequestService extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_request_service, container, false);
        setupView();
        return view;
    }

    private void setupView() {
        Button btnK=(Button)view.findViewById(R.id.btn_request_service_okay);
        Button btnNo=(Button)view.findViewById(R.id.btn_request_service_cancel);
        ClickManager c = new ClickManager(getActivity());
        btnK.setOnClickListener(c);
        btnNo.setOnClickListener(c);
    }

}
