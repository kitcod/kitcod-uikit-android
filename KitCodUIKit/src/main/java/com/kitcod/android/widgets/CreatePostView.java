package com.kitcod.android.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.databinding.DataBindingUtil;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcCreatePostViewBinding;

public class CreatePostView extends FrameLayout {
    KcCreatePostViewBinding binding;

    public CreatePostView(@NonNull Context context) {
        this(context, null);
    }

    public CreatePostView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_createpost_style);
    }

    public CreatePostView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CreatePostView, defStyle, 0);
        try {
            this.binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.kc_create_post_view, this, true);
            int selectedTypeAppearance = a.getResourceId(R.styleable.CreatePostView_kc_post_view_selected_label_appearence, R.style.KitcodDarkMed16);
            int selectedDescTypeAppearance = a.getResourceId(R.styleable.CreatePostView_kc_post_view_selected_desc_appearence, R.style.KitcodLightReg14);
            int nameAppearance = a.getResourceId(R.styleable.CreatePostView_kc_post_view_name_appearance, R.style.KitcodDarkMed16);
            int nameIdTypeAppearance = a.getResourceId(R.styleable.CreatePostView_kc_post_view_name_id_appearance, R.style.KitcodLightReg16);
            int groupNameAppearance = a.getResourceId(R.styleable.CreatePostView_kc_post_view_groupname_appearance, R.style.KitcodBlueNormal14);
            int headlineAppearance = a.getResourceId(R.styleable.CreatePostView_kc_post_view_headline_appearance, R.style.KitcodDarkMed20);
            int postContentAppearance = a.getResourceId(R.styleable.CreatePostView_kc_post_view_postcontent_appearance, R.style.KitcodDarkReg18);
            int postviewMoreAppearance = a.getResourceId(R.styleable.CreatePostView_kc_post_view_more_appearance, R.style.KitcodDarkMed16);
            int postviewmoreDescAppearance = a.getResourceId(R.styleable.CreatePostView_kc_post_view_more_desc_appearance, R.style.KitcodLightReg14);
//            int postviewMoreTintAppearance = a.getResourceId(R.styleable.CreatePostView_kc_post_view_more_icon_tint, R.color.kitcodColorBlack);
            ColorStateList postviewMoreTintAppearance = a.getColorStateList(R.styleable.CreatePostView_kc_post_view_more_icon_tint);
            //            int selectedTypeBackgroundTintAppearance = a.getResourceId(R.styleable.CreatePostView_kc_post_view_selected_type_background_tint);
            ColorStateList selectedTypeBackgroundTintAppearance = a.getColorStateList(R.styleable.CreatePostView_kc_post_view_selected_type_background_tint);
//            int selectedTypeIconTintAppearance = a.getResourceId(R.styleable.CreatePostView_kc_post_view_selected_type_icon_tint, R.color.kitcodColorBlack);
            ColorStateList selectedTypeIconTintAppearance = a.getColorStateList(R.styleable.CreatePostView_kc_post_view_selected_type_icon_tint);
//            int selectedTypeMoreTintAppearance = a.getResourceId(R.styleable.CreatePostView_kc_post_view_selected_more_icon_tint, R.color.kitcodColorBlack);
            ColorStateList selectedTypeMoreTintAppearance = a.getColorStateList(R.styleable.CreatePostView_kc_post_view_selected_more_icon_tint);
            binding.tvPostTypeLabel.setTextAppearance(context, selectedTypeAppearance);
            binding.tvTypeDesc.setTextAppearance(context, selectedDescTypeAppearance);
            binding.tvName.setTextAppearance(context, nameAppearance);
            binding.tvNameId.setTextAppearance(context, nameIdTypeAppearance);
            binding.tvGroupname.setTextAppearance(context, groupNameAppearance);
            binding.etTitle.setTextAppearance(context, headlineAppearance);
            binding.etDesc.setTextAppearance(context, postContentAppearance);
            binding.tvMoreLabel.setTextAppearance(context, postviewMoreAppearance);
            binding.textView10.setTextAppearance(context, postviewmoreDescAppearance);
//            binding.imageView2.setColorFilter(selectedTypeIconTintAppearance);
            ImageViewCompat.setImageTintList(binding.imageView2, selectedTypeIconTintAppearance);
            ImageViewCompat.setImageTintList(binding.ivPostTypeMore, postviewMoreTintAppearance);
            ImageViewCompat.setImageTintList( binding.ivMore, selectedTypeMoreTintAppearance);
            binding.imageView2.setBackgroundTintList(selectedTypeBackgroundTintAppearance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ImageView getSelectedIcon() {
        return binding.imageView2;
    }

    public ConstraintLayout getPostSelectedType() {
        return binding.clSelectedtype;
    }

    public TextView getSelectedType() {
        return binding.tvPostTypeLabel;
    }

    public TextView getSelectedDesc() {
        return binding.tvTypeDesc;
    }


    public ConstraintLayout getMoreOption() {
        return binding.clMore;
    }

    public ButtonContainedView getPostButton() {
        return binding.btnPost;
    }

    public ImageView getProfilePic() {
        return binding.circleImageView;
    }
}
