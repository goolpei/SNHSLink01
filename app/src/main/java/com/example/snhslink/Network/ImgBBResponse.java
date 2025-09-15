package com.example.snhslink.Network;

import com.google.gson.annotations.SerializedName;

public class ImgBBResponse {
    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        @SerializedName("url")
        private String url;

        @SerializedName("delete_url")
        private String deleteUrl; // This is NOT the API delete URL!

        public String getDeleteHash() {
            if (deleteUrl != null && deleteUrl.contains("/")) {
                return deleteUrl.substring(deleteUrl.lastIndexOf("/") + 1); // Extracts correct hash
            }
            return null;
        }

        public String getDeleteUrl() {
            String deleteHash = getDeleteHash();
            if (deleteHash != null) {
                return "https://api.imgbb.com/1/delete/" + deleteHash; // ✅ Correct API delete URL
            }
            return null;
        }

        public String getUrl() {
            return url;
        }
    }
}

