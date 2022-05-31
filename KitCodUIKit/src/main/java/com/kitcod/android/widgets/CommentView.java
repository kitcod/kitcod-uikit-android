package com.kitcod.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kitcod.android.R;
import com.kitcod.android.databinding.KcRowCommentBinding;

public class CommentView extends FrameLayout {
    KcRowCommentBinding binding;

    public CommentView(@NonNull Context context) {
        this(context, null);
    }

    public CommentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.kc_comment_style);
    }

    public CommentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CommentView, defStyle, 0);
        try {
            this.binding = KcRowCommentBinding.inflate(LayoutInflater.from(getContext()));
            addView(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int nameappearence = a.getResourceId(R.styleable.CommentView_kc_comment_tv_name, R.style.KitcodDarkMed14);
            int idappearence = a.getResourceId(R.styleable.CommentView_kc_comment_tv_id, R.style.KitcodCaption2OnLight02);
            int timeappearence = a.getResourceId(R.styleable.CommentView_kc_comment_tv_timepost, R.style.KitcodCaption2OnLight02);
            int commentappearence = a.getResourceId(R.styleable.CommentView_kc_comment_tv_comment, R.style.KitcodDarkReg14);
            int replyappearence = a.getResourceId(R.styleable.CommentView_kc_comment_tv_reply, R.style.KitcodBlueNormal14);
            this.binding.tvName.setTextAppearance(context, nameappearence);
            this.binding.tvId.setTextAppearance(context, idappearence);
            this.binding.tvTimepost.setTextAppearance(context, timeappearence);
            this.binding.tvComment.setTextAppearance(context, commentappearence);
            this.binding.tvReply.setTextAppearance(context, replyappearence);
            this.binding.tvViewAllReply.setTextAppearance(context, replyappearence);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
