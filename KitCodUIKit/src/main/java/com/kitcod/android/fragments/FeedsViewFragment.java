package com.kitcod.android.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kitcod.android.KitCodUIKit;
import com.kitcod.android.R;
import com.kitcod.android.activities.KcCreateGroupActivity;
import com.kitcod.android.activities.KcCreatePostActivity;
import com.kitcod.android.activities.KcGroupHomeActivity;
import com.kitcod.android.activities.KcSinglePostActivity;
import com.kitcod.android.activities.KcUserProfileActivity;
import com.kitcod.android.adapters.FeedsViewAdapter;
import com.kitcod.android.adapters.GroupListFeedAdapter;
import com.kitcod.android.consts.StringSet;
import com.kitcod.android.databinding.FeedsViewFragmentBinding;
import com.kitcod.android.interfaces.OnItemClickListener;
import com.kitcod.android.model.ExoplayerModel;
import com.kitcod.android.model.FeedsView;
import com.kitcod.android.model.KcGroup;
import com.kitcod.android.model.KcGroupsBulk;
import com.kitcod.android.model.Post;
import com.kitcod.android.player.PlaybackStatus;
import com.kitcod.android.player.RadioManager;
import com.kitcod.android.services.BackgroundNotificationService;
import com.kitcod.android.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FeedsViewFragment extends BaseFragment {

    private FeedsViewViewModel mViewModel;
    private FeedsViewFragmentBinding binding;
    private String headerTitle = null;
    FeedsViewAdapter adapter;
    GroupListFeedAdapter adapter1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    ArrayList<KcGroup> groupArrayList = new ArrayList<>();

    public static FeedsViewFragment newInstance() {
        return new FeedsViewFragment();
    }

    RadioManager radioManager;
    private View.OnClickListener headerLeftButtonListener;
    private View.OnClickListener headerRightButtonListener;
    private OnItemClickListener createGroupListener;
    ArrayList<Post> postArrayList = new ArrayList<>();
    ArrayList<Post> newPostArrayList = new ArrayList<>();
    private OnItemClickListener<Post> itemClickListener;
    private OnItemClickListener<String> userClickListener;
    private OnItemClickListener<KcGroup> feedGroupClickListener;
    private OnItemClickListener createPostListener;
    ArrayList<KcGroupsBulk> groupsBulksList;
    private OnItemClickListener<KcGroup> groupOnItemClickListener;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        int themeResId = KitCodUIKit.getDefaultThemeMode().getResId();
        if (args != null) {
            themeResId = args.getInt(StringSet.KEY_THEME_RES_ID, KitCodUIKit.getDefaultThemeMode().getResId());
        }
        if (getActivity() != null) {
            getActivity().setTheme(themeResId);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.feeds_view_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeaderOnCreated();
        init();
        setAdapter();
        setData();
    }

    private void init() {
        radioManager = RadioManager.with(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        radioManager.bind();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private void setData() {
        FeedsView feedsView = new Gson().fromJson(loadJSONFromAsset(), FeedsView.class);
        if (feedsView.embedded.postList != null && feedsView.embedded.postList.size() > 0) {
            postArrayList.addAll(feedsView.embedded.postList);

        }

        groupsBulksList = new Gson().fromJson(Util.loadJSONFromAsset("communitybulkresponse.json", getActivity()), new TypeToken<List<KcGroupsBulk>>() {
        }.getType());
        for (int i = 0; i < postArrayList.size(); i++) {
            for (int j = 0; j < groupsBulksList.size(); j++) {
                if (postArrayList.get(i).communityId.equalsIgnoreCase(groupsBulksList.get(j).id)) {
                    postArrayList.get(i).group = groupsBulksList.get(j).body;
                }
            }
        }
        adapter.notifyDataSetChanged();

        FeedsView feedsView1 = new Gson().fromJson(Util.loadJSONFromAsset("travelgroups.json", getActivity()), FeedsView.class);
        if (feedsView1.embedded.groupList != null && feedsView1.embedded.groupList.size() > 0) {
            groupArrayList.addAll(feedsView1.embedded.groupList);
            KcGroup kcGroup = new KcGroup();
            kcGroup.name = "Create new group";
            groupArrayList.add(0, kcGroup);
            adapter1.notifyDataSetChanged();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("TravelBlog.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    int position;

    private void setAdapter() {
        if (groupOnItemClickListener == null) {
            groupOnItemClickListener = (view, position, post) -> {
                Intent intent = new Intent(getActivity(), KcGroupHomeActivity.class);
                intent.putExtra(Util.GROUP, new Gson().toJson(post));
                startActivity(intent);
            };
        }

        if (itemClickListener == null) {
            itemClickListener = (view, position, post) -> {
                Intent intent = new Intent(getActivity(), KcSinglePostActivity.class);
                intent.putExtra(Util.POST, new Gson().toJson(post));
                startActivity(intent);
            };
        }

        if (feedGroupClickListener == null) {
            feedGroupClickListener = (view, position, group) -> {
                Intent intent = new Intent(getActivity(), KcGroupHomeActivity.class);
                intent.putExtra(Util.GROUP, new Gson().toJson(group));
                startActivity(intent);
            };
        }

        if (userClickListener == null) {
            userClickListener = (view, position, group) -> {
                Intent intent = new Intent(getActivity(), KcUserProfileActivity.class);
                intent.putExtra(Util.USERID, group);
                startActivity(intent);
            };

        }

        if (createGroupListener == null) {
            createGroupListener = (view, position, post) -> {
                Intent intent = new Intent(getActivity(), KcCreateGroupActivity.class);
                startActivity(intent);
            };

        }

        adapter1 = new GroupListFeedAdapter(getActivity(), groupArrayList);
        adapter1.setOnItemClickListener(groupOnItemClickListener);
        adapter1.setOnCreateGroupListener(createGroupListener);
//        binding.feedsView.getGroupsView().setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.feedsView.getGroupsView().setAdapter(adapter1);

        adapter = new FeedsViewAdapter(getActivity(), postArrayList, new FeedsViewAdapter.FeedListener() {
            @Override
            public void onDocClick(String url) {
                if (checkPermission()) {
                    Intent intent = new Intent(getActivity(), BackgroundNotificationService.class);
                    intent.putExtra("TEST", url);
                    getActivity().startService(intent);
                } else {
                    requestPermission();
                }

            }

            @Override
            public void onAudPlayClick(int mposition) {
                position = mposition;
                radioManager.playOrPause(postArrayList.get(mposition).media.auds.get(0).mediaUrl);

            }

            @Override
            public void onLayoutListener(Post post) {


               /* GroupPostFragment fragment = new GroupPostFragment();
                fragment.setArguments(bundle);
                new Handler().post(new Runnable() {
                    public void run() {
                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.sb_fragment_container, fragment).commit();
                    }
                });*/
            }
        });
        adapter.setOnUserClickListener(userClickListener);
        adapter.setOnItemClickListener(itemClickListener);
        adapter.setOnFeedGroupClickListener(feedGroupClickListener);
        binding.feedsView.getFeedsView().setAdapter(adapter);
    }

    @Subscribe
    public void onEvent(ExoplayerModel exoplayerModel) {
        if (exoplayerModel.getExoPlayer().getDuration() > 0) {
            postArrayList.get(position).setExoplayerModel(exoplayerModel);
            adapter.notifyItemChanged(position);
        }
        switch (exoplayerModel.status) {

            case PlaybackStatus.LOADING:

                // loading

                break;

            case PlaybackStatus.ERROR:

//                Toast.makeText(this, R.string.no_stream, Toast.LENGTH_SHORT).show();

                break;
            case PlaybackStatus.PLAYING:
                ArrayList<Post> newPostArrayList = new ArrayList<>();
                for (int i = 0; i < postArrayList.size(); i++) {
                    newPostArrayList.add(postArrayList.get(i).clone());
                }

                newPostArrayList.get(position).isPlaying = true;
                Toast.makeText(getActivity(), "PLAYING", Toast.LENGTH_SHORT).show();
                adapter.setData(newPostArrayList);
                break;
            case PlaybackStatus.PAUSED:
                ArrayList<Post> newPostArrayList1 = new ArrayList<>();
                for (int i = 0; i < postArrayList.size(); i++) {
                    newPostArrayList1.add(postArrayList.get(i).clone());
                }

                newPostArrayList1.get(position).isPlaying = false;
//                newPostArrayList.get(position).media.auds.get(0).isPlaying = false;
//                Toast.makeText(getActivity(), "PAUSED", Toast.LENGTH_SHORT).show();
                adapter.setData(newPostArrayList1);
                break;
            case PlaybackStatus.STOPPED:
                Toast.makeText(getActivity(), "STOPPED", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void initHeaderOnCreated() {
        Bundle args = getArguments();
        boolean useHeader = false;
        boolean useHeaderLeftButton = true;
        boolean useHeaderRightButton = true;
        boolean useGroupsView = true;
        boolean useCreatePostView = true;
        int headerLeftButtonIconResId = R.drawable.icon_arrow_left;
        int headerRightButtonIconResId = R.drawable.icon_info;
        ColorStateList headerLeftButtonIconTint = null;
        ColorStateList headerRightButtonIconTint = null;
        if (args != null) {
            useGroupsView = args.getBoolean(StringSet.KEY_USE_GROUPS_FEEDS, true);
            useCreatePostView = args.getBoolean(StringSet.KEY_USE_CREATEPOST_FEEDS, true);
            useHeader = args.getBoolean(StringSet.KEY_USE_HEADER, false);
            useHeaderLeftButton = args.getBoolean(StringSet.KEY_USE_HEADER_LEFT_BUTTON, true);
            useHeaderRightButton = args.getBoolean(StringSet.KEY_USE_HEADER_RIGHT_BUTTON, true);
            headerLeftButtonIconResId = args.getInt(StringSet.KEY_HEADER_LEFT_BUTTON_ICON_RES_ID, R.drawable.icon_arrow_left);
            headerRightButtonIconResId = args.getInt(StringSet.KEY_HEADER_RIGHT_BUTTON_ICON_RES_ID, R.drawable.icon_info);
            headerLeftButtonIconTint = args.getParcelable(StringSet.KEY_HEADER_LEFT_BUTTON_ICON_TINT);
            headerRightButtonIconTint = args.getParcelable(StringSet.KEY_HEADER_RIGHT_BUTTON_ICON_TINT);
            headerTitle = args.getString(StringSet.KEY_HEADER_TITLE, null);
        }
        binding.chvChannelHeader.setVisibility(useHeader ? View.VISIBLE : View.GONE);
        binding.feedsView.getCreatePostView().setVisibility(useCreatePostView ? View.VISIBLE : View.GONE);
        binding.feedsView.getGroupsView().setVisibility(useGroupsView ? View.VISIBLE : View.GONE);
        if (createPostListener == null) {
            createPostListener = (view, position, group) -> {
                Intent intent = new Intent(getActivity(), KcCreatePostActivity.class);
                startActivity(intent);
            };
        }
        binding.feedsView.getCreatePostView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPostListener.onItemClick(view, 0, "");
            }
        });
        binding.chvChannelHeader.setUseLeftImageButton(useHeaderLeftButton);
        binding.chvChannelHeader.setUseRightButton(useHeaderRightButton);
        if (headerTitle != null) {
            binding.chvChannelHeader.getTitleTextView().setText(headerTitle);
        }
        binding.chvChannelHeader.setLeftImageButtonResource(headerLeftButtonIconResId);
        if (args != null && args.containsKey(StringSet.KEY_HEADER_LEFT_BUTTON_ICON_RES_ID)) {
            binding.chvChannelHeader.setLeftImageButtonTint(headerLeftButtonIconTint);
        }
        binding.chvChannelHeader.setRightImageButtonResource(headerRightButtonIconResId);
        if (args != null && args.containsKey(StringSet.KEY_HEADER_RIGHT_BUTTON_ICON_RES_ID)) {
            binding.chvChannelHeader.setRightImageButtonTint(headerRightButtonIconTint);
        }
        binding.chvChannelHeader.setLeftImageButtonClickListener(v -> finish());
    }

    public static class Builder {
        private final Bundle bundle;
        private FeedsViewFragment customFragment;
        private View.OnClickListener headerLeftButtonListener;
        private View.OnClickListener headerRightButtonListener;
        private View.OnClickListener contentListener;
        private View.OnClickListener inputLeftButtonListener;
        private OnItemClickListener<Post> itemClickListener;
        private OnItemClickListener<KcGroup> feedGroupClickListener;
        private OnItemClickListener<String> userClickListener;
        OnItemClickListener<KcGroup> groupOnItemClickListener;
        private OnItemClickListener createPostListener;
        private OnItemClickListener createGroupListener;

        /**
         * Constructor
         */
        public Builder() {
            bundle = new Bundle();
        }

        public Builder(@StyleRes int customThemeResId) {
            bundle = new Bundle();
            bundle.putInt(StringSet.KEY_THEME_RES_ID, customThemeResId);
        }

        /**
         * Sets the custom channel fragment. It must inherit {@link FeedsViewFragment}.
         *
         * @param fragment custom channel fragment.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @since 1.0.4
         */
        public <T extends FeedsViewFragment> FeedsViewFragment.Builder setCustomChannelFragment(T fragment) {
            this.customFragment = fragment;
            return this;
        }

        /**
         * Sets whether the header is used.
         *
         * @param useHeader <code>true</code> if the header is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public FeedsViewFragment.Builder setUseHeader(boolean useHeader) {
            bundle.putBoolean(StringSet.KEY_USE_HEADER, useHeader);
            return this;
        }

        /**
         * Sets whether the right button of the header is used.
         *
         * @param useHeaderRightButton <code>true</code> if the right button of the header is used,
         *                             <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public FeedsViewFragment.Builder setUseHeaderRightButton(boolean useHeaderRightButton) {
            bundle.putBoolean(StringSet.KEY_USE_HEADER_RIGHT_BUTTON, useHeaderRightButton);
            return this;
        }

        /**
         * Sets whether the Groups View is used.
         *
         * @param useHeader <code>true</code> if the Groups View is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public FeedsViewFragment.Builder setUseGroupsFeed(boolean useHeader) {
            bundle.putBoolean(StringSet.KEY_USE_GROUPS_FEEDS, useHeader);
            return this;
        }

        /**
         * Sets whether the Create Post View is used.
         *
         * @param useHeader <code>true</code> if the Create Post View is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public FeedsViewFragment.Builder setUseCreatePostFeed(boolean useHeader) {
            bundle.putBoolean(StringSet.KEY_USE_CREATEPOST_FEEDS, useHeader);
            return this;
        }

        /**
         * Sets whether the left button of the header is used.
         *
         * @param useHeaderLeftButton <code>true</code> if the left button of the header is used,
         *                            <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public FeedsViewFragment.Builder setUseHeaderLeftButton(boolean useHeaderLeftButton) {
            bundle.putBoolean(StringSet.KEY_USE_HEADER_LEFT_BUTTON, useHeaderLeftButton);
            return this;
        }

        /**
         * Sets whether the marker of last seen at is used.
         *
         * @param useLastSeenAt <code>true</code> if the marker of last seen at is used,
         *                      <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @Deprecated Deprecate last seen at feature.
         */
        @Deprecated
        public FeedsViewFragment.Builder setUseLastSeenAt(boolean useLastSeenAt) {
            return this;
        }


        /**
         * Sets the title of the header.
         *
         * @param title text to be displayed.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @since 2.1.1
         */
        public FeedsViewFragment.Builder setHeaderTitle(String title) {
            bundle.putString(StringSet.KEY_HEADER_TITLE, title);
            return this;
        }

        /**
         * Sets the icon on the left button of the header.
         *
         * @param resId the resource identifier of the drawable.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public FeedsViewFragment.Builder setHeaderLeftButtonIconResId(@DrawableRes int resId) {
            return setHeaderLeftButtonIcon(resId, null);
        }

        /**
         * Sets the icon on the left button of the header.
         *
         * @param resId the resource identifier of the drawable.
         * @param tint  Color state list to use for tinting this resource, or null to clear the tint.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @since 2.1.0
         */
        public FeedsViewFragment.Builder setHeaderLeftButtonIcon(@DrawableRes int resId, @Nullable ColorStateList tint) {
            bundle.putInt(StringSet.KEY_HEADER_LEFT_BUTTON_ICON_RES_ID, resId);
            bundle.putParcelable(StringSet.KEY_HEADER_LEFT_BUTTON_ICON_TINT, tint);
            return this;
        }

        /**
         * Sets the icon on the right button of the header.
         *
         * @param resId the resource identifier of the drawable.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public FeedsViewFragment.Builder setHeaderRightButtonIconResId(@DrawableRes int resId) {
            return setHeaderRightButtonIcon(resId, null);
        }

        /**
         * Sets the icon on the right button of the header.
         *
         * @param resId the resource identifier of the drawable.
         * @param tint  Color state list to use for tinting this resource, or null to clear the tint.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @since 2.1.0
         */
        public FeedsViewFragment.Builder setHeaderRightButtonIcon(@DrawableRes int resId, @Nullable ColorStateList tint) {
            bundle.putInt(StringSet.KEY_HEADER_RIGHT_BUTTON_ICON_RES_ID, resId);
            bundle.putParcelable(StringSet.KEY_HEADER_RIGHT_BUTTON_ICON_TINT, tint);
            return this;
        }

        /**
         * Sets whether the left button of the input is used.
         *
         * @param useInputLeftButton <code>true</code> if the left button of the input is used,
         *                           <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @since 2.0.1
         */
        public FeedsViewFragment.Builder setUseInputLeftButton(boolean useInputLeftButton) {
            bundle.putBoolean(StringSet.KEY_USE_INPUT_LEFT_BUTTON, useInputLeftButton);
            return this;
        }

        public FeedsViewFragment.Builder setButtonTitle(String title) {
            bundle.putString(StringSet.KEY_BUTTON_TITLE, title);
            return this;
        }


        /**
         * Sets the click listener on the left button of the header.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public FeedsViewFragment.Builder setHeaderLeftButtonListener(View.OnClickListener listener) {
            this.headerLeftButtonListener = listener;
            return this;
        }


        /**
         * Sets the click listener on the right button of the header.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public FeedsViewFragment.Builder setHeaderRightButtonListener(View.OnClickListener listener) {
            this.headerRightButtonListener = listener;
            return this;
        }

        public FeedsViewFragment.Builder setCreateGroupListener(OnItemClickListener createGroupListener) {
            this.createGroupListener = createGroupListener;
            return this;
        }


        public Builder setItemClickListener(OnItemClickListener<Post> itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }

        public Builder setUserClickListener(OnItemClickListener<String> userClickListener) {
            this.userClickListener = userClickListener;
            return this;
        }

        public Builder setFeedGroupClickListener(OnItemClickListener<KcGroup> feedGroupClickListener) {
            this.feedGroupClickListener = feedGroupClickListener;
            return this;
        }

        public Builder setGroupOnItemClickListener(OnItemClickListener<KcGroup> groupOnItemClickListener) {
            this.groupOnItemClickListener = groupOnItemClickListener;
            return this;
        }

        public Builder setCreatePostItemClickListener(OnItemClickListener createPostListener) {
            this.createPostListener = createPostListener;
            return this;
        }


        /**
         * Sets the click listener on the left button of the input.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public FeedsViewFragment.Builder setInputLeftButtonListener(View.OnClickListener listener) {
            this.inputLeftButtonListener = listener;
            return this;
        }

        /**
         * Creates an {@link FeedsViewFragment} with the arguments supplied to this
         * builder.
         *
         * @return The {@link FeedsViewFragment} applied to the {@link Bundle}.
         */
        public FeedsViewFragment build() {
            FeedsViewFragment fragment = customFragment != null ? customFragment : new FeedsViewFragment();
            fragment.setArguments(bundle);
            fragment.setHeaderLeftButtonListener(headerLeftButtonListener);
            fragment.setHeaderRightButtonListener(headerRightButtonListener);
            fragment.setItemClickListener(itemClickListener);
            fragment.setFeedGroupClickListener(feedGroupClickListener);
            fragment.setUserClickListener(userClickListener);
            fragment.setGroupOnItemClickListener(groupOnItemClickListener);
            fragment.setCreatePostItemClickListener(createPostListener);
            fragment.setCreateGroupListener(createGroupListener);
            return fragment;
        }

    }

    private void setCreateGroupListener(OnItemClickListener createGroupListener) {
        this.createGroupListener = createGroupListener;
    }

    private void setCreatePostItemClickListener(OnItemClickListener createPostListener) {
        this.createPostListener = createPostListener;
    }


    private void setGroupOnItemClickListener(OnItemClickListener<KcGroup> groupOnItemClickListener) {
        this.groupOnItemClickListener = groupOnItemClickListener;
    }


    private void setFeedGroupClickListener(OnItemClickListener<KcGroup> feedGroupClickListener) {
        this.feedGroupClickListener = feedGroupClickListener;
    }


    private void setItemClickListener(OnItemClickListener<Post> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private void setUserClickListener(OnItemClickListener<String> userClickListener) {
        this.userClickListener = userClickListener;
    }


    private void setHeaderLeftButtonListener(View.OnClickListener listener) {
        this.headerLeftButtonListener = listener;
    }

    private void setHeaderRightButtonListener(View.OnClickListener listener) {
        this.headerRightButtonListener = listener;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//                    startImageDownload();
                } else {
                }
                break;
        }
    }


}