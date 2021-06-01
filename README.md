# Battleships_PA_2021

Repository-ul pentru proiectul de final de semestru. Tema aleasa este jocul `Battleships`, echipa fiind formata din __Goanta Matei (2B5)__ si __Vlad Theodor (2B5)__.

Contributia membrilor echipei se poate gasi mai jos.

## Goanta Matei, 2B5
* Implementare server-side si dezvoltare jucator AI pentru modul Singleplayer
* Server-ul accepta continuu clienti pentru care porneste un `ClientThread` si ii asociaza unui joc Singleplayer sau Multiplayer, in functie de alegere
* Clasa `MultiplayerManager` se ocupa de administrarea sesiunilor de joc intre doi clienti locali
* In cadrul `SingleGameThread` si `MultiGameThread` serverul se ocupa de logica partidei, trimite si receptioneaza comenzi de la client, updateaza tablele de joc ale acestora, determina cand s-a terminat jocul
* Pentru modul Singleplayer AI-ul este realizat dupa un design propriu, efectueaza mutarile dupa mai multe reguli prestabilite.
* Pana in momentul in care loveste un camp ocupat de barca, acest spatiu este cautat verificandu-se ca acel spatiu sa nu fie adiacent unei barci deja distruse (deoarece barcile nu pot fi plasate una langa cealalta), iar in acel camp este destul spatiu ramas pentru eventuala pozitionare a unei barci (e.g.: nu poate exista o barca de 5 intr-un spatiu de 2).
* In momentul in care este lovit un camp, se incearca distrugerea barcii prin lovirea in campuri adiacente pe aceeasi linie sau pe aceeasi coloana.
* Creare executabile `Client.jar`, `Server.jar` (jocul poate fi pornit prin deschiderea serverului urmata de deschiderea clientului sau clientilor pentru modul Multiplayer)
* Ecran How To Play pentru Client GUI

## Vlad Theodor, 2B5

* Implementare client-side si intefata grafica pentru client
* GUI implementat folosind `Java Swing`, sub forma unei colectii de `JFrame`-uri administrate de clasa `WindowManager`
* Fiecare `JFrame` este populat folosind elemente de tip `JPanel`, `JLabel`, `JButton`, `JTextField` etc.
* Ecranele pentru pozitionarea barcilor si pentru jocul propriu-zis se folosesc de componente de tip `Graphics2D` si `BufferedImage` pentru a reda efectului tablei de joc
* Detectarea atat a pozitionarii incorecte a barcilor (prin suprapunere/adiacenta/iesire inafara tablei de joc), cat si a miscarilor invalide in timpul jocului (mutarea intr-un spatiu incercat anterior)
* Pentru conexiunea la server, clasa singleton `GameConnection` a fost implementata
* Comunicarea cu serverul in timpul jocului se realizeaza pe un thread separat (folosind un `SwingWorker`) pentru a nu bloca GUI-ul jucatorului
* Suport pentru modul de joc din linia de comanda
