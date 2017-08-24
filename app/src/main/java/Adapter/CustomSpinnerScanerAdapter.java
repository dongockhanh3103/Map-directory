package Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.vision.text.Text;
import com.itshareplus.googlemapdemo.R;
import com.itshareplus.googlemapdemo.Template_MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ngoc Khanh on 8/23/2017.
 */

public class CustomSpinnerScanerAdapter extends BaseAdapter {
    private final Context activity;
    private  int layout;
    private String[] Place;
    private int[] Icon;
    private LayoutInflater layoutInflater;

    public CustomSpinnerScanerAdapter(Context context, String[] Place, int[] icon,int layout) {
        this.Place = Place;
        this.Icon = icon;
        activity = context;
        this.layout=layout;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.custom_cell_spinner, null);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageSpinner);
        TextView textView = (TextView) convertView.findViewById(R.id.textSpinner);

        imageView.setImageResource(Icon[position]);
        textView.setText(Place[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.custom_cell_spinner, null);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageSpinner);
        TextView textView = (TextView) convertView.findViewById(R.id.textSpinner);

        imageView.setImageResource(Icon[position]);
        textView.setText(Place[position]);

        return convertView;

    }
}
