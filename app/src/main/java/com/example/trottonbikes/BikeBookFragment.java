   package com.example.trottonbikes;

  import android.content.Context;
  import android.content.Intent;
  import android.content.SharedPreferences;
  import android.os.Bundle;
  import android.view.LayoutInflater;
  import android.view.View;
  import android.view.ViewGroup;
  import android.widget.RadioButton;
  import android.widget.Toast;

  import com.android.volley.Request;
  import com.android.volley.RequestQueue;
  import com.android.volley.toolbox.JsonObjectRequest;
  import com.android.volley.toolbox.Volley;
  import com.example.trottonbikes.databinding.FragmentBikeBookBinding;
  import com.google.firebase.auth.FirebaseAuth;
  import com.google.firebase.auth.FirebaseUser;
  import com.google.firebase.firestore.CollectionReference;
  import com.google.firebase.firestore.DocumentReference;
  import com.google.firebase.firestore.FirebaseFirestore;
  import com.google.gson.Gson;

  import org.json.JSONException;

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
      long time;

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
                  long selectedTime = getSelectedTime();
                  //long time = QueryUtils.getCurrentTime();
                  //getCurrentTime();
                  //Using system time instead of other methods.
                  time = System.currentTimeMillis();

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
                  intent.putExtra("bookingTime", time);
                  intent.putExtra("bike", bike);

                  SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("booking", Context.MODE_PRIVATE);
                  SharedPreferences.Editor prefEditor = sharedPreferences.edit();
                  Gson gson = new Gson();
                  String json = gson.toJson(newBooking);
                  prefEditor.putString("booking", json);
                  prefEditor.putBoolean("hasBooked", true);
                  prefEditor.putLong("timeCode", selectedTime);
                  prefEditor.putLong("timeBooked", time);
                  prefEditor.commit();
                  startActivity(intent);
              }
          }));

          return binding.getRoot();
      }

      public void getCurrentTime() {
          RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
          requestQueue.start();

          String url = "http://worldtimeapi.org/api/timezone/Asia/Kolkata";
          JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
              //Toast.makeText(getActivity(), "Inside response", Toast.LENGTH_SHORT).show();
              try {
                  time = response.getLong("unixtime");
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }, error -> Toast.makeText(getActivity(), "Error Getting Time!", Toast.LENGTH_SHORT).show());

          requestQueue.add(jsonObjectRequest);
          //return time;
      }

      public long getSelectedTime() {
          int id = binding.radioGroup.getCheckedRadioButtonId();
          RadioButton selectedButton = binding.radioGroup.findViewById(id);

          int x = binding.radioGroup.indexOfChild(selectedButton);
          switch (x) {
              case 0 :
                  return (15*60*1000);
              case 1 :
                  return (30*60*1000);
              case 2 :
                  return (60*60*1000);
              case 3 :
                  return (90*60*1000);
          }

          return 0;
      }
  }