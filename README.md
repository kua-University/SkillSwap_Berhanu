

# 📌 SkillSwap

**SkillSwap** is an Android app that connects people looking to learn new skills with those who can teach them. Users can offer and learn skills, chat in real time, post updates, and search for others with similar interests.


## 🚀 Features

- **User Authentication**:  login, signup, and password reset
-  **Personalized Profiles**: Users can enter their mame, bio, age, and other details
-  **Skill Matching**: Connects users based on their teaching and learning preferences
- **Real-Time Chat**: Instant messaging for skill-sharing discussions
- **Social Feed**: Users can post thoughts & updates
- **Notifications**: Alerts for new posts and interactions
- **User Search**: Find other users

---

## 📱 User Flow

### 1️⃣ Authentication Flow

- **Login Page**: Users log in if successful resirectes to the main page
- **ForgotMyPassword page**: Reset password
- **Registration Page**: Create a new account

### 2️⃣ Signup Process

- **UserInfo page**: Users enter their personal details
- **SkillsToTeach page**: Select skills to teach
- **SkillsToLearn page**: Select skills to learn
- Redirected to **Main page**

### 3️⃣ Main Page (Core App Experience)

- **Bottom Toolbar with 4 Icons**
  1. **Homepage ** 🏠: Scrollable feed (default screen)
  2. **Search page** 🔍: Find users
  3. **Post page** ➕: Share thoughts (visible in HomeFragment)
  4. **MessagingF page** 💬: Real-time chat with users
 **  Top Toolbar with 2 icons**
  1. **Profile **: Shows user details (name, age, etc.)
  2. **Notifications** 🔔: Alerts for new posts and interactions

---

## 🛠 Tech Stack

- **Frontend**: Java (Jetpack Compose / XML)
- **Backend**: Firebase (Firestore, Authentication, Storage)

### APIs Used:
- **Authentication API**: Login & registration
- **Messaging API**: Real-time chat (MessagingFragment)
- **Posts API**: Upload & retrieve posts (PostFragment)
- **Home API**: Display posts (HomeFragment)
- **Search API**: Find users (SearchFragment)
- **User Info API**: Store & retrieve user details (UserInfoFragment)
- **Learn & Teach API**: User skill preferences (SkillsToTeachActivity, SkillsToLearnActivity)

---

## 📝 Usage Guide

### 1️⃣ Register & Set Up Profile
- Sign up using email & password
- Enter personal details

### 2️⃣ Choose Skills to Learn & Teach
- Select a skill you want to learn
- Select a skill you can teach

### 3️⃣ Connect & Chat
- Search for users
- Start real-time chat in MessagingFragment

 ### 4️⃣  View posts
#3 installation
### Share Your thoughts
- Post thoughts & updates in PostFragment

### Installation
  - clone the following repository
  - git clone https://github.com/kua-University/SkillSwap_Berhanu.git
     
 ## Berhanu Hiluf
## UGR/170254/12
