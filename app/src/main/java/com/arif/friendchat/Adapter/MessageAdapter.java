package com.arif.friendchat.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arif.friendchat.Entity.ChatMessage;
import com.arif.friendchat.R;

import java.util.List;

/**
 * Created by HP on 5/11/2018.
 */

public class MessageAdapter extends BaseAdapter {
    Context context;
    List<ChatMessage> messages;
    int flags[];
    String email;
    LayoutInflater inflter;

    public MessageAdapter(Context applicationContext, List<ChatMessage> messages,String email) {
        this.context = context;
        this.messages = messages;
        this.email = email;
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
        TextView your_message = (TextView)           view.findViewById(R.id.your_message);
        TextView your_name = (TextView) view.findViewById(R.id.your_name);
        TextView my_message = (TextView) view.findViewById(R.id.my_message);
        TextView my_name = (TextView) view.findViewById(R.id.my_name);
        LinearLayout you_layout = (LinearLayout) view.findViewById(R.id.you_layout);
        LinearLayout my_layout = (LinearLayout) view.findViewById(R.id.my_layout);
       if(email.equals(messages.get(i).email))
       {
           my_layout.setVisibility(View.VISIBLE);
           you_layout.setVisibility(View.GONE);
         //  my_name.setText(" ");
           my_message.setText(messages.get(i).message);
       }else
       {
           my_layout.setVisibility(View.GONE);
           you_layout.setVisibility(View.VISIBLE);
           your_name.setText(" "+messages.get(i).email);
           your_message.setText(messages.get(i).message);
       }

        Log.e("customadapter",""+i);
       // icon.setImageResource(flags[i]);
        return view;
    }
    }