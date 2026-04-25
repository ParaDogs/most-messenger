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

## Troubleshooting auth error `CONFIGURATION_NOT_FOUND`

If Android logs show:

`RecaptchaCallWrapper ... RecaptchaAction(action=signUpPassword) ... [ CONFIGURATION_NOT_FOUND ]`

check these first:

1. Firebase Console → **Authentication** → **Sign-in method** → enable **Email/Password** and click Save.
2. Ensure `google-services.json` belongs to the same Firebase project where Email/Password was enabled.
3. Ensure Android package name in Firebase is exactly `com.most.messenger`.
4. Re-download `google-services.json` after any Firebase Console app/auth changes and replace local file.
5. Rebuild/reinstall the app (clear app data first).
6. If using API key restrictions in Google Cloud, temporarily remove restrictions for testing auth.

The app now logs Firebase runtime identity on startup (`projectId`, `appId`) so it is easier to spot project/config mismatches.

## Next steps TODO map

- **Step 3**: chat list data, chat messages, direct/group chat creation.
- **Step 4**: quest creation flow + quest message cards with actions.
- **Step 5**: quest dashboard and group quest board data aggregation.
- **Step 6**: XP updates and profile counters on completion.
