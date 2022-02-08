package com.example.slidingarcviewdemo.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.slidingarcviewdemo.R;
import com.example.slidingarcviewdemo.domain.BoxBean;

/**
 * @author 小訾
 * @projectName SlidingArcViewDemo
 * @packageName com.example.slidingarcviewdemo.fragment
 * @description:
 * @date :2022/2/7 10:14
 */
public class TabPicFragment extends Fragment {

    private BoxBean boxBean;

    public TabPicFragment(BoxBean type) {
        super();
        boxBean = type;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_box_pic, null);
    }

    private ImageView iv_tb_box_pic;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_tb_box_pic = view.findViewById(R.id.iv_tb_box_pic);
        initData();
    }

    private void initData() {
        String url = boxBean.getUrl();
        if (!TextUtils.isEmpty(url)) {
            //设置盲盒图片 ic_blind_box
            Glide.with(this)
                    .applyDefaultRequestOptions(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher))
                    .load(url)
                    .into(iv_tb_box_pic);
        } else {
            iv_tb_box_pic.setImageResource(R.mipmap.ic_launcher);
        }
    }
}
