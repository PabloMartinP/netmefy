package ar.com.netmefy.netmefy.router;

/**
 * Created by fiok on 09/07/2017.
 */

public class RestartTry {

    private boolean success;
    private int tryNumber;
    private String description;

    public RestartTry(boolean success, int tryNumber, String description){
        this.success = success;
        this.tryNumber = tryNumber;
        this.description = description;
    }

    public boolean is_success() {
        return success;
    }

    public void set_success(boolean success) {
        this.success = success;
    }

    public int get_tryNumber() {
        return tryNumber;
    }

    public void set_tryNumber(int tryNumber) {
        this.tryNumber = tryNumber;
    }

    public  String get_description(){return description;}
    public void set_description(String description){
        this.description = description;
    }

    public String toString(){
        if(is_success())
            return "OK - Intento " + String.valueOf(get_tryNumber());
        else
            return "Intento " + String.valueOf(get_tryNumber()) + ": " + get_description();
    }
}
