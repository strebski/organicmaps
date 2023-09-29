package app.organicmaps.bookmarks.data;

import android.annotation.SuppressLint;
import android.os.Parcel;

import androidx.annotation.IntRange;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import app.organicmaps.routing.RoutePointInfo;
import app.organicmaps.search.Popularity;

// TODO consider refactoring to remove hack with MapObject unmarshalling itself and Bookmark at the same time.
@SuppressLint("ParcelCreator")
public class Bookmark extends MapObject
{
  private final Icon mIcon;
  private final long mCategoryId;
  private final long mBookmarkId;
  private final double mMerX;
  private final double mMerY;

  // Called from JNI.
  @Keep
  @SuppressWarnings("unused")
  public Bookmark(@NonNull FeatureId featureId, @IntRange(from = 0) long categoryId,
                  @IntRange(from = 0) long bookmarkId, String title, @Nullable String secondaryTitle,
                  @Nullable String subtitle, @Nullable String address, @Nullable RoutePointInfo routePointInfo,
                  @OpeningMode int openingMode, @NonNull Popularity popularity, @NonNull String description,
                  @Nullable String[] rawTypes)
  {
    super(featureId, BOOKMARK, title, secondaryTitle, subtitle, address, 0, 0, "",
          routePointInfo, openingMode, popularity, description, RoadWarningMarkType.UNKNOWN.ordinal(), rawTypes);

    mCategoryId = categoryId;
    mBookmarkId = bookmarkId;
    mIcon = getIconInternal();

    final ParcelablePointD ll = BookmarkManager.INSTANCE.getBookmarkXY(mBookmarkId);
    mMerX = ll.x;
    mMerY = ll.y;

    initXY();
  }

  private void initXY()
  {
    setLat(Math.toDegrees(2.0 * Math.atan(Math.exp(Math.toRadians(mMerY))) - Math.PI / 2.0));
    setLon(mMerX);
  }

  @Override
  public void writeToParcel(Parcel dest, int flags)
  {
    super.writeToParcel(dest, flags);
    dest.writeLong(mCategoryId);
    dest.writeLong(mBookmarkId);
    dest.writeParcelable(mIcon, flags);
    dest.writeDouble(mMerX);
    dest.writeDouble(mMerY);
  }

  // Do not use Core while restoring from Parcel! In some cases this constructor is called before
  // the App is completely initialized.
  // TODO: Method restoreHasCurrentPermission causes this strange behaviour, needs to be investigated.
  protected Bookmark(@MapObjectType int type, Parcel source)
  {
    super(type, source);
    mCategoryId = source.readLong();
    mBookmarkId = source.readLong();
    mIcon = source.readParcelable(Icon.class.getClassLoader());
    mMerX = source.readDouble();
    mMerY = source.readDouble();
    initXY();
  }

  private Icon getIconInternal()
  {
    return new Icon(BookmarkManager.INSTANCE.getBookmarkColor(mBookmarkId),
                    BookmarkManager.INSTANCE.getBookmarkIcon(mBookmarkId));
  }

  public long getCategoryId()
  {
    return mCategoryId;
  }

  public long getBookmarkId()
  {
    return mBookmarkId;
  }

  @NonNull
  public String getBookmarkDescription()
  {
    return BookmarkManager.INSTANCE.getBookmarkDescription(mBookmarkId);
  }
}
