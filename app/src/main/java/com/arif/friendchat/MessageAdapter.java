package com.arif.friendchat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by HP on 5/11/2018.
 */

public class MessageAdapter extends BaseAdapter {
    Context context;
    List<ChatMessage> messages;
    int flags[];
    LayoutInflater inflter;

    public MessageAdapter(Context applicationContext, List<ChatMessage> messages) {
        this.context = context;
        this.messages = messages;
        this.flags = flags;
        inflter = (LayoutInflater.from(applicationContext));
    }
public void SetData(List<ChatMessage> messages)
{
    this.messages = messages;
    notifyDataSetChanged();
}
    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.message_listview, null);
        TextView country = (TextView)           view.findViewById(R.id.textView);
        TextView userName = (TextView) view.findViewById(R.id.userName);
        country.setText(" "+messages.get(i).message);
        userName.setText(messages.get(i).email);
        Log.e("customadapter",""+i);
       // icon.setImageResource(flags[i]);
        return view;
    }
    }