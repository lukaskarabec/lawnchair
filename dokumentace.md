# Dokumentace Lawnchair

## Úvod
Lawnchair je bezplatná open‑source aplikace, která nahrazuje výchozí domovskou obrazovku systému Android. Projekt vychází z projektu Launcher3 (základní launcher Androidu) a přidává funkce známé z Pixel Launcheru a rozsáhlé možnosti přizpůsobení.

Tento dokument popisuje, jak projekt funguje a jak je strukturován. Je psán tak, aby byl srozumitelný i pro laiky.

## Co Lawnchair umí
- **Material You** – vzhled se přizpůsobuje tapetě a systémovému tématu.
- **At a Glance** – widget zobrazující důležité informace na první pohled.
- **Global Search** – hledání aplikací, kontaktů a webových výsledků přímo z domovské obrazovky.
- **QuickSwitch** – integrace s přehledem posledních aplikací (vyžaduje root).
- **Široké možnosti přizpůsobení** – úprava ikon, písma, rozložení mřížky či barvy rozhraní.

## Struktura repozitáře
```
├── AndroidManifest.xml    – základní manifest aplikace
├── build.gradle           – hlavní konfigurační soubor Gradle
├── gradle/                – skripty Gradle a wrapper
├── res/                   – obrázky a textové řetězce
├── src/                   – hlavní zdrojový kód v jazyce Kotlin
├── quickstep/             – část kódu zajišťující integraci s Přehledem aplikací
├── protos/                – definice protokolů pro komunikaci mezi částmi aplikace
└── docs/                  – doplňující dokumentace a obrázky
```

### Klíčové adresáře
- **`src/`** – Obsahuje hlavní logiku aplikace. Kód je psán převážně v jazyce Kotlin. Zde najdete implementaci domovské obrazovky, panelu aplikací, vyhledávání i nastavení.
- **`quickstep/`** – Umožňuje přechod mezi aplikacemi podobně jako v Pixel Launcheru. Komunikuje s Androidem na nízké úrovni a vyžaduje speciální oprávnění, pokud chcete QuickSwitch používat.
- **`res/`** – Zdrojové soubory, které vidí uživatel: ikony, barvy, layouty a překlady. Pokud chcete aplikaci přeložit, právě zde jsou uloženy textové řetězce.
- **`build/`** – Výstupní adresář vytvořený během kompilace. Zpravidla se nenahrává do repozitáře.
- **`docs/`** – Obsahuje doprovodné materiály, např. obrázky používané v README nebo další dokumentaci.

## Jak projekt sestavit
Lawnchair se sestavuje pomocí nástroje **Gradle**. Pro uživatele, kteří nemají s vývojem zkušenosti, je nejjednodušší použít **Android Studio**:

1. Nainstalujte Android Studio z [oficiálních stránek](https://developer.android.com/studio).
2. Na úvodní obrazovce zvolte `Get from VCS` a vložte adresu repozitáře GitHubu.
3. Po stažení Android Studio automaticky stáhne potřebné závislosti.
4. Pro vytvoření testovací (debug) verze klikněte na tlačítko **Run** nebo spusťte příkaz `./gradlew assembleDebug`.
5. Výsledný APK soubor najdete v `build/outputs/apk/debug/`.

> **Tip:** Pokud chcete aplikaci nainstalovat do telefonu bez kompilace, můžete ji stáhnout z Google Play nebo z GitHubu.

## Základní principy fungování
1. **Start aplikace:** Po instalaci se Lawnchair nastaví jako výchozí domovská obrazovka. Android po stisknutí tlačítka Domů spustí právě tuto aplikaci.
2. **Zobrazení ikon:** Lawnchair načte seznam nainstalovaných aplikací a pro každou zobrazí ikonu. Uživatel může ikony přesouvat, seskupovat do složek nebo je skrývat.
3. **Widgety:** Podporovány jsou widgety systému Android. Lawnchair poskytuje vlastní správu widgetů, aby bylo přidávání a úprava snadná.
4. **Vyhledávání:** Při zadání textu do vyhledávací lišty se prohledají místně nainstalované aplikace a případně se odešle dotaz na webový vyhledávač.
5. **Nastavení:** Všechny možnosti přizpůsobení jsou dostupné v Nastavení. Zde lze upravit vzhled, chování nebo záložní kopie.

## Jak přispívat
Vývoj Lawnchair je otevřený. Pokud chcete přispět:
1. Vytvořte si vlastní fork projektu na GitHubu.
2. Proveďte změny ve svém forku.
3. Otevřete pull request.
4. Sledujte pokyny v souboru `CONTRIBUTING.md`.

## Časté otázky
**Je Lawnchair bezpečný?**
Ano. Kód je open‑source, takže jej může kdokoliv zkontrolovat. Aplikace také prochází standardními bezpečnostními kontrolami před vydáním na Google Play.

**Co je QuickSwitch a proč potřebuje root?**
QuickSwitch umožňuje Lawnchair nahradit systémovou aplikaci pro přehled otevřených aplikací. Tato funkce sahá hluboko do systému a proto vyžaduje oprávnění root.

**Mohu Lawnchair používat bez znalostí programování?**
Ano. Dokumentace výše se zaměřuje především na práci vývojářů, ale samotné používání je snadné: nainstalujte aplikaci, nastavte ji jako výchozí launcher a začněte přizpůsobovat.

## Závěr
Lawnchair přináší uživatelům Androidu moderní a přizpůsobitelnou domovskou obrazovku. Díky otevřenému vývoji může kdokoli sledovat, jak aplikace funguje, nebo se zapojit do jejího vývoje.

