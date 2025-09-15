package com.example.snhslink.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snhslink.AdminInterface.ViewStudentDetails;
import com.example.snhslink.R;
import com.example.snhslink.Models.StudentApplication;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class StudentAdapter extends FirestoreRecyclerAdapter<StudentApplication, StudentAdapter.StudentViewHolder> {

    private final Context context;

    public StudentAdapter(@NonNull FirestoreRecyclerOptions<StudentApplication> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull StudentViewHolder holder, int position, @NonNull StudentApplication model) {
        // Get the Firestore document ID (userID)
        String userID = getSnapshots().getSnapshot(position).getId();

        // Retrieve student first and last name from Firestore
        getSnapshots().getSnapshot(position).getReference().collection("basic_information")
                .document("data").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String firstName = documentSnapshot.getString("firstName");
                        String lastName = documentSnapshot.getString("lastName");
                        holder.studentName.setText(firstName + " " + lastName);
                    } else {
                        holder.studentName.setText("Unknown Student");
                    }
                });

        // Retrieve application status from Firestore
        getSnapshots().getSnapshot(position).getReference().collection("application_status")
                .document("status").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String status = documentSnapshot.getString("applicationStatus");
                        holder.applicationStatus.setText("Status: " + (status != null ? status : "Pending"));
                    } else {
                        holder.applicationStatus.setText("Status: Pending");
                    }
                });

        // Click listener to open student details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewStudentDetails.class);
            intent.putExtra("userID", userID);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, applicationStatus;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.text_student_name);
            applicationStatus = itemView.findViewById(R.id.text_application_status);
        }
    }
}
