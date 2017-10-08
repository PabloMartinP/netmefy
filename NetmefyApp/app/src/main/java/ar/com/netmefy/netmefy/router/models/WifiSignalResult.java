package ar.com.netmefy.netmefy.router.models;

/**
 * Created by fiok on 12/08/2017.
 */

public class WifiSignalResult {
    private int _signal;
    private int _numberIntent;
    private int _dB;

    public WifiSignalResult(){
        set_signal(0);
        set_numberIntent(0);

    }

    public int get_dB() {
        return _dB;
    }
    public String get_dBWithUnit() {
        return String.valueOf(_dB) + "dB";
    }

    public void set_dB(int _dB) {
        this._dB = _dB;
    }

    public void newIntent(int signal, int rssi){
        set_signal(signal);
        set_dB(rssi);
        set_numberIntent(get_numberIntent() + 1);
    }

    @Override
    public String toString(){
        return "N°" + String.valueOf(get_numberIntent()) + ", Level:" + String.valueOf(get_signal()) + ", dBm:"+String.valueOf(get_dB());
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
