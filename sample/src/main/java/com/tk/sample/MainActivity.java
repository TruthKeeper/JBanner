package com.tk.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tk.jbanner.Indicator;
import com.tk.jbanner.JBanner;
import com.tk.sample.indicator.SampleIndicatorView;
import com.tk.sample.transformer.FadeInTransformer;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements JBanner.OnJBannerListener {

    @BindView(R.id.jbanner)
    JBanner jbanner;
    @BindView(R.id.text_indicator)
    TextView textIndicator;
    private static final String[] STR_1 = new String[]{
            "https://raw.githubusercontent.com/TruthKeeper/JBanner/master/banner/banner_1.jpg",
    };
    private static final String[] STR_3 = new String[]{
            "https://raw.githubusercontent.com/TruthKeeper/JBanner/master/banner/banner_1.jpg",
            "https://raw.githubusercontent.com/TruthKeeper/JBanner/master/banner/banner_2.jpg",
            "https://raw.githubusercontent.com/TruthKeeper/JBanner/master/banner/banner_3.jpg",
    };
    private static final String[] STR_5 = new String[]{
            "https://raw.githubusercontent.com/TruthKeeper/JBanner/master/banner/banner_4.jpg",
            "https://raw.githubusercontent.com/TruthKeeper/JBanner/master/banner/banner_5.jpg",
            "https://raw.githubusercontent.com/TruthKeeper/JBanner/master/banner/banner_6.jpg",
            "https://raw.githubusercontent.com/TruthKeeper/JBanner/master/banner/banner_7.jpg",
            "https://raw.githubusercontent.com/TruthKeeper/JBanner/master/banner/banner_8.jpg",
    };
    @BindView(R.id.indicator_view)
    SampleIndicatorView sampleIndicatorView;
    @BindView(R.id.cb_disable)
    CheckBox cbDisable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        cbDisable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                jbanner.setDisableTouch(isChecked);
            }
        });

        jbanner.addIndicator(sampleIndicatorView);
        jbanner.addIndicator(new Indicator() {
            @Override
            public void onScroll(int position, float offsetX) {
                textIndicator.setText("第" + position + "页\t\t偏移" + offsetX);
            }
        });
        jbanner.setOnJBannerListener(this);
        jbanner.setTransformer(new FadeInTransformer());
    }


    @Override
    public void onClick(int position) {
        Toast.makeText(MainActivity.this, "点击了list第" + position + "张图片", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoad(ImageView imageView, int position) {
        String url;
        if (jbanner.getListData().size() == 1) {
            url = STR_1[position];
        } else if (jbanner.getListData().size() == 3) {
            url = STR_3[position];
        } else {
            url = STR_5[position];
        }
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @OnClick({R.id.start_im, R.id.start, R.id.stop, R.id.size_1, R.id.size_3, R.id.size_5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start_im:
//                SparseIntArray array=new SparseIntArray();
//                array.put(0,1000);
//                array.put(1,2000);
//                array.put(2,3000);
//                array.put(3,4000);
                jbanner.startAutoPlay(true);
                break;
            case R.id.start:
                jbanner.startAutoPlay(false);
                break;
            case R.id.stop:
                jbanner.stopAutoPlay();
                break;
            case R.id.size_1:
                jbanner.initData(new ArrayList<String>(Arrays.asList(STR_1)));
                sampleIndicatorView.refreshUI(1);
                break;
            case R.id.size_3:
                jbanner.initData(new ArrayList<String>(Arrays.asList(STR_3)));
                sampleIndicatorView.refreshUI(3);
                break;
            case R.id.size_5:
                jbanner.initData(new ArrayList<String>(Arrays.asList(STR_5)));
                sampleIndicatorView.refreshUI(5);
                break;
        }
    }
}
