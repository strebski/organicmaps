package app.organicmaps.search;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

/**
 * Native search will return results via this interface.
 */
@Keep
public interface NativeSearchListener
{
  /**
   * @param results Search results.
   * @param timestamp Timestamp of search request.
   */
  default void onResultsUpdate(@NonNull SearchResult[] results, long timestamp) {}

  /**
   * @param timestamp Timestamp of search request.
   */
  default void onResultsEnd(long timestamp) {}
}
