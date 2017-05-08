package com.cerezaconsulting.compendio.presentation.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseActivity;
import com.cerezaconsulting.compendio.data.local.CompedioDbHelper;
import com.cerezaconsulting.compendio.data.local.SessionManager;
import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.data.model.UserEntity;
import com.cerezaconsulting.compendio.presentation.fragments.CourseFragment;
import com.cerezaconsulting.compendio.presentation.presenters.CoursePresenter;
import com.cerezaconsulting.compendio.services.SocketService;
import com.cerezaconsulting.compendio.utils.ActivityUtils;
import com.cerezaconsulting.compendio.utils.GlideUtils;


public class PanelActivity extends BaseActivity {

    DrawerLayout mDrawer;
    NavigationView navigationView;
    SessionManager mSessionManager;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    public TextView tv_username;
    public ImageView profile_image;
    public TextView tv_state_gender;
    public UserEntity mUser;
    CourseFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mSessionManager = new SessionManager(this);


        /**
         *Setup the DrawerLayout and NavigationView
         */
        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        /**
         * Setup click events on the Navigation View Items.
         */

        setSupportActionBar(toolbar);

        toolbar.setTitle("Mis Cursos");

        setupDrawerContent(navigationView);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                    /* host Activity */
                mDrawer,                    /* DrawerLayout object */
                toolbar,
                R.string.app_name,  /* "open drawer" description for accessibility */
                R.string.app_name  /* "close drawer" description for accessibility */
        );
        mDrawerToggle.syncState();
        mDrawer.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
        View header = navigationView.getHeaderView(0);

        tv_username = (TextView) header.findViewById(R.id.tv_fullnanme);
        tv_state_gender = (TextView) header.findViewById(R.id.tv_state_gender);
        profile_image = (ImageView) header.findViewById(R.id.imageView);
        //  startService(new Intent(this, GeolocationService.class));
        initHeader();


        fragment = (CourseFragment) getSupportFragmentManager().findFragmentById(R.id.body);
        if (fragment == null) {
            fragment = CourseFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.body);
        }

        new CoursePresenter(fragment, getApplicationContext());


        if (isMyServiceRunning(SocketService.class)) {
            nextActivity(this,null,LoadSocketActivity.class,false);
        }
    }


    /*private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DatePendingProfileFragment(), "Pendientes");
        adapter.addFragment(new DateRealizeProfileFragment(), "Realizadas");
        adapter.addFragment(new DateExpiredProfileFragment(), "Canceladas");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {


                    menuItem.setChecked(false);
                    menuItem.setCheckable(false);


                    switch (menuItem.getItemId()) {
                        case R.id.nav_connect:
                         /*  Intent intent_connect = new Intent(getBaseContext(), ProfileActivity.class);
                            startActivityForResult(intent_connect,200);
            */
                            break;
                        case R.id.nav_signout:

                            CloseSession();

                            break;

                        default:

                            break;
                    }
                    menuItem.setChecked(false);
                    //  mDrawer.closeDrawers();
                    return true;
                });
    }


    private void CloseSession() {
        stopService(new Intent(getBaseContext(), SocketService.class));
        mSessionManager.closeSession();
        deleteDatabase(CompedioDbHelper.DATABASE_NAME);
        newActivityClearPreview(this, null, LoginActivity.class);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;

            case R.id.menu_refresh:
                Toast.makeText(this, "Sincronizando", Toast.LENGTH_SHORT).show();
                startService(new Intent(getBaseContext(), SocketService.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initHeader() {

        mUser = mSessionManager.getUserEntity();
        if (mUser != null) {

            tv_username.setText(mUser.getFullName());
            tv_state_gender.setText(mUser.getEmail());
            if (mUser.getCompany() != null) {
                GlideUtils.loadImage(profile_image, mUser.getCompany().getLogo(), this);

            }
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sinc, menu);
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Activity.RESULT_OK == resultCode)
            if (200 == requestCode) {
                initHeader();
                return;
            }


        if (data != null) {

            CourseEntity courseEntity = (CourseEntity) data.getSerializableExtra("course");


            if (courseEntity != null) {


                Bundle bundle = new Bundle();
                bundle.putSerializable("course", courseEntity);
                fragment.openChapter(bundle
                );
            }


        }


    }

    @Override
    public void onBackPressed() {
        if (this.mDrawer.isDrawerOpen(GravityCompat.START)) {
            this.mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
