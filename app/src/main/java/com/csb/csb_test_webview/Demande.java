package com.csb.csb_test_webview;


import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.csb.csb_test_webview.R;

/**
 * Created by Danny on 31/05/2017.
 */

public class Demande extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offer_view, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditText medit = (EditText) getActivity().findViewById(R.id.editText2);
        final Datacollector dc = new Datacollector("&typeItem=2", getActivity(), medit.getText().toString());
        dc.execute();

    }
}