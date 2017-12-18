# Health App
Technical test for Android development

1. The app downloads the 10 most recently updated patients from the test server at start up.
2. The app displays a list of the patients to the user, with picture if exists and name. 
3. The user is allowed to do the following to a patient object:
  * Tap on a patient card to view his/her family name, given name, gender, and birthday (if information is empty Unknown or default date is displayed). The user can update these information on the same view
  * After modifying the patient information, Tap the floating button (Save Icon) to update the patient on the FHIR test server with the new information. 
  * Perform horizontal swipe (left or right) on a patient card from the list to delete the patient from the FHIR test server.
4. Pull down the list to refresh the list and fetch the new latest 10 patients. 
5. Tap on the menu icons from the toolbar to change the order of patients displayed, based on birthday, or name (alphabetical).
6. The app can also perform other CRUD operations with the test server (tap the list floating button (Add Icon) to create a patient on the test server).
7. The number of patients that are downloaded at one time can be adjusted from the code with NUMBER_PATIENTS_DOWNLOADED constant.
8. The patient data are not stored locally, only the dao classes are done for now.
9. 'E MMM dd HH:mm:ss Z yyyy' date format is used
10. 'ca.uhn.hapi.fhir:hapi-fhir-android:3.1.0-SNAPSHOT' and 'ca.uhn.hapi.fhir:hapi-fhir-structures-dstu3:3.1.0-SNAPSHOT' libraries are used with their dependencies. The rest are Android support libraries.
