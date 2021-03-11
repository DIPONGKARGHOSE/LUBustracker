package com.example.lubustracker;

import com.example.lubustracker.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.Equalizer;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.util.Util;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static java.lang.String.*;


public class Student_MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private Location mLoction;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2000;
    private long FASTEST_INTERVAL = 5000;
    private LocationManager locationManager;
    private LatLng latLng;
    private boolean isPermission;
    Double anlo, anla;

    final List<String> req = new ArrayList<>();

    Profile n = new Profile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__maps);

        this.setTitle("Map");


        if (requestSinglePermission()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            checkLocation();


        }


    }

    private boolean checkLocation() {
        if (!isLocationEnabled()) {
            showAlert();
        }
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations setting is set to Off.nPlease enable")
                .setPositiveButton("Location Setting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent myIntend = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntend);
                    }
                })
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean requestSinglePermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        isPermission = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            isPermission = false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    }
                }).check();
        return isPermission;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (latLng != null) {
            mMap.addMarker(new MarkerOptions().position(latLng).title("Me"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 200));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 200));


        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng nm = marker.getPosition();
                String msg = "Updated Location: " +
                        Double.toString(nm.latitude) + "," +
                        Double.toString(nm.longitude);
                System.out.println(msg);
                String msg1 = "Updated Location: " +
                        Double.toString(latLng.latitude) + "," +
                        Double.toString(latLng.longitude);
               // System.out.println(msg1);
                float ss = distanceBetween(latLng, nm);
                Toast.makeText(Student_MapsActivity.this,"Bus Distance from you :" +valueOf(ss)+"meter", Toast.LENGTH_SHORT).show();

                return true;
            }
        });


    }

    private static float distanceBetween(LatLng location1, LatLng location2) {
        double earthRadius = 3958.75;
        double latDifference = Math.toRadians(location2.latitude - location1.latitude);
        double lonDifference = Math.toRadians(location2.longitude - location1.longitude);
        double a = Math.sin(latDifference / 2) * Math.sin(latDifference / 2) +
                Math.cos(Math.toRadians(location1.latitude)) * Math.cos(Math.toRadians(location2.latitude)) *
                        Math.sin(lonDifference / 2) * Math.sin(lonDifference / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = Math.abs(earthRadius * c);
        int meterConversion = 1609;
        return (float) (dist * meterConversion);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(4000);
        mLocationRequest.setFastestInterval(4000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startLocationUpdates();
        mLoction = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLoction == null) {
            startLocationUpdates();
        } else {
            Toast.makeText(this, "Location Detecting", Toast.LENGTH_SHORT).show();
        }
    }

    private void startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(4000)
                .setFastestInterval(4000);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Driver");
        Bundle bundle = getIntent().getExtras();

        final double w = bundle.getDouble("lat");
        final int ro = Profile.ponit();
        // Toast.makeText(this,String.valueOf(w),Toast.LENGTH_SHORT).show();
        final double p1 = location.getLatitude();

        final GeoFire geoFire1 = new GeoFire(databaseReference);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot idSnapshot : dataSnapshot.getChildren()) {
                 //   System.out.println(idSnapshot.child("Route Name").getValue());
                  //  String b=idSnapshot.child("Route Name").getValue().toString();
                //   System.out.println("hi" +b);

                   // int a = Integer.parseInt(String.valueOf(idSnapshot.child("Route Name").getValue(Integer.class)));
                   // System.out.println(a);

                    if (ro == 0) {
                        geoFire1.getLocation(idSnapshot.getKey(), new LocationCallback() {
                            @Override
                            public void onLocationResult(String key, GeoLocation location) {
                                if (location != null) {
                                    LatLng latLng1 = new LatLng(location.latitude, location.longitude);
                                    double p = Math.abs(location.latitude - w);
                                    if (p <= 0.009) {
                                        final String CHANNEL_ID = "Personal Notification";
                                        Intent intent = new Intent(Student_MapsActivity.this, Student_MapsActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra(EXTRA_NOTIFICATION_ID, 0);

                                        PendingIntent pendingIntent = PendingIntent.getActivity(Student_MapsActivity.this, 0, intent, 0);

                                        // Create the NotificationChannel, but only on API 26+ because
                                        // the NotificationChannel class is new and not in the support library
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            CharSequence name = "Personal Notification";
                                            String description = "include all personal notification";
                                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                                            channel.setDescription(description);
                                            // Register the channel with the system; you can't change the importance
                                            // or other notification behaviors after this
                                            NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                            notificationManager.createNotificationChannel(channel);
                                        }


                                        NotificationCompat.Builder builder = new NotificationCompat.Builder(Student_MapsActivity.this, CHANNEL_ID)
                                                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_normal)
                                                .setContentTitle("My notification")
                                                .setContentText("BUS IS COMING IN YOUR PONIT IN SOME TIME")
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                // Set the intent that will fire when the user taps the notification
                                                .setContentIntent(pendingIntent)
                                                .setAutoCancel(true);
                                        NotificationManagerCompat nf = NotificationManagerCompat.from(Student_MapsActivity.this);
                                        nf.notify(10, builder.build());

                                    }

                                    if (latLng1 != null) {
                                        mMap.addMarker(new MarkerOptions().position(latLng1).icon(BitmapDescriptorFactory.fromResource(R.drawable.location)).
                                                title("NOT Me"));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

                                    }

                                    //System.out.println(format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                                } else {
                                    //System.out.println(format("There is no location for key %s in GeoFire", key));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //System.err.println("There was an error getting the GeoFire location: " + databaseError);
                            }
                        });
                    } else {
                        if (ro == idSnapshot.child("Route Name").getValue(Integer.class)||idSnapshot.child("Route Name").getValue(Integer.class)==0) {
                            geoFire1.getLocation(idSnapshot.getKey(), new LocationCallback() {
                                @Override
                                public void onLocationResult(String key, GeoLocation location) {
                                    if (location != null) {
                                        LatLng latLng1 = new LatLng(location.latitude, location.longitude);
                                        double p = Math.abs(location.latitude - w);
                                        if (p <= 0.009) {
                                            final String CHANNEL_ID = "Personal Notification";
                                            Intent intent = new Intent(Student_MapsActivity.this, Student_MapsActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.putExtra(EXTRA_NOTIFICATION_ID, 0);

                                            PendingIntent pendingIntent = PendingIntent.getActivity(Student_MapsActivity.this, 0, intent, 0);

                                            // Create the NotificationChannel, but only on API 26+ because
                                            // the NotificationChannel class is new and not in the support library
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                CharSequence name = "Personal Notification";
                                                String description = "include all personal notification";
                                                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                                                channel.setDescription(description);
                                                // Register the channel with the system; you can't change the importance
                                                // or other notification behaviors after this
                                                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                                                notificationManager.createNotificationChannel(channel);
                                            }


                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Student_MapsActivity.this, CHANNEL_ID)
                                                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_normal)
                                                    .setContentTitle("My notification")
                                                    .setContentText("BUS IS COMING IN YOUR PONIT IN SOME TIME")
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                    // Set the intent that will fire when the user taps the notification
                                                    .setContentIntent(pendingIntent)
                                                    .setAutoCancel(true);
                                            NotificationManagerCompat nf = NotificationManagerCompat.from(Student_MapsActivity.this);
                                            nf.notify(10, builder.build());

                                        }

                                        if (latLng1 != null) {
                                            mMap.addMarker(new MarkerOptions().position(latLng1).icon(BitmapDescriptorFactory.fromResource(R.drawable.location)).
                                                    title("NOT Me"));
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

                                        }

                                        //System.out.println(format("The location for key %s is [%f,%f]", key, location.latitude, location.longitude));
                                    } else {
                                       // System.out.println(format("There is no location for key %s in GeoFire", key));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                   // System.err.println("There was an error getting the GeoFire location: " + databaseError);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        sStudent sstudent = new sStudent(
                location.getLongitude(),
                location.getLatitude()
        );
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Student");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {

            }
        });

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mMap.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Student");

        GeoFire geoFire = new GeoFire(ref);
        //geoFire.removeLocation(userId,null);
    }


}
