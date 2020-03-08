package com.brocode.saviour.ui.main;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brocode.saviour.Common.Common;
import com.brocode.saviour.Model.WeatherForecastResult;
import com.brocode.saviour.Package.IOpenWeatherMap;
import com.brocode.saviour.Package.RetrofitClient;
import com.brocode.saviour.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    private CompositeDisposable compositeDisposable;
    private IOpenWeatherMap mService;

    private TextView tvCity;
    private RecyclerView recyclerView;

    private static ForecastFragment instance;

    public static ForecastFragment getInstance() {
        if(instance==null)
            instance = new ForecastFragment();
        return instance;
    }

    public ForecastFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_forecast, container, false);

        tvCity = itemView.findViewById(R.id.tvForecastCity);
        recyclerView = itemView.findViewById(R.id.rvForecast);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        getWeatherForecastInfo();

        return itemView;
    }

    private void getWeatherForecastInfo() {
        compositeDisposable.add(mService.getWeatherForecastByLatLong(
                String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        displayWeatherForecast(weatherForecastResult);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Forecast", ""+throwable.getMessage());
                    }
                })
        );
    }

    private void displayWeatherForecast(WeatherForecastResult weatherForecastResult) {
        tvCity.setText(new StringBuilder(weatherForecastResult.getCity().getName()));

        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getContext(), weatherForecastResult);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        compositeDisposable.clear();
    }
}
