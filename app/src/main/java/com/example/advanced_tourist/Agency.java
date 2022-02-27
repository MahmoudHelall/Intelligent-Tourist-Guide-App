package com.example.advanced_tourist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Agency extends AppCompatActivity {
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView locationLong;
    TextView locationLat;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    FirebaseDatabase database;
    TextView tripTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mAuth = FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference();
        tripTitle=(TextView)findViewById(R.id.tripTitle);
        TextView description=(TextView)findViewById(R.id.descripition);
        TextView startFrom=(TextView)findViewById(R.id.startFromTime);
        TextView totime=(TextView)findViewById(R.id.endTime);
        TextView pickUp=(TextView)findViewById(R.id.pickupPlaceInput);
        TextView dropOff=(TextView)findViewById(R.id.dropOffPlaceinput);
        TextView languages=(TextView)findViewById(R.id.languagesTourist);
        ListView languagesList=(ListView)findViewById(R.id.LanguagesList);
        TextView priceSingle=(TextView)findViewById(R.id.singlePrice);
        TextView priceDouble=(TextView) findViewById(R.id.price2P);
        TextView price3Pers=(TextView) findViewById(R.id.price3p);
        TextView price6Pers=(TextView) findViewById(R.id.price6P);
        TextView priceChildr16=(TextView) findViewById(R.id.priceChildren16);
        TextView priceChildr6=(TextView) findViewById(R.id.priceChildren6);
        locationLat = (TextView) findViewById(R.id.latitudeInput);
        locationLong=(TextView) findViewById(R.id.longitudeInput);
        Button getLocation=(Button) findViewById(R.id.getLocationButton);
        TextView placesIn=(TextView) findViewById(R.id.placestextInput);
        Button addPlaceButt=(Button) findViewById(R.id.addPlacesButton);
        ListView placesListView=(ListView) findViewById(R.id.placesList);
        TextView tripType=(TextView) findViewById(R.id.tripType);
        Button addTripButt=(Button) findViewById(R.id.addTrip);
        ArrayAdapter <String>langAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1);
        ArrayAdapter<String>placesAdapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        languagesList.setAdapter(langAdapter);
        placesListView.setAdapter(placesAdapter);
        Button addImage=(Button) findViewById(R.id.addImageButton);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,3);
            }
        });
        languages.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                langAdapter.add(languages.getText().toString());
                languages.setText("");
            }
        });
        languagesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });
        addPlaceButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesAdapter.add(placesIn.getText().toString());
                placesIn.setText("");
            }
        });
        placesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });
        addTripButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ref.child("Agency").addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       tripTitle.setText(snapshot.getValue(String.class));
                       Toast.makeText(Agency.this, "Trip Title is Added", Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }
        });
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoc();
            }
        });
    }
    private void getLoc() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(Agency.this, Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        locationLong.setText(String.valueOf(location.getLongitude()));
                        locationLat.setText(String.valueOf(location.getLatitude()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data!=null){
            Uri selectedImage=data.getData();
            ImageView imageView=(ImageView) findViewById(R.id.imageView);
            imageView.setImageURI(selectedImage);
        }
    }
}