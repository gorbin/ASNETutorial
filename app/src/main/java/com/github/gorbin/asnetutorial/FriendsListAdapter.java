package com.github.gorbin.asnetutorial;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.gorbin.asne.core.persons.SocialPerson;
import com.github.gorbin.asne.odnoklassniki.OkSocialNetwork;
import com.github.gorbin.asne.vk.VkSocialNetwork;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendsListAdapter extends BaseAdapter {
    private final Activity context;
    private ViewHolder holder;
    private ArrayList<SocialPerson> friends;
    private int networkId;
    private int image;

    public FriendsListAdapter(Activity context, ArrayList<SocialPerson> friends, int socialNetworkID) {
        this.context = context;
        this.friends = friends;
        this.networkId = socialNetworkID;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int i) {
        return friends.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.friend_row, null, true);
            holder = new ViewHolder();
            holder.id = (TextView) convertView.findViewById(R.id.id);
            holder.label = (TextView) convertView.findViewById(R.id.label);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        colorRow(networkId);
        holder.id.setText(friends.get(position).id);
        holder.label.setText(friends.get(position).name);
        if (friends.get(position).avatarURL != null) {
            Picasso.with(context)
                    .load(friends.get(position).avatarURL)
                    .placeholder(image)
                    .error(image)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(image);
        }
        return convertView;
    }

    private void colorRow(int networkId){
        int color = context.getResources().getColor(R.color.dark);
        switch (networkId) {
            case VkSocialNetwork.ID:
                color = context.getResources().getColor(R.color.vk);
                image = R.drawable.vk_user;
                break;
            case OkSocialNetwork.ID:
                color = context.getResources().getColor(R.color.ok);
                image = R.drawable.ok_user;
                break;
        }
        holder.label.setTextColor(color);
    }

    static class ViewHolder {
        public ImageView imageView;
        public TextView id;
        public TextView label;
    }
}