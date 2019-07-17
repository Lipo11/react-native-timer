package com.ui.timer;

import android.content.Context;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Timer;
import java.util.TimerTask;

public class UiTimerView extends ViewGroup
{
	double start = (double)System.currentTimeMillis() / 1000.0;
	double timeout = 60;
	double speed = 2;
	double tail_length_min = Math.PI / 16;
	double tail_length_max = Math.PI * 3 / 2;

	float scale;

	Paint secondsPaint;
	Paint intervalPaint;
	Paint textPaint;

	Timer timer = new Timer();
	TimerTask myTask = new TimerTask()
	{
		@Override
		public void run()
		{
			postInvalidate();
		}
	};

	public UiTimerView(Context context)
	{
		super(context);

		DisplayMetrics dm = getResources().getDisplayMetrics() ;
		scale = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm);

		secondsPaint = new Paint();
		secondsPaint.setStrokeWidth(1*scale);
		secondsPaint.setPathEffect(null);
		secondsPaint.setColor(Color.WHITE);
		secondsPaint.setStyle(Paint.Style.STROKE);
		secondsPaint.setAntiAlias(true);

		intervalPaint = new Paint();
		intervalPaint.setStrokeWidth(3*scale);
		intervalPaint.setPathEffect(null);
		intervalPaint.setColor(Color.WHITE);
		intervalPaint.setStyle(Paint.Style.STROKE);
		intervalPaint.setAntiAlias(true);

		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Paint.Align.CENTER);

		timer.schedule(myTask, 30, 30);
	}

	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);

		double elapsed = Math.min( timeout, (double)System.currentTimeMillis() / 1000.0 - start );

		double size = Math.min(  canvas.getWidth(), canvas.getHeight() );
		double centerX = canvas.getWidth() / 2.0;
		double centerY = canvas.getHeight() / 2.0;
		double second_fraction = elapsed % 1;
		double velocity = ( second_fraction < 0.5 ? Math.pow(second_fraction * 2, speed) / 2 : Math.pow( ( 1 - second_fraction ) * 2, speed) / 2 );

		double second_end_angle = 2 * Math.PI * ( second_fraction < 0.5 ? velocity : 1 - velocity ) + tail_length_min + 3 * Math.PI / 2;
		double second_start_angle = second_end_angle - tail_length_min - ( tail_length_max - tail_length_min ) * velocity;
		double total_start_angle = 3 * Math.PI / 2;
		double total_end_angle = total_start_angle + 2 *  Math.PI * elapsed / timeout;

		final RectF oval = new RectF();
		oval.set((float)(centerX - size / 2.0 + 2 * scale), (float)(centerY - size / 2.0 + 2 * scale), (float)(centerX + size / 2.0 - 2 * scale), (float)(centerY + size / 2.0 - 2 * scale));

		if( elapsed < timeout )
		{
			Path secondsPath = new Path();
			secondsPath.arcTo(oval, (float)(second_start_angle / Math.PI * 180), (float)(Math.min(360, (2 * Math.PI + ( second_end_angle - second_start_angle)) % ( 2 * Math.PI ) / Math.PI * 180)), true);
			canvas.drawPath(secondsPath, secondsPaint);

			Path intervalPath = new Path();
			intervalPath.arcTo(oval, (float) (total_start_angle / Math.PI * 180), (float) (Math.min(360, (2 * Math.PI + (total_end_angle - total_start_angle)) % (2 * Math.PI) / Math.PI * 180)), true);
			canvas.drawPath(intervalPath, intervalPaint);
		}
		else{
			myTask.cancel();
			//intervalPath.arcTo(oval, 0, 360, true);
			canvas.drawCircle((float)centerX, (float)centerY, (float)(size / 2.0 - 2 * scale), intervalPaint);
		}

		textPaint.setTextSize((float)(size / 2.8));
		canvas.drawText(String.format("%d", (int)Math.ceil(timeout - elapsed)), (float)centerX, (float)centerY -((textPaint.descent() + textPaint.ascent()) / 2), textPaint);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
	}

	public void setTimeout(Integer timeout)
	{
		this.timeout = timeout;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		myTask.cancel();
		timer.purge();
	}
}