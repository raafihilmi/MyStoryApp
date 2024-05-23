# MyStoryApp

Welcome to MyStoryApp! This is my third Android application project, developed to allow users to share and view stories. The application was completed as part of a certification program on Dicoding, and the certification can be viewed [here](https://www.dicoding.com/certificates/JMZV1OJOQXN9).

## Table of Contents

- [Features](#features)
- [Screenshots](#screenshots)
- [Installation](#installation)
- [Usage](#usage)
- [Architecture](#architecture)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- **Story List**: View a list of stories shared by users.
- **Story Details**: Detailed view of each story.
- **Add Story**: Create and share a new story.
- **User Authentication**: User login and registration.
- **Search Stories**: Search for stories by title or content.
- **Favorite Stories**: Mark and manage your favorite stories.

## Screenshots
<img height="400px" src="https://github.com/raafihilmi/MyGithubUsers/assets/69000583/b6aee011-37f7-4bdb-b548-69f7bb346f55" alt="splashscreen" />
<img height="400px" src="https://github.com/raafihilmi/MyGithubUsers/assets/69000583/a1400042-22a2-46ee-b7b9-80b5af1d2b73" alt="home" />
<img height="400px" src="https://github.com/raafihilmi/MyGithubUsers/assets/69000583/1dab55b2-39f3-403d-bab1-bd472a8466f2" alt="detail" />
<img height="400px" src="https://github.com/raafihilmi/MyGithubUsers/assets/69000583/21c8e6eb-e584-4a26-9847-137a0384c2f7" alt="favorite" />
<img height="400px" src="https://github.com/raafihilmi/MyGithubUsers/assets/69000583/e55f4b6c-3dbc-469c-a4df-fd1d616a98297" alt="darkmode" />

## Installation

To get a local copy up and running, follow these simple steps:

1. **Clone the repo**
   ```sh
   git clone https://github.com/raafihilmi/MyStoryApp.git
2. Open the project in Android Studio
3. Build the project: Let Android Studio download and install the required dependencies.
4. Run the app: Connect your Android device or use an emulator to run the app.

## Usage

1. **Launch the app**: Start the application on your Android device.
2. **Browse stories**: Scroll through the list to see various stories.
3. **View details**: Tap on any story to view more information.
4. **Add story**: Use the add button to create and share a new story.
5. **Search**: Use the search bar to find specific stories by title or content.
6. **Favorite**: Tap the star icon to add stories to your favorites.

## Architecture

### MVVM (Model-View-ViewModel) Design Pattern

The application follows the MVVM design pattern, which helps to separate the concerns of the application into different layers and promotes a cleaner, more maintainable codebase.

- **Model**: Represents the data layer of the application. It handles data operations and provides data to the ViewModel. The data can come from various sources such as a database or a network API.
- **View**: Represents the UI layer of the application. It displays the data provided by the ViewModel and forwards user interactions to the ViewModel.
- **ViewModel**: Acts as a bridge between the Model and the View. It holds and manages UI-related data and logic, and it communicates with the Model to retrieve data and update the View.

### Other Architectural Components

- **Repository Pattern**: A Repository class abstracts the data sources, providing a clean API for data access to the rest of the application.
- **LiveData**: Used to observe data changes in a lifecycle-aware manner. The ViewModel exposes LiveData objects, which the View observes to update the UI.
- **Room**: An ORM (Object Relational Mapping) library used for data persistence, providing an abstraction layer over SQLite.
- **Retrofit**: A type-safe HTTP client for Android and Java, used for network requests to the story API.

## Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. **Fork the project**
2. **Create your feature branch** (`git checkout -b feature/NewFeature`)
3. **Commit your changes** (`git commit -m 'Add some NewFeature'`)
4. **Push to the branch** (`git push origin feature/NewFeature`)
5. **Open a pull request**

## License

Distributed under the MIT License. See [LICENSE](https://github.com/raafihilmi/MyStoryApp/blob/main/LICENSE) for more information.

## Contact

Raafi Hilmi - [Your Email] - [Your LinkedIn or other contact]

Project Link: [https://github.com/raafihilmi/MyStoryApp](https://github.com/raafihilmi/MyStoryApp)

