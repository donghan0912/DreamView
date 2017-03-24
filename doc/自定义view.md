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
