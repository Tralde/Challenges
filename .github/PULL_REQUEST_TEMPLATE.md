<!-- INFORMATION ABOUT THE PR MUST BE GIVEN -->
<!-- CHECK IF IMPORTANT INFORMATION SHOULD BE INCLUDED IN THE README.MD -->

This pull request includes:

- [ ] new release (master branch)
- [x] new feature (development branch)
- [ ] bug fix (development branch)

### Changes made to the repository:

Added a new Multiplayer Randomizer challenge category with several per-player randomizer challenge variants.

The goal of this change is to provide randomizer challenges where random results are calculated individually per player instead of globally for all players.

Added:

- New setting category:
  - MULTIPLAYER_RANDOMIZER

- New shared helper/base logic:
  - PlayerRandomizerSetting

- New challenge implementations:
  - PlayerBlockRandomizerChallenge
  - PlayerChunkRandomEffectChallenge
  - PlayerRandomItemChallenge
  - PlayerRandomItemDroppingChallenge
  - PlayerRandomItemRemovingChallenge
  - PlayerRandomItemSwappingChallenge

- New language keys:
  - category-multiplayer_randomizer
  - item-player-block-randomizer-challenge
  - item-player-chunk-effect-challenge
  - item-player-random-item-challenge
  - item-player-random-dropping-challenge
  - item-player-random-removing-challenge
  - item-player-random-swapping-challenge

Main affected files:

plugin/src/main/java/net/codingarea/challenges/plugin/challenges/implementation/challenge/multiplayer/
plugin/src/main/java/net/codingarea/challenges/plugin/challenges/type/abstraction/PlayerRandomizerSetting.java
plugin/src/main/java/net/codingarea/challenges/plugin/management/challenges/ChallengeLoader.java
plugin/src/main/java/net/codingarea/challenges/plugin/management/menu/generator/categorised/SettingCategory.java
language/files/de.json
language/files/en.json



### Documentation of test results:

Confirmed:

Plugin builds successfully after resolving local build dependencies.
New Multiplayer Randomizer category appears in the challenge menu.
New challenge items are visible in the menu.
Language keys display correctly after updating the runtime language files.
New challenges can be enabled through the menu.

Basic functional checks:

PlayerBlockRandomizerChallenge gives player-specific block drops.
PlayerChunkRandomEffectChallenge applies effects when entering chunks.
Timed player-randomizer item challenges appear in the menu and can be activated.

Notes:

Existing global randomizer challenges were not modified.
The new challenges were implemented as separate variants to avoid changing existing challenge behavior.
Language files may need to be bundled or synchronized with the runtime plugins/Challenges/messages/*.json files, depending on the deployment setup.

DE

This pull request includes:

- [ ] new release (master branch)
- [x] new feature (development branch)
- [ ] bug fix (development branch)

## Changes made to the repository:

In diesem Pull Request wurde eine neue Challenge-Kategorie für spielerspezifische Randomizer ergänzt.

Ziel der Änderung ist es, Randomizer-Challenges bereitzustellen, bei denen Zufallsergebnisse nicht global für alle Spieler gleich berechnet werden, sondern individuell pro Spieler. Dadurch können Multiplayer-Challenges entstehen, bei denen jeder Spieler eigene Blockdrops, Effekte oder Item-Ereignisse erhält.

Ergänzt wurde:

- Neue Kategorie:
  - MULTIPLAYER_RANDOMIZER

- Neue gemeinsame Hilfs-/Basisklasse:
  - PlayerRandomizerSetting

- Neue Challenge-Implementierungen:
  - PlayerBlockRandomizerChallenge
  - PlayerChunkRandomEffectChallenge
  - PlayerRandomItemChallenge
  - PlayerRandomItemDroppingChallenge
  - PlayerRandomItemRemovingChallenge
  - PlayerRandomItemSwappingChallenge

- Neue Sprachkeys für Deutsch und Englisch:
  - category-multiplayer_randomizer
  - item-player-block-randomizer-challenge
  - item-player-chunk-effect-challenge
  - item-player-random-item-challenge
  - item-player-random-dropping-challenge
  - item-player-random-removing-challenge
  - item-player-random-swapping-challenge

Betroffene zentrale Dateien:

plugin/src/main/java/net/codingarea/challenges/plugin/challenges/implementation/challenge/multiplayer/
plugin/src/main/java/net/codingarea/challenges/plugin/challenges/type/abstraction/PlayerRandomizerSetting.java
plugin/src/main/java/net/codingarea/challenges/plugin/management/challenges/ChallengeLoader.java
plugin/src/main/java/net/codingarea/challenges/plugin/management/menu/generator/categorised/SettingCategory.java
language/files/de.json
language/files/en.json

### Dokumentation der Testergebnisse:

Bestätigt:

Das Plugin lässt sich erfolgreich bauen, nachdem die lokalen Build-Abhängigkeiten aufgelöst wurden.  
Die neue Kategorie Multiplayer Randomizer`erscheint im Challenge-Menü.  
Die neuen Challenge-Items sind im Menü sichtbar.  
Die Sprachkeys werden korrekt angezeigt, nachdem die zur Laufzeit verwendeten Sprachdateien aktualisiert wurden.  
Die neuen Challenges können über das Menü aktiviert werden.

Grundlegende Funktionstests:

PlayerBlockRandomizerChallenge erzeugt spielerspezifische Blockdrops.  
PlayerChunkRandomEffectChallenge wendet Effekte beim Betreten neuer Chunks an.  
Die zeitbasierten Player-Randomizer-Item-Challenges erscheinen im Menü und können aktiviert werden.

Hinweise:

Die bestehenden globalen Randomizer-Challenges wurden nicht verändert.  
Die neuen Challenges wurden als separate Varianten umgesetzt, um das bestehende Verhalten nicht zu verändern.  
Je nach Deployment-Setup müssen die Sprachdateien möglicherweise in die JAR eingebunden oder mit den zur Laufzeit verwendeten Dateien unter plugins/Challenges/messages/*.json synchronisiert werden.
