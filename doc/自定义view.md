http://www.gcssloop.com/customview/CustomViewIndex/

http://blog.csdn.net/guolin_blog/article/details/17357967

Android自定义属性，attr format取值类型
	1. reference：参考某一资源ID
	2. color：颜色值
	3. boolean：布尔值
	4. dimension：尺寸值
	5. float：浮点值
	6. integer：整型值
	7. string：字符串
	8. fraction：百分数
	9. enum：枚举值
	10. flag：位或运算
	
LayoutInflater 工作原理：
XmlPullParser


Inflate(resId , null ) 只创建temp ,返回temp
Inflate(resId , parent, false )创建temp，然后执行temp.setLayoutParams(params);返回temp
Inflate(resId , parent, true ) 创建temp，然后执行root.addView(temp, params);最后返回root

inflate()方法源码
 View result = root;
// Temp is the root view that was found in the xml
final View temp = createViewFromTag(root, name, inflaterContext, attrs);

ViewGroup.LayoutParams params = null;

if (root != null) {
	// Create layout params that match root, if supplied
	params = root.generateLayoutParams(attrs);
	if (!attachToRoot) {
		temp.setLayoutParams(params);
	}
  }
                  
if (root != null && attachToRoot) {
	root.addView(temp, params);
}

if (root == null || !attachToRoot) {
	result = temp;
}

return result;




View绘制流程：
1. onMeasure()
	MeasureSpec的specMode,一共三种类型：
	EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
	AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
	UNSPECIFIED：表示子布局想要多大就多大，很少使用

setMeasuredDimension():设置view宽高 

invalidate() 视图重绘，但这时measure和layout流程是不会重新执行的
requestLayout() 视图的绘制流程完整走一遍


使用自定义属性：
TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
defStyleAttr、defStyleRes 默认为0，即不使用系统默认值。

注意：使用自定义属性的时候，要调用a.recycle(),释放对象，方便复用。