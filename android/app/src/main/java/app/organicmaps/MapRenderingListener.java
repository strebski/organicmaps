package app.organicmaps;

import androidx.annotation.Keep;

// Used by JNI.
@Keep
@SuppressWarnings("unused")
interface MapRenderingListener
{
  void onRenderingCreated();
  void onRenderingRestored();
  void onRenderingInitializationFinished();
}
