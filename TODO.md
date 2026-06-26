# UES Projekat – Lista zadataka

> **Uputstvo za Claude:** 
> - Pre svakog zadatka pročitaj ceo njegov opis.
> - Kada završiš, stavi `[x]` i **nemoj više dirati taj zadatak**.
> - Uvek kreiraj fajlove na tačno navedenim mestima.
> - Poštuj Java i Angular konvencije (Spring Boot paketi, Angular komponente).
> - Kada pišeš kod, dodaj i osnovne logove (log4j2).

---

## Faza 0: Inicijalizacija projekta
- [x] **0.1** Kreirati Spring Boot projekat (Maven) sa dependencies: Web, JPA, MySQL, Security, Validation, Mail, Elasticsearch, MinIO, Log4j2.
- [x] **0.2** Kreirati Angular projekat sa routing-om, dodati Angular Material i Chart.js.
- [x] **0.3** Konfigurisati `application.properties` za MySQL, MinIO, Elasticsearch, email.
- [x] **0.4** Dodati `data.sql` za inicijalnog administratora (email: admin@ues.com, password hash).
- [x] **0.5** Dodati `data.sql` sa admin korisnikom:
  - email: `admin@ues.com`
  - password: BCrypt hash od `admin123`
  - uloga: `ROLE_ADMIN`
- [x] **0.6** Konfigurisati CORS za Angular (localhost:4200).

---

## Faza 1: Model podataka (JPA entiteti)
- [x] **1.1** Kreirati `model/AccountRequest.java` (sa enum `RequestStatus`).
- [x] **1.2** Kreirati `model/User.java` (osnovni korisnik).
- [x] **1.3** Kreirati `model/Administrator.java` (extends User, bez dodatnih polja).
- [x] **1.4** Kreirati `model/Location.java` (sa vezama ka Image, DescriptionDocument, Event, Review, Manages).
- [x] **1.5** Kreirati `model/Event.java` (sa vezom ka Location i Image).
- [x] **1.6** Kreirati `model/Review.java` (sa vezama ka User, Location, Event, Rate, Comment).
- [x] **1.7** Kreirati `model/Rate.java` (ocene).
- [x] **1.8** Kreirati `model/Comment.java` (sa samoreferencirajućom vezom).
- [x] **1.9** Kreirati `model/Image.java`.
- [x] **1.10** Kreirati `model/DescriptionDocument.java`.
- [x] **1.11** Kreirati `model/Manages.java` (veza User-Location sa datumima).
- [x] **1.12** Kreirati JPA repozitorijume za sve entitete (`repository/` paket).

---

## Faza 2: Autentifikacija i autorizacija
- [x] **2.1** Implementirati `UserDetailsService` (kroz `User`).
- [x] **2.2** Implementirati JWT filter i `AuthenticationManager`.
- [x] **2.3** Napisati `controller/AuthController.java` sa endpointima:
  - `POST /api/auth/register` (kreira AccountRequest)
  - `POST /api/auth/login` (vraća JWT)
- [x] **2.4** Dodati `controller/AdminController.java` za obradu zahteva (samo admin):
  - `GET /api/admin/account-requests`
  - `PUT /api/admin/account-requests/{id}/accept`
  - `PUT /api/admin/account-requests/{id}/reject`
- [x] **2.5** Nakon prihvatanja, kreirati `User` i poslati email (mock ili JavaMail).
- [x] **2.6** Implementirati logout (na frontendu brisanje tokena, opciono blacklist na back-endu).
- [x] **2.7** Dodati `@PreAuthorize` anotacije na sve kontrolere (ADMIN, MANAGER, USER).

---

## Faza 3: Upravljanje mestima (Location) – administrator
- [x] **3.1** Kreirati `controller/AdminLocationController.java` (samo admin):
  - `POST /api/admin/locations` (sa slikom)
  - `PUT /api/admin/locations/{id}` (sva polja)
  - `DELETE /api/admin/locations/{id}`
- [x] **3.2** Implementirati MinIO upload za sliku (`util/MinioUtil.java`).
- [x] **3.3** Sačuvati `Image` entitet i povezati sa `Location`.
- [x] **3.4** Dodati `controller/ManagerLocationController.java` (samo menadžer):
  - `PUT /api/manager/locations/{id}` (samo adresa, tip, opis)
- [x] **3.5** Omogućiti prikaz mesta sa ocenom (izračunato).

---

## Faza 4: Upravljanje događajima (Event) – menadžer
- [x] **4.1** Kreirati `controller/ManagerEventController.java`:
  - `POST /api/manager/locations/{locationId}/events` (sa slikom)
  - `PUT /api/manager/events/{id}`
  - `DELETE /api/manager/events/{id}`
- [x] **4.2** Slika se čuva u MinIO, kreira se `Image` entitet.

---

## Faza 5: Recenzije (Review) i ocene (Rate)
- [x] **5.1** Kreirati `controller/ReviewController.java`:
  - `POST /api/locations/{locationId}/reviews` (samo prijavljeni)
  - Validacija: događaj mora biti redovan i već prošao.
- [x] **5.2** Pri kreiranju izračunati `eventCount` (broj pojavljivanja tog događaja do datuma).
- [x] **5.3** Sačuvati `Review`, `Rate`, opcioni komentar (koreni).
- [x] **5.4** Ažurirati `Location.totalRating` (prosek svih važećih recenzija).
- [x] **5.5** Kreirati endpoint za prikaz recenzija sa sortiranjem (po oceni i datumu).
- [x] **5.6** Dodati `deleted` boolean polje u `Review` (tehnički dodatak za logičko brisanje).
- [x] **5.7** Precizirati računanje `eventCount`: broj pojavljivanja događaja sa istim `name` i `type` na istom mestu do datuma recenzije.

---

## Faza 6: Komentari (Comment) i odgovori
- [x] **6.1** Kreirati `controller/CommentController.java`:
  - `POST /api/reviews/{reviewId}/comments` – koreni komentar (od korisnika)
  - `POST /api/comments/{commentId}/replies` – odgovor (menadžer ili korisnik)
- [x] **6.2** Implementirati logiku: menadžer može odgovoriti na bilo koji komentar; običan korisnik samo na komentar menadžera (proveriti).
- [x] **6.3** Omogućiti prikaz stabla komentara.

---

## Faza 7: Pretraga i filtriranje (SQL)
- [x] **7.1** Kreirati `controller/LocationController.java` (javni, ali za prijavljene):
  - `GET /api/locations` sa parametrima: naziv, adresa, tip (pretraga po LIKE).
- [x] **7.2** Kreirati `controller/EventController.java`:
  - `GET /api/events` sa parametrima: tip, mesto (ID), adresa, cena (min/max), datum (od-do).
  - `GET /api/events/today` – današnji događaji.
- [x] **7.3** Kreirati `controller/HomeController.java`:
  - `GET /api/home/popular-locations` (top 5 po oceni)
  - `GET /api/home/latest-reviews` (3 najskorija sa najpopularnijeg mesta)

---

## Faza 8: Profil korisnika i promena lozinke
- [ ] **8.1** Kreirati `controller/UserController.java`:
  - `GET /api/users/profile`
  - `PUT /api/users/profile` (izmena imena, telefona, rođendana, adrese, grada, slike)
  - `PUT /api/users/change-password` (stara + nova dva puta)
- [ ] **8.2** Nakon promene lozinke poslati email.
- [ ] **8.3** Dodati endpoint `GET /api/users/reviews` – svi utisci korisnika.
- [ ] **8.4** Dodati endpoint `GET /api/users/managed-locations` – mesta na kojima je menadžer.

---

## Faza 9: Menadžerske funkcije (sakrivanje, brisanje recenzija, analitika)
- [ ] **9.1** Kreirati `controller/ManagerReviewController.java`:
  - `GET /api/manager/locations/{locationId}/reviews` (sve recenzije)
  - `PUT /api/manager/reviews/{id}/hide` (postavi hidden=true)
  - `DELETE /api/manager/reviews/{id}` (logičko brisanje – ne računa se ocena)
- [ ] **9.2** Pri brisanju recenzije, ažurirati `Location.totalRating` i ukloniti iz ES indeksa (kasnije).
- [ ] **9.3** Kreirati `controller/ManagerAnalyticsController.java`:
  - `GET /api/manager/locations/{locationId}/analytics` sa parametrima: period (nedeljni, mesečni, godišnji, proizvoljni opseg)
  - Vratiti: ukupan broj događaja, podelu na redovne/neredovne, besplatne/plaćene, top liste događaja i mesta po prosečnim ocenama, 3 najskorija utiska sa najpopularnijeg mesta.

---

## Faza 10: Administratorske funkcije (dodela menadžera)
- [ ] **10.1** U `controller/AdminController.java` dodati:
  - `POST /api/admin/locations/{locationId}/managers` (dodeli korisnika sa startDate)
  - `DELETE /api/admin/locations/{locationId}/managers/{userId}` (postavi endDate)
- [ ] **10.2** Validirati da korisnik postoji i da nije već aktivan menadžer na tom mestu.

---

## Faza 11: UES – Elasticsearch indeksiranje i MinIO PDF
- [ ] **11.1** Kreirati `model/LocationIndex.java` (Spring Data Elasticsearch dokument) sa svim poljima.
- [ ] **11.2** Kreirati `repository/LocationSearchRepository.java` (extends ElasticsearchRepository).
- [ ] **11.3** Kreirati `config/ElasticsearchConfig.java` sa custom analyzer-om (ćirilica→latinica, lowercase, stop reči).
- [ ] **11.4** Implementirati sinhronizaciju: pri svakoj promeni mesta (kreiranje, izmena, brisanje recenzije) ažurirati ili indeksirati `LocationIndex`.
- [ ] **11.5** Implementirati PDF upload za mesto (MinIO):
  - Dodati endpoint `POST /api/admin/locations/{id}/pdf` (samo admin)
  - Parsirati PDF (Apache PDFBox) i izvući tekst.
  - Sačuvati PDF u MinIO, kreirati `DescriptionDocument`, upisati tekst u `LocationIndex.fileDescription`.

---

## Faza 12: UES – Napredna pretraga (S1)
- [ ] **12.1** Kreirati `controller/SearchController.java` sa endpointom:
  - `GET /api/search/locations` sa svim parametrima (naziv, opis, pdf, reviewCount opseg, ocene opsezi, boolean operator, specijalni upiti, more like this).
- [ ] **12.2** Implementirati logiku pretrage:
  - Detekcija `"..."` (phrase), `*` (prefix), `~` (fuzzy).
  - Kombinovanje `BoolQueryBuilder` sa AND/OR.
  - Dodavanje `MoreLikeThisQuery` na osnovu `name`, `description`, `fileDescription`.
  - Highlight na `name` i `description`.
- [ ] **12.3** Sortiranje po nazivu (koristiti `name.sort` keyword).
- [ ] **12.4** Dodati endpoint `GET /api/search/locations/{id}/pdf` za preuzimanje PDF-a iz MinIO.

---

## Faza 13: Frontend – Angular struktura i osnovne komponente
- [ ] **13.1** Kreirati module: `AuthModule`, `HomeModule`, `LocationModule`, `EventModule`, `ProfileModule`, `AdminModule`, `ManagerModule`, `SearchModule`.
- [ ] **13.2** Kreirati servise: `AuthService`, `UserService`, `LocationService`, `EventService`, `ReviewService`, `CommentService`, `AdminService`, `ManagerService`, `SearchService`.
- [ ] **13.3** Kreirati modele (klase/interfejsi) za sve entitete.
- [ ] **13.4** Implementirati `AuthGuard` za zaštitu ruta.
- [ ] **13.5** Dodati `RoleGuard` koji proverava da li korisnik ima dozvoljenu ulogu za rutu.

---

## Faza 14: Frontend – Stranice (komponente)
- [ ] **14.1** `LoginComponent` i `RegisterComponent` (sa formama).
- [ ] **14.2** `HomeComponent` – prikaz današnjih događaja, popularnih mesta, najnovijih utisaka.
- [ ] **14.3** `LocationListComponent` – lista mesta sa pretragom (naziv, adresa, tip).
- [ ] **14.4** `LocationDetailComponent` – detalji mesta, događaji, recenzije sa sortiranjem.
- [ ] **14.5** `EventListComponent` – lista događaja sa filtriranjem (tip, mesto, cena, datum).
- [ ] **14.6** `CreateReviewComponent` – forma za recenziju (biranje događaja, ocene, komentar).
- [ ] **14.7** `UserProfileComponent` – prikaz profila, izmena podataka, slika, promena lozinke.
- [ ] **14.8** `AdminDashboardComponent` – upravljanje zahtevima, mestima, menadžerima.
- [ ] **14.9** `ManagerDashboardComponent` – upravljanje događajima, recenzijama (sakrivanje/brisanje), analitika sa grafikonima (Chart.js).

---

## Faza 15: Frontend – UES pretraga
- [ ] **15.1** `SearchComponent` – napredna forma sa svim parametrima (naziv, opis, PDF, opsezi, boolean operator, specijalni upiti, MLT).
- [ ] **15.2** Prikaz rezultata sa highlight-om i sortiranjem po nazivu.
- [ ] **15.3** Dugme za preuzimanje PDF-a za svako mesto.

---

## Faza 16: Testiranje, logovanje i dorade
- [ ] **16.1** Ručno testirati sve funkcionalnosti kroz UI.
- [ ] **16.2** Proveriti logove (log4j2) – svi važni događaji se beleže.
- [ ] **16.3** Popraviti sve uočene bagove.
- [ ] **16.4** Dodati još neke sitne detalje (validacije, lepši UI).