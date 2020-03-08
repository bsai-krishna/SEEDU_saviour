package com.brocode.saviour.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.brocode.saviour.Common.Common;
import com.brocode.saviour.Model.WeatherResult;
import com.brocode.saviour.Package.IOpenWeatherMap;
import com.brocode.saviour.Package.RetrofitClient;
import com.brocode.saviour.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class TodayWeather extends Fragment {

    TextView tvCity, tvHumidity, tvSunrise, tvSunset, tvPressure, tvTemp, tvDes, tvDate, tvWind, tvCoord;
    LinearLayout weatherPanel;
    ProgressBar loading;

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    public TodayWeather() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_today_weather, container, false);
        initViews(view);
//        Log.e("Location", String.valueOf(Common.current_location.getLatitude())+ "/" +  String.valueOf(Common.current_location.getLongitude()));
//        Log.e("App ID", Common.APP_ID);
        getWeatherInfo();
        return view;
    }

    private void getWeatherInfo() {
        compositeDisposable.add(mService.getWeatherByLatLong(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                String.valueOf(Common.APP_ID),
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        setWeatherInfo(weatherResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }));
    }

    private void setWeatherInfo(WeatherResult weatherResult) {
        tvCity.setText(weatherResult.getName());
        tvDes.setText(new StringBuilder("Weather In ").append(weatherResult.getName()).append(" : ").append(weatherResult.getWeather().get(0).getDescription()).toString());
        tvTemp.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
        tvDate.setText(Common.convertUnixToDate(weatherResult.getDt()));
        tvPressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append(" hpa").toString());
        tvHumidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append("%").toString());
        tvSunrise.setText(Common.convertUnixToHours(weatherResult.getSys().getSunrise()));
        tvSunset.setText(Common.convertUnixToHours(weatherResult.getSys().getSunset()));
        tvCoord.setText(new StringBuilder(weatherResult.getCoord().toString()));
        tvWind.setText(new StringBuilder(" Speed : ").append(weatherResult.getWind().getSpeed()).append("   Degrees : ").append(weatherResult.getWind().getDeg()).toString());

        weatherPanel.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    private void initViews(View view)
    {
        tvCity = view.findViewById(R.id.tvCityName);
        tvHumidity = view.findViewById(R.id.tvHumidity);
        tvCoord = view.findViewById(R.id.tvCoordinates);
        tvSunrise = view.findViewById(R.id.tvSunrise);
        tvSunset = view.findViewById(R.id.tvSunset);
        tvPressure = view.findViewById(R.id.tvPressure);
        tvTemp = view.findViewById(R.id.tvTemp);
        tvDes = view.findViewById(R.id.tvDes);
        tvDate = view.findViewById(R.id.tvDateTime);
        tvWind = view.findViewById(R.id.tvWind);

        weatherPanel = view.findViewById(R.id.weatherPanel);
        loading = view.findViewById(R.id.pbLoading);
    }

    @Override
    public void onStop() {
        super.onStop();

        compositeDisposable.clear();
    }
}
