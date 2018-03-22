package com.muzi.library;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by muzi on 2018/3/22.
 * 727784430@qq.com
 */

public class SelectState {

    public static final int NONE = 0;//没有选中
    public static final int START = 1;//开始
    public static final int BETWEEN = 2;//中间值
    public static final int END = 3;//结束
    public static final int SINGLE = 4;//开始和结束合并

    @IntDef({NONE, START, BETWEEN, END, SINGLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

}
