# Wasteless Eats *Project part of a hackfest

Welcome to Wasteless Eats – an innovative solution addressing food waste and promoting sustainability.

## Mission

Bridge the gap between surplus, imperfect food, and those who can benefit. Wasteless Eats extends beyond edibles; it's a platform for donating/selling various items, including electronics, books, and more.

## Problem Statement

In a world where millions suffer from hunger, edible food is discarded due to imperfections, worsening global hunger and environmental degradation. Wasteless Eats aims to connect surplus food with those in need, reducing waste globally.

## Key Features

- 🌽 **Imperfect Food Marketplace**: Browse and choose from a variety of perfectly edible but imperfect food items.
  
- 🔄 **Diverse Items Exchange**: Facilitate the exchange of unused items, fostering a culture of reuse.

- 🌍 **Local and Global Impact**: Contribute to community and global waste reduction efforts.

- 🤝 **Charitable Initiatives**: Support organizations addressing hunger and promoting sustainability.

- 🎨 **User-Friendly Interface**: An intuitive, seamless experience for both donors and recipients.

## Additional Features

- 🔄 **Real-time Database**: Use Firebase for seamless item tracking.

- 🧠 **Rotten Food Detector**: Utilize TensorFlow for identifying and preventing the distribution of spoiled items.

- 🗺️ **Location-Based Services**: Leverage Google Maps API for pinpointing item locations.

By incorporating these features, Wasteless Eats revolutionizes the donation and exchange process, ensuring safety, quality, and efficient tracking of items. Join us in the fight against food waste and contribute to a more sustainable world.

# Installation

### 1. Clone or Download Source Code

- Clone this repository using Git:

    ```bash
    git clone https://github.com/zoneeoX/Wastelesseats.git
    ```

  - **OR**

- Download the Zip file and extract it to your preferred directory (e.g., `C:\Users\Username\AndroidStudioProjects`).

### 2. Wait for Build Gradle to Complete

Navigate to the project directory, open it in Android Studio, and wait for the build process to complete.

### 3. Congratulations! You are done installing this project. 🎉

# Running the App

- Connect your Android phone to your computer or launch an emulator.
- In Android Studio, click on the "Run" button (🚀) or use the keyboard shortcut `Shift + F10` to deploy the app on your connected device.

# Application Features and Appearance

1. **Login and Register**
   <p align="center">
     <img width="250" alt="wastelesseats" src="https://github.com/zoneeoX/Wastelesseats/assets/26033026/d1c8ee4d-6609-4f1e-b360-9e237844c5f0">  
     <img width="250" alt="wastelesseats" src="https://github.com/zoneeoX/Wastelesseats/assets/26033026/f7c6792f-3723-4643-be2f-9c19511e4030">

   </p><br>

2. **Home Page**
   <p align="center"><img width="250" alt="wastelesseats" src="https://github.com/zoneeoX/Wastelesseats/assets/26033026/078f7253-0fe7-457a-b27b-c3e268e58410"></p><br>

3. **Map Page*
   <p align="center"><img width="250" alt="wastelesseats" src="https://github.com/zoneeoX/Wastelesseats/assets/26033026/9e667488-4e45-4557-8ff0-1881b545b13a"></p><br>

4. **Your item and Post page**
   <p align="center"><img width="250" alt="wastelesseats" src="https://github.com/zoneeoX/Wastelesseats/assets/26033026/e21429a3-6404-4887-8012-6b646b670a08"></p><br>
   <p align="center"><img width="250" alt="wastelesseats" src="   https://github.com/zoneeoX/Wastelesseats/assets/26033026/065a762f-a3a7-4038-9229-44da69d806a3"></p><br>

5. **Details Post Page**
   <p align="center"><img width="250" alt="wastelesseats" src="https://github.com/zoneeoX/Wastelesseats/assets/26033026/25638732-057d-4a01-83b9-8eaa18455339"></p><br>
   <p align="center"><img width="250" alt="wastelesseats" src="https://github.com/zoneeoX/Wastelesseats/assets/26033026/284bb2c9-55b5-43eb-8bbc-bddd0258dce9"></p><br>
   <p align="center"><img width="250" alt="wastelesseats" src="https://github.com/zoneeoX/Wastelesseats/assets/26033026/f7cdb9c4-ec17-4533-a869-17bf55efd0f0"></p><br>

6. **Notifications Page**
   <p align="center"><img width="250" alt="wastelesseats" src="https://github.com/zoneeoX/Wastelesseats/assets/26033026/8fbae71b-3fbe-4b9f-8d79-0d717ea1f3ba"></p><br>

7. **Profile Page**
    <p align="center"><img width="250" alt="wastelesseats" src="https://github.com/zoneeoX/Wastelesseats/assets/26033026/bc110b99-6704-4733-bc05-43d6e9789129"></p><br>

8. **Machine Learning**
    <p align="center"><img width="250" alt="wastelesseats" src="https://github.com/zoneeoX/Wastelesseats/assets/26033026/981b4c7d-1fd7-402c-8088-8a3103f3ae28"></p><br>
    <p align="center"><img width="250" alt="wastelesseats" src="https://github.com/zoneeoX/Wastelesseats/assets/26033026/39548278-1a40-4ec9-a1b7-b76674793de5"></p><br>

# Machine Learning Integration

Wasteless Eats harnesses the power of TensorFlow, a robust open-source machine learning library, to elevate user experiences and champion sustainable practices.

## Gathering Data

Data collection was curated from diverse sources, ensuring a rich and varied dataset. Utilizing datasets like [Kaggle - Rotten and Fresh Food Images](https://www.kaggle.com/datasets/sriramr/fruits-fresh-and-rotten-for-classification), we create a foundation for effective machine learning.a

## Classification Targets:

1. 🍏 apple
2. 🍊 orange
3. 🍌 banana
4. 🫑 okra
5. 🥒 cucumber
6. 🥔 potato
7. 🍅 tomato

##  Data Processing

Gathered data undergoes meticulous preprocessing to optimize the model's performance. Classification into six distinct classes is complemented by the application of data augmentation techniques, mitigating potential overfitting.

##  Model Development

Implementing transfer learning with TensorFlow Hub's MobileNet V3, we leverage pre-trained weights to achieve high accuracy. The model undergoes fine-tuning, integrating additional dense layers, dropout layers for regularization, and a softmax layer for precise classification.

# Thanks for Being a Part of Wasteless Eats!

Your time spent exploring Wasteless Eats is truly appreciated. By joining us in the mission to reduce food waste and embrace sustainability, you've made a significant contribution.

Whether you're using, supporting, or contributing to Wasteless Eats, your involvement is pivotal. Let's continue creating a community that values shared resources and environmental consciousness.

As you navigate through Wasteless Eats, your commitment to positive change is invaluable. Together, we're shaping a future where surplus resources make a difference in building a more equitable and waste-conscious society.

Thank you for your interest and participation. Here's to a sustainable and fulfilling experience with Wasteless Eats! 🌍🍽️
