# NutriTrack
### NutriTrack is a Automatic Nutrition Logging app that use Deep learning and image processing to analyse the food type in your plate to predict, estimate and calculate your nutritional intake to help you make healthier life choices.

## Local Setup:
1. Clone the github repo into your local machine: `git clone https://github.com/pradeepkarthik77/calorietracker.git`
2. Change the cwd to calorietracker: `cd ../<relative_path_to_calorietracker`
3. Frontend Setup:
* From Android Studio, go to Open Project > Open the folder frontend
4. Backend Setup:
* Change the cwd to Backend folder: `cd Backend`
* Install npm dependencies: `npm install`
* Change the mongodb url to your own mongodb cluster
* Start the server: `node app.js`

## Features:
1. Automatic nutrition calculation from the given image
2. Customized specially for Indian foods
3. Recommendation for daily calorie intake, protien intake and other macronutrients based on user's physical data.
4. Weekly, Monthly and Yearly Analysis of your calorie, water and macronutrients intake.
5. Setup goals for your daily calorie, water and macronutrients intake
