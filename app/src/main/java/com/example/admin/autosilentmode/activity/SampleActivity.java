package com.example.admin.autosilentmode.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.admin.autosilentmode.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SampleActivity extends AppCompatActivity {

    WifiManager mainWifiObj;
    WifiScanReceiver wifiReciever;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_wifi);


        mainWifiObj = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
//        mainWifiObj.startScan();
//        mainWifiObj.setWifiEnabled(true);


    }


    private void finallyConnect(String networkPass, String networkSSID) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

        // remember id
        int netId = mainWifiObj.addNetwork(wifiConfig);
        mainWifiObj.disconnect();
        mainWifiObj.enableNetwork(netId, true);
        mainWifiObj.reconnect();

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"\"" + networkSSID + "\"\"";
        conf.preSharedKey = "\"" + networkPass + "\"";
        mainWifiObj.addNetwork(conf);
    }


    private void parseData() {

        try {
            InputStream is = getAssets().open("htmlSample.html");
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String str = new String(buffer);
            inputHtmlString(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void inputHtmlString(String htmlsString) {

        Document doc = Jsoup.parse(htmlsString);

        for (Element element:doc.getElementsByClass("btDevices")){
            Elements option = element.getElementsByTag("option");

            for (Element element1:option){
                Log.e("Jay","->"+element1.attr("value"));
                Log.e("Jay->","->"+element1.text());
            }
        }

    }

    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    public void onClick(View view) {
        parseData();
    }

    class WifiScanReceiver extends BroadcastReceiver {
        @SuppressLint("UseValueOf")
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = mainWifiObj.getScanResults();
            ArrayList<String> ssidList = new ArrayList<>();

            for (ScanResult bean : wifiScanList) {
                if (!ssidList.contains(bean.SSID)) {
                    ssidList.add(bean.SSID);
                    Log.e("Call", "->" + bean.SSID);
                }
            }
        }
    }

}
