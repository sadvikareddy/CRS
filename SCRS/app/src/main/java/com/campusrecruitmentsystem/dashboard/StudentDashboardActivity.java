package com.campusrecruitmentsystem.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.campusrecruitmentsystem.MainActivity;
import com.campusrecruitmentsystem.R;
import com.campusrecruitmentsystem.Utils;
import com.campusrecruitmentsystem.databinding.ActivityStudentDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class StudentDashboardActivity extends AppCompatActivity {

    private ActivityStudentDashboardBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference rootRef;
    private NavController navController;
    private Utils.AccountType accountType;
    private AppBarConfiguration mAppBarConfiguration;
    private TextView userName;
    private Boolean isBlocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        accountType = (Utils.AccountType) getIntent().getSerializableExtra("accountType");
        isBlocked = getIntent().getBooleanExtra("isBlocked", false);
        if(isBlocked) binding.getRoot().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(getString(R.string.account_type), 1).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("isBlocked", isBlocked).apply();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_apply_for_jobs
                , R.id.nav_view_job_status
                , R.id.nav_update_details
                , R.id.nav_change_password)
                .setDrawerLayout(binding.drawerLayout)
                .build();

        navController = Navigation.findNavController(this, R.id.student_fragment_container_view_tag);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference().child("Student")
                .child(mAuth.getCurrentUser().getUid());

        userName = binding.navView.getHeaderView(0).findViewById(R.id.userName);
        TextView userEmailId = binding.navView.getHeaderView(0).findViewById(R.id.userEmailId);

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userName.setText(snapshot.child("Profile").child("full_name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                System.out.println(error);
            }
        });
        userEmailId.setText(currentUser.getEmail());

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.log_out_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.log_out){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Do you want to log out?").setTitle("LOG OUT")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAuth.getInstance().signOut();
                            startActivity(new Intent(StudentDashboardActivity.this,
                                    MainActivity.class));
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.student_fragment_container_view_tag);
        if(isBlocked)
            return true;
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void updateUserName(String name){
        userName.setText(name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}