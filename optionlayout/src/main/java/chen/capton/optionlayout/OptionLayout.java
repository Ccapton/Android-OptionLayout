package chen.capton.optionlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by CAPTON on 2017/1/10.
 */

public class OptionLayout extends HorizontalScrollView {

    private String leftOptionText;
    private int leftTextColor;
    private Drawable leftBgColor;
    private int leftTextSize;

    private String rightOptionText;
    private int rightTextColor;
    private Drawable rightBgColor;
    private int rightTextSize;

    private LinearLayout wrapper;
    private ViewGroup leftLayout;
    private LinearLayout rightLayout;
    private Button leftOptionBtn;
    private Button rightOptionBtn;

    GestureDetector detector;
    GestureDetector.SimpleOnGestureListener gesturelistener;

    private  int position;

    public void setPosition(int position){
        this.position=position;
    }
    public int getPositoin(){
        return position;
    }

    public OptionLayout(Context context) {
        this(context,null);
    }

    public OptionLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);

        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.OptionLayout);
        leftOptionText=ta.getString(R.styleable.OptionLayout_leftOptionText);
        rightOptionText=ta.getString(R.styleable.OptionLayout_rightOptionText);
        leftTextColor=ta.getColor(R.styleable.OptionLayout_leftTextColor, Color.WHITE);
        rightTextColor=ta.getColor(R.styleable.OptionLayout_rightTextColor, Color.WHITE);
        leftBgColor=getResources().getDrawable(R.drawable.left_btn);
        rightBgColor=getResources().getDrawable(R.drawable.right_btn);
        leftTextSize= (int) ta.getDimension(R.styleable.OptionLayout_leftTextSize,44);
        rightTextSize= (int) ta.getDimension(R.styleable.OptionLayout_rightTextSize,44);


        rightLayout=new LinearLayout(context);


        leftOptionBtn=new Button(context);
        leftOptionBtn.setBackground(leftBgColor);
        leftOptionBtn.setText(leftOptionText);
        leftOptionBtn.setTextColor(leftTextColor);
        leftOptionBtn.setTextSize(DisplayUtil.px2sp(context,leftTextSize));


        rightOptionBtn=new Button(context);
        rightOptionBtn.setBackground(rightBgColor);
        rightOptionBtn.setText(rightOptionText);
        rightOptionBtn.setTextColor(rightTextColor);
        rightOptionBtn.setTextSize(DisplayUtil.px2sp(context,rightTextSize));

        leftOptionBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(optionClicklistener!=null) {
                    optionClicklistener.leftOptionClick(v,position);
                    smoothScrollTo(0,0);
                }else {
                    Log.w("OptionLayout", "optionClicklistener is null");
                }
            }
        });
        rightOptionBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(optionClicklistener!=null) {
                    optionClicklistener.rightOptionClick(v,position);
                }else {
                    Log.w("OptionLayout", "optionClicklistener is null");
                }
            }
        });

        ta.recycle();
    }

    public OptionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    boolean once;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);

        while (!once) {
            wrapper= (LinearLayout) getChildAt(0);

            leftLayout= (ViewGroup) wrapper.getChildAt(0);
            ViewGroup.LayoutParams lp3 = leftLayout.getLayoutParams();
            lp3.width=widthSize;
            lp3.height=heightSize;
            leftLayout.setLayoutParams(lp3);



            Log.i("leftLayout", ": width "+widthSize);

            if(wrapper.getChildCount()>1){
               for(int i=1;i<wrapper.getChildCount();i++){
                   ViewGroup childGroup= (ViewGroup) wrapper.getChildAt(i);
                   ViewGroup.LayoutParams lp = childGroup.getLayoutParams();
                   lp.width=0;
                   lp.height=0;
                   childGroup.setLayoutParams(lp);
               }
            }

            if (rightLayout == null) {
                Log.i("onMeasure", ": rightLayout is null");
            } else {
                Log.i("onMeasure", ": rightLayout is not null");
            }
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams((int) (widthSize*0.46),heightSize);
            rightLayout.setLayoutParams(lp2);

            final int  rigthLayoutWidth=(int) (widthSize*0.46);


            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,heightSize,1);
            leftOptionBtn.setLayoutParams(lp);
            rightOptionBtn.setLayoutParams(lp);


            rightLayout.addView(leftOptionBtn);
            rightLayout.addView(rightOptionBtn);
            wrapper.addView(rightLayout);

            gesturelistener=new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if(e1.getX()-e2.getX()>=40 ){
                        smoothScrollTo(rigthLayoutWidth,0);
                        return false;
                    }
                    if(e2.getX()-e1.getX()>=20 ){
                        smoothScrollTo(0,0);
                        return false;
                    }
                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if(optionClicklistener!=null) {
                        optionClicklistener.itemClick(OptionLayout.this,position);
                    }else {
                        Log.w("OptionLayout", "optionClicklistener is null");
                    }
                    return false;
                }
            };
            detector=new GestureDetector(gesturelistener);
            once=true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        return true;
    }

    OptionClickListener optionClicklistener;
    public void setOptionClickListener(OptionClickListener listener){
        this.optionClicklistener=listener;
    }
    public interface OptionClickListener{
        void leftOptionClick(View view, int position);
        void rightOptionClick(View view, int position);
        void itemClick(View view, int position);
    }


}
