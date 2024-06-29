package com.example.lab1_ph32983;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<Map<String, Object>> cityList;

    public CityAdapter(List<Map<String, Object>> cityList) {
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cities, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        Map<String, Object> city = cityList.get(position);
        holder.tvName.setText((String) city.get("name"));
        holder.tvState.setText((String) city.get("state"));
        holder.tvCountry.setText((String) city.get("country"));
        holder.tvDanso.setText(String.valueOf(city.get("danso")));

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code xử lý sự kiện chỉnh sửa
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức xóa trong Activity hoặc Fragment
                ((LogoutActivity) v.getContext()).handleDeleteCity(city, position);
            }
        });
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức hiển thị dialog chỉnh sửa
                ((LogoutActivity) v.getContext()).showEditItemDialog(city, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvState, tvCountry, tvDanso;
        ImageView imgEdit, imgDelete;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvState = itemView.findViewById(R.id.tvState);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvDanso = itemView.findViewById(R.id.tvDanso);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}