# Walkthrough - Balkan Clicker 2.0 "The Big Move"

The **Balkan Clicker 2.0** update is a major overhaul of the game, introducing persistence, prestige mechanics, and deep localization. The app is now **fully localized in Bulgarian**, providing an authentic experience for the Balkan market.

## 🇧🇬 Fully in Bulgarian
The entire user interface, including upgrade descriptions, events, and stats, has been translated to Bulgarian.
- **App Name**: Балкански Кликър
- **Shop**: Балкански Базар
- **Prestige**: Емиграция

## 📱 Phone-Optimized Layout
Based on user feedback, the **Market (Bazaar) is now back below the coin** on phone devices. This allows for a seamless vertical scrolling experience where you can click and buy upgrades without switching screens.

## 🚀 The 6-Point Mega-Update

### 1. Room Persistence
Your progress is now safe! We implemented a **Room Database** to save your score, lifetime stats, and purchased upgrades.
- **Offline Progress**: The game remembers where you left off.
- **Data Integrity**: Uses a robust relational mapping for owned upgrades.

### 2. Prestige System: Emigration (Емиграция)
When you've earned enough in the Balkans, you can choose to "Emigrate" to the West for a fresh start with a massive boost.
- **Threshold**: Requires at least **100,000** total lifetime coins.
- **Points**: You gain **1 Prestige Point** for every 100,000 lifetime coins.
- **Multiplier**: Each point adds a permanent **+10% bonus** to all future income.
- **Reset**: Resets your current balance and upgrades, but your lifetime stats and multiplier remain.

### 3. New Special Upgrades
Two game-changing upgrades have been added to the Bazaar:
- **Municipal Connections (Връзки в общината)**: Having a "cousin" in the municipality pays off. Each level reduces the cost of all other upgrades by **5%** (up to 50%).
- **IT Nephew (Племенник ИТ-специалист)**: Your nephew "knows about computers." He provides auto-clicking power, clicking **2 times per second** per level automatically.

### 4. Regional Themes & Dynamic Currency
As you earn more, your currency evolves to reflect your rising status.
- **Currencies**: Starts with Bulgarian Lev (BGN), eventually reaching the Euro (EUR) and beyond.
- **Icons**: Adaptive icons that change based on your current "Regional Theme."

### 5. Random Events Manager
The Balkans are unpredictable! Random events can now trigger during gameplay:
- **Balkan Wedding (Балканска сватба)**: 2x income for a limited time! 🎺
- **Hyperinflation (Хиперинфлация)**: Your currency loses value, resulting in 0.5x income. 📉

### 6. Enhanced UX & Haptics
The game feels "crunchier" than ever:
- **Haptic Feedback**: Every click, upgrade purchase, and event trigger provides tactile feedback.
- **Animated Banners**: Smooth entry/exit animations for events using `AnimatedVisibility`.
- **Floating Text**: Vibrant "+X" indicators that pop out and fade away when you click.

---

## 🛠️ Verification Summary
- **Unit Tests**: All 13 tests passed, covering persistence logic, prestige calculation, and upgrade discounts.
- **Build**: Successfully assembled `debug` variant.
- **UI**: Verified vertical scrolling layout on phone-sized previews.
