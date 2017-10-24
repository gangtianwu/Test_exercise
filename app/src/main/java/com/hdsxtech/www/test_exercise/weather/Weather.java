package com.hdsxtech.www.test_exercise.weather;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.hdsxtech.www.test_exercise.R;
import com.hdsxtech.www.test_exercise.api.ApiService;
import com.hdsxtech.www.test_exercise.bean.Province;
import com.hdsxtech.www.test_exercise.utils.ApiUtils;
import com.hdsxtech.www.test_exercise.utils.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者:丁文 on 2017/10/19.
 * copyright: www.tpri.org.cn
 */

public class Weather extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content_main)
    RelativeLayout contentMain;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navView;
    private Menu menu;
    private Realm realm;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_menu, R.string.close_menu);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        menu = navView.getMenu();
        initData();
    }

    private void initData() {
        realm = Realm.getDefaultInstance();

        /**
         * 本地数据库有数据,从本地获取,没有则网络获取
         */
//        if (true){
//
//        }else {
            /*
            网络获取省级数据
             */
        apiService = ApiUtils.getInstance();
        final Observable<List<Province>> province = apiService.getProvince();
        Subscriber<List<Province>> subscriber = new Subscriber<List<Province>>() {
            @Override
            public void onCompleted() {
                LogUtils.i("省级列表", "获取省市完成");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.i("省级列表", "获取省市错误" + e);
            }

            @Override
            public void onNext(final List<Province> provinces) {
                LogUtils.i("省级列表", provinces.size() + "");
                for (int i = 0; i < provinces.size(); i++) {
                    try {
                        menu.add(1, provinces.get(i).getId(), 0, provinces.get(i).getName());
                        final int finalI = i;
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                Province province = realm.createObject(Province.class);
                                province.setId(provinces.get(finalI).getId());
                                province.setName(provinces.get(finalI).getName());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                navView.setNavigationItemSelectedListener(Weather.this);
            }

        };
        province.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
//        }


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            // TODO: 2017/10/19
            /**
             * 返回主界面也可以,有待添加
             */
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CharSequence title = menu.getItem(item.getItemId()).getTitle();


//        apiService.getCity()
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        ;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int id = item.getItemId();
        switchFragment(id);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void switchFragment(int id) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        // TODO: 2017/10/19
        //展示天气页面
        new ThemeFragment();

    }

}
