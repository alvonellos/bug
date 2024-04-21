package com.alvonellos.bug.dto;

public record GeoIPResponse(String country, String subdivision,
                            String city, String postal,
                            String latitude, String longitude){

    @Override public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(this.country);
        b.append("|");
        b.append(this.subdivision);
        b.append("|");
        b.append(this.city);
        b.append("|");
        b.append(this.postal);
        b.append("|");
        b.append(this.latitude);
        b.append("|");
        b.append(this.longitude);
        return b.toString();
    }
}
