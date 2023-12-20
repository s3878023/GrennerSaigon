package com.example.grennersaigon.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grennersaigon.Model.PinModel;
import com.example.grennersaigon.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PinAdapter extends RecyclerView.Adapter<PinAdapter.PinViewHolder> {

    private List<PinModel> pinList;

    public PinAdapter(List<PinModel> pinList) {
        this.pinList = pinList;
    }

    @NonNull
    @Override
    public PinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pin, parent, false);
        return new PinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PinViewHolder holder, int position) {
        PinModel pin = pinList.get(position);

        holder.textViewSiteName.setText("Site Name: " + pin.getSiteName());
        holder.textViewSiteDescription.setText("Site Description: " + pin.getSiteDescription());
        holder.textViewSiteAddress.setText("Site Address: " + pin.getSiteAddress());
        holder.textViewSiteReport.setText("Site Report: " + pin.getSiteReport());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String formattedDateTime = dateFormat.format(pin.getDateTime());
        holder.textViewDateTime.setText("Date & Time: " + formattedDateTime);

        // Add more TextViews for other fields and set their values
    }

    @Override
    public int getItemCount() {
        return pinList.size();
    }

    static class PinViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSiteName, textViewSiteDescription, textViewSiteAddress, textViewSiteReport, textViewDateTime;

        PinViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSiteName = itemView.findViewById(R.id.textViewSiteName);
            textViewSiteDescription = itemView.findViewById(R.id.description);
            textViewSiteAddress = itemView.findViewById(R.id.siteAddress);
            textViewSiteReport = itemView.findViewById(R.id.siteReport);
            textViewDateTime = itemView.findViewById(R.id.DateTime);
        }
    }
}
