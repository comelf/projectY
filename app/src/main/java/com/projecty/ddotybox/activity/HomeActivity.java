/*
목적: 메인 화면
동작
    - 메인 화면 보여주기
    - 액션바 세팅
    - 우측 상단 네비게이션 세팅
*/

package com.projecty.ddotybox.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mocoplex.adlib.AdlibManager;
import com.mocoplex.adlib.dlg.AdlibDialogAdListener;
import com.projecty.ddotybox.R;
import com.projecty.ddotybox.fragment.BjLogFragment;
import com.projecty.ddotybox.fragment.CommunityFragment;
import com.projecty.ddotybox.fragment.FavoriteFragment;
import com.projecty.ddotybox.fragment.PlayListFragment;
import com.projecty.ddotybox.fragment.RecommendFragment;
import com.projecty.ddotybox.fragment.SandboxNetworkFragment;
import com.projecty.ddotybox.fragment.SearchFragment;
import com.projecty.ddotybox.fragment.SearchResultFragment;
import com.projecty.ddotybox.fragment.SettingFragment;
import com.projecty.ddotybox.fragment.StoreFragment;
import com.projecty.ddotybox.fragment.VideoFragment;
import com.projecty.ddotybox.model.UserProfile;
import com.projecty.ddotybox.util.Global;

import java.util.ArrayList;
import java.util.List;
public class HomeActivity extends ActionBarActivity implements View.OnClickListener{

    private static final int HOME_LAYOUT = 0;
    private static final int PLAYLIST_LAYOUT = 1;
    private static final int FAVORITE_LAYOUT = 2;
    private static final int FEATURED_LAYOUT = 3;
    private static final int BJLOG_LAYOUT = 4;
    private static final int COMMUNITY_LAYOUT = 5;
    private static final int BJ1_LAYOUT = 6;
    private static final int BJ2_LAYOUT = 7;
    private static final int BJ3_LAYOUT = 8;
    private static final int BJ4_LAYOUT = 9;
    private static final int BJ5_LAYOUT = 10;
    private static final int BJ6_LAYOUT = 11;
    private static final int BJ7_LAYOUT = 12;
    private static final int STORE_LAYOUT = 13;
    
    
    private static final String SET = "set";
    private static final String HOME = "home";
    private static final String PLAYLIST = "playlist";
    private static final String FAVORITE = "favorite";
    private static final String RECOMMEND= "recommend";
    private static final String DLOG = "dlog";
    private static final String COMMUNITY = "community";
    private static final String BJ1 = "bj1";
    private static final String BJ2 = "bj2";
    private static final String BJ3 = "bj3";
    private static final String BJ4 = "bj4";
    private static final String BJ5 = "bj5";
    private static final String BJ6 = "bj6";
    private static final String BJ7 = "bj7";
    private static final String STORE = "mycube";

    private AdlibManager _amanager;

    // 애드립 광고를 테스트 하기 위한 키 입니다.
    private String ADLIB_API_KEY = "5573f4b80cf2d5747f1ccc77";

    private List<FrameLayout> layoutList = new ArrayList<FrameLayout>();
    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle dtToggle;
    private MenuItem item;
    LinearLayout drawer;

    public UserProfile user;


    /*
    이름: onBackPressed()
    목적: 뒤로가기 버튼
    동작
        - 클릭마다 스택으로 fragment를 쌓음
        - fragment가 <= 1일(메인화면) 때 종료 여부 물어보기  
     */
    
    @Override
    public void onBackPressed() {
        int num = getSupportFragmentManager().getBackStackEntryCount();
        if(num <= 1){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                int[] colors = new int[]{0xffffffff, 0xffa8a8a8, 0xff404040, 0xff404040, 0xffdfdfdf};
                _amanager.showAdDialog("취소", "확인", "App 을 정말로 종료하시겠습니까?", colors, new AdlibDialogAdListener() {

                    @Override
                    public void onLeftClicked() {
                    }

                    @Override
                    public void onRightClicked() {
                        HomeActivity.this.finish();
                    }
                });
            } else {
                AlertDialog dialog;
                dialog = new AlertDialog.Builder(this)
                        // .setIcon(R.drawable.warning)
                        .setMessage("종료하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                //dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        })
                        .show();
            }

        }
            else {
            FragmentManager.BackStackEntry topFr = getSupportFragmentManager().getBackStackEntryAt(num - 2);
            String name = topFr.getName();
            backStack(name);
            getSupportFragmentManager().popBackStackImmediate();
        }
    }
    
    /*
    이름: checkBackStack()
    목적: 스택에 쌓을 때 main, 검색외엔 쌓지 않게 하기 위해. 뒤로가기 하면 무조건 main으로
    동작
        - main 화면을 제외하고는 모두 스택에 쌓지 않음
     */
    
    private void checkBackStack(){
        int num = getSupportFragmentManager().getBackStackEntryCount();
        while (num >=2){
            getSupportFragmentManager().popBackStackImmediate();
            num--;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("홈");
        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.string.app_name, R.string.app_name);
        dlDrawer.setDrawerListener(dtToggle);
        drawer = (LinearLayout) findViewById(R.id.drawer);
        drawer.setOnClickListener(new DrawerItemClickListener());

        setupTopButton();
        setLayoutList();
        setUserProfile();




        // 애드립 광고
        // 각 애드립 액티비티에 애드립 앱 키값을 필수로 넣어주어야 합니다.
        _amanager = new AdlibManager(ADLIB_API_KEY);
        _amanager.onCreate(this);
//        _amanager.setAdlibTestMode(true); // 테스트 광고 노출로, 상용일 경우 꼭 제거해야 합니다.



        if (Global.YOUTUBE_API_KEY.startsWith("YOUR_API_KEY")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("Edit ApiKey.java and replace \"YOUR_API_KEY\" with your Applications Browser API Key")
                    .setTitle("Missing API Key")
                    .setNeutralButton("Ok, I got it!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (savedInstanceState == null) {
            String tag=HOME;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new VideoFragment(), tag).addToBackStack(tag)
                    .commit();
        }

        setUP();
    }

    private void setUserProfile() {
        TextView id = (TextView) findViewById(R.id.user_id);
        TextView email = (TextView) findViewById(R.id.user_eamil);

        user = UserProfile.getUser();
        id.setText(user.getUserName());
        email.setText(user.getUserEmail());
    }

    private void setUP() {
        TextView userId = (TextView) findViewById(R.id.user_id);
        TextView bjName = (TextView) findViewById(R.id.bj_name);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "NotoSans.otf");
        userId.setTypeface(custom_font);
        bjName.setTypeface(custom_font);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                SearchResultFragment fragment = new SearchResultFragment();
                fragment.setQuery(s);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, fragment,"SEARCH_RESULT")
                        .commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    closeFragment();

                    searchView.onActionViewCollapsed();
                }
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new SearchFragment(),"SEARCH")
                        .commit();
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                closeFragment();
                return false;

            }
        });

        return true;
    }

    private void closeFragment() {
        Fragment search = getSupportFragmentManager().findFragmentByTag("SEARCH");
        if(search!=null) {
            getSupportFragmentManager().popBackStack(search.getId(),0);
        }
        Fragment search_result = getSupportFragmentManager().findFragmentByTag("SEARCH_RESULT");
        if(search_result!=null) {
            getSupportFragmentManager().popBackStack(search_result.getId(),0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        dtToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }



    private void setupTopButton() {
        List<Button> buttons = new ArrayList<Button>();
        buttons.add((Button) findViewById(R.id.settingButton));
        buttons.add((Button) findViewById(R.id.inviteButton));
//        buttons.add((Button) findViewById(R.id.readButton));
        buttons.add((Button) findViewById(R.id.homeButton));
        buttons.add((Button) findViewById(R.id.favoriteButton));
        buttons.add((Button) findViewById(R.id.featuredButton));
        buttons.add((Button) findViewById(R.id.bjlogButton));
        buttons.add((Button) findViewById(R.id.communityButton));
        buttons.add((Button) findViewById(R.id.mycubeButton));
        buttons.add((Button) findViewById(R.id.playlistButton));
        buttons.add((Button) findViewById(R.id.bj1));
        buttons.add((Button) findViewById(R.id.bj2));
        buttons.add((Button) findViewById(R.id.bj3));
        buttons.add((Button) findViewById(R.id.bj4));
        buttons.add((Button) findViewById(R.id.bj5));
        buttons.add((Button) findViewById(R.id.bj6));
        buttons.add((Button) findViewById(R.id.bj7));

        Typeface font = Typeface.createFromAsset(getAssets(), "NotoSans.otf");
        for(Button btn : buttons){
            btn.setOnClickListener(this);
            btn.setTypeface(font);
        }
    }

    
    /*
    이름: onClick()
    목적: 클릭에 따른 화면 이동
    
     */

    
    @Override
    public void onClick(View v) {

        String tag;

        switch (v.getId()){
            case R.id.settingButton:
                toolbar.setTitle("설정");
                tag =SET;
                checkBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new SettingFragment(), tag).addToBackStack(tag)
                        .commit();

                break;
            case R.id.inviteButton:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "[도티 BOX 초대]\n" +
                        "도티TV의 모바일 공식 앱 '도티 BOX' 전격 출시! 앱 다운받고 도티의 게임방송을 더욱 쉽고 빠르게 시청해보세요! https://play.google.com/store/apps/details?id=com.projecty.ddotybox");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
//            case R.id.readButton:
//                Toast toast2 = Toast.makeText(getApplicationContext(),
//                        "준비중입니다.", Toast.LENGTH_SHORT);
//                toast2.setGravity(Gravity.CENTER, 0, 0);
//                toast2.show();
//                break;
            case R.id.homeButton:
                toolbar.setTitle("홈");
                tag = HOME;
                setLayoutBackgroundColor(HOME_LAYOUT);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new VideoFragment(), tag)
                        .commit();
                break;
            case R.id.playlistButton:
                toolbar.setTitle("재생목록");
                tag =PLAYLIST;
                setLayoutBackgroundColor(PLAYLIST_LAYOUT);

                checkBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new PlayListFragment(),tag).addToBackStack(tag)
                        .commit();
                break;
            case R.id.favoriteButton:
                toolbar.setTitle("즐겨찾기");
                tag =FAVORITE;
                setLayoutBackgroundColor(FAVORITE_LAYOUT);

                checkBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new FavoriteFragment(),tag).addToBackStack(tag)
                        .commit();
                break;
            case R.id.featuredButton:
                toolbar.setTitle("추천영상");
                tag=RECOMMEND;
                setLayoutBackgroundColor(FEATURED_LAYOUT);

                checkBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new RecommendFragment(), tag).addToBackStack(tag)
                        .commit();
                break;

            case R.id.bjlogButton:
                tag=DLOG;
                toolbar.setTitle("도티로그");
                setLayoutBackgroundColor(BJLOG_LAYOUT);

                checkBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new BjLogFragment(), tag).addToBackStack(tag)
                        .commit();
                break;
            case R.id.communityButton:
                tag=COMMUNITY;
                toolbar.setTitle("커뮤니티");
                setLayoutBackgroundColor(COMMUNITY_LAYOUT);

                checkBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new CommunityFragment(), tag).addToBackStack(tag)
                        .commit();
                break;

            case R.id.mycubeButton:
                tag=STORE;
                toolbar.setTitle("샌드박스 스토어");
                setLayoutBackgroundColor(STORE_LAYOUT);


                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new StoreFragment(), tag).commit();
                break;
            
            
            case R.id.bj1:
                toolbar.setTitle("잠뜰 BOX");
                tag=BJ1;
                setLayoutBackgroundColor(BJ1_LAYOUT);
                SandboxNetworkFragment fr1 = new SandboxNetworkFragment();
                fr1.setSandboxId("UUg7rkxrTnIhiHEpXY1ec9NA");
                fr1.setSandboxCh("UCg7rkxrTnIhiHEpXY1ec9NA");

                checkBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,fr1, tag).addToBackStack(tag)
                        .commit();
                break;
            case R.id.bj2:
                tag=BJ2;
                toolbar.setTitle("쁘허 BOX");
                setLayoutBackgroundColor(BJ2_LAYOUT);
                SandboxNetworkFragment fr2 = new SandboxNetworkFragment();
                fr2.setSandboxId("UUtCnnCUn9IDDQRU9_04JD3g");
                fr2.setSandboxCh("UCtCnnCUn9IDDQRU9_04JD3g");

                checkBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fr2, tag).addToBackStack(tag)
                        .commit();
                break;
            case R.id.bj3:
                tag=BJ3;
                toolbar.setTitle("태경 BOX");
                setLayoutBackgroundColor(BJ3_LAYOUT);
                SandboxNetworkFragment fr3 = new SandboxNetworkFragment();
                fr3.setSandboxId("UUEPuItFWOOJ2o5hTu65NlEg");
                fr3.setSandboxCh("UCEPuItFWOOJ2o5hTu65NlEg");

                checkBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fr3, tag).addToBackStack(tag)
                        .commit();
                break;
            case R.id.bj4:
                tag=BJ4;
                toolbar.setTitle("빅민 BOX");
                setLayoutBackgroundColor(BJ4_LAYOUT);
                SandboxNetworkFragment fr4 = new SandboxNetworkFragment();
                fr4.setSandboxId("UUxmBxNybpaLO7x61dm0oD8w");
                fr4.setSandboxCh("UCxmBxNybpaLO7x61dm0oD8w");

                checkBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fr4, tag).addToBackStack(tag)
                        .commit();
                break;
            case R.id.bj5:
                tag=BJ5;
                toolbar.setTitle("비콘 BOX");
                setLayoutBackgroundColor(BJ5_LAYOUT);
                SandboxNetworkFragment fr5 = new SandboxNetworkFragment();
                fr5.setSandboxId("UUT_Sf9z6Cqy11VHOfbnQPNQ");
                fr5.setSandboxCh("UCT_Sf9z6Cqy11VHOfbnQPNQ");

                checkBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fr5, tag).addToBackStack(tag)
                        .commit();
                break;
            case R.id.bj6:
                tag=BJ6;
                toolbar.setTitle("퀸톨 BOX");
                setLayoutBackgroundColor(BJ6_LAYOUT);
                SandboxNetworkFragment fr6 = new SandboxNetworkFragment();
                fr6.setSandboxId("UUiwOunGuqfKjcLIBsteAAJQ");
                fr6.setSandboxCh("UCiwOunGuqfKjcLIBsteAAJQ");

                checkBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fr6, tag).addToBackStack(tag)
                        .commit();
                break;
            case R.id.bj7:
                tag=BJ7;
                toolbar.setTitle("찬이 BOX");
                setLayoutBackgroundColor(BJ7_LAYOUT);
                SandboxNetworkFragment fr7 = new SandboxNetworkFragment();
                fr7.setSandboxId("UUt51IEo3ZxxOysVAG_ylR6w");
                fr7.setSandboxCh("UCt51IEo3ZxxOysVAG_ylR6w");

                checkBackStack();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fr7, tag).addToBackStack(tag)
                        .commit();
                break;


        }
        dlDrawer.closeDrawer(drawer);
    }

    private void setLayoutBackgroundColor(int layout_id){
        for(FrameLayout layout : layoutList){
            layout.setBackgroundResource(R.color.drawer_background);
        }
        layoutList.get(layout_id).setBackgroundResource(R.color.theme_color2);
    }

    private void setLayoutList() {
        layoutList.add((FrameLayout) findViewById(R.id.home_layout));
        layoutList.add((FrameLayout) findViewById(R.id.playlist_layout));
        layoutList.add((FrameLayout) findViewById(R.id.favorite_layout));
        layoutList.add((FrameLayout) findViewById(R.id.featured_layout));
        layoutList.add((FrameLayout) findViewById(R.id.bjlog_layout));
        layoutList.add((FrameLayout) findViewById(R.id.community_layout));
        layoutList.add((FrameLayout) findViewById(R.id.mycube_layout));
        layoutList.add((FrameLayout) findViewById(R.id.bj1_layout));
        layoutList.add((FrameLayout) findViewById(R.id.bj2_layout));
        layoutList.add((FrameLayout) findViewById(R.id.bj3_layout));
        layoutList.add((FrameLayout) findViewById(R.id.bj4_layout));
        layoutList.add((FrameLayout) findViewById(R.id.bj5_layout));
        layoutList.add((FrameLayout) findViewById(R.id.bj6_layout));
        layoutList.add((FrameLayout) findViewById(R.id.bj7_layout));
    }

    /*
            Drawer 클릭시 닫음 (필요없을때 제거)
     */
    private class DrawerItemClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }
    public void backStack(String fr) {

        switch (fr){
            case SET:
                toolbar.setTitle("설정");
                break;
            case HOME:
                toolbar.setTitle("홈");
                setLayoutBackgroundColor(HOME_LAYOUT);
                break;
            case PLAYLIST:
                toolbar.setTitle("재생목록");
                setLayoutBackgroundColor(PLAYLIST_LAYOUT);
                break;
            case FAVORITE:
                toolbar.setTitle("즐겨찾기");
                setLayoutBackgroundColor(FAVORITE_LAYOUT);

                break;
            case RECOMMEND:
                toolbar.setTitle("추천영상");
                setLayoutBackgroundColor(FEATURED_LAYOUT);
                break;
            case DLOG:
                toolbar.setTitle("도티로그");
                setLayoutBackgroundColor(BJLOG_LAYOUT);
                break;
            case COMMUNITY:
                toolbar.setTitle("커뮤니티");
                setLayoutBackgroundColor(COMMUNITY_LAYOUT);
                break;
            case STORE:
                toolbar.setTitle("샌드박스 스토어");
                setLayoutBackgroundColor(STORE_LAYOUT);
                break;
            
            case BJ1:
                toolbar.setTitle("잠뜰 BOX");
                setLayoutBackgroundColor(BJ1_LAYOUT);
                break;
            case BJ2:
                toolbar.setTitle("쁘허 BOX");
                setLayoutBackgroundColor(BJ2_LAYOUT);
                break;
            case BJ3:
                toolbar.setTitle("태경 BOX");
                setLayoutBackgroundColor(BJ3_LAYOUT);
                break;
            case BJ4:
                toolbar.setTitle("빅민 BOX");
                setLayoutBackgroundColor(BJ4_LAYOUT);
                break;
            case BJ5:
                toolbar.setTitle("비콘 BOX");
                setLayoutBackgroundColor(BJ5_LAYOUT);
                break;
            case BJ6:
                toolbar.setTitle("퀸톨 BOX");
                setLayoutBackgroundColor(BJ6_LAYOUT);
                break;
            case BJ7:
                toolbar.setTitle("찬이 BOX");
                setLayoutBackgroundColor(BJ7_LAYOUT);
                break;
            default:
                break;

        }
    }
}
