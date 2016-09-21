package com.tk.jbanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.jbanner)
    JBanner jbanner;
    @BindView(R.id.text_indicator)
    TextView textIndicator;
    //    private static final int[] STR = new int[]{
//            R.drawable.icon1,
//            R.drawable.icon2,
//            R.drawable.icon3,
//            R.drawable.icon4,
//            R.drawable.icon5,
//    };
    private static final String[] STR = new String[]{
            "http://img3.imgtn.bdimg.com/it/u=61479672,2332040295&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=185992804,3994750883&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=527015161,3693588828&fm=21&gp=0.jpg",
            "http://image.game.uc.cn/2015/7/1/10648069.jpg",
            "http://image.game.uc.cn/2015/7/1/10648070.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        jbanner.setOmOnItemClickListener(new JBanner.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(MainActivity.this, "点击了list第" + position + "张图片", Toast.LENGTH_SHORT).show();
            }
        });
        jbanner.setIndicator(new Indicator() {
            @Override
            public void onScroll(int position, float offsetX) {
                textIndicator.setText(position + " " + offsetX);
            }
        });
    }

    @OnClick({R.id.start_im, R.id.start, R.id.pause, R.id.size_1, R.id.size_3, R.id.size_5})
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
                break;
        }
    }
}
