package de.petesky.motorlist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import de.petesky.motorlist.databinding.ActivityMainBinding;

import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SortedListAdapter.Callback {

    private static final Comparator<ListModel> COMPARATOR = new SortedListAdapter.ComparatorBuilder<ListModel>()
            .setOrderForModel(ListModel.class, new Comparator<ListModel>() {
                @Override
                public int compare(ListModel a, ListModel b) {
                    return Integer.signum(a.getRank() - b.getRank());
                }
            })
            .build();

    private ListAdapter mAdapter;
    private ActivityMainBinding mBinding;
    private Animator mAnimator;

    private List<ListModel> mModels;

    private void getVersionInfo() {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String message = getString(R.string.alert_info, versionCode, versionName);
        String title = getString(R.string.title_info);
        new MaterialDialog.Builder(MainActivity.this)
                .theme(Theme.LIGHT)
                .iconRes(R.mipmap.ic_launcher_round)
                .title(title)
                .content(message)
                .positiveText("OK")
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(mBinding.toolBar);

        mAdapter = new ListAdapter(this, COMPARATOR, new ListAdapter.Listener() {
            public void onExampleModelClicked(ListModel model) {

                new MaterialDialog.Builder(MainActivity.this)
                    .theme(Theme.LIGHT)
                    .title(model.getAkz())
                    .content(model.getDetail())
                    .positiveText("OK")
                    .show();
            }
        });

        mAdapter.addCallback(this);

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);

        mModels = new ArrayList<>();
        final String[] akz = getResources().getStringArray(R.array.akz);
        final String[] detail = getResources().getStringArray(R.array.detail);
        for (int i = 0; i < akz.length; i++) {
            mModels.add(new ListModel(i, i + 1, akz[i],detail[i]));
        }
        mAdapter.edit()
                .replaceAll(mModels)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final MenuItem settingsItem = menu.findItem(R.id.settings);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            /*
            new MaterialDialog.Builder(MainActivity.this)
                    .theme(Theme.LIGHT)
                    .iconRes(R.mipmap.ic_launcher_round)
                    .title("Motoren Liste")
                    .content("© 2017 Peter Marowsky")
                    .positiveText("OK")
                    .show();
            */
            getVersionInfo();
            return true;
        }
        if (id == R.id.exit){
            finish();
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<ListModel> filteredModelList = filter(mModels, query);
        mAdapter.edit()
                .replaceAll(filteredModelList)
                .commit();
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private static List<ListModel> filter(List<ListModel> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<ListModel> filteredModelList = new ArrayList<>();
        for (ListModel model : models) {
            final String akz = model.getAkz().toLowerCase();
            final String detail = model.getDetail().toLowerCase();
            if (akz.contains(lowerCaseQuery) || detail.contains(lowerCaseQuery)) {
            //if (akz.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onEditStarted() {
        if (mBinding.editProgressBar.getVisibility() != View.VISIBLE) {
            mBinding.editProgressBar.setVisibility(View.VISIBLE);
            mBinding.editProgressBar.setAlpha(0.0f);
        }

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ObjectAnimator.ofFloat(mBinding.editProgressBar, View.ALPHA, 1.0f);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.start();

        mBinding.recyclerView.animate().alpha(0.5f);
    }

    @Override
    public void onEditFinished() {
        mBinding.recyclerView.scrollToPosition(0);
        mBinding.recyclerView.animate().alpha(1.0f);

        if (mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ObjectAnimator.ofFloat(mBinding.editProgressBar, View.ALPHA, 0.0f);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {

            private boolean mCanceled = false;

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mCanceled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!mCanceled) {
                    mBinding.editProgressBar.setVisibility(View.GONE);
                }
            }
        });
        mAnimator.start();
    }
}
