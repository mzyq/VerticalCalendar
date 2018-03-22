package com.muzi.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.muzi.library.adapter.CalendarAdapter;
import com.muzi.library.bean.DayBean;
import com.muzi.library.bean.MonthBean;
import com.muzi.library.bean.SelectBean;
import com.muzi.library.manager.MLinearLayoutManager;
import com.muzi.library.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by muzi on 2018/3/21.
 * 727784430@qq.com
 */

public class CalendarView extends RelativeLayout {

    /**
     * 月数
     */
    private int maxMonth = 6;
    /**
     * 日历数据
     */
    private DayBean dayBean;
    private MonthBean monthBean;
    private List<DayBean> dayList;
    private List<MonthBean> monthList = new ArrayList<>();

    private RecyclerView recyclerView;
    private CalendarAdapter calendarAdapter;
    /**
     * 开始日期
     */
    private TextView textStart;
    private TextView textMonthStart;
    private TextView textMontStartMark;

    /**
     * 租期结束
     */
    private TextView textEnd;
    private TextView textMonthEnd;
    private TextView textMontEndMark;

    private Calendar calendar;
    private Calendar todayCalendar;
    private Calendar curreCalendar;

    private int tempYear;
    private int tempMonth;
    private String tempWeek;
    private int tempEmptyDayNum;

    private int curreYear;
    private int curreMonth;
    private int curreDay;

    /**
     * 当月天数
     */
    private int numDay;

    private int rvPosition = -1;

    /**
     * 选中日期
     */
    private DayBean selectDayBean;
    private List<DayBean> tempDayBeanList;

    //开始和结束
    private int days;
    private SelectBean startSelectBean;
    private SelectBean endSelectBean;
    private SelectBean tempSelectBean;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_calendar, this, true);
        recyclerView = findViewById(R.id.recyclerView);
        textStart = findViewById(R.id.textStart);
        textMonthStart = findViewById(R.id.textMonthStart);
        textMontStartMark = findViewById(R.id.textMontStartMark);

        textEnd = findViewById(R.id.textEnd);
        textMonthEnd = findViewById(R.id.textMonthEnd);
        textMontEndMark = findViewById(R.id.textMontEndMark);

        initToday();
        initCalendarList();
        initRecyclerView();
    }

    private void initToday() {
        todayCalendar = Calendar.getInstance();
        curreYear = todayCalendar.get(Calendar.YEAR);
        curreMonth = (todayCalendar.get(Calendar.MONTH) + 1);
        curreDay = todayCalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 初始化日历数据
     */
    private void initCalendarList() {
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);

        for (int i = 0; i < maxMonth; i++) {
            calendar.add(Calendar.MONTH, 1);
            //当前年月
            tempYear = calendar.get(Calendar.YEAR);
            tempMonth = calendar.get(Calendar.MONTH) + 1;

            monthBean = new MonthBean();
            dayList = new ArrayList<>();

            //当月天数
            numDay = CalendarUtils.getMonthLastDay(tempYear, tempMonth - 1);

            for (int tempDay = 1; tempDay <= numDay; tempDay++) {
                dayBean = new DayBean();
                dayBean.setYear(tempYear);
                dayBean.setMonth(tempMonth);
                dayBean.setDay(tempDay);
                if (tempDay == 1) {
                    //计算第一天的星期
                    tempWeek = CalendarUtils.getDayOfWeekByDate(tempYear, tempMonth - 1);
                    dayBean.setWeek(tempWeek);
                    addEmptyDay(tempWeek, dayList);
                }

                curreCalendar = CalendarUtils.getCurreCalendar(tempYear, tempMonth - 1, tempDay);

                //是否是today
                if (CalendarUtils.equalsCalendar(curreCalendar, todayCalendar)) {
                    dayBean.setSelectState(SelectState.UNABLE);
                    dayBean.setCurreDay(true);
                    dayBean.setContent("今天");
                }

                //如果是今天之前就unable
                if (curreCalendar.before(todayCalendar)) {
                    dayBean.setSelectState(SelectState.UNABLE);
                }

                //默认选中
                onDefaultSelect(curreCalendar, dayBean);

                dayList.add(dayBean);
            }
            monthBean.setYear(tempYear);
            monthBean.setMonth(tempMonth);
            monthBean.setDayList(dayList);

            monthList.add(monthBean);
        }

    }

    /**
     * 填补空白天数
     */
    private void addEmptyDay(String week, List<DayBean> dayList) {
        switch (week) {
            case "周日":
                tempEmptyDayNum = 0;
                break;
            case "周一":
                tempEmptyDayNum = 1;
                break;
            case "周二":
                tempEmptyDayNum = 2;
                break;
            case "周三":
                tempEmptyDayNum = 3;
                break;
            case "周四":
                tempEmptyDayNum = 4;
                break;
            case "周五":
                tempEmptyDayNum = 5;
                break;
            case "周六":
                tempEmptyDayNum = 6;
                break;
        }
        for (int i = 0; i < tempEmptyDayNum; i++) {
            dayList.add(new DayBean(true));
        }
        tempEmptyDayNum = 0;
    }

    /**
     * 默认选中
     *
     * @param calendar
     */
    private void onDefaultSelect(Calendar calendar, DayBean dayBean) {

    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        recyclerView.setLayoutManager(new MLinearLayoutManager(getContext()));
        calendarAdapter = new CalendarAdapter(getContext(), R.layout.item_month, monthList);
        recyclerView.setAdapter(calendarAdapter);
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.rvMonth) {
                    rvPosition = position;
                }
            }
        });
        calendarAdapter.setListener(new CalendarAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (rvPosition > -1) {
                    handleClick(rvPosition, position);
                    //刷新adapter
                    calendarAdapter.notifyDataSetChanged();
                    rvPosition = -1;
                }
            }
        });
    }


    /**
     * 点击事件处理
     *
     * @param rvPosition-recyclerView position
     * @param dayPosition-day         position
     */
    private void handleClick(int rvPosition, int dayPosition) {
        dayBean = monthList.get(rvPosition).getDayList().get(dayPosition);
        switch (dayBean.getSelectState()) {
            case SelectState.NONE:
                if (startSelectBean != null && endSelectBean == null) {
                    //更改开始状态
                    selectDayBean = monthList.get(startSelectBean.getSelectRv()).getDayList().get(startSelectBean.getSelectDay());
                    selectDayBean.setSelectState(SelectState.START);

                    //更改结束的状态
                    dayBean.setSelectState(SelectState.END);
                    dayBean.setContent("归还");
                    endSelectBean = new SelectBean();
                    endSelectBean.setDayBean(dayBean);
                    endSelectBean.setSelectRv(rvPosition);
                    endSelectBean.setSelectDay(dayPosition);

                    //更改中间的状态
                    changeBetweenState(SelectState.BETWEEN);
                } else {
                    //更改开始
                    clear();
                    dayBean.setSelectState(SelectState.PREVIEW_START);
                    dayBean.setContent("起租");
                    startSelectBean = new SelectBean();
                    startSelectBean.setDayBean(dayBean);
                    startSelectBean.setSelectRv(rvPosition);
                    startSelectBean.setSelectDay(dayPosition);
                }
                break;
            case SelectState.SINGLE:
                //开始和结束重合
                dayBean.setSelectState(SelectState.PREVIEW_START);
                dayBean.setContent("起租");
                endSelectBean = null;
                break;
            case SelectState.PREVIEW_START:
                //准备开始
                dayBean.setSelectState(SelectState.SINGLE);
                dayBean.setContent("一天");
                endSelectBean = new SelectBean();
                endSelectBean.setDayBean(dayBean);
                endSelectBean.setSelectRv(rvPosition);
                endSelectBean.setSelectDay(dayPosition);
                break;
            case SelectState.START:
                //开始
                clear();
                dayBean.setSelectState(SelectState.PREVIEW_START);
                dayBean.setContent("起租");
                startSelectBean = new SelectBean();
                startSelectBean.setDayBean(dayBean);
                startSelectBean.setSelectRv(rvPosition);
                startSelectBean.setSelectDay(dayPosition);
                endSelectBean = null;
                break;
            case SelectState.BETWEEN:
                //中间
                clear();
                dayBean.setSelectState(SelectState.PREVIEW_START);
                dayBean.setContent("起租");
                startSelectBean = new SelectBean();
                startSelectBean.setDayBean(dayBean);
                startSelectBean.setSelectRv(rvPosition);
                startSelectBean.setSelectDay(dayPosition);
                endSelectBean = null;
                break;
            case SelectState.END:
                //结束
                clear();
                dayBean.setSelectState(SelectState.PREVIEW_START);
                dayBean.setContent("起租");
                startSelectBean = new SelectBean();
                startSelectBean.setDayBean(dayBean);
                startSelectBean.setSelectRv(rvPosition);
                startSelectBean.setSelectDay(dayPosition);

                endSelectBean = null;
                break;
        }

        //开始时间晚于结束时间，反转时间
        if (startSelectBean != null && endSelectBean != null &&
                endSelectBean.getDayBean().getCalendar().before(startSelectBean.getDayBean().getCalendar())) {

            selectDayBean = monthList.get(startSelectBean.getSelectRv()).getDayList().get(startSelectBean.getSelectDay());
            selectDayBean.setContent("归还");
            selectDayBean.setSelectState(SelectState.END);

            selectDayBean = monthList.get(endSelectBean.getSelectRv()).getDayList().get(endSelectBean.getSelectDay());
            selectDayBean.setContent("起租");
            selectDayBean.setSelectState(SelectState.START);

            try {
                tempSelectBean = endSelectBean.clone();
                endSelectBean = startSelectBean.clone();
                startSelectBean = tempSelectBean;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            changeBetweenState(SelectState.BETWEEN);
        }

        handleListener();
    }

    /**
     * 处理回调
     */
    private void handleListener() {

        if (startSelectBean != null) {
            if (onCalendarChange != null) {
                onCalendarChange.onStart(startSelectBean.getDayBean());
            }
            changeStartText(false);
            textMonthStart.setText(startSelectBean.getDayBean().getMonth() + "月" + startSelectBean.getDayBean().getDay() + "日");
        } else {
            changeStartText(true);
        }

        if (endSelectBean != null) {
            if (onCalendarChange != null) {
                onCalendarChange.onEnd(endSelectBean.getDayBean());
            }
            changeEndText(false);
            textMonthEnd.setText(endSelectBean.getDayBean().getMonth() + "月" + endSelectBean.getDayBean().getDay() + "日");
        } else {
            changeEndText(true);
        }

        if (startSelectBean != null && endSelectBean != null) {
            days = CalendarUtils.differentDays(startSelectBean.getDayBean().getCalendar(),
                    endSelectBean.getDayBean().getCalendar());
            if (onCalendarChange != null) {
                onCalendarChange.onDays(days + 1);
            }
        }

    }


    /**
     * 清空选项
     */
    private void clear() {
        if (startSelectBean == null) {
            return;
        }
        if (endSelectBean == null) {
            //只选择开始
            changeStartState(SelectState.NONE, null);
        } else {
            //更改开始和结束状态
            changeStartState(SelectState.NONE, null);
            changeEndState(SelectState.NONE, null);

            //更改中间的状态
            changeBetweenState(SelectState.NONE);
        }

        startSelectBean = null;
        endSelectBean = null;
    }

    /**
     * 重置状态
     */
    public void resetState() {
        clear();
        changeStartText(true);
        changeEndText(true);
    }

    /**
     * 更改开始状态
     *
     * @param state
     * @param content
     */
    private void changeStartState(@SelectState.State int state, String content) {
        if (startSelectBean == null) {
            return;
        }
        monthList.get(startSelectBean.getSelectRv()).getDayList().get(startSelectBean.getSelectDay()).setContent(content);
        monthList.get(startSelectBean.getSelectRv()).getDayList().get(startSelectBean.getSelectDay()).setSelectState(state);
    }

    /**
     * 更改结束状态
     *
     * @param state
     * @param content
     */
    private void changeEndState(@SelectState.State int state, String content) {
        if (endSelectBean == null) {
            return;
        }
        monthList.get(endSelectBean.getSelectRv()).getDayList().get(endSelectBean.getSelectDay()).setContent(content);
        monthList.get(endSelectBean.getSelectRv()).getDayList().get(endSelectBean.getSelectDay()).setSelectState(state);
    }


    /**
     * 更改区间状态
     *
     * @param state
     */
    private void changeBetweenState(@SelectState.State int state) {
        if (startSelectBean == null || endSelectBean == null) {
            return;
        }
        if (startSelectBean.getSelectRv() == endSelectBean.getSelectRv()) {
            //在同一个月
            for (int i = startSelectBean.getSelectRv(); i <= endSelectBean.getSelectRv(); i++) {
                tempDayBeanList = monthList.get(i).getDayList();
                for (int j = startSelectBean.getSelectDay() + 1; j < endSelectBean.getSelectDay(); j++) {
                    tempDayBeanList.get(j).setSelectState(state);
                }
            }
        } else {
            //跨月
            for (int i = startSelectBean.getSelectRv(); i <= endSelectBean.getSelectRv(); i++) {
                tempDayBeanList = monthList.get(i).getDayList();
                if (i == startSelectBean.getSelectRv()) {
                    //开始月份
                    for (int i1 = startSelectBean.getSelectDay() + 1; i1 < tempDayBeanList.size(); i1++) {
                        tempDayBeanList.get(i1).setSelectState(state);
                    }
                } else if (i == endSelectBean.getSelectRv()) {
                    //结束月份
                    for (int i1 = 0; i1 < endSelectBean.getSelectDay(); i1++) {
                        tempDayBeanList.get(i1).setSelectState(state);
                    }
                } else if (i > startSelectBean.getSelectRv() && i < endSelectBean.getSelectRv()) {
                    //中间月份
                    for (int i1 = 0; i1 < tempDayBeanList.size(); i1++) {
                        tempDayBeanList.get(i1).setSelectState(state);
                    }
                }
            }
        }
    }

    /**
     * 是否恢复初始状态
     *
     * @param isVisiable
     */
    private void changeStartText(boolean isVisiable) {
        textStart.setVisibility(isVisiable ? VISIBLE : GONE);
        textMonthStart.setVisibility(!isVisiable ? VISIBLE : GONE);
        textMontStartMark.setVisibility(!isVisiable ? VISIBLE : GONE);
    }

    /**
     * 是否恢复初始状态
     *
     * @param isVisiable
     */
    private void changeEndText(boolean isVisiable) {
        textEnd.setVisibility(isVisiable ? VISIBLE : GONE);
        textMonthEnd.setVisibility(!isVisiable ? VISIBLE : GONE);
        textMontEndMark.setVisibility(!isVisiable ? VISIBLE : GONE);
    }

    private OnCalendarChange onCalendarChange;

    public void setOnCalendarChange(OnCalendarChange onCalendarChange) {
        this.onCalendarChange = onCalendarChange;
    }

    public static abstract class OnCalendarChange {

        public abstract void onStart(DayBean dayBean);

        public abstract void onEnd(DayBean dayBean);

        public void onDays(int day) {
        }

    }

}
