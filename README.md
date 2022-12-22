# MetalTraveller
FU-AU2022 Android Project II

En samling av lokaler, festivaler, barer och spelningar där man kan gå till, dricka öl och lyssna på metal. 

## Design

- MainActivity: welcome message, description and instructions, start button
- AuthActivity: create user, log in buttons
- Maps: map view, add markers with coordinates, see markers in map, button to see them in a list
- RecyclerView: scrollable list of places
- Log in every time the app starts?
- Work in progress....


### TO-DO
- [x] Main structure ✅ 2022-12-19
- [x] Firebase Auth ✅ 2022-12-19
- [x] Firebase Sync ✅ 2022-12-19
- [x] RecyclerView List ✅ 2022-12-19
- [x] Detailed Activity ✅ 2022-12-19
- [x] Expanded List Item ✅ 2022-12-19
- [x] RatingBar object ✅ 2022-12-22
- [x] Google Maps and Position Support ✅ 2022-12-22
- [ ] User and community lists
- [ ] Read data from db and `.toObject()` to populate obj list
- [ ] Expand DetailedView with more info
- [ ] Image support (from camera and/or google search)
- [ ] Material Design

### Steps
- [x] Learn Firestore ✅ 2022-12-09
- [x] Learn Firebase Auth ✅ 2022-12-09
- [x] Review RecycleView: [[6. RecycleView App]] ✅ 2022-12-19
- [ ] Design activities on Figma
- [ ] TBA


### Krav

#### G
- [x] Användaren skall själv kunna lägga till nya favoritställen ✅ 2022-12-19
- [x] Dessa ska tillsammans med alla andras platser visas upp i en recyclerView ✅ 2022-12-19
- [x] För att lagra all data skall Firebase firestore användas ✅ 2022-12-19
- [x] För att få lägga till egna favoritställen krävs att användaren är inloggad och då skall Firebase authentication användas. ✅ 2022-12-19
- [x] För att få mer information om ett ställe skall det kunna navigeras vidare från recyclerviewn till mer information om just det ställe användaren vill se mer information om. ✅ 2022-12-19

#### VG
- [x] För VG skall även favoritställen kunna visas upp på en karta. ✅ 2022-12-22
