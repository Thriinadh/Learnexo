package com.learnexo.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtil {

    public static FirebaseAuth sAuth;
    public static StorageReference sStorageReference;
    public FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();


     static {
        sAuth = FirebaseAuth.getInstance();
        sStorageReference = FirebaseStorage.getInstance().getReference();
    }

    public static boolean doesUserExist() {
        return sAuth.getCurrentUser() != null;
    }

    public static String getCurrentUserId() {
         FirebaseUser user= sAuth.getCurrentUser();
        if (user == null) {
            return "";
        } else {
            return user.getUid();
        }
    }

    public static void sAuthSingOut() {
        sAuth.signOut();
    }
}
