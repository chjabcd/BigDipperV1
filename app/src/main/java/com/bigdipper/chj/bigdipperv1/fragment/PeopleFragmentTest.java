//package com.bigdipper.chj.bigdipperv1.fragment;
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.NavigationView;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bigdipper.chj.bigdipperv1.MainActivity;
//import com.bigdipper.chj.bigdipperv1.R;
//import com.bigdipper.chj.bigdipperv1.model.EventItem;
//import com.bigdipper.chj.bigdipperv1.model.HeaderModel;
//import com.bigdipper.chj.bigdipperv1.model.ListModel;
//import com.bigdipper.chj.bigdipperv1.model.PhoneBookModel;
//import com.bigdipper.chj.bigdipperv1.service.SoundSearcher;
//import com.daimajia.swipe.SwipeLayout;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by chj on 2017-12-27.
// */
//
//public class PeopleFragmentTest extends Fragment{
//
//    RecyclerView recyclerView;
//    DrawerLayout drawerLayout;
//    Toolbar toolbar;
//    TextView toolbarCount;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_people,container,false);
//        recyclerView = (RecyclerView)view.findViewById(R.id.peoplefragment_recyclerview);
//        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
//        recyclerView.setAdapter(new PeopleFragmentRecyclerViewAdapter());
//        recyclerView.setHasFixedSize(true);
//
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//
//        setHasOptionsMenu(true);
//
//        toolbar = (Toolbar)view.findViewById(R.id.toolbar_people);
//        drawerLayout = (DrawerLayout)view.findViewById(R.id.fragment_people_drawerlayout);
//        MainActivity activity = (MainActivity)getActivity();
//        activity.setSupportActionBar(toolbar);
//        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbarCount = (TextView)view.findViewById(R.id.toolbar_people_count);
//        int count = recyclerView.getAdapter().getItemCount();
//
//        toolbarCount.setText(""+ count +"");
//        System.out.println(count +"");
//
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity,drawerLayout,toolbar,0,0);
//        drawerLayout.setDrawerListener(toggle);
//        toggle.syncState();
//
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
//
//        return view;
//    }
//
//    class PeopleFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
//
//        List<PhoneBookModel.Id> phoneBooks;
//        List<ListModel> models;
//        public PeopleFragmentRecyclerViewAdapter() {
//            phoneBooks = new ArrayList<>();
//            final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            models = new ArrayList<>();
//
//            String[] stringName = {"ㄱ","ㄴ","ㄷ","ㄹ","ㅁ","ㅂ","ㅅ"};
//
//            for(int j=0;j<stringName.length;j++){
//                HeaderModel header = new HeaderModel();
//                header.setHeader(stringName[j]);
//                models.add(header);
//                FirebaseGetInstance(myUid,stringName[j]);
//            }
//
//            System.out.println("1  "+models.size());
//            System.out.println("2  "+phoneBooks.size());
//            FirebaseDatabase.getInstance().getReference().child("users").child(myUid).child("phonebook").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    phoneBooks.clear();
//                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                        PhoneBookModel.Id phoneBook = snapshot.getValue(PhoneBookModel.Id.class);
//                        phoneBooks.add(phoneBook);
//                    }
//                    notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//            System.out.println("3  "+phoneBooks.size());
//        }
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
////            if(viewType == ListModel.TYPE_HEADER){
////                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_header,parent,false);
////                return new CustomViewHolderHeader(view);
////            }else{
////                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);
////                SwipeLayout item = (SwipeLayout) view.findViewById(R.id.frienditem_swipe);
////                item.setShowMode(SwipeLayout.ShowMode.PullOut);
////                item.addDrag(SwipeLayout.DragEdge.Left, item.findViewById(R.id.bottom_wrapper));
////                item.addDrag(SwipeLayout.DragEdge.Right, item.findViewById(R.id.bottom_wrapper_2));
////                return new CustomViewHolder(view);
////            }
//
//
//
//
//
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return models.get(position).getType();
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//            int type = getItemViewType(position);
//            if(type == ListModel.TYPE_HEADER){
//                HeaderModel header = (HeaderModel)models.get(position);
//                ((CustomViewHolderHeader)holder).textView_header.setText(header.getHeader());
//            }else {
//
//                ((CustomViewHolder) holder).imageView.setImageResource(R.drawable.bigdipper);
//                ((CustomViewHolder) holder).textViewId.setText(phoneBooks.get(position).name);
//                ((CustomViewHolder) holder).textViewPhone.setText(phoneBooks.get(position).phoneNumber);
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        Intent intent = new Intent(view.getContext(), InfoActivity.class);
////                        intent.putExtra("destinationUid",userModels.get(position).uid);
////                        ActivityOptions activityOptions = null;
////                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
////                            activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromtop,R.anim.tostop);
////                            startActivity(intent,activityOptions.toBundle());
////                        }
//                    }
//                });
//            }
//
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return models.size();
//        }
//
//
//        private class CustomViewHolder extends RecyclerView.ViewHolder {
//            public ImageView imageView;
//            public TextView textViewId;
//            public TextView textViewPhone;
//            public TextView textView_comment;
//            public TextView textView_header;
//
//            public CustomViewHolder(View view) {
//                super(view);
//                imageView = (ImageView)view.findViewById(R.id.frienditem_imageview);
//                textViewId = (TextView)view.findViewById(R.id.frienditem_id);
//                textViewPhone = (TextView)view.findViewById(R.id.frienditem_phone);
//                textView_comment = (TextView)view.findViewById(R.id.frienditem_textview_comment);
//            }
//        }
//        private class CustomViewHolderHeader extends RecyclerView.ViewHolder {
//            public TextView textView_header;
//
//            public CustomViewHolderHeader(View view){
//                super(view);
//                textView_header = (TextView)view.findViewById(R.id.frienditem_header_id);
//            }
//        }
//        private void FirebaseGetInstance(String uid, final String firstName){
//            FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("phonebook").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    phoneBooks.clear();
//                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                        PhoneBookModel.Id phoneBook = snapshot.getValue(PhoneBookModel.Id.class);
//                        if(SoundSearcher.matchStringFirst(phoneBook.name,firstName)){
//                            EventItem item = new EventItem();
//                            item.setItem(phoneBook.name);
//                            phoneBooks.add(phoneBook);
//                            models.add(item);
//                        }
//
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
//        }
//    }
//
//
//    //    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        return super.onOptionsItemSelected(item);
////    }
////
////    @Override
////    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
////        inflater.inflate(R.menu.navigationview_main_drawer_people_left,menu);
////        super.onCreateOptionsMenu(menu, inflater);
////    }
//
////    void showDialog(Context context) {
////        AlertDialog.Builder builder = new AlertDialog.Builder(context,android.R.style.Theme_Holo_Light);
////
////        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
////        View view = layoutInflater.inflate(R.layout.dialog_info,null);
////
////        builder.setView(view);
////
////        AlertDialog dialog = builder.create();
////        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomAnim;
////        dialog.show();
////    }
//
//
//
////    FirebaseDatabase.getInstance().getReference().child("users").child(myUid).child("phonebook").addValueEventListener(new ValueEventListener() {
////        @Override
////        public void onDataChange(DataSnapshot dataSnapshot) {
////            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
////                UserModel userModel = snapshot.getValue(UserModel.class);
////                userModels.add(userModel);
////            }
////            notifyDataSetChanged();
////        }
////
////        @Override
////        public void onCancelled(DatabaseError databaseError) {
////
////        }
////    });
//
//
//}
