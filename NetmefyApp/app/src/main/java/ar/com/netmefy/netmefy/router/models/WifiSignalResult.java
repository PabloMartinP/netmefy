package ar.com.netmefy.netmefy.router.models;

/**
 * Created by fiok on 12/08/2017.
 */

public class WifiSignalResult {
    private int _signal;
    private int _numberIntent;

    public WifiSignalResult(){
        set_signal(0);
        set_numberIntent(0);
    }

    public void newIntent(int signal){
        set_signal(signal);
        set_numberIntent(get_numberIntent() + 1);
    }

    @Override
    public String toString(){
        return "intent n°" + String.valueOf(get_numberIntent()) + ", Value: " + String.valueOf(get_signal());
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
