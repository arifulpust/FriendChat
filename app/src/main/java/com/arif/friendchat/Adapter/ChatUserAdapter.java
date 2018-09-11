package com.arif.friendchat.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arif.friendchat.Entity.User;
import com.arif.friendchat.Interface.OnItemClickListener;
import com.arif.friendchat.R;
import com.arif.friendchat.Utils.CircleImageView;
import com.arif.friendchat.Utils.CorrectSizeUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.util.List;



/**
 * Created by HP on 2/6/2018.
 */


public class ChatUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder >
{
    List<User> users;

    private Activity mActivity;
    OnItemClickListener onClickItemPosition;
    public ChatUserAdapter(Activity mActivity,
                           List<User> users, OnItemClickListener onClickItemPosition)
    {
        this.users = users;
        this.mActivity = mActivity;
        this.onClickItemPosition = onClickItemPosition;
    }
    public void SetData(List<User> users)
    {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = null;

                view = LayoutInflater.from(mActivity).inflate(R.layout.user_item, parent, false);
        CorrectSizeUtil.getInstance(mActivity).correctSize(view);
                return new ViewHolder(view,viewType);


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position)
    {
final  ViewHolder viewHolder=(ViewHolder) holder1;


//bottom_line_unread
//        viewHolder.coin.setText(""+notifications.get(position).unicoin);
//




//            Glide.with(context).load(users.get(position).email)
//                    .thumbnail(0.5f)
//                    .error(R.drawable.avatar)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(viewHolder.ProfilePick);

        viewHolder.userName.setText(users.get(position).name);
        viewHolder.lastMessage.setText(users.get(position).email);

       // viewHolder.time.setText(""+ Common.getCoomentTime(messages.get(position).created_at)+"");

        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("confirm","call");


                    onClickItemPosition.onClickItem(position);

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return (users.size());
    }
    public class ViewHolder extends RecyclerView.ViewHolder

    {

         TextView userName;
         TextView lastMessage;
         TextView time;
         CircleImageView ProfilePick;
        CardView card_view;


        public ViewHolder(View itemView, int ViewType)
        {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.userName);
            card_view = (CardView) itemView.findViewById(R.id.card_view);

            lastMessage = (TextView) itemView.findViewById(R.id.lastMessage);

            time = (TextView) itemView.findViewById(R.id.time);



            ProfilePick = (CircleImageView) itemView.findViewById(R.id.ProfilePic);


        }


    }
}