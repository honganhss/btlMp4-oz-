package com.example.btlmp4.Folder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btlmp4.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VdAdapter extends RecyclerView.Adapter<VdAdapter.ViewHolder> {
    private ArrayList<MediaFile> mediaFiles;
    private ArrayList<String> folderPath;
    private Context context;

    public VdAdapter(ArrayList<MediaFile> mediaFiles, ArrayList<String> folderPath, Context context) {
        this.mediaFiles = mediaFiles;
        this.folderPath = folderPath;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //Lấy dữ liệu từ arrayList
        int indexPath = folderPath.get(position).lastIndexOf("/");
        String nameFolder = folderPath.get(position).substring(indexPath+1);

        //Hiển thị dữ liệu lên màn hình
        holder.foldeName.setText(nameFolder);
        holder.folderPath.setText(folderPath.get(position));
        holder.noOfFile.setText(noOfFile(folderPath.get(position)) + " Videos");

        //Gói dữ liệu để chuyển sang activity mới khi click vào 1 item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoFileActivity.class);
                intent.putExtra("folderName", nameFolder);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return folderPath.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Khai báo các phần tử của folder_item trong giao diện 1 item
        TextView foldeName, folderPath, noOfFile;
        public ViewHolder(@NonNull @NotNull View itemView) {
            //Ánh xạ các phần tử
            super(itemView);
            foldeName = itemView.findViewById(R.id.folder_name);
            folderPath = itemView.findViewById(R.id.folder_path);
            noOfFile = itemView.findViewById(R.id.noOfFile);
        }
    }

    int noOfFile(String folder_name) {
        int filesNo = 0;
        for (MediaFile mediaFile : mediaFiles){
            //Kiểm tra xem đường dẫn tệp thuộc thư mục đang cần đếm số lượng tệp không ?
            if(mediaFile.getPath().substring(0, mediaFile.getPath().lastIndexOf("/")).endsWith(folder_name)) {
                filesNo ++;
            }
        }
        return filesNo;
    }
}
