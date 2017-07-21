 ViewDragHelper源码 {

    STATE_IDLE      空闲状态
    STATE_DRAGGING  拖动状态
    STATE_SETTLING  安置状态(自动滚动过程)

    // Pointer 触摸点：处理多点触控
    // 每个Pointer有自己的id和index
    public boolean shouldInterceptTouchEvent(MotionEvent ev) {
        // 获取当前event的action
        final int action = MotionEventCompat.getActionMasked(ev);
        final int actionIndex = MotionEventCompat.getActionIndex(ev);

        // 如果当前action是按下事件，重置一些信息
        if (action == MotionEvent.ACTION_DOWN) {
            // Reset things for a new event stream, just in case we didn't get
            // the whole previous stream.
            cancel();
        }

        // 初始化事件速率
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                // 获取第一个触摸点Pointer的pointerId
                final int pointerId = ev.getPointerId(0);
                // 保存触摸信息(触摸点的x、y坐标，边缘触控等信息)
                saveInitialMotion(x, y, pointerId);
                // 获取当前触摸点下最顶层的子View
                // 注意该方法中，调用了callback 中的 getOrderedChildIndex()方法
                // 所以我们就可以指定捕获哪个view，使用场景：比如点击到层叠在一起的两个View内
                final View toCapture = findTopChildUnder((int) x, (int) y);

                //如果toCapture是已经捕获的View,而且正在处于被释放状态
                //那么就重新捕获
                if (toCapture == mCapturedView && mDragState == STATE_SETTLING) {
                    tryCaptureViewForDrag(toCapture, pointerId);
                }
                //如果触摸了边缘,回调callback的onEdgeTouched()方法
                final int edgesTouched = mInitialEdgesTouched[pointerId];
                if ((edgesTouched & mTrackingEdges) != 0) {
                    mCallback.onEdgeTouched(edgesTouched & mTrackingEdges, pointerId);
                }
                break;
            }

            // 当又有一个手指触摸时
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int pointerId = ev.getPointerId(actionIndex);
                final float x = ev.getX(actionIndex);
                final float y = ev.getY(actionIndex);
                // 保存触摸信息
                saveInitialMotion(x, y, pointerId);

                //因为同一时间ViewDragHelper只能操控一个View,所以当有新的手指触摸时
                if (mDragState == STATE_IDLE) {
                    //1. 无触摸发生时,如果是边缘触摸，则回调callback.onEdgeTouched()方法
                    final int edgesTouched = mInitialEdgesTouched[pointerId];
                    if ((edgesTouched & mTrackingEdges) != 0) {
                        mCallback.onEdgeTouched(edgesTouched & mTrackingEdges, pointerId);
                    }
                } else if (mDragState == STATE_SETTLING) {
                    //2. 正在处于释放状态时重新捕获View
                    // Catch a settling view if possible.
                    final View toCapture = findTopChildUnder((int) x, (int) y);
                    if (toCapture == mCapturedView) {
                        tryCaptureViewForDrag(toCapture, pointerId);
                    }
                }
                break;
            }

            //当手指移动时
            case MotionEvent.ACTION_MOVE: {
                if (mInitialMotionX == null || mInitialMotionY == null) break;

                // First to cross a touch slop over a draggable view wins. Also report edge drags.
                //得到触摸点的数量,并循环处理,只处理第一个发生了拖拽的事件
                final int pointerCount = ev.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    final int pointerId = ev.getPointerId(i);

                    // If pointer is invalid then skip the ACTION_MOVE.
                    if (!isValidPointerForActionMove(pointerId)) continue;

                    final float x = ev.getX(i);
                    final float y = ev.getY(i);
                    //获得拖拽偏移量
                    final float dx = x - mInitialMotionX[pointerId];
                    final float dy = y - mInitialMotionY[pointerId];

                    //获取当前触摸点下最顶层的子View
                    final View toCapture = findTopChildUnder((int) x, (int) y);

                    //如果找到了最顶层View,并且产生了拖动(checkTouchSlop()返回true)
                    final boolean pastSlop = toCapture != null && checkTouchSlop(toCapture, dx, dy);
                    if (pastSlop) {
                        //根据callback的四个方法getView[Horizontal|Vertical]DragRange和
                        //clampViewPosition[Horizontal|Vertical]来检查是否可以拖动(或者说是确定拖动范围)
                        final int oldLeft = toCapture.getLeft();
                        final int targetLeft = oldLeft + (int) dx;
                        final int newLeft = mCallback.clampViewPositionHorizontal(toCapture,
                                targetLeft, (int) dx);
                        final int oldTop = toCapture.getTop();
                        final int targetTop = oldTop + (int) dy;
                        final int newTop = mCallback.clampViewPositionVertical(toCapture, targetTop,
                                (int) dy);
                        final int horizontalDragRange = mCallback.getViewHorizontalDragRange(
                                toCapture);
                        final int verticalDragRange = mCallback.getViewVerticalDragRange(toCapture);
                        //如果都不允许移动则跳出循环
                        if ((horizontalDragRange == 0 || horizontalDragRange > 0
                                && newLeft == oldLeft) && (verticalDragRange == 0
                                || verticalDragRange > 0 && newTop == oldTop)) {
                            // 如果callback.clampViewPositionVertical(clampViewPositionHorizontal)的返回值
                            //       和原值相同，表示不能移动
                            break;
                        }
                    }
                    //记录并回调是否有边缘触摸
                    reportNewEdgeDrags(dx, dy, pointerId);
                    if (mDragState == STATE_DRAGGING) {
                        // Callback might have started an edge drag
                        break;
                    }

                    //如果产生了拖动则调用tryCaptureViewForDrag()
                    if (pastSlop && tryCaptureViewForDrag(toCapture, pointerId)) {
                        break;
                    }
                }
                //保存触摸点的信息
                saveLastMotion(ev);
                break;
            }

            //当有一个手指抬起时,清除这个手指的触摸数据
            case MotionEventCompat.ACTION_POINTER_UP: {
                final int pointerId = ev.getPointerId(actionIndex);
                clearMotionHistory(pointerId);
                break;
            }

            //清除所有触摸数据
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                cancel();
                break;
            }
        }

        return mDragState == STATE_DRAGGING;
    }



    public void processTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        final int actionIndex = MotionEventCompat.getActionIndex(ev);

        ..（省去部分代码）
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                ..（省去部分代码）
                break;
            }

            case MotionEventCompat.ACTION_POINTER_DOWN: {

                if (mDragState == STATE_IDLE) {
                    // 如果当前是空置状态，尝试捕获view
                    final View toCapture = findTopChildUnder((int) x, (int) y);
                    tryCaptureViewForDrag(toCapture, pointerId);

                    final int edgesTouched = mInitialEdgesTouched[pointerId];
                    if ((edgesTouched & mTrackingEdges) != 0) {
                        mCallback.onEdgeTouched(edgesTouched & mTrackingEdges, pointerId);
                    }
                } else if (isCapturedViewUnder((int) x, (int) y)) {
                    // 如果已经捕获的captureView 还在当前触摸点下，再次尝试捕获view
                    tryCaptureViewForDrag(mCapturedView, pointerId);
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                //如果现在已经是拖拽状态
                if (mDragState == STATE_DRAGGING) {
                    ..（省去部分代码）
                    //拖拽至指定位置
                    dragTo(mCapturedView.getLeft() + idx, mCapturedView.getTop() + idy, idx, idy);
                } else {
                    // Check to see if any pointer is now over a draggable view.
                    //如果还不是拖拽状态,遍历触摸点数量，捕获view
                    final int pointerCount = ev.getPointerCount();
                    for (int i = 0; i < pointerCount; i++) {
                        ..（省去部分代码）
                    }

                }
                break;
            }

            //当多个手指中的一个手机松开时
            case MotionEventCompat.ACTION_POINTER_UP: {
                final int pointerId = ev.getPointerId(actionIndex);
                //如果当前抬起触摸点正在被拖拽,则在剩余的触摸点中寻找是否正在View上
                if (mDragState == STATE_DRAGGING && pointerId == mActivePointerId) {
                    // Try to find another pointer that's still holding on to the captured view.
                    int newActivePointer = INVALID_POINTER;
                    final int pointerCount = ev.getPointerCount();
                    for (int i = 0; i < pointerCount; i++) {
                        final int id = ev.getPointerId(i);
                        if (id == mActivePointerId) {
                            // This one's going away, skip.
                            continue;
                        }

                        final float x = ev.getX(i);
                        final float y = ev.getY(i);
                        if (findTopChildUnder((int) x, (int) y) == mCapturedView
                                && tryCaptureViewForDrag(mCapturedView, id)) {
                            newActivePointer = mActivePointerId;
                            break;
                        }
                    }

                    if (newActivePointer == INVALID_POINTER) {
                        // We didn't find another pointer still touching the view, release it.
                        //如果没找到则释放View
                        releaseViewForPointerUp();
                    }
                }
                clearMotionHistory(pointerId);
                break;
            }

            case MotionEvent.ACTION_UP: {
                if (mDragState == STATE_DRAGGING) {
                    //如果是拖拽状态的释放则调用
                    releaseViewForPointerUp();
                }
                cancel();
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                if (mDragState == STATE_DRAGGING) {
                    dispatchViewReleased(0, 0);
                }
                cancel();
                break;
            }
        }
    }




    findTopChildUnder()方法
    public View findTopChildUnder(int x, int y) {
        final int childCount = mParentView.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            // 这里调用了callback的getOrderedChildIndex()方法
            final View child = mParentView.getChildAt(mCallback.getOrderedChildIndex(i));
            if (x >= child.getLeft() && x < child.getRight()
                    && y >= child.getTop() && y < child.getBottom()) {
                return child;
            }
        }
        return null;
    }

    checkTouchSlop()方法，判断是否是拖动动作
    private boolean checkTouchSlop(View child, float dx, float dy) {
        if (child == null) {
            return false;
        }
        // 注意，这里调用了callback.getViewHorizontalDragRange()和getViewVerticalDragRange()方法
        final boolean checkHorizontal = mCallback.getViewHorizontalDragRange(child) > 0;
        final boolean checkVertical = mCallback.getViewVerticalDragRange(child) > 0;

        if (checkHorizontal && checkVertical) {
            return dx * dx + dy * dy > mTouchSlop * mTouchSlop;
        } else if (checkHorizontal) {
            return Math.abs(dx) > mTouchSlop;
        } else if (checkVertical) {
            return Math.abs(dy) > mTouchSlop;
        }
        return false;
    }
    用来根据mTouchSlop最小拖动的距离来判断是否属于拖动,mTouchSlop根据我们设定的灵敏度决定.

    tryCaptureViewForDrag()方法
    boolean tryCaptureViewForDrag(View toCapture, int pointerId) {
        // 如果已经捕获该view，直接返回true
        if (toCapture == mCapturedView && mActivePointerId == pointerId) {
            // Already done!
            return true;
        }
        //根据mCallback.tryCaptureView()方法来最终决定是否可以捕获View
        if (toCapture != null && mCallback.tryCaptureView(toCapture, pointerId)) {
            mActivePointerId = pointerId;
            //如果可以则调用captureChildView()
            captureChildView(toCapture, pointerId);
            // 返回true
            return true;
        }
        return false;
    }

    public void captureChildView(View childView, int activePointerId) {
        if (childView.getParent() != mParentView) {
            throw new IllegalArgumentException("captureChildView: parameter must be a descendant "
                    + "of the ViewDragHelper's tracked parent view (" + mParentView + ")");
        }
        //赋值mCapturedView
        mCapturedView = childView;
        mActivePointerId = activePointerId;
        // 回调callback。onViewCaptured()方法
        mCallback.onViewCaptured(childView, activePointerId);
        // 设置mDragState状态为滑动状态
        setDragState(STATE_DRAGGING);
    }
    如果程序执行到这里,就证明View已经处于拖拽状态了,后续的触摸操作,将直接根据mDragState为STATE_DRAGGING的状态处理.

    dragTo()方法
    private void dragTo(int left, int top, int dx, int dy) {
        int clampedX = left;
        int clampedY = top;
        final int oldLeft = mCapturedView.getLeft();
        final int oldTop = mCapturedView.getTop();
        if (dx != 0) {
            //回调callback来决定View最终被拖拽的x方向上的偏移量
            clampedX = mCallback.clampViewPositionHorizontal(mCapturedView, left, dx);
            //移动View
            ViewCompat.offsetLeftAndRight(mCapturedView, clampedX - oldLeft);
        }
        if (dy != 0) {
            //回调callback来决定View最终被拖拽的y方向上的偏移量
            clampedY = mCallback.clampViewPositionVertical(mCapturedView, top, dy);
            //移动View
            ViewCompat.offsetTopAndBottom(mCapturedView, clampedY - oldTop);
        }

        if (dx != 0 || dy != 0) {
            final int clampedDx = clampedX - oldLeft;
            final int clampedDy = clampedY - oldTop;
            // 调用callback的onViewPositionChanged方法
            mCallback.onViewPositionChanged(mCapturedView, clampedX, clampedY,
                    clampedDx, clampedDy);
        }
    }
    因为dragTo()方法是在processTouchEvent()中的MotionEvent.ACTION_MOVE case被调用所以当程序运行到这里时View就会不断的被拖动了。
    如果一旦手指释放则最终会调用releaseViewForPointerUp()方法

    releaseViewForPointerUp()方法
    private void releaseViewForPointerUp() {
        //计算出当前x和y方向上的加速度
        mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
        final float xvel = clampMag(
                VelocityTrackerCompat.getXVelocity(mVelocityTracker, mActivePointerId),
                mMinVelocity, mMaxVelocity);
        final float yvel = clampMag(
                VelocityTrackerCompat.getYVelocity(mVelocityTracker, mActivePointerId),
                mMinVelocity, mMaxVelocity);
        dispatchViewReleased(xvel, yvel);
    }
    计算完加速度后就调用了dispatchViewReleased():

    private void dispatchViewReleased(float xvel, float yvel) {
        //设定当前正处于释放阶段
        mReleaseInProgress = true;
        //回调callback的onViewReleased()方法
        mCallback.onViewReleased(mCapturedView, xvel, yvel);
        mReleaseInProgress = false;

        //设定状态
        if (mDragState == STATE_DRAGGING) {
            // onViewReleased didn't call a method that would have changed this. Go idle.
            //如果onViewReleased()中没有调用任何方法,则状态设定为STATE_IDLE
            setDragState(STATE_IDLE);
        }
    }

    所以最后释放后的处理交给了callback中的onViewReleased()方法,如果我们什么都不做,那么这个被拖拽的View就是停止在当前位置,或者我们可以调用ViewDragHelper提供给我们的这几个方法:

    settleCapturedViewAt(int finalLeft, int finalTop) 以松手前的滑动速度为初速动，让捕获到的View自动滚动到指定位置。只能在Callback的onViewReleased()中调用。
    flingCapturedView(int minLeft, int minTop, int maxLeft, int maxTop) 以松手前的滑动速度为初速动，让捕获到的View在指定范围内fling。只能在Callback的onViewReleased()中调用。
    smoothSlideViewTo(View child, int finalLeft, int finalTop) 指定某个View自动滚动到指定的位置，初速度为0，可在任何地方调用。


    1. 第一次点击的时候，callback中调用的方法
            getOrderedChildIndex()(如果是边缘触控，还会调用onEdgeTouched()方法)
    2. 手指移动的时候，会调用callback中的
            getViewHorizontalDragRange()和getViewVerticalDragRange()方法
    3. 当手指移动为拖动行为时，会调用callback中的
            // toCapture 当前拖拽View，targetLeft: getLeft()值 dx: x轴偏移量(注意是手指前后两次触摸的偏移量，实际开发中基本用不到)
            clampViewPositionHorizontal(toCapture, targetLeft,(int) dx)
            // toCapture 当前拖拽View，targetTop: getLeft()值 dy: y轴偏移量
            clampViewPositionVertical(toCapture, targetTop,(int) dy)
            注意：这两个方法的返回值最终决定，当前view是否能移动，能移动的位置
                    1. 如果返回的是view.getLeft()值，那么将导致view不能移动，可以在shouldInterceptTouchEvent MOVE事件源码中看结果
                    2. 在dragTo()方法中，决定移动位置
    4. boolean tryCaptureView(View child, int pointerId); // 尝试捕获被拖拽的view
            child // 当前触摸子View
            pointerId // 触摸点Pointer的id

    5. onViewCaptured(View capturedChild, int activePointerId) {}
            capturedChild 被捕获的子view
    6. public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {}
            //当View被拖拽位置发生改变时回调(拖拽移动的时候)
            //changedView ：被拖拽的View
            //left : 被拖拽后View的left边缘坐标(getLeft()值)
            //top : 被拖拽后View的top边缘坐标(getTop()值)
            //dx : 拖动的x轴偏移量
            //dy : 拖动的y轴偏移量
    7. public void onViewReleased(View releasedChild, float xvel, float yvel) {}
            //当被捕获拖拽的View被释放是回调(松手时回调)
            //releasedChild : 被释放的View
            //xvel : 释放View的x方向上的加速度
            //yvel : 释放View的y方向上的加速度
	8. public int getViewHorizontalDragRange(View child) {
            return 0;
        }
		当拖拽的子View可以直接消费处理触摸事件时，这个方法必须要重写，且返回值要大于0
		注意：这个值，并不是允许拖拽的区域
			private int computeAxisDuration(int delta, int velocity, int motionRange) {// motionRange就是getViewHorizontalDragRange()返回值
        省略代码...
        int duration;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
        } else {
			// 这个返回值得作用，仅仅是当velocity速率为0的时候，
				// startScroll执行的时间
            final float range = (float) Math.abs(delta) / motionRange;
            duration = (int) ((range + 1) * BASE_SETTLE_DURATION);
        }
        return Math.min(duration, MAX_SETTLE_DURATION);
    }

