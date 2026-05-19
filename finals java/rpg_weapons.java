import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class rpg_weapons {
    static Scanner Scanner = new Scanner(System.in);
    static String player_string_reply = null;
    static int player_numerical_reply = 0;

    static int[] STAT_MOD_TO_PLAYER_INDEX = { 5, 7, 4, 6, 3, 2 };

    static void ApplyWeaponStats(Object[] weaponEntry, double sign) {
        Object[] rolledData   = (Object[]) weaponEntry[0];
        Object[] prefix       = (Object[]) rolledData[0];
        Object[] suffix       = (Object[]) rolledData[2];
        double   rolledAttack = (double)   rolledData[3];

        double[] prefixMods = (double[]) prefix[1];
        double[] suffixMods = (double[]) suffix[1];

        // Apply the weapon's rolled attack value to player ATK
        PlayerInformation[5] = (double) PlayerInformation[5] + sign * rolledAttack;

        // Apply each prefix and suffix stat modifier
        for (int i = 0; i < prefixMods.length; i++) {
            int playerIndex = STAT_MOD_TO_PLAYER_INDEX[i];
            PlayerInformation[playerIndex] = (double) PlayerInformation[playerIndex] + sign * prefixMods[i];
            PlayerInformation[playerIndex] = (double) PlayerInformation[playerIndex] + sign * suffixMods[i];
        }
    }

    static  Object[] PlayerInformation = {
            "Name",   // PlayerName [0]
            "Class",  // PlayerClass [1]
            100.0,    // HP [2]
            100.0,    // MP [3]
            10.0,     // Speed [4]
            35.5,      // Attack Damage [5]
            90.0,      // Defense [6]
            65.0,     // Critical Hit Chance [7]
            1.2 ,      // Critical Hit Multiplier [8]
            0.0,      // EXP [9]
            1.0,        // Player Level [10]
            350.0,       // Starting Gold [11]

            null, // Equipped Weapon [12]
            null, // armor

            new Object[] {
                null
            }//inventory
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
            "Mage",         // Class [0]
            "A glass cannon that channels destructive arcane power at the cost of fragility.", // Description [1]
            80.0,           // HP [2]
            200.0,          // MP [3]
            8.0,            // Speed [4]
            72.0,           // Attack Damage [5]
            18.0,           // Defense [6]
            22.0,           // Critical Hit Chance [7]
            2.0,            // Critical Hit Multiplier [8]
            0,              // EXP [9]
            1,              // Player Level [10]
            0               // Starting Gold [11]
        },
        {
            "Paladin",      // Class [0]
            "A holy warrior who balances strong defense with divine healing and smite power.", // Description [1]
            200.0,          // HP [2]
            90.0,           // MP [3]
            7.0,            // Speed [4]
            30.0,           // Attack Damage [5]
            80.0,           // Defense [6]
            10.0,           // Critical Hit Chance [7]
            1.6,            // Critical Hit Multiplier [8]
            0,              // EXP [9]
            1,              // Player Level [10]
            0               // Starting Gold [11]
        },
        {
            "Archer",       // Class [0]
            "A ranged expert who picks off enemies from safety with precise, rapid shots.", // Description [1]
            100.0,          // HP [2]
            55.0,           // MP [3]
            15.0,           // Speed [4]
            48.0,           // Attack Damage [5]
            22.0,           // Defense [6]
            40.0,           // Critical Hit Chance [7]
            1.9,            // Critical Hit Multiplier [8]
            0,              // EXP [9]
            1,              // Player Level [10]
            0               // Starting Gold [11]
        },
        {
            "Assassin",     // Class [0]
            "A cold-blooded killer who vanishes into shadow and delivers lethal precision strikes.", // Description [1]
            88.0,           // HP [2]
            75.0,           // MP [3]
            22.0,           // Speed [4]
            55.0,           // Attack Damage [5]
            18.0,           // Defense [6]
            70.0,           // Critical Hit Chance [7]
            3.0,            // Critical Hit Multiplier [8]
            0,              // EXP [9]
            1,              // Player Level [10]
            0               // Starting Gold [11]
        }
    };

    static Object[] WeaponAffinities = {
        "Blade",      // 0
        "Staff",      // 1
        "Grimoire",   // 2
        "Totem",      // 3
        "Orb",        // 4
        "Bow",        // 5
        "Crossbow",   // 6
        "Fist",       // 7
        "Shield",     // 8
        "Dagger",     // 9
        "Scythe",     // 10
        "Spear",      // 11
    };

    static Object[][] BaseWeaponPool = {
        // name           minATK  maxATK  affinity  type         statBias
        { "Dagger",        10.0,  25.0,     0,     "blade",       1 },  // biases CRIT
        { "Greatsword",    30.0,  60.0,     0,     "blade",       0 },  // biases ATK
        { "Shortsword",    18.0,  38.0,     0,     "blade",       2 },  // biases SPD
        { "Staff",         20.0,  50.0,     1,     "staff",       4 },  // biases MP
        { "Wand",          15.0,  40.0,     1,     "staff",       1 },  // biases CRIT
        { "Shortbow",      15.0,  40.0,     5,     "bow",         2 },  // biases SPD  -- was affinity 2 (Grimoire)
        { "Longbow",       25.0,  55.0,     5,     "bow",         0 },  // biases ATK  -- was affinity 2 (Grimoire)
        { "Tome",          25.0,  55.0,     2,     "grimoire",    4 },  // biases MP   -- was affinity 4 (Orb), type "tome"
        { "Gauntlets",     20.0,  45.0,     7,     "fist",        0 },  // biases ATK  -- was affinity 3 (Totem)
        { "Katar",         18.0,  38.0,     7,     "fist",        1 },  // biases CRIT -- was affinity 3 (Totem)
        { "Tower Shield",   5.0,  15.0,     8,     "shield",      3 },  // biases DEF  -- was affinity 5 (Bow)
    };
    
    // statMods index contract — same order everywhere
    // [0]=ATK  [1]=CRIT  [2]=SPD  [3]=DEF  [4]=MP

    static Object[][] WeaponPrefixPool = {
        // label           statMods [ATK,CRIT,SPD,DEF,MP]    weight   tag        minStatReq  threshold
        // Common
        { "Burning",   new double[]{ 10,  0,   0,   0,  0 },  0.65,  "fire",        -1,        0.0  },
        { "Swift",     new double[]{  0,  5,  20,   0,  0 },  0.60,  "wind",        -1,        0.0  },
        { "Iron",      new double[]{  5,  0,   0,  15,  0 },  0.60,  "heavy",        3,        0.56 }, // DEF > 50
        { "Gilded",    new double[]{  8,  0,   0,   5,  0 },  0.70,  "noble",       -1,        0.0  },
        { "Jagged",    new double[]{ 12,  8,   0,  -5,  0 },  0.55,  "brutal",      -1,        0.0  },
        { "Tempered",  new double[]{ 10,  0,   0,  10,  0 },  0.55,  "heavy",       -1,        0.0  },
        { "Stalwart",  new double[]{  0,  0,  -5,  22,  0 },  0.55,  "iron",         3,        0.44 }, // DEF > 40
        { "Wicked",    new double[]{ 14,  6,   0,   0,  0 },  0.50,  "dark",        -1,        0.0  },

        // Uncommon
        { "Ancient",   new double[]{  8,  8,   5,   0,  0 },  0.20,  "relic",       -1,        0.0  },
        { "Arcane",    new double[]{  0, 10,   0,   0, 25 },  0.30,  "arcane",       4,        0.48 }, // MP > 100
        { "Void",      new double[]{  5, 15,   0,   0,  0 },  0.30,  "dark",         1,        0.43 }, // CRIT > 30
        { "Frost",     new double[]{  0,  8,  12,   5,  0 },  0.35,  "frost",        2,        0.40 }, // SPD > 8
        { "Storm",     new double[]{  5, 10,  15,   0,  0 },  0.30,  "wind",         2,        0.50 }, // SPD > 11
        { "Venomous",  new double[]{  5, 12,   8,   0,  0 },  0.35,  "poison",       1,        0.35 }, // CRIT > 24
        { "Runic",     new double[]{  0, 12,   0,   0, 18 },  0.30,  "arcane",       4,        0.40 }, // MP > 84
        { "Phantom",   new double[]{  0,  8,  18,   0,  0 },  0.35,  "shadow",       2,        0.60 }, // SPD > 13
        { "Radiant",   new double[]{  0,  0,   0,  18, 15 },  0.25,  "holy",         3,        0.44 }, // DEF > 40
        { "Ethereal",  new double[]{  0,  5,  12,   0, 20 },  0.25,  "arcane",       4,        0.45 }, // MP > 94

        // Rare
        { "Savage",    new double[]{ 25,  0,  -5,  -5,  0 },  0.15,  "brutal",       0,        0.77 }, // ATK > 60
        { "Cursed",    new double[]{ 20,  0,   0, -10,  0 },  0.10,  "dark",        -1,        0.0  },
        { "Obsidian",  new double[]{ 20,  5,   0,   8,  0 },  0.12,  "heavy",        0,        0.60 }, // ATK > 47
        { "Soulbound", new double[]{ 10, 18,   0,   0, 10 },  0.10,  "relic",        1,        0.55 }, // CRIT > 38
        { "Bloodforged",new double[]{ 30, 10, -10,  0,  0 },  0.08,  "brutal",       0,        0.70 }, // ATK > 54

                // Legendary
        { "Divine",      new double[]{ 20, 15,  10,  15, 20 },  0.04,  "holy",    -1,   0.0  },
        { "Abyssal",     new double[]{ 35, 20,   0, -10,  0 },  0.03,  "dark",    -1,   0.0  },
        { "Celestial",   new double[]{  0, 25,  20,   0, 30 },  0.04,  "arcane",  -1,   0.0  },
        { "Primordial",  new double[]{ 30,  0,   0,  25,  0 },  0.03,  "relic",    3,   0.50 },

        // Mythic
        { "Godforged",   new double[]{ 60, 20, -10,  10,  0 },  0.01,  "brutal",  -1,   0.0  },
        { "Voidborn",    new double[]{  0, 55,  15, -20,  0 },  0.008, "shadow",  -1,   0.0  },
        { "Eternal",     new double[]{ 25, 25,  15,  20, 25 },  0.005, "relic",   -1,   0.0  },
    };

    static Object[][] WeaponSuffixPool = {
        // label              statMods [ATK,CRIT,SPD,DEF,MP]    weight    tag       minStatReq  threshold
        // Common
        { "of Ruin",        new double[]{ 20,  0,   0,   0,  0 },   0.45,  "chaos",       -1,        0.0  },
        { "of Swiftness",   new double[]{  0,  0,  25,   0,  0 },   0.50,  "wind",         2,        0.50 }, // SPD > 11
        { "of the Fallen",  new double[]{  0, 15,   0,   8,  0 },   0.45,  "dark",        -1,        0.0  },
        { "of Valor",       new double[]{  8,  0,   0,  14,  0 },   0.50,  "noble",       -1,        0.0  },
        { "of the Hunt",    new double[]{ 12,  0,  10,   0,  0 },   0.50,  "wind",        -1,        0.0  },
        { "of Fury",        new double[]{ 15,  5,   0,  -5,  0 },   0.45,  "brutal",      -1,        0.0  },
        { "of the North",   new double[]{  0,  0,   8,  18,  0 },   0.45,  "frost",        3,        0.40 }, // DEF > 36

        // Uncommon
        { "of Shadows",     new double[]{  5, 20,  10,   0,  0 },   0.20,  "shadow",       1,        0.50 }, // CRIT > 35
        { "of the Titan",   new double[]{ 15,  0,   0,  20,  0 },   0.15,  "iron",         3,        0.50 }, // DEF > 45
        { "of Arcana",      new double[]{  0,  5,   0,   0, 35 },   0.20,  "arcane",       4,        0.50 }, // MP > 105
        { "of the Storm",   new double[]{  8, 12,  15,   0,  0 },   0.20,  "wind",         2,        0.55 }, // SPD > 12
        { "of the Void",    new double[]{ 10, 18,   0,   0,  0 },   0.20,  "dark",         1,        0.43 }, // CRIT > 30
        { "of the Sage",    new double[]{  0,  8,   0,   0, 30 },   0.25,  "arcane",       4,        0.45 }, // MP > 94
        { "of the Ancients",new double[]{  8,  8,   5,   8,  5 },   0.15,  "relic",       -1,        0.0  },
        { "of Vengeance",   new double[]{ 14, 14,   0,   0,  0 },   0.20,  "chaos",        1,        0.40 }, // CRIT > 28
        { "of the Phoenix", new double[]{ 12,  0,   0,   0, 20 },   0.20,  "fire",         4,        0.35 }, // MP > 73
        { "of the Wilds",   new double[]{  8,  5,  14,   0,  0 },   0.30,  "wind",         2,        0.40 }, // SPD > 8

        // Rare
        { "of Precision",   new double[]{  0, 30,   5,   0,  0 },   0.12,  "precise",      1,        0.50 }, // CRIT > 35
        { "of Carnage",     new double[]{ 35,  0, -10,   0,  0 },   0.08,  "brutal",       0,        0.50 }, // ATK > 39
        { "of Malice",      new double[]{  5, 35,   5,   0,  0 },   0.08,  "shadow",       1,        0.65 }, // CRIT > 45
        { "of the Dragon",  new double[]{ 30,  0,   0,  10,  0 },   0.07,  "fire",         0,        0.60 }, // ATK > 47
        { "of Oblivion",    new double[]{ 20, 20,   0, -10,  0 },   0.05,  "dark",         1,        0.70 }, // CRIT > 49

        // Legendary
        { "of the Seraph",   new double[]{ 15, 20,  10,  15, 25 },  0.04,  "holy",    -1,   0.0  },
        { "of Ruination",    new double[]{ 40, 10, -10,   0,  0 },  0.03,  "chaos",   -1,   0.0  },
        { "of the Eclipse",  new double[]{ 10, 30,  15,   0, 15 },  0.04,  "shadow",  -1,   0.0  },
        { "of Eternity",     new double[]{ 20,  0,   0,  30, 20 },  0.03,  "relic",   -1,   0.0  },

        // Mythic
        { "of Armageddon",   new double[]{ 70,  0, -15,   0,  0 },  0.01,  "brutal",  -1,   0.0  },
        { "of the Cosmos",   new double[]{ 20, 30,  20,  20, 30 },  0.008, "arcane",  -1,   0.0  },
        { "of Oblivion's End",new double[]{ 30, 40,   0, -15,  0 },  0.005, "dark",   -1,   0.0  },
    };

    static int minimum_amount_of_weapons = 5;
    static int maximum_amount_of_weapons = 10;
    static Object[] CreatedWeapons = {};

    static final double[][] TIER_WEIGHT_BANDS = {
        { 0.40, 1.00 },  // 1 - Common
        { 0.15, 0.39 },  // 2 - Uncommon
        { 0.05, 0.14 },  // 3 - Rare
        { 0.02, 0.04 },  // 4 - Legendary
        { 0.00, 0.01 },  // 5 - Mythic
    };

    static Object[] DeriveWeaponAffinities(double[] normalized) {
        // ── ARCHETYPE MATRIX ─────────────────────────────────────────────
        // Each row is one affinity's ideal stat profile (normalized 0–1)
        // HP, MP, SPD, ATK, DEF, CRIT, CRITMUL
        // [0] [1] [2]  [3]  [4]   [5]   [6]
        double[][] archetypes = {
            // HP    MP    SPD   ATK   DEF   CRIT  CRITMUL
            { 0.3,  0.1,  0.8,  0.6,  0.2,  0.8,  0.7 },  // 0  Blade
            { 0.2,  0.9,  0.3,  0.8,  0.1,  0.3,  0.4 },  // 1  Staff
            { 0.1,  1.0,  0.2,  0.6,  0.1,  0.4,  0.5 },  // 2  Grimoire
            { 0.5,  0.7,  0.3,  0.3,  0.4,  0.2,  0.3 },  // 3  Totem 
            { 0.1,  1.0,  0.1,  0.3,  0.1,  0.5,  0.6 },  // 4  Orb 
            { 0.4,  0.2,  0.9,  0.6,  0.1,  0.7,  0.5 },  // 5  Bow
            { 0.3,  0.1,  0.7,  0.7,  0.1,  0.6,  0.6 },  // 6  Crossbow
            { 0.7,  0.1,  0.5,  1.0,  0.3,  0.4,  0.9 },  // 7  Fist
            { 0.9,  0.2,  0.2,  0.4,  1.0,  0.1,  0.2 },  // 8  Shield 
            { 0.2,  0.3,  0.9,  0.5,  0.1,  0.9,  0.8 },  // 9  Dagger 
            { 0.3,  0.5,  0.4,  0.8,  0.1,  0.5,  1.0 },  // 10 Scythe 
            { 0.5,  0.1,  0.6,  0.7,  0.4,  0.4,  0.5 },  // 11 Spear
        };

        double[] weights = { 0.8, 0.9, 0.8, 1.0, 0.8, 1.0, 1.0 };
        double[] distances = new double[archetypes.length];

        for (int a = 0; a < archetypes.length; a++) {
            double sum = 0.0;
            for (int s = 0; s < normalized.length; s++) {
                double diff = normalized[s] - archetypes[a][s];
                sum += weights[s] * (diff * diff);
            }
            distances[a] = Math.sqrt(sum);
        }

        int primary_affinity  = 0;
        int secondary_affinity = 1;

        for (int i = 1; i < distances.length; i++) {
            if (distances[i] < distances[primary_affinity]) {
                secondary_affinity = primary_affinity;
                primary_affinity  = i;
            } else if (distances[i] < distances[secondary_affinity]) {
                secondary_affinity = i;
            }
        }

        return new Object[] {
            primary_affinity,
            secondary_affinity,
            distances,
            WeaponAffinities[primary_affinity],
            WeaponAffinities[secondary_affinity]
        };
    }

    static int[] DeriveForbiddenAffinities(double[] normalized, int primary_affinity, int secondary_affinity) {
        // Blade=0, Staff=1, Grimoire=2, Totem=3, Orb=4, Bow=5, Crossbow=6,
        // Fist=7, Shield=8, Dagger=9, Scythe=10, Spear=11
        int[][] ForbiddenWeaponsMapping = {
            { 3, 4, 2, 1 },     // 0  Blade     → forbids Totem, Orb, Grimoire, Staff
            { 10, 6, 8, 7 },    // 1  Staff     → forbids Scythe, Crossbow, Shield, Fist
            { 10, 11, 8, 7 },   // 2  Grimoire  → forbids Scythe, Spear, Shield, Fist
            { 10, 7, 6 },       // 3  Totem     → forbids Scythe, Fist, Crossbow
            { 7, 10, 11 },      // 4  Orb       → forbids Fist, Scythe, Spear
            { 8, 7 },           // 5  Bow       → forbids Shield, Fist
            { 8, 7, 3 },        // 6  Crossbow  → forbids Shield, Fist, Totem
            { 4, 3, 2, 1, 5 },        // 7  Fist      → forbids Orb, Totem, Grimoire
            { 9, 4, 2, 1, 5, 6 },        // 8  Shield    → forbids Dagger, Orb, Grimoire
            { 8, 11, 10 },      // 9  Dagger    → forbids Shield, Spear, Scythe
            { 8, 3, 4 },        // 10 Scythe    → forbids Shield, Totem, Orb
            { 2, 4, 3 },        // 11 Spear     → forbids Grimoire, Orb, Totem
        };

        int[] Primary   = ForbiddenWeaponsMapping[primary_affinity];
        int[] Secondary = ForbiddenWeaponsMapping[secondary_affinity];
        List<Integer> forbidden = new ArrayList<>();

        for (int affinity : Primary) {
            if (!forbidden.contains(affinity)) {
                forbidden.add(affinity);
            }
        }

        for (int affinity : Secondary) {
            if (!forbidden.contains(affinity)) {
                forbidden.add(affinity);
            }
        }

        forbidden.removeIf(f -> f == primary_affinity || f == secondary_affinity);
        return forbidden.stream().mapToInt(Integer::intValue).toArray();
    }

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
 
    static Object[] GenerateWeaponBaseData(Object[] Class) {

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

        Object[] DerivedAffinities = DeriveWeaponAffinities(normalized);
        Object[] Tags = DeriveArmorTags(normalized);
        int[] ForbiddenArchetypes = DeriveForbiddenAffinities(normalized, (int) DerivedAffinities[0], (int) DerivedAffinities[1]);

        //assemble { normalized, primaryStat, affinity, forbidden, tags };

        return new Object[] {DerivedAffinities, Tags, ForbiddenArchetypes, normalized};
    }

    static int[] WeaponsStatReqMap = { 3, 5, 2, 4, 1 };
    static Object[] FilterWeaponStats(int[] forbidden_affinities, double[] normalized_stats, int targetTier) {
        List<Object[]> FilteredWeapons  = new ArrayList<>();
        List<Object[]> ScoredPrefixes   = new ArrayList<>();
        List<Object[]> ScoredSuffixes   = new ArrayList<>();

        double tierMin = TIER_WEIGHT_BANDS[targetTier - 1][0];
        double tierMax = TIER_WEIGHT_BANDS[targetTier - 1][1];

        outer:
        for (Object[] weapon : BaseWeaponPool) {
            int weaponAffinity = (int) weapon[3];
            for (int forbidden_affinity : forbidden_affinities) {
                if (weaponAffinity == forbidden_affinity) continue outer;
            }
            FilteredWeapons.add(weapon);
        }

        for (Object[] prefix : WeaponPrefixPool) {
            double weight          = (double) prefix[2];
            int    minStatReq      = (int)    prefix[4];
            double minStatThreshold = (double) prefix[5];

            if (weight < tierMin || weight > tierMax) continue;
            if (minStatReq != -1 && normalized_stats[WeaponsStatReqMap[minStatReq]] < minStatThreshold) continue;

            ScoredPrefixes.add(prefix);
        }

        for (Object[] suffix : WeaponSuffixPool) {
            double weight           = (double) suffix[2];
            int    minStatReq       = (int)    suffix[4];
            double minStatThreshold = (double) suffix[5];

            if (weight < tierMin || weight > tierMax) continue;
            if (minStatReq != -1 && normalized_stats[WeaponsStatReqMap[minStatReq]] < minStatThreshold) continue;

            ScoredSuffixes.add(suffix);
        }

        // Fallback: if no prefixes survived the stat-req gate for this tier,
        // retry keeping the tier band but ignoring stat requirements.
        // This prevents an empty pool crash when a class (e.g. Warrior) fails
        // every gated prefix at a given rarity tier.
        if (ScoredPrefixes.isEmpty()) {
            for (Object[] prefix : WeaponPrefixPool) {
                double weight = (double) prefix[2];
                if (weight < tierMin || weight > tierMax) continue;
                ScoredPrefixes.add(prefix);
            }
        }

        // Fallback: same logic for suffixes.
        // e.g. Warrior at Tier 3 (Rare) fails every suffix's ATK/CRIT gate.
        if (ScoredSuffixes.isEmpty()) {
            for (Object[] suffix : WeaponSuffixPool) {
                double weight = (double) suffix[2];
                if (weight < tierMin || weight > tierMax) continue;
                ScoredSuffixes.add(suffix);
            }
        }

        return new Object[] { FilteredWeapons, ScoredPrefixes, ScoredSuffixes };
    }

    static Object[] WeightWeaponStats(double[] normalizedClassStats, List<Object[]> scored_bases, List<Object[]> scored_prefixes, List<Object[]> scored_suffixes) {

        double[] classMods = {
            normalizedClassStats[3],  // [0] ATK
            normalizedClassStats[5],  // [1] CRIT
            normalizedClassStats[2],  // [2] SPD
            normalizedClassStats[4],  // [3] DEF
            normalizedClassStats[1],  // [4] MP
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

        // [0]=ATK  [1]=CRIT  [2]=SPD  [3]=DEF  [4]=MP
        double[][] ScoredBases = new double[scored_bases.size()][2];
        for (int i = 0; i < scored_bases.size(); i++) {
            int statBias = (int) scored_bases.get(i)[5];
            double score = 1.0 * (1 + classMods[statBias]);

            ScoredBases[i][0] = i;
            ScoredBases[i][1] = score;
        }

        return new Object[] { ScoredPrefixes, ScoredBases, ScoredSuffixes };
    }

    static Object[] WeaponWeightedPick(double[][] scored, List<Object[]> pool) {
        double total = 0;
        for (double[] entry : scored) total += entry[1];
    
        double roll    = Math.random() * total;
        double running = 0;
    
        for (double[] entry : scored) {
            running += entry[1];
            if (running > roll) {
                return pool.get((int) entry[0]);
            }
        }
    
        // Floating-point edge case: running never exceeded roll.
        // Guard against an empty pool (should never happen after FilterWeaponStats
        // fallback, but defensive just in case).
        if (pool.isEmpty()) {
            throw new IllegalStateException(
                "WeaponWeightedPick: pool is empty — check FilterWeaponStats fallback logic");
        }
        return pool.get(0);
    }

    static Object[] RollWeapon(Object[] weighted_stats, List<Object[]> filtered_prefixes, List<Object[]> filtered_bases, List<Object[]> filtered_suffixes) {
        double[][] ScoredPrefixes = (double[][]) weighted_stats[0];
        double[][] ScoredBases    = (double[][]) weighted_stats[1];
        double[][] ScoredSuffixes = (double[][]) weighted_stats[2];

        System.out.println("");

        Object[] SelectedPrefix = WeaponWeightedPick(ScoredPrefixes, filtered_prefixes);
        Object[] SelectedBase   = WeaponWeightedPick(ScoredBases,    filtered_bases);
        Object[] SelectedSuffix = WeaponWeightedPick(ScoredSuffixes, filtered_suffixes);

        double MinAttack    = (double) SelectedBase[1];
        double MaxAttack    = (double) SelectedBase[2];
        double RolledAttack = MinAttack + Math.random() * (MaxAttack - MinAttack);

        return new Object[] { SelectedPrefix, SelectedBase, SelectedSuffix, RolledAttack };
    }   

    static Object[] ComposeWeapons(int[] ForbiddenAffinities, double[] NormalizedClassStats, int targetTier) {
        Object[] FilteredData = FilterWeaponStats(ForbiddenAffinities, NormalizedClassStats, targetTier);

        List<Object[]> filteredWeapons  = (List<Object[]>) FilteredData[0];
        List<Object[]> filteredPrefixes = (List<Object[]>) FilteredData[1];
        List<Object[]> filteredSuffixes = (List<Object[]>) FilteredData[2];

        Object[] WeightedData = WeightWeaponStats(
            NormalizedClassStats,
            filteredWeapons,
            filteredPrefixes,
            filteredSuffixes
        );

        Object[] RolledData = RollWeapon(
            WeightedData,
            filteredPrefixes,
            filteredWeapons,
            filteredSuffixes
        );

        return RolledData;
    }

    static int PriceWeapon(Object[] rolledWeapon, int primaryAffinity, int secondaryAffinity) {

        Object[] prefix       = (Object[]) rolledWeapon[0];
        Object[] base         = (Object[]) rolledWeapon[1];
        Object[] suffix       = (Object[]) rolledWeapon[2];
        double   rolledAttack = (double)   rolledWeapon[3];

        double[] prefixMods = (double[]) prefix[1];
        double   prefixWeight = (double) prefix[2];

        double[] suffixMods = (double[]) suffix[1];
        double   suffixWeight = (double) suffix[2];

        int weaponAffinity = (int) base[3];

        double basePrice = rolledAttack * 3.5;  
        double rarityScore = ((1.0 - prefixWeight) + (1.0 - suffixWeight)) / 2.0;
        double rarityMult  = 1.0 + (rarityScore * 0.45);  
        double[] statGoldValues = { 1.2, 1.8, 1.0, 1.1, 0.8 };
        double statBonus = 0;

        for (int s = 0; s < prefixMods.length; s++) {
            statBonus += prefixMods[s] * statGoldValues[s];
            statBonus += suffixMods[s] * statGoldValues[s];
        }

        statBonus = Math.max(-20.0, Math.min(statBonus, 40.0));

        double demandMult = 1.0;
        if (weaponAffinity == primaryAffinity)        demandMult = 1.10;
        else if (weaponAffinity == secondaryAffinity) demandMult = 1.05;

        double rawPrice = (basePrice + statBonus) * rarityMult * demandMult;
        rawPrice = Math.max(15.0, rawPrice); // price floor

        return (int) Math.round(rawPrice / 5.0) * 5;
    }

    static void GenerateWeaponsForClass(Object Class) {
        Object[] BaseData       = GenerateWeaponBaseData((Object[]) Class);
        Object[] AffinityData   = (Object[]) BaseData[0];

        int PrimaryAffinity   = (int) AffinityData[0];
        int SecondaryAffinity = (int) AffinityData[1];

        int extras           = (int) (Math.random() * (maximum_amount_of_weapons - minimum_amount_of_weapons + 1));
        int weapons_to_generate = 5 + extras;
        CreatedWeapons       = new Object[weapons_to_generate];

        for (int tier = 1; tier <= 5; tier++) {
            Object[] RolledData = ComposeWeapons((int[]) BaseData[2], (double[]) BaseData[3], tier);
            int Price = PriceWeapon(RolledData, PrimaryAffinity, SecondaryAffinity);
            CreatedWeapons[tier - 1] = new Object[] { RolledData, Price };
        }

        for (int i = 5; i < weapons_to_generate; i++) {
            int randomTier = (int) (Math.random() * 5) + 1;
            Object[] RolledData = ComposeWeapons((int[]) BaseData[2], (double[]) BaseData[3], randomTier);
            int Price = PriceWeapon(RolledData, PrimaryAffinity, SecondaryAffinity);
            CreatedWeapons[i] = new Object[] { RolledData, Price };
        }
    }

     static void OpenWeaponsShop() {
        String[] statLabels = { "ATK", "CRIT", "SPD", "DEF", "MP" };
 
        PrintDivider(true);
        System.out.println("  WEAPON SHOP  (" + CreatedWeapons.length + " items available)");
        PrintDivider(false);
 
        for (int i = 0; i < CreatedWeapons.length; i++) {
            Object[] entry      = (Object[]) CreatedWeapons[i];
            Object[] rolledData = (Object[]) entry[0];
            int      price      = (int)      entry[1];
 
            Object[] prefix = (Object[]) rolledData[0];
            Object[] base   = (Object[]) rolledData[1];
            Object[] suffix = (Object[]) rolledData[2];
            double   atk    = (double)   rolledData[3];
 
            String prefixLabel = (String) prefix[0];
            String baseName    = (String) base[0];
            String suffixLabel = (String) suffix[0];
            String weaponName  = prefixLabel + " " + baseName + " " + suffixLabel;
 
            double[] prefixMods = (double[]) prefix[1];
            double[] suffixMods = (double[]) suffix[1];
 
            System.out.printf("%n  [%d] %s%n", i + 1, weaponName);
            PrintDivider(false);
            System.out.printf("  %-22s %s%n", "Type:", ((String) base[4]).substring(0, 1).toUpperCase() + ((String) base[4]).substring(1));
            System.out.printf("  %-22s %.1f%n", "Attack Damage:", atk);
 
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

        do {
            PrintDivider(false);
            System.out.print("What would you like to buy?: ");
            player_string_reply = Scanner.nextLine();

            if (player_string_reply.equalsIgnoreCase("exit")) break;
            player_numerical_reply = Integer.parseInt(player_string_reply);

            while (player_numerical_reply <= 0 || player_numerical_reply > CreatedWeapons.length) {
                System.out.println("Invalid Reply.");
                System.out.print("What would you like to buy?: ");
                player_string_reply = Scanner.nextLine();

                if (player_string_reply.equalsIgnoreCase("exit")) {
                    break;
                }

                player_numerical_reply = Integer.parseInt(player_string_reply);
            }

            Object[] SelectedWeapon = (Object[]) CreatedWeapons[player_numerical_reply - 1];
            double WeaponCost = (double)(int) SelectedWeapon[1];
            double PlayerGold = (double) PlayerInformation[11];

            Object[] PlayerEquippedWeapon = (Object[]) PlayerInformation[12];
            Object[] WeaponData = (Object[]) SelectedWeapon[0];
            Object[] prefix = (Object[]) WeaponData[0];
            Object[] base   = (Object[]) WeaponData[1];
            Object[] suffix = (Object[]) WeaponData[2];

            if (PlayerGold < WeaponCost) {
                PrintDivider(false);
                System.out.println("You don't have enough gold. You need " + (WeaponCost - PlayerGold) + " more gold to purchase this weapon.");
            } else {
                PrintDivider(false);
                System.out.println("You equipped: " + prefix[0] + " " + base[0] + " " + suffix[0]);

                if (PlayerEquippedWeapon == null) {
                    PlayerInformation[12] = SelectedWeapon;
                    ApplyWeaponStats(SelectedWeapon, +1.0);
                } else {
                    ApplyWeaponStats(PlayerEquippedWeapon, -1.0);

                    Object[] CurrentInventory = (Object[]) PlayerInformation[14];
                    Object[] NewInventory = new Object[CurrentInventory.length + 1];
                    System.arraycopy(CurrentInventory, 0, NewInventory, 0, CurrentInventory.length);
                    NewInventory[CurrentInventory.length] = PlayerEquippedWeapon;

                    PlayerInformation[14] = NewInventory;
                    PlayerInformation[12] = SelectedWeapon;
                    ApplyWeaponStats(SelectedWeapon, +1.0);
                }

                PlayerInformation[11] = PlayerGold - WeaponCost;
                break;
            }

        } while (!player_string_reply.equalsIgnoreCase("exit"));

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
        System.out.println("\n[SYSTEM]: Generating base weapon classes");
        GenerateWeaponsForClass(classes[0]);
        System.out.println("[SYSTEM]: Finished Creating Weapons");
        OpenWeaponsShop();
    }
}