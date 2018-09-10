package com.arif.friendchat.Fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.transition.Fade;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arif.friendchat.R;
import com.arif.friendchat.Utils.CorrectSizeUtil;
import com.arif.friendchat.constant.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class HomeFragment extends Fragment {

Unbinder unbinder;
@BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.odd)
    RelativeLayout odd;
    @BindView(R.id.event)
    RelativeLayout event;
    boolean flag=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        unbinder= ButterKnife.bind(this,view);
        CorrectSizeUtil.getInstance(getActivity()).correctSize(view);
        return view;
    }
@OnClick(R.id.chat)
public void Chat()
{
//    else
//    {
//        flag=true;
//        event.setVisibility(View.VISIBLE);
//        event.animate()
//                .translationXBy(event.getWidth())
//                .translationY(0)
//                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
//
//    }

}
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
