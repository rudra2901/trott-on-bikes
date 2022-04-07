   package com.example.trottonbikes;

  import android.content.Intent;
  import android.os.Bundle;
  import android.view.LayoutInflater;
  import android.view.View;
  import android.view.ViewGroup;

  import com.example.trottonbikes.databinding.FragmentBikeBookBinding;
  import com.google.firebase.firestore.DocumentReference;
  import com.google.firebase.firestore.FirebaseFirestore;

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

          binding.bookBTN.setOnClickListener(v -> documentReference.get().addOnSuccessListener(documentSnapshot -> {
              if(documentSnapshot.exists() && ((Long)documentSnapshot.get("booked") == 0)) {
                  int selectedTime = binding.radioGroup.getCheckedRadioButtonId();
                  Intent intent = new Intent(getContext(), BikeBookedActivity.class);
                  intent.putExtra("timecode", selectedTime);
                  intent.putExtra("bike", bike);
                  startActivity(new Intent(getContext(), BikeBookedActivity.class));

                  //TODO : make a singleton class to get time from nist severs using apache commons net
              }
          }));

          return binding.getRoot();
      }
  }