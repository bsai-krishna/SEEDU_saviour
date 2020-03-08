package com.brocode.saviour.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brocode.saviour.Common.Common;
import com.brocode.saviour.Model.WeatherForecastResult;
import com.brocode.saviour.R;
import com.squareup.picasso.Picasso;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyCardHolder> {

    Context mContext;
    WeatherForecastResult weatherForecastResult;

    public WeatherForecastAdapter(Context mContext, WeatherForecastResult weatherForecastResult) {
        this.mContext = mContext;
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_weather_forecast, parent, false);
        return new MyCardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCardHolder holder, int position) {
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResult.getList().get(position).getWeather().get(0).getIcon()).append(".png").toString())
                .into(holder.ivWeather);

        holder.tvTemp.setText(new StringBuilder(String.valueOf(weatherForecastResult.getList().get(position).getMain().getTemp())).append("°C"));
        holder.tvDate.setText(new StringBuilder(Common.convertUnixToDate(weatherForecastResult.getList().get(position).getDt())));
        holder.tvDes.setText(new StringBuilder(weatherForecastResult.getList().get(position).getWeather().get(0).getDescription()));
    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.getList().size();
    }

    public class MyCardHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvDes, tvTemp;
        ImageView ivWeather;

        public MyCardHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvCardDateTime);
            tvDes = itemView.findViewById(R.id.tvCardDes);
            tvTemp = itemView.findViewById(R.id.tvCardTemp);
            ivWeather = itemView.findViewById(R.id.ivCardWeather);
        }
    }
}
