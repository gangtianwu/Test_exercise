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
import com.hdsxtech.www.test_exercise.bean.City;
import com.hdsxtech.www.test_exercise.bean.Country;
import com.hdsxtech.www.test_exercise.bean.Province;
import com.hdsxtech.www.test_exercise.utils.ApiUtils;
import com.hdsxtech.www.test_exercise.utils.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
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
    int PROVINCE = 1;

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
                PROVINCE = 1;
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
                                realm.copyToRealmOrUpdate(provinces.get(finalI));
//                                Province province = realm.createup(Province.class,provinces.get(finalI).getId());
////                                province.setId(provinces.get(finalI).getId());
//                                province.setName(provinces.get(finalI).getName());
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
//        CharSequence title = menu.getItem(item.getItemId()).getTitle();
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

        if (PROVINCE == 1)
            doGetProvince(item);
         if(PROVINCE == 2)
            doGetCity(item);
        if (PROVINCE == 3){
            int id = item.getItemId();
            doGetWeather(id);
        }



//        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void doGetWeather(int item) {
        String title = (String) menu.getItem(item).getTitle();
        Country name = realm.where(Country.class).equalTo("name", title).findFirst();
        int cityId = name.getCityId();
        String weather_id = name.getWeather_id();

        switchFragment(cityId,weather_id);
    }

    private void doGetCity(MenuItem item) {
        String title = (String) menu.getItem(item.getItemId()).getTitle();

        LogUtils.i("title", title);
        LogUtils.i("title_item", item.getItemId()+"");
        City name = realm.where(City.class).equalTo("name", title).findFirst();
         int provinceId = name.getProvinceId();
        final int cityid = name.getId();

        LogUtils.i("title", provinceId + "省级id");
        LogUtils.i("title_item", cityid+"市级id");
        Observable<List<Country>> observableCountry = apiService.getCountry(provinceId, cityid);
        Subscriber<List<Country>> subscriber = new Subscriber<List<Country>>() {
            @Override
            public void onCompleted() {
                LogUtils.i("区县级列表", "获取区县完成");
                PROVINCE = 3;
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.i("区县级列表", "获取区县失败");
            }

            @Override
            public void onNext(final List<Country> countries) {
                menu.removeGroup(1);
                LogUtils.i("青岛区县级列表", countries.size() + "青岛区县");
                for (int i = 0; i < countries.size(); i++) {
                    try {
                        menu.add(1, i+1, 0, countries.get(i).getName());
                        LogUtils.i("青岛区县级列表",countries.get(i).getName()+ "青岛区县");
                        final int finalI = i;
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                countries.get(finalI).setCityId(cityid);
                                realm.copyToRealmOrUpdate(countries.get(finalI));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        observableCountry.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }

    private void doGetProvince(@NonNull MenuItem item) {
        String title = (String) menu.getItem(item.getItemId()).getTitle();
        RealmResults<Province> name = realm.where(Province.class).equalTo("name", title).findAll();
        final int provinceId = name.get(0).getId();
        Observable<List<City>> cityObservable = apiService.getCity(provinceId);
        Subscriber<List<City>> subscriber = new Subscriber<List<City>>() {
            @Override
            public void onCompleted() {
                LogUtils.i("市级列表", "获取市完成");
                PROVINCE = 2;
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.i("市级列表", "获取市失败"+e);
            }

            @Override
            public void onNext(final List<City> cities) {
                LogUtils.i("市级列表数", cities.size() +"");
                menu.removeGroup(1);
                for (int i = 0; i < cities.size(); i++) {
                    try {
                        menu.add(1, i+1, 0, cities.get(i).getName());
                        final int finalI = i;
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                cities.get(finalI).setProvinceId(provinceId);
                                realm.copyToRealmOrUpdate(cities.get(finalI));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        cityObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


    private void switchFragment(int id, String weather_id) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        // TODO: 2017/10/19
        //展示天气页面
        new ThemeFragment();

    }

}
