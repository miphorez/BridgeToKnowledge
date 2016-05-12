package com.cezia.bridgetoknowledge;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        BookPartFragment.ChangeBookPartListener,
        DialogPartPickerTest.OnClickListener
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menuMain = menu;
        BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
        if (partFragment == null) {
            setPartBookTransaction(EBookPart.getFirstBookPart(), false, true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //действия по кнопкам инструментальной панели
        BookPartFragment bookPartFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
        switch (item.getItemId()) {
            case R.id.action_goto_part:
                goToDialogPart();
                return true;
            case R.id.action_shrift_up:
                bookPartFragment.fontBookPart.sizeFontUp();
                return true;
            case R.id.action_shrift_down:
                bookPartFragment.fontBookPart.sizeFontDown();
                return true;
            case R.id.action_about:
                new DialogAbout(this);
//        Toast.makeText(getApplicationContext(), R.string.about_version, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_endbook:
                onExitApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToDialogPart() {
        Bundle bundle = new Bundle();
        String[] values1 = new String[] {"a","bb","ccc","dddd","eeeeee","fffffff"};
//        String[] values1 = EBookPart.getListParts(getApplicationContext());
        bundle.putStringArray(getResources().getString(R.string.string_picker_dialog_part), values1);
        String[] values2 = new String[] {"1","22","333","4444","55555","666666"};
        bundle.putStringArray(getResources().getString(R.string.string_picker_dialog_fragment), values2);
        String preset1 = "bb";
        bundle.putString(getResources().getString(R.string.string_picker_dialog_preset_part), preset1);
        String preset2 = "55555";
        bundle.putString(getResources().getString(R.string.string_picker_dialog_preset_fragment), preset2);
        DialogFragment dialogPartPicker = new DialogPartPickerTest();
        dialogPartPicker.setArguments(bundle);
        dialogPartPicker.show(getSupportFragmentManager(),"dialogPartPicker");
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
        setPartBookTransaction(new BookMark(numPart, 1), true, false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setPartBookTransaction(BookMark bookMark, boolean modeStack, boolean modeRestore) {
        BookPartFragment partFragment = new BookPartFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BookMark lastBookMark;
        if (!modeStack) {
            lastBookMark = new BookMark(BookPartPosition.loadPrefLastPart(getApplicationContext()));
        }
        else lastBookMark = new BookMark(bookMark);
        partFragment.setBookPart(lastBookMark, modeRestore);
        transaction.replace(R.id.fragment_book_container, partFragment);
        transaction.commit();
        hideShowButtons(lastBookMark);
    }

    private void hideShowButtons(BookMark bookMark) {
        FloatingActionButton btn;
        btn = (FloatingActionButton) findViewById(R.id.arrow_left);
        if (btn != null) {
            if (bookMark.getNumPart() == 0) btn.hide();
            else btn.show();
        }
        btn = (FloatingActionButton) findViewById(R.id.arrow_right);
        if (btn != null) {
            if (EBookPart.isLastBookPart(bookMark)) btn.hide();
            else btn.show();
        }
    }

    private void setNextPartBookTransaction() {
        BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
        BookMark bookMark = partFragment.getBookMark();
        bookMark = EBookPart.getNextBookMark(bookMark);
        setPartBookTransaction(bookMark, true, false);
    }

    private void setPrevPartBookTransaction() {
        BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
        BookMark bookMark = partFragment.getBookMark();
        bookMark = EBookPart.getPrevBookMark(bookMark);
        setPartBookTransaction(bookMark, true, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
        partFragment.positionBookPart.savePrefLastPartPosition();
    }

    private void onExitApp() {
        this.finish();
    }

    @Override
    public void changeBookPart(BookMark bookMark) {
        int idStr = 0;
        switch (bookMark.getNumPart()) {
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
                partFragment.setFlRestoreView(false);
                return;
            }
            changeIntentMenuShare();
        }
    }

    private void changeIntentMenuShare() {
        BookPartFragment partFragment = (BookPartFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_book_container);
        MenuItem shareItem = menuMain.findItem(R.id.action_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, partFragment.getBookPartText());
        shareActionProvider.setShareIntent(intent);
    }

    @Override
    public void onClick(String strPart, String strFragment) {

    }
}
