   package com.example.trottonbikes;

  import android.content.Intent;
  import android.os.Bundle;
  import android.view.LayoutInflater;
  import android.view.View;
  import android.view.ViewGroup;
  import android.widget.Toast;

  import com.android.volley.Request;
  import com.android.volley.Response;
  import com.android.volley.VolleyError;
  import com.android.volley.toolbox.JsonObjectRequest;
  import com.example.trottonbikes.databinding.FragmentBikeBookBinding;
  import com.google.android.gms.tasks.OnSuccessListener;
  import com.google.firebase.auth.FirebaseAuth;
  import com.google.firebase.auth.FirebaseUser;
  import com.google.firebase.firestore.CollectionReference;
  import com.google.firebase.firestore.DocumentReference;
  import com.google.firebase.firestore.FirebaseFirestore;

  import org.json.JSONException;
  import org.json.JSONObject;

  import java.util.Collection;

  import androidx.annotation.NonNull;
  import androidx.fragment.app.Fragment;

  /**
   * A simple {@link Fragment} subclass.
   * Use the {@link BikeBookFragment#newInstance} factory method to
   * create an instance of this fragment.
   */
  public class BikeBookFragment extends android.app.Fragment {

      FragmentBikeBookBinding binding;
      FirebaseFirestore firestore;
      long time = 0;

      // TODO: Rename parameter arguments, choose names that match
      // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
      private static final String ARG_PARAM1 = "param1";
      private static final String ARG_PARAM2 = "param2";

      // TODO: Rename and change types of parameters
      private String mParam1;
      private String mParam2;

      public BikeBookFragment() {
          // Required empty public constructor
      }

      /**
       * Use this factory method to create a new instance of
       * this fragment using the provided parameters.
       *
       * @param param1 Parameter 1.
       * @param param2 Parameter 2.
       * @return A new instance of fragment BikeOptionFragment.
       */
      // TODO: Rename and change types and number of parameters
      public static BikeBookFragment newInstance(String param1, String param2) {
          BikeBookFragment fragment = new BikeBookFragment();
          Bundle args = new Bundle();
          args.putString(ARG_PARAM1, param1);
          args.putString(ARG_PARAM2, param2);
          fragment.setArguments(args);
          return fragment;
      }

      @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          if (getArguments() != null) {
              mParam1 = getArguments().getString(ARG_PARAM1);
              mParam2 = getArguments().getString(ARG_PARAM2);
          }
      }

      @Override
      public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
          // Inflate the layout for this fragment
          binding = FragmentBikeBookBinding.inflate(inflater, container, false);

          Bundle bundle = this.getArguments();
          Bike bike = bundle.getParcelable("bike");
          firestore = FirebaseFirestore.getInstance();
          DocumentReference documentReference = firestore.collection("available").document(bike.getId());
          CollectionReference bookingReference = firestore.collection("booking");

          binding.bookBTN.setOnClickListener(v -> documentReference.get().addOnSuccessListener(documentSnapshot -> {
              if(documentSnapshot.exists() && ((Long)documentSnapshot.get("booked") == 0)) {
                  int selectedTime = binding.radioGroup.getCheckedRadioButtonId();
                  //long time = QueryUtils.getCurrentTime();
                  time = getCurrentTime();

                  DocumentReference bookDoc = bookingReference.document();
                  String bookingID = bookDoc.getId();

                  FirebaseAuth auth = FirebaseAuth.getInstance();
                  FirebaseUser user = auth.getCurrentUser();
                  String userName;
                  assert user != null;
                  userName = user.getDisplayName();

                  Booking newBooking = new Booking(bookingID, userName, selectedTime, time);
                  bookDoc.set(newBooking).addOnSuccessListener(unused -> Toast.makeText(getActivity(), "Booking Uploaded!", Toast.LENGTH_SHORT).show());

                  documentReference.update("booked", 1);
                  Intent intent = new Intent(getContext(), BikeBookedActivity.class);
                  intent.putExtra("timecode", selectedTime);
                  intent.putExtra("bike", bike);
                  startActivity(intent);
              }
          }));

          return binding.getRoot();
      }

      public long getCurrentTime() {
          String url = "http://worldtimeapi.org/api/timezone/Asia/Kolkata";
          JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
              try {
                  time = response.getLong("timeunix");
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }, error -> Toast.makeText(getActivity(), "Error Getting Time!", Toast.LENGTH_SHORT).show());

          return time;
      }
  }