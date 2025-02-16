package StudentInterface;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.R;

import SystemInterface.DummyStudentDataStorage;

public class UploadStudentDocuments extends AppCompatActivity {

    private DummyStudentDataStorage dummyDataStorage;

    // For launching the image picker to select an image.
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), this::handleImagePicked);

    // Variables to track the current upload context.
    private String currentDocumentType;
    private TextView currentStatusText;
    private ImageView currentUploadIcon;
    private ProgressBar currentLoadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enables edge-to-edge layout (optional).
        setContentView(R.layout.activity_upload_student_documents);

        // Initialize UI components
        dummyDataStorage = new DummyStudentDataStorage();
        TextView textViewAdmissionFormLink = findViewById(R.id.textview_back_to_admission_form);
        Button submitButton = findViewById(R.id.button_submit_documents);
        submitButton.setOnClickListener(v -> handleSubmit());

        // Set up upload areas for different document types.
        setupUploadArea(R.id.upload_report_card_area, "Grade 10 Report Card",
                R.id.upload_icon_report_card, R.id.loading_report_card, R.id.status_text_report_card);
        setupUploadArea(R.id.upload_birth_certificate_area, "Birth Certificate",
                R.id.upload_icon_birth_certificate, R.id.loading_birth_certificate, R.id.status_text_birth_certificate);
        setupUploadArea(R.id.upload_id_picture_area, "I.D. Picture",
                R.id.upload_icon_id_picture, R.id.loading_id_picture, R.id.status_text_id_picture);

        // Admission Form Link
        textViewAdmissionFormLink.setOnClickListener(view -> navigateToAdmissionForm());
    }

    private void navigateToAdmissionForm() {
        // Intent to navigate back to StudentPersonalInformation activity
        Intent intent = new Intent(UploadStudentDocuments.this, StudentPersonalInformation.class);
        startActivity(intent);
    }

    // Configures each upload area with its icon, loading spinner, and status text.
    private void setupUploadArea(int layoutId, String documentType,
                                 int iconId, int loadingId, int statusTextId) {
        RelativeLayout uploadArea = findViewById(layoutId);
        ImageView uploadIcon = findViewById(iconId);
        ProgressBar loadingSpinner = findViewById(loadingId);
        TextView statusText = findViewById(statusTextId);

        // Handle click events on the upload area.
        uploadArea.setOnClickListener(v -> {
            if (dummyDataStorage.isDocumentUploaded(documentType)) {
                // If the document is already uploaded, show the pop-up to view or delete it.
                showImagePopup(dummyDataStorage.getDocument(documentType), documentType, statusText, uploadIcon, loadingSpinner);
            } else {
                // Start the upload process if no document is uploaded yet.
                currentDocumentType = documentType;
                currentStatusText = statusText;
                currentUploadIcon = uploadIcon;
                currentLoadingSpinner = loadingSpinner;
                startImageUpload();
            }
        });
    }

    // Launches the file picker for image upload.

    // Ps. don't remove the spaces before the 'Uploading...', it is intentional.
    private void startImageUpload() {
        currentStatusText.setText("        Uploading...");
        currentUploadIcon.setVisibility(View.GONE); // Hide the icon.
        currentLoadingSpinner.setVisibility(View.VISIBLE); // Show the loading spinner.
        imagePickerLauncher.launch("image/*"); // Open the file picker.
    }

    // Handles the result of the file picker.
    private void handleImagePicked(Uri imageUri) {
        currentLoadingSpinner.setVisibility(View.GONE); // Hide the spinner after the process

        if (imageUri != null) {
            // Update the UI and save the document if an image was selected
            currentStatusText.setText("Uploaded, tap to view");
            currentUploadIcon.setImageResource(R.drawable.ic_check); // Show success icon
            currentUploadIcon.setVisibility(View.VISIBLE);
            dummyDataStorage.storeDocument(currentDocumentType, imageUri.toString());
            Toast.makeText(this, currentDocumentType + " uploaded successfully!", Toast.LENGTH_SHORT).show();
        } else {
            // Reset the UI if no image was selected
            currentStatusText.setText("Tap to upload");
            currentUploadIcon.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Displays a pop-up with options to view or delete the uploaded image.
    @SuppressLint("ClickableViewAccessibility")
    private void showImagePopup(String imageUri, String documentType,
                                TextView statusText, ImageView uploadIcon, ProgressBar loadingSpinner) {
        // Create a custom dialog
        Dialog dialog = new Dialog(this, R.style.TransparentPopup);
        dialog.setContentView(R.layout.popup_image_view); // Inflate your popup layout
        dialog.setCancelable(true); // Allow dismissal with the back button

        // Set the dialog's background to be transparent
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
        }

        // Bind views from the popup layout
        ImageView imageView = dialog.findViewById(R.id.popup_image_view);
        Button deleteButton = dialog.findViewById(R.id.button_delete);
        Button backButton = dialog.findViewById(R.id.button_back);

        // Load image URI
        imageView.setImageURI(Uri.parse(imageUri));

        // Handle delete and back buttons
        deleteButton.setOnClickListener(v -> {
            // Delete the document from storage
            dummyDataStorage.storeDocument(documentType, null);

            // Reset the UI
            statusText.setText("Tap to upload");
            uploadIcon.setImageResource(R.drawable.ic_upload);
            dialog.dismiss();

            Toast.makeText(this, documentType + " deleted successfully!", Toast.LENGTH_SHORT).show();
        });

        backButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Checks if all required documents are uploaded.
    private boolean areAllDocumentsUploaded() {
        return dummyDataStorage.isDocumentUploaded("Grade 10 Report Card") &&
                dummyDataStorage.isDocumentUploaded("Birth Certificate") &&
                dummyDataStorage.isDocumentUploaded("I.D. Picture");
    }

    // Handles the final "Submit" button click.
    private void handleSubmit() {
        if (areAllDocumentsUploaded()) {
            // Navigate to the confirmation screen if all documents are uploaded.
            startActivity(new Intent(this, PleaseWaitForConfirmation.class));
            finish();
        } else {
            // Show an error message if any document is missing.
            Toast.makeText(this, "Please upload all documents before submitting!", Toast.LENGTH_LONG).show();
        }
    }
}
