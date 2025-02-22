## Berhanu Hiluf
## UGR/170254/12

# 📌 SkillSwap

**SkillSwap** is a social learning platform that connects users based on the skills they can teach and the skills they want to learn. The app features real-time chat, a social feed, and personalized user profiles to make learning interactive and engaging.


## 🚀 Features

- ✅ **User Authentication**: Secure login, signup, and password reset
- ✅ **Personalized Profiles**: Users can enter their bio, age, and skills
- ✅ **Skill Matching**: Connects users based on their teaching and learning preferences
- ✅ **Real-Time Chat**: Instant messaging for skill-sharing discussions
- ✅ **Social Feed**: Users can post thoughts & updates (like a social media feed)
- ✅ **Notifications**: Alerts for new posts and interactions
- ✅ **User Search**: Find other users based on skills or interests

---

## 📱 User Flow

### 1️⃣ Authentication Flow

- **LoginActivity**: Users log in or navigate to:
  - **ForgotMyPasswordActivity**: Reset password
  - **SignupActivity**: Create a new account

### 2️⃣ Signup Process

- **UserInfoFragment**: Users enter their personal details:
  - Profile image
  - Name
  - Age
  - Bio
  - Occupation

- **SkillsToTeachActivity**: Select skills to teach
- **SkillsToLearnActivity**: Select skills to learn
- Redirected to **MainActivity**

### 3️⃣ MainActivity (Core App Experience)

- **Bottom Toolbar with 6 Icons**:
  1. **HomeFragment** 🏠: Scrollable feed (default screen)
  2. **SearchFragment** 🔍: Find users based on skills
  3. **PostFragment** ➕: Share thoughts (visible in HomeFragment)
  4. **MessagingFragment** 💬: Real-time chat with users
- **  Top Toolbar with 2 Icons**:
  5. **Profile **: Shows user details (name, age, etc.)
  6. **Notifications** 🔔: Alerts for new posts and interactions

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
- Enter age, bio, and skills

### 2️⃣ Choose Skills to Learn & Teach
- Select a skill you want to learn
- Select a skill you can teach

### 3️⃣ Connect & Chat
- Search for users based on skills
- Start real-time chat in MessagingFragment

### 4️⃣ Share Your Progress
- Post thoughts & updates in PostFragment
- View posts in HomeFragment
