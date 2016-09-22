# JBanner
图片广告轮播，自由配置，Just Banner，写完发现了写了个JB。。
##架构：
![](https://github.com/TruthKeeper/JBanner/blob/master/screenshots/framework.jpg)
##目前支持功能：
1.设置轮播list，支持单张无限循环<br><br>
2.开启轮播，关闭轮播<br><br>
3.XML调整轮播速度和切换速度<br><br>
4.XML设置页数最大限制<br><br>
5.XML自定义PageTransformer，自带FadIn（显式），Fold（折叠）<br><br>
6.解耦，外接Indicator（可多个），传入自定义view或者其他接口实现类即可<br><br>
7.将内部adapter的imageview和点击事件暴露出来，方便集成加载框架

###PS：Sample 集成了[ButterKnife](https://github.com/JakeWharton/butterknife)和[Glide](https://github.com/bumptech/glide)，详细内容请移步下载[Smaple](https://github.com/TruthKeeper/JBanner/blob/master/sample_1.1.apk)查看

###自定义属性

```xml
    <declare-styleable name="JBanner">
        <!--页数最大上限，默认5-->
        <attr name="pagerLimit" format="integer" />
        <!--自动播放，默认true-->
        <attr name="autoPlay" format="boolean" />
        <!--轮播时间间隔(ms)，默认5000ms -->
        <attr name="playTime" format="integer" />
        <!--翻页速度(ms)，默认1000ms -->
        <attr name="switchTime" format="integer" />
        <!--翻页动画效果，默认为fadeIn，关闭传入字符串null-->
        <attr name="transformer" format="string" />
    </declare-styleable>
```

###其中自定义 transformer 传入全路径。仿照Google的CoordinatorLayout的反射实现Behavior。
###自带的折叠效果
![](https://github.com/TruthKeeper/JBanner/blob/master/screenshots/fold.gif)  
###DIY的盒式效果
![](https://github.com/TruthKeeper/JBanner/blob/master/screenshots/box.gif)  
