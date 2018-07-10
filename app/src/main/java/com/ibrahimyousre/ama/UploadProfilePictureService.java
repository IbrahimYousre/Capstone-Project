package com.ibrahimyousre.ama;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.ibrahimyousre.ama.data.Repository;

import java.io.ByteArrayOutputStream;

import timber.log.Timber;

import static com.ibrahimyousre.ama.data.DatabaseConstants.PATH_USERS;
import static com.ibrahimyousre.ama.util.Constants.EXTRA_USER_ID;

public class UploadProfilePictureService extends IntentService {

    FirebaseStorage firebaseStorage;

    public UploadProfilePictureService() {
        super("UploadProfilePictureService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            final String userId = intent.getStringExtra(EXTRA_USER_ID);
            Bitmap bitmap = (Bitmap) intent.getExtras().get("data");
            if (bitmap == null) {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
                int scale = 2;
                while (scale * 153 < bitmap.getWidth() && scale * 204 < bitmap.getHeight())
                    scale *= 2;
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(intent.getData()), null, o2);
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            UploadTask uploadTask = firebaseStorage.getReference().child(userId)
                    .putBytes(bytes.toByteArray());
            Tasks.await(uploadTask);
            Task<Uri> uriTask = uploadTask.getResult().getStorage().getDownloadUrl();
            Tasks.await(uriTask);
            String imageUrl = uriTask.getResult().toString();
            Task<Void> updateTask = Repository.getInstance().getFirebaseDatabase().getReference(PATH_USERS)
                    .child(userId).child("photoUrl").setValue(imageUrl);
            Tasks.await(updateTask);
        } catch (Throwable t) {
            Timber.e(t);
        }
    }
}
