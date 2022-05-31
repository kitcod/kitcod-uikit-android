package com.kitcod.android.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.kitcod.android.KitCodUIKit;
import com.kitcod.android.R;
import com.kitcod.android.activities.KcGroupHomeActivity;
import com.kitcod.android.adapters.GroupListAdapter;
import com.kitcod.android.consts.StringSet;
import com.kitcod.android.databinding.GroupsListFragmentBinding;
import com.kitcod.android.interfaces.OnItemClickListener;
import com.kitcod.android.model.FeedsView;
import com.kitcod.android.model.KcGroup;
import com.kitcod.android.utils.Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GroupListFragment extends BaseFragment {
    GroupsListFragmentBinding binding;
    private String headerTitle = null;
    private View.OnClickListener headerLeftButtonListener;
    private View.OnClickListener headerRightButtonListener;
    ArrayList<KcGroup> groupArrayList = new ArrayList<>();
    GroupListAdapter adapter;
    private OnItemClickListener<KcGroup> itemClickListener;

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
        binding = DataBindingUtil.inflate(inflater, R.layout.groups_list_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeaderOnCreated();
        setAdapter();
        setData();
    }

    private void setAdapter() {

        if (itemClickListener == null) {
            itemClickListener = (view, position, post) -> {
                Intent intent = new Intent(getActivity(), KcGroupHomeActivity.class);
                intent.putExtra(Util.GROUP, new Gson().toJson(post));
                startActivity(intent);
            };
        }

        adapter = new GroupListAdapter(getActivity(), groupArrayList, new GroupListAdapter.GroupListener() {
            @Override
            public void onJoinClick(int position) {

            }
        });
        adapter.setOnItemClickListener(itemClickListener);
        binding.rvFeeds.setAdapter(adapter);
    }

    private void setData() {
        FeedsView feedsView = new Gson().fromJson(loadJSONFromAsset(), FeedsView.class);
        if (feedsView.embedded.groupList != null && feedsView.embedded.groupList.size() > 0) {
            groupArrayList.addAll(feedsView.embedded.groupList);
            adapter.notifyDataSetChanged();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("travelgroups.json");
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
        }
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
        private GroupListFragment customFragment;
        private View.OnClickListener headerLeftButtonListener;
        private View.OnClickListener headerRightButtonListener;
        private View.OnClickListener contentListener;
        private View.OnClickListener inputLeftButtonListener;
        private OnItemClickListener<KcGroup> itemClickListener;

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
         * Sets the custom channel fragment. It must inherit {@link GroupListFragment}.
         *
         * @param fragment custom channel fragment.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @since 1.0.4
         */
        public <T extends GroupListFragment> GroupListFragment.Builder setCustomChannelFragment(T fragment) {
            this.customFragment = fragment;
            return this;
        }

        /**
         * Sets whether the header is used.
         *
         * @param useHeader <code>true</code> if the header is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupListFragment.Builder setUseHeader(boolean useHeader) {
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
        public GroupListFragment.Builder setUseHeaderRightButton(boolean useHeaderRightButton) {
            bundle.putBoolean(StringSet.KEY_USE_HEADER_RIGHT_BUTTON, useHeaderRightButton);
            return this;
        }

        /**
         * Sets whether the Groups View is used.
         *
         * @param useHeader <code>true</code> if the Groups View is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */

        /**
         * Sets whether the Create Post View is used.
         *
         * @param useHeader <code>true</code> if the Create Post View is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */

        /**
         * Sets whether the left button of the header is used.
         *
         * @param useHeaderLeftButton <code>true</code> if the left button of the header is used,
         *                            <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupListFragment.Builder setUseHeaderLeftButton(boolean useHeaderLeftButton) {
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
        public GroupListFragment.Builder setUseLastSeenAt(boolean useLastSeenAt) {
            return this;
        }


        /**
         * Sets the title of the header.
         *
         * @param title text to be displayed.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @since 2.1.1
         */
        public GroupListFragment.Builder setHeaderTitle(String title) {
            bundle.putString(StringSet.KEY_HEADER_TITLE, title);
            return this;
        }

        /**
         * Sets the icon on the left button of the header.
         *
         * @param resId the resource identifier of the drawable.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupListFragment.Builder setHeaderLeftButtonIconResId(@DrawableRes int resId) {
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
        public GroupListFragment.Builder setHeaderLeftButtonIcon(@DrawableRes int resId, @Nullable ColorStateList tint) {
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
        public GroupListFragment.Builder setHeaderRightButtonIconResId(@DrawableRes int resId) {
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
        public GroupListFragment.Builder setHeaderRightButtonIcon(@DrawableRes int resId, @Nullable ColorStateList tint) {
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
        public GroupListFragment.Builder setUseInputLeftButton(boolean useInputLeftButton) {
            bundle.putBoolean(StringSet.KEY_USE_INPUT_LEFT_BUTTON, useInputLeftButton);
            return this;
        }

        public GroupListFragment.Builder setButtonTitle(String title) {
            bundle.putString(StringSet.KEY_BUTTON_TITLE, title);
            return this;
        }


        /**
         * Sets the click listener on the left button of the header.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupListFragment.Builder setHeaderLeftButtonListener(View.OnClickListener listener) {
            this.headerLeftButtonListener = listener;
            return this;
        }


        /**
         * Sets the click listener on the right button of the header.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupListFragment.Builder setHeaderRightButtonListener(View.OnClickListener listener) {
            this.headerRightButtonListener = listener;
            return this;
        }

        public GroupListFragment.Builder setItemClickListener(OnItemClickListener<KcGroup> itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }


        /**
         * Sets the click listener on the left button of the input.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupListFragment.Builder setInputLeftButtonListener(View.OnClickListener listener) {
            this.inputLeftButtonListener = listener;
            return this;
        }

        /**
         * Creates an {@link GroupListFragment} with the arguments supplied to this
         * builder.
         *
         * @return The {@link GroupListFragment} applied to the {@link Bundle}.
         */
        public GroupListFragment build() {
            GroupListFragment fragment = customFragment != null ? customFragment : new GroupListFragment();
            fragment.setArguments(bundle);
            fragment.setHeaderLeftButtonListener(headerLeftButtonListener);
            fragment.setHeaderRightButtonListener(headerRightButtonListener);
            fragment.setItemClickListener(itemClickListener);
            return fragment;
        }

    }

    private void setItemClickListener(OnItemClickListener<KcGroup> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    private void setHeaderLeftButtonListener(View.OnClickListener listener) {
        this.headerLeftButtonListener = listener;
    }

    private void setHeaderRightButtonListener(View.OnClickListener listener) {
        this.headerRightButtonListener = listener;
    }


}
