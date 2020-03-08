package com.brocode.saviour;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class Disease extends Fragment {
    /*  */
    private static final int PICK_IMAGE = 1;
    private static final int MAX_LENGTH = 100;
    private Uri imageuri;
    private Result result;
    private int mInputsize=224;
    private String mModelPath = "disease_trained_model_brocode.tflite";
    private String mLabelPath = "disease_output_brocode.txt";
    ImageView selectImage;
    ImageView uploadiv;
    Button search;
    TextView clear;
    TextView Selecttv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disease, container, false);

        imageuri=null;
        selectImage=view.findViewById(R.id.selectimage);
        search=view.findViewById(R.id.searchbutton);
        clear=view.findViewById(R.id.clearbtn);
        Selecttv=view.findViewById(R.id.TV_select);
        uploadiv=view.findViewById(R.id.uploadIV);

        AssetManager assetManager = getResources().getAssets();
        result =new Result(assetManager,mModelPath,mLabelPath,mInputsize);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(getContext(),Disease.this);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage.setImageResource(R.color.grey);
                Selecttv.setVisibility(View.VISIBLE);
                uploadiv.setVisibility(View.VISIBLE);
                imageuri=null;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageuri!=null){
                    clear.setVisibility(View.INVISIBLE);

                    Bitmap bitmap;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageuri);
                        if(result.recognizeImage(bitmap).size()>0){
                            Result.Recognition data = result.recognizeImage(bitmap).get(0);
//                            tv.setText(data.getTitle()+"\n Accuracy:"+data.getConfidence());

                            Intent intent =new Intent(getContext(),DiseaseDetection.class);
                            intent.putExtra("disease",data.getTitle());
                            intent.putExtra("accuracy",String.valueOf(data.getConfidence()));
                            startActivity(intent);

                        }else{
//                            tv.setText("n/a"+"\n Accuracy: n/a");
                            Intent intent =new Intent(getContext(),DiseaseDetection.class);
                            intent.putExtra("disease","n/a");
                            intent.putExtra("accuracy","n/a");
                            startActivity(intent);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    tv.setVisibility(View.VISIBLE);
                    selectImage.setImageResource(R.color.grey);
                    Selecttv.setVisibility(View.VISIBLE);
                    uploadiv.setVisibility(View.VISIBLE);
                    imageuri=null;
                    clear.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getActivity(), "please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageuri = result.getUri();
                selectImage.setImageURI(imageuri);
                Selecttv.setVisibility(View.INVISIBLE);
                uploadiv.setVisibility(View.INVISIBLE);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }
        }
    }


}
