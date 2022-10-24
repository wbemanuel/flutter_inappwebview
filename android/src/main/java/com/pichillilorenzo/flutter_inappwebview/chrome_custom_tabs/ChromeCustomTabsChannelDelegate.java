package com.pichillilorenzo.flutter_inappwebview.chrome_custom_tabs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pichillilorenzo.flutter_inappwebview.types.ChannelDelegateImpl;
import com.pichillilorenzo.flutter_inappwebview.types.CustomTabsActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class ChromeCustomTabsChannelDelegate extends ChannelDelegateImpl {
  @Nullable
  private ChromeCustomTabsActivity chromeCustomTabsActivity;

  public ChromeCustomTabsChannelDelegate(@NonNull ChromeCustomTabsActivity chromeCustomTabsActivity, @NonNull MethodChannel channel) {
    super(channel);
    this.chromeCustomTabsActivity = chromeCustomTabsActivity;
  }

  @Override
  public void onMethodCall(@NonNull final MethodCall call, @NonNull final MethodChannel.Result result) {
    switch (call.method) {
      case "launchUrl":
        if (chromeCustomTabsActivity != null) {
          String url = (String) call.argument("url");
          if (url != null) {
            Map<String, String> headers = (Map<String, String>) call.argument("headers");
            List<String> otherLikelyURLs = (List<String>) call.argument("otherLikelyURLs");
            chromeCustomTabsActivity.launchUrl(url, headers, otherLikelyURLs);
            result.success(true);
          } else {
            result.success(false);
          }
        } else {
          result.success(false);
        }
        break;
      case "mayLaunchUrl":
        if (chromeCustomTabsActivity != null) {
          String url = (String) call.argument("url");
          if (url != null) {
            Map<String, String> headers = (Map<String, String>) call.argument("headers");
            List<String> otherLikelyURLs = (List<String>) call.argument("otherLikelyURLs");
            chromeCustomTabsActivity.mayLaunchUrl(url, headers, otherLikelyURLs);
            result.success(true);
          } else {
            result.success(false);
          }
        } else {
          result.success(false);
        }
        break;
      case "updateActionButton":
        if (chromeCustomTabsActivity != null) {
          byte[] icon = (byte[]) call.argument("icon");
          String description = (String) call.argument("description");
          chromeCustomTabsActivity.updateActionButton(icon, description);
          result.success(true);
        } else {
          result.success(false);
        }
        break;
      case "validateRelationship":
        if (chromeCustomTabsActivity != null && chromeCustomTabsActivity.customTabsSession != null) {
          Integer relation = (Integer) call.argument("relation");
          String origin = (String) call.argument("origin");
          chromeCustomTabsActivity.customTabsSession.validateRelationship(relation, Uri.parse(origin), null);
          result.success(true);
        } else {
          result.success(false);
        }
        break;
      case "close":
        if (chromeCustomTabsActivity != null) {
          chromeCustomTabsActivity.onStop();
          chromeCustomTabsActivity.onDestroy();
          chromeCustomTabsActivity.close();

          if (chromeCustomTabsActivity.manager != null && chromeCustomTabsActivity.manager.plugin != null && 
                  chromeCustomTabsActivity.manager.plugin.activity != null) {
            Activity activity = chromeCustomTabsActivity.manager.plugin.activity;
            // https://stackoverflow.com/a/41596629/4637638
            Intent myIntent = new Intent(activity, activity.getClass());
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(myIntent);
          }
          chromeCustomTabsActivity.dispose();
          result.success(true);
        } else {
          result.success(false);
        }
        break;
      default:
        result.notImplemented();
    }
  }

  public void onServiceConnected() {
    MethodChannel channel = getChannel();
    if (channel == null) return;
    Map<String, Object> obj = new HashMap<>();
    channel.invokeMethod("onServiceConnected", obj);
  }

  public void onOpened() {
    MethodChannel channel = getChannel();
    if (channel == null) return;
    Map<String, Object> obj = new HashMap<>();
    channel.invokeMethod("onOpened", obj);
  }

  public void onCompletedInitialLoad() {
    MethodChannel channel = getChannel();
    if (channel == null) return;
    Map<String, Object> obj = new HashMap<>();
    channel.invokeMethod("onCompletedInitialLoad", obj);
  }

  public void onNavigationEvent(int navigationEvent) {;
    MethodChannel channel = getChannel();
    if (channel == null) return;
    Map<String, Object> obj = new HashMap<>();
    obj.put("navigationEvent", navigationEvent);
    channel.invokeMethod("onNavigationEvent", obj);
  }

  public void onClosed() {
    MethodChannel channel = getChannel();
    if (channel == null) return;
    Map<String, Object> obj = new HashMap<>();
    channel.invokeMethod("onClosed", obj);
  }

  public void onItemActionPerform(int id, String url, String title) {
    MethodChannel channel = getChannel();
    if (channel == null) return;
    Map<String, Object> obj = new HashMap<>();
    obj.put("id", id);
    obj.put("url", url);
    obj.put("title", title);
    channel.invokeMethod("onItemActionPerform", obj);
  }

  public void onRelationshipValidationResult(int relation, @NonNull Uri requestedOrigin, boolean result) {
    MethodChannel channel = getChannel();
    if (channel == null) return;
    Map<String, Object> obj = new HashMap<>();
    obj.put("relation", relation);
    obj.put("requestedOrigin", requestedOrigin.toString());
    obj.put("result", result);
    channel.invokeMethod("onRelationshipValidationResult", obj);
  }

  @Override
  public void dispose() {
    super.dispose();
    chromeCustomTabsActivity = null; 
  }
}
