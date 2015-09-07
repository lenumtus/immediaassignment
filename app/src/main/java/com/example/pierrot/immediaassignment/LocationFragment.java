package com.example.pierrot.immediaassignment;

import android.content.Context;
import android.app.ListFragment;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.pierrot.immediaassignment.model.Venue;
import com.example.pierrot.immediaassignment.model.location;
import com.example.pierrot.immediaassignment.utils.LoadLocation;
import com.example.pierrot.immediaassignment.utils.LoadVenues;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends ListFragment implements
        LoadLocation.Callback {

    private List<location> mVenues;
    private VenueAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocationFragment() {}

    //called when the LoadVenues asynctask has returned with data
    //updates the list of venues being shown
    @Override
    public void onLocationLoaded(ArrayList<location> venues) {
        if (isAdded()) {
            mVenues = venues;
            if (mAdapter == null) {
                setListAdapter(new VenueAdapter(
                        getActivity(),
                        R.layout.venue_adapter_item,
                        mVenues));
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    //when an error has occurred in LoadVenues asynctask
    @Override
    public void onError(String errorMsg) {
        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    //update the distances for all venues listed

    public void updateDistances(Location newLocation) {
        if (mVenues != null) {
            for (location venue : mVenues) {
                venue.updateDistance(newLocation);
            }
            if (mAdapter != null)
                mAdapter.notifyDataSetChanged();
        }
    }

    private class VenueAdapter extends ArrayAdapter<location> {
        private int layoutId;

        public VenueAdapter(Context context, int resource, List<location> objects) {
            super(context, resource, objects);
            layoutId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(layoutId, null);
            }
            mVenues.get(position).populate(convertView);
            return convertView;
        }
    }
}
