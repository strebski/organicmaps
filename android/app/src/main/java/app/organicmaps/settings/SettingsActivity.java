package app.organicmaps.settings;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import app.organicmaps.R;
import app.organicmaps.base.BaseToolbarActivity;

public class SettingsActivity extends BaseToolbarActivity
                              implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback,
                                         PreferenceFragmentCompat.OnPreferenceStartScreenCallback
{
  @Override
  protected int getContentLayoutResId()
  {
    return R.layout.activity_settings;
  }

  @Override
  protected Class<? extends Fragment> getFragmentClass()
  {
    return SettingsPrefsFragment.class;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean onPreferenceStartFragment(@NonNull PreferenceFragmentCompat caller, @NonNull Preference pref)
  {
    String title = TextUtils.isEmpty(pref.getTitle()) ? null : pref.getTitle().toString();
    try
    {
      Class<? extends Fragment> fragment = (Class<? extends Fragment>) Class.forName(pref.getFragment());
      stackFragment(fragment, title, pref.getExtras());
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    return true;
  }

  @Override
  public boolean onPreferenceStartScreen(@NonNull PreferenceFragmentCompat preferenceFragmentCompat,
                                         @NonNull PreferenceScreen preferenceScreen)
  {
    Bundle args = new Bundle();
    args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.getKey());
    stackFragment(SettingsPrefsFragment.class, preferenceScreen.getTitle().toString(), args);
    return true;
  }
}
