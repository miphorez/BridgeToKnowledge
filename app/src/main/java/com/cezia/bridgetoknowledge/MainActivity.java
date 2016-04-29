package com.cezia.bridgetoknowledge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import static com.cezia.bridgetoknowledge.BookPartFragment.*;

public class MainActivity extends AppCompatActivity
        implements  NavigationView.OnNavigationItemSelectedListener,
                    BookPartFragment.ChangeBookPartListener
{

    private Menu menuMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.arrow_right);
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setNextPartBookTransaction();
                }
            });

        fab = (FloatingActionButton) findViewById(R.id.arrow_left);
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPrevPartBookTransaction();
                }
            });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            if (navigationView != null) {
                navigationView.setNavigationItemSelectedListener(this);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            //если открыто оглавление - только закрываем его
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                //если оглавление закрыто, возвращаемся к предыдущему фрагменту
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) return;
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuMain = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
        if (partFragment == null) {
            setPartBookTransaction(0, false, true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //действия по кнопкам инструментальной панели
        switch (item.getItemId()) {
            case R.id.action_shrift_up:
                sizeFontUp();
                return true;
            case R.id.action_shrift_down:
                sizeFontDown();
                return true;
            case R.id.action_about:
//                BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
//                long partId = partFragment.getBookPartId();
//                Toast.makeText(getApplicationContext(), "partId: "+Integer.toString((int)partId), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), R.string.about_version, Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_endbook:
                onExitApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sizeFontUp() {
        BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
        TextView textView = partFragment.getTextViewBookPart();
        float size = textView.getTextSize();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, ++size);
        partFragment.savePrefFontSize(size);
    }

    private void sizeFontDown() {
        BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
        TextView textView = partFragment.getTextViewBookPart();
        float size = textView.getTextSize();
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, --size);
        partFragment.savePrefFontSize(size);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // навигация по оглавлению
        int id = item.getItemId();
        int numPart = 0;
        switch (id) {
            case R.id.foreword:
                numPart = 0;
                break;
            case R.id.part1:
                numPart = 1;
                break;
            case R.id.part2:
                numPart = 2;
                break;
            case R.id.part3:
                numPart = 3;
                break;
            case R.id.part4:
                numPart = 4;
                break;
            case R.id.part5:
                numPart = 5;
                break;
            case R.id.endbook:
                onExitApp();
                break;
        }
        setPartBookTransaction(numPart, true, false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setPartBookTransaction(int numPart, boolean modeStack, boolean modeRestore) {
        BookPartFragment partFragment = new BookPartFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int lastPart;
        if (!modeStack){
            lastPart = (int) loadPrefLastPart();
        } else lastPart = numPart;
        partFragment.setBookPart(lastPart, modeRestore);
        transaction.replace(R.id.fragment_book_container, partFragment);
//        if (modeStack) transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setNextPartBookTransaction() {
        BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
        long numPart = partFragment.getBookPartId();
        if (numPart != partFragment.getMaxBookPartId()) {
            numPart++;
            setPartBookTransaction((int) numPart, true, false);
        }
    }

    private void setPrevPartBookTransaction() {
        BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
        long numPart = partFragment.getBookPartId();
        if (numPart != 0) {
            numPart--;
            setPartBookTransaction((int) numPart, true, false);
        }
    }

    @Override
    public void changeBookPart(long id) {
        int idStr = 0;
        switch ((int) id) {
            case 0:
                idStr = R.string.foreword;
                break;
            case 1:
                idStr = R.string.part1;
                break;
            case 2:
                idStr = R.string.part2;
                break;
            case 3:
                idStr = R.string.part3;
                break;
            case 4:
                idStr = R.string.part4;
                break;
            case 5:
                idStr = R.string.part5;
                break;
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(idStr);
            //после поворота экрана оставить прежний интент
            BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
            if (partFragment.isFlRestoreView()) {
//                resetStackActivity();
                partFragment.setFlRestoreView(false);
                return;
            }
//            changeIntentMenuShare();
        }
    }

    private void resetStackActivity() {
//        while (getSupportFragmentManager().getBackStackEntryCount() > 0){
//            getSupportFragmentManager().popBackStackImmediate();
//        }
        FragmentManager manager = getSupportFragmentManager();
        int cnt = manager.getBackStackEntryCount();
        if (cnt > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(cnt-1);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

//    private void changeIntentMenuShare() {
//        BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
//        BookPart bookPart = partFragment.getItemBookPart();
//        MenuItem shareItem = menuMain.findItem(R.id.action_share);
//        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_TEXT, bookPart.getText());
//        shareActionProvider.setShareIntent(intent);
//    }

    private long loadPrefLastPart(){
        long lastPart = PREF_LAST_PART;
        SharedPreferences prefSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (prefSettings.contains(APP_PREFERENCES_LAST_PART)) {
            lastPart = prefSettings.getLong(APP_PREFERENCES_LAST_PART, PREF_LAST_PART);
        }
        return lastPart;
    }

    private void savePrefLastPartPosition(){
        BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
        ScrollView scrollView = partFragment.getScrollViewBookPart();
        SharedPreferences prefSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefSettings.edit();
        editor.putInt(APP_PREFERENCES_LAST_PART_POSITION, scrollView.getScrollY());
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePrefLastPartPosition();
    }

    private void onExitApp() {
        this.finish();
    }
}
