    package com.spacECE.spaceceedu.GrowthTracker;

    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;

    import androidx.appcompat.app.AppCompatActivity;

    import com.spacECE.spaceceedu.R;

    public class GrowthTrackerTips extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_growth_tracker_tips);
            TextView titleTextView = findViewById(R.id.growth_tracker_tips_title);
            TextView contentTextView = findViewById(R.id.growth_tracker_tips_description);
            String Category = getIntent().getStringExtra("Category clicked");
            // Update UI dynamically based on the category
            if (Category != null) {
                switch (Category) {
                    case "Water Intake":
                        titleTextView.setText("Recommended Water Intake for Kids");
                        contentTextView.setText("How much water does your child need to drink in a day? A good rule of thumb is to take half their body weight and drink that many ounces of water. For instance, if your child weighs 25 pounds, aim for around 12-13 ounces of water a day. If they weigh 100 pounds, they would need around 50 ounces a day.");
                        break;

                    case "Fruit Intake":
                        titleTextView.setText("Recommended Fruit Intake for Kids");
                        contentTextView.setText("Children need 1 to 2 cups of fruit daily, depending on their age and activity level. Fresh, whole fruits like apples, bananas, and oranges are great choices. Encourage variety and include seasonal fruits for better nutrition.");
                        break;

                    case "Vegetable Intake":
                        titleTextView.setText("Recommended Vegetable Intake for Kids");
                        contentTextView.setText("Kids should eat 1 to 3 cups of vegetables daily, depending on age and activity. Include a mix of leafy greens, cruciferous veggies like broccoli, and root vegetables like carrots for balanced nutrition.");
                        break;

                    case "Outdoor Play":
                        titleTextView.setText("Recommended Outdoor Play Time for Kids");
                        contentTextView.setText("Outdoor play is essential for kids' physical and mental development. Children should engage in at least 1-2 hours of outdoor play daily. Activities like running, cycling, and playing sports promote health and social skills.");
                        break;

                    case "Sleep Time":
                        titleTextView.setText("Recommended Sleep Time for Kids");
                        contentTextView.setText("Children need adequate sleep for growth and development. Toddlers (1-2 years) need 11-14 hours, preschoolers (3-5 years) require 10-13 hours, and school-aged kids (6-12 years) should sleep for 9-12 hours per day.");
                        break;

                    case "Screen Time":
                        titleTextView.setText("Recommended Screen Time for Kids");
                        contentTextView.setText("For kids under 2 years, avoid screen time except for video chats. Children aged 2-5 should have no more than 1 hour of high-quality screen time daily. Encourage alternative activities like reading and playing outdoors.");
                        break;

                    default:
                        titleTextView.setText("Tips for Healthy Habits");
                        contentTextView.setText("Encourage healthy habits in your child through balanced nutrition, regular play, and adequate sleep. Each habit contributes to their overall development.");
                        break;
                }
            }
            Button backButton = findViewById(R.id.growth_tracker_tips_back_btn);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate back to the previous activity
                    onBackPressed();
                }
            });
        }
    }