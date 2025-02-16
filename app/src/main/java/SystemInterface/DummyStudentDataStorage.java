package SystemInterface;

import java.util.HashMap;

public class DummyStudentDataStorage {
    private final HashMap<String, String> documentStorage = new HashMap<>();

    // Store a document in the storage
    public void storeDocument(String documentType, String documentUri) {
        if (documentUri == null) {
            // Remove the document if null is passed
            documentStorage.remove(documentType);
        } else {
            // Store or update the document
            documentStorage.put(documentType, documentUri);
        }
    }

    // Check if a document is uploaded
    public boolean isDocumentUploaded(String documentType) {
        return documentStorage.containsKey(documentType) && documentStorage.get(documentType) != null;
    }

    // Retrieve the document URI
    public String getDocument(String documentType) {
        return documentStorage.get(documentType); // Returns null if not found
    }
}
