package com.wfl.explorer.framework.common.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2 - 20;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 20;
			dst_top = 20;
			dst_right = width-20;
			dst_bottom = width-20;
		} else {
			roundPx = height / 2 - 20;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 20;
			dst_top = 20;
			dst_right = height-20;
			dst_bottom = height-20;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}
	

    public static Bitmap drawShadow(Bitmap map, int radius) {
        if (map == null)
            return null;

        BlurMaskFilter blurFilter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.OUTER);
        Paint shadowPaint = new Paint();
        shadowPaint.setMaskFilter(blurFilter);
        
        int[] offsetXY = new int[2];
        Bitmap shadowImage = map.extractAlpha(shadowPaint, offsetXY);
        shadowImage = shadowImage.copy(Config.ARGB_8888, true);
        Canvas c = new Canvas(shadowImage);
        c.drawBitmap(map, -offsetXY[0], -offsetXY[1], null);
        return shadowImage;
    }
    
    
    
    /**
     * 压缩图像并摆正照片
     * @param filePath
     * @param outputPath
     */
    public static boolean scaleLoadImage(String filePath, String outputPath) {
    	Bitmap tBitmap;
    	if(null == filePath || null == outputPath) {
    		return false;
    	}
    	BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inSampleSize = 1;
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opt);
		int bitmapSize = opt.outHeight * opt.outWidth;
		double scale = bitmapSize / (600 * 900);  // 缩放到最大这么多像素
		int sampleSize = (int) Math.sqrt(scale);
		opt.inSampleSize = sampleSize;
		opt.inJustDecodeBounds = false;
		tBitmap = BitmapFactory.decodeFile(filePath, opt);
		if(null == tBitmap) {
			return false;
		}
		int orientation = readPictureDegree(filePath);
		if(orientation != 0) {
			Matrix matrix = new Matrix();
			matrix.setRotate(orientation, tBitmap.getWidth()/2, tBitmap.getHeight()/2);
			try {
				Bitmap bm1 = Bitmap.createBitmap(tBitmap, 0, 0, tBitmap.getWidth(), tBitmap.getHeight(), matrix, true);
				bm1.compress(CompressFormat.JPEG, 90, new FileOutputStream(outputPath));
			} catch (OutOfMemoryError ex) {
				ex.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			try {
				tBitmap.compress(CompressFormat.JPEG, 90, new FileOutputStream(outputPath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return true;
    }
    
    
    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
                ExifInterface exifInterface = new ExifInterface(path);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return degree;
    }
    
   /*
    * 旋转图片 
    * @param angle 
    * @param bitmap 
    * @return Bitmap 
    */ 
   public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
       //旋转图片 动作   
       Matrix matrix = new Matrix();;  
       matrix.postRotate(angle);  
       System.out.println("angle2=" + angle);  
       // 创建新的图片   
       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
               bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
       return resizedBitmap;  
   }
    
    /**
    * 将图片缩放到指定大小
    * 
    * @param bitmap
    * @param w
    * @param h
    * @return
    */
    public synchronized static Bitmap scaleBitmap(Bitmap bitmap, float w,
    		float h) {
	    if (bitmap == null) {
	    	return null;
	    }
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    Matrix matrix = new Matrix();
	    float scaleW = w / (float) width;
	    float scaleH = h / (float) height;
	    matrix.postScale(scaleW, scaleH);
	    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	    return bitmap;
    }
    
    /**
     * 
     * @param v
     * @param isParemt
     * @return
     */
    public static Bitmap loadBitmapFromView(View v, boolean isParemt) {
		if (v == null) {
			return null;
		}
		Bitmap screenshot;
		
		Rect rect = new Rect();
		v.getGlobalVisibleRect(rect);
		int height = rect.height();
		//if(v.getHeight() > v.getGlobalVisibleRect(r))) {
		//}
		screenshot = Bitmap.createBitmap(v.getWidth(), height, Config.ARGB_4444);
		Canvas c = new Canvas(screenshot);
		c.translate(-v.getScrollX(), -v.getScrollY());
		v.draw(c);
		return screenshot;
	}
    
    public static boolean writeBitmap2File(Bitmap bitmap, String filePath) {
    	if(TextUtils.isEmpty(filePath)) {
    		return false;
    	}
    	try {
			bitmap.compress(CompressFormat.JPEG, 90, new FileOutputStream(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
    	return true;
    }

	public static Bitmap decodeBitmapNoScale(String path) {
    	/*
    	BitmapFactory.Options opt = new Options();
    	opt.inPurgeable = true;
    	opt.inInputShareable = true;
    	return BitmapFactory.decodeFile(path, opt);
    	*/
		Bitmap bmp;
		Bitmap bmp1;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inJustDecodeBounds = false;
		try {
			bmp = BitmapFactory.decodeFile(path, opts);
			//return bmp;
		} catch (OutOfMemoryError err) {
			return null;
		}
		if(null == bmp) {
			return null;
		}
		int orientation = readPictureDegree(path);
		if(orientation != 0) {
			Matrix matrix = new Matrix();
			matrix.setRotate(orientation, bmp.getWidth()/2, bmp.getHeight()/2);
			try {
				bmp1 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
				return bmp1;
			} catch (OutOfMemoryError ex) {
				ex.printStackTrace();
				return null;
			}
		} else {
			return bmp;
		}

	}

    
    public static Bitmap decodeBitmap(String path, int width, int height) {
    	/*
    	BitmapFactory.Options opt = new Options();
    	opt.inPurgeable = true;
    	opt.inInputShareable = true;
    	return BitmapFactory.decodeFile(path, opt);
    	*/
    	Bitmap bmp;
    	Bitmap bmp1;
    	BitmapFactory.Options opts = new BitmapFactory.Options();
    	opts.inJustDecodeBounds = true;
    	BitmapFactory.decodeFile(path, opts);
    	Log.d("BitmapUtil", "image width: " + opts.outWidth);
    	Log.d("BitmapUtil", "image height: " + opts.outHeight);
    	int inSampleSize = computeInitialSampleSize(opts, -1, width * height);
    	opts.inSampleSize = inSampleSize;
    	Log.d("BitmapUtil", "image inSampleSize: " + inSampleSize);
    	opts.inPurgeable = true;
    	opts.inInputShareable = true;
    	opts.inJustDecodeBounds = false;
    	try {
    	 bmp = BitmapFactory.decodeFile(path, opts);
    	 //return bmp;
    	} catch (OutOfMemoryError err) {
    		return null;
    	}
    	if(null == bmp) {
    		return null;
    	}
    	int orientation = readPictureDegree(path);
		if(orientation != 0) {
			Matrix matrix = new Matrix();
			matrix.setRotate(orientation, bmp.getWidth()/2, bmp.getHeight()/2);
			try {
				bmp1 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
				return bmp1;
			} catch (OutOfMemoryError ex) {
				ex.printStackTrace();
				return null;
			}
		} else {
			return bmp;
		}
		
    }
    
    /**
     * compute Sample Size
     * 
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options,
    		int minSideLength, int maxNumOfPixels) {
    	int initialSize = computeInitialSampleSize(options, minSideLength,
    			maxNumOfPixels);

    	int roundedSize;
    	if (initialSize <= 8) {
    		roundedSize = 1;
    		while (roundedSize < initialSize) {
    			roundedSize <<= 1;
    		}
    	} else {
    		roundedSize = (initialSize + 7) / 8 * 8;
    	}

    	return roundedSize;
    }

    /**
     * compute Initial Sample Size
     * 
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options,
    		int minSideLength, int maxNumOfPixels) {
    	double w = options.outWidth;
    	double h = options.outHeight;

    	// 上下限范围
    	int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
    			.sqrt(w * h / maxNumOfPixels));
    	int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
    			Math.floor(w / minSideLength), Math.floor(h / minSideLength));

    	if (upperBound < lowerBound) {
    		// return the larger one when there is no overlapping zone.
    		return lowerBound;
    	}

    	if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
    		return 1;
    	} else if (minSideLength == -1) {
    		return lowerBound;
    	} else {
    		return upperBound;
    	}
    }
    
    
    /**
     * 地图截图
     * @param bitmap
     * @return
     */
    public static Bitmap cropMapBitmap(Bitmap bitmap) {
    	if(bitmap == null) {
    		return null;
    	}
    	int width = bitmap.getWidth();
    	int height = bitmap.getHeight();
    	int tHeight = (int) (width * 0.5);
    	int y = height / 2 - tHeight / 2;
    	Bitmap result = Bitmap.createBitmap(bitmap, 0, y, width, tHeight);
    	return result;
    }

	/**
	 * 聊天选择地图剪裁
	 * @param bitmap
	 * @return
	 */
	public static Bitmap cropImMapBitmap(Bitmap bitmap) {
		if(bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int tHeight = (int) (width * 0.6);
		int y = height / 2 - tHeight / 2;
		Bitmap result = Bitmap.createBitmap(bitmap, 0, y, width, tHeight);
		return result;
	}

	/**
	 * 聊天选择地图剪裁
	 * @param bitmap
	 * @return
	 */
	public static Bitmap cropImVideoBitmap(Bitmap bitmap) {
		if(bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int tHeight = (int) (width * 0.75f);
		int y = height / 2 - tHeight / 2;
		Bitmap result = Bitmap.createBitmap(bitmap, 0, y, width, tHeight);
		return result;
	}

    public static Bitmap loadLocalImageWithSize(String filePath, int width, int height) {
    	Bitmap tBitmap;
    	if(null == filePath) {
    		return null;
    	}
    	BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inSampleSize = 1;
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opt);
		int bitmapSize = opt.outHeight * opt.outWidth;
		double scale;
		if(width != 0 && height != 0) {
			scale = bitmapSize / (width * height);  // 缩放到最大这么多像素
		} else {
			scale = bitmapSize / (200 * 300);
		}
		int sampleSize = (int) Math.sqrt(scale);
		opt.inSampleSize = sampleSize;
		opt.inJustDecodeBounds = false;
		tBitmap = BitmapFactory.decodeFile(filePath, opt);
		return tBitmap;
    }


	public static Bitmap resizeBitmapByCenterCrop(Bitmap src, int destWidth, int destHeight) {
		if (src == null || destWidth == 0 || destHeight == 0) {
			return null;
		}
		// 图片宽度
		int w = src.getWidth();
		// 图片高度
		int h = src.getHeight();
		// Imageview宽度
		int x = destWidth;
		// Imageview高度
		int y = destHeight;
		// 高宽比之差
		int temp = (y / x) - (h / w);
		/**
		 * 判断高宽比例，如果目标高宽比例大于原图，则原图高度不变，宽度为(w1 = (h * x) / y)拉伸
		 * 画布宽高(w1,h),在原图的((w - w1) / 2, 0)位置进行切割
		 */

		if (temp > 0) {
			// 计算画布宽度
			int w1 = (h * x) / y;
			// 创建一个指定高宽的图片
			Bitmap newb = Bitmap.createBitmap(src, (w - w1) / 2, 0, w1, h);
			//原图回收
			src.recycle();
			return newb;
		} else {
			/**
			 * 如果目标高宽比小于原图，则原图宽度不变，高度为(h1 = (y * w) / x),
			 * 画布宽高(w, h1), 原图切割点(0, (h - h1) / 2)
			 */

			// 计算画布高度
			int h1 = (y * w) / x;
			// 创建一个指定高宽的图片
			Bitmap newb = Bitmap.createBitmap(src, 0, (h - h1) / 2, w, h1);
			//原图回收
			src.recycle();
			return newb;
		}
	}

}
