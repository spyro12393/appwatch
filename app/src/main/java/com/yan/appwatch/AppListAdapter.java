package com.yan.appwatch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by bang on 2017/9/15.
 */

public class AppListAdapter extends ArrayAdapter<AppList> {

    private static ArrayList<AppList> appLists;
    Context context;
    int resource;

    public AppListAdapter(Context context , int resource, ArrayList<AppList> appLists) {
        super(context, resource, appLists);
        this.appLists = appLists;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.use_list_layout, null , true);
        }

        AppList appList = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewProduct);
        //Picasso.with(context).load(appList.getImage()).into(imageView);
        imageView.setImageDrawable(appList.getImage());
        TextView txtTitle =  (TextView) convertView.findViewById(R.id.txtAppName);
        txtTitle.setText(appList.getAppName());
        //TextView txtInfo =  (TextView) convertView.findViewById(R.id.txtPackageName);
        //txtInfo.setText(appList.getPackageName());
        TextView txtPrice =  (TextView) convertView.findViewById(R.id.txtUseTime);
        txtPrice.setText(appList.getUseTime());

        return convertView;
    }

}
