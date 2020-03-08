package com.brocode.saviour.Package;

import com.brocode.saviour.Model.WeatherForecastResult;
import com.brocode.saviour.Model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLong(@Query("lat") String lat,
                                                  @Query("lon") String lon,
                                                  @Query("appid") String appID,
                                                  @Query("units") String unit);

    @GET("forecast")
    Observable<WeatherForecastResult> getWeatherForecastByLatLong(@Query("lat") String lat,
                                                                  @Query("lon") String lon,
                                                                  @Query("appid") String appID,
                                                                  @Query("units") String unit);
}
