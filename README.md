# 自定义日历选择


## 效果图

![img](https://github.com/mzyq/VerticalCalendar/blob/master/img/preview.gif)

[APK下载](https://w-5.net/YPqDk)

## 使用

[![](https://jitpack.io/v/mzyq/VerticalCalendar.svg)](https://jitpack.io/#mzyq/VerticalCalendar)

### Gradle
```
allprojects {
    repositories {
		...
		maven { url 'https://jitpack.io' }
		}
	}
```

```
dependencies {
        compile 'com.github.mzyq:VerticalCalendar:{VERSION}'
    }
```

### Maven
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```
<dependency>
    <groupId>com.github.mzyq</groupId>
    <artifactId>VerticalCalendar</artifactId>
    <version>{VERSION}</version>
</dependency>
```

>{VERSION}替换为上面的版本号

## 功能
* 默认显示6个月信息
* 连续选择
* 设置不可选项
* 自动调换开始和结束位置
* 回调和get获取选择的日期信息
* 设置可用区间
* 自定义拦截区间
* ***如果喜欢横向滑动，可以切换到```v_horizontal```分支***

## 方法
* ```addUnableDays(int days);```
设置今天之后不可点击选择的天数

* ```resetState();```
取消所有选中，但是不会取消不可点击的天数，可以使用
```addUnableDays(0);```

* 回调
```
setOnCalendarChange(new CalendarView.OnCalendarChange() {
            @Override
            public void onStart(DayBean dayBean) {
            }

            @Override
            public void onEnd(DayBean dayBean) {
            }

            @Override
            public void onDays(int day) {
                super.onDays(day);
            }
        });
```
>DayBean
```
public class DayBean {
    private boolean isTodayDay;
    private int month;
    private int year;
    private int day;

    public DayBean() {
    }

    public boolean isTodayDay() {
        return isTodayDay;
    }

    public void setTodayDay(boolean todayDay) {
        isTodayDay = todayDay;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return year + "-" + month + "-" + day;
    }

    public Calendar getCalendar() {
        return CalendarUtils.getCurreCalendar(year, month - 1, day);
    }
}
```

* get方法

```
      /**
        * 获取开始日期
        *
        * @return
        */
       public DayBean getSelectStartDays() {
           return startSelectBean.getDayBean();
       }

       /**
        * 获取结束日期
        *
        * @return
        */
       public DayBean getSelectEndDays() {
           return endSelectBean.getDayBean();
       }

       /**
        * 获取选择天数
        *
        * @return
        */
       public int getSelectDays() {
           return selectDays;
       }
```
* 设置区间可用（区间以外都不点击）```setEnableRange(Calendar start, Calendar end);```

* 自定义拦截处理```setOnIntercept(OnIntercept onIntercept);```
> 示例：遍历列表设置区间可用
```
 calendarView.setOnIntercept(new OnIntercept() {
            @Override
            public void onIntercept(Calendar today, List<MonthBean> monthList, CalendarAdapter adapter) {
                for (MonthBean monthBean : monthList) {
                    for (DayBean dayBean : monthBean.getDayList()) {
                        if (dayBean.getCalendar().before(activeStart) ||
                                dayBean.getCalendar().after(activeEnd)) {
                            dayBean.setSelectState(SelectState.UNABLE);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
```

## 日志
* 0.1.9:优化，打包源码
* 0.1.8:增加区间和拦截
* 0.1.6-0.1.7:UI调整
* 0.1.5:重新设计在途tag
* 0.1.4:优化在途tag, addUnableDays()-->setUnableDays();
* 0.1.3:增加在途tag
* 0.1.2:优化get返回的数据
* 0.1.1:修复getSelectDays()错误

## 混淆
```
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}
```
