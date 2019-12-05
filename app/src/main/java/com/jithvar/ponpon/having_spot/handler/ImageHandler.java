package com.jithvar.ponpon.having_spot.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by KinG on 17-12-2017.
 * Created by ${EMAIL}.
 */

public class ImageHandler implements Serializable {
    private transient Bitmap mapSnapShot;
    private transient Bitmap bitmap;

    public ImageHandler(Bitmap bitmap) {
        this.mapSnapShot = bitmap;
    }

    public Bitmap getMapSnapShot() {


        return mapSnapShot;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        // This will serialize all fields that you did not mark with 'transient'
        // (Java's default behaviour)
        oos.defaultWriteObject();
        // Now, manually serialize all transient fields that you want to be serialized
        if (mapSnapShot != null) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            boolean success = mapSnapShot.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            if (success) {
                oos.writeObject(byteStream.toByteArray());
            }
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        // Now, all again, deserializing - in the SAME ORDER!
        // All non-transient fields
        ois.defaultReadObject();
        // All other fields that you serialized
        byte[] image = (byte[]) ois.readObject();
        if (image != null && image.length > 0) {
            mapSnapShot = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
    }

}
