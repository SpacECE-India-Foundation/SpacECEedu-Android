package com.spacECE.spaceceedu.LibForSmall;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.spacECE.spaceceedu.R;

public class AddBooks extends Fragment {

    ImageView pickBookImg;

    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_books, container, false);
        v.setBackgroundColor(Color.WHITE);

        pickBookImg = v.findViewById(R.id.imageView_photo_add_book);

        // Initialize the result launcher
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        pickBookImg.setImageURI(uri);
                    }
                });

        pickBookImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AddBooks.this)
                        .crop()                  // Crop image (Optional), Check Customization for more options
                        .compress(1024)          // Final image size will be less than 1 MB (Optional)
                        .maxResultSize(1080, 1080)   // Final image resolution will be less than 1080 x 1080 (Optional)
                        .createIntent(intent -> {
                            resultLauncher.launch(intent);
                            return null;
                        });
            }
        });

        return v;
    }
}
