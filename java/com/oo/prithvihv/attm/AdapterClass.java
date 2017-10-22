package com.oo.prithvihv.attm;

/**
 * Created by Prithvihv on 22-10-2017.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Prithvihv on 22-10-2017.
 */

public class AdapterClass extends ArrayAdapter {

    private static final String TAG = "AdapterActivity";
    private ArrayList<Attmodel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        CheckBox checkBox;
    }

    public AdapterClass(ArrayList<Attmodel> data, Context context) {
        super(context, R.layout.list_model, data);
        this.dataSet = data;
        this.mContext = context;

    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Attmodel getItem(int position) {
        return (Attmodel) dataSet.get(position);
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_model, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.roll);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Attmodel item = getItem(position);

        Log.d(TAG, "getView: ");
        viewHolder.txtName.setText(item.roll);
        viewHolder.checkBox.setChecked(item.checked);


        return result;
    }
}

