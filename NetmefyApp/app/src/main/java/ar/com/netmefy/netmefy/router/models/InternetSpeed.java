package ar.com.netmefy.netmefy.router.models;

/**
 * Created by fiok on 12/08/2017.
 */

public class InternetSpeed {
    private String _speed;
    private String _unit;

    public String get_unit() {
        return _unit;
    }

    public void set_unit(String _unit) {
        this._unit = _unit;
    }

    public String get_speed() {
        return _speed;
    }
    public int get_speedRounded(){
        float f = Float.parseFloat(_speed);
        //String s = Float.toString(f);

        int rs = Math.round(f);
        return rs;
    }

    public void set_speed(String _speed) {
        this._speed = _speed;
    }

    public String toString(){
        return String.valueOf(get_speed()) + " " + get_unit();
    }
}
