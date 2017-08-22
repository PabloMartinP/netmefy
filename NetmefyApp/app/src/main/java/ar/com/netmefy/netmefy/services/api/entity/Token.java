package ar.com.netmefy.netmefy.services.api.entity;

/**
 * Created by fiok on 21/08/2017.
 */

public class Token {
    private String access_token;
    private String token_type;
    private int expires_in;

    /*@JsonCreator
    public Token(@JsonProperty("Country") String country, @JsonProperty("Region") String region, @JsonProperty("Year") Integer fecha, @JsonProperty("Population") Integer poblacion) {
        super();
        this.country = country;
        this.region = region;
        this.fecha = fecha;
        this.poblacion = poblacion;
    }*/
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

}
