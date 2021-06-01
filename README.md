# Battleships_PA_2021

Repository-ul pentru proiectul de final de semestru. Tema aleasa este jocul `Battleships`, echipa fiind formata din __Goanta Matei (2B5)__ si __Vlad Theodor (2B5)__.

Contributia membrilor echipei se poate gasi mai jos.

## Goanta Matei, 2B5
TODO

## Vlad Theodor, 2B5

* Implementare client-side si intefata grafica pentru client
* GUI implementat folosind `Java Swing`, sub forma unei colectii de `JFrame`-uri administrate de clasa `WindowManager`
* Fiecare `JFrame` este populat folosind elemente de tip `JPanel`, `JLabel`, `JButton`, `JTextField` etc.
* Ecranele pentru pozitionarea barcilor si pentru jocul propriu-zis se folosesc de componente de tip `Graphics2D` si `BufferedImage` pentru a reda efectului tablei de joc
* Detectarea atat a pozitionarii incorecte a barcilor (prin suprapunere/adiacenta/iesire inafara tablei de joc), cat si a miscarilor invalide in timpul jocului (mutarea intr-un spatiu incercat anterior)
* Pentru conexiunea la server, clasa singleton `GameConnection` a fost implementata
* Comunicarea cu serverul in timpul jocului se realizeaza pe un thread separat (folosind un `SwingWorker`) pentru a nu bloca GUI-ul jucatorului
* Suport pentru modul de joc din linia de comanda