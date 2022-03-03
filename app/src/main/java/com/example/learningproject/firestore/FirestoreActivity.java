package com.example.learningproject.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.learningproject.R;
import com.example.learningproject.databinding.ActivityFirestoreBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class FirestoreActivity extends AppCompatActivity {
private static final String TAG="FirestoreActivity";
private static final String KEY_TITLE="title";
private static final String KEY_DESCRIPTION="description";
private FirebaseFirestore db=FirebaseFirestore.getInstance();
private static ActivityFirestoreBinding binding;
private DocumentReference noteRef=db.collection("Notebook").document("my First Note");
private ListenerRegistration noteListener;

    @Override
    protected void onStart() {
        super.onStart();
        //automatically detach the listener
        noteRef.addSnapshotListener(this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    Toast.makeText(FirestoreActivity.this,"Error While Loading",Toast.LENGTH_LONG).show();
                    return;
                }
                if(documentSnapshot.exists())
                {
                    /*String title=documentSnapshot.getString(KEY_TITLE);
                    String description=documentSnapshot.getString(KEY_DESCRIPTION);
                    binding.textViewData.setText("Title:"+title+"\n"+"Description:" +description);*/
                    //custom object
                    Note note=documentSnapshot.toObject(Note.class);
                    String title=note.getTitle();
                    String description =note.getDescription();
                    binding.textViewData.setText("Title:"+title+"\n"+"Description:" +description);
                }
                else
                {
                    binding.textViewData.setText("");
                }
            }
        });
    }
//detach the listener
    @Override
    protected void onStop() {
        super.onStop();
        noteListener.remove();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    binding= DataBindingUtil.setContentView(this,R.layout.activity_firestore);
    binding.fetchButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveNote();
        }
    });
    binding.load.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadNote();
        }
    });
    binding.update.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateDescription();
        }
    });
    binding.deleteDescription.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteDescription();
        }
    });
    binding.delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteNote();
        }
    });
    }


    private void saveNote() {
        String title=binding.editTextTitle.getText().toString();
        String description=binding.editTextDescription.getText().toString();
        Map<String,Object> note=new HashMap<>();
        note.put(KEY_TITLE,title);
        note.put(KEY_DESCRIPTION,description);
        Note note1= new Note(title,description);
       // db.document("Notebook/My First Note");
        noteRef.set(note1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(FirestoreActivity.this,"Success",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FirestoreActivity.this,"failure",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateDescription()
    {
     String description=binding.editTextDescription.getText().toString();
     Map<String,Object> note = new HashMap<>();
     note.put(KEY_DESCRIPTION,description);
     //
     noteRef.set(note);
     //merge if already exists
        noteRef.set(note, SetOptions.merge());
        //update note
        noteRef.update(note);
        //update only description
        noteRef.update(KEY_DESCRIPTION,description);
    }
    public void deleteDescription()
    {
        /*Map<String,Object> note= new HashMap<>();
        note.put(KEY_DESCRIPTION, FieldValue.delete());
        noteRef.update(note);*/
        noteRef.update(KEY_DESCRIPTION,FieldValue.delete());

    }
    public  void  deleteNote()
    {
        noteRef.delete();
    }
    private void loadNote() {
  noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
    @Override
    public void onSuccess(DocumentSnapshot documentSnapshot) {
       if(documentSnapshot.exists())
       {
          /* String title=documentSnapshot.getString(KEY_TITLE);
           String description=documentSnapshot.getString(KEY_DESCRIPTION);
           binding.textViewData.setText("Title:"+title+"\n"+"Description:" +description);*/
           Note note=documentSnapshot.toObject(Note.class);
           String title=note.getTitle();
           String description =note.getDescription();
           binding.textViewData.setText("Title:"+title+"\n"+"Description:" +description);
       }
       else
       {
           Toast.makeText(FirestoreActivity.this,"Document does not exists",Toast.LENGTH_LONG).show();
       }
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(FirestoreActivity.this,"Error",Toast.LENGTH_LONG).show();
    }
});
    }

}