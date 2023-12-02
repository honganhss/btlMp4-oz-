# btlMp4
1.AndroidManifest - check icon, tổng quan, theme, activity 
	build.gradle.kts: các thư viện dc sử dụng trong app
2.AllowPermission, activity_allow_permission.xml - bố cục, về cấp quyền 
3.MediaFile.java 
	- tạo class đối tượng MediaFile vs các thuộc tính cơ bản, contructor, setter getter các kiểu 
	- implements Parcelable để đối tượng của lớp đó có thể được đóng gói và truyền giữa các thành phần của ứng dụng bằng cách sử dụng Intent.
4. MainActivity.java, activity_main.xml, VdAdapter.java :Hiển thị danh sách thư mục
5. VideoFileActivity.java, activity_video_file.xml, FileVdAdapter.java: Hiện danh sách video 
6. VidoePlayerActivity.java 
