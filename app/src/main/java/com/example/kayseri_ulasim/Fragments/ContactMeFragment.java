package com.example.kayseri_ulasim.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.kayseri_ulasim.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ContactMeFragment extends Fragment implements OnMapReadyCallback {

    private Context mContext;
    private View rootView;
    private GoogleMap mMap;
    private MapView mapView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contant_me, container, false);

        TextView geriBildirim = rootView.findViewById(R.id.geriBildirim);

        geriBildirim.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bm.umuttekin@gmail.com"});
            startActivity(Intent.createChooser(intent, "Mail gönder"));
        });

        // Gets the MapView from the XML layout and creates it
        mapView = rootView.findViewById(R.id.mapView1);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    /*    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView1);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);*/

        return rootView;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        //Kayseri Ulaşım enlem-boylam al
        LatLng userLocation = new LatLng(38.738302, 35.367024);
        //Haritanın odaklandığı yere marker ekle
        googleMap.addMarker(new MarkerOptions().position(userLocation).title(getString(R.string.app_name)).draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        //Haritanın gösterim şeklini spinner yardımıyla seç
        setMapType();
        //Girilen konuma haritayı taşı ve zoom yap
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15.0f));

    }

    private void setMapType() {
        //Create Shared Preferences
        final SharedPreferences pref_map_type = mContext.getSharedPreferences("about_map_type", Context.MODE_PRIVATE);

        /*Create Spinner
         *Spinner'ın mevcut değeri kayıt ediliyor.
         * Uygulama yeniden açıldığında spinner mevcut değerini koruyor ve o değerden açılıyor.
         * Aynı şekilde google map'in türüde son haliyle açılıyor.
         */
        Spinner spinner = rootView.findViewById(R.id.spin_map_type);
        String[] arrayItems = {"Normal", "Uydu", "Uydu-Sade"};
        final int[] actualValues = {1, 4, 2};

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_item, arrayItems);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(pref_map_type.getInt("about_map_value", 0));//Listener bu kodla tetiklenmiyor.

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMap.setMapType(actualValues[position]);
                pref_map_type.edit().putInt("about_map_value", position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}
