package com.kitcod.android.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kitcod.android.KitCodUIKit;
import com.kitcod.android.R;
import com.kitcod.android.adapters.CommentsAdapter;
import com.kitcod.android.adapters.PollOptionsAdapter;
import com.kitcod.android.consts.StringSet;
import com.kitcod.android.databinding.KcRowFeedsBinding;
import com.kitcod.android.model.CommentModel;
import com.kitcod.android.model.FeedsView;
import com.kitcod.android.model.InteractionCounts;
import com.kitcod.android.model.InteractionSummary;
import com.kitcod.android.model.Post;
import com.kitcod.android.model.UserInteraction;
import com.kitcod.android.services.BackgroundNotificationService;
import com.kitcod.android.utils.Util;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class GroupPostFragment extends BaseFragment {
    private KcRowFeedsBinding binding;
    private String headerTitle = null;
    private View.OnClickListener headerLeftButtonListener;
    private View.OnClickListener headerRightButtonListener;
    private Post getGroupPosts;
    Handler handler;
    private static final int PERMISSION_REQUEST_STORAGE = 2006;
    PollOptionsAdapter pollOptionsAdapter;
    ArrayList<CommentModel> commentList = new ArrayList<>();
    CommentsAdapter adapter;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        int themeResId = KitCodUIKit.getDefaultThemeMode().getResId();
        if (args != null) {
            themeResId = args.getInt(StringSet.KEY_THEME_RES_ID, KitCodUIKit.getDefaultThemeMode().getResId());
            getGroupPosts = new Gson().fromJson(getArguments().getString(Util.POST), Post.class);
        }

        if (getActivity() != null) {
            getActivity().setTheme(themeResId);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.kc_row_feeds, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHeaderOnCreated();
        setData();
        setCommentData();
    }

    private void setCommentData() {
        FeedsView feedsView = new Gson().fromJson(loadJSONFromAsset(), FeedsView.class);
        if (feedsView.embedded.commentList != null && feedsView.embedded.commentList.size() > 0) {
            commentList.addAll(feedsView.embedded.commentList);
            if (commentList != null && commentList.size() > 0) {
                binding.rvComment.setVisibility(View.VISIBLE);
                adapter = new CommentsAdapter(getActivity(), commentList);
                binding.rvComment.setAdapter(adapter);
            }

        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("comments.json");
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
        binding.tvTitle.setText(getGroupPosts.title);
        if (Util.validateString(getGroupPosts.contentMarkdown)) {
            binding.tvContent.setText(getGroupPosts.contentMarkdown);
        } else {
            binding.tvContent.setText("");
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            if (Util.validateString(getGroupPosts.publishedTS)) {

                Date createDate = simpleDateFormat.parse(getGroupPosts.publishedTS.replace("+0000", "Z"));
                Date date = new Date();
                String stringDate = simpleDateFormat.format(date);
                Date currentDate = simpleDateFormat.parse(stringDate);
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd MMM yyyy");
                SimpleDateFormat localDateFormat;
                if (!android.text.format.DateFormat.is24HourFormat(getActivity())) {
                    localDateFormat = new SimpleDateFormat("hh:mm a");
                } else {
                    localDateFormat = new SimpleDateFormat("HH:mm");
                }
                binding.tvTime.setVisibility(View.VISIBLE);
                long diff = currentDate.getTime() - createDate.getTime();
                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000);
                int diffInDays = (int) ((currentDate.getTime() - createDate.getTime()) / (1000 * 60 * 60 * 24));
                if (diffInDays > 1) {
                    binding.tvTime.setText(simpleDateFormat1.format(createDate) + " at " + localDateFormat.format(createDate));
                } else if (diffHours > 24 && diffHours < 48) {
                    binding.tvTime.setText("Yesterday at " + localDateFormat.format(createDate));
                } else if ((diffHours < 1)) {
                    if (diffMinutes < 2) {
                        binding.tvTime.setText("A moment ago");
                    } else {
                        binding.tvTime.setText(diffMinutes + " minutes " + "ago");
                    }
                } else if ((diffHours < 24)) {
                    if (diffHours == 1) {
                        binding.tvTime.setText(diffHours + " hour " + "ago");
                    } else {
                        binding.tvTime.setText(diffHours + " hours " + "ago");
                    }
                }
            } else {
                binding.tvTime.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (getGroupPosts.media.imgs != null && getGroupPosts.media.imgs.size() > 0) {
            binding.postImage.setVisibility(View.VISIBLE);
            if (getGroupPosts.media.imgs.get(0).resizedMediaList != null && getGroupPosts.media.imgs.get(0).resizedMediaList.size() > 0) {
                for (int i = 0; i < getGroupPosts.media.imgs.get(0).resizedMediaList.size(); i++) {
                    if (getGroupPosts.media.imgs.get(0).resizedMediaList.get(i).type.equalsIgnoreCase("resized")) {
                        final String img = getGroupPosts.media.imgs.get(0).resizedMediaList.get(i).mediaUrl;
                        Picasso.get()
                                .load(img)
                                .into(binding.postImage);

                    }
                }

            }

        } else {
            binding.postImage.setVisibility(View.GONE);
        }

        if (getGroupPosts.media != null && getGroupPosts.media.docs != null
                && getGroupPosts.media.docs.size() > 0) {
            for (int i = 0; i < getGroupPosts.media.docs.size(); i++) {
                if (getGroupPosts.media.docs.get(i).mediaUrl != null) {
                    String extension = getGroupPosts.media.docs.get(i).mediaUrl.substring(getGroupPosts.media.docs.get(i).mediaUrl.lastIndexOf("."));
                    if (extension.equalsIgnoreCase(".doc") || extension.equalsIgnoreCase(".docx")) {
                        Picasso.get().load(R.drawable.doc_icon).into(binding.ivImg);
                    } else if (extension.equalsIgnoreCase(".xls") || extension.equalsIgnoreCase(".xlsx")) {
                        Picasso.get().load(R.drawable.xls_icon).into(binding.ivImg);
                    } else if (extension.equalsIgnoreCase(".pdf")) {
                        Picasso.get().load(R.drawable.pdf_icon).into(binding.ivImg);
                    } else {
                        Picasso.get().load(R.drawable.otherfile_icon).into(binding.ivImg);
                    }
                    binding.layoutDoc.setVisibility(View.VISIBLE);
                    binding.layoutDoc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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
                                    intent.putExtra("TEST", getGroupPosts.media.docs.get(0).mediaUrl);
                                    getActivity().startService(intent);
                                }
                            });
                        }
                    });
                    binding.layoutDoc.setTag(getGroupPosts.media.docs.get(i).mediaUrl);

                    binding.tvNameDoc.setText(URLUtil.guessFileName(getGroupPosts.media.docs.get(i).mediaUrl, null, null));
                    int finalI = i;
                    new FileSize(binding.tvSizeDoc, getGroupPosts.media.docs.get(finalI).mediaUrl).execute();

                } else {
                    binding.layoutDoc.setVisibility(View.GONE);
                }
            }
        } else {
            binding.layoutDoc.setVisibility(View.GONE);
        }

        if (getGroupPosts.interactionSummary != null && getGroupPosts.interactionSummary.userInteractions != null && getGroupPosts.interactionSummary.userInteractions.size() > 0) {
            for (int i = 0; i < getGroupPosts.interactionSummary.userInteractions.size(); i++) {
                if (getGroupPosts.interactionSummary.userInteractions.get(i).interactionType.equalsIgnoreCase("LIKEOPTION1")) {
                    binding.ivLike.setImageResource(R.drawable.peace_icon);
                } else {
                    binding.ivLike.setImageResource(R.drawable.ic_like_icon);
                }
            }
        } else {
            binding.ivLike.setImageResource(R.drawable.ic_like_icon);
        }

        if (getGroupPosts.interactionSummary != null && getGroupPosts.interactionSummary.interactionCounts != null) {
            int likecount = getGroupPosts.interactionSummary.interactionCounts.likeoption1 +
                    getGroupPosts.interactionSummary.interactionCounts.likeoption2 +
                    getGroupPosts.interactionSummary.interactionCounts.likeoption3 +
                    getGroupPosts.interactionSummary.interactionCounts.likeoption4 +
                    getGroupPosts.interactionSummary.interactionCounts.likeoption5 +
                    getGroupPosts.interactionSummary.interactionCounts.likeoption6;
            binding.tvLike.setText("" + likecount);
        }

        if (getGroupPosts.postType.equalsIgnoreCase("POLL")) {
            if (getGroupPosts.interactionSummary != null) {
                int voteCount = getGroupPosts.interactionSummary.interactionCounts.polloption1 +
                        getGroupPosts.interactionSummary.interactionCounts.polloption2 +
                        getGroupPosts.interactionSummary.interactionCounts.polloption3 +
                        getGroupPosts.interactionSummary.interactionCounts.polloption4 +
                        getGroupPosts.interactionSummary.interactionCounts.polloption5;
                binding.tvTotalVotes.setVisibility(View.VISIBLE);
                if (voteCount == 1) {
                    binding.tvTotalVotes.setText(voteCount + " Vote");
                } else {
                    binding.tvTotalVotes.setText(voteCount + " Votes");
                }
            } else {
                binding.tvTotalVotes.setVisibility(View.GONE);
                binding.tvTotalVotes.setText(0 + " Votes");
            }
            boolean isPollEnded = true;
            if (Util.validateString(getGroupPosts.poll.pollEndsDateTime)) {

                try {
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date pollEndDate = simpleDateFormat2.parse(getGroupPosts.poll.pollEndsDateTime.replace("+0000", "Z"));
                    Date date = new Date();
                    String stringDate = simpleDateFormat2.format(date);
                    Date currentDate = simpleDateFormat2.parse(stringDate);
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd MMM yyyy");
                    SimpleDateFormat localDateFormat;
                    if (!android.text.format.DateFormat.is24HourFormat(getActivity())) {
                        localDateFormat = new SimpleDateFormat("hh:mm a");
                    } else {
                        localDateFormat = new SimpleDateFormat("HH:mm");
                    }
                    long diff = pollEndDate.getTime() - currentDate.getTime();
                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000);
                    int diffInDays = (int) ((currentDate.getTime() - pollEndDate.getTime()) / (1000 * 60 * 60 * 24));

                    if (diffInDays > 0 && diffHours > 0) {
                        isPollEnded = false;
                        binding.tvPollleft.setText("Ends in " + diffInDays + " days " + diffHours + " hours");
                    } else if (diffInDays <= 0 && diffHours >= 1) {
                        isPollEnded = false;
                        if (diffMinutes > 0) {
                            binding.tvPollleft.setText("Ends in " + diffHours + " hours " + diffMinutes + " minutes");
                        } else {
                            binding.tvPollleft.setText("Ends in " + diffHours + " hours");
                        }
                    } else if (diffInDays <= 0 && diffHours <= 0 && diffMinutes > 0) {
                        isPollEnded = false;
                        binding.tvPollleft.setText("Ends in " + diffMinutes + " minutes");
                    } else {
                        isPollEnded = true;
                        binding.tvPollleft.setText("Poll ended");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                binding.tvPollleft.setText("");
                isPollEnded = false;
            }
            binding.layoutPolls.setVisibility(View.VISIBLE);
            binding.tvTitle.setVisibility(View.VISIBLE);
//                    binding.tvContent.setTypeface(CollabDeenApplication.mRegularFont);
            binding.rvPollOptions.setVisibility(View.VISIBLE);
            pollOptionsAdapter = new PollOptionsAdapter(getActivity(), getGroupPosts.poll.options,
                    true, getGroupPosts.interactionSummary, onPollOptionClickListener, getGroupPosts, isPollEnded);
           /* pollOptionsAdapter = new PollOptionsAdapter(context, getGroupPosts.poll.pollOptions,
                    getGroupPosts.isVoted, getGroupPosts.interactionSummary, onPollOptionClickListener, getGroupPosts, isPollEnded);*/
            binding.rvPollOptions.setAdapter(pollOptionsAdapter);
            binding.rvPollOptions.setAdapter(pollOptionsAdapter);
        } else {
            binding.layoutPolls.setVisibility(View.GONE);
            binding.rvPollOptions.setVisibility(View.GONE);
        }

        if (getGroupPosts.media != null && getGroupPosts.media.auds != null
                && getGroupPosts.media.auds.size() > 0) {
            binding.audioLayout.setVisibility(View.VISIBLE);
            binding.btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    feedListener.onAudPlayClick(position);
                }
            });

            if (getGroupPosts.exoplayerModel != null) {
                int timer = (int) getGroupPosts.exoplayerModel.exoPlayer.getDuration();
//                    binding.tv_audio_timer.setText(Utils.stringForTime((int) getCommunitiesPosts.get(position).media.auds.get(0).exoplayerModel.exoPlayer.getDuration()));

                binding.tvAudioTimer.setText(Util.stringForTime(timer));
                binding.progressAudio.setProgress(0);
                binding.progressAudio.setMax(timer);
                if (handler == null) handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (getGroupPosts.exoplayerModel != null && getGroupPosts.exoplayerModel.getExoPlayer() != null && getGroupPosts.isPlaying) {
                            binding.progressAudio.setMax((int) getGroupPosts.getExoplayerModel().getExoPlayer().getDuration() / 1000);
                            int mCurrentPosition = (int) getGroupPosts.getExoplayerModel().getExoPlayer().getCurrentPosition() / 1000;
                            binding.progressAudio.setProgress(mCurrentPosition);
                            int remainingtime = (int) getGroupPosts.exoplayerModel.exoPlayer.getDuration() - (int) getGroupPosts.exoplayerModel.exoPlayer.getContentPosition();
                            binding.tvAudioStart.setText(Util.stringForTime((int) getGroupPosts.exoplayerModel.exoPlayer.getCurrentPosition()));
//                    txtEndTime.setText(stringForTime((int) exoPlayer.getDuration()));
                            binding.tvMoveBack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getGroupPosts.exoplayerModel.exoPlayer.seekTo(((mCurrentPosition - 15) * 1000));
                                }
                            });
                            binding.tvMoveForward.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getGroupPosts.exoplayerModel.exoPlayer.seekTo(((mCurrentPosition + 15) * 1000));
                                }
                            });
                            handler.postDelayed(this, 1000);
                        }
                    }
                });

            }

            if (getGroupPosts.isPlaying) {
                Picasso.get().load(R.drawable.pause_audio_btn).into(binding.btnPlay);
            } else {
                Picasso.get().load(R.drawable.play_audio_btn).into(binding.btnPlay);
            }
        }

    }

    class FileSize extends AsyncTask<Integer, Integer, String> {
        TextView tv_size_doc;
        String URL;

        FileSize(TextView textView, String URL) {
            this.tv_size_doc = textView;
            this.URL = URL;
        }

        @Override
        protected String doInBackground(Integer... integers) {
            java.net.URL url = null;
            try {
                url = new URL(URL);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                long file_size = urlConnection.getContentLength();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_size_doc.setText(Util.getFileSize(file_size));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private View.OnClickListener onPollOptionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           /* if (getUser.type.equalsIgnoreCase(PreferencesManager.PrefKey.ANONYMOUS)) {
                SignUpDialog addPhotoBottomDialogFragment = new SignUpDialog();

                addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
                        SignUpDialog.TAG);

                return;
            }*/
            Post mCommunityPost = (Post) v.getTag(R.id.mainPost);
            int mChildPosition = (int) v.getTag(R.id.childPollposition);
            int mainPosition = (int) v.getTag(R.id.mainPostpostion);

            JsonObject interactionJson = new JsonObject();
            interactionJson.addProperty("resourceId", mCommunityPost.id);
            interactionJson.addProperty("resourceType", "POST");
            interactionJson.addProperty("resourceSubType", "POLL");
            interactionJson.addProperty("interactionType", "POLLOPTION" + (mChildPosition + 1));
            v.setClickable(false);

            Gson gson = new Gson();
            UserInteraction userInteraction = gson.fromJson(interactionJson.toString(), UserInteraction.class);

//            for (int i = 0; i < getCommunitiesPosts.size(); i++) {
//                if (getGroupPosts != null && getGroupPosts.id.equalsIgnoreCase(mCommunityPost.id)) {
            getGroupPosts.isVoted = true;
            if (getGroupPosts.interactionSummary != null && getGroupPosts.interactionSummary.userInteractions != null) {
                boolean isUserReactedonPoll = false;
                for (int j = 0; j < getGroupPosts.interactionSummary.userInteractions.size(); j++) {
                    if (getGroupPosts.interactionSummary.userInteractions.get(j).resourceSubType != null && getGroupPosts.interactionSummary.userInteractions.get(j).resourceSubType.equalsIgnoreCase("POLL")) {
                        //USER POLL EXISTS
                        isUserReactedonPoll = true;


                    }
                }

                if (!isUserReactedonPoll) {
                    if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION1")) {
                        int polloption1 = getGroupPosts.interactionSummary.interactionCounts.polloption1 + 1;
                        getGroupPosts.interactionSummary.interactionCounts.polloption2 = polloption1;
                    } else if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION2")) {
                        int polloption1 = getGroupPosts.interactionSummary.interactionCounts.polloption2 + 1;
                        getGroupPosts.interactionSummary.interactionCounts.polloption2 = polloption1;
                    } else if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION3")) {
                        int polloption1 = getGroupPosts.interactionSummary.interactionCounts.polloption3 + 1;
                        getGroupPosts.interactionSummary.interactionCounts.polloption3 = polloption1;
                    } else if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION4")) {
                        int polloption1 = getGroupPosts.interactionSummary.interactionCounts.polloption4 + 1;
                        getGroupPosts.interactionSummary.interactionCounts.polloption4 = polloption1;
                    } else if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION5")) {
                        int polloption1 = getGroupPosts.interactionSummary.interactionCounts.polloption5 + 1;
                        getGroupPosts.interactionSummary.interactionCounts.polloption5 = polloption1;
                    }

                    getGroupPosts.interactionSummary.userInteractions.add(userInteraction);
                }
            } else {
                if (getGroupPosts.interactionSummary == null) {
                    InteractionSummary interactionSummary = new InteractionSummary();
                    InteractionCounts counts = new InteractionCounts();
                    if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION1")) {
                        counts.polloption1++;
                    } else if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION2")) {
                        counts.polloption2++;
                    } else if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION3")) {
                        counts.polloption3++;
                    } else if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION4")) {
                        counts.polloption4++;
                    } else if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION5")) {
                        counts.polloption5++;
                    }

                    interactionSummary.interactionCounts = counts;
                    getGroupPosts.interactionSummary = interactionSummary;
                    List<UserInteraction> userInteractionList = new ArrayList<>();
                    userInteractionList.add(userInteraction);
                    getGroupPosts.interactionSummary.userInteractions = userInteractionList;

                } else if (getGroupPosts.interactionSummary.userInteractions == null) {
                    if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION1")) {
                        int polloption1 = getGroupPosts.interactionSummary.interactionCounts.polloption1 + 1;
                        getGroupPosts.interactionSummary.interactionCounts.polloption1 = polloption1;
                    } else if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION2")) {
                        int polloption1 = getGroupPosts.interactionSummary.interactionCounts.polloption2 + 1;
                        getGroupPosts.interactionSummary.interactionCounts.polloption2 = polloption1;
                    } else if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION3")) {
                        int polloption1 = getGroupPosts.interactionSummary.interactionCounts.polloption3 + 1;
                        getGroupPosts.interactionSummary.interactionCounts.polloption3 = polloption1;
                    } else if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION4")) {
                        int polloption1 = getGroupPosts.interactionSummary.interactionCounts.polloption4 + 1;
                        getGroupPosts.interactionSummary.interactionCounts.polloption4 = polloption1;
                    } else if (userInteraction.interactionType.equalsIgnoreCase("POLLOPTION5")) {
                        int polloption1 = getGroupPosts.interactionSummary.interactionCounts.polloption5 + 1;
                        getGroupPosts.interactionSummary.interactionCounts.polloption5 = polloption1;
                    }
                    if (getGroupPosts.interactionSummary.userInteractions == null) {
                        getGroupPosts.interactionSummary.userInteractions = new ArrayList<>();
                        getGroupPosts.interactionSummary.userInteractions.add(userInteraction);
                    } else {
                        getGroupPosts.interactionSummary.userInteractions.add(userInteraction);
                    }
                }


            }
            boolean isPollEnded = true;
            if (Util.validateString(getGroupPosts.poll.pollEndsDateTime)) {

                try {
                    SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date pollEndDate = simpleDateFormat2.parse(getGroupPosts.poll.pollEndsDateTime.replace("+0000", "Z"));
                    Date date = new Date();
                    String stringDate = simpleDateFormat2.format(date);
                    Date currentDate = simpleDateFormat2.parse(stringDate);
                    long diff = pollEndDate.getTime() - currentDate.getTime();
                    long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000);
                    int diffInDays = (int) ((currentDate.getTime() - pollEndDate.getTime()) / (1000 * 60 * 60 * 24));

                    if (diffInDays > 0 && diffHours > 0) {
                        isPollEnded = false;
                        binding.tvPollleft.setText("Ends in " + diffInDays + " days " + diffHours + " hours");
                    } else if (diffInDays <= 0 && diffHours >= 1) {
                        isPollEnded = false;
                        binding.tvPollleft.setText("Ends in " + diffHours + " hours");
                    } else if (diffInDays <= 0 && diffHours <= 0 && diffMinutes > 0) {
                        isPollEnded = false;
                        binding.tvPollleft.setText("Ends in " + diffMinutes + " minutes");
                    } else {
                        isPollEnded = true;
                        binding.tvPollleft.setText("Poll ended");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                binding.tvPollleft.setText("");
                isPollEnded = false;
            }
            if (getGroupPosts.postType.equalsIgnoreCase("POLL")) {
                if (getGroupPosts.interactionSummary != null) {
                    int voteCount = getGroupPosts.interactionSummary.interactionCounts.polloption1 +
                            getGroupPosts.interactionSummary.interactionCounts.polloption2 +
                            getGroupPosts.interactionSummary.interactionCounts.polloption3 +
                            getGroupPosts.interactionSummary.interactionCounts.polloption4 +
                            getGroupPosts.interactionSummary.interactionCounts.polloption5;
                    if (voteCount == 1) {
                        binding.tvTotalVotes.setText(voteCount + " Vote");
                    } else {
                        binding.tvTotalVotes.setText(voteCount + " Votes");
                    }
                } else {
                    binding.tvTotalVotes.setText(0 + " Votes");
                }
            }
            pollOptionsAdapter = new PollOptionsAdapter(getActivity(), getGroupPosts.poll.options,
                    getGroupPosts.isVoted, getGroupPosts.interactionSummary, onPollOptionClickListener, getGroupPosts, isPollEnded);
            binding.rvPollOptions.setAdapter(pollOptionsAdapter);
//                    break;
//                }
//            }


        }
    };

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
        private GroupPostFragment customFragment;
        private View.OnClickListener headerLeftButtonListener;
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

        public Builder(String post) {
            bundle = new Bundle();
            bundle.putString(Util.POST, post);
        }

        public Builder(String post, @StyleRes int customThemeResId) {
            bundle = new Bundle();
            bundle.putInt(StringSet.KEY_THEME_RES_ID, customThemeResId);
            bundle.putString(Util.POST, post);
        }


        /**
         * Sets the custom channel fragment. It must inherit {@link GroupPostFragment}.
         *
         * @param fragment custom channel fragment.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @since 1.0.4
         */
        public <T extends GroupPostFragment> GroupPostFragment.Builder setCustomChannelFragment(T fragment) {
            this.customFragment = fragment;
            return this;
        }

        /**
         * Sets whether the header is used.
         *
         * @param useHeader <code>true</code> if the header is used, <code>false</code> otherwise.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupPostFragment.Builder setUseHeader(boolean useHeader) {
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
        public GroupPostFragment.Builder setUseHeaderRightButton(boolean useHeaderRightButton) {
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
        public GroupPostFragment.Builder setUseHeaderLeftButton(boolean useHeaderLeftButton) {
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
        public GroupPostFragment.Builder setUseLastSeenAt(boolean useLastSeenAt) {
            return this;
        }


        /**
         * Sets the title of the header.
         *
         * @param title text to be displayed.
         * @return This Builder object to allow for chaining of calls to set methods.
         * @since 2.1.1
         */
        public GroupPostFragment.Builder setHeaderTitle(String title) {
            bundle.putString(StringSet.KEY_HEADER_TITLE, title);
            return this;
        }

        /**
         * Sets the icon on the left button of the header.
         *
         * @param resId the resource identifier of the drawable.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupPostFragment.Builder setHeaderLeftButtonIconResId(@DrawableRes int resId) {
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
        public GroupPostFragment.Builder setHeaderLeftButtonIcon(@DrawableRes int resId, @Nullable ColorStateList tint) {
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
        public GroupPostFragment.Builder setHeaderRightButtonIconResId(@DrawableRes int resId) {
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
        public GroupPostFragment.Builder setHeaderRightButtonIcon(@DrawableRes int resId, @Nullable ColorStateList tint) {
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
        public GroupPostFragment.Builder setUseInputLeftButton(boolean useInputLeftButton) {
            bundle.putBoolean(StringSet.KEY_USE_INPUT_LEFT_BUTTON, useInputLeftButton);
            return this;
        }

        public GroupPostFragment.Builder setButtonTitle(String title) {
            bundle.putString(StringSet.KEY_BUTTON_TITLE, title);
            return this;
        }


        /**
         * Sets the click listener on the left button of the header.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupPostFragment.Builder setHeaderLeftButtonListener(View.OnClickListener listener) {
            this.headerLeftButtonListener = listener;
            return this;
        }

        /**
         * Sets the click listener on the right button of the header.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupPostFragment.Builder setHeaderRightButtonListener(View.OnClickListener listener) {
            this.headerRightButtonListener = listener;
            return this;
        }


        /**
         * Sets the click listener on the left button of the input.
         *
         * @param listener The callback that will run.
         * @return This Builder object to allow for chaining of calls to set methods.
         */
        public GroupPostFragment.Builder setInputLeftButtonListener(View.OnClickListener listener) {
            this.inputLeftButtonListener = listener;
            return this;
        }

        /**
         * Creates an {@link GroupPostFragment} with the arguments supplied to this
         * builder.
         *
         * @return The {@link GroupPostFragment} applied to the {@link Bundle}.
         */
        public GroupPostFragment build() {
            GroupPostFragment fragment = customFragment != null ? customFragment : new GroupPostFragment();
            fragment.setArguments(bundle);
            fragment.setHeaderLeftButtonListener(headerLeftButtonListener);
            fragment.setHeaderRightButtonListener(headerRightButtonListener);
            return fragment;
        }

    }

    private void setHeaderLeftButtonListener(View.OnClickListener listener) {
        this.headerLeftButtonListener = listener;
    }

    private void setHeaderRightButtonListener(View.OnClickListener listener) {
        this.headerRightButtonListener = listener;
    }

}
