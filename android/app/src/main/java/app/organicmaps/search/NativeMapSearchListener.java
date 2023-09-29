package app.organicmaps.search;

import androidx.annotation.Keep;

public interface NativeMapSearchListener
{
  class Result
  {
    public final String countryId;
    public final String matchedString;

    // Called from JNI.
    @Keep
    @SuppressWarnings("unused")
    public Result(String countryId, String matchedString)
    {
      this.countryId = countryId;
      this.matchedString = matchedString;
    }
  }

  // Called from JNI.
  @Keep
  @SuppressWarnings("unused")
  void onMapSearchResults(Result[] results, long timestamp, boolean isLast);
}
