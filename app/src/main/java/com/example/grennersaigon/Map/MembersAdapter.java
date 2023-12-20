package com.example.grennersaigon.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grennersaigon.R;

import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {

    private List<String> memberNames;

    public MembersAdapter(List<String> memberNames) {
        this.memberNames = memberNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String memberName = memberNames.get(position);
        holder.memberNameTextView.setText(memberName);
    }

    @Override
    public int getItemCount() {
        return memberNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView memberNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memberNameTextView = itemView.findViewById(R.id.memberNameTextView);
        }
    }
}
