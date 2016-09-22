package com.tk.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tk.jbanner.Indicator;
import com.tk.jbanner.JBanner;
import com.tk.sample.indicator.SampleIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements JBanner.OnJBannerListener {

    @BindView(R.id.jbanner)
    JBanner jbanner;
    @BindView(R.id.text_indicator)
    TextView textIndicator;
    private static final String[] STR = new String[]{
            "http://img3.imgtn.bdimg.com/it/u=61479672,2332040295&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=185992804,3994750883&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=527015161,3693588828&fm=21&gp=0.jpg",
            "http://image.game.uc.cn/2015/7/1/10648069.jpg",
            "http://image.game.uc.cn/2015/7/1/10648070.jpg"
    };
    @BindView(R.id.indicator_view)
    SampleIndicatorView sampleIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        jbanner.addIndicator(sampleIndicatorView);
        jbanner.addIndicator(new Indicator() {
            @Override
            public void onScroll(int position, float offsetX) {
                textIndicator.setText("第" + position + "\t\t偏移" + offsetX);
            }
        });
        jbanner.setmOnJBannerListener(this);
    }

    @OnClick({R.id.start_im, R.id.start, R.id.pause,
            R.id.size_1, R.id.size_3, R.id.size_5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_im:
                jbanner.startAutoPlay(true);
                break;
            case R.id.start:
                jbanner.startAutoPlay(false);
                break;
            case R.id.pause:
                jbanner.stopAutoPlay();
                break;
            case R.id.size_1:
                List<JBannerBean> list = new ArrayList<JBannerBean>();
                JBannerBean bannerBean = new JBannerBean();
                bannerBean.setImgUrl(STR[0]);
                list.add(bannerBean);
                jbanner.setJBannerList(list);
                sampleIndicatorView.refreshUI(1);
                break;
            case R.id.size_3:
                List<JBannerBean> list2 = new ArrayList<JBannerBean>();
                JBannerBean bannerBean2;
                for (int i = 0; i < 3; i++) {
                    bannerBean2 = new JBannerBean();
                    bannerBean2.setImgUrl(STR[i]);
                    list2.add(bannerBean2);
                }
                jbanner.setJBannerList(list2);
                sampleIndicatorView.refreshUI(3);
                break;
            case R.id.size_5:
                List<JBannerBean> list3 = new ArrayList<JBannerBean>();
                JBannerBean bannerBean3;
                for (int i = 0; i < 5; i++) {
                    bannerBean3 = new JBannerBean();
                    bannerBean3.setImgUrl(STR[i]);
                    list3.add(bannerBean3);
                }
                jbanner.setJBannerList(list3);
                sampleIndicatorView.refreshUI(5);
                break;
        }
    }

    @Override
    public void onClick(int position) {
        Toast.makeText(MainActivity.this, "点击了list第" + position + "张图片", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoad(ImageView imageView, int position) {
        Glide.with(this)
                .load(STR[position])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
