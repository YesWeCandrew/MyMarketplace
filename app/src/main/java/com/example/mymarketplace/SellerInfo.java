package com.example.mymarketplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymarketplace.Entities.Sellers;
import com.example.mymarketplace.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SellerInfo extends AppCompatActivity implements OnMapReadyCallback  {

    private Sellers.Seller seller;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_info);

        seller = getIntent().getSerializableExtra("seller", Sellers.Seller.class);

        // Getting text views
        TextView textSellerTitle = (TextView) findViewById(R.id.textViewSellerTitle);
        TextView textSellerAddress = (TextView) findViewById(R.id.textViewSellerAddress);

        // Setting text values
        textSellerTitle.setText(seller.name);
        textSellerAddress.setText(seller.addressAsText);

        // Calling the mapOnReady function for the mapView.
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        // Add a marker at the sellers latitude and longitude
        LatLng sellerLocation = new LatLng(seller.latitude, seller.longitude);
        map.addMarker(new MarkerOptions().position(sellerLocation).title(seller.name).snippet(seller.addressAsText));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sellerLocation,13f));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}


