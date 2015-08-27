package tn.amineghabi.acquawatchface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.SurfaceHolder;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MyWatchFace extends CanvasWatchFaceService {


    private static final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }

    private class Engine extends CanvasWatchFaceService.Engine {

        private final Handler mUpdateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (R.id.message_update == message.what) {
                    invalidate();
                    if (shouldTimerBeRunning()) {
                        long timeMs = System.currentTimeMillis();
                        long delayMs = INTERACTIVE_UPDATE_RATE_MS
                                - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                        mUpdateTimeHandler.sendEmptyMessageDelayed(R.id.message_update, delayMs);
                    }
                }
            }
        };

        private final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTime.clear(intent.getStringExtra("time-zone"));
                mTime.setToNow();
            }
        };

        private boolean mRegisteredTimeZoneReceiver = false;

        private Time mTime;

        private Paint mBackgroundAmbientPaint;
        private Paint mBackgroundPaintFilled;
        private Paint mBackgroundPaintNotFilled;
        private Paint mPaintHourText;
        private Paint mPaintMinuteText;

        private boolean mAmbient;

        private int mWidth;
        private int mHeight;
        private float mCenterX;
        private float mCenterY;

        private Typeface mTypefaceRobotoThin;
        private Typeface mTypefaceRobotoLight;

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(MyWatchFace.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());

            mTypefaceRobotoThin = Typeface.createFromAsset(getAssets(), "robotothin.ttf");
            mTypefaceRobotoLight = Typeface.createFromAsset(getAssets(), "robotolight.ttf");

            mBackgroundPaintFilled = new Paint();
            mBackgroundPaintFilled.setColor(getResources().getColor(R.color.color_bg));

            mBackgroundPaintNotFilled = new Paint();
            mBackgroundPaintNotFilled.setColor(getResources().getColor(R.color.color_gray));

            mBackgroundAmbientPaint = new Paint();
            mBackgroundAmbientPaint.setColor(Color.BLACK);
            mBackgroundAmbientPaint.setAntiAlias(true);

            mPaintHourText = new Paint();
            mPaintHourText.setColor(Color.WHITE);
            mPaintHourText.setAntiAlias(true);
            mPaintHourText.setTextSize(90);
            mPaintHourText.setTypeface(mTypefaceRobotoThin);

            mPaintMinuteText = new Paint();
            mPaintMinuteText.setColor(Color.WHITE);
            mPaintMinuteText.setAntiAlias(true);
            mPaintMinuteText.setTextSize(45);
            mPaintHourText.setTypeface(mTypefaceRobotoLight);

            mTime = new Time();
        }

        @Override
        public void onDestroy() {
            mUpdateTimeHandler.removeMessages(R.id.message_update);
            super.onDestroy();
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            if (mAmbient != inAmbientMode) {
                mAmbient = inAmbientMode;
                invalidate();
            }


            updateTimer();
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            mWidth = width;
            mHeight = height;
            mCenterX = mWidth / 2f;
            mCenterY = mHeight / 2f;
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            mTime.setToNow();

            //hour and minute
            String mHour = String.valueOf(mTime.hour);
            String mMinute = String.valueOf(mTime.minute);

            if (mMinute.length() < 2)
                mMinute = "0" + mMinute;
            if (mHour.length() < 2)
                mHour = "0" + mHour;
            if (mAmbient)
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mBackgroundAmbientPaint);
            else {
                canvas.drawRect(0, mHeight - (mTime.minute / 60f) * mHeight, canvas.getWidth(), canvas.getHeight(), mBackgroundPaintFilled);
                canvas.drawRect(0, 0, canvas.getWidth(), mHeight - (mTime.minute / 60f) * mHeight, mBackgroundPaintNotFilled);
            }

            Rect hourBound = new Rect();
            mPaintHourText.getTextBounds(mHour, 0, mHour.length(), hourBound);
            canvas.drawText(mHour, mCenterX - (hourBound.width() / 2f), mCenterY + (hourBound.height() / 2f), mPaintHourText);

            Rect minuteBound = new Rect();
            mPaintMinuteText.getTextBounds(mMinute, 0, mMinute.length(), minuteBound);
            canvas.drawText(mMinute, mCenterX + (mCenterX / 2f), mCenterY + (minuteBound.height() / 2f), mPaintMinuteText);

            canvas.save();
            canvas.restore();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                mTime.clear(TimeZone.getDefault().getID());
                mTime.setToNow();
            } else {
                unregisterReceiver();
            }

            updateTimer();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            MyWatchFace.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            MyWatchFace.this.unregisterReceiver(mTimeZoneReceiver);
        }

        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(R.id.message_update);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(R.id.message_update);
            }
        }

        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }
    }
}
