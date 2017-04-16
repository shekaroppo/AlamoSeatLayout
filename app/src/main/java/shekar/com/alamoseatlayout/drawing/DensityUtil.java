package shekar.com.alamoseatlayout.drawing;

import android.content.Context;

public class DensityUtil {

/** 
 * dip to px
 */  
public static float dip2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return  (dpValue * scale );
}

  /**
   * sip to px
   */
  public static float sip2px(Context context, float spValue) {
    final float scale = context.getResources().getDisplayMetrics().scaledDensity;
    return  (spValue * scale );
  }

  /**
 * px to dp
 */  
public static float px2dip(Context context, float pxValue) {
    final float scale =  context.getResources().getDisplayMetrics().density;  
    return (pxValue / scale );
    }  
} 