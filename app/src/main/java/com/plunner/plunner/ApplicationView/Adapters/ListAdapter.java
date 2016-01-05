package com.plunner.plunner.ApplicationView.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.plunner.plunner.R;

import java.util.List;

/**
 * Created by giorgiopea on 04/01/16.
 */
public class ListAdapter extends ArrayAdapter<String> {

    public ListAdapter(Context context, List<String> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // View Holder pattern

        String text = getItem(position);


        liContent liContent;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.meetings_row, null);

            liContent = new liContent();

            liContent.title = (TextView) convertView.
                    findViewById(R.id.liTitle);
            liContent.subtitle = (TextView) convertView
                    .findViewById(R.id.liSubtitle);

            convertView.setTag(liContent);

        } else {
            liContent = (liContent) convertView.getTag();
        }


        liContent.title.setText(text);
        liContent.subtitle.setText(text+" subtitle");

        return convertView;

    }

    static class liContent {
        TextView title;
        TextView subtitle;

    }
}