package com.bigdipper.chj.bigdipperv1.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bigdipper.chj.bigdipperv1.Manifest;
import com.bigdipper.chj.bigdipperv1.R;
import com.bigdipper.chj.bigdipperv1.model.PhoneBookModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chj on 2017-12-29.
 */

public class InfomationFragment extends Fragment implements View.OnClickListener {

    private List<PhoneBookModel> phoneBookModels;
    private FirebaseDatabase firebaseDatabase;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infomation,container,false);

        firebaseDatabase = FirebaseDatabase.getInstance();





        Button commentButton = (Button)view.findViewById(R.id.infomationFragment_button_comment);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(view.getContext());
            }
        });
        Button phoneBookButton = (Button)view.findViewById(R.id.infomationFragment_button_phonebook);
        phoneBookButton.setOnClickListener(this);




        return view;
    }



    void showDialog(Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_comment,null);
        final EditText editText = (EditText)view.findViewById(R.id.commentDialog_edittext);
        builder.setView(view)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Map<String,Object> stringObjectMap = new HashMap<>();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                stringObjectMap.put("comment",editText.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(stringObjectMap);
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        //AlertDialog dialog = builder.create();
        //dialog.getWindow().getAttributes().windowAnimations = R.style.CustomAnim;

        builder.show();
    }

    //주소록동기화
    @Override
    public void onClick(View view) {
        List<PhoneBookModel.Id> phoneList = new ArrayList<>();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Cursor cursor = view.getContext().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,null,null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " asc"
        );

        String[] arrPhone = {ContactsContract.CommonDataKinds.Phone.NUMBER};

        while (cursor.moveToNext()){
            PhoneBookModel.Id phoneId = new PhoneBookModel.Id();

            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

            phoneId.id = id;
            phoneId.name = name;
            //폰
            Cursor phoneC = view.getContext().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrPhone,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null, null
            );
            while (phoneC.moveToNext()){
                phoneId.phoneNumber = phoneC.getString(0);
            }
            phoneC.close();
            //이메일
            Cursor emailC = view.getContext().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    arrPhone,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id,
                    null, null
            );
            while (emailC.moveToNext()){
                phoneId.email = emailC.getString(0);
            }
            emailC.close();
            //노트
            String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " +ContactsContract.Data.MIMETYPE + " = ?";
            String[] noteParams = new String[]{
                    cursor.getString(0),
                    ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
            Cursor noteC = view.getContext().getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    noteWhere,
                    noteParams, null
            );
            while (noteC.moveToNext()){
                phoneId.note = noteC.getString(noteC.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
            }
            noteC.close();
            //주소
            String addWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            String[] addParams = new String[]{
                    cursor.getString(0),
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
            Cursor addC = view.getContext().getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    addWhere,
                    addParams, null
            );
            while (addC.moveToNext()){
                phoneId.adrPoBox = addC.getString(addC.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                phoneId.adrStreet = addC.getString(addC.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                phoneId.adrCity = addC.getString(addC.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                phoneId.adrRegion = addC.getString(addC.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                phoneId.adrPostCode = addC.getString(addC.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                phoneId.adrCountry = addC.getString(addC.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                phoneId.adrType = addC.getString(addC.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
            }
            addC.close();
            //회사,직급
            String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
            String[] orgParams = new String[]{
                    cursor.getString(0),
                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
            Cursor orgC = view.getContext().getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    orgWhere,
                    orgParams, null
            );
            while (orgC.moveToNext()){
                phoneId.office = addC.getString(addC.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                phoneId.officeTitle = addC.getString(addC.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
            }
            orgC.close();

//            phoneList.add(phoneId);
            firebaseDatabase.getReference().child("users").child(uid).child("phonebook").push().setValue(phoneId);
        }
        cursor.close();


    }
}
