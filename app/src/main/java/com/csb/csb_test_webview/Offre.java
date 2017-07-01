package com.csb.csb_test_webview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danny on 07/06/2017.
 */

public class Offre extends ListFragment {
    comunicate cm;

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
        final Datacollector dc = new Datacollector("&typeItem=1", getActivity(), medit.getText().toString());
        dc.execute();

    }

}
