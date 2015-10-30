public void downloadImageUsingThread(String url) {
	/*
		@url is the url to download url
	*/
		// boolean succcessfull = false;
		// manifest needed permission <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	URL downloadURL = null;
	HttpURLConnection connection = null;
	InputStream inputStream = null;
	FileOutputStream fileOutputStream = null;
	File file = null;
	try {
		downloadURL = new URL(url);
		// open a connection
		connection = (HttpURLConnection)downloadURL.openConnection();
		inputStream = coonection.getInputStream();
		
		// this will give the external folder storage on the sd card and this will gives you @SD_CARD/PICTURES_NAME and @URL.parse will gave you the name of image ex .jpg images
		file = new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+Uri.parse(url).getLastPathSegment());
		file.getAbsolutePath(); // this will gave you a entire file location this is not require just for a test
		fileOutputStream = new FileOutputStream(file);
		int read = -1; // assuming that no data read
		byte[] buffer = new byte(1024);
		while((read = inputStream.read(buffer) != -1)  {
			fileOutputStream.write(buffer, 0, read);
		}
		// successful = true;
	} catch() {

	} catch() {

	} finally {
		if(connection != null) {
				// conserving resource 
			connection.disconnect();
		}
		if(inputStream != null) {
			inputStream.close(); // sorround this with try catch
		}
		if(fileOutputStream != null) {
			fileOutputStream.close(); // sorround this with try catch
		}
	}

	//return successfull;
}
private class DownloadImagesThread implements Runnable {
			String url;
			public DownloadImagesThread(String url) {
				this.url = url;
			}
			public void run() {
					downloadImageUsingThread("some url");
			}

	}
// in the onclick download or your listener to download @example
public void click(View view) {
		Thread myThread = new Thread(new DownloadImagesThread(<pass the url image here>));
		myThread.start();
}