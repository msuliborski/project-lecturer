package com.ms.projectlecturer.view;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ms.projectlecturer.BuildConfig;
import com.ms.projectlecturer.R;
import com.ms.projectlecturer.util.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private boolean _locationPermissionGranted = false;
    private MapView _mapView;
    private GoogleMap _map;
    private FusedLocationProviderClient _fusedLocationClient;
    private boolean _mapIsReady;

    private Fragment _currentFragment;
    private LatLng _yourPos;
    private FragmentManager _fragmentManager;
    private FragmentTransaction _fragmentTransaction;
    private List<Marker> _markers;
    private PlacesClient _placesClient;
    private Place _place;
    private int _invokeCounter = 0;
    private Map<String, String> _placesOnMap = new HashMap<>();
    private LecturersActivity _lecturersActivity;
    private boolean _lockedOnPlace;

    public boolean getLockedOnPlace () {
        return _lockedOnPlace;
    }

    public void setLockedOnPlace(boolean lockedOnPlace) {
        _lockedOnPlace = lockedOnPlace;
    }

    public Map<String, String> getPlacesOnMap () {
        return _placesOnMap;
    }

    public void getLastKnownLocationWithPermissionCheck() {
        if (checkMapServices() && _locationPermissionGranted) {
            getLastKnownLocation();
        } else {
            getLocationPermission();
        }
    }

    private void initGoogleMaps(Bundle savedInstanceState, View view) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(Constants.MAPVIEW_BUNDLE_KEY);
        }
        _mapView = (MapView) view.findViewById(R.id.map);
        _mapView.onCreate(mapViewBundle);
        _mapView.getMapAsync(this);
        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }



    public void getLastKnownLocationWithOnNullInvoking() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        _fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    if (location == null) {
                        if (_invokeCounter < 5) {
                            try {
                                //Toast.makeText(getContext(), "KURWA", Toast.LENGTH_SHORT).show();
                                Thread.sleep(400);
                                _invokeCounter++;
                                getLastKnownLocationWithOnNullInvoking();
                            } catch (InterruptedException iex) {
                                iex.printStackTrace();
                            }
                        }
                    } else {
                        _invokeCounter = 0;
                        _yourPos = new LatLng(location.getLatitude(), location.getLongitude());
                        if (!_lockedOnPlace) {
                            moveCamera(_yourPos, Constants.DEFAULT_ZOOM);
                        }
                    }
                }
            }

        });

    }

    public void getLastKnownLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        _fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    if (location == null) {
                        return;
                    }
                    _yourPos = new LatLng(location.getLatitude(), location.getLongitude());
                    if (!_lockedOnPlace) {
                    moveCamera(_yourPos, Constants.DEFAULT_ZOOM);
                    }
                }
            }
        });
    }

    public void moveCamera(LatLng latLng, float zoom) {
        _map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private boolean checkMapServices() {
        if (isServicesOk() && isGPSEnabled())
            return true;
        return false;
    }

    public boolean isServicesOk() {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        int available = gaa.isGooglePlayServicesAvailable(getActivity());

        if (available == ConnectionResult.SUCCESS) {
            // everything is fine and the user can make map requests
            return true;
        } else if (gaa.isUserResolvableError(available)) {
            // an error occured but it could be resolved
            Dialog dialog = gaa.getErrorDialog(getActivity(), available, Constants.ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "Cannot make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean isGPSEnabled() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("This application requires GPS to work properly, you have to enable it.");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(enableGpsIntent, Constants.PERMISSIONS_REQUEST_ENABLE_GPS);
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (!_locationPermissionGranted) {
                    getLocationPermission();
                } else {
                    getLastKnownLocation();
                }
                // else DO STH
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        _locationPermissionGranted = false;
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // if request is cancelled, the result array is empty
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    _locationPermissionGranted = true;
                    getLastKnownLocation();
                }
            }
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            _locationPermissionGranted = true;
            // DO STH;
            getLastKnownLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _lecturersActivity = (LecturersActivity) getActivity();
        Places.initialize(getActivity(), BuildConfig.GoogleSecAPIKEY);
        _placesClient = Places.createClient(getActivity());
        initGoogleMaps(savedInstanceState, view);
        _fragmentManager = getChildFragmentManager();
        _fragmentTransaction = _fragmentManager.beginTransaction();
        _fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("tag", "onComplete: ONRESUME");
        _mapView.onResume();
        if (checkMapServices() && _locationPermissionGranted) {
            getLastKnownLocationWithOnNullInvoking();
            Log.d("tag", "onComplete: kurwa32");
        } else {
            getLocationPermission();
            Log.d("tag", "onComplete: kurwa33");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        _mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        _mapView.onStop();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap map) {
        _map = map;
        _map.setOnInfoWindowClickListener(this);
        _map.setMyLocationEnabled(true);
        _mapIsReady = true;
    }

    @Override
    public void onPause() {
        _mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (_mapView != null) _mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        _mapView.onLowMemory();
    }


    private void addMarkerAt(LatLng pos, String placeName,String enquireContent) {

        float colour = 0f;

//        switch (type)
//        {
//            case Events:
//                colour = Constants.EVENTS_RED;
//                break;
//            case Facilities:
//                colour = Constants.FACILITIES_BROWN;
//                break;
//            case Food:
//                colour = Constants.FOOD_GREEN;
//                break;
//            case Accomodation:
//                colour = Constants.STAY_BLUE;
//                break;
//        }
        Marker marker = _map.addMarker(new MarkerOptions()
                .position(pos)
                .title(placeName)
                .snippet(enquireContent)
                .icon(BitmapDescriptorFactory.defaultMarker(colour)));
        _markers.add(marker);
    }


    public void setMarkersVisible(boolean isVisible) {
        for (Marker marker : _markers) {
            marker.setVisible(isVisible);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //_lockedOnPlace = false;
        LatLng latLng = marker.getPosition();
        String placeID = _placesOnMap.get(latLng.latitude + "_" + latLng.longitude);
        Log.d("tag", "onComplete: kurwAAAA");
    }


}
