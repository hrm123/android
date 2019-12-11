package com.hrm123.videowithnotes1.ui.gallery;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hrm123.videowithnotes1.R;
import com.hrm123.videowithnotes1.Video;
import com.hrm123.videowithnotes1.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private List<Video> mVideosList = new ArrayList<>();
    private ListView mListViewVideos;
    private VideoAdapter mVideoAdapter;
    private Cursor videocursor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        String selection = MediaStore.Video.Media.DATA +" like?";
        String[] selectionArgs=new String[]{"%Sceneform%"};
        String[] parameters = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE };
        videocursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                parameters, selection, selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");
        int count = videocursor.getCount();
        int video_column_index = videocursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        while(videocursor.moveToNext()){
            String vUrl = videocursor.getString(video_column_index);
            mVideosList.add(new Video(vUrl));
        }
        videocursor.close();

        mListViewVideos  = root.findViewById(R.id.video_list_view);
        mVideoAdapter = new VideoAdapter(getContext(), mVideosList);
        mListViewVideos.setAdapter(mVideoAdapter);

        return root;
    }
}