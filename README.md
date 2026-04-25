# most (Android MVP)

This repository contains the Android MVP for **most** — a messenger with trackable quests.

## Implemented now

- Firebase email/password auth + profile setup.
- Chat list screen with direct and group chats.
- Direct chat and group chat screens with message feed + inline quest cards.
- Quest creation from chat (direct and open group).
- Quest dashboard with tabs (Incoming, Saved, In Progress, Open Group, Personal, Created, Completed).
- Group quest board screen.
- Profile screen with counters.
- In-memory data source for chats/messages/quests to make MVP flows runnable end-to-end before full Firestore wiring.

## Run locally

1. Install Android Studio and Android SDK.
2. Add `app/google-services.json`.
3. Enable Email/Password in Firebase Authentication.
4. Sync Gradle and run app on emulator/device.

## Current architecture

- UI: Jetpack Compose + Material 3
- Presentation: ViewModels + StateFlow
- Data: Repository interfaces + Firebase auth/profile + in-memory chat/quest repository implementation
- Navigation: Compose Navigation routes

## Security note about Google API key

- `google-services.json` must never be committed (already ignored in `.gitignore`).
- If a leak is reported, rotate keys in Google Cloud Console and apply Android restrictions (package name + SHA-1/SHA-256).

## Next iteration focus

- Move chat/quest data layer from in-memory to Cloud Firestore.
- Add real-time updates and robust error handling around quest actions.
- Add bottom-sheet quest creation form with full fields.
- Add FCM notifications and unread counters.
