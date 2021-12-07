package ch.heigvd.iict.sym.lab.lab3

import android.Manifest
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import android.os.RemoteException
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.altbeacon.beacon.Region
import org.altbeacon.beacon.MonitorNotifier

private const val BEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"

class iBeaconActivity : AppCompatActivity(), BeaconConsumer {
    private lateinit var beaconManager: BeaconManager
    private lateinit var beaconRecyclerAdapter: iBeaconRecyclerAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ibeacon)

        // Link GUI
        recyclerView = findViewById(R.id.ibeacon_recycler_view)

        // Create adapter for recyclerAdapter
        beaconRecyclerAdapter = iBeaconRecyclerAdapter()
        recyclerView.adapter = beaconRecyclerAdapter
        // Set up the layout manager to be linear
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Ask user for location permission
        if (ContextCompat.checkSelfPermission(this@iBeaconActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@iBeaconActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@iBeaconActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@iBeaconActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        }

        // Get instance
        beaconManager = BeaconManager.getInstanceForApplication(this)
        // Parse beacon with a certain beacon type, to detect proprietary beacon
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BEACON_FORMAT))
    }


    override fun onResume() {
        super.onResume()
        beaconManager.bind(this)
    }

    override fun onPause() {
        super.onPause()
        beaconManager.unbind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconManager.unbind(this)
    }

    override fun unbindService(conn: ServiceConnection) {
        super.unbindService(conn)
    }

    override fun bindService(service: Intent?, conn: ServiceConnection, flags: Int): Boolean {
        return super.bindService(service, conn, flags)
    }

    override fun onBeaconServiceConnect() {

        // new Region for ranging or monitoring
        val region = Region("beacons", null, null, null)

        // Called when the BeaconService sees or stops seeing a Region of beacons
        beaconManager.addMonitorNotifier(object : MonitorNotifier {

            override fun didEnterRegion(region: Region) {
                try {
                    beaconManager.startRangingBeaconsInRegion(region)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }

            override fun didExitRegion(region: Region) {
                try {
                    beaconManager.stopRangingBeaconsInRegion(region)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }

            override fun didDetermineStateForRegion(state: Int, region: Region) {
                println("I have just switched from seeing/not seeing beacons: $state")
            }
        })

        beaconManager.addRangeNotifier { beacons, region ->
            beaconRecyclerAdapter.setCollectionBeacons(beacons)
            beaconRecyclerAdapter.notifyDataSetChanged()
        }

        try {
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (e: RemoteException) {
            Log.e("iBeacon Activity - ", e.printStackTrace().toString())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@iBeaconActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this,
                            R.string.ibeacon_permission_ok, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this,
                        R.string.ibeacon_permission_denied, Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}