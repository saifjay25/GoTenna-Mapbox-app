package com.mycode.myapplication.ui
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import com.mycode.myapplication.R
import com.mycode.myapplication.adapter.RecyclerViewAdapter
import com.mycode.myapplication.entity.PinData
import com.mycode.myapplication.viewModel.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), OnMapReadyCallback, LocationEngineListener, PermissionsListener,
    RecyclerViewAdapter.OnItemClickListener{
    companion object{
        var requestGranted = true
    }
    private lateinit var mapBoxMap: MapboxMap
    private var arrivalMarker : Marker? = null
    private val marker = MarkerOptions()
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var recycle: RecyclerView
    private lateinit var viewModel : MainViewModel
    private var mDisposable =  CompositeDisposable()
    private lateinit var originPosition: com.mapbox.geojson.Point
    lateinit var originLocation: Location
    private lateinit var destination : com.mapbox.geojson.Point
    private var locationLayerPlugin: LocationLayerPlugin? = null
    private lateinit var permissionsManager: PermissionsManager
    private var locationEngine: LocationEngine? = null
    private var mapRoute : NavigationMapRoute? =null
    var providerFactory : ViewModelProviderFactory? = null
        @Inject set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Mapbox.getInstance(this,getString(R.string.token))
        recycle =recyclerView
        recycle.layoutManager = LinearLayoutManager(this)
        recycle.setHasFixedSize(true)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        navigate.setOnClickListener {
            if(requestGranted) {
                val launcherOptions = NavigationLauncherOptions.builder()
                    .origin(originPosition)
                    .destination(destination)
                    .build()
                NavigationLauncher.startNavigation(this@MainActivity, launcherOptions)
            }
        }
    }
    override fun onMapReady(mapboxMap: MapboxMap) {
        mapBoxMap= mapboxMap
        viewModel = ViewModelProviders.of(this, providerFactory).get(MainViewModel::class.java)
        viewModel.PinDataAPICall().observe(this, object: Observer<List<PinData>>{
            override fun onChanged(t: List<PinData>) {
                for(i in t.indices){
                    mDisposable.add(viewModel.addPinData(t[i])
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe())
                }
                adapter = RecyclerViewAdapter(applicationContext, t)
                recycle.adapter = adapter
                adapter.setOnItemClickListener(this@MainActivity)
            }

        })
        locationPermission()
    }

    fun retrieveRoute(origin:com.mapbox.geojson.Point, arrival: com.mapbox.geojson.Point){
        NavigationRoute.builder().accessToken(Mapbox.getAccessToken())
            .origin(origin)
            .destination(arrival)
            .build()
            .getRoute(object:Callback<DirectionsResponse>{
                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                    return
                }

                override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                    if(response.body() == null ){
                        return
                    }
                    if(response.body()!!.routes().size ==0){
                        return
                    }
                    val route = response.body()!!.routes().get(0)
                    if(mapRoute != null){
                        mapRoute!!.removeRoute()
                    }else{
                        mapRoute = NavigationMapRoute(null,mapView,mapBoxMap)
                    }
                    mapRoute!!.addRoute(route)
                    navigate.isEnabled = true
                    navigate.setBackgroundResource(R.color.colorPrimary)
                }

            })
    }
    override fun itemClick(position: Int) {
        if(requestGranted) {
            viewModel.allPinData.observe(this, object : Observer<List<PinData>> {
                override fun onChanged(t: List<PinData>) {
                    if (arrivalMarker != null) {
                        mapBoxMap.removeMarker(arrivalMarker!!)
                    }
                    destination = com.mapbox.geojson.Point.fromLngLat(t[position].longitude, t[position].latitude)
                    originPosition =
                        com.mapbox.geojson.Point.fromLngLat(originLocation.longitude, originLocation.latitude)
                    marker.position = LatLng(t[position].latitude, t[position].longitude)
                    arrivalMarker = mapBoxMap.addMarker(marker)
                    val pos: CameraPosition =
                        CameraPosition.Builder().target(LatLng(t[position].latitude, t[position].longitude)).zoom(11.0)
                            .build()
                    mapBoxMap.animateCamera(CameraUpdateFactory.newCameraPosition(pos), 1000)
                    retrieveRoute(originPosition, destination)
                }

            })
        }
    }
    @SuppressWarnings("missingPermission")
    private fun locationPermission(){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            locationEngine = LocationEngineProvider(this).obtainBestLocationEngineAvailable()
            locationEngine!!.priority = LocationEnginePriority.HIGH_ACCURACY
            locationEngine!!.activate()
            locationEngine!!.addLocationEngineListener(this)
            locationLayerPlugin = LocationLayerPlugin(mapView, mapBoxMap, locationEngine)
            locationLayerPlugin!!.setLocationLayerEnabled(true)
            locationLayerPlugin!!.cameraMode = CameraMode.TRACKING
            locationLayerPlugin!!.renderMode = RenderMode.NORMAL

        }else{
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    private fun setCameraLocation(location: Location){
        mapBoxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,
            location.longitude), 11.0))
    }

    override fun onLocationChanged(location: Location?) {
        if(location != null){
            originLocation = location
            setCameraLocation(location)
        }
    }

    @SuppressWarnings("missingPermission")
    override fun onConnected() {
        locationEngine!!.requestLocationUpdates()
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(applicationContext, "This app needs you current location", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if(granted){
            locationPermission()
        }else{
            requestGranted = false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    @SuppressWarnings("missingPermission")
    override fun onStart() {
        super.onStart()
        if(locationEngine != null) {
            locationEngine!!.requestLocationUpdates()
        }
        if(locationLayerPlugin != null) {
            locationLayerPlugin!!.onStart()
        }
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        if(locationLayerPlugin != null) {
            locationLayerPlugin!!.onStop()
        }
        if(locationEngine != null) {
            locationEngine!!.removeLocationUpdates()
        }
        mDisposable.clear()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(locationEngine != null){
            locationEngine!!.deactivate()
        }
        mDisposable.add(viewModel.deleteAllPinData().
            subscribeOn(Schedulers.io()).
            observeOn(AndroidSchedulers.mainThread()).
            subscribe())
        mDisposable.clear()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
