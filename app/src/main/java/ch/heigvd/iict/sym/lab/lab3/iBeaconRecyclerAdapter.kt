package ch.heigvd.iict.sym.lab.lab3

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import org.altbeacon.beacon.Beacon
import java.util.ArrayList
import android.view.LayoutInflater
import android.view.ViewGroup


class iBeaconRecyclerAdapter : RecyclerView.Adapter<iBeaconRecyclerAdapter.ViewHolder>() {
    private var beacons: List<Beacon> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val uuid: TextView = itemView.findViewById(R.id.uuid)
        val major: TextView = itemView.findViewById(R.id.major)
        val minor: TextView = itemView.findViewById(R.id.minor)
        val rssi: TextView = itemView.findViewById(R.id.rssi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): iBeaconRecyclerAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_ibeacon_recycler_adapter, parent,false))
    }

    override fun onBindViewHolder(holder: iBeaconRecyclerAdapter.ViewHolder, position: Int) {

        val beacon: Beacon = beacons[position]
        holder.uuid.text = beacon.id1.toString()
        holder.major.text = beacon.id2.toString()
        holder.minor.text = beacon.id3.toString()
        holder.rssi.text = beacon.rssi.toString()
    }

    override fun getItemCount(): Int {
        return beacons.size
    }

    fun setCollectionBeacons(collection : Collection<Beacon>) {
        beacons = collection.toList()
    }
}