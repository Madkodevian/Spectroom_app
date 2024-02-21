<h1 align="center"># 📱Spectroom_app📱</h1>
<h4 align="center">:construction: Project in progress :construction:</h4>

<h1 align="center"><img src="https://github.com/Madkodevian/Spectroom_app/assets/56256350/8c817bd7-e71f-4856-8703-3ccbb7d2dcde" width="100"></h1>

## Table of Contents
1. [General Info](#general-info)
2. [Technologies](#technologies)
3. [Installation](#installation)
4. [Spectroom Content](#spectroom-content)
5. [User](#user)
6. [Administrator](#Administrator)
7. [Project in progress](#Project-in-progress)

### General Info
***
Spectroom is a graphic design application, vinyl designs and others.

This application is made with the Java programming language and I'm making it on Android Studio Hedgehog 2023.1.1.
To test the App use, I have started the app with an Nexus 6 API 34 emulator.

## Technologies
***
The technologies that I used in my project are:
* Technology stack: Android Studio Hedgehog 2023.1.1
* Database: Firebase (Authentication and Realtime Database)

## Installation
***
To use my project, clone the repository in the work area that you are going to use to see the project.

## Spectroom Content
## User
***
¿What can you see as User?
* Login Activity and Firebase Authentication
<p>For now, you can see a Login Activity, which can only be accessed if you have previously registered with a valid email and password.
The email and password are saved in Firebase Authentication, and when you return to Login, you can log in with your User account that is detected by Firebase in the Login Activity.</p>

* Logging in
<p>Once you're inside the application, you'll see a welcome message on the screen. Additionally, you'll find a small menu in the top right corner with two images (a shopping cart and a user profile picture) which you can click on to go to the shopping cart or to your profile.</p>

* Main Menu
<p>You can access the application's main menu by clicking on the top left corner, where you'll see different activities that you can interact with.</p>

* Store
<p>To have items in your cart, you must first purchase one or more products.</p>

<p>First, let's go to the main menu, select Store, and we'll see the content of the activity.
In the store, we'll see a list of products that the Application Administrator has previously added.
We can click on the one we want, and once we've selected it, we'll go to another activity where we'll select the characteristics that the product needs to have through checkboxes.</p>

<p>Once chosen, I access the next activity which shows me the selected product with the details I've wanted to add, and by clicking on the add button, the chosen product is saved in a new list called Cart where I'll have the products I'm saving.</p>

* Cart
<p>In the cart activity, I can delete the Cart list if I don't want the products or purchase the products.</p>

* Purchase
<p>In this activity, I can view the items I've purchased in the Cart; a purchase history.</p>

* My Profile
<p>In my profile, I can check the products I've ordered.</p>

* Contact
<p>In the contact section, I can send text that will be saved in Firebase Realtime Database, with a headline and a message.</p>

* Exit Profile
<p>I can exit the profile and return to the Login, once I click on Exit.</p>

## Administrator
***
¿What can you see as Administrator?

* Logging in as Administrator
<p>Once you're inside the application, you'll see a welcome message on the screen, but this time as an Administrator.
In the top right corner, you'll only see a small menu with one image (a user profile picture) which you can click on to go to your profile.</p>

<p>You cannot access either the Basket or the Order History of users, but you can manage the Store.</p>

* Store as Administrator
<p>In the store, you can add and delete products that are in the Store list (or add new ones whether or not there are products in the Store).</p>

* Profile as Administrator
<p>In your profile, you'll be able to access the Store through a button (which can also be accessed from the Main Menu).<p>

## Project in progress
***
<p>That's all for now.<p>

<p>It's an ongoing application, and it requires time to be improved.
It will be enhanced and updated gradually. Thank you very much.<p>
