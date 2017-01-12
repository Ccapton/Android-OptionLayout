# OptionLayout
> 关于我，欢迎关注  
  博客：ccapton(http://blog.csdn.net/ccapton) 微信：[Ccapton]()   
 
###简介: 

这是一个仿照QQ的自定义View，实现侧滑弹出选项按钮的效果。

###示例:  

在QQ中是这样的

![](https://raw.githubusercontent.com/Ccapton/optionLayout/master/qq.gif)

而我的效果是这样的

![](https://raw.githubusercontent.com/Ccapton/optionLayout/master/my.gif)



###特性 

左滑出现选项按钮，例如“置顶”,“删除”。可以改为自己想要的名称，功能也可自定义
###原理说明
这是一个自定义HorizontalScrollView，在里面动态添加了两个选项按钮，通过手势滑动监测实现滚动，从而显示出选项菜单。

### 如何配置

下载optionlayout这个目录下的所有内容，拷贝到你的项目中即可使用;也可以将整个optionlayout这个文件夹作为一个独立module，import进项目中。

###使用方法

在layout目录下新建一个xml布局文件，命名自定义(例如:demo中的option_laout.xml)，内容格式如下

``` xml
<?xml version="1.0" encoding="utf-8"?>
<chen.capton.optionlayout.OptionLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:OptionLayout="http://schemas.android.com/apk/res-auto" //自命名空间
    android:layout_height="70dp" //自定义高度
    android:layout_width="match_parent" 
    OptionLayout:leftOptionText="置顶"  //左边按钮的文字
    OptionLayout:leftTextSize="14sp"    //左边按钮文字的大小 （若不写，默认14sp）
    OptionLayout:leftTextColor="@android:color/white"  //左边按钮的字体颜色
    OptionLayout:rightOptionText="删除"      //右边按钮的文字
    OptionLayout:rightTextSize="14sp"         //右边按钮文字的大小 （若不写，默认14sp）
    OptionLayout:rightTextColor="@android:color/white"  //右边按钮的字体颜色
    >
    <LinearLayout
        android:orientation="horizontal"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

          <!--把你自己的item布局include进来-->
        <include layout="@layout/left_item"  />

    </LinearLayout>

</chen.capton.optionlayout.OptionLayout>
``` 
在例如ListView中的BaseAdapter中的getView方法中使用

(1)适配器实现接口以完成回调
``` code
   class Adapter extends BaseAdapter implements OptionLayout.OptionClickListener{
       ...省略代码...
         @Override
        public void leftOptionClick(View view,int postition) {
            //点击左边的按钮实现的事件，view为按钮对象，position为后续设置的item所在位置
        }

        @Override
        public void rightOptionClick(View view,int postition) {
             //点击右边的按钮实现的事件，view为按钮对象，position为后续设置的item所在位置
        }

        @Override
        public void itemClick(View view,int postition) {
             //点击整个item实现的事件，view为整个OptionLayout对象，position为后续设置的item所在位置
        }
   }
``` 
(2)OptionLayout在适配器中的使用
``` code
   @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if(convertView==null){
                convertView= inflater.inflate(R.layout.option_layout,parent,false);//R.layout.option_layout即为我们自己写的一个xml文件，把它inflate进来就可以了
                viewHolder=new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }

            ((OptionLayout)convertView).setOptionClickListener(this); //强转为我们的OptionLaout,为适配器回调事件，例如点击事件，以实现我们的效果
            ((OptionLayout)convertView).setPosition(position);       // 设置OptionLayout这个View在ListView中的位置。
            viewHolder.textView.setText(dataList.get(position));

            return convertView;
        }
```
### 作者的话
 挺简单的一个自定义View，主要是实现了我想要效果就行，大家试试就行，源码仅供参考。

