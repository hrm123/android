package com.hrm123.videowithnotes1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

import androidx.annotation.NonNull;

public class VideoAdapter extends ArrayAdapter<Video> {

    private Context mContext;
    private List<Video> mVideos;


    public VideoAdapter(@NonNull Context context, @NonNull List<Video> objects) {
        super(context, R.layout.video_row, objects);

        mContext = context;
        mVideos = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;


        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.video_row, null);
            holder = new ViewHolder();

            // holder.videoView = (VideoView) convertView
               //     .findViewById(R.id.videoView);

            holder.textView = (TextView) convertView
                 .findViewById(R.id.videoText);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView tv = (TextView) view;
                    String txt = tv.getText().toString();

                    String base = "http://3.135.87.107/";
                    Intent intent = new Intent(getContext(),FullScreenVideoActivity.class);
                    intent.putExtra("vurl", base + txt);
                    intent.putExtra("fullScreen","y");
                    ((Activity)getContext()).startActivity(intent);
                }
            });
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        Video video = mVideos.get(position);
        //play video using android api, when video view is clicked.
        String url = video.getVideoUrl(); // your URL here
        // Uri videoUri = Uri.parse(url);
        holder.textView.setText(url);

        /***get clicked view and play video url at this position**/
        /*
        try {
            Video video = mVideos.get(position);
            //play video using android api, when video view is clicked.
            String url = video.getVideoUrl(); // your URL here
            Uri videoUri = Uri.parse(url);
            holder.videoView.setVideoURI(videoUri);
            holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    holder.videoView.start();
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
         */

        return convertView;
    }

    public static class ViewHolder {
        // VideoView videoView;
        TextView textView;


    }
}
