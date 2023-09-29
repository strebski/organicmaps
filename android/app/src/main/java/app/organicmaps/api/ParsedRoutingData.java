package app.organicmaps.api;

import androidx.annotation.Keep;

import app.organicmaps.Framework;

/**
 * Represents Framework::ParsedRoutingData from core.
 */
public class ParsedRoutingData
{
  public final RoutePoint[] mPoints;
  @Framework.RouterType
  public final int mRouterType;

  // Called from JNI.
  @Keep
  @SuppressWarnings("unused")
  public ParsedRoutingData(RoutePoint[] points, int routerType) {
    this.mPoints = points;
    this.mRouterType = routerType;
  }
}
