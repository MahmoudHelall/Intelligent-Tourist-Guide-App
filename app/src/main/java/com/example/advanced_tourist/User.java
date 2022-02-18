package com.example.advanced_tourist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class User extends AppCompatActivity {
    ListView listViewData;
    Button d;
    ArrayAdapter<String> adapter;
    String[]choices={"Pyramids","Museum","Temples","Castles","Beach","Diving","Fishing","Islamic Places","Coptic","Jewish Places","Healing & Relax","Treatment Places"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        d=findViewById(R.id.done);
        listViewData=findViewById(R.id.listview_data);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,choices);
        listViewData.setAdapter(adapter);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalChecked = 0;
                String itemselected="Selected items :\n";
                for(int i=0;i<listViewData.getCount();i++)
                {
                    if(listViewData.isItemChecked(i)){
                        itemselected+=listViewData.getItemAtPosition(i)+"\n";
                        totalChecked++;
                    }
                }
                if(totalChecked==0)
                {
                    Toast.makeText(User.this,"No Items Selected!!?",Toast.LENGTH_LONG).show();
                }
                else if (totalChecked > 4)
                {
                    Toast.makeText(User.this,"Cant select more than four items !!?",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(User.this, itemselected, Toast.LENGTH_LONG).show();
                }
            }

        });
    }
}