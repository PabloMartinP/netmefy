package ar.com.netmefy.netmefy.router.models;

/**
 * Created by fiok on 12/08/2017.
 */

public class WifiSignalResult {
    private int _signal;
    private int _numberIntent;
    private int _rssi;

    public WifiSignalResult(){
        set_signal(0);
        set_numberIntent(0);

    }

    public int get_rssi() {
        return _rssi;
    }

    public void set_rssi(int _rssi) {
        this._rssi = _rssi;
    }

    public void newIntent(int signal, int rssi){
        set_signal(signal);
        set_rssi(rssi);
        set_numberIntent(get_numberIntent() + 1);
    }

    @Override
    public String toString(){
        return "N°" + String.valueOf(get_numberIntent()) + ", Level:" + String.valueOf(get_signal()) + ", dBm:"+String.valueOf(get_rssi());
    }

    public int get_signal() {
        return _signal;
    }

    public void set_signal(int _signal) {
        this._signal = _signal;
    }

    public int get_numberIntent() {
        return _numberIntent;
    }

    public void set_numberIntent(int _numberIntent) {
        this._numberIntent = _numberIntent;
    }
}
