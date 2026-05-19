
import java.util.*;

// ============================================================
//  DUNGEON RAID RPG  —  Complete Single-File Java Implementation
// ============================================================

public class DungeonRaid {

    // ───────────────────────────── CONSTANTS ─────────────────────────────
    static final int INVENTORY_CAPACITY = 20;
    static final int TOTAL_FLOORS       = 5;
    static final Random RNG             = new Random();

    // ══════════════════════════════════════════════════════════════════════
    //  DATA CLASSES
    // ══════════════════════════════════════════════════════════════════════

    // ─────────────────────────── Item ────────────────────────────────────
    static class Item {
        String name;
        int    quantity;
        String rarity;   // Common / Uncommon / Rare / Epic
        int    sellValue;
        String type;     // HealthPotion / ManaPotion / BuffPotion / Material / Drop / Equipment

        Item(String name, int quantity, String rarity, int sellValue, String type) {
            this.name      = name;
            this.quantity  = quantity;
            this.rarity    = rarity;
            this.sellValue = sellValue;
            this.type      = type;
        }

        @Override public String toString() {
            return String.format("%-20s x%-3d [%-9s] Sell: %dg", name, quantity, rarity, sellValue);
        }
    }

    // ─────────────────────────── Equipment ───────────────────────────────
    static class Equipment {
        String name;
        String slot;          // Weapon / Armor
        int    cost;
        int    durability;
        int    maxDurability;
        int    atkBonus;
        int    defBonus;
        int    hpBonus;
        int    mpBonus;
        int    levelReq;
        String rarity;
        int    sellValue;

        Equipment(String name, String slot, int cost, int durability,
                  int atkBonus, int defBonus, int hpBonus, int mpBonus,
                  int levelReq, String rarity) {
            this.name          = name;
            this.slot          = slot;
            this.cost          = cost;
            this.durability    = durability;
            this.maxDurability = durability;
            this.atkBonus      = atkBonus;
            this.defBonus      = defBonus;
            this.hpBonus       = hpBonus;
            this.mpBonus       = mpBonus;
            this.levelReq      = levelReq;
            this.rarity        = rarity;
            this.sellValue     = cost / 2;
        }

        boolean isBroken()  { return durability <= 0; }
        int effectiveAtk()  { return isBroken() ? 0 : atkBonus; }
        int effectiveDef()  { return isBroken() ? 0 : defBonus; }
        int effectiveHp()   { return isBroken() ? 0 : hpBonus; }

        @Override public String toString() {
            String broken = isBroken() ? " [BROKEN]" : "";
            return String.format("%-22s [%s] ATK+%d DEF+%d HP+%d  DUR:%d/%d  LvReq:%d  [%s]%s",
                name, slot, atkBonus, defBonus, hpBonus,
                durability, maxDurability, levelReq, rarity, broken);
        }
    }

    // ─────────────────────────── Monster ─────────────────────────────────
    static class Monster {
        String name;
        int    hp, maxHp;
        int    attack, defense, speed;
        int    expReward, goldReward;
        String specialEffect;   // none / poison / stun / heal / crit / drain / bleed
        boolean alive = true;

        Monster(String name, int hp, int attack, int defense, int speed,
                int expReward, int goldReward, String specialEffect) {
            this.name          = name;
            this.hp = this.maxHp = hp;
            this.attack        = attack;
            this.defense       = defense;
            this.speed         = speed;
            this.expReward     = expReward;
            this.goldReward    = goldReward;
            this.specialEffect = specialEffect;
        }

        @Override public String toString() {
            return String.format("%s  HP:%d/%d  ATK:%d  DEF:%d  SPD:%d  [%s]",
                name, hp, maxHp, attack, defense, speed, specialEffect);
        }
    }

    // ─────────────────────────── BattleRecord ────────────────────────────
    // Tracks per-battle stats for rankings
    static class BattleRecord {
        String monsterName;
        int    turnsUsed;
        int    dmgDealt;
        int    dmgReceived;
        int    monsterMaxHp;
        boolean wasEscape;

        BattleRecord(String monsterName, int turnsUsed, int dmgDealt,
                     int dmgReceived, int monsterMaxHp, boolean wasEscape) {
            this.monsterName  = monsterName;
            this.turnsUsed    = turnsUsed;
            this.dmgDealt     = dmgDealt;
            this.dmgReceived  = dmgReceived;
            this.monsterMaxHp = monsterMaxHp;
            this.wasEscape    = wasEscape;
        }

        // Efficiency score: damage dealt per turn, penalised by damage received
        double efficiencyScore() {
            if (turnsUsed == 0 || wasEscape) return 0;
            return (double) dmgDealt / turnsUsed - (double) dmgReceived / turnsUsed * 0.5;
        }
    }

    // ─────────────────────────── Player ──────────────────────────────────
    static class Player {
        // Identity
        String name;
        String playerClass;   // Warrior / Mage / Rogue

        // Core stats
        int level = 1;
        int exp   = 0;
        int expToNext = 100;

        int baseHp,  maxHp,  hp;
        int baseMp,  maxMp,  mp;
        int baseAtk, basedef;
        double critChance;

        // Gold
        int gold;

        // Equipment slots
        Equipment weapon = null;
        Equipment armor  = null;

        // Inventory
        List<Item>      inventory  = new ArrayList<>();
        List<Equipment> equipBag   = new ArrayList<>();

        // Status effects
        boolean poisoned  = false;
        boolean stunned   = false;
        boolean bleeding  = false;   // NEW: bleed deals flat dmg each turn
        int     buffTurns  = 0;
        int     buffAmount = 0;

        // ── Analytics ──
        int statDmgDealt        = 0;
        int statDmgReceived     = 0;
        int statGoldEarned      = 0;
        int statExpGained       = 0;
        int statPotionsUsed     = 0;
        int statMonstersKilled  = 0;
        int statCritsLanded     = 0;
        int statFloorsCleared   = 0;
        int statBossesKilled    = 0;
        int statTotalHealing    = 0;
        int statGoldSpent       = 0;
        int statHighestDmg      = 0;
        int statTotalTurns      = 0;
        int statDeathsAvoided   = 0;

        String statStrongestMonster  = "None";
        String statMostDangerousMonster = "None";
        int    statMostDangerousDmg  = 0;
        String statStrongestEquip    = "None";
        String statMostUsedItem      = "None";
        int    statLowestHpSurvived  = 9999;

        List<String>         levelMilestones    = new ArrayList<>();
        Map<String,Integer>  itemUsageCount     = new HashMap<>();
        List<String>         lootObtained       = new ArrayList<>();   // per-floor, reset each floor
        List<String>         allLootObtained    = new ArrayList<>();   // cumulative
        List<String>         monstersEncountered= new ArrayList<>();
        List<BattleRecord>   battleHistory      = new ArrayList<>();

        // Highest value loot tracking
        String statHighestValueLoot     = "None";
        int    statHighestValueLootGold = 0;

        long startTimeMs = System.currentTimeMillis();

        Player(String name, String cls, int startGold) {
            this.name        = name;
            this.playerClass = cls;
            this.gold        = startGold;

            switch (cls) {
                case "Warrior":
                    baseHp=150; baseMp=40;  baseAtk=18; basedef=12; critChance=0.10; break;
                case "Mage":
                    baseHp=90;  baseMp=120; baseAtk=25; basedef=5;  critChance=0.12; break;
                default: // Rogue
                    baseHp=110; baseMp=70;  baseAtk=22; basedef=8;  critChance=0.20; break;
            }
            maxHp = baseHp; hp = maxHp;
            maxMp = baseMp; mp = maxMp;
        }

        // Total ATK including equipment & buffs
        int totalAtk() {
            int eq   = (weapon != null) ? weapon.effectiveAtk() : 0;
            int buff = (buffTurns > 0)  ? buffAmount : 0;
            return baseAtk + eq + buff;
        }
        int totalDef() {
            int eq = (armor != null) ? armor.effectiveDef() : 0;
            return basedef + eq;
        }
        int totalMaxHp() {
            int eq = (weapon != null ? weapon.effectiveHp() : 0)
                   + (armor  != null ? armor.effectiveHp()  : 0);
            return maxHp + eq;
        }

        void addExp(int amount) {
            exp += amount;
            statExpGained += amount;
            while (exp >= expToNext) {
                exp -= expToNext;
                levelUp();
            }
        }

        void levelUp() {
            level++;
            expToNext  = (int)(expToNext * 1.4);
            maxHp     += 15; hp = Math.min(hp + 15, totalMaxHp());
            maxMp     += 10; mp = Math.min(mp + 10, maxMp);
            baseAtk   += 3;
            basedef   += 2;
            critChance = Math.min(critChance + 0.01, 0.50);
            levelMilestones.add("Level " + level);
            System.out.println("\n★ LEVEL UP! You are now Level " + level + "! ★");
            System.out.printf("  HP +15 | MP +10 | ATK +3 | DEF +2 | CRIT +1%%\n");
        }

        // Skill costs & output
        int skillMpCost() {
            switch (playerClass) {
                case "Warrior": return 20;
                case "Mage":    return 35;
                default:        return 25;
            }
        }
        int skillDamage() {
            int base = totalAtk();
            switch (playerClass) {
                case "Warrior": return (int)(base * 2.0) + level * 5;
                case "Mage":    return (int)(base * 2.8) + level * 8;
                default:        return (int)(base * 2.3) + level * 6;
            }
        }
        String skillName() {
            switch (playerClass) {
                case "Warrior": return "Blade Storm";
                case "Mage":    return "Arcane Burst";
                default:        return "Shadow Strike";
            }
        }

        // Inventory helpers
        boolean addItem(Item newItem) {
            // Try to stack first
            for (Item it : inventory) {
                if (it.name.equals(newItem.name) && !it.type.equals("Equipment")) {
                    it.quantity += newItem.quantity;
                    // Track highest value loot
                    trackLootValue(it);
                    return true;
                }
            }
            if (inventory.size() >= INVENTORY_CAPACITY) return false;
            inventory.add(newItem);
            trackLootValue(newItem);
            return true;
        }

        void trackLootValue(Item it) {
            if (it.sellValue > statHighestValueLootGold) {
                statHighestValueLootGold = it.sellValue;
                statHighestValueLoot     = it.name + " (" + it.sellValue + "g)";
            }
        }

        Item findItem(String type) {
            for (Item it : inventory)
                if (it.type.equals(type) && it.quantity > 0) return it;
            return null;
        }

        boolean useItem(String type) {
            Item it = findItem(type);
            if (it == null || it.quantity <= 0) return false;
            it.quantity--;
            statPotionsUsed++;
            itemUsageCount.merge(it.name, 1, Integer::sum);
            if (it.quantity == 0) inventory.remove(it);
            return true;
        }

        String mostUsedItem() {
            return itemUsageCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse("None");
        }

        int inventoryValue() {
            int v = 0;
            for (Item it      : inventory) v += it.sellValue * it.quantity;
            for (Equipment eq : equipBag)  v += eq.sellValue;
            return v;
        }

        boolean isDead() { return hp <= 0; }

        void printStats() {
            System.out.println("┌─────────────────────────────────────────┐");
            System.out.printf( "│  %s  [%s]  Level %d\n", name, playerClass, level);
            System.out.printf( "│  HP: %d/%d   MP: %d/%d\n", hp, totalMaxHp(), mp, maxMp);
            System.out.printf( "│  ATK: %d  DEF: %d  CRIT: %.0f%%\n", totalAtk(), totalDef(), critChance*100);
            System.out.printf( "│  Gold: %dg\n", gold);
            System.out.printf( "│  EXP: %d/%d\n", exp, expToNext);
            String wpn = (weapon != null) ? weapon.name + (weapon.isBroken()?" [BROKEN]":"") : "Bare Hands";
            String arm = (armor  != null) ? armor.name  + (armor.isBroken()? " [BROKEN]":"") : "Cloth Rags";
            System.out.printf( "│  Weapon: %-20s  Armor: %s\n", wpn, arm);
            if (poisoned)  System.out.println("│  [STATUS: POISONED]");
            if (stunned)   System.out.println("│  [STATUS: STUNNED]");
            if (bleeding)  System.out.println("│  [STATUS: BLEEDING]");
            if (buffTurns > 0) System.out.printf("│  [BUFF: +%d ATK for %d turns]\n", buffAmount, buffTurns);
            System.out.println("└─────────────────────────────────────────┘");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    //  GAME ENGINE
    // ══════════════════════════════════════════════════════════════════════

    static Scanner sc = new Scanner(System.in);

    // ─── Input helpers ────────────────────────────────────────────────────
    static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }
    static int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                int v = Integer.parseInt(readLine(prompt));
                if (v >= min && v <= max) return v;
                System.out.printf("  Enter a number between %d and %d.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Enter a number.");
            }
        }
    }
    static void pause() { readLine("  [Press ENTER to continue]"); }
    static void printLine() { System.out.println("─".repeat(50)); }
    static void printHeader(String s) { printLine(); System.out.println("  " + s); printLine(); }

    // ══════════════════════════════════════════════════════════════════════
    //  CHARACTER CREATION
    // ══════════════════════════════════════════════════════════════════════
    static Player createCharacter() {
        printHeader("⚔  DUNGEON RAID  ⚔  Character Creation");
        String name = "";
        while (name.isEmpty()) name = readLine("  Enter your character name: ");

        System.out.println("\n  Choose your class:");
        System.out.println("  1) Warrior  — High HP & DEF, powerful melee");
        System.out.println("  2) Mage     — High MP & skill damage, low DEF");
        System.out.println("  3) Rogue    — Balanced, highest crit chance");
        int choice = readInt("  > ", 1, 3);
        String[] classes = {"Warrior","Mage","Rogue"};
        String cls = classes[choice-1];

        int gold = 0;
        while (gold < 50 || gold > 500) {
            try {
                gold = Integer.parseInt(readLine("  Starting gold (50-500): "));
                if (gold < 50 || gold > 500) {
                    System.out.println("  Please enter a value between 50 and 500.");
                    gold = 0;
                }
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input.");
                gold = 0;
            }
        }

        Player p = new Player(name, cls, gold);
        System.out.printf("\n  Welcome, %s the %s!\n", name, cls);
        p.printStats();
        pause();
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  SHOP SYSTEM
    // ══════════════════════════════════════════════════════════════════════

    static List<Equipment> weaponShopStock() {
        List<Equipment> s = new ArrayList<>();
        s.add(new Equipment("Iron Sword",      "Weapon", 60,  50, 8,  0,  0,  0, 1, "Common"));
        s.add(new Equipment("Steel Sword",     "Weapon", 120, 60, 14, 0,  0,  0, 2, "Uncommon"));
        s.add(new Equipment("Enchanted Blade", "Weapon", 250, 70, 22, 0,  10, 0, 3, "Rare"));
        s.add(new Equipment("Shadow Dagger",   "Weapon", 180, 55, 18, 0,  0,  5, 2, "Uncommon"));
        s.add(new Equipment("Arcane Staff",    "Weapon", 200, 60, 20, 0,  0, 15, 3, "Rare"));
        s.add(new Equipment("Dragon Fang",     "Weapon", 400, 80, 35, 0,  15, 10,4, "Epic"));
        return s;
    }
    static List<Equipment> armorShopStock() {
        List<Equipment> s = new ArrayList<>();
        s.add(new Equipment("Leather Armor",  "Armor",  50,  50, 0,  6,  10, 0, 1, "Common"));
        s.add(new Equipment("Chain Mail",     "Armor", 100,  60, 0, 10,  20, 0, 2, "Uncommon"));
        s.add(new Equipment("Steel Plate",    "Armor", 200,  70, 0, 18,  30, 0, 3, "Rare"));
        s.add(new Equipment("Mystic Robes",   "Armor", 150,  55, 0,  8,  15, 20,2, "Uncommon"));
        s.add(new Equipment("Dragon Scale",   "Armor", 380,  80, 0, 28,  50, 10,4, "Epic"));
        return s;
    }

    static void visitShops(Player p) {
        boolean shopping = true;
        while (shopping) {
            printHeader("TOWN — SHOPS  (Gold: " + p.gold + "g)");
            System.out.println("  1) Weapon Shop");
            System.out.println("  2) Armor Shop");
            System.out.println("  3) Potion Shop");
            System.out.println("  4) Blacksmith");
            System.out.println("  5) Sell Items");
            System.out.println("  6) Leave Town → Enter Dungeon");
            int c = readInt("  > ", 1, 6);
            switch (c) {
                case 1: weaponShop(p);  break;
                case 2: armorShop(p);   break;
                case 3: potionShop(p);  break;
                case 4: blacksmith(p);  break;
                case 5: sellItems(p);   break;
                case 6: shopping = false; break;
            }
        }
    }

    static void weaponShop(Player p) {
        printHeader("WEAPON SHOP  (Gold: " + p.gold + "g)");
        List<Equipment> stock = weaponShopStock();
        for (int i = 0; i < stock.size(); i++) {
            Equipment eq = stock.get(i);
            String lock = (p.gold >= eq.cost && p.level >= eq.levelReq) ? "" : " [LOCKED]";
            System.out.printf("  %d) %s  Cost:%dg%s\n", i+1, eq, eq.cost, lock);
        }
        System.out.println("  0) Back");
        int c = readInt("  Buy > ", 0, stock.size());
        if (c == 0) return;
        Equipment chosen = stock.get(c-1);
        if (p.gold < chosen.cost)       { System.out.println("  Not enough gold!"); pause(); return; }
        if (p.level < chosen.levelReq)  { System.out.println("  Level too low!");   pause(); return; }
        p.gold -= chosen.cost; p.statGoldSpent += chosen.cost;
        p.equipBag.add(chosen);
        System.out.println("  Purchased " + chosen.name + "!");
        if (p.weapon == null || chosen.atkBonus > p.weapon.atkBonus) {
            p.weapon = chosen;
            System.out.println("  Auto-equipped " + chosen.name + "!");
        }
        pause();
    }

    static void armorShop(Player p) {
        printHeader("ARMOR SHOP  (Gold: " + p.gold + "g)");
        List<Equipment> stock = armorShopStock();
        for (int i = 0; i < stock.size(); i++) {
            Equipment eq = stock.get(i);
            String lock = (p.gold >= eq.cost && p.level >= eq.levelReq) ? "" : " [LOCKED]";
            System.out.printf("  %d) %s  Cost:%dg%s\n", i+1, eq, eq.cost, lock);
        }
        System.out.println("  0) Back");
        int c = readInt("  Buy > ", 0, stock.size());
        if (c == 0) return;
        Equipment chosen = stock.get(c-1);
        if (p.gold < chosen.cost)       { System.out.println("  Not enough gold!"); pause(); return; }
        if (p.level < chosen.levelReq)  { System.out.println("  Level too low!");   pause(); return; }
        p.gold -= chosen.cost; p.statGoldSpent += chosen.cost;
        p.equipBag.add(chosen);
        System.out.println("  Purchased " + chosen.name + "!");
        if (p.armor == null || chosen.defBonus > p.armor.defBonus) {
            p.armor = chosen;
            System.out.println("  Auto-equipped " + chosen.name + "!");
        }
        pause();
    }

    static void potionShop(Player p) {
        printHeader("POTION SHOP  (Gold: " + p.gold + "g)");
        System.out.println("  1) Health Potion    — Restore 60 HP     — Cost: 30g each");
        System.out.println("  2) Mana Potion      — Restore 40 MP     — Cost: 25g each");
        System.out.println("  3) Buff Potion      — ATK+10 for 3 turns — Cost: 50g each");
        System.out.println("  4) Mega HP Potion   — Restore 150 HP    — Cost: 80g each");
        System.out.println("  0) Back");
        int c = readInt("  Buy > ", 0, 4);
        if (c == 0) return;
        int[]    costs = {0, 30, 25, 50, 80};
        String[] names = {"","Health Potion","Mana Potion","Buff Potion","Mega HP Potion"};
        String[] types = {"","HealthPotion","ManaPotion","BuffPotion","HealthPotion"};
        int qty = readInt("  Quantity (1-10): ", 1, 10);
        int total = costs[c] * qty;
        if (p.gold < total) {
            System.out.printf("  Not enough gold! Need %dg, have %dg.\n", total, p.gold);
            pause(); return;
        }
        p.gold -= total; p.statGoldSpent += total;
        Item pot = new Item(names[c], qty, "Common", costs[c]/2, types[c]);
        if (c == 4) pot.sellValue = 40;
        if (!p.addItem(pot)) {
            System.out.println("  Inventory full! Refunding gold.");
            p.gold += total; p.statGoldSpent -= total;
        } else {
            System.out.printf("  Bought %d x %s for %dg!\n", qty, names[c], total);
        }
        pause();
    }

    static void blacksmith(Player p) {
        printHeader("BLACKSMITH  (Gold: " + p.gold + "g)");
        System.out.println("  1) Repair Weapon");
        System.out.println("  2) Repair Armor");
        System.out.println("  3) Upgrade Weapon  (Cost: 80g + 2 Iron Ore)");
        System.out.println("  4) Upgrade Armor   (Cost: 80g + 2 Iron Ore)");
        System.out.println("  5) Craft Weapon    (Cost: 150g + 5 Iron Ore + 2 Magic Crystal)");
        System.out.println("  0) Back");
        int c = readInt("  > ", 0, 5);
        if (c == 0) return;
        if (c == 1) repairEquipment(p, "Weapon");
        else if (c == 2) repairEquipment(p, "Armor");
        else if (c == 3) upgradeEquipment(p, "Weapon");
        else if (c == 4) upgradeEquipment(p, "Armor");
        else craftWeapon(p);
        pause();
    }

    static void repairEquipment(Player p, String slot) {
        Equipment eq = slot.equals("Weapon") ? p.weapon : p.armor;
        if (eq == null) { System.out.println("  No " + slot + " equipped!"); return; }
        int dmg = eq.maxDurability - eq.durability;
        if (dmg == 0) { System.out.println("  " + eq.name + " is already at full durability!"); return; }
        int cost = dmg * 2;
        System.out.printf("  Repair %s: restore %d durability for %dg\n", eq.name, dmg, cost);
        if (p.gold < cost) { System.out.printf("  Not enough gold! Need %dg, have %dg.\n", cost, p.gold); return; }
        p.gold -= cost; p.statGoldSpent += cost;
        eq.durability = eq.maxDurability;
        System.out.println("  " + eq.name + " fully repaired!");
    }

    static void upgradeEquipment(Player p, String slot) {
        Equipment eq = slot.equals("Weapon") ? p.weapon : p.armor;
        if (eq == null) { System.out.println("  No " + slot + " equipped!"); return; }
        Item ore = null;
        for (Item it : p.inventory) if (it.name.equals("Iron Ore") && it.quantity >= 2) { ore = it; break; }
        if (p.gold < 80 || ore == null) {
            System.out.printf("  Need 80g and 2 Iron Ore. Have: %dg\n", p.gold);
            return;
        }
        p.gold -= 80; p.statGoldSpent += 80;
        ore.quantity -= 2;
        if (ore.quantity == 0) p.inventory.remove(ore);
        eq.atkBonus += 4; eq.defBonus += 3; eq.maxDurability += 10; eq.durability += 10;
        eq.name += "+";
        System.out.println("  " + eq.name + " upgraded! ATK+4 DEF+3 DUR+10");
    }

    static void craftWeapon(Player p) {
        Item ore = null, crystal = null;
        for (Item it : p.inventory) {
            if (it.name.equals("Iron Ore")      && it.quantity >= 5) ore     = it;
            if (it.name.equals("Magic Crystal") && it.quantity >= 2) crystal = it;
        }
        if (p.gold < 150 || ore == null || crystal == null) {
            System.out.println("  Need 150g, 5 Iron Ore, 2 Magic Crystal.");
            return;
        }
        p.gold -= 150; p.statGoldSpent += 150;
        ore.quantity     -= 5; if (ore.quantity     == 0) p.inventory.remove(ore);
        crystal.quantity -= 2; if (crystal.quantity == 0) p.inventory.remove(crystal);
        Equipment crafted = new Equipment("Forged Rune Blade","Weapon",300,80,30,5,20,10,p.level,"Rare");
        p.equipBag.add(crafted);
        p.weapon = crafted;
        System.out.println("  Crafted Forged Rune Blade and equipped it!");
    }

    static void sellItems(Player p) {
        printHeader("SELL ITEMS");
        if (p.inventory.isEmpty() && p.equipBag.isEmpty()) { System.out.println("  Nothing to sell."); pause(); return; }
        int idx = 1;
        List<Object> sellable = new ArrayList<>();
        for (Item it      : p.inventory) { System.out.printf("  %d) %s\n", idx++, it); sellable.add(it); }
        for (Equipment eq : p.equipBag)  {
            System.out.printf("  %d) %-22s Sell:%dg [%s]\n", idx++, eq.name, eq.sellValue, eq.rarity);
            sellable.add(eq);
        }
        System.out.println("  0) Back");
        int c = readInt("  Sell item # > ", 0, sellable.size());
        if (c == 0) return;
        Object obj = sellable.get(c-1);
        if (obj instanceof Item) {
            Item it = (Item) obj;
            int earn = it.sellValue * it.quantity;
            p.gold += earn;
            System.out.printf("  Sold %s x%d for %dg!\n", it.name, it.quantity, earn);
            p.inventory.remove(it);
        } else {
            Equipment eq = (Equipment) obj;
            if (eq == p.weapon) p.weapon = null;
            if (eq == p.armor)  p.armor  = null;
            p.gold += eq.sellValue;
            System.out.printf("  Sold %s for %dg!\n", eq.name, eq.sellValue);
            p.equipBag.remove(eq);
        }
        pause();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  MONSTER GENERATION
    // ══════════════════════════════════════════════════════════════════════
    static List<Monster> generateFloorMonsters(int floor) {
        List<Monster> list = new ArrayList<>();
        int s = floor; // scaling
        switch (floor) {
            case 1:
                list.add(new Monster("Goblin",        30+s*5,  6+s,  2, 8,  20, 10, "none"));
                list.add(new Monster("Slime",         25+s*5,  4+s,  1, 5,  15,  8, "poison"));
                list.add(new Monster("Bat",           20+s*3,  5+s,  1,12,  12,  6, "none"));
                list.add(new Monster("Cave Rat",      18+s*3,  7+s,  2, 9,  18,  9, "bleed"));
                list.add(new Monster("Giant Spider",  35+s*5,  9+s,  3, 7,  25, 12, "poison"));
                break;
            case 2:
                list.add(new Monster("Orc Warrior",  60+s*8,  12+s, 5,  7, 40, 20, "none"));
                list.add(new Monster("Skeleton",     50+s*6,  10+s, 4,  8, 35, 18, "none"));
                list.add(new Monster("Werewolf",     70+s*8,  14+s, 6,  9, 50, 25, "crit"));
                list.add(new Monster("Dark Mage",    45+s*5,  16+s, 2, 10, 45, 22, "stun"));
                list.add(new Monster("Zombie Horde", 80+s*8,   8+s, 8,  4, 55, 28, "poison"));
                break;
            case 3:
                list.add(new Monster("Troll",       100+s*10, 18+s,10,  5, 70, 35, "heal"));
                list.add(new Monster("Harpy",        80+s*8,  20+s, 7, 13, 65, 32, "stun"));
                list.add(new Monster("Death Knight",110+s*10, 22+s,12,  8, 80, 40, "crit"));
                list.add(new Monster("Witch",        75+s*8,  24+s, 5, 11, 75, 38, "poison"));
                list.add(new Monster("Minotaur",    120+s*10, 20+s,14,  6, 85, 42, "bleed"));
                break;
            case 4:
                list.add(new Monster("Vampire Lord", 140+s*12, 26+s,14,  9,100, 50, "drain"));
                list.add(new Monster("Stone Golem",  160+s*12, 20+s,20,  4,110, 55, "stun"));
                list.add(new Monster("Shadow Demon", 130+s*10, 30+s,10, 12,105, 52, "crit"));
                list.add(new Monster("Basilisk",     150+s*12, 22+s,16,  7,115, 58, "poison"));
                list.add(new Monster("Lich",         120+s*10, 32+s, 8, 10,120, 60, "stun"));
                break;
            case 5: // Boss floor guards
                list.add(new Monster("Elite Guard",  180+s*12, 28+s,18,  8,130, 65, "none"));
                list.add(new Monster("Elite Guard",  180+s*12, 28+s,18,  8,130, 65, "none"));
                break;
        }
        return list;
    }

    static Monster generateBoss() {
        return new Monster("DRAGON OVERLORD", 600, 45, 20, 10, 500, 300, "crit");
    }

    // ══════════════════════════════════════════════════════════════════════
    //  COMBAT SYSTEM
    // ══════════════════════════════════════════════════════════════════════

    /**
     * Determines whether the player acts first this turn based on speed comparison.
     * Player speed is derived from class + level. Ties go to player.
     */
    static int playerSpeed(Player p) {
        int base;
        switch (p.playerClass) {
            case "Warrior": base = 8;  break;
            case "Mage":    base = 10; break;
            default:        base = 14; break; // Rogue fastest
        }
        return base + p.level;
    }

    static boolean doBattle(Player p, Monster m, boolean isBoss) {
        printHeader((isBoss ? "★ BOSS BATTLE ★  " : "BATTLE — ") + m.name);
        System.out.println("  " + m);
        System.out.println();

        p.monstersEncountered.add(m.name);

        int turnCount      = 0;
        int bossPhase      = 1;
        int bossEnrageTurn = 8; // Boss enrages after this many turns (separate from phase)
        boolean bossEnraged = false;

        // Per-battle analytics
        int battleDmgDealt    = 0;
        int battleDmgReceived = 0;
        boolean escaped       = false;

        while (p.hp > 0 && m.hp > 0) {
            turnCount++;
            p.statTotalTurns++;

            System.out.printf("\n--- Turn %d ---  [Player HP: %d/%d  MP: %d/%d  SPD:%d]  " +
                              "[%s HP: %d/%d  SPD:%d]\n",
                turnCount, p.hp, p.totalMaxHp(), p.mp, p.maxMp, playerSpeed(p),
                m.name, m.hp, m.maxHp, m.speed);

            // ── Boss phase transitions ──
            if (isBoss) {
                int newPhase = m.hp <= m.maxHp * 0.3 ? 3 : m.hp <= m.maxHp * 0.6 ? 2 : 1;
                if (newPhase > bossPhase) {
                    bossPhase = newPhase;
                    m.attack  += 10; m.defense += 5;
                    System.out.println("  !! BOSS ENTERS PHASE " + bossPhase + " — Stats Increased !!");
                    if (bossPhase == 3)
                        System.out.println("  !! PHASE 3: Boss gains a DRAIN ability !!");
                }
                // Turn-count enrage (separate from HP-phase enrage)
                if (!bossEnraged && turnCount >= bossEnrageTurn) {
                    bossEnraged = true;
                    m.attack += 15;
                    System.out.println("  !! BOSS ENRAGED after " + bossEnrageTurn +
                                       " turns — DOUBLE ATTACK UNLOCKED, ATK+" + 15 + " !!");
                }
            }

            // ── Determine turn order by speed ──
            boolean playerFirst = (playerSpeed(p) >= m.speed);

            if (playerFirst) {
                // Player acts first
                if (!p.stunned) {
                    boolean[] result = new boolean[1];
                    int[] dmgOut = new int[1];
                    boolean acted = playerTurn(p, m, isBoss, dmgOut, result);
                    battleDmgDealt += dmgOut[0];
                    if (result[0]) { escaped = true; break; } // escaped
                    if (m.hp <= 0) break;
                } else {
                    System.out.println("  You are STUNNED and lose your turn!");
                    p.stunned = false;
                }

                // Apply player status effects after action
                int statusDmg = applyPlayerStatusEffects(p);
                battleDmgReceived += statusDmg;
                if (p.hp <= 0) break;
                if (p.buffTurns > 0) p.buffTurns--;

                // Monster acts
                if (m.hp > 0) {
                    int mDmg = monsterTurn(p, m, isBoss && bossEnraged, isBoss && bossPhase == 3);
                    battleDmgReceived += mDmg;
                    // Track most dangerous monster
                    if (mDmg > p.statMostDangerousDmg) {
                        p.statMostDangerousDmg    = mDmg;
                        p.statMostDangerousMonster = m.name;
                    }
                }
            } else {
                // Monster acts first (faster)
                System.out.println("  [" + m.name + " is faster — attacks first!]");
                int mDmg = monsterTurn(p, m, isBoss && bossEnraged, isBoss && bossPhase == 3);
                battleDmgReceived += mDmg;
                if (mDmg > p.statMostDangerousDmg) {
                    p.statMostDangerousDmg    = mDmg;
                    p.statMostDangerousMonster = m.name;
                }
                if (p.hp <= 0) break;

                // Player acts
                if (!p.stunned) {
                    int[] dmgOut = new int[1];
                    boolean[] result = new boolean[1];
                    playerTurn(p, m, isBoss, dmgOut, result);
                    battleDmgDealt += dmgOut[0];
                    if (result[0]) { escaped = true; break; }
                    if (m.hp <= 0) break;
                } else {
                    System.out.println("  You are STUNNED and lose your turn!");
                    p.stunned = false;
                }

                // Apply player status effects
                int statusDmg = applyPlayerStatusEffects(p);
                battleDmgReceived += statusDmg;
                if (p.hp <= 0) break;
                if (p.buffTurns > 0) p.buffTurns--;
            }

            // Track low HP survival
            if (p.hp > 0 && p.hp <= 15) {
                p.statDeathsAvoided++;
                if (p.hp < p.statLowestHpSurvived) p.statLowestHpSurvived = p.hp;
            }
        }

        // Record battle
        p.battleHistory.add(new BattleRecord(m.name, turnCount,
            battleDmgDealt, battleDmgReceived, m.maxHp, escaped));

        if (escaped) {
            System.out.println("  You successfully escaped!");
            m.alive = false;
            return true; // not a death
        }

        if (p.hp <= 0) {
            System.out.println("\n  ✖ You have been defeated by " + m.name + "...");
            return false;
        }

        // Victory
        System.out.printf("\n  ✔ %s defeated! (took %d turns)\n", m.name, turnCount);
        p.statMonstersKilled++;
        if (isBoss) p.statBossesKilled++;

        int goldGain = m.goldReward + RNG.nextInt(m.goldReward / 2 + 1);
        p.gold += goldGain;
        p.statGoldEarned += goldGain;
        System.out.printf("  EXP +%d  Gold +%dg\n", m.expReward, goldGain);
        p.addExp(m.expReward);

        // Durability wear
        if (p.weapon != null) p.weapon.durability = Math.max(0, p.weapon.durability - RNG.nextInt(4) - 1);
        if (p.armor  != null) p.armor.durability  = Math.max(0, p.armor.durability  - RNG.nextInt(3) - 1);
        if (p.weapon != null && p.weapon.isBroken()) System.out.println("  ⚠ Your weapon broke!");
        if (p.armor  != null && p.armor.isBroken())  System.out.println("  ⚠ Your armor broke!");

        // Strongest monster tracking
        if (m.maxHp > 100 || isBoss) p.statStrongestMonster = m.name;

        // Loot
        List<Item> droppedLoot = dropLoot(p, m, isBoss);
        for (Item it : droppedLoot) p.lootObtained.add(it.name + " x" + it.quantity);

        pause();
        return true;
    }

    // ── Player status effects (poison / bleed) ─────────────────────────
    static int applyPlayerStatusEffects(Player p) {
        int totalDmg = 0;
        if (p.poisoned) {
            int dmg = 5 + p.level;
            p.hp -= dmg; p.statDmgReceived += dmg; totalDmg += dmg;
            System.out.printf("  ☠ POISON deals %d damage!  HP:%d/%d\n", dmg, p.hp, p.totalMaxHp());
            if (RNG.nextInt(100) < 25) { p.poisoned = false; System.out.println("  Poison wore off."); }
        }
        if (p.bleeding) {
            int dmg = 3 + p.level / 2;
            p.hp -= dmg; p.statDmgReceived += dmg; totalDmg += dmg;
            System.out.printf("  🩸 BLEED deals %d damage!  HP:%d/%d\n", dmg, p.hp, p.totalMaxHp());
            if (RNG.nextInt(100) < 30) { p.bleeding = false; System.out.println("  Bleeding stopped."); }
        }
        return totalDmg;
    }

    // ── Player turn (returns damage dealt via array; result[0]=true means escaped) ─
    static boolean playerTurn(Player p, Monster m, boolean isBoss, int[] dmgOut, boolean[] escaped) {
        System.out.println("  Actions:  1) Attack  2) Skill(" + p.skillName() +
                           ")  3) Defend  4) Potion  5) Escape");
        int action = readInt("  > ", 1, 5);
        dmgOut[0]  = 0;
        escaped[0] = false;
        switch (action) {
            case 1: dmgOut[0] = basicAttack(p, m);  break;
            case 2: dmgOut[0] = skillAttack(p, m);  break;
            case 3: defend(p);                       break;
            case 4: usePotion(p);                    break;
            case 5:
                if (!isBoss && RNG.nextInt(100) < 40) {
                    escaped[0] = true;
                } else {
                    System.out.println(isBoss ? "  Cannot escape a boss battle!" : "  Escape failed!");
                }
                break;
        }
        return true;
    }

    static int basicAttack(Player p, Monster m) {
        boolean crit = RNG.nextDouble() < p.critChance;
        int raw  = p.totalAtk() + RNG.nextInt(5);
        int dmg  = Math.max(1, raw - m.defense + (crit ? raw / 2 : 0));
        m.hp    -= dmg;
        p.statDmgDealt += dmg;
        if (dmg > p.statHighestDmg) p.statHighestDmg = dmg;
        if (crit) {
            p.statCritsLanded++;
            System.out.printf("  ★ CRITICAL HIT! %d damage to %s!\n", dmg, m.name);
        } else {
            System.out.printf("  You attack %s for %d damage!\n", m.name, dmg);
        }
        return dmg;
    }

    static int skillAttack(Player p, Monster m) {
        int cost = p.skillMpCost();
        if (p.mp < cost) {
            System.out.println("  Not enough MP! (" + cost + " required) — using basic attack instead.");
            return basicAttack(p, m);
        }
        p.mp -= cost;
        boolean crit = RNG.nextDouble() < p.critChance + 0.05;
        int raw  = p.skillDamage() + RNG.nextInt(10);
        int dmg  = Math.max(1, raw - m.defense + (crit ? raw / 2 : 0));
        m.hp    -= dmg;
        p.statDmgDealt += dmg;
        if (dmg > p.statHighestDmg) p.statHighestDmg = dmg;
        System.out.printf("  ✦ %s deals %d damage to %s!%s\n",
            p.skillName(), dmg, m.name, crit ? " ★CRIT!" : "");
        if (crit) p.statCritsLanded++;
        return dmg;
    }

    static void defend(Player p) {
        int shield = p.totalDef() / 2 + 5;
        p.hp = Math.min(p.totalMaxHp(), p.hp + shield);
        p.statTotalHealing += shield;
        // Cleanse one status on defend
        if (p.poisoned)  { p.poisoned  = false; System.out.println("  You shrug off the poison!"); }
        if (p.bleeding)  { p.bleeding  = false; System.out.println("  You stop the bleeding!"); }
        System.out.printf("  You brace yourself — recovered %d HP. HP:%d/%d\n",
            shield, p.hp, p.totalMaxHp());
    }

    static void usePotion(Player p) {
        System.out.println("  Potions:  1) Health  2) Mana  3) Buff  0) Cancel");
        int c = readInt("  > ", 0, 3);
        if (c == 0) return;
        String[] types = {"","HealthPotion","ManaPotion","BuffPotion"};
        String t = types[c];
        Item it = p.findItem(t);
        if (it == null || it.quantity <= 0) {
            System.out.println("  No " + t.replace("Potion","") + " Potion available!");
            return;
        }
        // Capture name before consuming (fixes potential remove-then-read bug)
        String itemName  = it.name;
        boolean isMega   = itemName.contains("Mega");
        p.useItem(t);

        switch (t) {
            case "HealthPotion":
                int heal   = isMega ? 150 : 60;
                int actual = Math.min(heal, p.totalMaxHp() - p.hp);
                p.hp += actual; p.statTotalHealing += actual;
                System.out.printf("  Used %s — restored %d HP. HP:%d/%d\n",
                    itemName, actual, p.hp, p.totalMaxHp());
                break;
            case "ManaPotion":
                int mana = Math.min(40, p.maxMp - p.mp);
                p.mp += mana;
                System.out.printf("  Used %s — restored %d MP. MP:%d/%d\n",
                    itemName, mana, p.mp, p.maxMp);
                break;
            case "BuffPotion":
                p.buffTurns = 3; p.buffAmount = 10;
                System.out.println("  Used Buff Potion — ATK+10 for 3 turns!");
                break;
        }
    }

    /**
     * Monster's turn. Returns total damage dealt to player this turn.
     * drainMode: boss phase 3 drains player HP.
     * doubleAtk:  boss enrage double-attack.
     */
    static int monsterTurn(Player p, Monster m, boolean doubleAtk, boolean drainMode) {
        int totalDmg = 0;
        int baseAtk  = m.attack + (doubleAtk ? m.attack / 2 : 0);
        boolean mCrit = m.specialEffect.equals("crit") && RNG.nextInt(100) < 20;

        int raw = baseAtk + RNG.nextInt(5);
        int dmg = Math.max(1, raw - p.totalDef() + (mCrit ? raw / 4 : 0));

        // Special effects
        switch (m.specialEffect) {
            case "poison":
                if (RNG.nextInt(100) < 30) {
                    p.poisoned = true;
                    System.out.println("  " + m.name + " POISONS you!");
                }
                break;
            case "stun":
                if (RNG.nextInt(100) < 25) {
                    p.stunned = true;
                    System.out.println("  " + m.name + " STUNS you! (next turn lost)");
                }
                break;
            case "heal":
                if (RNG.nextInt(100) < 20) {
                    int healAmt = m.maxHp / 10;
                    m.hp = Math.min(m.maxHp, m.hp + healAmt);
                    System.out.printf("  %s heals %d HP! HP:%d/%d\n",
                        m.name, healAmt, m.hp, m.maxHp);
                }
                break;
            case "bleed":
                if (RNG.nextInt(100) < 30) {
                    p.bleeding = true;
                    System.out.println("  " + m.name + " makes you BLEED!");
                }
                break;
            case "drain":
                if (RNG.nextInt(100) < 35) {
                    int drainAmt = Math.min(15, p.hp - 1);
                    if (drainAmt > 0) {
                        p.hp      -= drainAmt;
                        m.hp       = Math.min(m.maxHp, m.hp + drainAmt);
                        p.statDmgReceived += drainAmt;
                        totalDmg  += drainAmt;
                        System.out.printf("  %s DRAINS %d HP from you! HP:%d/%d\n",
                            m.name, drainAmt, p.hp, p.totalMaxHp());
                    }
                }
                break;
        }

        // Boss phase 3 special: drain regardless of effect flag
        if (drainMode && RNG.nextInt(100) < 25) {
            int drainAmt = Math.min(20, p.hp - 1);
            if (drainAmt > 0) {
                p.hp  -= drainAmt;
                m.hp   = Math.min(m.maxHp, m.hp + drainAmt);
                p.statDmgReceived += drainAmt;
                totalDmg += drainAmt;
                System.out.printf("  !! Dragon DRAINS %d HP!! HP:%d/%d\n",
                    drainAmt, p.hp, p.totalMaxHp());
            }
        }

        p.hp -= dmg;
        p.statDmgReceived += dmg;
        totalDmg += dmg;
        System.out.printf("  %s attacks you for %d damage!%s  HP:%d/%d\n",
            m.name, dmg, mCrit ? " [CRITICAL]" : "", p.hp, p.totalMaxHp());

        // Double-attack (enraged boss)
        if (doubleAtk) {
            int dmg2 = Math.max(1, raw / 2 - p.totalDef() / 2);
            p.hp -= dmg2;
            p.statDmgReceived += dmg2;
            totalDmg += dmg2;
            System.out.printf("  %s attacks AGAIN for %d damage! HP:%d/%d\n",
                m.name, dmg2, p.hp, p.totalMaxHp());
        }
        return totalDmg;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  LOOT SYSTEM
    // ══════════════════════════════════════════════════════════════════════
    static List<Item> dropLoot(Player p, Monster m, boolean isBoss) {
        List<Item> drops = new ArrayList<>();
        if (isBoss) {
            drops.add(new Item("Dragon Scale Fragment", 2, "Epic",    80, "Material"));
            drops.add(new Item("Boss Core",             1, "Epic",   150, "Drop"));
            drops.add(new Item("Magic Crystal",         3, "Rare",    40, "Material"));
        } else {
            if (RNG.nextInt(100) < 50) drops.add(new Item("Iron Ore",       RNG.nextInt(2)+1,"Common",  5,"Material"));
            if (RNG.nextInt(100) < 30) drops.add(new Item("Monster Fang",   1,               "Common", 10,"Drop"));
            if (RNG.nextInt(100) < 20) drops.add(new Item("Magic Crystal",  1,               "Rare",   40,"Material"));
            if (RNG.nextInt(100) < 15) drops.add(new Item("Health Potion",  1,               "Common", 15,"HealthPotion"));
            if (RNG.nextInt(100) < 10) drops.add(new Item("Rare Gemstone",  1,               "Rare",   75,"Drop"));
        }
        List<Item> added = new ArrayList<>();
        if (!drops.isEmpty()) {
            System.out.println("  Loot dropped:");
            for (Item dr : drops) {
                boolean ok = p.addItem(dr);
                if (ok) {
                    System.out.println("    + " + dr);
                    p.allLootObtained.add(dr.name);
                    added.add(dr);
                } else {
                    System.out.println("    [INVENTORY FULL] " + dr.name + " lost!");
                }
            }
        }
        return added;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  RANDOM FLOOR EVENTS
    // ══════════════════════════════════════════════════════════════════════
    static void randomEvent(Player p) {
        int roll = RNG.nextInt(7);
        printHeader("⚡ RANDOM EVENT");
        switch (roll) {
            case 0:
                int stolen = Math.min(p.gold, 15 + RNG.nextInt(20));
                p.gold -= stolen;
                System.out.printf("  A thief steals %dg from you!\n", stolen);
                break;
            case 1:
                int bonus = 20 + RNG.nextInt(30);
                p.gold += bonus; p.statGoldEarned += bonus;
                System.out.printf("  You find a hidden chest! +%dg\n", bonus);
                break;
            case 2:
                int trap = 10 + RNG.nextInt(15);
                p.hp = Math.max(1, p.hp - trap); p.statDmgReceived += trap;
                System.out.printf("  A trap springs! You take %d damage! HP:%d/%d\n",
                    trap, p.hp, p.totalMaxHp());
                break;
            case 3:
                int hpRestore = 20 + RNG.nextInt(20);
                int actual    = Math.min(hpRestore, p.totalMaxHp() - p.hp);
                p.hp += actual; p.statTotalHealing += actual;
                System.out.printf("  A healing spring restores %d HP! HP:%d/%d\n",
                    actual, p.hp, p.totalMaxHp());
                break;
            case 4:
                p.buffTurns = 5; p.buffAmount = 8;
                System.out.println("  A mysterious altar blesses you! ATK+8 for 5 turns!");
                break;
            case 5:
                Item found = new Item("Magic Crystal", 1, "Rare", 40, "Material");
                System.out.println(p.addItem(found)
                    ? "  You discover a Magic Crystal!"
                    : "  You find a Crystal but inventory is full!");
                break;
            case 6:
                int mpRestore = 15 + RNG.nextInt(15);
                p.mp = Math.min(p.maxMp, p.mp + mpRestore);
                System.out.printf("  A mana fountain restores %d MP! MP:%d/%d\n",
                    mpRestore, p.mp, p.maxMp);
                break;
        }
        pause();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  FLOOR SUMMARY
    // ══════════════════════════════════════════════════════════════════════
    static void floorSummary(Player p, int floor, int monstersKilled,
                              int goldEarned, int expEarned, int potionsUsed) {
        printHeader("FLOOR " + floor + " SUMMARY");
        System.out.printf("  Monsters Defeated  : %d\n",   monstersKilled);
        System.out.printf("  Gold Earned        : %dg\n",  goldEarned);
        System.out.printf("  EXP Earned         : %d\n",   expEarned);
        System.out.printf("  Potions Used       : %d\n",   potionsUsed);
        System.out.printf("  Remaining HP/MP    : %d/%d  /  %d/%d\n",
            p.hp, p.totalMaxHp(), p.mp, p.maxMp);
        String wpnDur = (p.weapon != null)
            ? p.weapon.durability + "/" + p.weapon.maxDurability : "N/A";
        String armDur = (p.armor  != null)
            ? p.armor.durability  + "/" + p.armor.maxDurability  : "N/A";
        System.out.printf("  Weapon Durability  : %s\n",   wpnDur);
        System.out.printf("  Armor Durability   : %s\n",   armDur);
        // Loot collected this floor
        if (!p.lootObtained.isEmpty()) {
            System.out.println("  Loot Collected     :");
            for (String loot : p.lootObtained) System.out.println("      • " + loot);
        } else {
            System.out.println("  Loot Collected     : None");
        }
        p.lootObtained.clear(); // reset for next floor
        pause();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  INVENTORY DISPLAY
    // ══════════════════════════════════════════════════════════════════════
    static void showInventory(Player p) {
        printHeader("INVENTORY  (" + p.inventory.size() + "/" + INVENTORY_CAPACITY + ")");
        if (p.inventory.isEmpty()) System.out.println("  (empty)");
        else for (Item it : p.inventory) System.out.println("  " + it);
        System.out.println("\n  Equipment Bag:");
        if (p.equipBag.isEmpty()) System.out.println("  (empty)");
        else for (Equipment eq : p.equipBag) System.out.println("  " + eq);
        System.out.printf("\n  Inventory Value: %dg\n", p.inventoryValue());
        pause();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  FLOOR MERCHANT (mid-dungeon shop)
    // ══════════════════════════════════════════════════════════════════════
    static void floorMerchant(Player p) {
        printHeader("★ WANDERING MERCHANT  (Gold: " + p.gold + "g)");
        System.out.println("  1) Health Potion x2  — 50g");
        System.out.println("  2) Mana Potion x2    — 40g");
        System.out.println("  3) Buff Potion x1    — 60g");
        System.out.println("  4) Iron Ore x3       — 30g");
        System.out.println("  0) Leave");
        int c = readInt("  > ", 0, 4);
        switch (c) {
            case 1:
                if (p.gold < 50) { System.out.println("  Not enough gold!"); break; }
                p.gold -= 50; p.statGoldSpent += 50;
                p.addItem(new Item("Health Potion", 2, "Common", 15, "HealthPotion"));
                System.out.println("  Bought 2x Health Potion!"); break;
            case 2:
                if (p.gold < 40) { System.out.println("  Not enough gold!"); break; }
                p.gold -= 40; p.statGoldSpent += 40;
                p.addItem(new Item("Mana Potion", 2, "Common", 12, "ManaPotion"));
                System.out.println("  Bought 2x Mana Potion!"); break;
            case 3:
                if (p.gold < 60) { System.out.println("  Not enough gold!"); break; }
                p.gold -= 60; p.statGoldSpent += 60;
                p.addItem(new Item("Buff Potion", 1, "Uncommon", 25, "BuffPotion"));
                System.out.println("  Bought 1x Buff Potion!"); break;
            case 4:
                if (p.gold < 30) { System.out.println("  Not enough gold!"); break; }
                p.gold -= 30; p.statGoldSpent += 30;
                p.addItem(new Item("Iron Ore", 3, "Common", 5, "Material"));
                System.out.println("  Bought 3x Iron Ore!"); break;
        }
        pause();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  DUNGEON FLOOR RUNNER
    // ══════════════════════════════════════════════════════════════════════
    static boolean runFloor(Player p, int floor) {
        printHeader("═══  DUNGEON FLOOR " + floor + "  ═══");
        List<Monster> monsters = generateFloorMonsters(floor);

        int floorGoldStart = p.statGoldEarned;
        int floorExpStart  = p.statExpGained;
        int floorPotStart  = p.statPotionsUsed;
        int floorKills     = 0;

        System.out.println("  Options:  1) View Stats  2) View Inventory  3) Proceed");
        int intro = readInt("  > ", 1, 3);
        if (intro == 1) { p.printStats(); pause(); }
        if (intro == 2) showInventory(p);

        // Random event
        if (RNG.nextInt(100) < 40) randomEvent(p);
        if (p.isDead()) return false;

        // Merchant on floors 2+
        if (floor >= 2 && RNG.nextInt(100) < 35) floorMerchant(p);

        // Fight each monster
        for (Monster m : monsters) {
            if (!m.alive) continue;
            System.out.printf("\n  ⚔ A %s appears!\n", m.name);
            boolean survived = doBattle(p, m, false);
            if (!survived) return false;
            if (m.alive && m.hp <= 0) floorKills++;  // killed (not escaped)
            else if (!m.alive)        {}              // escaped — don't count
            // Recalculate from stats
        }

        // Recalculate kills from stat delta for accuracy
        int actualKills = p.statMonstersKilled -
                          (p.statMonstersKilled - (p.statMonstersKilled)); // keep running total
        // Use floor-level delta approach
        floorKills = p.statMonstersKilled; // will be corrected per floor below

        // Treasure room
        if (RNG.nextInt(100) < 30) {
            printHeader("✦ TREASURE ROOM");
            int treasureGold = 30 + RNG.nextInt(50);
            p.gold += treasureGold; p.statGoldEarned += treasureGold;
            System.out.printf("  You found a treasure chest! +%dg\n", treasureGold);
            if (RNG.nextInt(100) < 50) {
                Item gem = new Item("Rare Gemstone", 1, "Rare", 75, "Drop");
                boolean added = p.addItem(gem);
                System.out.println(added ? "  + Rare Gemstone!" : "  Gemstone lost — inventory full!");
                if (added) p.lootObtained.add("Rare Gemstone x1");
            }
            pause();
        }

        p.statFloorsCleared++;

        // Pass per-floor deltas to summary
        floorSummary(p, floor,
            p.statMonstersKilled,           // cumulative shown in floor context
            p.statGoldEarned  - floorGoldStart,
            p.statExpGained   - floorExpStart,
            p.statPotionsUsed - floorPotStart);

        return true;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  BOSS FLOOR
    // ══════════════════════════════════════════════════════════════════════
    static boolean runBossFloor(Player p) {
        printHeader("═══  FLOOR 5 — THE FINAL CHAMBER  ═══");
        System.out.println("  The ground trembles. A roar echoes through the dungeon...");
        System.out.println("  Two Elite Guards block the path to the Dragon Overlord!");
        pause();

        // Track floor stats
        int floorGoldStart = p.statGoldEarned;
        int floorExpStart  = p.statExpGained;
        int floorPotStart  = p.statPotionsUsed;

        List<Monster> elites = generateFloorMonsters(5);
        for (Monster m : elites) {
            System.out.printf("\n  %s stands in your way!\n", m.name);
            if (!doBattle(p, m, false)) return false;
        }

        printHeader("★★★  BOSS: DRAGON OVERLORD  ★★★");
        System.out.println("  Phase 1 → Normal attacks");
        System.out.println("  Phase 2 (60% HP) → Stats increase + Drain ability");
        System.out.println("  Phase 3 (30% HP) → Drain intensifies");
        System.out.println("  Turn 8+ → ENRAGED: ATK+15 + Double Attack!");
        pause();

        Monster boss = generateBoss();
        p.statStrongestMonster = boss.name;
        boolean won = doBattle(p, boss, true);

        if (won) {
            p.statFloorsCleared++;
            printHeader("★★★  VICTORY!  ★★★");
            System.out.println("  The Dragon Overlord falls! You have conquered the dungeon!");
            System.out.printf("  Reward: +500g  +500 EXP\n");
            p.gold += 500; p.statGoldEarned += 500;
            p.addExp(500);
            pause();

            floorSummary(p, 5,
                p.statMonstersKilled,
                p.statGoldEarned  - floorGoldStart,
                p.statExpGained   - floorExpStart,
                p.statPotionsUsed - floorPotStart);
        }
        return won;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  EQUIPMENT MANAGEMENT
    // ══════════════════════════════════════════════════════════════════════
    static void equipmentManagement(Player p) {
        printHeader("EQUIPMENT MANAGEMENT");
        System.out.printf("  Equipped Weapon : %s\n", p.weapon != null ? p.weapon.name : "None");
        System.out.printf("  Equipped Armor  : %s\n", p.armor  != null ? p.armor.name  : "None");
        System.out.println("\n  Equipment Bag:");
        if (p.equipBag.isEmpty()) { System.out.println("  (empty)"); pause(); return; }
        for (int i = 0; i < p.equipBag.size(); i++)
            System.out.printf("  %d) %s\n", i+1, p.equipBag.get(i));
        System.out.println("  0) Back");
        int c = readInt("  Equip item # > ", 0, p.equipBag.size());
        if (c == 0) return;
        Equipment eq = p.equipBag.get(c-1);
        if (p.level < eq.levelReq) { System.out.println("  Level too low!"); pause(); return; }
        if (eq.slot.equals("Weapon")) { p.weapon = eq; System.out.println("  Equipped " + eq.name + " as weapon!"); }
        else                          { p.armor  = eq; System.out.println("  Equipped " + eq.name + " as armor!"); }
        pause();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  BATTLE RANKINGS (derived from battleHistory)
    // ══════════════════════════════════════════════════════════════════════

    /** Returns the BattleRecord with the most turns (longest battle). */
    static BattleRecord longestBattle(Player p) {
        BattleRecord best = null;
        for (BattleRecord r : p.battleHistory) {
            if (!r.wasEscape && (best == null || r.turnsUsed > best.turnsUsed)) best = r;
        }
        return best;
    }

    /** Returns the BattleRecord with the highest efficiency score. */
    static BattleRecord mostEfficientBattle(Player p) {
        BattleRecord best = null;
        for (BattleRecord r : p.battleHistory) {
            if (!r.wasEscape && (best == null || r.efficiencyScore() > best.efficiencyScore())) best = r;
        }
        return best;
    }

    /** Returns the BattleRecord with the most damage received (most dangerous). */
    static BattleRecord mostDangerousBattle(Player p) {
        BattleRecord best = null;
        for (BattleRecord r : p.battleHistory) {
            if (!r.wasEscape && (best == null || r.dmgReceived > best.dmgReceived)) best = r;
        }
        return best;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  RAID ANALYTICS REPORT
    // ══════════════════════════════════════════════════════════════════════
    static void generateReport(Player p, boolean victory) {
        long elapsedSec  = (System.currentTimeMillis() - p.startTimeMs) / 1000;
        int  totalGoldNet = p.statGoldEarned - p.statGoldSpent;

        p.statMostUsedItem  = p.mostUsedItem();
        if (p.weapon != null) p.statStrongestEquip = p.weapon.name;
        if (p.armor  != null && p.armor.defBonus > (p.weapon != null ? p.weapon.atkBonus : 0))
            p.statStrongestEquip = p.armor.name;

        // Rank score
        int score = p.statMonstersKilled * 10
                  + p.statFloorsCleared  * 50
                  + p.statDmgDealt       / 10
                  + p.level              * 20
                  - p.statDmgReceived    / 20;
        String rank;
        if (!victory)          rank = "F — Fallen Hero";
        else if (score >= 700) rank = "S — Legendary Raider";
        else if (score >= 500) rank = "A — Elite Raider";
        else if (score >= 300) rank = "B — Veteran Raider";
        else                   rank = "C — Brave Adventurer";

        // Battle rankings
        BattleRecord longest    = longestBattle(p);
        BattleRecord efficient  = mostEfficientBattle(p);
        BattleRecord dangerous  = mostDangerousBattle(p);

        // Highest value loot
        String topLoot = p.statHighestValueLoot.equals("None") ? "None" : p.statHighestValueLoot;

        printLine();
        System.out.println("        ╔══════════════════════════════════════╗");
        System.out.println("        ║       RAID ANALYTICS REPORT          ║");
        System.out.println("        ╚══════════════════════════════════════╝");
        printLine();
        System.out.printf("  Raid Result     : %s\n", victory ? "✔ SUCCESS" : "✖ FAILED");
        System.out.printf("  Raid Rank       : %s\n", rank);
        printLine();

        System.out.println("  ── PLAYER INFO ──");
        System.out.printf("  Name            : %s\n", p.name);
        System.out.printf("  Class           : %s\n", p.playerClass);
        System.out.printf("  Final Level     : %d\n", p.level);
        System.out.printf("  Remaining HP/MP : %d/%d  /  %d/%d\n",
            p.hp, p.totalMaxHp(), p.mp, p.maxMp);
        System.out.printf("  Final Gold      : %dg\n", p.gold);
        printLine();

        System.out.println("  ── COMBAT STATS ──");
        System.out.printf("  Monsters Defeated      : %d\n",   p.statMonstersKilled);
        System.out.printf("  Bosses Defeated        : %d\n",   p.statBossesKilled);
        System.out.printf("  Total Turns (all btls) : %d\n",   p.statTotalTurns);
        System.out.printf("  Total Damage Dealt     : %d\n",   p.statDmgDealt);
        System.out.printf("  Total Damage Received  : %d\n",   p.statDmgReceived);
        System.out.printf("  Total Healing Restored : %d\n",   p.statTotalHealing);
        System.out.printf("  Critical Hits Landed   : %d\n",   p.statCritsLanded);
        System.out.printf("  Highest Single Hit     : %d\n",   p.statHighestDmg);
        System.out.printf("  Avg Dmg / Battle       : %.1f\n",
            p.statMonstersKilled > 0 ? (double)p.statDmgDealt / p.statMonstersKilled : 0);
        printLine();

        System.out.println("  ── ECONOMY ──");
        System.out.printf("  Total Gold Earned      : %dg\n",  p.statGoldEarned);
        System.out.printf("  Total Gold Spent       : %dg\n",  p.statGoldSpent);
        System.out.printf("  Net Gold Profit/Loss   : %s%dg\n",totalGoldNet >= 0 ? "+" : "", totalGoldNet);
        System.out.printf("  Inventory Value        : %dg\n",  p.inventoryValue());
        System.out.printf("  Potions Used           : %d\n",   p.statPotionsUsed);
        printLine();

        System.out.println("  ── PROGRESSION ──");
        System.out.printf("  Dungeon Floors Cleared : %d/%d\n", p.statFloorsCleared, TOTAL_FLOORS);
        System.out.printf("  Total EXP Gained       : %d\n",   p.statExpGained);
        System.out.printf("  Level Milestones       : %s\n",
            p.levelMilestones.isEmpty() ? "None" : String.join(", ", p.levelMilestones));
        System.out.printf("  Playtime (seconds)     : %ds\n",  elapsedSec);
        printLine();

        System.out.println("  ── EQUIPMENT & ITEMS ──");
        System.out.printf("  Strongest Equipment    : %s\n",   p.statStrongestEquip);
        System.out.printf("  Most Used Item         : %s\n",   p.statMostUsedItem);
        String wpn = (p.weapon != null) ? p.weapon.name : "None";
        String arm = (p.armor  != null) ? p.armor.name  : "None";
        System.out.printf("  Final Weapon           : %s\n",   wpn);
        System.out.printf("  Final Armor            : %s\n",   arm);
        printLine();

        System.out.println("  ── RANKINGS & RECORDS ──");
        System.out.printf("  Strongest Monster Defeated   : %s\n", p.statStrongestMonster);
        System.out.printf("  Most Dangerous Monster       : %s (dealt %d dmg to you)\n",
            p.statMostDangerousMonster, p.statMostDangerousDmg);
        System.out.printf("  Highest Value Loot Obtained  : %s\n", topLoot);
        System.out.printf("  Lowest HP Survived With      : %s\n",
            p.statLowestHpSurvived == 9999 ? "N/A" : p.statLowestHpSurvived + " HP");
        System.out.printf("  Near-Death Moments           : %d\n", p.statDeathsAvoided);

        if (longest != null) {
            System.out.printf("  Longest Battle               : vs %s (%d turns)\n",
                longest.monsterName, longest.turnsUsed);
        } else {
            System.out.println("  Longest Battle               : N/A");
        }
        if (efficient != null) {
            System.out.printf("  Most Efficient Battle        : vs %s (score: %.1f)\n",
                efficient.monsterName, efficient.efficiencyScore());
        } else {
            System.out.println("  Most Efficient Battle        : N/A");
        }
        if (dangerous != null) {
            System.out.printf("  Most Dangerous Battle        : vs %s (%d dmg received)\n",
                dangerous.monsterName, dangerous.dmgReceived);
        } else {
            System.out.println("  Most Dangerous Battle        : N/A");
        }
        printLine();

        System.out.println("  ── PROFIT / LOSS STATUS ──");
        System.out.printf("  %s\n", totalGoldNet >= 0
            ? "✔ PROFITABLE RAID  (Net +" + totalGoldNet + "g)"
            : "✖ NET LOSS  (Spent " + Math.abs(totalGoldNet) + "g more than earned)");
        printLine();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  MAIN
    // ══════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║          ⚔  DUNGEON RAID RPG  ⚔                 ║");
        System.out.println("║   Survive 5 floors and defeat the Dragon Overlord ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        Player player = createCharacter();

        printHeader("BEFORE YOU ENTER THE DUNGEON");
        System.out.println("  You have time to visit the town before raiding.");
        visitShops(player);

        // Floors 1-4
        boolean alive = true;
        for (int floor = 1; floor <= 4 && alive; floor++) {
            alive = runFloor(player, floor);
            if (!alive) break;

            if (floor < 4) {
                printHeader("BETWEEN FLOORS — Rest & Prepare");
                System.out.println("  1) Continue to next floor");
                System.out.println("  2) View stats");
                System.out.println("  3) View inventory");
                System.out.println("  4) Manage equipment");
                int choice = readInt("  > ", 1, 4);
                if (choice == 2) { player.printStats(); pause(); }
                if (choice == 3) showInventory(player);
                if (choice == 4) equipmentManagement(player);
            }
        }

        // Boss floor
        boolean victory = false;
        if (alive) victory = runBossFloor(player);

        // Final report
        generateReport(player, victory);
        System.out.println("\n  Thanks for playing DUNGEON RAID RPG!");
        sc.close();
    }
}