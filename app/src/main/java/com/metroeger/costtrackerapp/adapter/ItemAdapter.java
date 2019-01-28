package com.metroeger.costtrackerapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.metroeger.costtrackerapp.R;
import com.metroeger.costtrackerapp.model.Item;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemAdapter extends ArrayAdapter {

    private int resource;

    public ItemAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = (Item) getItem(position);

        if(convertView==null){
            convertView=LayoutInflater.from(getContext()).inflate(resource,null);
        }

        TextView tvItem = convertView.findViewById(R.id.tvItem);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);

        SimpleDateFormat print = new SimpleDateFormat("dd/MM/yyyy");
        String date = print.format(item.getDate());

        tvItem.setText(item.getName());
        tvDate.setText(date);
        tvAmount.setText(item.getAmount()+"");

        return convertView;
    }
}
