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

    private LinearLayout wrapper;//外层布局(HorizontalScrollView的唯一子View)，包裹左边的主屏幕显示布局，右边的选项布局
    private ViewGroup leftLayout;//左边的主屏幕显示布局
    private LinearLayout rightLayout;//右边的选项布局
    private Button leftOptionBtn;  //第一个选项按钮
    private Button rightOptionBtn; //第二个选项按钮

    GestureDetector detector;
    GestureDetector.SimpleOnGestureListener gesturelistener;

    private  int position;//保存后续设置的item位置
    int  rigthLayoutWidth; //整个ViewGroup向左滑动的宽度


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
        ta.recycle();

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
                    scrollTo(0,0);
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
                    scrollTo(0,0);
                }else {
                    Log.w("OptionLayout", "optionClicklistener is null");
                }
            }
        });
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
                    scrollTo(0,0);
                }else {
                    Log.w("OptionLayout", "optionClicklistener is null");
                }
                return false;
            }
        };
        detector=new GestureDetector(gesturelistener);
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

            /*
            * 保证布局的唯一性，除了第一个子View外，其他后续的子View不可见。
            * */
            if(wrapper.getChildCount()>1){
               for(int i=1;i<wrapper.getChildCount();i++){
                   ViewGroup childGroup= (ViewGroup) wrapper.getChildAt(i);
                   ViewGroup.LayoutParams lp = childGroup.getLayoutParams();
                   lp.width=0;
                   lp.height=0;
                   childGroup.setLayoutParams(lp);
               }
            }

            /*
            * 设置选项布局的属性。宽度为屏幕宽度的0.46倍，高度与父布局相同
            * */
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams((int) (widthSize*0.46),heightSize);
            rightLayout.setLayoutParams(lp2);
            rigthLayoutWidth=(int) (widthSize*0.46);

             /*
            * 设置两个选项按钮的属性。宽度等分，高度与父布局相同
            * */
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,heightSize,1);
            leftOptionBtn.setLayoutParams(lp);
            rightOptionBtn.setLayoutParams(lp);

            /*
            * 添加按钮到选项布局中，添加选项布局到外层布局中
            * */
            rightLayout.addView(leftOptionBtn);
            rightLayout.addView(rightOptionBtn);
            wrapper.addView(rightLayout);

            once=true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        detector.onTouchEvent(ev);
        return true;
    }

    /*
    * 设置回调函数，让调用者实现点击事件
    * */
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
