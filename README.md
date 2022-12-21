# MetalTraveller
FU-AU2022 Android Project II

En samling av lokaler, festivaler, barer och spelningar där man kan gå till, dricka öl och lyssna på metal. 

## Design

- MainActivity: welcome message, description and instructions, start button
- AuthActivity: create user, log in buttons
- Maps: map view, add markers with coordinates, see markers in map, button to see them in a list
- RecyclerView: scrollable list of places
- Expand Item View with details
- Work in progress....


### Steps
- [x] Learn Firestore ✅ 2022-12-09
- [x] Learn Firebase Auth ✅ 2022-12-09
- [x] Review RecycleView: [[6. RecycleView App]] ✅ 2022-12-16
- [ ] TBA

### TODO
- [x] #todo Delete right item from RecyclerView from item delete button ✅ 2022-12-16
- [x] #todo Delete document from Firebase ✅ 2022-12-16
- [x] #todo Add new activity with item details ✅ 2022-12-20
- [x] #todo Add insert coordinates function ✅ 2022-12-21
- [x] #todo Add map with markers ✅ 2022-12-20
- [ ] #todo Firebase auth: log in every time you open the app?
- [ ] #todo Add auth.currentUser (video 7 december)
- [ ] #todo Update list from Firebase firestore so that each user can add items to the community list
- [ ] #todo Add Material Design to app
- [ ] #todo Error handling

### IMPROVE
- [ ] #IMPROVE SnapshotListener, read from db and convert to object
- [ ] #IMPROVE Code cleanup
- [ ] #IMPROVE Gallery from google search in DetailedView
- [ ] #IMPROVE Material Design


### Problems
- [x] LatLng in object crashes app ✅ 2022-12-21 
- [x] Markers and map not working/crashing ✅ 2022-12-21
- [x] Delete entry from database ✅ 2022-12-21
- [x] Add latitude and logitude ✅ 2022-12-21
- [ ] Add location from map





### Krav

#### G
- [x] Användaren skall själv kunna lägga till nya favoritställen ✅ 2022-12-16
- [x] Dessa ska tillsammans med alla andras platser visas upp i en recyclerView ✅ 2022-12-14
- [x] För att lagra all data skall Firebase firestore användas ✅ 2022-12-19
- [x] För att få lägga till egna favoritställen krävs att användaren är inloggad och då skall Firebase authentication användas. ✅ 2022-12-09
- [x] För att få mer information om ett ställe skall det kunna navigeras vidare från recyclerviewn till mer information om just det ställe användaren vill se mer information om. ✅ 2022-12-19

#### VG
- [ ] För VG skall även favoritställen kunna visas upp på en karta.
