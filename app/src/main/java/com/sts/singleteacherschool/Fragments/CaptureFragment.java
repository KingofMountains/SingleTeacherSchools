package com.sts.singleteacherschool.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sts.singleteacherschool.Listeners.OnFragmentInteractionListener;
import com.sts.singleteacherschool.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.sts.singleteacherschool.ReportFormActivity.data;

public class CaptureFragment extends Fragment {

    private static final String DIR_NAME = ".STS";
    private static final int CAMERA_REQUEST = 101;
    private static String FILE_PATH = "";
    private static String DIR_PATH = "";
    View v;
    ImageView imgOne, imgTwo, imgThree, imgFour;
    private OnFragmentInteractionListener mListener;
    private int clickedImage = 0;
    Activity thisActivity;

    public CaptureFragment() {
        // Required empty public constructor
    }

    public static CaptureFragment newInstance() {
        CaptureFragment fragment = new CaptureFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_capture, container, false);
        init();
        return v;
    }

    private void init() {

        thisActivity = getActivity();

        DIR_PATH = Environment.getExternalStorageDirectory() + File.separator + DIR_NAME + File.separator;

        imgOne = (ImageView) v.findViewById(R.id.imageView01);
        imgTwo = (ImageView) v.findViewById(R.id.imageView02);
        imgThree = (ImageView) v.findViewById(R.id.imageView03);
        imgFour = (ImageView) v.findViewById(R.id.imageView04);

        imgOne.setOnClickListener(new OnImageClick());
        imgTwo.setOnClickListener(new OnImageClick());
        imgThree.setOnClickListener(new OnImageClick());
        imgFour.setOnClickListener(new OnImageClick());

        v.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onFragmentInteraction("submit");
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            long currentTimestamp = System.currentTimeMillis();
            FILE_PATH = DIR_PATH + "sts_image_" + currentTimestamp + ".jpg";

            try {
                Bitmap photo = BitmapFactory.decodeFile(DIR_PATH + "temp.jpg");
                File outFile = new File(FILE_PATH);
                if (!outFile.exists())
                    outFile.createNewFile();
                ExifInterface exif = new ExifInterface(DIR_PATH + "temp.jpg");
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                if (orientation == 6) {

                }
                FileOutputStream fos = new FileOutputStream(outFile);

                if (orientation == 6) {
                    photo = rotateImage(photo, 90);
                }

                if (photo.getWidth() < photo.getHeight()) {
                    photo = Bitmap.createScaledBitmap(photo, 600, 800, false);
                } else {
                    photo = Bitmap.createScaledBitmap(photo, 800, 600, false);
                }

                photo.compress(Bitmap.CompressFormat.JPEG, 75, fos);

                System.out.println("orientation ----------- " + orientation);
                fos.flush();
                fos.close();
                setImageToImageView(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Bitmap rotateImage(Bitmap src, int degree) {

        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);

        return bmp;
    }

    private void setImageToImageView(Bitmap photo) {
        switch (clickedImage) {
            case R.id.imageView01:
                imgOne.setImageBitmap(photo);
                data.imageone = FILE_PATH;
                break;
            case R.id.imageView02:
                imgTwo.setImageBitmap(photo);
                data.imagetwo = FILE_PATH;
                break;
            case R.id.imageView03:
                imgThree.setImageBitmap(photo);
                data.imagethree = FILE_PATH;
                break;
            case R.id.imageView04:
                imgFour.setImageBitmap(photo);
                data.imagefour = FILE_PATH;
                break;
        }
        clickedImage = 0;
        FILE_PATH = "";
    }

    private class OnImageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            clickedImage = v.getId();

            Uri outputFileUri;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            outputFileUri = Uri.fromFile(new File(DIR_PATH + "temp.jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.putExtra("return-data", true);
            getActivity().startActivityFromFragment(CaptureFragment.this, intent, CAMERA_REQUEST);
        }
    }
}
