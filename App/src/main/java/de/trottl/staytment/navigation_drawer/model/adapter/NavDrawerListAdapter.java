package de.trottl.staytment.navigation_drawer.model.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.trottl.staytment.R;
import de.trottl.staytment.navigation_drawer.model.item.NavDrawerItem;

public class NavDrawerListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItemList;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItemList) {
        this.context = context;
        this.navDrawerItemList = navDrawerItemList;
    }

    @Override
    public int getCount() {
        return navDrawerItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, parent, false);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        imgIcon.setImageResource(navDrawerItemList.get(position).getIcon());
        txtTitle.setText(navDrawerItemList.get(position).getTitle());

        return convertView;
    }
}
