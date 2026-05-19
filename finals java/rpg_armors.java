import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class rpg_armors {
    static Scanner Scanner = new Scanner(System.in);
    static String player_string_reply = null;
    static int player_numerical_reply = 0;

    static int[] STAT_MOD_TO_PLAYER_INDEX = { 5, 7, 4, 6, 3, 2 };

    static void ApplyArmorStats(Object[] armorEntry, double sign) {
        Object[] rolledData = (Object[]) armorEntry[0];
        Object[] prefix     = (Object[]) rolledData[0];
        Object[] suffix     = (Object[]) rolledData[2];
        double   rolledDef  = (double)   rolledData[3];

        double[] prefixMods = (double[]) prefix[1];
        double[] suffixMods = (double[]) suffix[1];

        PlayerInformation[6] = (double) PlayerInformation[6] + sign * rolledDef;

        for (int i = 0; i < prefixMods.length; i++) {
            int playerIndex = STAT_MOD_TO_PLAYER_INDEX[i];
            PlayerInformation[playerIndex] = (double) PlayerInformation[playerIndex] + sign * prefixMods[i];
            PlayerInformation[playerIndex] = (double) PlayerInformation[playerIndex] + sign * suffixMods[i];
        }
    }

   static Object[] PlayerInformation = {
            "Name",   // PlayerName [0]
            "Class",  // PlayerClass [1]
            100.0,    // HP [2]
            100.0,    // MP [3]
            10.0,     // Speed [4]
            35.5,      // Attack Damage [5]
            90.0,      // Defense [6]
            65.0,     // Critical Hit Chance [7]
            1.2 ,      // Critical Hit Multiplier [8]
            0,      // EXP [9]
            1,        // Player Level [10]
            5600.0,       // Starting Gold [11]

            null, // Equipped Weapon [12]
            new Object[] {       // Armor [13]
                null, // head
                null // plate
            },

            new Object[] {   // Raid Analytics Report [14]
                // ---- Raid Summary ----
                null,   // [14][0]  Player Name (reference to [0] at end)
                null,   // [14][1]  Final Level
                null,   // [14][2]  Class Chosen
                null,   // [14][3]  Remaining HP
                null,   // [14][4]  Remaining MP
                null,   // [14][5]  Final Gold Amount

                // ---- Combat Statistics ----
                0,      // [14][6]  Monsters Defeated
                0,      // [14][7]  Bosses Defeated
                0,      // [14][8]  Total Playtime Turns
                0.0,    // [14][9]  Total Damage Dealt
                0.0,    // [14][10] Total Damage Received
                0.0,    // [14][11] Total Healing Restored
                0.0,    // [14][12] Highest Single-Hit Damage
                0.0,    // [14][13] Average Damage Per Battle

                // ---- Economy Statistics ----
                0.0,    // [14][14] Total Gold Spent
                0.0,    // [14][15] Total Gold Earned
                0.0,    // [14][16] Inventory Value
                null,   // [14][17] Profit or Loss Status (e.g., "Profit", "Loss", "Break Even")

                // ---- Progression ----
                0,      // [14][18] Dungeon Floors Completed
                null,   // [14][19] Level Milestones Reached (e.g., int[] {5, 10, 15})

                // ---- Equipment & Items ----
                null,   // [14][20] Strongest Equipment Obtained
                null,   // [14][21] Most Used Item

                // ---- End Conditions ----
                null,   // [14][22] Raid Success Status (e.g., "Victory", "Escaped", "Defeated")
                null,   // [14][23] Overall Raid Rank (e.g., "S", "A", "B", "C", "D")

                // ---- Rankings ----
                null,   // [14][24] Highest Value Loot Obtained
                null,   // [14][25] Most Dangerous Monster Encountered
                null,   // [14][26] Most Efficient Battle (e.g., "Floor 3 - Goblin, 2 turns, 0 damage taken")
                null,   // [14][27] Longest Battle (e.g., "Floor 7 - Dragon, 18 turns")
                0.0     // [14][28] Lowest HP Survived With
            },

            new Object[] {}  // Inventory [15]
    };
        
    static Object[][] classes = {
        {
            "Warrior",      // Class [0]
            "A battle-hardened frontliner built to absorb punishment and deal steady damage.", // Description [1]
            220.0,          // HP [2]
            40.0,           // MP [3]
            9.0,            // Speed [4]
            35.5,           // Attack Damage [5]
            90.0,           // Defense [6]
            12.0,           // Critical Hit Chance [7]
            1.5,            // Critical Hit Multiplier [8]
            0,              // EXP [9]
            1,              // Player Level [10]
            0               // Starting Gold [11]
        },
        {
            "Mage",
            "A glass cannon that channels destructive arcane power at the cost of fragility.",
            80.0,
            200.0,
            8.0,
            72.0,
            18.0,
            22.0,
            2.0,
            0, 1, 0
        },
        {
            "Paladin",
            "A holy warrior who balances strong defense with divine healing and smite power.",
            200.0,
            90.0,
            7.0,
            30.0,
            80.0,
            10.0,
            1.6,
            0, 1, 0
        },
        {
            "Archer",
            "A ranged expert who picks off enemies from safety with precise, rapid shots.",
            100.0,
            55.0,
            15.0,
            48.0,
            22.0,
            40.0,
            1.9,
            0, 1, 0
        },
        {
            "Assassin",
            "A cold-blooded killer who vanishes into shadow and delivers lethal precision strikes.",
            88.0,
            75.0,
            22.0,
            55.0,
            18.0,
            70.0,
            3.0,
            0, 1, 0
        }
    };

    // ── ARMOR AFFINITIES ────────────────────────────────────────────────────
    // Replaces WeaponAffinities. Each type maps to an armor archetype with
    // its own stat identity and forbidden-affinity exclusions.
    static Object[] ArmorAffinities = {
        "Plate",      // 0  — Heavy DEF + HP  (Warrior/Paladin)
        "Mail",       // 1  — Balanced DEF + ATK
        "Leather",    // 2  — Light SPD + CRIT  (Archer)
        "Cloth",      // 3  — Caster MP heavy  (Mage)
        "Scale",      // 4  — Dragon DEF + ATK  (Paladin/Warrior)
        "Bone",       // 5  — Undead CRIT + ATK  (Assassin)
        "Crystal",    // 6  — Arcane MP + CRIT  (Mage)
        "Shadow",     // 7  — Assassin SPD + CRIT
        "Runed",      // 8  — Scholar MP + DEF
        "Beast",      // 9  — Ranger SPD + ATK  (Archer)
        "Warden",     // 10 — Fortress DEF + HP  (Paladin)
        "Celestial",  // 11 — Holy DEF + MP + HP
    };

    static final double[][] TIER_WEIGHT_BANDS = {
        { 0.40, 1.00 },  // 1 - Common
        { 0.15, 0.39 },  // 2 - Uncommon
        { 0.05, 0.14 },  // 3 - Rare
        { 0.02, 0.04 },  // 4 - Legendary
        { 0.00, 0.01 },  // 5 - Mythic
    };

    // ── BASE ARMOR POOL ──────────────────────────────────────────────────────
    // name           minDEF  maxDEF  affinity  type          statBias
    // statBias: [0]=ATK  [1]=CRIT  [2]=SPD  [3]=DEF  [4]=MP  [5]=HP
   static Object[][] BaseArmorPool = {
        // name              minDEF maxDEF aff  type        bias  slot
        { "Iron Helm",        15.0, 35.0,  0,  "plate",    3,    "head" },
        { "Plate Cuirass",    35.0, 70.0,  0,  "plate",    5,    "body" },
        { "Chainmail Vest",   20.0, 45.0,  1,  "mail",     0,    "body" },
        { "Ringmail Coif",    12.0, 28.0,  1,  "mail",     3,    "head" }, 
        { "Leather Armor",    10.0, 25.0,  2,  "leather",  2,    "body" },
        { "Mage Robes",        5.0, 15.0,  3,  "cloth",    4,    "body" },
        { "Tome Vestment",     8.0, 18.0,  3,  "cloth",    4,    "body" },
        { "Scale Hauberk",    28.0, 55.0,  4,  "scale",    3,    "body" },
        { "Bone Armor",       14.0, 32.0,  5,  "bone",     1,    "body" },
        { "Crystal Shroud",    6.0, 18.0,  6,  "crystal",  4,    "body" },
        { "Shadow Wraps",      8.0, 20.0,  7,  "shadow",   2,    "body" },
        { "Runed Coat",       10.0, 24.0,  8,  "runed",    4,    "body" },
        { "Beast Hide",       14.0, 30.0,  9,  "beast",    2,    "body" },
        { "Warden Plate",     30.0, 60.0, 10,  "warden",   3,    "body" },
        { "Celestial Mail",   18.0, 38.0, 11,  "celestial",5,    "body" },
    };

    // statMods index contract — same order everywhere
    // [0]=ATK  [1]=CRIT  [2]=SPD  [3]=DEF  [4]=MP  [5]=HP
    //
    // HP is new to the armor system. It does not exist in the weapon statMods
    // and reflects that armor is the primary source of survivability upgrades.

    static Object[][] ArmorPrefixPool = {
        // label           statMods [ATK,CRIT,SPD,DEF,MP,HP]    weight   tag         minStatReq  threshold
        // ── Common ────────────────────────────────────────────────────────────────
        { "Iron",        new double[]{  0,  0,  -5, 15,  0, 10 },  0.60,  "heavy",       3,        0.44 }, // DEF > 40
        { "Reinforced",  new double[]{  0,  0,   0, 12,  0,  8 },  0.65,  "heavy",      -1,        0.0  },
        { "Padded",      new double[]{  0,  0,   5,  8,  0,  5 },  0.65,  "comfort",    -1,        0.0  },
        { "Gilded",      new double[]{  5,  0,   0,  8,  0,  0 },  0.60,  "noble",      -1,        0.0  },
        { "Blessed",     new double[]{  0,  0,   0,  8, 10,  0 },  0.55,  "holy",       -1,        0.0  },
        { "Tempered",    new double[]{  5,  0,   0, 10,  0,  0 },  0.55,  "heavy",      -1,        0.0  },
        { "Swift",       new double[]{  0,  5,  20, -5,  0,  0 },  0.60,  "wind",        2,        0.40 }, // SPD > 8
        { "Stalwart",    new double[]{  0,  0,  -5, 22,  0, 15 },  0.55,  "iron",        3,        0.44 }, // DEF > 40

        // ── Uncommon ──────────────────────────────────────────────────────────────
        { "Enchanted",   new double[]{  0,  0,   0, 10, 20,  0 },  0.30,  "arcane",      4,        0.48 }, // MP > 100
        { "Warded",      new double[]{  0,  0,   0, 18,  0, 12 },  0.25,  "iron",        3,        0.50 }, // DEF > 45
        { "Soulforged",  new double[]{  0, 10,   0, 10, 10,  0 },  0.25,  "relic",      -1,        0.0  },
        { "Frost",       new double[]{  0,  8,  12,  5,  0,  0 },  0.35,  "frost",       2,        0.40 }, // SPD > 8
        { "Radiant",     new double[]{  0,  0,   0, 15, 15,  0 },  0.25,  "holy",        4,        0.40 }, // MP > 84
        { "Ethereal",    new double[]{  0,  5,  12,  0, 20,  0 },  0.25,  "arcane",      4,        0.45 }, // MP > 94
        { "Ancient",     new double[]{  5,  8,   5,  8,  0,  5 },  0.20,  "relic",      -1,        0.0  },
        { "Phantom",     new double[]{  0,  8,  18, -5,  0,  0 },  0.30,  "shadow",      2,        0.60 }, // SPD > 13
        { "Storm",       new double[]{  5, 10,  15,  0,  0,  0 },  0.30,  "wind",        2,        0.50 }, // SPD > 11
        { "Void",        new double[]{  5, 15,   0,  0,  0, -5 },  0.30,  "dark",        1,        0.43 }, // CRIT > 30

        // ── Rare ──────────────────────────────────────────────────────────────────
        { "Obsidian",    new double[]{  5,  0,  -5, 25,  0, 15 },  0.12,  "heavy",       3,        0.60 }, // DEF > 54
        { "Soulbound",   new double[]{  0, 18,   0, 10, 10,  5 },  0.10,  "relic",       1,        0.55 }, // CRIT > 38
        { "Cursed",      new double[]{ 20,  0,   0,-10,  0,  0 },  0.10,  "dark",       -1,        0.0  },
        { "Bloodforged", new double[]{ 20, 10, -10, 10,  0,  0 },  0.08,  "brutal",      0,        0.60 }, // ATK > 47
        { "Dragonscale", new double[]{  5,  0,   0, 30,  0, 20 },  0.07,  "fire",        3,        0.70 }, // DEF > 63

        // ── Legendary ─────────────────────────────────────────────────────────────
        { "Titanic",     new double[]{  0,  0,  -5, 40,  0, 35 },  0.04,  "heavy",      -1,        0.0  },
        { "Sanctified",  new double[]{  0,  0,   0, 25, 25, 20 },  0.03,  "holy",       -1,        0.0  },
        { "Abyssal",     new double[]{ 25, 20,   0,  5,  0,  0 },  0.03,  "dark",       -1,        0.0  },
        { "Draconic",    new double[]{ 15,  0,   0, 35,  0, 25 },  0.02,  "fire",       -1,        0.0  },

        // ── Mythic ────────────────────────────────────────────────────────────────
        { "Godforged",   new double[]{ 10, 15,   5, 50, 20, 40 },  0.01,  "relic",      -1,        0.0  },
        { "Wraithbound", new double[]{ 30, 40,  20,  0,  0,  0 },  0.008, "shadow",     -1,        0.0  },
        { "Undying",     new double[]{  0,  0,   0, 30, 30, 80 },  0.005, "holy",       -1,        0.0  },
    };

    static Object[][] ArmorSuffixPool = {
        // label                  statMods [ATK,CRIT,SPD,DEF,MP,HP]   weight    tag        minStatReq  threshold
        // ── Common ────────────────────────────────────────────────────────────────
        { "of the Fortress",  new double[]{  0,  0,  -5, 20,  0, 15 },  0.45,  "iron",        3,        0.40 }, // DEF > 36
        { "of Swiftness",     new double[]{  0,  0,  25,  0,  0,  0 },  0.50,  "wind",        2,        0.50 }, // SPD > 11
        { "of the Fallen",    new double[]{  0, 15,   0,  8,  0,  0 },  0.45,  "dark",       -1,        0.0  },
        { "of Valor",         new double[]{  8,  0,   0, 14,  0,  8 },  0.50,  "noble",      -1,        0.0  },
        { "of the Hunt",      new double[]{ 12,  0,  10,  0,  0,  0 },  0.50,  "wind",       -1,        0.0  },
        { "of the North",     new double[]{  0,  0,   8, 18,  0, 10 },  0.45,  "frost",       3,        0.40 }, // DEF > 36
        { "of Fury",          new double[]{ 15,  5,   0, -5,  0,  0 },  0.45,  "brutal",     -1,        0.0  },

        // ── Uncommon ──────────────────────────────────────────────────────────────
        { "of the Titan",     new double[]{  0,  0,  -5, 25,  0, 20 },  0.15,  "iron",        3,        0.50 }, // DEF > 45
        { "of Arcana",        new double[]{  0,  5,   0,  0, 35,  0 },  0.20,  "arcane",      4,        0.50 }, // MP > 105
        { "of the Sage",      new double[]{  0,  8,   0,  5, 30,  0 },  0.25,  "arcane",      4,        0.45 }, // MP > 94
        { "of the Storm",     new double[]{  8, 12,  15,  0,  0,  0 },  0.20,  "wind",        2,        0.55 }, // SPD > 12
        { "of Shadows",       new double[]{  5, 20,  10,  0,  0,  0 },  0.20,  "shadow",      1,        0.50 }, // CRIT > 35
        { "of the Ancients",  new double[]{  5,  8,   5,  8,  5,  8 },  0.15,  "relic",      -1,        0.0  },
        { "of Vengeance",     new double[]{ 14, 14,   0,  0,  0,  0 },  0.20,  "chaos",       1,        0.40 }, // CRIT > 28
        { "of the Wilds",     new double[]{  8,  5,  14,  0,  0,  0 },  0.30,  "wind",        2,        0.40 }, // SPD > 8
        { "of the Phoenix",   new double[]{  8,  0,   0,  0, 20, 15 },  0.20,  "fire",        4,        0.35 }, // MP > 73
        { "of the Void",      new double[]{ 10, 18,   0,  0,  0,  0 },  0.20,  "dark",        1,        0.43 }, // CRIT > 30

        // ── Rare ──────────────────────────────────────────────────────────────────
        { "of the Dragon",    new double[]{ 15,  0,   0, 30,  0, 15 },  0.07,  "fire",        3,        0.55 }, // DEF > 50
        { "of Precision",     new double[]{  0, 30,   5,  0,  0,  0 },  0.12,  "precise",     1,        0.50 }, // CRIT > 35
        { "of Malice",        new double[]{  5, 35,   5,  0,  0,  0 },  0.08,  "shadow",      1,        0.65 }, // CRIT > 45
        { "of Carnage",       new double[]{ 35,  0, -10,  0,  0,  0 },  0.08,  "brutal",      0,        0.50 }, // ATK > 39
        { "of Oblivion",      new double[]{ 20, 20,   0,-10,  0,  0 },  0.05,  "dark",        1,        0.70 }, // CRIT > 49

        // ── Legendary ─────────────────────────────────────────────────────────────
        { "of the Colossus",  new double[]{  0,  0,  -5, 35,  0, 30 },  0.04,  "iron",       -1,        0.0  },
        { "of the Eternal",   new double[]{  0,  0,   0, 20, 20, 25 },  0.03,  "holy",       -1,        0.0  },
        { "of Ruin",          new double[]{ 30, 25,   0,  0,  0,  0 },  0.03,  "dark",       -1,        0.0  },
        { "of the Wyrm",      new double[]{ 15,  0,   0, 30,  0, 20 },  0.02,  "fire",       -1,        0.0  },

        // ── Mythic ────────────────────────────────────────────────────────────────
        { "of Divinity",      new double[]{ 10, 10,   5, 40, 30, 50 },  0.01,  "holy",       -1,        0.0  },
        { "of the Abyss",     new double[]{ 35, 50,   0,  0,  0,  0 },  0.008, "dark",       -1,        0.0  },
        { "of Eternity",      new double[]{  0,  5,   0, 25, 40, 45 },  0.005, "arcane",     -1,        0.0  },
    };

    static int minimum_amount_of_armors = 5;
    static int maximum_amount_of_armors = 10;
    static Object[] CreatedArmors = {};

    // ── ARCHETYPE MATRIX ──────────────────────────────────────────────────────
    // Each row is one armor affinity's ideal stat profile (normalized 0–1).
    // Weighted Euclidean distance to this matrix drives primary/secondary affinity.
    // Layout: HP, MP, SPD, ATK, DEF, CRIT, CRITMUL
    //          [0] [1] [2]  [3]  [4]   [5]   [6]
    static Object[] DeriveArmorAffinities(double[] normalized) {
        double[][] archetypes = {
            // HP    MP    SPD   ATK   DEF   CRIT  CRITMUL
            { 0.9,  0.1,  0.2,  0.4,  1.0,  0.1,  0.2 },  // 0  Plate
            { 0.6,  0.2,  0.4,  0.6,  0.7,  0.3,  0.3 },  // 1  Mail
            { 0.3,  0.2,  0.9,  0.4,  0.2,  0.6,  0.5 },  // 2  Leather
            { 0.1,  1.0,  0.2,  0.2,  0.1,  0.4,  0.5 },  // 3  Cloth
            { 0.6,  0.1,  0.4,  0.8,  0.8,  0.3,  0.4 },  // 4  Scale
            { 0.3,  0.2,  0.5,  0.7,  0.3,  0.8,  0.9 },  // 5  Bone
            { 0.2,  0.9,  0.3,  0.3,  0.2,  0.7,  0.6 },  // 6  Crystal
            { 0.2,  0.3,  1.0,  0.5,  0.1,  0.9,  0.8 },  // 7  Shadow
            { 0.3,  0.8,  0.2,  0.3,  0.5,  0.4,  0.4 },  // 8  Runed
            { 0.4,  0.2,  0.8,  0.7,  0.2,  0.7,  0.6 },  // 9  Beast
            { 0.7,  0.5,  0.2,  0.4,  0.9,  0.1,  0.2 },  // 10 Warden
            { 0.6,  0.7,  0.2,  0.3,  0.8,  0.2,  0.3 },  // 11 Celestial
        };

        // HP and DEF are weighted higher for armor affinity fitting
        double[] weights = { 0.9, 0.9, 0.8, 0.9, 1.0, 0.9, 0.8 };
        double[] distances = new double[archetypes.length];

        for (int a = 0; a < archetypes.length; a++) {
            double sum = 0.0;
            for (int s = 0; s < normalized.length; s++) {
                double diff = normalized[s] - archetypes[a][s];
                sum += weights[s] * (diff * diff);
            }
            distances[a] = Math.sqrt(sum);
        }

        int primary_affinity   = 0;
        int secondary_affinity = 1;

        for (int i = 1; i < distances.length; i++) {
            if (distances[i] < distances[primary_affinity]) {
                secondary_affinity = primary_affinity;
                primary_affinity   = i;
            } else if (distances[i] < distances[secondary_affinity]) {
                secondary_affinity = i;
            }
        }

        return new Object[] {
            primary_affinity,
            secondary_affinity,
            distances,
            ArmorAffinities[primary_affinity],
            ArmorAffinities[secondary_affinity]
        };
    }

    // ── FORBIDDEN AFFINITY MAP ────────────────────────────────────────────────
    // Plate=0, Mail=1, Leather=2, Cloth=3, Scale=4, Bone=5,
    // Crystal=6, Shadow=7, Runed=8, Beast=9, Warden=10, Celestial=11
    //
    // Heavy classes exclude light/caster armors; light/caster classes exclude
    // heavy armors. This mirrors the weapon forbidden-affinity logic exactly.
    static int[] DeriveForbiddenArmorAffinities(double[] normalized, int primary_affinity, int secondary_affinity) {
        int[][] ForbiddenArmorMapping = {
            { 3, 6, 7, 8 },      // 0  Plate     → forbids Cloth, Crystal, Shadow, Runed
            { 3, 6 },            // 1  Mail      → forbids Cloth, Crystal
            { 0, 4, 10, 11 },    // 2  Leather   → forbids Plate, Scale, Warden, Celestial
            { 0, 4, 5, 10 },     // 3  Cloth     → forbids Plate, Scale, Bone, Warden
            { 3, 6, 7, 8 },      // 4  Scale     → forbids Cloth, Crystal, Shadow, Runed
            { 0, 10, 11, 3 },    // 5  Bone      → forbids Plate, Warden, Celestial, Cloth
            { 0, 4, 5, 10 },     // 6  Crystal   → forbids Plate, Scale, Bone, Warden
            { 0, 4, 10, 11 },    // 7  Shadow    → forbids Plate, Scale, Warden, Celestial
            { 0, 4, 5 },         // 8  Runed     → forbids Plate, Scale, Bone
            { 0, 3, 10, 11 },    // 9  Beast     → forbids Plate, Cloth, Warden, Celestial
            { 3, 6, 7, 8 },      // 10 Warden    → forbids Cloth, Crystal, Shadow, Runed
            { 5, 7, 6, 3 },      // 11 Celestial → forbids Bone, Shadow, Crystal, Cloth
        };

        int[] Primary   = ForbiddenArmorMapping[primary_affinity];
        int[] Secondary = ForbiddenArmorMapping[secondary_affinity];
        List<Integer> forbidden = new ArrayList<>();

        for (int affinity : Primary) {
            if (!forbidden.contains(affinity)) forbidden.add(affinity);
        }
        for (int affinity : Secondary) {
            if (!forbidden.contains(affinity)) forbidden.add(affinity);
        }

        forbidden.removeIf(f -> f == primary_affinity || f == secondary_affinity);
        return forbidden.stream().mapToInt(Integer::intValue).toArray();
    }

    // Unchanged from weapons — tags describe the class's top 3 stat identities
    static String[] DeriveArmorTags(double[] normalized) {
        String[] stats = {
            "endurance",  // HP
            "arcane",     // MP
            "swift",      // SPD
            "brutal",     // ATK
            "iron",       // DEF
            "precise",    // CRIT
            "lethal"      // CRIT_MULT
        };

        String[] tags = new String[3];
        boolean[] used = new boolean[normalized.length];

        for (int t = 0; t < 3; t++) {
            int best = -1;
            for (int i = 0; i < normalized.length; i++) {
                if (!used[i] && (best == -1 || normalized[i] > normalized[best])) {
                    best = i;
                }
            }
            tags[t] = stats[best];
            used[best] = true;
        }

        return tags;
    }

    // Identical normalization logic — class stats drive the entire generation pipeline
    static Object[] GenerateBaseArmorData(Object[] Class) {
        double[] stat_maxes = new double[7];
        for (Object[] c : classes) {
            stat_maxes[0] = Math.max(stat_maxes[0], (double) c[2]);
            stat_maxes[1] = Math.max(stat_maxes[1], (double) c[3]);
            stat_maxes[2] = Math.max(stat_maxes[2], (double) c[4]);
            stat_maxes[3] = Math.max(stat_maxes[3], (double) c[5]);
            stat_maxes[4] = Math.max(stat_maxes[4], (double) c[6]);
            stat_maxes[5] = Math.max(stat_maxes[5], (double) c[7]);
            stat_maxes[6] = Math.max(stat_maxes[6], (double) c[8]);
        }

        double[] normalized = new double[7];
        normalized[0] = (double) Class[2] / stat_maxes[0];  // HP
        normalized[1] = (double) Class[3] / stat_maxes[1];  // MP
        normalized[2] = (double) Class[4] / stat_maxes[2];  // Speed
        normalized[3] = (double) Class[5] / stat_maxes[3];  // ATK
        normalized[4] = (double) Class[6] / stat_maxes[4];  // DEF
        normalized[5] = (double) Class[7] / stat_maxes[5];  // CRIT
        normalized[6] = (double) Class[8] / stat_maxes[6];  // CRIT_MULT

        Object[] DerivedAffinities = DeriveArmorAffinities(normalized);
        Object[] Tags              = DeriveArmorTags(normalized);
        int[] ForbiddenArchetypes  = DeriveForbiddenArmorAffinities(
            normalized,
            (int) DerivedAffinities[0],
            (int) DerivedAffinities[1]
        );

        return new Object[] { DerivedAffinities, Tags, ForbiddenArchetypes, normalized };
    }

    // minStatReq → normalized index mapping
    // 0=ATK→[3]  1=CRIT→[5]  2=SPD→[2]  3=DEF→[4]  4=MP→[1]  5=HP→[0]
    // HP (req=5) is new for the armor system; maps to normalized[0]
    static int[] ArmorStatReqMap = { 3, 5, 2, 4, 1, 0 };

    static Object[] FilterArmorStats(int[] forbidden_affinities, double[] normalized_stats, int tier) {
        List<Object[]> FilteredArmors  = new ArrayList<>();
        List<Object[]> ScoredPrefixes  = new ArrayList<>();
        List<Object[]> ScoredSuffixes  = new ArrayList<>();

        double tierMin = TIER_WEIGHT_BANDS[tier - 1][0];
        double tierMax = TIER_WEIGHT_BANDS[tier - 1][1];

        outer:
        for (Object[] armor : BaseArmorPool) {
            int armorAffinity = (int) armor[3];
            for (int forbidden : forbidden_affinities) {
                if (armorAffinity == forbidden) continue outer;
            }
            FilteredArmors.add(armor);
        }

        for (Object[] prefix : ArmorPrefixPool) {
            double weight           = (double) prefix[2];
            int    minStatReq       = (int)    prefix[4];
            double minStatThreshold = (double) prefix[5];
            if (weight < tierMin || weight > tierMax) continue;
            if (minStatReq != -1 && normalized_stats[ArmorStatReqMap[minStatReq]] < minStatThreshold) continue;
            ScoredPrefixes.add(prefix);
        }

        // Fallback: if no prefixes passed both the tier filter and stat-req gate,
        // retry with the tier filter only (relaxes stat requirements).
        if (ScoredPrefixes.isEmpty()) {
            for (Object[] prefix : ArmorPrefixPool) {
                double weight = (double) prefix[2];
                if (weight >= tierMin && weight <= tierMax) {
                    ScoredPrefixes.add(prefix);
                }
            }
        }

        for (Object[] suffix : ArmorSuffixPool) {
            double weight           = (double) suffix[2];
            int    minStatReq       = (int)    suffix[4];
            double minStatThreshold = (double) suffix[5];
            if (weight < tierMin || weight > tierMax) continue;
            if (minStatReq != -1 && normalized_stats[ArmorStatReqMap[minStatReq]] < minStatThreshold) continue;
            ScoredSuffixes.add(suffix);
        }

        // Same fallback for suffixes.
        if (ScoredSuffixes.isEmpty()) {
            for (Object[] suffix : ArmorSuffixPool) {
                double weight = (double) suffix[2];
                if (weight >= tierMin && weight <= tierMax) {
                    ScoredSuffixes.add(suffix);
                }
            }
        }

        return new Object[] { FilteredArmors, ScoredPrefixes, ScoredSuffixes };
    }

    static Object[] WeightArmorStats(double[] normalizedClassStats, List<Object[]> scored_bases, List<Object[]> scored_prefixes, List<Object[]> scored_suffixes) {
        // classMods order matches statMods: [ATK, CRIT, SPD, DEF, MP, HP]
        // HP is now included at index [5] — new for the armor system
        double[] classMods = {
            normalizedClassStats[3],  // [0] ATK
            normalizedClassStats[5],  // [1] CRIT
            normalizedClassStats[2],  // [2] SPD
            normalizedClassStats[4],  // [3] DEF
            normalizedClassStats[1],  // [4] MP
            normalizedClassStats[0],  // [5] HP  ← armor extension
        };

        double[][] ScoredPrefixes = new double[scored_prefixes.size()][2];
        for (int i = 0; i < scored_prefixes.size(); i++) {
            double[] statMods = (double[]) scored_prefixes.get(i)[1];
            double weight     = (double)   scored_prefixes.get(i)[2];

            double maxMod = 0;
            for (double mod : statMods) maxMod = Math.max(maxMod, Math.abs(mod));

            double dot = 0;
            for (int s = 0; s < statMods.length; s++) {
                double normalizedMod = maxMod > 0 ? statMods[s] / maxMod : 0;
                dot += normalizedMod * classMods[s];
            }

            ScoredPrefixes[i][0] = i;
            ScoredPrefixes[i][1] = weight * (1 + dot);
        }

        double[][] ScoredSuffixes = new double[scored_suffixes.size()][2];
        for (int i = 0; i < scored_suffixes.size(); i++) {
            double[] statMods = (double[]) scored_suffixes.get(i)[1];
            double weight     = (double)   scored_suffixes.get(i)[2];

            double maxMod = 0;
            for (double mod : statMods) maxMod = Math.max(maxMod, Math.abs(mod));

            double dot = 0;
            for (int s = 0; s < statMods.length; s++) {
                double normalizedMod = maxMod > 0 ? statMods[s] / maxMod : 0;
                dot += normalizedMod * classMods[s];
            }

            ScoredSuffixes[i][0] = i;
            ScoredSuffixes[i][1] = weight * (1 + dot);
        }

        // statBias [0]=ATK [1]=CRIT [2]=SPD [3]=DEF [4]=MP [5]=HP
        double[][] ScoredBases = new double[scored_bases.size()][2];
        for (int i = 0; i < scored_bases.size(); i++) {
            int statBias = (int) scored_bases.get(i)[5];
            double score = 1.0 * (1 + classMods[statBias]);

            ScoredBases[i][0] = i;
            ScoredBases[i][1] = score;
        }

        return new Object[] { ScoredPrefixes, ScoredBases, ScoredSuffixes };
    }

    // Unchanged — weighted random selection by accumulated probability
    static Object[] WeightedArmorPick(double[][] scored, List<Object[]> pool) {
        double total = 0;
        for (double[] entry : scored) total += entry[1];

        double roll    = Math.random() * total;
        double running = 0;

        for (double[] entry : scored) {
            running += entry[1];
            if (running > roll) return pool.get((int) entry[0]);
        }

        if (pool.isEmpty()) {
            throw new IllegalStateException(
                "WeightedPick: pool is empty — check FilterArmorStats fallback logic");
        }
        return pool.get(0);
    }

    static Object[] RollArmor(Object[] weighted_stats, List<Object[]> filtered_prefixes, List<Object[]> filtered_bases, List<Object[]> filtered_suffixes) {
        double[][] ScoredPrefixes = (double[][]) weighted_stats[0];
        double[][] ScoredBases    = (double[][]) weighted_stats[1];
        double[][] ScoredSuffixes = (double[][]) weighted_stats[2];

        Object[] SelectedPrefix = WeightedArmorPick(ScoredPrefixes, filtered_prefixes);
        Object[] SelectedBase   = WeightedArmorPick(ScoredBases,    filtered_bases);
        Object[] SelectedSuffix = WeightedArmorPick(ScoredSuffixes, filtered_suffixes);

        // base[1]=minDEF  base[2]=maxDEF — replaced minATK/maxATK
        double MinDefense    = (double) SelectedBase[1];
        double MaxDefense    = (double) SelectedBase[2];
        double RolledDefense = MinDefense + Math.random() * (MaxDefense - MinDefense);

        return new Object[] { SelectedPrefix, SelectedBase, SelectedSuffix, RolledDefense };
    }

    static Object[] ComposeArmors(int[] ForbiddenAffinities, double[] NormalizedClassStats, int tier) {
        Object[] FilteredData = FilterArmorStats(ForbiddenAffinities, NormalizedClassStats, tier);

        List<Object[]> filteredArmors   = (List<Object[]>) FilteredData[0];
        List<Object[]> filteredPrefixes = (List<Object[]>) FilteredData[1];
        List<Object[]> filteredSuffixes = (List<Object[]>) FilteredData[2];

        Object[] WeightedData = WeightArmorStats(
            NormalizedClassStats,
            filteredArmors,
            filteredPrefixes,
            filteredSuffixes
        );

        Object[] RolledData = RollArmor(
            WeightedData,
            filteredPrefixes,
            filteredArmors,
            filteredSuffixes
        );

        return RolledData;
    }

    static int PriceArmor(Object[] rolledArmor, int primaryAffinity, int secondaryAffinity) {
        Object[] prefix        = (Object[]) rolledArmor[0];
        Object[] base          = (Object[]) rolledArmor[1];
        Object[] suffix        = (Object[]) rolledArmor[2];
        double   rolledDefense = (double)   rolledArmor[3];

        double[] prefixMods   = (double[]) prefix[1];
        double   prefixWeight = (double)   prefix[2];

        double[] suffixMods   = (double[]) suffix[1];
        double   suffixWeight = (double)   suffix[2];

        int armorAffinity = (int) base[3];

        // Armor pricing anchors on defense (× 4.0 vs weapon's ATK × 3.5)
        double basePrice   = rolledDefense * 4.0;
        double rarityScore = ((1.0 - prefixWeight) + (1.0 - suffixWeight)) / 2.0;
        double rarityMult  = 1.0 + (rarityScore * 0.45);

        // Gold value per stat point: ATK, CRIT, SPD, DEF, MP, HP
        // DEF and HP are the premium stats for armor; ATK/SPD are off-roles
        double[] statGoldValues = { 1.0, 1.5, 0.8, 1.8, 0.7, 1.2 };
        double statBonus = 0;

        for (int s = 0; s < prefixMods.length; s++) {
            statBonus += prefixMods[s] * statGoldValues[s];
            statBonus += suffixMods[s] * statGoldValues[s];
        }

        statBonus = Math.max(-20.0, Math.min(statBonus, 40.0));

        double demandMult = 1.0;
        if (armorAffinity == primaryAffinity)        demandMult = 1.10;
        else if (armorAffinity == secondaryAffinity) demandMult = 1.05;

        double rawPrice = (basePrice + statBonus) * rarityMult * demandMult;
        rawPrice = Math.max(15.0, rawPrice);

        return (int) Math.round(rawPrice / 5.0) * 5;
    }

    static void GenerateArmorForClass(Object Class) {
        Object[] BaseData     = GenerateBaseArmorData((Object[]) Class);
        Object[] AffinityData = (Object[]) BaseData[0];

        int PrimaryAffinity   = (int) AffinityData[0];
        int SecondaryAffinity = (int) AffinityData[1];

        int extras = (int) (Math.random() * (maximum_amount_of_armors - minimum_amount_of_armors + 1));
        int armors_to_generate = 5 + extras;
        CreatedArmors = new Object[armors_to_generate];

        // Guaranteed one armor per tier (Common → Mythic)
        for (int tier = 1; tier <= 5; tier++) {
            Object[] RolledData = ComposeArmors((int[]) BaseData[2], (double[]) BaseData[3], tier);
            int Price = PriceArmor(RolledData, PrimaryAffinity, SecondaryAffinity);
            CreatedArmors[tier - 1] = new Object[] { RolledData, Price };
        }

        // Random-tier extras
        for (int i = 5; i < armors_to_generate; i++) {
            int randomTier = (int) (Math.random() * 5) + 1;
            Object[] RolledData = ComposeArmors((int[]) BaseData[2], (double[]) BaseData[3], randomTier);
            int Price = PriceArmor(RolledData, PrimaryAffinity, SecondaryAffinity);
            CreatedArmors[i] = new Object[] { RolledData, Price };
        }
    }

    static void OpenArmorsShop() {
        String[] statLabels = { "ATK", "CRIT", "SPD", "DEF", "MP", "HP" };

        PrintDivider(true);
        System.out.println("  ARMOR SHOP  (" + CreatedArmors.length + " items available)");
        PrintDivider(false);

        for (int i = 0; i < CreatedArmors.length; i++) {
            Object[] entry      = (Object[]) CreatedArmors[i];
            Object[] rolledData = (Object[]) entry[0];
            int      price      = (int)      entry[1];

            Object[] prefix  = (Object[]) rolledData[0];
            Object[] base    = (Object[]) rolledData[1];
            Object[] suffix  = (Object[]) rolledData[2];
            double   defense = (double)   rolledData[3];

            String prefixLabel = (String) prefix[0];
            String baseName    = (String) base[0];
            String suffixLabel = (String) suffix[0];
            String armorName   = prefixLabel + " " + baseName + " " + suffixLabel;

            double[] prefixMods = (double[]) prefix[1];
            double[] suffixMods = (double[]) suffix[1];

            System.out.printf("%n  [%d] %s%n", i + 1, armorName);
            PrintDivider(false);
            System.out.printf("  %-22s %s%n", "Type:", ((String) base[4]).substring(0, 1).toUpperCase() + ((String) base[4]).substring(1));
            System.out.printf("  %-22s %.1f%n", "Defense:", defense);

            System.out.printf("  %-22s%n", "Stat Modifiers:");
            boolean anyMod = false;
            for (int s = 0; s < statLabels.length; s++) {
                double total = prefixMods[s] + suffixMods[s];
                if (total != 0) {
                    String sign = total > 0 ? "+" : "";
                    System.out.printf("    %-20s %s%.1f%n", statLabels[s] + ":", sign, total);
                    anyMod = true;
                }
            }
            if (!anyMod) System.out.printf("    %-20s %s%n", "None", "");

            System.out.printf("  %-22s %dg%n", "Price:", price);
            PrintDivider(false);
        }
        PrintDivider(true);
        System.out.println("Please type 'exit' to exit shop.");

       boolean running = true;

        while (running) {
            PrintDivider(false);
            System.out.print("What would you like to buy?: ");
            player_string_reply = Scanner.nextLine();

            if (player_string_reply.equalsIgnoreCase("exit")) {
                running = false;
                continue;
            }

            player_numerical_reply = Integer.parseInt(player_string_reply);

            if (player_numerical_reply <= 0 || player_numerical_reply > CreatedArmors.length) {
                System.out.println("Invalid Reply.");
                continue;
            }

            Object[] SelectedArmor = (Object[]) CreatedArmors[player_numerical_reply - 1];
            double ArmorCost  = (double)(int) SelectedArmor[1];
            double PlayerGold = (double) PlayerInformation[11];

            Object[] slots    = (Object[]) PlayerInformation[13];
            Object[] ArmorData = (Object[]) SelectedArmor[0];
            Object[] prefix = (Object[]) ArmorData[0];
            Object[] base   = (Object[]) ArmorData[1];
            Object[] suffix = (Object[]) ArmorData[2];

            String armorSlot = (String) base[6];
            int slotIndex = armorSlot.equals("head") ? 0 : 1;

            if (PlayerGold < ArmorCost) {
                PrintDivider(false);
                System.out.println("You don't have enough gold. You need " + (ArmorCost - PlayerGold) + " more gold to purchase this armor.");
            } else {
                PrintDivider(false);
                System.out.println("You equipped: " + prefix[0] + " " + base[0] + " " + suffix[0]);

                Object[] currentInSlot = (Object[]) slots[slotIndex];

                if (currentInSlot == null) {
                    ApplyArmorStats(SelectedArmor, +1.0);
                } else {
                    ApplyArmorStats(currentInSlot, -1.0);
                    Object[] CurrentInventory = (Object[]) PlayerInformation[15];
                    Object[] NewInventory = new Object[CurrentInventory.length + 1];
                    System.arraycopy(CurrentInventory, 0, NewInventory, 0, CurrentInventory.length);
                    NewInventory[CurrentInventory.length] = currentInSlot;
                    PlayerInformation[15] = NewInventory;
                    ApplyArmorStats(SelectedArmor, +1.0);
                }

                slots[slotIndex] = SelectedArmor;
                PlayerInformation[11] = PlayerGold - ArmorCost;
                running = false; 
            }
        }
        PrintDivider(false);
        System.out.println("Exiting shop...");
        System.out.println("You've exited the shop");
    }

    static void PrintDivider(boolean create_newline){
        if (create_newline) {
            System.out.println("\n──────────────────────────────────────────────────");
        } else {
            System.out.println("──────────────────────────────────────────────────");
        }
    }

    public static void main(String[] args) {

        System.out.println("\n[SYSTEM]: Generating armor for class");
        GenerateArmorForClass(classes[0]);  // Warrior
        System.out.println("[SYSTEM]: Finished creating armor");
        OpenArmorsShop();
    }
}