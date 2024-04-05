## About
StoryAppSubmission is an app built in Kotlin that implement intermediate level Android theories. This application is about making stories using camera or gallery with additional such as user login, theme and language setting.

## Specifications
These specifications represent some of the characteristics of one of this FindMeOnGithub app.
1. Authentication Page, with requirement:
   - A login page to enter the application, that contains:
     - Email
     - Password
   - A registration page to register in the application, that contains:
     - Name 
     - Email 
     - Password 
   - Hidden password
   - A Custom View as an EditText on the login or register page with the following conditions.
     - If the password length is less than 8 characters, it display an error message directly on the EditText without needing to switch forms or click a button first.
   - Save session data and token in preferences. Session data is used to manage the application flow with the following specifications.
     - If already logged in, directly enter the main page.
     - If not, then enter the login page.
   - There is a logout feature on the main page with the following conditions.
     - When the logout button is pressed, the token and session information is deleted.
    
2. List of Stories (List Story), with requirements:
   - A list of stories from the provided API. The following information is displayed.
     - User's name 
     - Photo 
   - Display detail view when one of the story items is pressed. The following information is displayed.
     - User's name
     - Photo
     - Description 

3. Add Story, with requirements:
   - There is detailed information about a user. Here are some mandatory information to be displayed on the application page.
     - Photo file (able to be chosen from the gallery)
     - Story description
   - The following are the conditions for adding a new story:
     - There is a button to upload data to the server.
     - After clicking this button and the upload process is successful, it returns to the list of stories page.
     - The latest story data appears at the top.

4. Display Animation, with requirements:
   - Animation in the application using Property Animation.
     
5. Displaying Maps, with requirements:
   - A new page containing a map that shows all stories with their locations correctly. It can be represented by markers or icons in the form of images. Story data with location latitude and longitude can be retrieved through the location parameter as follows: https://story-api.dicoding.dev/v1/stories?location=1.

6. Paging List, with requirements:
   - A list of stories using Paging 3 correctly.

7. Testing Implementation, with requirements:
   - Unit tests on functions within the ViewModel that retrieve Paging data lists.
   
## Demonstration
Here is a video demonstration of StoryAppSubmission app:
https://github.com/alsytaqiya/Story-App/assets/93572947/7afeb9c9-cd53-4af5-9c7a-78b80b588373

