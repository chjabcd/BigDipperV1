package com.bigdipper.chj.bigdipperv1.interest;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigdipper.chj.bigdipperv1.R;

/**
 * Created by chj on 2018-01-25.
 */

public class InterestDetailImageFragment extends Fragment {

    public static InterestDetailImageFragment newInstance(int imageUrl) {

        Bundle args = new Bundle();

        args.putInt("imageUrl",imageUrl);
        InterestDetailImageFragment fragment = new InterestDetailImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interest_detail_image,container,false);
        ImageView imageView = (ImageView)view.findViewById(R.id.fragment_interest_detail_image_imageview);
        imageView.setImageResource(getArguments().getInt("imageUrl"));
        return view;
    }
}
