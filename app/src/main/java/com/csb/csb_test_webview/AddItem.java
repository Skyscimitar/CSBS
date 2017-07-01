package com.csb.csb_test_webview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddItem extends AppCompatActivity {
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Bundle bundle = getIntent().getExtras();
        token = bundle.getString("token");
        final EditText description = (EditText) findViewById(R.id.item_description);
        final EditText price = (EditText) findViewById(R.id.item_price);
        final CheckBox offer = (CheckBox) findViewById(R.id.item_offer);
        Button button = (Button) findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(offer.isChecked()){
                    DataSender ds = new DataSender(description.getText().toString(), price.getText().toString(), "typeItem=1", token );
                    ds.execute();
                }else{
                    DataSender ds = new DataSender(description.getText().toString(), price.getText().toString(), "typeItem=2", token );
                    ds.execute();
                }

        }
    });
    }
}
