package com.example.slidingarcviewdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.slidingarcviewdemo.domain.BoxBean;
import com.example.slidingarcviewdemo.domain.Products;
import com.example.slidingarcviewdemo.fragment.TabPicFragment;
import com.example.slidingarcviewdemo.fragment.ViewPagerAdapter;
import com.example.slidingarcviewdemo.view.CustomBubbleView;
import com.example.slidingarcviewdemo.view.SlidingArcView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager scv_box;
    private CustomBubbleView mBubbleView;
    private SlidingArcView slidingArcView;
    private List<Fragment> fragments;
    private List<BoxBean> list;
    private int index = 0;//选中第条目

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }
    ViewPagerAdapter viewPagerAdapter;
    private void initView() {
        scv_box = findViewById(R.id.scv_box);
        mBubbleView = findViewById(R.id.mBubbleView);
        slidingArcView = findViewById(R.id.slidingArcView);
        list = new ArrayList<>();
        fragments = new ArrayList<>();
         viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, list);
        scv_box.setAdapter(viewPagerAdapter);
        scv_box.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                index = position;

            }

            @Override
            public void onPageSelected(int position) {
                ArrayList<Products> products = list.get(position).getProducts();
                mBubbleView.setProduct(products);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBubbleView.setmBubbleClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null && v.getTag() instanceof Products) {
                    Products products = (Products) v.getTag();
                    Toast.makeText(MainActivity.this, products.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        list.clear();
        fragments.clear();
        for (int i = 0; i < 6; i++) {
            BoxBean boxBean = new BoxBean();
            boxBean.setName("商品盒子" + i);
            boxBean.setUrl("https://img.tuguaishou.com/ips_templ_preview/ab/60/78/lg_2569800_1574230508_5dd4d9ecb3510.jpg!w1024_w?auth_key=2276649122-0-0-3835342d80965ecb1c3acc44cc16317a");
            ArrayList<Products> products = new ArrayList<>();
            products.clear();
            for (int j = 0; j < 4; j++) {
                Products product = new Products();
                product.setUrl("https://img.tuguaishou.com/ips_templ_preview/ab/60/78/lg_2569800_1574230508_5dd4d9ecb3510.jpg!w1024_w?auth_key=2276649122-0-0-3835342d80965ecb1c3acc44cc16317a");
                product.setName("商品" + j);
                products.add(product);
            }
            boxBean.setProducts(products);
            list.add(boxBean);
            fragments.add(new TabPicFragment(boxBean));
        }
        refreshTabLayout(list);
        viewPagerAdapter.notifyDataSetChanged();
        scv_box.setCurrentItem(index);
        mBubbleView.setProduct(list.get(index).getProducts());

    }
    private void refreshTabLayout(List<BoxBean> data){
        if (data != null && data.size()>0) {
            List<String> result = new ArrayList<>();
            for (BoxBean datum : data) {
                result.add(datum.getName());
            }
            slidingArcView.setData(result,result.size());
            slidingArcView.setUpWithViewPager(scv_box);
        }
    }

}