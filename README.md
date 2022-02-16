![Tuwaiq Academy Logo](https://camo.githubusercontent.com/37ca472e2afb74974a0314d89af8f470422a79582bed0d188f9927777230195d/68747470733a2f2f6c61756e63682e73612f6173736574732f696d616765732f6c6f676f732f7475776169712d61636164656d792d6c6f676f2e737667)

<!-- ![PlantEye Header](https://b.top4top.io/p_2213bkzyu1.png) -->

# Android Final Capstone - PLANTEye App

Tuwaiq Academy final project using Kotlin.

## Table of contents 📄
- [Overview](#overview)
- [Software Requirements Specification (SRS)](#software-requirements-specification-srs)
- [Used Technologies](#used-technologies)
- [User Stories](#user-stories)
- [Wireframe](#wireframe)
- [Planning and Development](#planning-and-development-process)
- [Final App](#final-app)

## Overview
<!-- ![PlantEye Header](https://b.top4top.io/p_2213bkzyu1.png) -->
![PlantEye Header](https://k.top4top.io/p_22131whvu1.png)

> There is an app for everything

PlantEye is a mobile application designed for anyone who enjoys plants or is interested in nature. Identify plants, flowers, and trees by photo. Furthermore, scan your plant to determine its health status!

## Software Requirements Specification (SRS)

The application includes the following features:
1. User identification:
    - sign up & login
    - Reset password
    - View profile
2. Plant identification: This feature is based on the [Plant.id](https://web.plant.id/plant-identification-api/) API, which uses machine learning to identify plant species. The user will be able to:
    - Identify plants based on their images
    - Add the plant to the "Save plants" collection
    - View or delete plant details, and add a note to a specific plant
    - Gain access to additional information about each plant
3. Plant disease diagnosis: The [Plant.id](https://web.plant.id/plant-identification-api/) API can also diagnose disease. The user will be able to:
    - Identify the plant's health state based on its image
    - View more information about the disease
    - View the disease's diagnosis history
4. Articles: The app will include a variety of articles with farming and plant care tips.

<hr>

## Used Technologies

- Firebase database
- API ([Plant.id](https://web.plant.id/plant-identification-api/))
- Recycler view 
- Diff util
- Intent
- View binding
- Fragments 
- Notification
- Live data

### Tools
- Android Studio
- Git & Github
- Figma

<hr>

## User Stories
'PlantEye' app user stories: 
- As a user, I want to know the name or type of plant so that I can learn more about it.
- As a user, I want to be able to identify the plants so that I can easily find and purchase them.
- As a user, I want to check the health of my plants so that I can take appropriate action if nessecary.
- As a user, I want to get plant care tips so I can take care of my indoor plants.
- As a user, I want to have a list of  plants that I'm interested in so that I can easily return to them.

<hr>

## Wireframe

For the wireframe I used Figma, you can preview my work [here](https://www.figma.com/file/aWkuGfhZ6abgWSeHs2HLQ5/Capstone-3---Plant-identifier?node-id=308%3A769)!
![plantEye Wireframe](https://k.top4top.io/p_22382uq0u1.png)
<hr>

## Planning and Development Process 

Every good application requires a plan to meet the app specifications and requirements of the user. My strategy was as follows:

1. Brain storming and wirefram design.
2. Front-end: XML design in Android Studio.
3. Back-end:
    - Firebase Database
    - Plant.id API
5. Testing including unit testing

I started by brainstorming ideas for the app requirements, and then I looked at different apps in the Google Play store to get a better understanding of the app workflow. Then, I provided my application with a proper name and a logo. After that, I used Figma to create the app interfaces.

During the development process, I tested the API in Postman to ensure that it functioned properly. Then, in Android Studio, I started with the XML design for the UI. After that, I moved on to the backend, beginning with the API, followed by the Firestore repo, and finally implemented the functionalities.

<hr>

## Final App

![plantEye welcome frame](https://g.top4top.io/p_2238gnnp01.png)

![plantEye diagnose frame](https://j.top4top.io/p_2238jty741.png)

![plantEye identify frame](https://i.top4top.io/p_2238a7xcp3.png)
<hr>

## Dependencies

- Firebase - For authentication, data storage, and notification
```groovy
   dependencies { 
         //...
        implementation 'com.google.firebase:firebase-auth-ktx:21.0.1'
        implementation 'com.google.firebase:firebase-firestore-ktx:24.0.0'
        implementation 'com.google.firebase:firebase-storage-ktx:20.0.0'
        implementation 'com.google.firebase:firebase-database-ktx:20.0.3'
        implementation 'com.google.firebase:firebase-messaging-ktx:23.0.0'
        implementation 'com.firebaseui:firebase-ui-firestore:8.0.0'

        // Play service for coroutines to work with the Firestore
        implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.4.3-native-mt'
        // ...
   }
```

- Glide - For image display
```groovy
    dependencies { 
        // ...
        implementation 'com.github.bumptech.glide:glide:4.12.0'
        annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
    }
```

- Matisse - local image selector
```groovy
    dependencies { 
        // ...
        implementation 'com.zhihu.android:matisse:0.5.3-beta3'
    }
```

