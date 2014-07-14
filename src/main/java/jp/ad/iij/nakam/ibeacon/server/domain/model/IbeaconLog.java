package jp.ad.iij.nakam.ibeacon.server.domain.model;

import java.util.Date;

import org.json.JSONObject;

public class IbeaconLog {

    public int logId;
    public String mAddress;
    public String uuid;
    public String major;
    public String minor;
    public int rssi;
    public String brand;
    public String user;
    public String version;
    public String device;
    public String product;
    public Date recordDate;

    public IbeaconLog(String jsonParam) {
        JSONObject json = new JSONObject(jsonParam);

        this.mAddress = json.getString("mAddress");
        this.uuid = json.getString("uuid");
        this.major = json.getString("major");
        this.minor = json.getString("minor");
        this.rssi = json.getInt("rssi");
        this.brand = json.getString("brand");
        this.user = json.getString("user");
        this.version = json.getString("version");
        this.device = json.getString("device");
        this.product = json.getString("product");
        this.recordDate = new Date();

    }
}