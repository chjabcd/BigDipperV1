package com.bigdipper.chj.bigdipperv1.fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigdipper.chj.bigdipperv1.MainActivity;
import com.bigdipper.chj.bigdipperv1.R;
import com.bigdipper.chj.bigdipperv1.model.PhoneBookModel;
import com.bigdipper.chj.bigdipperv1.model.UserModel;
import com.bigdipper.chj.bigdipperv1.people.InfoActivity;
import com.bigdipper.chj.bigdipperv1.service.SoundSearcher;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.swipe.SwipeLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chj on 2017-12-27.
 */

public class PeopleFragment extends Fragment{

    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    RecyclerView.Adapter adapter;
    Toolbar toolbar;
    TextView title;
    TextView toolbarCount;
    SearchView searchView;
    ArrayList<PhoneBookModel.Id> arrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people,container,false);
        adapter = new PeopleFragmentRecyclerViewAdapter();
        recyclerView = (RecyclerView)view.findViewById(R.id.peoplefragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        setHasOptionsMenu(true);

//        toolbar = (Toolbar)view.findViewById(R.id.toolbar_people);
//        drawerLayout = (DrawerLayout)view.findViewById(R.id.fragment_people_drawerlayout);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
//        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbarCount = (TextView)view.findViewById(R.id.toolbar_people_count);
//        int count = recyclerView.getAdapter().getItemCount();



//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity,drawerLayout,toolbar,0,0);
//        drawerLayout.setDrawerListener(toggle);
//        toggle.syncState();

//        final NavigationView navigationView = (NavigationView)view.findViewById(R.id.fragment_people_navigationview_right);
//        toolbar.findViewById(R.id.toolbar_people_email).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.openDrawer(GravityCompat.END);
//            }
//        });
//        navigationView.bringChildToFront(view);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                drawerLayout.closeDrawer(GravityCompat.END);
//
//                return true;
//            }
//        });

        searchView = (SearchView)view.findViewById(R.id.toolbar_people_search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                ArrayList<PhoneBookModel.Id> newList = new ArrayList<>();
                for(PhoneBookModel.Id name : arrayList){
                    String nameN = name.name.toLowerCase();
                    if(nameN.contains(newText)){
                        newList.add(name);
                    }
                }
                return false;
            }
        });


        return view;
    }


    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<UserModel> userModels;
        List<PhoneBookModel.Id> phoneBooks;
        List<PhoneBookModel.Id> phoneBooksFilter;
        public PeopleFragmentRecyclerViewAdapter() {
            userModels = new ArrayList<>();
            phoneBooks = new ArrayList<>();
            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userModels.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        UserModel userModel = snapshot.getValue(UserModel.class);
                        if(userModel.uid.equals(myUid)){
                            continue;
                        }
                        if(userModel.phonebook != null){

                        }
                        userModels.add(userModel);
                        System.out.println(userModels.size());
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

//            FirebaseDatabase.getInstance().getReference().child("users").child(myUid).child("phonebook").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    phoneBooks.clear();
//                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                        PhoneBookModel.Id phoneBook = snapshot.getValue(PhoneBookModel.Id.class);
//                        phoneBook.uid = snapshot.getKey();
//                        phoneBooks.add(phoneBook);
//
//                    }
//                    notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);

            SwipeLayout item = (SwipeLayout) view.findViewById(R.id.frienditem_swipe);
            item.setShowMode(SwipeLayout.ShowMode.PullOut);
            item.addDrag(SwipeLayout.DragEdge.Left, item.findViewById(R.id.bottom_wrapper));
            item.addDrag(SwipeLayout.DragEdge.Right, item.findViewById(R.id.bottom_wrapper_2));

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            Glide.with
                    (holder.itemView.getContext())
                    .load(userModels.get(position).profileImageUrl)
                    .apply(new RequestOptions().circleCrop())
                    .into(((CustomViewHolder)holder).imageView);
            ((CustomViewHolder)holder).textViewId.setText(userModels.get(position).userName);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), InfoActivity.class);
                    intent.putExtra("destinationUid",userModels.get(position).uid);
                    ActivityOptions activityOptions = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromtop,R.anim.tostop);
                        startActivity(intent,activityOptions.toBundle());
                    }
//                    showDialog(view.getContext());
//                    getFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout_info,new InfoFragment()).commit();

                }
            });
            if(userModels.get(position).comment != null) {
                ((CustomViewHolder) holder).textView_comment.setText(userModels.get(position).comment);
            }


//            ((CustomViewHolder)holder).imageView.setImageResource(R.drawable.bigdipper);
//            ((CustomViewHolder)holder).textViewId.setText(phoneBooks.get(position).name);
//            ((CustomViewHolder)holder).textViewPhone.setText(phoneBooks.get(position).phoneNumber);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(view.getContext(), InfoActivity.class);
//                    intent.putExtra("destinationUid",userModels.get(position).uid);
//                    ActivityOptions activityOptions = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromtop,R.anim.tostop);
//                        startActivity(intent,activityOptions.toBundle());
//                    }
//                }
//            });
            ((CustomViewHolder) holder).imageView_hello.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "hello", Toast.LENGTH_SHORT).show();
                }
            });
            ((CustomViewHolder) holder).imageView_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "chat", Toast.LENGTH_SHORT).show();
                }
            });
            ((CustomViewHolder) holder).imageView_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "call", Toast.LENGTH_SHORT).show();
                }
            });
            ((CustomViewHolder) holder).imageView_sns.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "sns", Toast.LENGTH_SHORT).show();
                }
            });
            ((CustomViewHolder) holder).imageView_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "chat", Toast.LENGTH_SHORT).show();
                }
            });
            ((CustomViewHolder) holder).imageView_hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "hide", Toast.LENGTH_SHORT).show();
                }
            });
            ((CustomViewHolder) holder).imageView_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), InfoActivity.class);
                    intent.putExtra("destinationUid",userModels.get(position).uid);
                    ActivityOptions activityOptions = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromtop,R.anim.tostop);
                        startActivity(intent,activityOptions.toBundle());
                    }
                }
            });



        }

        @Override
        public int getItemCount() {
            return userModels.size();
        }


        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textViewId;
            public TextView textViewPhone;
            public TextView textView_comment;
            public ImageView imageView_hello,imageView_chat,imageView_call,imageView_sns,imageView_hide,imageView_info;

            public CustomViewHolder(View view) {
                super(view);
                imageView = (ImageView)view.findViewById(R.id.frienditem_imageview);
                textViewId = (TextView)view.findViewById(R.id.frienditem_id);
                textViewPhone = (TextView)view.findViewById(R.id.frienditem_phone);
                textView_comment = (TextView)view.findViewById(R.id.frienditem_textview_comment);
                imageView_hello = (ImageView)view.findViewById(R.id.frienditem_hello);
                imageView_chat = (ImageView)view.findViewById(R.id.frienditem_chat);
                imageView_call = (ImageView)view.findViewById(R.id.frienditem_call);
                imageView_sns = (ImageView)view.findViewById(R.id.frienditem_sns);
                imageView_hide = (ImageView)view.findViewById(R.id.frienditem_hide);
                imageView_info = (ImageView)view.findViewById(R.id.frienditem_infomation);
            }
        }
        public void setFilter(ArrayList<PhoneBookModel.Id> newList){
            phoneBooksFilter = new ArrayList<>();
            phoneBooksFilter.addAll(newList);
            notifyDataSetChanged();
        }
    }


    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.navigationview_main_drawer_people_left,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    void showDialog(Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context,android.R.style.Theme_Holo_Light);
//
//        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
//        View view = layoutInflater.inflate(R.layout.dialog_info,null);
//
//        builder.setView(view);
//
//        AlertDialog dialog = builder.create();
//        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomAnim;
//        dialog.show();
//    }



//    FirebaseDatabase.getInstance().getReference().child("users").child(myUid).child("phonebook").addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                UserModel userModel = snapshot.getValue(UserModel.class);
//                userModels.add(userModel);
//            }
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    });


}
