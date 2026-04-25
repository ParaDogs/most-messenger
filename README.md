# most (Android MVP)

This repository contains the MVP foundation for **most**, a messenger app with trackable quests.

## Implemented in this iteration (Step 1 + Step 2)

- Project scaffolding with Kotlin + Jetpack Compose + Material 3.
- Core domain models and enums for users, chats, messages, quests.
- Repository interfaces and Firebase-backed repository classes.
- Firebase Auth implementation (email/password sign in and sign up).
- Firestore profile create/update + profile observing repository methods.
- Compose Navigation graph and routes.
- Auth screen and Profile Setup screen wired to ViewModels.
- Placeholder screens for the next MVP steps.

## Run locally

1. Install Android Studio (latest stable) and Android SDK.
2. Add a Firebase config file at `app/google-services.json`.
3. Open the project in Android Studio.
4. Sync Gradle.
5. Run the `app` configuration on an emulator/device.

## Firebase assumptions

- Firebase project exists and Android app is registered.
- Authentication provider: **Email/Password** is enabled.
- Firestore is enabled in Native mode.
- Collections used by MVP foundation:
  - `users/{userId}`
  - `chats/{chatId}`
  - `chats/{chatId}/messages/{messageId}`
  - `quests/{questId}`

## Next steps TODO map

- **Step 3**: chat list data, chat messages, direct/group chat creation.
- **Step 4**: quest creation flow + quest message cards with actions.
- **Step 5**: quest dashboard and group quest board data aggregation.
- **Step 6**: XP updates and profile counters on completion.
