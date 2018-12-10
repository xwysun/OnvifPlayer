package com.xwysun.onvifplayer.support.finder

import android.util.Log
import com.xwymodule.onvif.OnvifDevice
import java.io.IOException
import java.io.InterruptedIOException
import java.net.*
import java.util.*

 object OnvifFinder {

    private var mSocket: DatagramSocket? = null
    private var mPacket: DatagramPacket? = null
    var isSearching = false
        private set
    private lateinit var mListener: (OnvifDevice)->Unit
    private var workThread: Thread? = null

     val DISCOVERY_PROBE_TDS = "<?xml version=\"1.0\" encoding=\"utf-8\"?><Envelope xmlns:tds=\"http://www.onvif.org/ver10/device/wsdl\" xmlns=\"http://www.w3.org/2003/05/soap-envelope\"><Header><wsa:MessageID xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\">uuid:5101931c-dd3e-4f14-a8aa-c46144af3433</wsa:MessageID><wsa:To xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\">urn:schemas-xmlsoap-org:ws:2005:04:discovery</wsa:To><wsa:Action xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\">http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</wsa:Action></Header><Body><Probe xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\"><Types>dn:NetworkVideoTransmitter</Types><Scopes /></Probe></Body></Envelope>"
     val BROADCAST_SERVER_PORT = 3702
     val TAG = "OnvifFinder"

     val broadcast: String?
         @Throws(SocketException::class)
         get() {
             System.setProperty("java.net.preferIPv4Stack", "true")
             val niEnum = NetworkInterface
                     .getNetworkInterfaces()
             while (niEnum.hasMoreElements()) {
                 val ni = niEnum.nextElement()
                 if (!ni.isLoopback) {
                     for (interfaceAddress in ni
                             .interfaceAddresses) {
                         if (interfaceAddress.broadcast != null) {
                             return interfaceAddress.broadcast.toString()
                                     .substring(1)
                         }
                     }
                 }
             }
             return null
         }


    private val mSearchingRunnable = Runnable {
        initSocket()
        sendProbe()
        isSearching = true
        val Buff = ByteArray(4096)
        val packet = DatagramPacket(Buff,
                Buff.size)
        while (isSearching) {
            try {
                mSocket!!.receive(packet)
                if (packet.length > 0) {
                    val strPacket = String(packet.data, 0,
                            packet.length)
                    Log.v("ws-discovery", " receive packets:$strPacket")
                    processReceivedPacket(strPacket)
                }
            } catch (e: InterruptedIOException) {
                Log.d("OnvifFinder","receive timeout!!")
            } catch (e: IOException) {
                e.printStackTrace()
                break
            }

        }
        mSocket!!.close()
    }

    //	private boolean isUuidExistInCameraList(UUID uuid) {
    //		boolean result = false;
    //		for (CameraDevice device : mCameraList.getValue()) {
    //			if (device.uuid.equals(uuid)) {
    //				result = true;
    //			}
    //		}
    //		return result;
    //	}

    val localIpAddress: String?
        get() {
            try {
                val en = NetworkInterface
                        .getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    val intf = en.nextElement()
                    val enumIpAddr = intf
                            .inetAddresses
                    while (enumIpAddr.hasMoreElements()) {
                        val inetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress) {
                            return inetAddress.hostAddress.toString()
                        }
                    }
                }
            } catch (ex: SocketException) {
            }

            return null
        }



    fun start(listener: (OnvifDevice)->Unit):Boolean{
        return if (workThread == null || !workThread!!.isAlive) {
            mListener = listener
            workThread = Thread(mSearchingRunnable)
            workThread!!.start()
            true
        }else{
            false
        }
    }

    @Throws(InterruptedException::class)
    fun stop() {
        if (workThread != null && workThread!!.isAlive) {
            workThread!!.interrupt()
            isSearching = false
        }
    }

    private fun getMid(src: String, head: String, foot: String): String? {
        val headIndex = src.indexOf(head)
        if (headIndex == -1) {
            return null
        }
        val tmp = src.substring(headIndex + head.length)
        val footIndex = tmp.indexOf(foot)
        return if (footIndex == -1) {
            null
        } else tmp.substring(0, footIndex)
    }

    private fun processReceivedPacket(packet: String) {
        val uuid = getMid(packet, "Address>", "<")!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
        val url = getMid(packet, "XAddrs>", "<")!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        if (uuid != null && url != null) {
            val onvifDevice = OnvifDevice(URL(url).host, UUID.fromString(uuid))
            mListener(onvifDevice)
        }
    }

    private fun initSocket() {
        try {
            mSocket = DatagramSocket()
            mSocket!!.broadcast = true
            mSocket!!.soTimeout = 10000
        } catch (e: SocketException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }

    fun sendProbe() {
        Thread(Runnable {
            val buf = DISCOVERY_PROBE_TDS.toByteArray()
            try {
                val strBroadcast = broadcast ?: throw IOException("Broadcast is null")
                Log.d("ws-discovery", "Broadcast to:$strBroadcast")
                mPacket = DatagramPacket(buf, buf.size,
                        InetAddress.getByName(strBroadcast), BROADCAST_SERVER_PORT)
                mSocket!!.send(mPacket)
                Log.d(TAG, "send probe!")
            } catch (e: IOException) {
                Log.e(TAG, "Exception sending broadcast probe", e)
                return@Runnable
            }
        }).start()
    }


}