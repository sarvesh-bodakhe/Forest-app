# Forest-app
SDS Task

30 June :
  Very Basic UI.
  App is able to plant a tree for specific time (inputed by user through slider widget).
  If user tries to go back or cancel the tree, it shows Alert not to get distracted.
  New User can be registered , Old user can logIn/Logout.
  
## 5 July
  
# Forest-App

* Productivity App which let us stay focussed. Whenever we want to focus, we should plant a tree.
  Timer will get started. During this time, if we try to go Back or Minimize the application, warning will appear.
  User need to create an account and log in two start the application and view information about previous milestones.


* The project was first devloped using basic user interface and more foucs on programming logic.
  In later stages of devlopment, ui was improvised accordingly.
* UI consist of navigation drawer along with four fragments for timeline of user trees, HomeScreen, Pie Chart, Application list.
* Selection of time is done thorugh Seek Bar.
* For implementation of timer logic CountDownTimer(Android class) is used.
* *Firebase Realtime Database* is used for storing information about trees planted.
  User is registered through email and password. 
  Everytime when a tree is planted or buried, an object containing information about that particular tree gets stored on database.
* Object has attributes as - StartTime, ExpectedEndTime, ActualEndTime, Date(java.util.Date) object.
  RecyclerView is used to show information about previous trees as a timeline.
* To get statastics of successful and unsuccessful trees, pie chart is used.
* Whitelist still need a lot work to be done.


# How To Run:
* The Project was entirely developed using android studio. One can find .apk file to test the app or one can use android studio to clone the repository.
* to clone repo copy .git link and by using VCS Menu inside Android Studio one can clone entire git repository.
* Once git repository is cloned, click on run icon on Android Studio Toolbar.
* Android studio will fire up an emulator for you and will run the app inside it.


