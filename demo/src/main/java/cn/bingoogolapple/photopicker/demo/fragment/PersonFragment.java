package cn.bingoogolapple.photopicker.demo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.bingoogolapple.photopicker.demo.Config.EditLayout;
import cn.bingoogolapple.photopicker.demo.R;
import cn.bingoogolapple.photopicker.demo.activity.mywupingActivity;
import cn.bingoogolapple.photopicker.demo.activity.personinfoActivity;

public class PersonFragment extends Fragment {
    private EditLayout editLayout1;
    private EditLayout editLayout2;
    private EditLayout editLayout3;
    private EditLayout editLayout4;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragement_person,container,false);
        editLayout1 =(EditLayout) view.findViewById(R.id.asd1);
        editLayout2 =(EditLayout) view.findViewById(R.id.asd2);
        editLayout3 =(EditLayout) view.findViewById(R.id.asd3);
        editLayout4 =(EditLayout) view.findViewById(R.id.asd4);
        editLayout1.setOnEditLayoutEventLisenter(new EditLayout.onEditLayoutEventLisenter() {
            @Override
            public void onClickItem(String content) {

                Context context=getActivity();
                Intent i=new Intent(context,personinfoActivity.class);
                context.startActivity(i);
            }
        });
        editLayout2.setOnEditLayoutEventLisenter(new EditLayout.onEditLayoutEventLisenter() {
            @Override
            public void onClickItem(String content) {
                Context context=getActivity();
                Intent i=new Intent(context,mywupingActivity.class);
                context.startActivity(i);

            }
        });
        editLayout3.setOnEditLayoutEventLisenter(new EditLayout.onEditLayoutEventLisenter() {
            @Override
            public void onClickItem(String content) {

            }
        });
        editLayout4.setOnEditLayoutEventLisenter(new EditLayout.onEditLayoutEventLisenter() {
            @Override
            public void onClickItem(String content) {

            }
        });
        return view;
    }
}
