package com.example.snhslink.StudentInterface;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snhslink.BaseActivity;
import com.example.snhslink.BuildConfig;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;



import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnSuccessListener;


import com.example.snhslink.R;
import com.example.snhslink.Network.ImgBBApiClient;
import com.example.snhslink.Network.ImgBBApiService;
import com.example.snhslink.Network.ImgBBResponse;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// remove if unnecessary
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


import java.util.HashMap;
import java.util.Map;


import android.content.Context;
import android.database.Cursor;
import android.provider.OpenableColumns;




public class UploadStudentDocuments extends BaseActivity {

    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private ImgBBApiService imgBBApiService;

    // Securely retrieve API key from BuildConfig
    private static final String IMG_BB_API_KEY = BuildConfig.IMGBB_API_KEY;

    // For launching the image picker to select an image.
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), this::handleImagePicked);

    // Variables to track the current upload context.
    private String currentDocumentType;
    private TextView currentStatusText;
    private ImageView currentUploadIcon;
    private ProgressBar currentLoadingSpinner;

    // Declare constants
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_student_documents);

        // Initialize Firestore and Firebase Auth
        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        firestore.collection("students").get()
                .addOnSuccessListener(queryDocumentSnapshots -> Log.d("Firestore", "Firestore initialized successfully"))
                .addOnFailureListener(e -> Log.e("Firestore", "Firestore error: ", e));

        if (currentUser == null) {
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ImgBB API service
        imgBBApiService = ImgBBApiClient.getClient().create(ImgBBApiService.class);

        TextView textViewAdmissionFormLink = findViewById(R.id.textview_back_to_admission_form);
        Button submitButton = findViewById(R.id.button_submit_documents);
        submitButton.setOnClickListener(v -> handleSubmit());

        // Set up upload areas for different document types.
        setupUploadArea(R.id.upload_report_card_area_front, "Grade 10 Report Card (front)",
                R.id.upload_icon_report_card_front, R.id.loading_report_card_front, R.id.status_text_report_card_front);
        setupUploadArea(R.id.upload_report_card_area_back, "Grade 10 Report Card (back)",
                R.id.upload_icon_report_card_back, R.id.loading_report_card_back, R.id.status_text_report_card_back);
        setupUploadArea(R.id.upload_birth_certificate_area, "Birth Certificate",
                R.id.upload_icon_birth_certificate, R.id.loading_birth_certificate, R.id.status_text_birth_certificate);
        setupUploadArea(R.id.upload_id_picture_area, "I.D. Picture",
                R.id.upload_icon_id_picture, R.id.loading_id_picture, R.id.status_text_id_picture);

        // Admission Form Link
        textViewAdmissionFormLink.setOnClickListener(view -> navigateToAdmissionForm());
    }

    private void setupUploadArea(int layoutId, String documentType,
                                 int iconId, int loadingId, int statusTextId) {
        RelativeLayout uploadArea = findViewById(layoutId);
        ImageView uploadIcon = findViewById(iconId);
        ProgressBar loadingSpinner = findViewById(loadingId);
        TextView statusText = findViewById(statusTextId);

        // Handle click events on the upload area.
        uploadArea.setOnClickListener(v -> {
            checkIfDocumentExists(documentType, exists -> {
                if (exists) {
                    // If document exists in Firestore, show popup
                    fetchDocumentUrlAndShowPopup(documentType, statusText, uploadIcon, loadingSpinner);
                } else {
                    // Start upload process
                    currentDocumentType = documentType;
                    currentStatusText = statusText;
                    currentUploadIcon = uploadIcon;
                    currentLoadingSpinner = loadingSpinner;
                    startImageUpload();
                }
            });
        });
    }

    private void checkIfDocumentExists(String documentType, OnSuccessListener<Boolean> callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
            callback.onSuccess(false);
            return;
        }

        String userId = user.getUid();
        DocumentReference documentRef = db.collection("students")
                .document(userId)
                .collection("documents")
                .document(documentType);

        documentRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    boolean exists = documentSnapshot.exists() && documentSnapshot.contains("imageUrl");
                    callback.onSuccess(exists);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error checking document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onSuccess(false);
                });
    }

    /* private void isDocumentUploaded(String documentType, OnSuccessListener<Boolean> callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
            callback.onSuccess(false);
            return;
        }

        String userId = user.getUid();
        DocumentReference documentRef = db.collection("students")
                .document(userId)
                .collection("documents")
                .document(documentType);

        documentRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    boolean exists = documentSnapshot.exists() && documentSnapshot.contains("imageUrl");
                    callback.onSuccess(exists);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error checking document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onSuccess(false);
                });
    } */


    private void startImageUpload() {
        /* if (!hasStoragePermission()) {
            requestStoragePermission();
            return;
        }*/

        currentStatusText.setText("        Uploading...");
        currentUploadIcon.setVisibility(View.GONE); // Hide icon during upload
        currentLoadingSpinner.setVisibility(View.VISIBLE);
        imagePickerLauncher.launch("image/*");
    }

    private boolean hasStoragePermission() {
        return checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    private void handleImagePicked(Uri imageUri) {
        currentLoadingSpinner.setVisibility(View.GONE);

        if (imageUri != null) {
            try {
                /* if (!hasStoragePermission()) {
                    requestStoragePermission();
                    return;
                } */

                File imageFile = compressImage(imageUri); // Compress before upload

                if (imageFile != null && imageFile.exists()) { // ✅ Null & existence check
                    uploadToImgBB(imageFile);
                } else {
                    throw new Exception("File conversion failed");
                }

            } catch (Exception e) {
                currentStatusText.setText("Tap to upload");
                currentUploadIcon.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Failed to process image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            currentStatusText.setText("Tap to upload");
            currentUploadIcon.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadToImgBB(File imageFile) {
        if (imageFile.length() > 32 * 1024 * 1024) { // 32MB limit
            currentStatusText.setText("File too large. Max 32MB.");
            currentUploadIcon.setVisibility(View.VISIBLE);
            Toast.makeText(this, "File size exceeds limit!", Toast.LENGTH_SHORT).show();
            return;
        }

        currentStatusText.setText("        Uploading...");
        currentUploadIcon.setVisibility(View.GONE);
        currentLoadingSpinner.setVisibility(View.VISIBLE);

        // Prepare image file for upload
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);

        // Make API call
        Call<ImgBBResponse> call = imgBBApiService.uploadImage(IMG_BB_API_KEY, imagePart);
        call.enqueue(new Callback<ImgBBResponse>() {
            @Override
            public void onResponse(Call<ImgBBResponse> call, Response<ImgBBResponse> response) {
                currentLoadingSpinner.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    // ✅ Extract image URL & delete URL
                    String imageUrl = response.body().getData().getUrl();
                    String deleteUrl = response.body().getData().getDeleteUrl(); // ✅ Store full delete URL
                    String deleteHash = response.body().getData().getDeleteHash(); // ✅ Extract deleteHash

                    // ✅ Ensure all values exist before storing
                    if (imageUrl != null && deleteUrl != null && deleteHash != null) {
                        storeImageUrlInFirestore(currentDocumentType, imageUrl, deleteUrl, deleteHash);
                    } else {
                        currentStatusText.setText("Upload failed. Try again.");
                        currentUploadIcon.setVisibility(View.VISIBLE);
                        Toast.makeText(UploadStudentDocuments.this, "Error: Missing URL data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    currentStatusText.setText("Upload failed. Try again.");
                    currentUploadIcon.setVisibility(View.VISIBLE);
                    Toast.makeText(UploadStudentDocuments.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImgBBResponse> call, Throwable t) {
                currentLoadingSpinner.setVisibility(View.GONE);
                currentStatusText.setText("Upload failed. Try again.");
                currentUploadIcon.setVisibility(View.VISIBLE);
                Toast.makeText(UploadStudentDocuments.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void storeImageUrlInFirestore(String documentType, String imageUrl, String deleteUrl, String deleteHash) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        DocumentReference documentRef = db.collection("students")
                .document(userId)
                .collection("documents")
                .document(documentType);

        // Debugging: Check deleteUrl & deleteHash before storing
        if (deleteUrl == null || deleteUrl.isEmpty()) {
            Log.e("Firestore", "ERROR: deleteUrl is NULL before storing!");
            Toast.makeText(this, "Error: Delete URL is missing!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (deleteHash == null || deleteHash.isEmpty()) {
            Log.e("Firestore", "ERROR: deleteHash is NULL before storing!");
            Toast.makeText(this, "Error: Delete Hash is missing!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Data to store
        Map<String, Object> documentData = new HashMap<>();
        documentData.put("imageUrl", imageUrl);
        documentData.put("deleteUrl", deleteUrl);  // ✅ Store full delete URL
        documentData.put("deleteHash", deleteHash);
        documentData.put("uploadedAt", FieldValue.serverTimestamp());

        // Upload to Firestore
        documentRef.set(documentData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    currentStatusText.setText("Uploaded, tap to view");
                    currentUploadIcon.setImageResource(R.drawable.ic_check);
                    currentUploadIcon.setVisibility(View.VISIBLE);
                    Toast.makeText(UploadStudentDocuments.this, documentType + " uploaded successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    currentStatusText.setText("Upload failed. Try again.");
                    currentUploadIcon.setVisibility(View.VISIBLE);
                    Toast.makeText(UploadStudentDocuments.this, "Failed to store document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }




    private void fetchDocumentUrlAndShowPopup(String documentType,
                                              TextView statusText,
                                              ImageView uploadIcon,
                                              ProgressBar loadingSpinner) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        DocumentReference documentRef = db.collection("students")
                .document(userId)
                .collection("documents")
                .document(documentType);

        // Fetch document data
        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String imageUrl = documentSnapshot.getString("imageUrl");
                String deleteUrl = documentSnapshot.getString("deleteUrl"); // ✅ FIX: Use deleteUrl, NOT deleteHash

                if (imageUrl != null && deleteUrl != null) {
                    showImagePopup(imageUrl, deleteUrl, documentType, statusText, uploadIcon, loadingSpinner);
                } else {
                    Toast.makeText(this, "Error: Missing document data.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Document not found.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to fetch document: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }




    @SuppressLint("ClickableViewAccessibility")
    private void showImagePopup(String imageUrl, String deleteUrl, String documentType,
                                TextView statusText, ImageView uploadIcon, ProgressBar loadingSpinner) {
        Dialog dialog = new Dialog(this, R.style.TransparentPopup);
        dialog.setContentView(R.layout.popup_image_view);
        dialog.setCancelable(true);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
        }

        // Use PhotoView for zoomable images
        PhotoView photoView = dialog.findViewById(R.id.popup_image_view);
        Button deleteButton = dialog.findViewById(R.id.button_delete);
        Button backButton = dialog.findViewById(R.id.button_back);

        // Load image using Glide
        Glide.with(this).load(imageUrl).into(photoView);

        deleteButton.setOnClickListener(v -> {
            dialog.dismiss(); // Close popup

            // ✅ FIX: Ensure correct parameters are passed
            showDeleteConfirmationPopup(documentType, deleteUrl, statusText, uploadIcon);
        });

        backButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    private void showDeleteConfirmationPopup(String documentType, String deleteUrl,
                                             TextView statusText, ImageView uploadIcon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Document")
                .setMessage("Are you sure you want to delete this document?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser user = auth.getCurrentUser();

                    if (user == null) {
                        Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String userId = user.getUid();
                    DocumentReference documentRef = db.collection("students")
                            .document(userId)
                            .collection("documents")
                            .document(documentType);

                    // ✅ Fetch deleteHash from Firestore before deleting
                    documentRef.get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists() && documentSnapshot.contains("deleteHash")) {
                            String deleteHash = documentSnapshot.getString("deleteHash");

                            if (deleteHash != null) {
                                // ✅ Now delete from BOTH ImgBB & Firestore
                                deleteDocumentFromFirestore(documentType, statusText, uploadIcon);
                            } else {
                                Toast.makeText(statusText.getContext(), "Error: Delete hash not found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(statusText.getContext(), "Error: Document not found.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e ->
                            Toast.makeText(statusText.getContext(), "Error fetching document: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }






    private void deleteDocumentFromFirestore(String documentType, TextView statusText, ImageView uploadIcon) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        DocumentReference documentRef = db.collection("students")
                .document(userId)
                .collection("documents")
                .document(documentType);

        // ✅ Fetch the document from Firestore before deleting
        documentRef.get(Source.SERVER).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Log.d("Firestore", "Document Data: " + documentSnapshot.getData()); // Log full document data

                deleteDocumentFromFirestore(documentRef, documentType, statusText, uploadIcon);

                if (documentSnapshot.contains("deleteHash")) {
                    String deleteHash = documentSnapshot.getString("deleteHash");
                    Log.d("Firestore", "Extracted deleteHash: " + deleteHash);

                    if (deleteHash != null) {
                        // ✅ Now delete from ImgBB & Firestore
                        /*deleteImageFromImgBB(documentType, statusText, uploadIcon); // ✅ FIXED CALL
                        return; */

                    }
                }

            } else {
                Log.e("Firestore", "Document does not exist!");
                Toast.makeText(this, "Error: Document not found.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Failed to fetch document: " + e.getMessage());
            Toast.makeText(this, "Failed to fetch document.", Toast.LENGTH_SHORT).show();
        });
    }





    /*private void deleteImageFromImgBB(String documentType, TextView statusText, ImageView uploadIcon) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        DocumentReference documentRef = db.collection("students")
                .document(userId)
                .collection("documents")
                .document(documentType);

        // ✅ Fetch deleteHash from Firestore before deleting
        documentRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && documentSnapshot.contains("deleteHash")) {
                String deleteHash = documentSnapshot.getString("deleteHash");

                // ✅ Ensure deleteHash is valid before making API call
                if (deleteHash == null || deleteHash.isEmpty() || deleteHash.length() != 32) {
                    Log.e("ImgBB Debug", "ERROR: Invalid deleteHash. Length: " + (deleteHash != null ? deleteHash.length() : "NULL"));
                    Toast.makeText(UploadStudentDocuments.this, "Error: Invalid delete hash.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ✅ Correct API call order
                ImgBBApiService imgBBApiService = ImgBBApiClient.getInstance();
                imgBBApiService.deleteImage(deleteHash, IMG_BB_API_KEY).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("ImgBB", "Image deleted successfully from ImgBB.");
                        } else {
                            Log.e("ImgBB", "Failed to delete image from ImgBB. Response: " + response.code() + " - " + response.message());
                            Toast.makeText(UploadStudentDocuments.this, "Failed to delete image from ImgBB. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        }

                        // ✅ Always delete Firestore document, even if ImgBB delete fails
                        deleteDocumentFromFirestore(documentRef, documentType, statusText, uploadIcon);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("ImgBB", "Error deleting image from ImgBB: " + t.getMessage());
                        Toast.makeText(UploadStudentDocuments.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                        // ✅ Always delete Firestore document, even if ImgBB delete fails
                        deleteDocumentFromFirestore(documentRef, documentType, statusText, uploadIcon);
                    }
                });

            } else {
                Log.e("ImgBB Debug", "ERROR: Document does not exist in Firestore!");
                Toast.makeText(UploadStudentDocuments.this, "Error: Document not found in Firestore.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Log.e("ImgBB Debug", "ERROR: Failed to fetch document: " + e.getMessage());
            Toast.makeText(UploadStudentDocuments.this, "Error fetching document: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }*/



    // ✅ Helper function to delete Firestore document
    private void deleteDocumentFromFirestore(DocumentReference documentRef, String documentType, TextView statusText, ImageView uploadIcon) {
        documentRef.delete()
                .addOnSuccessListener(aVoid -> {
                    statusText.setText("Tap to upload");
                    uploadIcon.setImageResource(R.drawable.ic_upload);
                    Toast.makeText(UploadStudentDocuments.this, documentType + " deleted successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(UploadStudentDocuments.this, "Failed to delete document: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }



    /*private void deleteDocumentFromFirestoreFinal(DocumentReference documentRef, TextView statusText, ImageView uploadIcon) {
        documentRef.delete()
                .addOnSuccessListener(aVoid -> {
                    statusText.setText("Tap to upload");
                    uploadIcon.setImageResource(R.drawable.ic_upload);
                    Toast.makeText(UploadStudentDocuments.this, "Document deleted successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(UploadStudentDocuments.this, "Failed to delete document: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }*/



    private void areAllDocumentsUploaded(OnSuccessListener<Boolean> callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "Error: User not authenticated", Toast.LENGTH_SHORT).show();
            callback.onSuccess(false);
            return;
        }

        String userId = user.getUid();
        String[] requiredDocuments = {
                "Grade 10 Report Card (front)",
                "Grade 10 Report Card (back)",
                "Birth Certificate",
                "I.D. Picture"
        };

        // Create a list of document references
        Map<String, DocumentReference> documentRefs = new HashMap<>();
        for (String documentType : requiredDocuments) {
            documentRefs.put(documentType, db.collection("students")
                    .document(userId)
                    .collection("documents")
                    .document(documentType));
        }

        // Fetch all documents in one request
        Tasks.whenAllSuccess(documentRefs.values().stream().map(DocumentReference::get).toArray(Task[]::new))
                .addOnSuccessListener(results -> {
                    boolean allUploaded = true;
                    for (Object result : results) {
                        DocumentSnapshot snapshot = (DocumentSnapshot) result;
                        if (!snapshot.exists() || !snapshot.contains("imageUrl")) {
                            allUploaded = false;
                            break;
                        }
                    }
                    callback.onSuccess(allUploaded);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UploadStudentDocuments.this, "Error checking documents: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onSuccess(false);
                });
    }




    private void handleSubmit() {
        areAllDocumentsUploaded(allUploaded -> {
            if (allUploaded) {
                startActivity(new Intent(this, DonePleaseWaitForConfirmation.class));
                finish();
            } else {
                Toast.makeText(this, "Please upload all documents before submitting!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private File compressImage(Uri imageUri) throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; // Reduce resolution by half (adjustable)

        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        inputStream.close();

        // Create a temporary compressed file
        File compressedFile = new File(getCacheDir(), "compressed_image.jpg");
        FileOutputStream outputStream = new FileOutputStream(compressedFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream); // 70% quality
        outputStream.close();

        return compressedFile;
    }
    private void navigateToAdmissionForm() {
        Intent intent = new Intent(UploadStudentDocuments.this, StudentPersonalInformation.class);
        startActivity(intent);
    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) { // Storage permission request
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start file picker
                imagePickerLauncher.launch("image/*");
            } else {
                // Permission denied, show a message
                Toast.makeText(this, "Storage permission denied. Cannot upload documents.", Toast.LENGTH_LONG).show();
            }
        }
    } */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            // Convert Uri to File
            File imageFile = convertUriToFile(this, selectedImageUri);

            // Upload the file
            uploadToImgBB(imageFile);
        }
    }

    private File convertUriToFile(Context context, Uri uri) {
        File file = new File(context.getCacheDir(), getFileName(context, uri));
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error converting file!", Toast.LENGTH_SHORT).show();
        }
        return file;
    }

    private String getFileName(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIndex != -1) {
                String fileName = cursor.getString(nameIndex);
                cursor.close();
                return fileName;
            }
            cursor.close();
        }
        return "unknown_file";
    }
}
