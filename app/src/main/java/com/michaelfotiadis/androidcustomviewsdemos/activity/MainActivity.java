package com.michaelfotiadis.androidcustomviewsdemos.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.michaelfotiadis.androidcustomviewsdemos.R;
import com.michaelfotiadis.androidcustomviewsdemos.fragments.AnalogClockFragment;
import com.michaelfotiadis.androidcustomviewsdemos.fragments.ColourPickerFragment;
import com.michaelfotiadis.androidcustomviewsdemos.fragments.DateEditTextFragment;
import com.michaelfotiadis.androidcustomviewsdemos.fragments.FusionClockFragment;
import com.michaelfotiadis.androidcustomviewsdemos.fragments.TiledGridFragment;
import com.michaelfotiadis.androidcustomviewsdemos.fragments.TiledImageFragment;
import com.michaelfotiadis.androidcustomviewsdemos.fragments.TypewriterFragment;
import com.michaelfotiadis.androidcustomviewsdemos.fragments.WifiSignalFragment;
import com.michaelfotiadis.androidcustomviewsdemos.utils.DialogUtils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.Iconics;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

public class MainActivity extends AppCompatActivity {

    private static final int ICON_SIZE = 24;
    private static final int ICON_TINT = R.color.primary;
    private static final String TAG = "MainActivity";
    Drawer drawer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final PrimaryDrawerItem item1 = new PrimaryDrawerItem()
                .withIdentifier(0)
                .withIcon(getDrawerIcon(FontAwesome.Icon.faw_keyboard_o))
                .withName(R.string.title_section1);
        final PrimaryDrawerItem item2 = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withIcon(getDrawerIcon(FontAwesome.Icon.faw_picture_o))
                .withName(R.string.title_section2);
        final PrimaryDrawerItem item3 = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withIcon(getDrawerIcon(FontAwesome.Icon.faw_clock_o))
                .withName(R.string.title_section3);
        final PrimaryDrawerItem item4 = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withIcon(getDrawerIcon(FontAwesome.Icon.faw_clock_o))
                .withName(R.string.title_section4);
        final PrimaryDrawerItem item5 = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withIcon(getDrawerIcon(FontAwesome.Icon.faw_file_image_o))
                .withName(R.string.title_section5);
        final PrimaryDrawerItem item6 = new PrimaryDrawerItem()
                .withIdentifier(5)
                .withIcon(getDrawerIcon(FontAwesome.Icon.faw_file_image_o))
                .withName(R.string.title_section6);
        final PrimaryDrawerItem item7 = new PrimaryDrawerItem()
                .withIdentifier(6)
                .withIcon(getDrawerIcon(FontAwesome.Icon.faw_calendar))
                .withName(R.string.title_section7);
        final PrimaryDrawerItem item8 = new PrimaryDrawerItem()
                .withIdentifier(7)
                .withIcon(getDrawerIcon(FontAwesome.Icon.faw_wifi))
                .withName(R.string.title_section8);

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withCloseOnClick(true)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        item1, item2, item3, item4, item5, item6, item7, item8
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    drawer.closeDrawer();
                    return onNavigationDrawerItemSelected((int) drawerItem.getIdentifier());
                })
                .withSliderBackgroundColor(getResources().getColor(R.color.md_grey_200))
                .build();
        drawer.setSelection(0);
    }

    private IconicsDrawable getDrawerIcon(final FontAwesome.Icon icon) {
        return new IconicsDrawable(this)
                .icon(icon)
                .colorRes(ICON_TINT)
                .sizeDp(ICON_SIZE);
    }

    public boolean onNavigationDrawerItemSelected(final int id) {
        final Fragment fragment;
        switch (id) {
            case 0:
                fragment = TypewriterFragment.newInstance();
                break;
            case 1:
                fragment = ColourPickerFragment.newInstance();
                break;
            case 2:
                fragment = AnalogClockFragment.newInstance();
                break;
            case 3:
                fragment = FusionClockFragment.newInstance();
                break;
            case 4:
                fragment = TiledImageFragment.newInstance();
                break;
            case 5:
                fragment = TiledGridFragment.newInstance();
                break;
            case 6:
                fragment = DateEditTextFragment.newInstance();
                break;
            case 7:
                fragment = WifiSignalFragment.newInstance();
                break;
            default:
                fragment = null;
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            hideKeyboard();
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_about) {
            new DialogUtils.AboutDialog().show(getSupportFragmentManager(), "dialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

}
