package com.kitcod.android.fragments;

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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kitcod.android.KitCodUIKit;
import com.kitcod.android.R;
import com.kitcod.android.consts.StringSet;
import com.kitcod.android.databinding.CreatePostFragmentBinding;
import com.kitcod.android.widgets.MoreOptionDialogView;
import com.kitcod.android.widgets.PostTypeDialogView;

public class CreatePostFragment extends BaseFragment {

    private CreatePostViewModel mViewModel;
    CreatePostFragmentBinding binding;
    private String headerTitle = null, buttonTitle = null;
    private View.OnClickListener headerLeftButtonListener;
    private View.OnClickListener headerRightButtonListener, postbuttonListener;
    int themeResId;
    boolean isAudio = true, isVideo = true, isImage = true, isCTA = true;

    public static CreatePostFragment newInstance() {
        return new CreatePostFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        themeResId = KitCodUIKit.getDefaultThemeMode().getResId();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.create_post_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeaderOnCreated();
    }

    private void initHeaderOnCreated() {
        Bundle args = getArguments();
        boolean useHeader = false;
        boolean useHeaderLeftButton = true;
        boolean useMoreOption = true;
        boolean useHeaderRightButton = true;
        int headerLeftButtonIconResId = R.drawable.icon_arrow_left;
        int headerRightButtonIconResId = R.drawable.icon_info;
        ColorStateList headerLeftButtonIconTint = null;
        ColorStateList headerRightButtonIconTint = null;
        if (args != null) {
            useMoreOption = args.getBoolean(StringSet.KEY_MORE_OPTIONS_CREATEPOST, true);
            useHeader = args.getBoolean(StringSet.KEY_USE_HEADER, false);
            useHeaderLeftButton = args.getBoolean(StringSet.KEY_USE_HEADER_LEFT_BUTTON, true);
            useHeaderRightButton = args.getBoolean(StringSet.KEY_USE_HEADER_RIGHT_BUTTON, true);
            headerLeftButtonIconResId = args.getInt(StringSet.KEY_HEADER_LEFT_BUTTON_ICON_RES_ID, R.drawable.icon_arrow_left);
            headerRightButtonIconResId = args.getInt(StringSet.KEY_HEADER_RIGHT_BUTTON_ICON_RES_ID, R.drawable.icon_info);
            headerLeftButtonIconTint = args.getParcelable(StringSet.KEY_HEADER_LEFT_BUTTON_ICON_TINT);
            headerRightButtonIconTint = args.getParcelable(StringSet.KEY_HEADER_RIGHT_BUTTON_ICON_TINT);
            headerTitle = args.getString(StringSet.KEY_HEADER_TITLE, null);
            buttonTitle = args.getString(StringSet.KEY_BUTTON_TITLE, null);
            isAudio = args.getBoolean(StringSet.KEY_AUDIO_CREATEPOST, true);
            isImage = args.getBoolean(StringSet.KEY_IMAGE_CREATEPOST, true);
            isVideo = args.getBoolean(StringSet.KEY_VIDEO_CREATEPOST, true);
            isCTA = args.getBoolean(StringSet.KEY_CTA_CREATEPOST, true);
        }
        if (buttonTitle != null) {
            binding.createPostView.getPostButton().getTitleTextView().setText(buttonTitle);
        }
        binding.createPostView.getMoreOption().setVisibility(useMoreOption ? View.VISIBLE : View.GONE);
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
        binding.createPostView.getPostSelectedType().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        binding.createPostView.getMoreOption().setOnClickListener(view -> {
            openMoreOptionsDialog();
        });
        if (postbuttonListener != null) {
            binding.createPostView.getPostButton().setPostButtonClickListener(postbuttonListener);
        }

    }

    private void openMoreOptionsDialog() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.post_type_bottom_dialog, null);
        mBottomSheetDialog.setContentView(sheetView);
        PostTypeDialogView postTypeDialogView = sheetView.findViewById(R.id.sbContentViewPanel);
        MoreOptionDialogView moreOptionDialogView = sheetView.findViewById(R.id.more_options_layout);
        moreOptionDialogView.setVisibility(View.VISIBLE);
        postTypeDialogView.setVisibility(View.GONE);
        moreOptionDialogView.getImageLayout().setVisibility(isImage ? View.VISIBLE : View.GONE);
        moreOptionDialogView.getAudioLayout().setVisibility(isAudio ? View.VISIBLE : View.GONE);
        moreOptionDialogView.getVideoLayout().setVisibility(isVideo ? View.VISIBLE : View.GONE);
        moreOptionDialogView.getCTALayout().setVisibility(isCTA ? View.VISIBLE : View.GONE);

        mBottomSheetDialog.show();
    }

    private void openDialog() {
      /*  KitCodDialogFragment bottomSheet = new KitCodDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(StringSet.KEY_THEME_RES_ID,themeResId);
        bottomSheet.show(getActivity().getSupportFragmentManager(),
                "ModalBottomSheet");*/
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.post_type_bottom_dialog, null);
        mBottomSheetDialog.setContentView(sheetView);
        PostTypeDialogView postTypeDialogView = sheetView.findViewById(R.id.sbContentViewPanel);
        MoreOptionDialogView moreOptionDialogView = sheetView.findViewById(R.id.more_options_layout);
        moreOptionDialogView.setVisibility(View.GONE);
        postTypeDialogView.setVisibility(View.VISIBLE);
        postTypeDialogView.getAnnouncementType().setOnClickListener(view -> {
            binding.createPostView.getSelectedType().setText(postTypeDialogView.getAnnouncementTitle().getText());
            binding.createPostView.getSelectedDesc().setText(postTypeDialogView.getAnnouncementDesc().getText());
            binding.createPostView.getSelectedIcon().setImageDrawable(getResources().getDrawable(R.drawable.ic_announcement_icon));
            mBottomSheetDialog.dismiss();
        });

        postTypeDialogView.getPostType().setOnClickListener(view -> {
            binding.createPostView.getSelectedType().setText(postTypeDialogView.getPostTitle().getText());
            binding.createPostView.getSelectedDesc().setText(postTypeDialogView.getPostDesc().getText());
            binding.createPostView.getSelectedIcon().setImageDrawable(getResources().getDrawable(R.drawable.ic_post_icon_bg));
            mBottomSheetDialog.dismiss();
        });

        postTypeDialogView.getPollType().setOnClickListener(view -> {
            binding.createPostView.getSelectedType().setText(postTypeDialogView.getPollTitle().getText());
            binding.createPostView.getSelectedDesc().setText(postTypeDialogView.getPollDesc().getText());
            binding.createPostView.getSelectedIcon().setImageDrawable(getResources().getDrawable(R.drawable.ic_poll_icon_bg));
            mBottomSheetDialog.dismiss();
        });


        mBottomSheetDialog.show();
    }

    public static class Builder {
        private final Bundle bundle;
        private CreatePostFragment customFragment;
        private View.OnClickListener headerLeftButtonListener, postButtonListener;
        private View.OnClickListener headerRightButtonListener;
        private View.OnClickListener inputLeftButtonListener;

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
         * Sets the custom channel fragment. It must inherit {@link CreatePostFragment}.
         *
         * @param fragment custom channel fragment.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @since 1.0.4
         */
        public <T extends CreatePostFragment> CreatePostFragment.Builder setCustomChannelFragment(T fragment) {
            this.customFragment = fragment;
            return this;
        }

        /**
         * Sets whether the header is used.
         *
         * @param useHeader <code>true</code> if the header is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public CreatePostFragment.Builder setUseHeader(boolean useHeader) {
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
        public CreatePostFragment.Builder setUseHeaderRightButton(boolean useHeaderRightButton) {
            bundle.putBoolean(StringSet.KEY_USE_HEADER_RIGHT_BUTTON, useHeaderRightButton);
            return this;
        }

        /**
         * Sets whether the Groups View is used.
         *
         * @param useHeader <code>true</code> if the Groups View is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public CreatePostFragment.Builder setUseGroupsFeed(boolean useHeader) {
            bundle.putBoolean(StringSet.KEY_USE_GROUPS_FEEDS, useHeader);
            return this;
        }

        /**
         * Sets whether the More Options View is used.
         *
         * @param useHeader <code>true</code> if the More Options View is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public CreatePostFragment.Builder setMoreOptionLayout(boolean useHeader) {
            bundle.putBoolean(StringSet.KEY_MORE_OPTIONS_CREATEPOST, useHeader);
            return this;
        }

        /**
         * Sets whether the Video View is used in Dialog.
         *
         * @param useHeader <code>true</code> if the Video View is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public CreatePostFragment.Builder setVideo(boolean useHeader) {
            bundle.putBoolean(StringSet.KEY_VIDEO_CREATEPOST, useHeader);
            return this;
        }

        /**
         * Sets whether the Audio View is used in Dialog.
         *
         * @param useHeader <code>true</code> if the Audio View is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public CreatePostFragment.Builder setAudio(boolean useHeader) {
            bundle.putBoolean(StringSet.KEY_AUDIO_CREATEPOST, useHeader);
            return this;
        }

        /**
         * Sets whether the Image View is used in Dialog.
         *
         * @param useHeader <code>true</code> if the Image View is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public CreatePostFragment.Builder setImage(boolean useHeader) {
            bundle.putBoolean(StringSet.KEY_IMAGE_CREATEPOST, useHeader);
            return this;
        }

        /**
         * Sets whether the CTA View is used in Dialog.
         *
         * @param useHeader <code>true</code> if the CTA View is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public CreatePostFragment.Builder setCTA(boolean useHeader) {
            bundle.putBoolean(StringSet.KEY_CTA_CREATEPOST, useHeader);
            return this;
        }

        /**
         * Sets whether the left button of the header is used.
         *
         * @param useHeaderLeftButton <code>true</code> if the left button of the header is used,
         *                            <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public CreatePostFragment.Builder setUseHeaderLeftButton(boolean useHeaderLeftButton) {
            bundle.putBoolean(StringSet.KEY_USE_HEADER_LEFT_BUTTON, useHeaderLeftButton);
            return this;
        }


        /**
         * Sets the title of the header.
         *
         * @param title text to be displayed.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @since 2.1.1
         */
        public CreatePostFragment.Builder setHeaderTitle(String title) {
            bundle.putString(StringSet.KEY_HEADER_TITLE, title);
            return this;
        }

        /**
         * Sets the icon on the left button of the header.
         *
         * @param resId the resource identifier of the drawable.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public CreatePostFragment.Builder setHeaderLeftButtonIconResId(@DrawableRes int resId) {
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
        public CreatePostFragment.Builder setHeaderLeftButtonIcon(@DrawableRes int resId, @Nullable ColorStateList tint) {
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
        public CreatePostFragment.Builder setHeaderRightButtonIconResId(@DrawableRes int resId) {
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
        public CreatePostFragment.Builder setHeaderRightButtonIcon(@DrawableRes int resId, @Nullable ColorStateList tint) {
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
        public CreatePostFragment.Builder setUseInputLeftButton(boolean useInputLeftButton) {
            bundle.putBoolean(StringSet.KEY_USE_INPUT_LEFT_BUTTON, useInputLeftButton);
            return this;
        }

        public CreatePostFragment.Builder setButtonTitle(String title) {
            bundle.putString(StringSet.KEY_BUTTON_TITLE, title);
            return this;
        }


        /**
         * Sets the click listener on the left button of the header.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public CreatePostFragment.Builder setHeaderLeftButtonListener(View.OnClickListener listener) {
            this.headerLeftButtonListener = listener;
            return this;
        }


        /**
         * Sets the click listener on the left button of the header.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public CreatePostFragment.Builder setPostButtonListener(View.OnClickListener listener) {
            this.postButtonListener = listener;
            return this;
        }

        /**
         * Sets the click listener on the right button of the header.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public CreatePostFragment.Builder setHeaderRightButtonListener(View.OnClickListener listener) {
            this.headerRightButtonListener = listener;
            return this;
        }


        /**
         * Sets the click listener on the left button of the input.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public CreatePostFragment.Builder setInputLeftButtonListener(View.OnClickListener listener) {
            this.inputLeftButtonListener = listener;
            return this;
        }

        /**
         * Creates an {@link CreatePostFragment} with the arguments supplied to this
         * builder.
         *
         * @return The {@link CreatePostFragment} applied to the {@link Bundle}.
         */
        public CreatePostFragment build() {
            CreatePostFragment fragment = customFragment != null ? customFragment : new CreatePostFragment();
            fragment.setArguments(bundle);
            fragment.setHeaderLeftButtonListener(headerLeftButtonListener);
            fragment.setHeaderRightButtonListener(headerRightButtonListener);
            fragment.setPostButtonListener(postButtonListener);
            return fragment;
        }


    }

    private void setHeaderLeftButtonListener(View.OnClickListener listener) {
        this.headerLeftButtonListener = listener;
    }

    private void setHeaderRightButtonListener(View.OnClickListener listener) {
        this.headerRightButtonListener = listener;
    }

    private void setPostButtonListener(View.OnClickListener listener) {
        this.postbuttonListener = listener;
    }



/*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CreatePostViewModel.class);
        // TODO: Use the ViewModel
    }
*/

}