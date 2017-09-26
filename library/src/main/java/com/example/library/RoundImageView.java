package com.example.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by GaoSheng on 2016/12/17.
 * 18:07
 *
 * @VERSION V1.4
 * com.example.gs.roundimageview
 * 自定义三步 ,1 重写onmeasure() 2,重写 onlayout() (继承ViewGroup需要,反之不需要) 3 重写 ondraw()
 */

public class RoundImageView extends ImageView {


    private Paint mPaint;
    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private Bitmap mMaskBitmap;

    private WeakReference<Bitmap> mWeakBitmap;

    /**
     * 图片的类型 圆角or 圆形
     */
    private int type;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;


    /**
     * 默认半径的大小
     */
    private int DEFAULT_RADIUS = 10;

    //半径的大小
    private int mBorderRadius;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //拿到自定义的所有的属性
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable
                .roundImageViewAttrs, 0, 0);

        //设置默认半径大小
        mBorderRadius = typedArray.getDimensionPixelSize(R.styleable
                        .roundImageViewAttrs_borderRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS,
                        getResources().getDisplayMetrics()));
        //设置默认类型
        type = typedArray.getInt(R.styleable.roundImageViewAttrs_type, 1);

        typedArray.recycle();

        initData(context);
    }

    private void initData(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //如果是圆形,强制宽高一样
        if (type == TYPE_CIRCLE) {
            //保证能拿到数据
//            measure(0, 0);
            int width = Math.min(getMeasuredHeight(), getMeasuredWidth());
            //保存数据,申请空间
            setMeasuredDimension(width, width);
        }
    }


    @Override
    public void invalidate() {
        mWeakBitmap = null;
        if (mMaskBitmap != null) {
            mMaskBitmap.recycle();
            mMaskBitmap = null;
        }
        super.invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //缓存图片
        Bitmap bitmap = mWeakBitmap == null ? null : mWeakBitmap.get();

        if (bitmap == null || bitmap.isRecycled()) {

            //拿到drwaable (图片)
            Drawable drawable = getDrawable();

            if (drawable != null) {

                //拿到图片的原始宽高
                int dHeight = drawable.getIntrinsicHeight();
                int dWidth = drawable.getIntrinsicWidth();

                //创建bitmap
                bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                        Bitmap.Config.ARGB_8888);
                float scale = 1.0f;
                //创建画布
                Canvas drawCanvas = new Canvas(bitmap);
//                drawCanvas.save();
                //按照bitmap的宽高，以及view的宽高，计算缩放比例；因为设置的src宽高比例可能和imageview的宽高比例不同，这里我们不希望图片失真；
                if (type == TYPE_ROUND) {
                    // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
                    scale = Math.max(getWidth() * 1.0f / dWidth, getHeight()
                            * 1.0f / dHeight);
                } else {
                    scale = getWidth() * 1.0F / Math.min(dWidth, dHeight);
                }
                //根据缩放比例，设置bounds，相当于缩放图片了,实际上是设置绘图区域
                drawable.setBounds(0, 0, (int) (scale * dWidth),
                        (int) (scale * dHeight));
                //完成图片的缩放设置
                drawable.draw(drawCanvas);
                if (mMaskBitmap == null || mMaskBitmap.isRecycled()) {
                    mMaskBitmap = createBitmap();
                }

                //重置的画笔,难免干扰
                mPaint.reset();
                mPaint.setFilterBitmap(false);
                mPaint.setXfermode(mXfermode);
                //绘制形状
                drawCanvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
                mPaint.setXfermode(null);
//                drawCanvas.restore();

                //将准备好的bitmap绘制出来
                canvas.drawBitmap(bitmap, 0, 0, null);
                //bitmap缓存起来，避免每次调用onDraw，分配内存
                mWeakBitmap = new WeakReference<Bitmap>(bitmap);

            }

        }

        //如果bitmap还存在，则直接绘制即可
        if (bitmap != null) {
            mPaint.setXfermode(null);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint);
            return;
        }
    }

    /**
     * 绘制形状,在bitmap画圆角矩形跟圆形
     *
     * @return
     */
    public Bitmap createBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()),
                    mBorderRadius, mBorderRadius, paint);
        } else {
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2,
                    paint);
        }

        return bitmap;
    }


    public void setmBorderRadius(int mBorderRadius) {
        this.mBorderRadius = mBorderRadius;
    }

    public void setType(int type) {

        if (type != TYPE_CIRCLE || type != TYPE_ROUND) {
            type = TYPE_CIRCLE;
        } else {
            this.type = type;
        }

    }
}
