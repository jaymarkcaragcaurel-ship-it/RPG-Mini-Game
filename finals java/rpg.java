import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class rpg {
    static Scanner Scanner = new Scanner(System.in);
    
    /************************************************************/
    /*                                                          */
    /*                         PLAYER                           */
    /*                                                          */
    /************************************************************/

    static String player_string_reply = null;
    static int player_numerical_reply = 0;

    static boolean player_in_dungeon = false;
    static boolean game_over = false;

    static int[] STAT_MOD_TO_PLAYER_INDEX = { 5, 7, 4, 6, 3, 2 };

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
            0,       // Starting Gold [11]

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

    static Object[] PlayerLevels = {
        // index = level, value = EXP required to reach next level
        // [Lv.1  - Lv.5 ]
        100,    300,    600,    1_000,  1_500,
        // [Lv.6  - Lv.10]
        2_200,  3_100,  4_200,  5_600,  7_400,
        // [Lv.11 - Lv.15]
        9_600,  12_400, 15_800, 20_000, 25_200,
        // [Lv.16 - Lv.20]
        31_500, 39_200, 48_400, 59_500, 73_000,
        // [Lv.21 - Lv.25]
        89_000, 108_000, 130_000, 156_000, 187_000,
        // [Lv.26 - Lv.30]
        223_000, 265_000, 314_000, 371_000, 0
    };

    static int PlayerChooseClass(){
        PrintDivider(false);
        System.out.println("[SYSTEM]: AVAILABLE CHARACTER CLASSES");
        PrintDivider(false);
        System.out.println("");
        boolean valid_class = false;
        int class_index = 0;

        for (int playerclass = 0; playerclass < classes.length; playerclass++) {
            System.out.println(("[" + (char)('a' + playerclass) + "] " + classes[playerclass][0]).toUpperCase() + " -" + classes[playerclass][1]);
        }
    
        PrintDivider(true);
        while (valid_class == false) {
            System.out.print("\n[SYSTEM]: What's your character class? | Type Here: ");
            player_string_reply = Scanner.nextLine();

            class_index = player_string_reply.charAt(0) - 'a';
            if (class_index >= 0 && class_index < classes.length) {
                switch (player_string_reply.toLowerCase()) {
                    case "a": PlayerInformation[1] = "Warrior"; valid_class = true; break;
                    case "b": PlayerInformation[1] = "Mage"; valid_class = true; break;
                    case "c": PlayerInformation[1] = "Paladin"; valid_class = true; break;
                    case "d": PlayerInformation[1] = "Archer"; valid_class = true; break;
                    case "e": PlayerInformation[1] = "Assassin"; valid_class = true; break;
                    default: System.out.println("Invalid choice, please try again."); break;
                }

                if (valid_class) {
                    for (int class_data = 2; class_data < classes[class_index].length; class_data++) {
                        PlayerInformation[class_data] = classes[class_index][class_data];
                    }
                }
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }
        return class_index;
    }

    static void UpdatePlayerStatus(int index, double amount) {
        Object[] Report = (Object[]) PlayerInformation[14];

        switch (index) {

            case 2: // HP
                PlayerInformation[2] = (double) PlayerInformation[2] + amount;
                System.out.println("[SYSTEM]: HP updated to " + PlayerInformation[2]);

                double currentHP = (double) PlayerInformation[2];

                if (amount < 0) {
                    Report[10] = (double) Report[10] + Math.abs(amount);
                    if (currentHP > 0) {
                        if ((double) Report[28] == 0.0 || currentHP < (double) Report[28]) {
                            Report[28] = currentHP;
                        }
                    }

                } else if (amount > 0) {
                    Report[11] = (double) Report[11] + amount;
                }
                break;

            case 3: // MP
                PlayerInformation[3] = (double) PlayerInformation[3] + amount;
                System.out.println("[SYSTEM]: MP updated to " + PlayerInformation[3]);
                break;

            case 4: // Speed
                PlayerInformation[4] = (double) PlayerInformation[4] + amount;
                System.out.println("[SYSTEM]: Speed updated to " + PlayerInformation[4]);
                break;

            case 5: // Attack Damage
                PlayerInformation[5] = (double) PlayerInformation[5] + amount;
                System.out.println("[SYSTEM]: AttackDamage updated to " + PlayerInformation[5]);
                break;

            case 6: // Defense
                PlayerInformation[6] = (double) PlayerInformation[6] + amount;
                System.out.println("[SYSTEM]: Defense updated to " + PlayerInformation[6]);
                break;

            case 7: // Critical Hit Chance
                PlayerInformation[7] = (double) PlayerInformation[7] + amount;
                System.out.println("[SYSTEM]: CritHitChance updated to " + PlayerInformation[7]);
                break;

            case 8: // Critical Hit Multiplier
                PlayerInformation[8] = (double) PlayerInformation[8] + amount;
                System.out.println("[SYSTEM]: CritMultiplier updated to " + PlayerInformation[8]);
                break;

            case 9: // EXP
                int CurrentLevel = (int) PlayerInformation[10];

                if (CurrentLevel < PlayerLevels.length - 1) {
                    int NextLevel = (int) PlayerLevels[CurrentLevel - 1];

                    PlayerInformation[9] = (int) PlayerInformation[9] + (int) amount;
                    System.out.println("[SYSTEM]: EXP updated to " + PlayerInformation[9]);

                    if ((int) PlayerInformation[9] >= NextLevel) {
                        UpdatePlayerStatus(10, 1);
                    }
                }
                break;

            case 10: // Player Level
                PlayerInformation[10] = (int) PlayerInformation[10] + (int) amount;
                System.out.println("[SYSTEM]: Player Leveled up to " + PlayerInformation[10]);
                PrintDivider(false);

                int newLevel = (int) PlayerInformation[10];

                Report[1] = newLevel;

                if (Report[19] == null) {
                    Report[19] = new ArrayList<Integer>();
                }
                ((ArrayList<Integer>) Report[19]).add(newLevel);
                break;

            case 11: // Gold
                int goldChange = (int) amount;
                PlayerInformation[11] = (int) PlayerInformation[11] + goldChange;
                System.out.println("[SYSTEM]: Gold updated to " + PlayerInformation[11]);

                if (goldChange < 0) {
                    Report[14] = (double) Report[14] + Math.abs(goldChange);

                } else if (goldChange > 0) {
                    Report[15] = (double) Report[15] + goldChange;                
                }
                break;

            default:
                System.out.println("[SYSTEM]: Invalid index.");
                break;
        }
    }

    static void ViewPlayerProfile() {
        PrintDivider(true);
        System.out.println("                   PLAYER PROFILE");
        PrintDivider(false);

        // Basic Info
        System.out.println("  Name       : " + PlayerInformation[0]);
        System.out.println("  Class      : " + PlayerInformation[1]);
        System.out.println("  Level      : " + PlayerInformation[10]);
        System.out.println("  EXP        : " + PlayerInformation[9]);
        System.out.println("  Gold       : " + PlayerInformation[11]);

        PrintDivider(false);
        System.out.println("                     STATS");
        PrintDivider(false);

        System.out.println("  HP         : " + PlayerInformation[2]);
        System.out.println("  MP         : " + PlayerInformation[3]);
        System.out.println("  Speed      : " + PlayerInformation[4]);
        System.out.println("  Attack     : " + PlayerInformation[5]);
        System.out.println("  Defense    : " + PlayerInformation[6]);
        System.out.println("  Crit Chance: " + PlayerInformation[7] + "%");
        System.out.println("  Crit Multi : " + PlayerInformation[8] + "x");

        PrintDivider(false);
        System.out.println("                   EQUIPMENT");
        PrintDivider(false);

        // Weapon
        if (PlayerInformation[12] != null) {
            System.out.println("  Weapon     : " + PlayerInformation[12]);
        } else {
            System.out.println("  Weapon     : None");
        }

        // Armor
        Object[] Armor = (Object[]) PlayerInformation[13];
        System.out.println("  Head Armor : " + (Armor[0] != null ? Armor[0] : "None"));
        System.out.println("  Body Armor : " + (Armor[1] != null ? Armor[1] : "None"));

        PrintDivider(false);
    }
    
    static void GenerateGameReport() {
        System.out.println("══════════════════════════════════════════════════");
        System.out.println("\nYOUR PROGRESS");
        System.out.println("\n══════════════════════════════════════════════════");

        Object[] RaidAnalyticsReport = (Object[]) PlayerInformation[14];

        RaidAnalyticsReport[1]  = PlayerInformation[10];
        RaidAnalyticsReport[3]  = PlayerInformation[2];
        RaidAnalyticsReport[4]  = PlayerInformation[3]; 
        RaidAnalyticsReport[5]  = PlayerInformation[11];

        String PlayerName = (String) PlayerInformation[0];
        String PlayerClass = (String) PlayerInformation[1];

        int PlayerLevel = (int) RaidAnalyticsReport[1];
        double HP = (double) RaidAnalyticsReport[3];
        double MP = (double) RaidAnalyticsReport[4];
        int Gold = (int) RaidAnalyticsReport[5];

        int MonstersDefeated = (int) RaidAnalyticsReport[6];
        int BossesDefeated = (int) RaidAnalyticsReport[7];
        int TotalPlaytimeTurns = (int) RaidAnalyticsReport[8];

        double TotalDamageDealt = (double) RaidAnalyticsReport[9];
        double TotalDamageReceived = (double) RaidAnalyticsReport[10];
        double TotalHealingRestored = (double) RaidAnalyticsReport[11];
        double HighestHitDamage = (double) RaidAnalyticsReport[12];
        double AverageDamagePerBattle = (double) RaidAnalyticsReport[13];

        double TotalGoldSpent = (double) RaidAnalyticsReport[14];
        double TotalGoldEarned = (double) RaidAnalyticsReport[15];
        double InventoryValue = (double) RaidAnalyticsReport[16];
        String ProfitorLoss = (String) RaidAnalyticsReport[17];

        int FloorsCompleted = (int) RaidAnalyticsReport[18];
        int MilestonesReached = (RaidAnalyticsReport[19] == null)
                                ? 0
                                : ((java.util.ArrayList<?>) RaidAnalyticsReport[19]).size();

        System.out.println("");
        System.out.println("Player Name        : " + PlayerName);
        System.out.println("Player Class       : " + PlayerClass);
        System.out.println("Player Level       : " + PlayerLevel);
        System.out.println("Remaining HP       : " + HP);
        System.out.println("Remaining MP       : " + MP);
        System.out.println("Final Gold         : " + Gold);

        System.out.println("");
        System.out.println("Monsters Defeated  : " + MonstersDefeated);
        System.out.println("Bosses Defeated    : " + BossesDefeated);
        System.out.println("Total Turns Played : " + TotalPlaytimeTurns);

        System.out.println("");
        System.out.println("Total Damage Dealt     : " + TotalDamageDealt);
        System.out.println("Total Damage Received  : " + TotalDamageReceived);
        System.out.println("Total Healing Restored : " + TotalHealingRestored);
        System.out.println("Highest Single Hit     : " + HighestHitDamage);
        System.out.println("Avg Damage Per Battle  : " + AverageDamagePerBattle);

        System.out.println("");
        System.out.println("Total Gold Spent   : " + TotalGoldSpent);
        System.out.println("Total Gold Earned  : " + TotalGoldEarned);
        System.out.println("Inventory Value    : " + InventoryValue);
        System.out.println("Profit / Loss      : " + ProfitorLoss);

        System.out.println("");
        System.out.println("Floors Completed   : " + FloorsCompleted);
        System.out.println("Milestones Reached : " + MilestonesReached);
    }
    
    /************************************************************/
    /*                                                          */
    /*                        DUNGEON                           */
    /*                                                          */
    /************************************************************/

    static int minimum_floors = 2;
    static int maximum_floors = 5;

    static Object[] Dungeons = new Object[3];

    static String[] DungeonNames = {
        "Caverns of the Damned",
        "Ashveil Catacombs",
        "The Rotting Sanctum",
        "Thornspire Depths",
        "Tomb of the Forgotten King",
        "Shadowmere Abyss",
        "The Blighted Warrens",
        "Crypts of Zalthorath",
        "The Sunken Fortress",
        "Malachar's Domain",
        "Voidrift Dungeon",
        "The Festering Pit",
        "Dreadspire Keep",
        "Ruins of Nyxevara",
        "The Obsidian Labyrinth",
        "Gravehold Depths",
        "The Shattered Vault",
        "Bonechill Cavern",
        "Temple of the Cursed Flame",
        "The Abyssal Maw"
    };

    static void AdmitPlayerToDungeon(Object[] Dungeon){
        player_in_dungeon = true;
        int current_floor = 1;

        Object[] FloorData = (Object[]) Dungeon[current_floor];
        Object[] Report = (Object[]) PlayerInformation[14];
        Object[] monsters = (Object[]) FloorData[1];
        Object[] boss = (Object[]) FloorData[2];

        Object[] Monsters = new Object[monsters.length + 1];
        for (int i = 0; i < monsters.length; i++) {
            Monsters[i] = monsters[i];
        }
        Monsters[monsters.length] = boss;

        int monsters_defeated = 0;
        int monsters_left = Monsters.length;
        Object[] CurrentMonster = (Object[]) Monsters[monsters_defeated];

        do {
            PrintDivider(false);
            System.out.println("\n" + Dungeon[0] + " FLOOR " + current_floor);
            PrintDivider(true);

            String monster_order;
            if (monsters_defeated == Monsters.length - 1) {
                monster_order = "last";
            } else {
                switch (monsters_defeated) {
                    case 0:  monster_order = "1st"; break;
                    case 1:  monster_order = "2nd"; break;
                    case 2:  monster_order = "3rd"; break;
                    default: monster_order = (monsters_defeated + 1) + "th"; break;
                }
            }

            if (monsters_left == 1) {
                System.out.print("[SYSTEM]: The Dungeon Boss awaits. Enter? [Y/N]: ");
            } else if (monsters_left > 1) {
                System.out.print("[SYSTEM]: Would you like to battle the " + monster_order + " monster? [Y/N]: ");
            } else {
                System.out.println("\n[SYSTEM]: Dungeon Floor Cleared");
                PrintDivider(true);

                Report[18] = (int) Report[18] + 1;

                boolean resting = true;
                while (resting) {
                    System.out.println("  ★ FLOOR " + current_floor + " CLEARED — REST AREA");
                    PrintDivider(false);

                    System.out.println("  HP   : " + PlayerInformation[2]);
                    System.out.println("  MP   : " + PlayerInformation[3]);
                    System.out.println("  Gold : " + PlayerInformation[11]);
                    System.out.println("  EXP  : " + PlayerInformation[9]);
                    PrintDivider(false);

                    System.out.println("[1] - Move to Next Floor");
                    System.out.println("[2] - View Profile");
                    System.out.println("[3] - Open Shop");
                    System.out.println("[4] - Exit Dungeon");
                    System.out.print("What would you like to do? | ");
                    player_numerical_reply = Integer.parseInt(Scanner.nextLine());

                    switch (player_numerical_reply) {
                        case 1:
                            current_floor++;
                            if (current_floor >= Dungeon.length) {
                                System.out.println("\n[SYSTEM]: Congrats for clearing the dungeon!");
                                System.out.println("[SYSTEM]: Now exiting you out of the dungeon");
                                PrintDivider(false);
                                Report[22] = "Victory";
                                player_in_dungeon = false;
                            }
                            resting = false;
                            break;

                        case 2:
                            System.out.println("");
                            ViewPlayerProfile();
                            break;

                        case 3:
                            PrintDivider(false);
                            OpenShop();
                            break;

                        case 4:
                            System.out.println("\n[SYSTEM]: Exiting dungeon...");
                            PrintDivider(false);
                            player_in_dungeon = false;
                            resting = false;
                            break;

                        default:
                            System.out.println("[SYSTEM]: Invalid choice. Please enter 1-4.");
                            PrintDivider(false);
                            break;
                    }
                }

                if (!player_in_dungeon) break;

                FloorData = (Object[]) Dungeon[current_floor];
                monsters = (Object[]) FloorData[1];
                boss = (Object[]) FloorData[2];

                Monsters = new Object[monsters.length + 1];
                for (int i = 0; i < monsters.length; i++) {
                    Monsters[i] = monsters[i];
                }
                Monsters[monsters.length] = boss;

                monsters_defeated = 0;
                monsters_left = Monsters.length;
                CurrentMonster = (Object[]) Monsters[monsters_defeated];
                continue;
            }

            player_string_reply = Scanner.nextLine();
            PrintDivider(false);

            if (player_string_reply.equalsIgnoreCase("Y")) {

                boolean isFloorBossOrDungeonBoss = (monsters_defeated == Monsters.length - 1);
                boolean isDungeonBoss = (isFloorBossOrDungeonBoss
                        && (current_floor == Dungeon.length - 1)
                        && (CurrentMonster.length > 11 && (boolean) CurrentMonster[11]));

                String result = isDungeonBoss ? InitiateBossBattle(PlayerInformation, CurrentMonster) : InitiateRegularBattle(PlayerInformation, CurrentMonster);

                if (result.equals("escaped")) {
                    System.out.println("[SYSTEM]: You fled from " + CurrentMonster[0] + ".");
                    PrintDivider(false);
                    monsters_defeated++;
                    monsters_left--;
                    if (monsters_defeated < Monsters.length) {
                        CurrentMonster = (Object[]) Monsters[monsters_defeated];
                    }
                } else if (result.equals("victory")) {
                    Report[7] = (int) Report[7] + 1;
                    monsters_defeated++;
                    monsters_left--;
                    if (monsters_defeated < Monsters.length) {
                        CurrentMonster = (Object[]) Monsters[monsters_defeated];
                    }
                } else if (result.equals(PlayerInformation[0])) {
                    System.out.println("[SYSTEM]: You defeated " + CurrentMonster[0] + "!");
                    PrintDivider(false);
                    Report[6] = (int) Report[6] + 1;
                    UpdatePlayerStatus(9, (double)(int) CurrentMonster[5]);
                    UpdatePlayerStatus(11, (double)(int) CurrentMonster[6]);
                    monsters_defeated++;
                    monsters_left--;
                    if (monsters_defeated < Monsters.length) {
                        CurrentMonster = (Object[]) Monsters[monsters_defeated];
                    }
                } else {
                    System.out.println("\n[SYSTEM]: PLAYER DEFEATED. Game over.");
                    PrintDivider(false);
                    player_in_dungeon = false;
                    game_over = true;
                }
            }

        } while (player_in_dungeon == true);
    }

    static void printValues(Object... values) {
        for (Object value : values) {
            if (value instanceof Object[]) {
                printValues((Object[]) value); // recurse into nested arrays
            } else {
                System.out.println(value);
            }
        }
    }

    static void PrintDungeonFloor(Object[] dungeon) {
        for (int floor = 0; floor < dungeon.length; floor++) {
            Object[] floorData = (Object[]) dungeon[floor];
            Object[] monsters = (Object[]) floorData[1];

            System.out.println("\n=== FLOOR " + floorData[0] + " ===");
            System.out.println("Monsters: " + monsters.length);

            for (int m = 0; m < monsters.length; m++) {
                Object[] monster = (Object[]) monsters[m];
                System.out.println("\n  Monster " + (m + 1) + ": " + monster[0]);
                System.out.println("    HP:      " + monster[1]);
                System.out.println("    ATK:     " + monster[2]);
                System.out.println("    DEF:     " + monster[3]);
                System.out.println("    SPD:     " + monster[4]);
                System.out.println("    EXP:     " + monster[5]);
                System.out.println("    Gold:    " + monster[6]);
            }
        }
    }

    static Object[] CreateDungeonFloor(int floorNumber, boolean isLastFloor, int totalFloors) {
    
        Object[] monsters = new Object[(int)(Math.random() * 5) + 1];
        for (int i = 0; i < monsters.length; i++) {
            monsters[i] = CreateMonster();
        }
    
        Object[] boss = isLastFloor
            ? CreateDungeonBoss(totalFloors)
            : CreateFloorBoss();
    
        return new Object[] {
            floorNumber,
            monsters,
            boss,
            isLastFloor 
        };
    }

    static Object[] CreateDungeon() {
        int floorCount = (int)(Math.random() * (maximum_floors - minimum_floors + 1)) + minimum_floors;
        Object[] Dungeon = new Object[floorCount + 1];   // +1 for name at [0]
    
        String chosen_name = null;
        boolean is_name_used;
        do {
            int random_number = (int)(Math.random() * DungeonNames.length);
            chosen_name = DungeonNames[random_number];
            is_name_used = false;
            for (Object d : Dungeons) {
                if (d != null && chosen_name.equals(((Object[]) d)[0])) {
                    is_name_used = true;
                    break;
                }
            }
        } while (is_name_used);
    
        Dungeon[0] = chosen_name;
    
        for (int floor = 1; floor <= floorCount; floor++) {
            boolean isLastFloor = (floor == floorCount);
            Dungeon[floor] = CreateDungeonFloor(floor, isLastFloor, floorCount);
        }
    
        return Dungeon;
    }
   
    /************************************************************/
    /*                                                          */
    /*                       MONSTERS                           */
    /*                                                          */
    /************************************************************/

    static int monster_min_health = 30;
    static int monster_max_health = 120;
    static int monster_min_attack = 5;
    static int monster_max_attack = 25;
    static int monster_min_defense = 2;
    static int monster_max_defense = 15;
    static int monster_min_speed = 3;
    static int monster_max_speed = 12;
    static int monster_min_exp = 10;
    static int monster_max_exp = 50;
    static int monster_min_gold = 5;
    static int monster_max_gold = 30;

    static int boss_min_health = 150;
    static int boss_max_health = 750;
    static int boss_min_attack = 35;
    static int boss_max_attack = 90;
    static int boss_min_defense = 20;
    static int boss_max_defense = 55;
    static int boss_min_speed = 5;
    static int boss_max_speed = 18;
    static int boss_min_exp = 150;
    static int boss_max_exp = 500;
    static int boss_min_gold = 75;
    static int boss_max_gold = 300;

    static Object[] CreateMonster() {
       String[] names = {
        "verndunoz",
        "Grimfang",
        "Zarvok",
        "Morthul",
        "Skelrith",
        "Dreadmaw",
        "Vexthorn",
        "Kragnor",
        "Blightclaw",
        "Tharzul",
        "Nyxeron",
        "Frostmaw",
        "Gorvath",
        "Hexdrake",
        "Ravengore",
        "Zulkreth",
        "Voidfang",
        "Morgrin",
        "Ashripper",
        "Terrorix"
     };
  
         return new Object[] {
          /* TEMPLATE
            names
            HP
            attack damage
            defense
            speed
            EXP rewards
            gold rewards
           */

            names[(int)(Math.random() * names.length)],
            (int)(Math.random() * (monster_max_health - monster_min_health + 1)) + monster_min_health,
            (int)(Math.random() * (monster_max_attack - monster_min_attack + 1)) + monster_min_attack,
            (int)(Math.random() * (monster_max_defense - monster_min_defense + 1)) + monster_min_defense,
            (int)(Math.random() * (monster_max_speed - monster_min_speed + 1)) + monster_min_speed,
            (int)(Math.random() * (monster_max_exp - monster_min_exp + 1)) + monster_min_exp,
            (int)(Math.random() * (monster_max_gold - monster_min_gold + 1)) + monster_min_gold
       };
    }

    static Object[] CreateFloorBoss() {
        String[] names = {
            "Malachar the Undying",
            "Vorgath Soulreaper",
            "Zythera the Cursed",
            "Dreadlord Karnox",
            "Shadowfang Rex",
            "Gorvanus the Blighted",
            "Xelthar Doomcaller",
            "Nightshade Empress",
            "Thornback Tyrant",
            "Abyssal Warlord"
        };

        return new Object[] {
            names[(int)(Math.random() * names.length)],
            (int)(Math.random() * (boss_max_health - boss_min_health + 1)) + boss_min_health,
            (int)(Math.random() * (boss_max_attack - boss_min_attack + 1)) + boss_min_attack,
            (int)(Math.random() * (boss_max_defense - boss_min_defense + 1)) + boss_min_defense,
            (int)(Math.random() * (boss_max_speed - boss_min_speed + 1)) + boss_min_speed,
            (int)(Math.random() * (boss_max_exp - boss_min_exp + 1)) + boss_min_exp,
            (int)(Math.random() * (boss_max_gold - boss_min_gold + 1)) + boss_min_gold
        };
    }

    /************************************************************/
    /*                                                          */
    /*                       BOSSES                             */
    /*                                                          */
    /************************************************************/

    static Object[][] DungeonBossPool = {
        {
            "Malachar",    "The Undying",
            850.0, 75.0, 45.0, 8.0,
            500, 250,
            0.50, false, "Soul Drain",
            true
        },
        {
            "Vorgath",     "Soulreaper",
            720.0, 90.0, 35.0, 12.0,
            480, 230,
            0.45, false, "Reap",
            true
        },
        {
            "Zythera",     "The Cursed",
            680.0, 85.0, 30.0, 14.0,
            460, 210,
            0.40, false, "Hex Burst",
            true
        },
        {
            "Dreadlord Karnox", "Destroyer of Realms",
            950.0, 100.0, 50.0, 7.0,
            550, 300,
            0.55, false, "Void Slam",
            true
        },
        {
            "Xelthar",     "Doomcaller",
            760.0, 80.0, 40.0, 11.0,
            510, 270,
            0.50, false, "Death Knell",
            true
        },
        {
            "Nightshade Empress", "Queen of Shadows",
            700.0, 95.0, 25.0, 18.0,
            490, 260,
            0.35, false, "Umbral Strike",
            true
        }
    };

    static Object[] CreateDungeonBoss(int dungeonDepth) {
        int index = (int)(Math.random() * DungeonBossPool.length);
        Object[] template = DungeonBossPool[index];
    
        double scale = 1.0 + (dungeonDepth - 1) * 0.08;
        double atkScale = 1.0 + (dungeonDepth - 1) * 0.05;
    
        return new Object[] {
            template[0],                                          // [0]  name
            template[1],                                          // [1]  title
            Math.round((double) template[2] * scale),            // [2]  HP (scaled)
            Math.round((double) template[3] * atkScale),         // [3]  attack (scaled)
            template[4],                                          // [4]  defense
            template[5],                                          // [5]  speed
            template[6],                                          // [6]  EXP reward
            (int)((int) template[7] * scale),                    // [7]  gold reward (scaled)
            template[8],                                          // [8]  phase2HP threshold
            false,                                                // [9]  isInPhase2  (reset each run)
            template[10],                                         // [10] special move name
            true                                                  // [11] isBoss flag
        };
    }

    /************************************************************/
    /*                                                          */
    /*                         COMBAT                           */
    /*                                                          */
    /************************************************************/

    static int max_attack_turns = 30;

    static int GetBossPoolIndex(Object[] boss) {
        for (int i = 0; i < DungeonBossPool.length; i++) {
            if (DungeonBossPool[i][0].equals(boss[0])) return i;
        }
        return 0;
    }

    static String MonsterDecideAttack(Object[] Monster) {
        String[] decisions = {"basic", "heavy", "defend"};
        return decisions[(int)(Math.random() * decisions.length)];
    }

    static String InitiateRegularBattle(Object[] Entity1, Object[] Entity2) {
        System.out.println("[SYSTEM]: You engaged in a battle with " + Entity2[0]);
        PrintDivider(false);

        String battle_result = null;
        Object[] turns = CalculateTurns(Entity1, Entity2);
        
        int current_turn = 0;
        boolean player_defending = false;

       do {

        if (turns[current_turn].equals(Entity1[0])) {
            System.out.println("YOUR TURN");
            PrintDivider(false);

            System.out.println("YOUR HP: " + Entity1[2] + "          " + "Monster HP: " + Entity2[1]);
            PrintDivider(false);
            System.out.println("[A] Basic Attack");
            System.out.println("[B] Skill Attack");
            System.out.println("[C] Defend");
            System.out.println("[D] Use Potion");
            System.out.println("[E] Escape");
            System.out.print("[System]: How would you like to attack?: " );
            player_string_reply = Scanner.nextLine();
            PrintDivider(false);

            player_defending = false;

            switch (player_string_reply.toLowerCase()) {

                 case "a":
                    int    player_atk   = (int)((Number) Entity1[5]).doubleValue();
                    int    monster_def  = (int) Entity2[3];
                    double critChance   = ((Number) Entity1[7]).doubleValue();
                    double critMult     = ((Number) Entity1[8]).doubleValue(); 
                
                    boolean isCrit      = Math.random() * 100 < critChance;
                    int     raw_damage  = Math.max(player_atk - monster_def, 1);
                    int     basic_damage = isCrit
                                            ? (int)(raw_damage * critMult)
                                            : raw_damage;
                
                    Entity2[1] = (int) Entity2[1] - basic_damage;
                
                    if (isCrit) {
                        System.out.println("[SYSTEM]: ★ CRITICAL HIT! You attack " + Entity2[0] + " for " + basic_damage + " damage!");
                    } else {
                        System.out.println("[SYSTEM]: You attack " + Entity2[0] + " for " + basic_damage + " damage!");
                    }
                    System.out.println("[SYSTEM]: " + Entity2[0] + " HP is now " + Entity2[1]);
                
                    // keep analytics in sync (was missing in the original)
                    Object[] Report = (Object[]) PlayerInformation[14];
                    if (basic_damage > (double) Report[12]) Report[12] = (double) basic_damage;
                    Report[9] = (double) Report[9] + basic_damage;
                
                    PrintDivider(false);
                    break;
                case "c":
                    player_defending = true;
                    System.out.println("\n[SYSTEM]: You take a defensive stance! Incoming damage will be halved.");
                    PrintDivider(false);
                    break;

                case "e":
                    System.out.println("[SYSTEM]: You attempt to escape...");
                    double escape_chance = 0.5;
                    int player_spd  = (int)((Number) Entity1[4]).doubleValue();
                    int monster_spd = (int) Entity2[4];
                    if (player_spd > monster_spd) escape_chance = 0.75;
                    if (player_spd < monster_spd) escape_chance = 0.25;

                    if (Math.random() < escape_chance) {
                        System.out.println("[SYSTEM]: You successfully escaped!");
                        return "escaped";
                    } else {
                        System.out.println("[SYSTEM]: Escape failed! The monster blocks your way.");
                        PrintDivider(false);
                    }
                    break;

                default:
                    System.out.println("[SYSTEM]: Invalid choice. You hesitate and lose your turn.");
                    PrintDivider(false);
                    break;
            }

             if (((Number) Entity2[1]).doubleValue() <= 0) {
                battle_result = (String) Entity1[0];
             }
        } else {
            String monster_decision = MonsterDecideAttack(Entity2);

           switch (monster_decision) {
                case "basic":
                    int raw_basic = (int) Entity2[2] - (int)((Number) Entity1[6]).doubleValue();
                    int basic_dmg = Math.max(raw_basic, 1);
                    if (player_defending) basic_dmg = Math.max(basic_dmg / 2, 1);
                    System.out.println("[SYSTEM]: " + Entity2[0] + " attacks for " + basic_dmg + " damage!");
                    PrintDivider(false);
                    UpdatePlayerStatus(2, -basic_dmg);
                    break;

                case "heavy":
                    int raw_heavy = ((int) Entity2[2] * 2) - (int)((Number) Entity1[6]).doubleValue();
                    int heavy_dmg = Math.max(raw_heavy, 1);
                    if (player_defending) heavy_dmg = Math.max(heavy_dmg / 2, 1);
                    System.out.println("[SYSTEM]: " + Entity2[0] + " uses a heavy attack for " + heavy_dmg + " damage!");
                    PrintDivider(false);
                    UpdatePlayerStatus(2, -heavy_dmg);
                    break;

                case "defend":
                    System.out.println("[SYSTEM]: " + Entity2[0] + " takes a defensive stance!");
                    PrintDivider(false);
                    break;
            }

            if (((Number) Entity1[2]).doubleValue() <= 0) {
                battle_result = (String) Entity2[0];
            }
        }

        current_turn = (current_turn + 1) % turns.length;
        PrintDivider(false);
        System.out.println("\nTURN " + current_turn);
        PrintDivider(true);

       } while (battle_result == null);

       return battle_result;
    }
    
    static String InitiateBossBattle(Object[] Player, Object[] Boss) {
        PrintDivider(false);
        System.out.println("⚠  DUNGEON BOSS ENCOUNTERED");
        System.out.println("   " + Boss[0] + ", " + Boss[1]);
        System.out.println("   HP: " + Boss[2]);
        PrintDivider(false);
    
        Object[] turns = CalculateTurns(Player, Boss);
        int current_turn = 0;
        boolean player_defending = false;
        String battle_result = null;
    
        Object[] Report = (Object[]) PlayerInformation[14];
    
        do {
            double bossCurrentHP    = ((Number) Boss[2]).doubleValue();
            double bossMaxHP        = ((Number) DungeonBossPool[GetBossPoolIndex(Boss)][2]).doubleValue();
            double phase2Threshold  = (double) Boss[8];
            boolean alreadyPhase2   = (boolean) Boss[9];
    
            if (!alreadyPhase2 && bossCurrentHP / bossMaxHP <= phase2Threshold) {
                Boss[9] = true;
                PrintDivider(false);
                System.out.println("★  PHASE 2 — " + Boss[0] + " ENRAGES!");
                System.out.println("   Attack power surges. Brace yourself.");
                PrintDivider(false);
                Boss[3] = (int)(((Number) Boss[3]).doubleValue() * 1.30);
            }
    
            if (turns[current_turn].equals(Player[0])) {
                System.out.println("YOUR TURN (BOSS BATTLE)");
                PrintDivider(false);
                System.out.println("Your HP:   " + Player[2]);
                System.out.println("Boss HP:   " + Boss[2]);
                if ((boolean) Boss[9]) System.out.println("[!] ENRAGED PHASE"); PrintDivider(false);
                System.out.println("\n[A] Basic Attack");
                System.out.println("[B] Skill Attack");
                System.out.println("[C] Defend");
                System.out.println("[D] Use Potion");
                System.out.println("[E] Escape");
                System.out.print("[SYSTEM]: Choose your action: ");
                PrintDivider(false);
                player_string_reply = Scanner.nextLine();
    
                player_defending = false;
    
                switch (player_string_reply.toLowerCase()) {
    
                    case "a": {
                        int    player_atk = (int)((Number) Player[5]).doubleValue();
                        int    boss_def   = (int)((Number) Boss[4]).doubleValue();
                        double critChance = ((Number) Player[7]).doubleValue(); 
                        double critMult   = ((Number) Player[8]).doubleValue(); 
                    
                        boolean isCrit    = Math.random() * 100 < critChance;
                        int     raw       = Math.max(player_atk - boss_def, 1);
                        int     dmg       = isCrit
                                                ? (int)(raw * critMult)
                                                : raw;
                    
                        Boss[2] = (int)((Number) Boss[2]).doubleValue() - dmg;
                    
                        if (isCrit) {
                            System.out.println("[SYSTEM]: ★ CRITICAL HIT! You strike " + Boss[0] + " for " + dmg + " damage!");
                        } else {
                            System.out.println("[SYSTEM]: You strike " + Boss[0] + " for " + dmg + " damage!");
                        }
                        System.out.println("[SYSTEM]: Boss HP → " + Boss[2]);
                    
                        if (dmg > (double) Report[12]) Report[12] = (double) dmg;
                        Report[9] = (double) Report[9] + dmg;
                    
                        PrintDivider(false);
                        break;
                    }
    
                    case "c":
                        player_defending = true;
                        System.out.println("[SYSTEM]: You brace yourself. Incoming damage halved.");
                        PrintDivider(false);
                        break;
    
                    case "e":
                        System.out.println("[SYSTEM]: You cannot flee from a Dungeon Boss!");
                        PrintDivider(false);
                        break;
    
                    default:
                        System.out.println("[SYSTEM]: Invalid. You hesitate.");
                        PrintDivider(false);
                        break;
                }
    
                if (((Number) Boss[2]).doubleValue() <= 0) {
                    battle_result = "victory";
                }

            } else {
                boolean isPhase2 = (boolean) Boss[9];

                boolean useSpecial = isPhase2 && Math.random() < 0.30;
    
                if (useSpecial) {
                    int special_dmg = (int)(((Number) Boss[3]).doubleValue() * 1.5);
                    if (player_defending) special_dmg = Math.max(special_dmg / 2, 1);
                    System.out.println("\n[!] " + Boss[0] + " uses " + Boss[10] + "!");
                    System.out.println("    This attack pierces your defenses for " + special_dmg + " damage!");
                    PrintDivider(false);
                    UpdatePlayerStatus(2, -special_dmg);
    
                } else {
                    String decision = MonsterDecideAttack(Boss);
                    switch (decision) {
                        case "basic": {
                            int raw = (int)((Number) Boss[3]).doubleValue()
                                    - (int)((Number) Player[6]).doubleValue();
                            int dmg = Math.max(raw, 1);
                            if (player_defending) dmg = Math.max(dmg / 2, 1);
                            System.out.println("[SYSTEM]: " + Boss[0] + " attacks for " + dmg + " damage!");
                            PrintDivider(false);
                            UpdatePlayerStatus(2, -dmg);
                            break;
                        }
                        case "heavy": {
                            int raw = (int)(((Number) Boss[3]).doubleValue() * 2)
                                    - (int)((Number) Player[6]).doubleValue();
                            int dmg = Math.max(raw, 1);
                            if (player_defending) dmg = Math.max(dmg / 2, 1);
                            System.out.println("[SYSTEM]: " + Boss[0] + " slams you for " + dmg + " damage!");
                            PrintDivider(false);
                            UpdatePlayerStatus(2, -dmg);
                            break;
                        }
                        case "defend":
                            System.out.println("\n[SYSTEM]: " + Boss[0] + " readies itself...");
                            PrintDivider(false);
                            break;
                    }
                }
    
                if (((Number) Player[2]).doubleValue() <= 0) {
                    battle_result = "defeated";
                }
            }
    
            current_turn = (current_turn + 1) % turns.length;
    
        } while (battle_result == null);
 
        if (battle_result.equals("victory")) {
            PrintDivider(false);
            System.out.println("★  YOU DEFEATED " + Boss[0].toString().toUpperCase() + "!");
            System.out.println("   EXP gained : " + Boss[6]);
            System.out.println("   Gold earned: " + Boss[7]);
            PrintDivider(false);
    
            Report[7] = (int) Report[7] + 1;   // Bosses Defeated counter
            UpdatePlayerStatus(9,  (double)(int) Boss[6]);
            UpdatePlayerStatus(11, (double)(int) Boss[7]);
        }
    
        return battle_result;
    }
    
    static Object[] CalculateTurns(Object[] entity1, Object[] entity2) {
        Object[] attack_turns = new Object[max_attack_turns];

        if (((Number) entity1[4]).intValue() > ((Number) entity2[4]).intValue()) {
            attack_turns[0] = entity1[0];
        } else if (((Number) entity2[4]).intValue() > ((Number) entity1[4]).intValue()) {
            attack_turns[0] = entity2[0];
        } else {
            attack_turns[0] = (Math.random() < 0.5) ? entity1[0] : entity2[0];
        }

        for (int i = 1; i < attack_turns.length; i++) {
            if (i % 2 == 0) {
                attack_turns[i] = attack_turns[0];
            } else {
                if (attack_turns[0].equals(entity1[0])) {
                    attack_turns[i] = entity2[0];
                } else {
                    attack_turns[i] = entity1[0];
                }
            }
        }

        return attack_turns;
    }


    /************************************************************/
    /*                                                          */
    /*                        WEAPONS                           */
    /*                                                          */
    /************************************************************/

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

    static Object[] DeriveWeaponAffinities(double[] normalized) {
       
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
        int[][] ForbiddenWeaponsMapping = {
            { 3, 4, 2, 1 },     // 0  Blade 
            { 10, 6, 8, 7 },    // 1  Staff
            { 10, 11, 8, 7 },   // 2  Grimoire
            { 10, 7, 6 },       // 3  Totem
            { 7, 10, 11 },      // 4  Orb
            { 8, 7 },           // 5  Bow
            { 8, 7, 3 },        // 6  Crossbow 
            { 4, 3, 2, 1, 5 },        // 7  Fist
            { 9, 4, 2, 1, 5, 6 },        // 8  Shield
            { 8, 11, 10 },      // 9  Dagger
            { 8, 3, 4 },        // 10 Scythe
            { 2, 4, 3 },        // 11 Spear
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

        if (ScoredPrefixes.isEmpty()) {
            for (Object[] prefix : WeaponPrefixPool) {
                double weight = (double) prefix[2];
                if (weight < tierMin || weight > tierMax) continue;
                ScoredPrefixes.add(prefix);
            }
        }

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
        rawPrice = Math.max(15.0, rawPrice);

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

    static void ApplyWeaponStats(Object[] weaponEntry, double sign) {
        Object[] rolledData   = (Object[]) weaponEntry[0];
        Object[] prefix       = (Object[]) rolledData[0];
        Object[] suffix       = (Object[]) rolledData[2];
        double   rolledAttack = (double)   rolledData[3];

        double[] prefixMods = (double[]) prefix[1];
        double[] suffixMods = (double[]) suffix[1];

        PlayerInformation[5] = (double) PlayerInformation[5] + sign * rolledAttack;

        for (int i = 0; i < prefixMods.length; i++) {
            int playerIndex = STAT_MOD_TO_PLAYER_INDEX[i];
            PlayerInformation[playerIndex] = (double) PlayerInformation[playerIndex] + sign * prefixMods[i];
            PlayerInformation[playerIndex] = (double) PlayerInformation[playerIndex] + sign * suffixMods[i];
        }
    }

    /************************************************************/
    /*                                                          */
    /*                          ARMOR                           */
    /*                                                          */
    /************************************************************/

    static int minimum_amount_of_armors = 5;
    static int maximum_amount_of_armors = 10;
    static Object[] CreatedArmors = {};

    static int[] ArmorStatReqMap = { 3, 5, 2, 4, 1, 0 };

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

        double[][] ScoredBases = new double[scored_bases.size()][2];
        for (int i = 0; i < scored_bases.size(); i++) {
            int statBias = (int) scored_bases.get(i)[5];
            double score = 1.0 * (1 + classMods[statBias]);

            ScoredBases[i][0] = i;
            ScoredBases[i][1] = score;
        }

        return new Object[] { ScoredPrefixes, ScoredBases, ScoredSuffixes };
    }

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

        double basePrice   = rolledDefense * 4.0;
        double rarityScore = ((1.0 - prefixWeight) + (1.0 - suffixWeight)) / 2.0;
        double rarityMult  = 1.0 + (rarityScore * 0.45);

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

        for (int tier = 1; tier <= 5; tier++) {
            Object[] RolledData = ComposeArmors((int[]) BaseData[2], (double[]) BaseData[3], tier);
            int Price = PriceArmor(RolledData, PrimaryAffinity, SecondaryAffinity);
            CreatedArmors[tier - 1] = new Object[] { RolledData, Price };
        }

        for (int i = 5; i < armors_to_generate; i++) {
            int randomTier = (int) (Math.random() * 5) + 1;
            Object[] RolledData = ComposeArmors((int[]) BaseData[2], (double[]) BaseData[3], randomTier);
            int Price = PriceArmor(RolledData, PrimaryAffinity, SecondaryAffinity);
            CreatedArmors[i] = new Object[] { RolledData, Price };
        }
    }

    /************************************************************/
    /*                                                          */
    /*                           SHOP                           */
    /*                                                          */
    /************************************************************/

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
 
            System.out.printf("%n[%d] %s%n", i + 1, weaponName);
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
            double PlayerGold = ((Number) PlayerInformation[11]).doubleValue();

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

                    Object[] CurrentInventory = (Object[]) PlayerInformation[15];
                    Object[] NewInventory = new Object[CurrentInventory.length + 1];
                    System.arraycopy(CurrentInventory, 0, NewInventory, 0, CurrentInventory.length);
                    NewInventory[CurrentInventory.length] = PlayerEquippedWeapon;
                    PlayerInformation[15] = NewInventory;
                    PlayerInformation[12] = SelectedWeapon;
                    ApplyWeaponStats(SelectedWeapon, +1.0);
                }

                PlayerInformation[11] = (int)(PlayerGold - WeaponCost);
                break;
            }

        } while (!player_string_reply.equalsIgnoreCase("exit"));
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

            System.out.printf("%n[%d] %s%n", i + 1, armorName);
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
            double PlayerGold = ((Number) PlayerInformation[11]).doubleValue();

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
                PlayerInformation[11] = (int)(PlayerGold - ArmorCost);
                running = false; 
            }
        }
    }

    static void OpenShop(){
        boolean invalid_reply = false;
        boolean is_shopping = true;

        while (is_shopping) {
            PrintDivider(false);
            System.out.println("SHOP | " + "POUCH: " + PlayerInformation[11] + " Gold");
            PrintDivider(false);
            System.out.println("1 - ARMOR ");
            System.out.println("2 - POTION ");
            System.out.println("3 - WEAPON ");
            System.out.println("4 - Exit Shop ");
            System.out.print("What do you want to buy? : ");
            player_numerical_reply = Integer.parseInt(Scanner.nextLine());

            while (invalid_reply) {
                System.out.println("\n[SYSTEM]: Invalid reply. Please choose between 1-3");
                System.out.println("1 - ARMOR ");
                System.out.println("2 - POTION ");
                System.out.println("3 - WEAPON ");
                System.out.println("4 - Exit Shop ");
                player_numerical_reply = Integer.parseInt(Scanner.nextLine());
            }

            switch (player_numerical_reply) {
                case 1:
                    OpenArmorsShop();
                    break;

                case 2:
                    PrintDivider(false);
                    System.out.println("\nThe Potion Shop isn't available right now.");
                    break;

                case 3:
                    OpenWeaponsShop();
                    break;

                case 4:
                    PrintDivider(false);
                    System.out.println("\nYou've exited the shop");
                    is_shopping = false;
                    break;

                default:
                    invalid_reply = true;
                    break;
            }
        }

        if (is_shopping == false) {
            PrintDivider(false);
            System.out.println("Exiting shop...");
            System.out.println("You've exited the shop");
        }
    }

    /************************************************************/
    /*                                                          */
    /*                INITIALIZATIONS                           */
    /*                                                          */
    /************************************************************/

    static void PrintDivider(boolean create_newline){
        if (create_newline) {
            System.out.println("\n──────────────────────────────────────────────────");
        } else {
            System.out.println("──────────────────────────────────────────────────");
        }
    }
    public static void main(String[] args) {
        System.out.println("\n══════════════════════════════════════════════════");
        System.out.println("\n                 RPG MINI GAME                   ");
        System.out.println("\n══════════════════════════════════════════════════");
        System.out.println("For fun only, enjoy");
        System.out.println("Created: 05/10/26 -> 05/19/26");
        PrintDivider(false);

        System.out.print("\n[SYSTEM]: What's your character name? | Type Here: ");
        PlayerInformation[0] = Scanner.nextLine();

        int class_pointer = PlayerChooseClass();
        
        GenerateWeaponsForClass(classes[class_pointer]);
        GenerateArmorForClass(classes[class_pointer]);

        PrintDivider(false);
        System.out.print("[SYSTEM]: How much do gold you want to start?: ");
        PlayerInformation[11] = Integer.parseInt(Scanner.nextLine());
        PrintDivider(false);

        System.out.println("\n[SYSTEM]: Press ENTER to start");
        System.out.println(" ");
        player_string_reply = Scanner.nextLine();

        while (!player_string_reply.isBlank()) {
            PrintDivider(false);
            System.out.print("\n[SYSTEM]: Press ENTER to start");
            player_string_reply = Scanner.nextLine();
        };

        for (int dungeon_index = 0; dungeon_index < 3; dungeon_index++) {
            Dungeons[dungeon_index] = CreateDungeon();
        }
        
        while (true) {
            PrintDivider(true);
            System.out.println("[1] - Enter Town");
            System.out.println("[2] - Enter Dungeon");
            System.out.println("[3] - View Profile");
            System.out.println("[4] - Exit Game");
            System.out.print("Where do you want to go? | ");
            int main_menu_choice = Integer.parseInt(Scanner.nextLine());

            switch (main_menu_choice) {
                case 1:
                    PrintDivider(false);
                    System.out.println("TOWN");
                    PrintDivider(false);
                    OpenShop();
                    break;

                case 2:
                    Object[] ChosenDungeon = null;

                    PrintDivider(false);
                    System.out.println("DUNGEONS AVAILABLE");
                    PrintDivider(false);
                    for (int dungeon = 0; dungeon < Dungeons.length; dungeon++) {
                        System.out.println((dungeon + 1) + " - " + (String) ((Object[]) Dungeons[dungeon])[0]);
                    }

                    do {
                        PrintDivider(false);
                        System.out.print("[SYSTEM]: Which dungeon would you like to enter? Type 'N' to go back: ");
                        player_string_reply = Scanner.nextLine();

                        if (player_string_reply.equalsIgnoreCase("N")) break;

                        player_numerical_reply = Integer.parseInt(player_string_reply);

                        if (player_numerical_reply >= 1 && player_numerical_reply <= Dungeons.length) {
                            ChosenDungeon = (Object[]) Dungeons[player_numerical_reply - 1];
                        } else {
                            System.out.println("[SYSTEM]: Dungeon does not exist.");
                        }

                    } while (ChosenDungeon == null);

                    if (ChosenDungeon != null) {
                        PrintDivider(false);
                        System.out.print("[SYSTEM]: Would you like to enter " + ChosenDungeon[0] + "? [Y/N]: ");
                        player_string_reply = Scanner.nextLine();

                        if (player_string_reply.equalsIgnoreCase("Y")) {
                            AdmitPlayerToDungeon(ChosenDungeon);
                        }
                    }

                    break;

                case 3: ViewPlayerProfile(); break;
                case 4: break;
                default: System.out.println("[SYSTEM]: Invalid choice. Please enter 1-4."); break;
            }

            if (main_menu_choice == 4 || game_over == true) break;
        }

        GenerateGameReport();

        System.out.println("══════════════════════════════════════════════════");
        System.out.println("\n                 RPG MINI GAME                   ");
        System.out.println("\n══════════════════════════════════════════════════");

        System.out.println("\n[SYSTEM]: Thank you for playing!");
        Scanner.close();
    }
}
