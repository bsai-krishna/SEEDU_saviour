package com.brocode.saviour.Model;

import java.util.ArrayList;
import java.util.List;

public class WeatherForecastResult {
    private String cod;
    private int message;
    private int cnt;
    private ArrayList<MyList> list;
    private City city;

    public WeatherForecastResult() {
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public ArrayList<MyList> getList() {
        return list;
    }

    public void setList(ArrayList<MyList> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
