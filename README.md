# KNN - Aufgabe 1

## Benutzung:

die Implementationen der Aufgaben sind in dem Pakte Runner hinterlegt. Alle Nötigen Klassen im Bereich model.
Für die Ausführung ist als 1. Eingabe der Bitstream anzugeben und ggf. (für Aufgaben 3,4) der Wert von n als zweite Eingabe.

Aufgrund meiner Implementierung wird das Netz back-to-front ausgewertet. Dies führt dazu, dass die eigentlich berechneten Werte in der Ausgabe verzögert ankommen, sodass (abhängig von der Netztiefe) zuerst Werte ausgegeben werde, die nicht mit den eigentlichen Eingaben in Verbindung stehen.

Für den Aufbau eines Netz im allgemein wird vorausgesetzt, dass die Dimension der Eingabe und Ausgabe bekannt ist, sowie eine Netztiefe vorgegeben ist.
Beim Aufbau des Netzes müssen (back-to-front) Neuronen erstellt werden. Für jedes Neuron muss bei der Erstellung Eine Aktivierungsfunktion, eine Netzeingabe, sowie eine Ausgabefunktion vorgegeben werden. Diese Funktionen sind jedoch mit Funktionen mit gleichdimensionalen Ein- und Ausgabewerten ersetzbar.
Neuronen können mittels den Funktionen activate und deactivate Ein- und Ausgeschaltet werden. Nach Einscahltung aller Nötigen Neuronen (oder allen Neuronen wenn über die Netzfunktion aktiviert wird) kann das Netz mittels der Funktion run gestartet werden.
Das Netz bricht sich selbst ab, wenn alle Eingaben ausgewertet wurden und keine Zustandsänderung innerhalb der Ausgaben der Neuronen erfolgt.

## Schätzung der Neuronengrößen:
Alle Angaben sind inklusive Ausgabe und Eingabebeuronen

Für Aufgabe 1 und 2 sind 3 Neuronen erforderlch, das innere Neuron speichert und vergleicht hierbei mit dem Ergebnis aus dem letzten Takt.
Für Aufgabe 3 kann dies zu dem Wert (n+1) verallgemeinert werden. Wobei n hier größer als 0 sein muss.
Für Aufgabe 4 sind 2n+1 Neuronen nötig. 2n Neuronen geben hierbei die Bits des Palindroms an das Ausgabeneuron weiter, welches anschließend die Palindromeigenschaft mittels Kantengewichte analysieren kann.
