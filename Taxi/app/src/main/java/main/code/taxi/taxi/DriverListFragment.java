package main.code.taxi.taxi;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by xytr3k on 2/6/15.
 */
public class DriverListFragment extends ListFragment {

    ArrayList<Drivers> drivers;
    TextView nameTextView = (TextView)getActivity().findViewById(R.id.name);
    TextView seatsTextView = (TextView)getActivity().findViewById(R.id.seats);
    TextView distanceTextView = (TextView)getActivity().findViewById(R.id.distance);
    TextView numberPlateTextView = (TextView)getActivity().findViewById(R.id.number_plate);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        test();

        CrimeAdapter adapter = new CrimeAdapter(drivers);
        setListAdapter(adapter);
    }


    private class CrimeAdapter extends ArrayAdapter<Drivers> {
        public CrimeAdapter(ArrayList<Drivers> crimes) {
            super(getActivity(), 0, drivers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_fragment, null);
            }
            // Configure the view for this Crime
            Drivers d = getItem(position);

            nameTextView.setText("Name: " + d.getName());
            seatsTextView.setText("Seats: "+ d.getSeats());
            distanceTextView.setText("Distance: " + d.getDistance());
            numberPlateTextView.setText("Number Plate: " + d.getNumber_pate());
            return convertView;
        }
    }

    public void test(){

        drivers = new ArrayList<Drivers>();

        Drivers c = new Drivers();
        c.setName("Ricardo");
        c.setSeats("4");
        c.setDistance("150m");
        c.setNumber_pate("PAY1526");

        drivers.add(c);

        c = new Drivers();
        c.setName("Badooh");
        c.setSeats("8");
        c.setDistance("250m");
        c.setNumber_pate("PAC1526");

        drivers.add(c);

        c = new Drivers();
        c.setName("Jevon");
        c.setSeats("12");
        c.setDistance("300m");
        c.setNumber_pate("PBY1526");

    }



}
