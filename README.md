# Phone Detox App 
 A project that replicates the one-sec app, which helps people reduce their phone usage by asking them to select apps that they want to block and when they try to open them, they will be asked to wait for a second to think about whether they really want to open the app or not. If not, they should do journaling instead.

## Tasks
- [x] Main screen UI: 3 tabs: Home, Statistics, Journaling
- [x] Select blocked apps and store them in a local database
  - [x] UI for selecting apps
  - [x] Save the selected apps in a local database
  - [x] Retrieve the selected apps from the database and show them in the UI
- [ ] Detect when a blocked app is opened - using Foreground Service + WorkerMananger
    - [x] Detect when a blocked app is opened
    - [x] Show UI asking the user to wait for a second or do journaling
    - [ ] If the user wants to open the app, open the app and increase the number of times the user opened the app
- [ ] Statistics tab: how many times the user opened the blocked apps in a day, week, month, year
- [ ] Journaling feature: 
    - [ ] Add a journal
    - [ ] View all journals
    - [ ] Send weekly report to the user

## Used technologies: 
- Language: Kotlin
- UI: Jetpack Compose
- Database: Room (with proper migration policies)
- Foreground Service + WorkerManager
- Dependency Injection: Dagger-Hilt
