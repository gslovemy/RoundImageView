                Android自定义圆角图片
  * 序言
     >在写项目的时候用到了圆角图片,可是又不想导入fresco ,glide这些比较大的库,虽然他们做的比较好,功能全,但是对于项目来说如果仅仅就是个圆角图片就导入的话,太浪费资源了,于是打算自定义一个.
     
  * 自定义View的顺序
    <ol>
      <li> 选择继承View(包括widget中的控件),还是ViewGroup(报告四大布局)</li>
      <li> 重写onmeaure()方法 测量,重难点,需要了解测量模式.</li>
      <li> 重写 onlayout() 摆放位置,重难点(如果继承View不用重写此方法,反之)</li>
      <li> 重写 onDraw() 绘画 ,重难点(如果继承ViewGroup不用重写此方法,反之)
    </ol>

 * 自定义属性的顺序
   <ol>
   <li> 在res/values,新建attrs.xml.
    >       <?xml version="1.0" encoding="utf-8"?>
    		<resources>
    		<!--弧度的大小-->
    		<attr name="borderRadius" format="dimension"/>
    		<!--圆形图片和带有圆角的图片-->
    		<attr name="type" format="reference">
    		<enum name="circle" value="0"/>
    		<enum name="round" value="1"/>
    		</attr>
    		<declare-styleable name="roundImageViewAttrs">
    		<!--直接引用上面的-->
    		<attr name="borderRadius"/>
    		<attr name="type"/>
    		</declare-styleable>
    		</resources>
    </li>

     <li>自定义View,参考上面 </li>
	 <li>再布局文件中,引入xmln(命名空间), xmlns:gs="http://schemas.android.com/apk/res-auto" ,gs属于自定义的,随便写 </li>
	 <li>控件使用,需要先依赖libray,或者拷贝源码到项目中.
    >        <com.example.library.RoundImageView
		        android:layout_marginTop="100dp"
		        android:id="@+id/circle"
		        android:layout_width="wrap_content"
		        android:layout_height="wrap_content"
		        android:layout_centerHorizontal="true"
		        android:src="@drawable/a"
		        gs:type="circle"
        />
    </li>
		</li>


 
  * 效果图
    
	![](http://i.imgur.com/et1ab56.png)

   