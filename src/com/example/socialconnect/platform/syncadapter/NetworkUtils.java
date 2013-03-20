package com.example.socialconnect.platform.syncadapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

public class NetworkUtils {
	
	private static final String TAG = "NetworkUtils";

	public static byte[] downloadAvatar(final String avatarUrl) {
		// If there is no avatar, we're done
		if (TextUtils.isEmpty(avatarUrl)) {
			return null;
		}

		try {
			Log.i(TAG, "Downloading avatar: " + avatarUrl);
			// Request the avatar image from the server, and create a bitmap
			// object from the stream we get back.
			URL url = new URL(avatarUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			try {
				final BitmapFactory.Options options = new BitmapFactory.Options();
				final Bitmap avatar = BitmapFactory.decodeStream(connection.getInputStream(), null,
						options);

				// Take the image we received from the server, whatever format
				// it
				// happens to be in, and convert it to a JPEG image. Note: we're
				// not resizing the avatar - we assume that the image we get
				// from
				// the server is a reasonable size...
				Log.i(TAG, "Converting avatar to JPEG");
				ByteArrayOutputStream convertStream = new ByteArrayOutputStream(avatar.getWidth()
						* avatar.getHeight() * 4);
				avatar.compress(Bitmap.CompressFormat.JPEG, 95, convertStream);
				convertStream.flush();
				convertStream.close();
				// On pre-Honeycomb systems, it's important to call recycle on
				// bitmaps
				avatar.recycle();
				return convertStream.toByteArray();
			} finally {
				connection.disconnect();
			}
		} catch (MalformedURLException muex) {
			// A bad URL - nothing we can really do about it here...
			Log.e(TAG, "Malformed avatar URL: " + avatarUrl);
		} catch (IOException ioex) {
			// If we're unable to download the avatar, it's a bummer but not the
			// end of the world. We'll try to get it next time we sync.
			Log.e(TAG, "Failed to download user avatar: " + avatarUrl);
		}
		return null;
	}
}
