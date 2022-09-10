package com.planbar365.app.services.web_services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.wifi.WifiManager
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService

typealias NetworkChangedListener = (isConnected: Boolean) -> Unit

class ConnectivityListener(private val context: Context) {

    private val networkChangedListeners = arrayListOf<NetworkChangedListener>()

    //region used on api < 24
    private val intentFilter by lazy {
        IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    }

    private val networkBroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val isConnected = isConnected()
                networkChangedListeners.forEach { it.invoke(isConnected) }
            }
        }
    }

    private val networkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                networkChangedListeners.forEach { it.invoke(true) }
            }

            override fun onLost(network: Network) {
                networkChangedListeners.forEach { it.invoke(false) }
            }
        }
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getConnectivityManager()?.registerDefaultNetworkCallback(networkCallback)
        } else {
            context.registerReceiver(networkBroadcastReceiver, intentFilter)
        }
    }

    fun addListener(networkChangedListener: NetworkChangedListener) {
        networkChangedListeners.add(networkChangedListener)
    }

    fun removeListener(networkChangedListener: NetworkChangedListener) {
        networkChangedListeners.remove(networkChangedListener)
    }

    fun removeAll() {
        networkChangedListeners.clear()
    }

    fun isConnected(): Boolean {
        val activeNetwork = getConnectivityManager()?.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true
        return isConnected
    }

    fun checkWifiOnAndConnected(): Boolean {
        val wifiMgr = context.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiMgr.isWifiEnabled
    }

    fun getConnectivityManager() = getSystemService(context, ConnectivityManager::class.java)

}