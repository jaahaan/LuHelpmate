package com.example.luhelpmate.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.luhelpmate.R;
import com.example.luhelpmate.SliderAdapter;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;

public class AboutActivity extends AppCompatActivity {

    SliderView sliderView;
    int[] images = {R.drawable.view1,
            R.drawable.view2,
            R.drawable.view3,
            R.drawable.view4,
            R.drawable.view5,
            R.drawable.view6,
            R.drawable.view7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        sliderView = findViewById(R.id.image_slider);

        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.FILL);
        sliderView.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        sliderView.startAutoCycle();

    }
}