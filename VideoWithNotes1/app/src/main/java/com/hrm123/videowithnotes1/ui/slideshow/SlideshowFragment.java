package com.hrm123.videowithnotes1.ui.slideshow;

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
        import io.grpc.ManagedChannel;
        import io.grpc.ManagedChannelBuilder;

        import com.hrm123.nextgenvideosvc.FileListReq;
        import com.hrm123.nextgenvideosvc.FileListResp;
        import com.hrm123.nextgenvideosvc.FileName;
        import com.hrm123.nextgenvideosvc.NextGenVideoServiceGrpc;
        import com.hrm123.videowithnotes1.R;
        import com.hrm123.videowithnotes1.Video;
        import com.hrm123.videowithnotes1.VideoAdapter;

        import java.util.ArrayList;
        import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel galleryViewModel;
    private List<Video> mVideosList = new ArrayList<>();
    private ListView mListViewVideos;
    private VideoAdapter mVideoAdapter;
    private Cursor videocursor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        List<FileName> files = GetFileList();

        for(int i=0;i<files.size();i++){
            String vUrl = files.get(i).getFileName();
            mVideosList.add(new Video(vUrl));
        }

        mListViewVideos  = root.findViewById(R.id.video_list_view);
        mVideoAdapter = new VideoAdapter(getContext(), mVideosList);
        mListViewVideos.setAdapter(mVideoAdapter);

        return root;
    }


    private List<FileName> GetFileList(){
        /*
        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                "3.134.87.107", 33333 )
                .usePlaintext()
                .build();

         */

        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                "3.134.87.107", 33333 )
                //"3.134.87.107", 33333)
                .usePlaintext()
                .build();

        try {

            NextGenVideoServiceGrpc.NextGenVideoServiceStub stub = NextGenVideoServiceGrpc.newStub(channel);
            FileListReq.Builder reqBuilder = FileListReq.newBuilder();
            NextGenVideoServiceGrpc.NextGenVideoServiceBlockingStub blockingStub = NextGenVideoServiceGrpc.newBlockingStub(channel);
            FileListResp files = blockingStub.listFiles(reqBuilder.build());
            return files.getFilesList();
        }
        catch(Exception ex){
            return null;
        }

    }

}