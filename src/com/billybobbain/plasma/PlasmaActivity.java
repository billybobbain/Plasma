package com.billybobbain.plasma;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * This code is based on the legacy example code at
 * http://www.ic.sunysb.edu/Stu/jseyster/plasma/source.html
 * @author Billy Bob Bain converted this to Android in January 2010. For fun.
 *
 */

public class PlasmaActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SampleView view = new SampleView(this);        
        setContentView(view);
    }
    
  //Randomly displaces color value for midpoint depending on size
	//of grid piece.
	static double displace(double num, double width, double height)
	{
		double max = num / (double)(width + height) * 3;
		return ((double)Math.random() - 0.5d) * max;
	}
    
    private static class SampleView extends View {
        private Paint   mPaint = new Paint();
        public SampleView(Context context) {
            super(context);
        }
        boolean done = false;
        @Override protected void onDraw(Canvas canvas) {
            Paint paint = mPaint;
            paint.setAntiAlias(true);
            paint.setDither(true);
        		drawPlasma(this.getContext(), canvas,paint, canvas.getWidth(),canvas.getHeight());
        }
        private void touch_up() {
//        	Random r = new Random();
//        	
//			float[] filter = new float[20];
//			for(int i=0;i<20;i++) {
//				filter[i] = r.nextFloat();
//			}
//           mPaint.setColorFilter(new ColorMatrixColorFilter(filter));
//           //PaintFlagsDrawFilter df = new PaintFlagsDrawFilter(Paint.FILTER_BITMAP_FLAG,0);
           //canvas.setDrawFilter(df);
           //Toast.makeText(this.getContext(), df.toString(), Toast.LENGTH_SHORT).show();
        	//canvas.drawPaint(mPaint);
        	//drawPlasma(this.getContext(), canvas,mPaint, canvas.getWidth(),canvas.getHeight());

        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
//                    touch_start(x, y);
             //       invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
 //                   touch_move(x, y);
               //     invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }
    
    
  //Returns a color based on a color value, c.
	static int computeColor(double c)
	{		
		double Red = 0;
		double Green = 0;
		double Blue = 0;
		
		if (c < 0.5d)
		{
			Red = c * 2;
		}
		else
		{
			Red = (1.0d - c) * 2;
		}
		
		if (c >= 0.3d && c < 0.8d)
		{
			Green = (c - 0.3d) * 2;
		}
		else if (c < 0.3d)
		{
			Green = (0.3d - c) * 2;
		}
		else
		{
			Green = (1.3d - c) * 2;
		}
		
		if (c >= 0.5d)
		{
			Blue = (c - 0.5d) * 2;
		}
		else
		{
			Blue = (0.5d - c) * 2;
		}
		return Color.rgb((int)(255*Red), (int)(255*Green),(int)(255*Blue));
	}
    
    
	//This is something of a "helper function" to create an initial grid
	//before the recursive function is called.	
	static void drawPlasma(Context ctx, Canvas c, Paint p, int width, int height)
	{
		float c1, c2, c3, c4;
		
		//Assign the four corners of the intial grid random color values
		//These will end up being the colors of the four corners of the applet.		
		c1 = (float)Math.random();
		c2 = (float)Math.random();
		c3 = (float)Math.random();
		c4 = (float)Math.random();
		  p.setStyle(Paint.Style.STROKE);
		DivideGrid(c, p, 0, 0, width , height , c1, c2, c3, c4);
		
	}
	
	//This is the recursive function that implements the random midpoint
	//displacement algorithm.  It will call itself until the grid pieces
	//become smaller than one pixel.	
	static void DivideGrid(Canvas c, Paint p, double x, double y, double width, double height, double c1, double c2, double c3, double c4)
	{
		double Edge1, Edge2, Edge3, Edge4, Middle;
		double newWidth = width / 2;
		double newHeight = height / 2;

		if (width > 2 || height > 2)
		{	
			Middle = (c1 + c2 + c3 + c4) / 4 + displace(newWidth + newHeight, width, height);	//Randomly displace the midpoint!
			Edge1 = (c1 + c2) / 2;	//Calculate the edges by averaging the two corners of each edge.
			Edge2 = (c2 + c3) / 2;
			Edge3 = (c3 + c4) / 2;
			Edge4 = (c4 + c1) / 2;
			
			//Make sure that the midpoint doesn't accidentally "randomly displaced" past the boundaries!
			if (Middle < 0)
			{
				Middle = 0;
			}
			else if (Middle > 1.0f)
			{
				Middle = 1.0f;
			}
			
			//Do the operation over again for each of the four new grids.			
			DivideGrid(c, p, x, y, newWidth, newHeight, c1, Edge1, Middle, Edge4);
			DivideGrid(c, p, x + newWidth, y, newWidth, newHeight, Edge1, c2, Edge2, Middle);
			DivideGrid(c, p, x + newWidth, y + newHeight, newWidth, newHeight, Middle, Edge2, c3, Edge3);
			DivideGrid(c, p, x, y + newHeight, newWidth, newHeight, Edge4, Middle, Edge3, c4);
		}
		else	//This is the "base case," where each grid piece is less than the size of a pixel.
		{
			//The four corners of the grid piece will be averaged and drawn as a single pixel.
			double color = (c1 + c2 + c3 + c4) / 4;
			p.setStrokeWidth(2);
			int colorValue = computeColor(color);
			p.setColor(colorValue);
			c.drawPoint((float)x,(float) y, p);
			
		
			//c.drawRect((int)x, (int)y, 1, 1);	//Java doesn't have a function to draw a single pixel, so
								//a 1 by 1 rectangle is used.
		}
	}

//	//Draw a new plasma fractal whenever the applet is clicked.
//	public boolean mouseUp(Event evt, int x, int y)
//	{
//		drawPlasma(Context, getSize().width, getSize().height);
//		repaint();	//Force the applet to draw the new plasma fractal.
//		
//		return false;
//	}
	
}