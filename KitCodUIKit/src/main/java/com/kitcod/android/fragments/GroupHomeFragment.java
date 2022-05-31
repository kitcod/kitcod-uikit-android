package com.kitcod.android.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kitcod.android.KitCodUIKit;
import com.kitcod.android.R;
import com.kitcod.android.activities.KcCreatePostActivity;
import com.kitcod.android.activities.KcGroupHomeActivity;
import com.kitcod.android.activities.KcSinglePostActivity;
import com.kitcod.android.activities.KcUserProfileActivity;
import com.kitcod.android.adapters.FeedsViewAdapter;
import com.kitcod.android.consts.StringSet;
import com.kitcod.android.databinding.GroupHomeFragmentBinding;
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
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GroupHomeFragment extends BaseFragment {
    FeedsViewAdapter adapter;
    private GroupHomeViewModel mViewModel;
    GroupHomeFragmentBinding binding;
    private String headerTitle = null, buttonTitle = null;
    private View.OnClickListener headerLeftButtonListener;
    private View.OnClickListener headerRightButtonListener;
    private KcGroup groupDetail;
    ArrayList<Post> postArrayList = new ArrayList<>();
    private static final int PERMISSION_REQUEST_STORAGE = 2006;
    private OnItemClickListener<Post> itemClickListener;
    RadioManager radioManager;
    private OnItemClickListener<String> userClickListener;
    private OnItemClickListener<KcGroup> feedGroupClickListener;
    private OnItemClickListener createPostListener;
    ArrayList<KcGroupsBulk> groupsBulksList;

    public static GroupHomeFragment newInstance() {
        return new GroupHomeFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        int themeResId = KitCodUIKit.getDefaultThemeMode().getResId();
        if (args != null) {
            themeResId = args.getInt(StringSet.KEY_THEME_RES_ID, KitCodUIKit.getDefaultThemeMode().getResId());
            groupDetail = new Gson().fromJson(getArguments().getString(Util.GROUP), KcGroup.class);
        }
        if (getActivity() != null) {
            getActivity().setTheme(themeResId);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.group_home_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeaderOnCreated();
        init();
        setAdapter();
        setData();
    }

    int position;

    private void setAdapter() {

        if (itemClickListener == null) {
            itemClickListener = (view, position, post) -> {
                Intent intent = new Intent(getActivity(), KcSinglePostActivity.class);
                intent.putExtra(Util.POST, new Gson().toJson(post));
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
        if (feedGroupClickListener == null) {
            feedGroupClickListener = (view, position, group) -> {
                Intent intent = new Intent(getActivity(), KcGroupHomeActivity.class);
                intent.putExtra(Util.GROUP, new Gson().toJson(group));
                startActivity(intent);
            };
        }

        adapter = new FeedsViewAdapter(getActivity(), postArrayList, new FeedsViewAdapter.FeedListener() {
            @Override
            public void onDocClick(String url) {

                checkPermission(PERMISSION_REQUEST_STORAGE, new IPermissionHandler() {
                    @Override
                    public String[] getPermissions(int requestCode) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                            return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                        }
                        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE};
                    }

                    @Override
                    public void onPermissionGranted(int requestCode) {
                        Intent intent = new Intent(getActivity(), BackgroundNotificationService.class);
                        intent.putExtra("TEST", url);
                        getActivity().startService(intent);
                    }
                });


            }

            @Override
            public void onAudPlayClick(int mposition) {
                position = mposition;
                radioManager.playOrPause(postArrayList.get(mposition).media.auds.get(0).mediaUrl);

            }

            @Override
            public void onLayoutListener(Post post) {
            }
        });
        adapter.setOnUserClickListener(userClickListener);
        adapter.setOnFeedGroupClickListener(feedGroupClickListener);
        adapter.setOnItemClickListener(itemClickListener);
        binding.createGroupView.getBinding().rvFeeds.setAdapter(adapter);
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

    private void setData() {
        FeedsView feedsView = new Gson().fromJson(loadJSONFromAsset(), FeedsView.class);
        if (feedsView.embedded.postList != null && feedsView.embedded.postList.size() > 0) {
            postArrayList.addAll(feedsView.embedded.postList);
            adapter.setData(postArrayList);
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

        if (groupDetail.media.covImg != null) {
            if (groupDetail.media.covImg.resizedMediaList != null && groupDetail.media.covImg.resizedMediaList.size() > 0) {
                for (int i = 0; i < groupDetail.media.covImg.resizedMediaList.size(); i++) {
                    if (groupDetail.media.covImg.resizedMediaList.get(i).type.equalsIgnoreCase("resized")) {
                        final String img = groupDetail.media.covImg.resizedMediaList.get(i).mediaUrl;
                        Picasso.get()
                                .load(img)
                                .into(binding.createGroupView.getBinding().ivCoverImage);
                    }
                }

            }
        }
        if (Util.validateString(groupDetail.name)) {
            binding.createGroupView.getBinding().tvGroupname.setText(groupDetail.name);
        }

        if (Util.validateString(groupDetail.description)) {
            binding.createGroupView.getBinding().tvAboutDesc.setText(groupDetail.description);
        }


    }

    private void initHeaderOnCreated() {
        Bundle args = getArguments();
        boolean useHeader = false;
        boolean useHeaderLeftButton = true;
        boolean useHeaderRightButton = true;
        int headerLeftButtonIconResId = R.drawable.icon_arrow_left;
        int headerRightButtonIconResId = R.drawable.icon_info;
        ColorStateList headerLeftButtonIconTint = null;
        ColorStateList headerRightButtonIconTint = null;
        if (args != null) {
            useHeader = args.getBoolean(StringSet.KEY_USE_HEADER, false);
            useHeaderLeftButton = args.getBoolean(StringSet.KEY_USE_HEADER_LEFT_BUTTON, true);
            useHeaderRightButton = args.getBoolean(StringSet.KEY_USE_HEADER_RIGHT_BUTTON, true);
            headerLeftButtonIconResId = args.getInt(StringSet.KEY_HEADER_LEFT_BUTTON_ICON_RES_ID, R.drawable.icon_arrow_left);
            headerRightButtonIconResId = args.getInt(StringSet.KEY_HEADER_RIGHT_BUTTON_ICON_RES_ID, R.drawable.icon_info);
            headerLeftButtonIconTint = args.getParcelable(StringSet.KEY_HEADER_LEFT_BUTTON_ICON_TINT);
            headerRightButtonIconTint = args.getParcelable(StringSet.KEY_HEADER_RIGHT_BUTTON_ICON_TINT);
            headerTitle = args.getString(StringSet.KEY_HEADER_TITLE, null);
            buttonTitle = args.getString(StringSet.KEY_BUTTON_TITLE, null);
        }
        if (buttonTitle != null) {
            binding.createGroupView.getButtonView().setText(buttonTitle);
        }
        if (createPostListener == null) {
            createPostListener = (view, position, group) -> {
                Intent intent = new Intent(getActivity(), KcCreatePostActivity.class);
                startActivity(intent);
            };
        }
        binding.createGroupView.getBinding().clCreatepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPostListener.onItemClick(view, 0, "");
            }
        });

        binding.chvChannelHeader.setVisibility(useHeader ? View.VISIBLE : View.GONE);

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
        private GroupHomeFragment customFragment;
        private View.OnClickListener headerLeftButtonListener;
        private View.OnClickListener headerRightButtonListener;
        private View.OnClickListener inputLeftButtonListener;
        private OnItemClickListener<Post> itemClickListener;
        private OnItemClickListener createPostListener;
        private OnItemClickListener<KcGroup> feedGroupClickListener;
        private OnItemClickListener<String> userClickListener;

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

        public Builder(String group) {
            bundle = new Bundle();
            bundle.putString(Util.GROUP, group);
        }


        /**
         * Sets the custom channel fragment. It must inherit {@link GroupHomeFragment}.
         *
         * @param fragment custom channel fragment.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @since 1.0.4
         */
        public <T extends GroupHomeFragment> GroupHomeFragment.Builder setCustomChannelFragment(T fragment) {
            this.customFragment = fragment;
            return this;
        }

        /**
         * Sets whether the header is used.
         *
         * @param useHeader <code>true</code> if the header is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupHomeFragment.Builder setUseHeader(boolean useHeader) {
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
        public GroupHomeFragment.Builder setUseHeaderRightButton(boolean useHeaderRightButton) {
            bundle.putBoolean(StringSet.KEY_USE_HEADER_RIGHT_BUTTON, useHeaderRightButton);
            return this;
        }

        /**
         * Sets whether the Groups View is used.
         *
         * @param useHeader <code>true</code> if the Groups View is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupHomeFragment.Builder setUseGroupsFeed(boolean useHeader) {
            bundle.putBoolean(StringSet.KEY_USE_GROUPS_FEEDS, useHeader);
            return this;
        }

        /**
         * Sets whether the Create Post View is used.
         *
         * @param useHeader <code>true</code> if the Create Post View is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupHomeFragment.Builder setUseCreatePostFeed(boolean useHeader) {
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
        public GroupHomeFragment.Builder setUseHeaderLeftButton(boolean useHeaderLeftButton) {
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
        public GroupHomeFragment.Builder setUseLastSeenAt(boolean useLastSeenAt) {
            return this;
        }


        /**
         * Sets the title of the header.
         *
         * @param title text to be displayed.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @since 2.1.1
         */
        public GroupHomeFragment.Builder setHeaderTitle(String title) {
            bundle.putString(StringSet.KEY_HEADER_TITLE, title);
            return this;
        }

        /**
         * Sets the icon on the left button of the header.
         *
         * @param resId the resource identifier of the drawable.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupHomeFragment.Builder setHeaderLeftButtonIconResId(@DrawableRes int resId) {
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
        public GroupHomeFragment.Builder setHeaderLeftButtonIcon(@DrawableRes int resId, @Nullable ColorStateList tint) {
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
        public GroupHomeFragment.Builder setHeaderRightButtonIconResId(@DrawableRes int resId) {
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
        public GroupHomeFragment.Builder setHeaderRightButtonIcon(@DrawableRes int resId, @Nullable ColorStateList tint) {
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
        public GroupHomeFragment.Builder setUseInputLeftButton(boolean useInputLeftButton) {
            bundle.putBoolean(StringSet.KEY_USE_INPUT_LEFT_BUTTON, useInputLeftButton);
            return this;
        }

        public GroupHomeFragment.Builder setItemClickListener(OnItemClickListener<Post> itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }

        public GroupHomeFragment.Builder setButtonTitle(String title) {
            bundle.putString(StringSet.KEY_BUTTON_TITLE, title);
            return this;
        }


        /**
         * Sets the click listener on the left button of the header.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupHomeFragment.Builder setHeaderLeftButtonListener(View.OnClickListener listener) {
            this.headerLeftButtonListener = listener;
            return this;
        }

        /**
         * Sets the click listener on the right button of the header.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupHomeFragment.Builder setHeaderRightButtonListener(View.OnClickListener listener) {
            this.headerRightButtonListener = listener;
            return this;
        }


        /**
         * Sets the click listener on the left button of the input.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupHomeFragment.Builder setInputLeftButtonListener(View.OnClickListener listener) {
            this.inputLeftButtonListener = listener;
            return this;
        }

        public GroupHomeFragment.Builder setUserClickListener(OnItemClickListener<String> userClickListener) {
            this.userClickListener = userClickListener;
            return this;
        }

        public GroupHomeFragment.Builder setFeedGroupClickListener(OnItemClickListener<KcGroup> feedGroupClickListener) {
            this.feedGroupClickListener = feedGroupClickListener;
            return this;
        }

        public GroupHomeFragment.Builder setCreatePostItemClickListener(OnItemClickListener createPostListener) {
            this.createPostListener = createPostListener;
            return this;
        }

        /**
         * Creates an {@link GroupHomeFragment} with the arguments supplied to this
         * builder.
         *
         * @return The {@link GroupHomeFragment} applied to the {@link Bundle}.
         */
        public GroupHomeFragment build() {
            GroupHomeFragment fragment = customFragment != null ? customFragment : new GroupHomeFragment();
            fragment.setArguments(bundle);
            fragment.setHeaderLeftButtonListener(headerLeftButtonListener);
            fragment.setHeaderRightButtonListener(headerRightButtonListener);
            fragment.setItemClickListener(itemClickListener);
            fragment.setFeedGroupClickListener(feedGroupClickListener);
            fragment.setUserClickListener(userClickListener);
            fragment.setCreatePostItemClickListener(createPostListener);
            return fragment;
        }
    }

    private void setCreatePostItemClickListener(OnItemClickListener createPostListener) {
        this.createPostListener = createPostListener;
    }

    private void setFeedGroupClickListener(OnItemClickListener<KcGroup> feedGroupClickListener) {
        this.feedGroupClickListener = feedGroupClickListener;
    }

    private void setUserClickListener(OnItemClickListener<String> userClickListener) {
        this.userClickListener = userClickListener;
    }

    private void setItemClickListener(OnItemClickListener<Post> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    private void setHeaderLeftButtonListener(View.OnClickListener listener) {
        this.headerLeftButtonListener = listener;
    }

    private void setHeaderRightButtonListener(View.OnClickListener listener) {
        this.headerRightButtonListener = listener;
    }


/*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GroupHomeViewModel.class);
        // TODO: Use the ViewModel
    }
*/

}