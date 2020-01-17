package com.twwm.trafficviolationreport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAdapter extends ArrayAdapter<String> implements Filterable {

    ArrayList<String> NameList;
    ArrayList<String> LocList;
    ArrayList<String> LinkList;
    ArrayList<String> arrayList;

    HashMap<Integer,String> map;

    private ImageView imageView;

    private View v;

    public ListAdapter(Context context, int textViewResourceId, HashMap<Integer,String> binds, ArrayList<String> objects, ArrayList<String> loc, ArrayList<String> links) {
        super(context, textViewResourceId, objects);
        arrayList = objects;
        LocList = loc;
        LinkList = links;
        map = binds;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item, null);
        imageView = v.findViewById(R.id.listPic);

        RequestManager requestManager = Glide.with(getContext());
        if(LinkList.get(position)!=null) {
            RequestBuilder requestBuilder = requestManager.load(LinkList.get((Integer)getKeyFromValue(map, arrayList.get(position))));
            requestBuilder.into(imageView);
        }

        TextView textView = v.findViewById(R.id.listName);
        textView.setText(arrayList.get(position));

        TextView textViewDes = v.findViewById(R.id.listLocation);
        textViewDes.setText(LocList.get((Integer)getKeyFromValue(map, arrayList.get(position))));

        return v;
    }

    @Override
    public Filter getFilter()
    {
        Filter filter = new Filter()
        {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results)
            {

                arrayList = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults results = new FilterResults();
                List<String> FilteredArrList = new ArrayList<>();

                if (NameList == null)
                {
                    System.out.println("");
                    NameList = new ArrayList<String>(arrayList);
                }

                if (constraint == null || constraint.length() == 0)
                {
                    results.count = NameList.size();
                    results.values = NameList;
                }
                else
                {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < NameList.size(); i++)
                    {
                        String data = NameList.get(i);
                        if (data.toLowerCase().startsWith(constraint.toString()))
                        {
                            FilteredArrList.add(data);
                        }
                    }
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}
