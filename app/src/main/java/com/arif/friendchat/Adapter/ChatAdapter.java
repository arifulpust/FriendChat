package com.arif.friendchat.Adapter;


import android.app.Activity;
import android.content.Context;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arif.friendchat.Entity.ChatMessage;
import com.arif.friendchat.R;
import com.arif.friendchat.Utils.CorrectSizeUtil;

import java.util.List;



//import com.android.volley.toolbox.ImageLoader;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    List<ChatMessage> messages;
    private Activity mActivity;
    String senderName;
    String email;
    private static LayoutInflater inflater = null;
    public ChatAdapter(Activity mActivity, List<ChatMessage> messages, String senderName,String email) {
        this.mActivity = mActivity;
        this.messages = messages;
        this.email = email;
        this.senderName = senderName;

        this.inflater  = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.message_listview, parent, false);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);
        return  new ViewHolder(view);
    }
    public void SetData(List<ChatMessage> messages)
    {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      public   TextView your_message;
        public  TextView your_name;
        public TextView my_message;
        public  TextView my_name;
        public  LinearLayout you_layout;
        public  LinearLayout my_layout;

        public ViewHolder(View view) {
            super(view);
          this.   your_message = (TextView)           view.findViewById(R.id.your_message);
            this.  your_name = (TextView) view.findViewById(R.id.your_name);
            this.   my_message = (TextView) view.findViewById(R.id.my_message);
            this.  my_name = (TextView) view.findViewById(R.id.my_name);
            this.    you_layout = (LinearLayout) view.findViewById(R.id.you_layout);
            this.   my_layout = (LinearLayout) view.findViewById(R.id.my_layout);

        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        final ViewHolder holder=(ViewHolder)holder1;
        if(email.equals(messages.get(position).email))
        {
            holder.  my_layout.setVisibility(View.VISIBLE);
            holder.   you_layout.setVisibility(View.GONE);
            holder.   my_name.setText(" "+messages.get(position).email);
            holder.   my_name.setVisibility(View.GONE);
            holder.   my_message.setText(messages.get(position).message);
        }else
        {
            holder.   my_layout.setVisibility(View.GONE);
            holder.  you_layout.setVisibility(View.VISIBLE);
            holder.  your_name.setText(" "+senderName);
            holder. your_message.setText(messages.get(position).message);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }


}