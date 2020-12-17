package com.yong.apm;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private LinearLayout main;
    private TextView viewA;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector gestureScanner;

    int year, month, holiy=0;
    String thisyear, thismonth, thisday;
    String Capm = "", firstapm = "", nextapm = "", bbb="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureScanner = new GestureDetector(this);

        Date currentTime = Calendar.getInstance( ).getTime( );

         thisyear = new SimpleDateFormat("yyyy", Locale.getDefault( )).format(currentTime);
         thismonth = new SimpleDateFormat("MM", Locale.getDefault( )).format(currentTime);
         thisday = new SimpleDateFormat("dd", Locale.getDefault( )).format(currentTime);

        year = Integer.valueOf(thisyear);
        month = Integer.valueOf(thismonth);

        Capm = "pm";

        /*
        holiy = 6; // 월 5
        holiy = 5; // 화 4
        holiy = 4; // 수 3
        holiy = 3; // 목 2
        holiy = 2; // 금 1
        holiy = 1; // 토 0
        holiy = 0; // 일 6*/

        holiy = 5;

        calendarDisp();

    }

    private int getWeekOfYear(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] dates = date.split("-");
        int year = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        int day = Integer.parseInt(dates[2]);
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    private void leftSwap() {
        month--;

        if(month == 0) {
            month = 12;
            year = year - 1;
        }

        Calendar calendar = Calendar.getInstance( );

        // 1. 총일수 구하기
        calendar.set(year, month - 1, 1);

        // 1. 총일수 구하기
        int max_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당월의 마지막 날짜

        // 2. 시작요일 구하기
        int start_week = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 일요일 1, 토요일 7

        // 3. 총 몇 주인지 구하기
        calendar.set(year, month - 1, calendar.getActualMaximum(Calendar.DATE));
        int total_week = calendar.get(Calendar.WEEK_OF_MONTH);

        // 4. 마지막 요일 구하기
        int last_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        int day = 1;

        bbb = bbb + "\n";
        int old_week = start_week;
        start_week = 6 - last_week;
        last_week = 6 - old_week;

        int[] holiyA = {6, 0, 1, 2, 3, 4, 5}; // 월, 화, 수, 목, 금, 토, 일

        Capm = firstapm;
        for (int i = 1; i <= (total_week); i++) {
            for (int j = 0; j < 7; j++) {
                final TextView text = new TextView(this);
                if (!((i == 1 && j < start_week) || (i == (total_week) && j > last_week))) {
                    if (j == holiy) { // 휴무일
                            text.setBackgroundColor(Color.RED);
                            if (Capm.equals("am")) Capm = "pm";
                            else Capm = "am";
                    }
                    if (max_day == day && j == holiyA[holiy]) {
                        //Toast.makeText(getApplicationContext(), "max_day == day", Toast.LENGTH_SHORT).show();
                        if (Capm.equals("am")) Capm = "pm";
                        else Capm = "am";
                    }

                    bbb = bbb + day + Capm + "|";
                    day++;
                } else {
                    bbb = bbb + "...." + "|";
                }
            }
            bbb = bbb + "\n";
        }
        ////////////////////////////////////////////////
        calendarDisp();
    }

    private void rightSwap() {
        month++;

        if(month == 13) {
            month = 1;
            year = year + 1;
        }

        Capm = nextapm;
        calendarDisp();
    }


    private void calendarDisp() {
        String dayS;

        String[] weekA = {"일", "월", "화", "수", "목", "금", "토"};
        TextView textTitle;

        Calendar calendar = Calendar.getInstance( );

        // 1. 총일수 구하기
        calendar.set(year, month - 1, 1);

        // 1. 총일수 구하기
        int max_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당월의 마지막 날짜

        // 2. 시작요일 구하기
        int start_week = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 일요일 1, 토요일 7

        // 3. 총 몇 주인지 구하기
        calendar.set(year, month - 1, calendar.getActualMaximum(Calendar.DATE));
        int total_week = calendar.get(Calendar.WEEK_OF_MONTH);

        // 4. 마지막 요일 구하기
        int last_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        int day = 1;


        //String title = " < " + year + "년 " + month + "월 " + ">" + Capm + " : " + bbb;
        String title = " < " + year + "년 " + month + "월 " + ">";

        textTitle = (TextView)findViewById(R.id.title);
        textTitle.setText(title);

        final TableLayout tableLayout = (TableLayout)findViewById(R.id.table);
        tableLayout.removeAllViews( );

        String apm=""; // 1일 오전으로 시작

        int[] holiyA = {0, 6, 5, 4, 3, 2, 1}; // 일, 월, 화, 수, 목, 금, 토, 일

        firstapm = Capm;
        for (int i = 1; i <= (total_week + 1); i++) {
            final TableRow tableRow = new TableRow(this);

            tableRow.setLayoutParams(new TableLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

            for (int j = 0; j < 7; j++) {
                final TextView text = new TextView(this);
                if (i == 1) {
                    text.setText(weekA[j]);
                } else if (!((i == 2 && j < start_week) || (i == (total_week + 1) && j > last_week))) {

                    dayS = String.valueOf(day);

                    if (j == holiyA[holiy]) { // 휴무일
                        text.setBackgroundColor(Color.RED);
                        if (Capm.equals("am")) Capm = "pm";
                        else Capm = "am";

                        if (day == 1) {
                            firstapm = Capm;
                        }
                    }

                    if (Capm.equals("am")) {
                        nextapm = "am";
                        apm = "오전";
                    } else {
                        nextapm = "pm";
                        apm = "오후";
                    }

                    dayS = dayS + "\n" + apm + "\n";

                    text.setText(dayS);
                    day++;

                } else {
                    ;
                }

                text.setGravity(1);

                text.setLayoutParams(new TableRow.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                tableRow.addView(text);
            }
            tableLayout.addView(tableRow);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return gestureScanner.onTouchEvent(me);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;

            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //Toast.makeText(getApplicationContext(), "Left Swipe", Toast.LENGTH_SHORT).show();
                rightSwap();
            }
            // left to right swipe
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //Toast.makeText(getApplicationContext(), "Right Swipe", Toast.LENGTH_SHORT).show();
                leftSwap();
            }
        } catch (Exception e) {

        }
        return true;
    }
}