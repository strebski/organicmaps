
package app.organicmaps.widget.placepage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import app.organicmaps.Framework;
import app.organicmaps.R;
import app.organicmaps.util.StringUtils;

@SuppressLint("ViewConstructor")
public class FloatingMarkerView extends RelativeLayout implements IMarker
{
  @Nullable
  private Chart mChart;
  private static final int TRIANGLE_ROTATION_ANGLE = 180;
  @SuppressWarnings("NullableProblems")
  @NonNull
  private View mImage;
  @SuppressWarnings("NullableProblems")
  @NonNull
  private View mInfoFloatingContainer;
  @SuppressWarnings("NullableProblems")
  @NonNull
  private TextView mAltitudeView;
  @SuppressWarnings("NullableProblems")
  @NonNull
  private TextView mDistanceTextView;
  @SuppressWarnings("NullableProblems")
  @NonNull
  private TextView mDistanceValueView;
  @SuppressWarnings("NullableProblems")
  @NonNull
  private View mFloatingTriangle;
  @SuppressWarnings("NullableProblems")
  @NonNull
  private View mTextContentContainer;

  private float mOffset;

  public FloatingMarkerView(@NonNull Context context)
  {
    super(context);
    LayoutInflater.from(getContext()).inflate(R.layout.floating_marker_view, this, true);
  }

  public FloatingMarkerView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    LayoutInflater.from(getContext()).inflate(R.layout.floating_marker_view, this, true);
  }

  public FloatingMarkerView(Context context, AttributeSet attrs, int defStyleAttr)
  {
    super(context, attrs, defStyleAttr);
    LayoutInflater.from(getContext()).inflate(R.layout.floating_marker_view, this, true);
  }

  public FloatingMarkerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
  {
    super(context, attrs, defStyleAttr, defStyleRes);
    LayoutInflater.from(getContext()).inflate(R.layout.floating_marker_view, this, true);
  }

  public void setChartView(@NonNull Chart chart) {
    mChart = chart;
  }

  @Nullable
  public Chart getChartView() {
    return mChart;
  }

  @Override
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    mInfoFloatingContainer = findViewById(R.id.info_floating_container);
    mTextContentContainer = findViewById(R.id.floating_text_container);
    mFloatingTriangle = findViewById(R.id.floating_triangle);
    mImage = findViewById(R.id.image);
    mDistanceTextView = findViewById(R.id.distance_text);
    mAltitudeView = findViewById(R.id.altitude);
    mDistanceValueView = findViewById(R.id.distance_value);
  }

  // runs every time the MarkerView is redrawn, can be used to update the
  // content (user-interface)
  @Override
  public void refreshContent(Entry e, Highlight highlight)
  {
    updatePointValues(e);

    measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
    layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
  }

  @Override
  public MPPointF getOffset()
  {
    return new MPPointF(mOffset, -getHeight() / 2f);
  }

  @Override
  public MPPointF getOffsetForDrawingAtPoint(float posX, float posY)
  {
    return getOffset();
  }

  private boolean isInvertedOrder(@NonNull Highlight highlight)
  {
    float x = highlight.getX();
    float halfImg = Math.abs(mImage.getWidth()) / 2f;
    int wholeText = Math.abs(mInfoFloatingContainer.getWidth());
    float factor = calcHorizontalFactor();
    return x + (halfImg + wholeText ) * factor >= getChartView().getXChartMax();
  }

  private float calcHorizontalFactor() {
    float delta = getChartView().getXChartMax() - getChartView().getXChartMin();
    return delta / getChartView().getContentRect().width();
  }

  private float convertContainerHeight()
  {
    float height = getChartView().getContentRect().height();
    float delta = getChartView().getYMax() - getChartView().getYMin();
    float factor =  delta / height;
    return factor * mTextContentContainer.getHeight();
  }

  private void updatePointValues(@NonNull Entry entry)
  {
    mDistanceTextView.setText(R.string.elevation_profile_distance);
    mDistanceValueView.setText(StringUtils.nativeFormatDistance(entry.getX()).toString(mDistanceValueView.getContext()));
    mAltitudeView.setText(Framework.nativeFormatAltitude(entry.getY()).toString(mAltitudeView.getContext()));
  }

  @Override
  public void draw(Canvas canvas, float posX, float posY)
  {

    MPPointF offset = getOffsetForDrawingAtPoint(posX, posY);

    int saveId = canvas.save();
    // translate to the correct position and draw
    canvas.translate(posX + offset.x, posY + offset.y);
    draw(canvas);
    canvas.restoreToCount(saveId);
  }
}
