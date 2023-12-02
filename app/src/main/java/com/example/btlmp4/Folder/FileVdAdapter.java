package com.example.btlmp4.Folder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.*;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.btlmp4.R;
import com.example.btlmp4.VideoPlayer.VideoPlayerActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class FileVdAdapter extends RecyclerView.Adapter<FileVdAdapter.ViewHolder> {
    private ArrayList<MediaFile> videoList;
    private Context context;
    BottomSheetDialog bottomSheetDialog;

    public FileVdAdapter(ArrayList<MediaFile> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public FileVdAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.video_iteam, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FileVdAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Lấy dữ liệu của file video để hiển thị
        holder.videoName.setText(videoList.get(position).getDisplayName());
        String size = videoList.get(position).getSize();
        holder.videoSize.setText(android.text.format.Formatter
                .formatFileSize(context, Long.parseLong(size)));
        double milliSeconds = Double.parseDouble(videoList.get(position).getDuration());
        //đổi duration sang dạng xx:xx
        holder.videoDuration.setText(timeConversion((long) milliSeconds));

        //sử dụng thư viện Glide để hiển thị hình ảnh hoặc video thumbnail
        // từ đường dẫn tệp trong một ImageView
        Glide.with(context).load(new File(videoList.get(position).getPath()))
                .into(holder.thumbbail);

        holder.menu_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
                View bsView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout,
                        v.findViewById(R.id.bottom_sheet));
                bsView.findViewById(R.id.bs_rename)
                        .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Đổi Tên Video");
                        EditText editText = new EditText(context);
                        String path = videoList.get(position).getPath();
                        File file = new File(path);
                        String videoName = file.getName();
                        //bỏ đuôi .mp4
                        videoName = videoName.substring(0, videoName.lastIndexOf("."));
                        //đưa tên lên editText
                        editText.setText(videoName);
                        //Hiển thị edit lên dialog
                        alertDialog.setView(editText);
                        //Yêu cầu trỏ bàn phím tới editText đang nhập tên trong dialog
                        editText.requestFocus();

                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Nếu nhập tên rỗng thì không thể đổi tên file
                                if(TextUtils.isEmpty(editText.getText().toString())) {
                                    Toast.makeText(context, "Không thể đổi tên file", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                String onlyPath = file.getParentFile().getAbsolutePath(); //đường dẫn thư mục
                                String ext = file.getAbsolutePath(); //đường dẫn tới file
                                ext = ext.substring(ext.lastIndexOf(".")); //lấy phần mở rộng .mp4
                                String newPath = onlyPath + "/" + editText.getText().toString() + ext;
                                File newFile = new File(newPath);
                                boolean rename = file.renameTo(newFile);
                                // sử dụng để đổi tên (di chuyển đường dẫn) của tệp từ file sang newFile.
                                // Biến rename sẽ được đặt thành true nếu quá trình đổi tên thành công,
                                // ngược lại sẽ là false.
                                if (rename){
                                    //Xóa đường dẫn đến file với tên cũ
                                    ContentResolver resolver = context.getApplicationContext().getContentResolver();
                                    resolver.delete(MediaStore.Files.getContentUri("external"),
                                            MediaStore.MediaColumns.DATA + "=?", new String[]{
                                                    file.getAbsolutePath()
                                            });
                                    //Cập nhật lại MediaStore
                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                    intent.setData(Uri.fromFile(newFile));
                                    // Gửi broadcast để thông báo cho hệ thống rằng có một tệp mới cần được quét.
                                    context.getApplicationContext().sendBroadcast(intent);
                                    
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Đã đổi tên video", Toast.LENGTH_SHORT).show();

                                    SystemClock.sleep(200); //Tạm ngừng app trong 0.2s
                                    ((Activity) context).recreate(); //Load lại trang
                                }else{
                                    Toast.makeText(context, "Lỗi", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.create().show();
                        bottomSheetDialog.dismiss();
                    }
                });
                bsView.findViewById(R.id.bs_share)
                        .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(videoList.get(position).getPath());
                        //Kết nối với activity share có sẵn
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("video/*");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ Video "));
                        bottomSheetDialog.dismiss();
                    }
                });
                bsView.findViewById(R.id.bs_dele)
                        .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Xóa Video");
                        alertDialog.setMessage("Bạn có chắc chắn muốn xóa video này ?");
                        alertDialog.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Tạo uri đến videoitem
                                Uri contentUri = ContentUris
                                        .withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                                Long.parseLong(videoList.get(position).getId()));
                                //tạo đường dẫn đến videoiteam
                                File file = new File(videoList.get(position).getPath());
                                //xóa tệp hoặc thư mục mà file đại diện.
                                boolean delete = file.delete();

                                if(delete){
                                    //xóa một dòng dữ liệu từ cơ sở dữ liệu của hệ thống Android thông qua ContentResolver.
                                    context.getContentResolver().delete(contentUri, null, null);
                                    //Thông báo với recycler view
                                    videoList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemChanged(position, videoList.size());
                                    Toast.makeText(context, "Đã xóa Video", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Không thể xóa Video", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                        bottomSheetDialog.dismiss();
                    }
                });
                bsView.findViewById(R.id.bs_properties)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                        alertDialog.setTitle("Thuộc Tính");

                                        String line1 = "Tên: " + videoList.get(position).getDisplayName();
                                        String path = videoList.get(position).getPath();
                                        int indexOfPath = path.lastIndexOf("/");
                                        String line2 = "Địa Chỉ: " + path.substring(0,indexOfPath);
                                        String line3 = "Kích Thước: " + android.text.format.Formatter
                                                        .formatFileSize(context, Long.parseLong(videoList.get(position).getSize()));
                                        String line4 = "Độ Dài: " + timeConversion((long) milliSeconds);
                                        String namewithFormat  = videoList.get(position).getDisplayName();
                                        int index = namewithFormat.lastIndexOf(".");
                                        String format = namewithFormat.substring(index + 1);
                                        String line5 = "Dịnh Dạng: " + format;

                                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                                        mediaMetadataRetriever.setDataSource(videoList.get(position).getPath());
                                        String height = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                                        String width = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                                        String line6 = "Độ Lớn: " + width + "x" + height;

                                        alertDialog.setMessage(
                                                line1 + "\n\n" + line2 + "\n\n" + line3 + "\n\n" + line4 + "\n\n" +
                                                        line5 + "\n\n" + line6
                                        );
                                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        alertDialog.show();
                                        bottomSheetDialog.dismiss();
                                    }
                                });
                bottomSheetDialog.setContentView(bsView);
                bottomSheetDialog.show();
            }
        });
        // ấn vô video thì phát video
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("video_title", videoList.get(position).getDisplayName());
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("videoArrayList", videoList);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbbail, menu_more;
        TextView videoName, videoSize, videoDuration;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            thumbbail = itemView.findViewById(R.id.thumbnail);
            menu_more = itemView.findViewById(R.id.more);
            videoName = itemView.findViewById(R.id.video_name);
            videoSize = itemView.findViewById(R.id.video_size);
            videoDuration = itemView.findViewById(R.id.video_duration);
        }
    }
    public String timeConversion(long value){
        String videoTime;
        int duration = (int) value;
        int hrs = (duration/3600000);
        int mns = (duration/60000) % 60000;
        int scs = duration%60000/1000;
        if(hrs > 0 ){
            videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        }else{
            videoTime = String.format("%02d:%02d", mns, scs);
        }
        return videoTime;
    }
    void updateVideoFile(ArrayList<MediaFile> files) {
        videoList = new ArrayList<>();
        videoList.addAll(files);
        notifyDataSetChanged();
    }
}
